package com.icia.wapoo.controller;

import com.icia.wapoo.jwt.service.JwtService;
import com.icia.wapoo.model.Food;
import com.icia.wapoo.model.ImageFile;
import com.icia.wapoo.model.Store;
import com.icia.wapoo.service.AkinatorService;
import com.icia.wapoo.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/store")
@Slf4j
public class StoreController {
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final StoreService storeService;
    @Autowired
    private final AkinatorService akinatorService;

    private int getMemberIdByRequest(HttpServletRequest request) {

        String token = jwtService.resolveToken(request);
        Map<String, Object> claims = jwtService.getUserInfo(token);
        return ((Integer) claims.get("memberId")).intValue();
    }

    @PostMapping("/addstore")
    public ResponseEntity addStore(Store store
            , @RequestPart(value = "fileList", required = false) List<MultipartFile> files, HttpServletRequest request){
        store.setOwner_id(getMemberIdByRequest(request));
        int result = storeService.registerStore(store, files);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping(value = "/modifyStore")
    public ResponseEntity modifyStore(@RequestParam("delFileIdList") List<String> delFileIdList,
            @RequestParam(value = "fileList", required = false) List<MultipartFile> fileList, MultipartHttpServletRequest request,
                                      @RequestParam(value = "description", required = false) String description) {

        if(delFileIdList.size() > 0) {
            // store_file??? status??? ?????? ??? ?????? ?????????, store_id ??? 1??? ?????????. 1???????????? ????????????. ????????????.
            delFileIdList.forEach((fildId)->{
                storeService.removeFile(Integer.valueOf(fildId));
            });
        }


        // ?????? ?????? ??????.
        Store store = storeService.getStoreById(getMemberIdByRequest(request));
        Integer storeId = store.getStoreId();
        int result = 0;
        if(fileList !=null && fileList.size() > 0 ) {
            result = storeService.uploadStoreFile(storeId, fileList);
        }
        if(description != null) {
            storeService.modifyStore(storeId, description);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/findStore")
    public ResponseEntity findStore(HttpServletRequest request) {
        try{
            Integer memberId = null;
            memberId = getMemberIdByRequest(request);
            Store store = null;
            if( memberId != null ){
               
                store = storeService.getStoreById(memberId);
            }
            return new ResponseEntity(store, HttpStatus.OK);
        } catch (Exception e){
            System.out.println("findStore Strote Controller");
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/getAkinatorList")
    public ResponseEntity getAkinatorList() {
        List<Map<String,Object>> akinatorList = akinatorService.getAkinatorList();
        return new ResponseEntity(akinatorList, HttpStatus.OK);
    }

    @PostMapping("/addFood")
    public ResponseEntity addFood(
            Food food
            ,@RequestPart(value = "file", required = false) MultipartFile file, HttpServletRequest request
    ) {
        try{
            Integer memberId = null;
            memberId = getMemberIdByRequest(request);
            Store store = null;
            if( memberId != null ){
                store = storeService.getStoreById(memberId);

                if(store == null) {
                    //????????? ???????
                    System.out.println("????????? ????????????.");
                    return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                if(store.getStatus().equals("Y")){
                    // ????????? ??????????????? ?????? ???,
                    food.setStore_id(store.getStoreId());
           
                    int foodId = storeService.addFood(food, file);
                    if(foodId > 0 ){
           
                        return new ResponseEntity(foodId, HttpStatus.OK);
                    }
                    return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                // ????????? ??????????????? ?????????..
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
            // ?????????????????? ??????????
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            System.out.println("addFood/ Store controller");
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/setAkinator")
    public ResponseEntity getStoreList(@RequestBody Map<String, Object> data) {
        Map<String, Object> akinator = (Map<String, Object>) data.get("akinator");
        int foodId = ((Integer) data.get("foodId")).intValue();
        int result = akinatorService.addAkinator(akinator, foodId);
        if(result == 1){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getStoreFiles")
    public ResponseEntity getStoreFiles(@RequestParam("storeId") Integer storeId) {
        if(storeId != null) {
            List<ImageFile> fileList = storeService.getStoreFiles(storeId.intValue());
            return new ResponseEntity(fileList, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/getFoodList")
    public ResponseEntity getFoodList(@RequestBody Map<String, Object> data) {
        int listPerPage = ((Integer) data.get("listPerPage")).intValue();
        int currentPage = ((Integer) data.get("currentPage")).intValue();
        int storeId = ((Integer) data.get("storeId")).intValue();
        String option = (String) data.get("statusOption");
        if(listPerPage <= 0 || currentPage <=0){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
      
        // ?????????????????? ???????????????.

        List<Map<String, Object>> result = storeService.getFoodList(listPerPage, currentPage, option, storeId);
        return new ResponseEntity(result, HttpStatus.OK);
    }
    @GetMapping("/getFoodListCount")
    public ResponseEntity getFoodListCount(@RequestParam("option") String option, @RequestParam("storeId") Integer storeId) {

        int result = storeService.getFoodListCount(option, storeId);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("/getStoreListByLocation")
    public ResponseEntity getStoreListByLocation(@RequestBody Map<String, Object> data) {
        Double latitude = (Double) data.get("lat");
        Double longitude = (Double) data.get("lon");
        int quantity = 7; // ???????????? 5?????? ????????????.
        if(data.containsKey("quantity")){
            // ????????? ?????????????????????,
            quantity = ((Integer) data.get("quantity")).intValue();
        }
        String options = "ALL";
        if(data.containsKey("options")) {
            // ????????? ?????????????????????,
            options = (String) data.get("options");
        }
        float radius = 30;
        if(data.containsKey("radius")) {
            radius = (float) data.get("radius");
        }
        int loadFrom = 0;
        if(data.containsKey("loadFrom")){
            // ????????? ?????????????????????,
            loadFrom = ((Integer) data.get("loadFrom")).intValue();
        }
        List<Map<String, Object>> list = storeService.getNearStoresList(latitude, longitude, radius, quantity, options, loadFrom);
        if(list.size() == 0) {
            return new ResponseEntity(list, HttpStatus.OK);
        }
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/getStoreInfos")
    public ResponseEntity getStoreInfos(@RequestParam("storeId") Integer storeId) {

        Store store = storeService.getStoreInfo(storeId);
        List<ImageFile> fileList = storeService.getStoreFiles(storeId);
        List<Map<String, Object>> foodList = storeService.getAllFood(storeId);
        Map<String, Object> map = new HashMap<>();
        map.put("storeInfo", store);
        map.put("foodList", foodList);
        map.put("fileList", fileList);
        return new ResponseEntity(map, HttpStatus.OK);
    }
    @PostMapping("/updateFoodInfo")
    public ResponseEntity modifyFoodInfo(@RequestBody Map<String, Object> formData) {
        int result = storeService.modifyFoodInfo(formData);
        return new ResponseEntity(result , HttpStatus.OK);
    }
}
