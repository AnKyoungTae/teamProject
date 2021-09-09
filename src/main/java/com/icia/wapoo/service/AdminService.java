package com.icia.wapoo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icia.wapoo.dao.EventDao;
import com.icia.wapoo.model.AdminEvent;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service("adminService")
public class AdminService {
    @Autowired
    private final StoreService storeService;
    
    @Autowired
    EventDao eventDao;

    public List<Map<String, Object>> getStoreList(int listPerPage, int currentPage, String option) {
        return storeService.getStoreList(listPerPage, currentPage, option);
    }

    public int getStoreListCount(String option) {
        return storeService.getStoreListCount(option);
    }

    public void updateStoreStatus(int storeId, String status) {
        System.out.println(storeId+ " 가게의 상태를 "+status+" 로 바꿉니다.");
        storeService.updateStoreStatus(storeId, status);
    }
    
    //이벤트 가져오기
    public List<AdminEvent> adminEvent(int listPerPage, int currentPage, String option)
    {
    	List<AdminEvent> list = null;
    	
    	try
    	{
    		list = eventDao.adminEvent(listPerPage, currentPage, option);
    	}
    	catch(Exception e)
    	{
    		System.out.println("adminEvent 오류: " + e);
    	}
    	
    	return list;
    }
    
    //이벤스 스테터스 수정
    public int updateEventStatus(int eventId, String status)
    {
    	int count = 0;
    	try
    	{
    		count = eventDao.updateEventStatus(eventId, status);
    	}
    	catch(Exception e)
    	{
    		System.out.println("updateEventStatus 오류: " + e);
    	}
    	
    	return count;
    }
    
    
    public int adminEventcount(String option) {
        return eventDao.adminEventcount(option);
    }
    
    
}
