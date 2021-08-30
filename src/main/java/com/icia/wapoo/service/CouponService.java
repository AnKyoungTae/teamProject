package com.icia.wapoo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icia.wapoo.dao.CouponDao;
import com.icia.wapoo.model.Coupon;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CouponService {
   
    @Autowired
    private CouponDao couponDao;

    @Transactional
    public int insertCoupon(Coupon coupon){
    	System.out.println("쿠폰 서비스 들어옴");
    	int count = couponDao.insertCoupon(coupon);
        System.out.println("CouponService : 적용된 쿠폰 수 => " + count);
        
        if ( count > 0) {
        	System.out.println("CouponService 통과");
        	return count;
        	
        } else {
        	System.out.println("CouponService 통과못함");
            throw new RuntimeException("쿠폰 정보 삽입중에 오류발생!");
        }
    }
    

    public List<Map<String, Object>> getCouponList(int listPerPage, int requestPage, String option) {
        int StartLimit = (requestPage-1)* listPerPage;
        int EndLimit = listPerPage;
        System.out.println((StartLimit+1) + "번째 부터 시작하여 "+EndLimit +"개를 가져옵니다.");
//        List<Map<String, Object>> list = couponDao.selectCouponList(StartLimit, EndLimit, option);// 여기서에러
//        System.out.println("가져온 게시물 수 : " + list.size());
//        return list;
        return null;
    }


    public int getCouponListCount(String option) {
        return couponDao.selectCouponListCount(option);
    }
    
    public void updateCouponStatus(int couponId, String status) {
    	System.out.println("updateCouponStatus로 들어왔습니다.");
    	System.out.println(couponId+"를 "+status+"로 변경합니다.");
        couponDao.updateCouponStatus(couponId, status);
    }

    public Map<String, Object> getCouponInfoByEventId(Integer eventId) {
        return couponDao.selectCouponByEventId(eventId);
    }

    public int getCouponInstance(Integer memberId, Integer couponId) {
        return couponDao.insertMemberCoupon(memberId, couponId);
    }

    public Map<String, Object> getMemberCoupon(Integer memberId, Integer foodId) {
        return couponDao.selectMemberCoupon(memberId, foodId);
    }
}