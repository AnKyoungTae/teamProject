package com.icia.wapoo.dao;


import com.icia.wapoo.model.AdminEvent;
import com.icia.wapoo.model.Coupon;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.icia.wapoo.model.Event;

import java.util.List;
import java.util.Map;


@Repository
@Mapper
public interface EventDao {
	
    int insertEvent(Event event);

    List<Map<String, Object>> selectEventList(
            @Param("startLimit") int startLimit, @Param("endLimit") int endLimit, @Param("option") String option);

    int selectEventListCount(@Param("option") String option);

    Coupon selectCouponByEventId(@Param("eventId") Integer eventId);
    
    //이벤트 가져오기
    List<AdminEvent> adminEvent(int listPerPage, int currentPage, String option);
    
    //이벤트 수정
    int updateEventStatus(int eventId, String status);
}
