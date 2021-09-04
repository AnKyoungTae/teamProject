package com.icia.wapoo.controller;

import java.util.List;
import java.util.Map;


import com.icia.wapoo.model.Coupon;
import com.icia.wapoo.model.Store;
import com.icia.wapoo.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.icia.wapoo.jwt.service.JwtService;
import com.icia.wapoo.model.Event;
import com.icia.wapoo.model.Member;

import com.icia.wapoo.service.EventService;
import com.icia.wapoo.service.MemberService;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    private final EventService eventService;
    @Autowired
    private final MemberService memberService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final StoreService storeService;

    private Integer getMemberIdByRequest(HttpServletRequest request) {
        System.out.println("받은 토큰으로 멤버를 검색합니다");
        Integer memberId = 0;
        try
        {
            //JWT 토큰값
            String JWT = jwtService.resolveToken(request);
            System.out.println(JWT);

            Map<String, Object> token = jwtService.getUserInfo(JWT);
            // token에서 memberId 값 가져오기
            memberId = ((Integer) token.get("memberId")).intValue();
        }
        catch(Exception e)
        {
            System.out.println("memberId 없음");
        }
        return memberId;
    }

    @PostMapping("/eventinsert")
    public void eventInsert(@RequestBody Map<String, Object> data) {
        String title = ((String) data.get("title")).toString();
        String body = ((String) data.get("body")).toString();
        String dueDate = ((String) data.get("dueDate")).toString();
        String token = ((String) data.get("access_token")).toString();
        
        System.out.println(token);
        
        if(token == null) {
            System.out.println("토큰이 없습니다.");
        }
        boolean isValid = jwtService.validateToken(token);
        System.out.println("test : 유효한 토큰? -> "+isValid);
        if(isValid == false) {
            // 유효하지 않은 토큰
            System.out.println("유효하지 않은 토큰");
        }
        Map<String, Object> infos = jwtService.getUserInfo(token);
        System.out.println("test : 토큰정보 -> " +infos);
        int id = (int) infos.get("memberId");
        Member member = memberService.getMemberBymemberId(id);
        System.out.println(id);

        
        if(!title.isEmpty() && !body.isEmpty() && !dueDate.isEmpty()){
            Event event = new Event();
            event.setTitle(title);
            event.setBody(body);
            event.setDueDate(dueDate);
            event.setProvider_id(id);           
            System.out.println(event.getTitle()+" 이벤트에 대한 데이터가 들어갔습니다.");
            
            
            if(event.getProvider_id() == 1 || event.getProvider_id() == 2){
                System.out.println("provider_id가 있습니다.");
                eventService.insertEvent(event);	//서비스로 이동
                System.out.println("이벤트 insert 성공");
            }else {
            	System.out.println("관계자가 아닌 member_id");
            }
            
        }else {
        	System.out.println("데이터가 비어있습니다.");
        }
        
    }

    @PostMapping("/getEventList")
    public ResponseEntity getEventList(@RequestBody Map<String, Object> data) {
        int listPerPage = ((Integer) data.get("listPerPage")).intValue();
        int currentPage = ((Integer) data.get("currentPage")).intValue();
        String option = (String) data.get("statusOption");
        if(listPerPage <= 0 || currentPage <=0){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        System.out.println("요청페이지 : " + currentPage + ", 요청게시물 수 : "+listPerPage);
        System.out.println(option);

        List<Map<String, Object>> result = eventService.getEventList(listPerPage, currentPage, option);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/getEventListCount")
    public ResponseEntity getEventListCount(@RequestParam("option") String option) {
        int result = eventService.getEventListCount(option);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/getFoodList")
    public ResponseEntity getFoodList(HttpServletRequest request) {
        Integer requesterId = getMemberIdByRequest(request);
        if(requesterId == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Store store = storeService.getStoreById(requesterId);
        if(store == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        List<Map<String, Object>> foodList = storeService.getAllFood(store.getStoreId());
        return new ResponseEntity(foodList, HttpStatus.OK);
    }

}
