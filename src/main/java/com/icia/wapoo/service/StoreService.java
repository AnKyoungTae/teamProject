package com.icia.wapoo.service;

import com.icia.wapoo.S3.S3Service;
import com.icia.wapoo.dao.StoreDao;
import com.icia.wapoo.model.ImageFile;
import com.icia.wapoo.model.Food;
import com.icia.wapoo.model.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class StoreService {
    @Autowired
    private final S3Service s3Service;
    @Autowired
    private final StoreDao storeDao;

    public List<Map<String, Object>> getFoodListByFoodIdList(List<Integer> foodIdList) {
        List<Map<String, Object>> foodList = new ArrayList<>();
        for(int foodId : foodIdList) {
            Map<String, Object> food = storeDao.selectFoodByFoodId(foodId);
            if(food != null) {
                // 수량추가
                food.put("quantity", 1);
                foodList.add(food);
            }
        }
        return foodList;
    }

    @Transactional
    public int registerStore(Store store, List<MultipartFile> files){
        int result = storeDao.insertStore(store);
        System.out.println("StoreService.registerStore : 적용된 게시물수 => " + result);
        System.out.println("StoreService.registerStore : 새로생긴 가게 storeId => " + store.getStoreId());
        if ( result > 0) {

            for( MultipartFile file : files) {

                String fileURL = null;
                try {
                    fileURL = s3Service.upload(file, "store_"+store.getStoreId());
                } catch (IOException e) {
                    throw new RuntimeException("S3 업로드중 오류발생!");
                }
                ImageFile storeImageFile = new ImageFile();
                storeImageFile.setRef_id(store.getStoreId());
                storeImageFile.setFilesize(file.getSize());
                storeImageFile.setFiletype(file.getContentType());
                storeImageFile.setName(fileURL);
                storeImageFile.setOrgName(file.getName());
                storeDao.insertStoreFile(storeImageFile);
            }
            return result;

        } else {
            throw new RuntimeException("가게 정보 삽입중에 오류발생!");
        }
    }

    public List<Map<String, Object>> getStoreList(int listPerPage, int requestPage, String option) {
        int StartLimit = (requestPage-1)* listPerPage;
        int EndLimit = listPerPage;
        System.out.println((StartLimit+1) + "번째 부터 시작하여 "+EndLimit +"개를 가져옵니다.");
        List<Map<String, Object>> list = storeDao.selectStoreList(StartLimit, EndLimit, option);
        System.out.println("가져온 게시물 수 : " + list.size());
        return list;
    }

    public int getStoreListCount(String option) {
        return storeDao.selectStoreListCount(option);
    }

    public void updateStoreStatus(int storeId, String status) {
        storeDao.updateStoreStatus(storeId, status);
    }

    public Store getStoreById(int memberId) {
        List<Store> stores = storeDao.selectStoreById(memberId);
        // 가게가 여러개 있는일은 없어야하는데, 일단은 최상위 1개만 보내도록 하자.
        if(stores.size() > 0){
            // 결과가 있을때만 꺼내서 줌.
            return stores.get(0);
        }
        return new Store();
    }


    @Transactional
    public int addFood(Food food, MultipartFile file) {
        int result = storeDao.insertFood(food);
        System.out.println("적용된 수 : "+result + ", FoodId : "+food.getFoodId());
        if ( result > 0) {


                String fileURL = null;
                try {
                    fileURL = s3Service.upload(file, "food_"+food.getFoodId());
                } catch (IOException e) {
                    throw new RuntimeException("S3 업로드중 오류발생!");
                }
                ImageFile imageFile = new ImageFile();
                imageFile.setRef_id(food.getFoodId());
                imageFile.setFilesize(file.getSize());
                imageFile.setFiletype(file.getContentType());
                imageFile.setName(fileURL);
                imageFile.setOrgName(file.getName());
                storeDao.insertFoodFile(imageFile);



        } else {
            throw new RuntimeException("음식 정보 삽입중에 오류발생!");
        }
        return food.getFoodId();
    }

    public List<ImageFile> getStoreFiles(int storeId) {
        return storeDao.selectStoreFileList(storeId);
    }

    public List<Map<String, Object>> getFoodList(int listPerPage, int requestPage, String option, int store_id) {
        int StartLimit = (requestPage-1)* listPerPage;
        int EndLimit = listPerPage;
        System.out.println((StartLimit+1) + "번째 부터 시작하여 "+EndLimit +"개를 가져옵니다.");
        List<Map<String, Object>> list = storeDao.selectFoodList(StartLimit, EndLimit, option, store_id);
        System.out.println("가져온 게시물 수 : " + list.size());
        return list;
    }

    public int getFoodListCount(String option, int store_id) {
        return storeDao.selectFoodListCount(option, store_id);
    }

    public List<Map<String, Object>> getNearStoresList(Double latitude, Double longitude, float radius, int qantity, String options) {
        return storeDao.selectNearStoresList(latitude, longitude, radius, qantity, options);
    }

    public Store getStoreInfo(Integer storeId) {

        return storeDao.selectStoreByStoreId(storeId);
    }

    public List<Map<String, Object>> getAllFood(Integer storeId) {
        return storeDao.selectAllFoods(storeId);
    }

    public int modifyFoodInfo(Map<String, Object> formData) {
        String foodname = (String) formData.get("foodname");
        String desc = (String) formData.get("description");
        String status = (String) formData.get("status");
        int foodId = ((Integer)formData.get("foodId")).intValue();
        return storeDao.updateFood(foodname,desc,status,foodId);
    }
}
