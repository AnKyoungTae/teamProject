package com.icia.wapoo.service;

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

    @Transactional
    public int insertEvent(Event event){
    	System.out.println("서비스 들어옴");
    	int count = eventDao.insertEvent(event);
        System.out.println("EventService : 적용된 이벤트 수 => " + count);
        
        if ( count > 0) {
        	System.out.println("EventService 통과");
        	return count;
        	
        } else {
        	System.out.println("EventService 통과못함");
            throw new RuntimeException("이벤트 정보 삽입중에 오류발생!");
        }
    }

    public List<Map<String, Object>> getEventList(int listPerPage, int requestPage, String option) {
        int StartLimit = (requestPage-1)* listPerPage;
        int EndLimit = listPerPage;
        System.out.println((StartLimit+1) + "번째 부터 시작하여 "+EndLimit +"개를 가져옵니다.");
        List<Map<String, Object>> list = eventDao.selectEventList(StartLimit, EndLimit, option);
        System.out.println("가져온 게시물 수 : " + list.size());
        return list;
    }

    public int getEventListCount(String option) {
        return eventDao.selectEventListCount(option);
    }

    public Coupon getCouponInfoByEventId(Integer eventId) {
        return eventDao.selectCouponByEventId(eventId);
    }
}
