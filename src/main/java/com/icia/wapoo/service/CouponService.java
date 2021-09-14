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
       
        if ( count > 0) {
        	return count;
        	
        } else {
        
            throw new RuntimeException("쿠폰 정보 삽입중에 오류발생!");
        }
    }
    

    public List<Coupon> getCouponList(int listPerPage, int requestPage, String option) {
        int StartLimit = (requestPage-1)* listPerPage;
        int EndLimit = listPerPage;
       
        List<Coupon> list = couponDao.selectCouponList(StartLimit, EndLimit, option);// 여기서에러
    
        return list;
        //return null;
    }


    public int getCouponListCount(String option) {
        return couponDao.selectCouponListCount(option);
    }
    
    public void updateCouponStatus(int couponId, String status) {
  
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
    
    // 사용자가 쿠폰 있는지 확인
    public int existCounpon(Integer memberId, Integer couponId)
    {
    	int count = 0;
    	
    	try
    	{
    		count = couponDao.existCounpon(memberId, couponId);
    	}
    	catch(Exception e)
    	{
    		System.out.println("existCounpon: "+ e);
    	}
    	
    	
    	return count;
    }
}