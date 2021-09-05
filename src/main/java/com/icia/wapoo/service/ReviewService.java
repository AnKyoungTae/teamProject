package com.icia.wapoo.service;

import com.icia.wapoo.S3.S3Service;
import com.icia.wapoo.dao.ReviewDao;
import com.icia.wapoo.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ReviewService {
    @Autowired
    private final S3Service s3Service;
    @Autowired
    private final ReviewDao reviewDao;

    public int writeReview(Review review) {
        return reviewDao.insertReview(review);
    }
    @Transactional
    public int writeReview(Review review, MultipartFile file) {

        try {
            String fileURL = s3Service.upload(file, "review_"+review.getWriterid());
            review.setFoodPhoto(fileURL);
            int result = reviewDao.insertReview(review);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Map<String, Object>> getReviewList(int listPerPage, int requestPage, Integer storeId, String option) {
        int StartLimit = (requestPage-1)* listPerPage;
        int EndLimit = listPerPage;
        System.out.println((StartLimit+1) + "번째 부터 시작하여 "+EndLimit +"개를 가져옵니다.");
        List<Map<String, Object>> list = reviewDao.selectReviewList(StartLimit, EndLimit, storeId, option);
        System.out.println("가져온 게시물 수 : " + list.size());
        return list;
    }
    

    public int getReviewListCount(Integer storeId) {
        return reviewDao.selectReviewListCount(storeId);
    }
    public int getReviewListHasReplyCount(Integer storeId) {
        return reviewDao.selectReviewListHasReplyCount(storeId);
    }
    public float getAverageScore(Integer storeId) {
        return reviewDao.selectAverageScore(storeId);
    }

    public int addReply(Integer reviewId, String reply) {
        return reviewDao.updateReviewAddReply(reviewId, reply);
    }

    public int updateReviewState(Integer reviewId, String state) {
        return reviewDao.updateReviewStatus(reviewId, state);
    }
}
