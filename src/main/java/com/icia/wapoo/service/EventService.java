package com.icia.wapoo.service;

import com.icia.wapoo.dao.CouponDao;
import com.icia.wapoo.model.Coupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icia.wapoo.dao.EventDao;

import com.icia.wapoo.model.Event;


import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class EventService {
   
    @Autowired
    private final EventDao eventDao;
    @Autowired
    private final CouponDao couponDao;

    @Transactional
    public int addEvent(Event event, Coupon coupon){
    	int count = eventDao.insertEvent(event);
        if ( count > 0) {
            coupon.setEvent_id(event.getEventId());
        	int result = couponDao.insertCoupon(coupon);
        	if(result > 0) {
        	    return result;
            }
        } else {
  
            throw new RuntimeException("이벤트 정보 삽입중에 오류발생!");
        }
        return 0;
    }

    public List<Map<String, Object>> getEventList(int listPerPage, int requestPage, String option) {
        int StartLimit = (requestPage-1)* listPerPage;
        int EndLimit = listPerPage;
        
        List<Map<String, Object>> list = eventDao.selectEventList(StartLimit, EndLimit, option);
  
        return list;
    }

    public int getEventListCount(String option) {
        return eventDao.selectEventListCount(option);
    }

    public Coupon getCouponInfoByEventId(Integer eventId) {
        return eventDao.selectCouponByEventId(eventId);
    }
}
