package com.icia.wapoo.dao;


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
}
