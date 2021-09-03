package com.icia.wapoo.dao;


import com.icia.wapoo.model.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ReviewDao {

    int insertReview(Review review);

    List<Map<String, Object>> selectReviewList(
            @Param("startLimit") int startLimit,
            @Param("endLimit") int endLimit,
            @Param("storeId") Integer storeId,
            @Param("option") String option);

    int selectReviewListCount(@Param("storeId") Integer storeId) ;

    int selectReviewListHasReplyCount(@Param("storeId") Integer storeId) ;

    float selectAverageScore(@Param("storeId") Integer storeId);

    int updateReviewAddReply(@Param("reviewId") Integer reviewId,
                             @Param("reply") String reply);

    int updateReviewStatus(@Param("reviewId") Integer reviewId, @Param("status") String state);

}
