package com.icia.wapoo.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;


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

    @PostMapping("/addEvent")
    public ResponseEntity addEvent(@RequestBody Map<String, Object> data, HttpServletRequest request) {
        Integer memberId = getMemberIdByRequest(request);
        if(memberId == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Event event = new Event();
        event.setRegDate("NOW()");
        event.setUpdateDate((String) data.get("updateDate"));
        event.setDueDate((String) data.get("dueDate"));
        event.setStatus("S");
        event.setBody((String) data.get("body"));
        event.setTitle((String) data.get("title"));
        event.setProvider_id(memberId.intValue());


        Coupon coupon = new Coupon();
        coupon.setCouponNumber(UUID.randomUUID().toString());
        coupon.setCouponName((String) data.get("name"));
        coupon.setCouponEnd((String) data.get("couponEnd"));
        coupon.setStatus("Y");
        coupon.setCouponPrice((Integer) data.get("price"));
        coupon.setDiscountRate((Integer) data.get("discountRate"));
        coupon.setPublishedDate("NOW()");
        coupon.setFood_id((Integer) data.get("foodId"));
        int result = eventService.addEvent(event, coupon);
        if(result > 0) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
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
