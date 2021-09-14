package com.icia.wapoo.controller;

import com.icia.wapoo.jwt.service.JwtService;
import com.icia.wapoo.model.AdminEvent;
import com.icia.wapoo.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    @Autowired
    private final AdminService adminService;
    @Autowired
    private final JwtService jwtService;

    @PostMapping("/getStoreList")
    public ResponseEntity getStoreList(@RequestBody Map<String, Object> data) {
        int listPerPage = ((Integer) data.get("listPerPage")).intValue();
        int currentPage = ((Integer) data.get("currentPage")).intValue();
        String option = (String) data.get("statusOption");
        if(listPerPage <= 0 || currentPage <=0){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
 
        // 가게페이지를 가져옵니다.
       

        List<Map<String, Object>> result = adminService.getStoreList(listPerPage, currentPage, option);
        return new ResponseEntity(result, HttpStatus.OK);
    }
    @GetMapping("/getStoreListCount")
    public ResponseEntity getStoreListCount(@RequestParam("option") String option) {
        int result = adminService.getStoreListCount(option);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("/updateStoreStatus")
    public ResponseEntity updateStoreStatus(@RequestBody Map<String, Object> data) {
        int storeId = ((Integer) data.get("storeId")).intValue();
        String status = (String) data.get("status");
        if(status == null || storeId < 1){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        adminService.updateStoreStatus(storeId, status);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @PostMapping("/adminEvent")
    public ResponseEntity adminEvent(@RequestBody Map<String, Object> data)
    {
    	int listPerPage = ((Integer) data.get("listPerPage")).intValue();
        int currentPage = ((Integer) data.get("currentPage")).intValue();
        String option = (String) data.get("statusOption");
        
        if(currentPage != 1)
        {
        	
        	currentPage = 1 + listPerPage * (currentPage-1);
        }
        
        
        List<AdminEvent> list = adminService.adminEvent(listPerPage, currentPage, option);
        
      
        
       return new ResponseEntity(list, HttpStatus.OK);
    }
    
    //합계
    @GetMapping("/adminEventcount")
    public ResponseEntity adminEventcount(@RequestParam("option") String option)
    {
    	
    	int count = adminService.adminEventcount(option);
    	
    	
    	
    	return new ResponseEntity(count, HttpStatus.OK);
    }
    
    @PostMapping("/updateEventStatus")
    public ResponseEntity updateEventStatus(@RequestBody Map<String, Object> data)
    {
    	int eventId = ((Integer) data.get("eventId")).intValue();
        String status = (String) data.get("status");
        if(status == null || eventId < 1){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        
        if(adminService.updateEventStatus(eventId, status) > 0)
        {
        	
        	return new ResponseEntity("ok",HttpStatus.OK);
        }
        
    	return new ResponseEntity("no",HttpStatus.OK);
    }
    @GetMapping("/getAllArticles")
    public ResponseEntity getAllArticles(@RequestParam Integer memberId) {

        List<Map<String, Object>> articleList = adminService.getAllArticlesByMemberId(memberId);

        return new ResponseEntity(articleList, HttpStatus.OK);
    }
    @GetMapping("/getAllReviews")
    public ResponseEntity getAllReviews(@RequestParam Integer memberId) {

        List<Map<String, Object>> reviewList = adminService.getAllreviewByMemberId(memberId);

        return new ResponseEntity(reviewList, HttpStatus.OK);
    }
}
