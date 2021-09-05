package com.icia.wapoo.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.icia.wapoo.model.Review;
import com.icia.wapoo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.icia.wapoo.jwt.service.JwtService;
import com.icia.wapoo.service.AdminService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

	@Autowired
    private JwtService jwtService;

	@Autowired
    private final ReviewService reviewService;

    @PostMapping("/getReviewList")
    public ResponseEntity getReviewList(@RequestBody Map<String, Object> data) {
        int listPerPage = ((Integer) data.get("listPerPage")).intValue();
        int currentPage = ((Integer) data.get("currentPage")).intValue();
        int storeId = (int) data.get("storeId");
        String option = (String) data.get("showOption");
        if(listPerPage <= 0 || currentPage <=0){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        System.out.println("요청페이지 : " + currentPage + ", 요청게시물 수 : "+listPerPage);
        System.out.println(storeId);

        List<Map<String, Object>> result = reviewService.getReviewList(listPerPage, currentPage, storeId, option);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/getReviewListCount")
    public ResponseEntity getReviewListCount(@RequestParam("storeId") Integer storeId) {
        int result = reviewService.getReviewListCount(storeId);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/getReviewListHasReplyCount")
    public ResponseEntity getReviewListHasReplyCount(@RequestParam("storeId") Integer storeId) {
        int result = reviewService.getReviewListHasReplyCount(storeId);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/getAverageScore")
    public ResponseEntity getAverageScore(@RequestParam("storeId") Integer storeId) {
        float result = reviewService.getAverageScore(storeId);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("/writeReview")
    public ResponseEntity writeReview( Review review,
                                      @RequestPart(value = "imageFile", required = false) MultipartFile multipartFile) {
        int result;
        if (multipartFile == null){
            result = reviewService.writeReview(review);
        } else {
            result = reviewService.writeReview(review, multipartFile);
        }
        return new ResponseEntity(result,HttpStatus.OK);
    }

    @PostMapping("/addReply")
    public ResponseEntity addReply(@RequestBody Map<String, Object> data) {
        Integer reviewId = (Integer) data.get("reviewId");
        String reply = (String) data.get("reply");
        int result = reviewService.addReply(reviewId, reply);
        if(result == 0){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("/modifyReviewStatus")
    public ResponseEntity modifyReviewStatus(@RequestBody Map<String, Object> data) {
        Integer reviewId = (Integer) data.get("reviewId");
        String status = (String) data.get("status");
        int result = reviewService.updateReviewState(reviewId, status);
        if(result > 0){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
	
}
