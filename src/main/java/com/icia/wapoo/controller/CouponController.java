package com.icia.wapoo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.icia.wapoo.jwt.service.JwtService;
import com.icia.wapoo.model.Coupon;
import com.icia.wapoo.service.CouponService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/coupon")
public class CouponController {

	 @Autowired
	 private final CouponService couponService;
	    
	 @Autowired
	 private final JwtService jwtService;

	 @PostMapping("/couponinsert")
	 public void addCoupon(@RequestBody Map<String, Object> data) {
		 String couponNumber = ((String) data.get("couponNumber")).toString();
		 String couponName = ((String) data.get("couponName")).toString();
		 String couponEnd = ((String) data.get("couponEnd")).toString();
		 Integer couponPrice = 0;
		 Integer discountRate = 0;
		 if(((Integer) data.get("couponPrice")).intValue() != 0) {
			 couponPrice = ((Integer) data.get("couponPrice")).intValue();
		 }
		 if(((Integer) data.get("discountRate")).intValue() != 0) {
			 discountRate = ((Integer) data.get("discountRate")).intValue();
		 }
	     
//	     String token = ((String) data.get("access_token")).toString();	//db에 토큰(판매자가 추가하는지 관리자가 추가하는지) 추가하기
//	        
//	     System.out.println(token);
//	        
//	     if(token == null) {
//	    	 System.out.println("토큰이 없습니다.");
//	     }
//	     boolean isValid = jwtService.validateToken(token);
//	     System.out.println("test : 유효한 토큰? -> "+isValid);
//	     if(isValid == false) {
//	         // 유효하지 않은 토큰
//	         System.out.println("유효하지 않은 토큰");
//	     }
//	     Map<String, Object> infos = jwtService.getUserInfo(token);
//	     System.out.println("test : 토큰정보 -> " +infos);
//	     int id = (int) infos.get("memberId");
//	     //Member member = memberService.getMemberBymemberId(id);
//	     

	        
	     if(!couponNumber.isEmpty() && !couponName.isEmpty() && !couponEnd.isEmpty()){
	         Coupon coupon = new Coupon();
	         coupon.setCouponNumber(couponNumber);
	         coupon.setCouponName(couponName);
	         coupon.setCouponEnd(couponEnd);
	         coupon.setCouponPrice(couponPrice);
	         coupon.setDiscountRate(discountRate);   
	         System.out.println(coupon.getCouponName()+" 쿠폰에 대한 데이터가 들어갔습니다.");
	            
	         couponService.insertCoupon(coupon);
	            
	     }else {
	     System.out.println("데이터가 비어있습니다.");
	     }
	        
	 }
	 
	 @PostMapping("/getcouponList")
	    public ResponseEntity getCouponList(@RequestBody Map<String, Object> data) {
	        int listPerPage = ((Integer) data.get("listPerPage")).intValue();
	        int currentPage = ((Integer) data.get("currentPage")).intValue();
	        String option = (String) data.get("statusOption");
	        System.out.println("첫번째 "+listPerPage);
	        System.out.println("두번째 "+currentPage);
	        System.out.println("세번째 "+option);
	        if(listPerPage <= 0 || currentPage <=0){
	            return new ResponseEntity(HttpStatus.BAD_REQUEST);
	        }
	        System.out.println("요청페이지 : " + currentPage + ", 요청게시물 수 : "+listPerPage);
	        // 가게페이지를 가져옵니다.
	        System.out.println(option);

	        List<Map<String, Object>> result = couponService.getCouponList(listPerPage, currentPage, option);
	        return new ResponseEntity(result, HttpStatus.OK);
	    }
	    @GetMapping("/getcouponListCount")
	    public ResponseEntity getCouponListCount(@RequestParam("option") String option) {
	        int result = couponService.getCouponListCount(option);
	        return new ResponseEntity(result, HttpStatus.OK);
	    }

	    @PostMapping("/updatecouponStatus")
	    public ResponseEntity updateCouponStatus(@RequestBody Map<String, Object> data) {
	        int couponId = ((Integer) data.get("couponId")).intValue();
	        String status = ((String) data.get("status")).toString();
	        System.out.println("status를 변경합니다.");
	        if(status == null || couponId < 1){
	            return new ResponseEntity(HttpStatus.BAD_REQUEST);
	        }
	        System.out.println("updateCouponStatus로 들어갑니다.");
	        couponService.updateCouponStatus(couponId, status);
	        return new ResponseEntity(HttpStatus.OK);
	    }

	@GetMapping("/getCouponInfo")
	public ResponseEntity getCouponInfo(@RequestParam("eventId") Integer eventId) {
		Map<String, Object> coupon = couponService.getCouponInfoByEventId(eventId);
		return new ResponseEntity(coupon, HttpStatus.OK);
	}

	@PostMapping("/applyCoupon")
	public ResponseEntity applyCoupon(@RequestBody Map<String, Object> requestData) {
	 	Integer memberId = (Integer) requestData.get("memberId");
	 	Integer couponId = (Integer) requestData.get("couponId");
	 	System.out.println(memberId + "," + couponId);
	 	int result = couponService.getCouponInstance(memberId, couponId);
		if(result == 0) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 	return new ResponseEntity(HttpStatus.OK);
	}

	@GetMapping("/getMemberCoupon")
	public ResponseEntity getMemberCoupon(@RequestParam("memberId") Integer memberId, @RequestParam("foodId") Integer foodId) {
		Map<String, Object> couponInstance = couponService.getMemberCoupon(memberId, foodId);
	 	return new ResponseEntity(couponInstance, HttpStatus.OK);
	}
}
