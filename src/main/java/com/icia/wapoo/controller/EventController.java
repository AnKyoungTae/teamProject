package com.icia.wapoo.controller;

import java.util.List;
import java.util.Map;


import com.icia.wapoo.model.Coupon;
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


}
