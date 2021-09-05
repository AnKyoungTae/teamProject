package com.icia.wapoo.controller;

import com.icia.wapoo.jwt.service.JwtService;
import com.icia.wapoo.model.Food;
import com.icia.wapoo.model.KakaoPayApproval;
import com.icia.wapoo.model.KakaoPayReady;
import com.icia.wapoo.service.OrderService;
import com.icia.wapoo.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final StoreService storeService;
    @Autowired
    private final OrderService orderService;

    private int getMemberIdByRequest(HttpServletRequest request) {
        System.out.println("받은 토큰으로 멤버를 검색합니다");
        String token = jwtService.resolveToken(request);
        Map<String, Object> claims = jwtService.getUserInfo(token);
        return ((Integer) claims.get("memberId")).intValue();
    }

    @PostMapping("/getFoodList")
    public ResponseEntity getFoodList(@RequestBody Map<String, Object> data) {
        List<Integer> foodIdList = (List<Integer>) data.get("foodIdList");
        List<Map<String, Object>> foodList = storeService.getFoodListByFoodIdList(foodIdList);
        return new ResponseEntity(foodList, HttpStatus.OK);
    }

    @PostMapping("/putOrder")
    public ResponseEntity putOrder(@RequestBody Map<String, Object> orderData) {
        Integer orderId = orderService.createOrder(orderData);
        System.out.println("생성된 OrderId : "+orderId);
        if(orderId.intValue() > 0){
            return new ResponseEntity(orderId, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/putOrderInfo")
    public ResponseEntity putOrderInfo(@RequestBody List<Object> orderInfo) {
        System.out.println(orderInfo);
        Integer foodId = (Integer) orderInfo.get(0);
        Integer quantity = (Integer) orderInfo.get(1);
        Integer orderId = (Integer) orderInfo.get(2);
        int orderInfoId = orderService.createOrderInfo(orderId, quantity, foodId);
        if(orderInfoId >0){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/kakaoPay")
    public String kakaoPayReady() {
        // 리턴값이 "Bad"일 경우 예외처리해줘야함.
        return orderService.getKakaoPayReady();
    }

    @GetMapping("/kakaoPayInfo")
    public int kakaoPayInfo(@RequestParam("pg_token") String pg_token) {
        // null 일 경우 예외처리
        int orderId = orderService.getkakaoPayInfo(pg_token);
        return orderId;
    }

    @GetMapping("/getOrderedFoodList")
    public Map<String, Object> getOrderedFoodList(@RequestParam("orderId") Integer orderId) {
        Map<String, Object> resultMap = orderService.getOrderedFoodList(orderId);
        return resultMap;
    }

    @PostMapping("/requestCancel")
    public Map<String, Object> requestCancel(@RequestBody Integer orderId) {
        return null;
    }
}
