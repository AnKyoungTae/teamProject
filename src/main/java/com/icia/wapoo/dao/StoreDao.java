package com.icia.wapoo.dao;

import com.icia.wapoo.model.Food;
import com.icia.wapoo.model.Store;
import com.icia.wapoo.model.ImageFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface StoreDao {
    int insertStoreFile(ImageFile imageFile);

    int insertStore(Store store);

    List<Map<String, Object>> selectStoreList(@Param("startLimit") int startLimit, @Param("endLimit") int endLimit, @Param("option") String option);

    int selectStoreListCount(@Param("option") String option);

    int updateStoreStatus(@Param("storeId") int storeId, @Param("status") String status);

    List<Store> selectStoreById(@Param("memberId") int memberId);

    int insertFood( Food food);

    int insertFoodFile(ImageFile imageFile);

    List<ImageFile> selectStoreFileList(int storeId);

    List<Map<String, Object>> selectFoodList(@Param("startLimit") int startLimit, @Param("endLimit") int endLimit, @Param("option") String option, @Param("store_id") int store_id);

    int selectFoodListCount(@Param("option") String option, @Param("store_id") int store_id);

    List<Map<String, Object>> selectNearStoresList(
            @Param("lat") Double latitude,
            @Param("lon") Double longitude,
            @Param("radius") float radius,
            @Param("qantity") int qantity,
            @Param("options") String options,
            @Param("loadFrom") int loadFrom);

    Store selectStoreByStoreId(@Param("storeId") Integer storeId);

    List<Map<String, Object>> selectAllFoods(@Param("storeId") Integer storeId);

    int updateFood(@Param("foodname") String foodname,
                   @Param("desc") String desc,
                   @Param("status") String status,
                   @Param("foodId") int foodId);

    Map<String, Object> selectFoodByFoodId(@Param("foodId") int foodId);
}
