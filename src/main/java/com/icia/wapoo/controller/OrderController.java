package com.icia.wapoo.controller;

import com.icia.wapoo.jwt.service.JwtService;
import com.icia.wapoo.model.Food;
import com.icia.wapoo.model.GraphDay;
import com.icia.wapoo.model.GraphFood;
import com.icia.wapoo.model.GraphResntFood;
import com.icia.wapoo.model.KakaoPayApproval;
import com.icia.wapoo.model.KakaoPayReady;
import com.icia.wapoo.model.Store;
import com.icia.wapoo.model.StoreOrder;
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
import java.util.HashMap;
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
   
        if(orderId.intValue() > 0){
            return new ResponseEntity(orderId, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/putOrderInfo")
    public ResponseEntity putOrderInfo(@RequestBody List<Object> orderInfo) {

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
        // ???????????? "Bad"??? ?????? ????????????????????????.
        return orderService.getKakaoPayReady();
    }

    @GetMapping("/kakaoPayInfo")
    public int kakaoPayInfo(@RequestParam("pg_token") String pg_token) {
        // null ??? ?????? ????????????
        int orderId = orderService.getkakaoPayInfo(pg_token);
        return orderId;
    }

    @GetMapping("/getOrderedFoodList")
    public Map<String, Object> getOrderedFoodList(@RequestParam("orderId") Integer orderId) {
        Map<String, Object> resultMap = orderService.getOrderedFoodList(orderId);
        return resultMap;
    }

    @PostMapping("/requestCancel")
    public ResponseEntity requestCancel(@RequestBody Map<String, Object> data) {
        Integer orderId = (Integer) data.get("orderId");
        String status = (String) data.get("status");
        int result = orderService.setOrderStatus(orderId, status);
        if(result > 0) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    //?????????
    @PostMapping("/storeOrder")
    public ResponseEntity storeOrder(@RequestBody Map<String, Object> orderStatus, HttpServletRequest request)
    {
    	int memberId = getMemberIdByRequest(request);
    	
    	String status = (String) orderStatus.get("satus");
    	
    	//getStoreById
    	List<StoreOrder> storeOrder = null;
    	
    	Store store = storeService.getStoreById(memberId);
    	//?????????
    	storeOrder = orderService.storeOrder(store.getStoreId(), status);
    	//????????? ?????? ??????
    	int total = orderService.getTotalOrder(store.getStoreId(), status);
    	//?????? ?????? ?????? 
    	List<StoreOrder> cancelStoreOrder = orderService.storeOrder(store.getStoreId(), "C");
    	
    	Map<String, Object> map = new HashMap<>();
    	
    	map.put("storeOrder", storeOrder);
    	map.put("total", total);
    	map.put("cancelStoreOrder", cancelStoreOrder);
    	
    	return new ResponseEntity(map, HttpStatus.OK);
    }
    
    //?????? ?????? 1??? ??????
    @PostMapping("/deleteOrder")
    public ResponseEntity deleteOrder(@RequestBody String orderStoreId, HttpServletRequest request)
    {
    	int orderInfoId = Integer.parseInt(orderStoreId);
    	
   
    	
    	if(orderInfoId > 0)
    	{
    		if(orderService.deleteOrder(orderInfoId) > 0)
    		{
    			return new ResponseEntity("ok", HttpStatus.OK);
    		}
    	}
    	
    	return new ResponseEntity("no", HttpStatus.OK);
    }
    
  //?????? 1??? ??????
    @PostMapping("/deleteAllOrder")
    public ResponseEntity deleteAllOrder(@RequestBody List<Object> list, HttpServletRequest request)
    {
    	
    	if(list.size() > 0)
    	{
    		if(orderService.deleteAllOrder(list) > 0)
    		{
    			return new ResponseEntity("ok", HttpStatus.OK);
    		}
    	}
    	
    	return new ResponseEntity("no", HttpStatus.OK);
    }
    
    //?????? ?????? ?????? 
    @PostMapping("/approveOrder")
    public ResponseEntity approveOrder(@RequestBody List<Object> list, HttpServletRequest request)
    {

    	if(list.size() > 0)
    	{
    		if(orderService.approveOrder(list) > 0)
    		{
    			return new ResponseEntity("ok", HttpStatus.OK);
    		}
    	}
    	return new ResponseEntity("no", HttpStatus.OK);
    }
    
    
    //?????? ?????? ?????? ??????
    @PostMapping("/getFoodSaleAmount")
    public ResponseEntity getFoodSaleAmount(@RequestBody String date, HttpServletRequest request)
    {
    	
    	int memberId = getMemberIdByRequest(request);
    	
    	Store store = storeService.getStoreById(memberId);
    	
    	List<GraphFood> graphFood = orderService.getFoodSaleAmount(store.getStoreId(), date);
    	
    	return new ResponseEntity(graphFood, HttpStatus.OK);
    }
    
    //?????? ????????? ??????
    @PostMapping("/getDayAmount")
    public ResponseEntity getDayAmount(@RequestBody String date, HttpServletRequest request)
    {
    	
    	int memberId = getMemberIdByRequest(request);
    	
    	Store store = storeService.getStoreById(memberId);
    	
    	List<GraphDay> graphDay = orderService.getDayAmount(store.getStoreId(), date);
    	
    	return new ResponseEntity(graphDay, HttpStatus.OK);
    }
    
    //?????? ????????? ?????? ????????? 
    @PostMapping("/getResentFood")
    public ResponseEntity getResentFood(@RequestBody Map<String, Object> params,  HttpServletRequest request)
    {
    	int memberId = getMemberIdByRequest(request);
    	
    	Store store = storeService.getStoreById(memberId);
    	
    	List<String> list = orderService.getStoreAllFood(store.getStoreId());
    	
    	
    	
    	String date = (String) params.get("date");

    	String name = (String) params.get("name");
    	
    	Map<String, Object> map = new HashMap<>();
    	
    	List<GraphResntFood> graphResntFood = null;
    
    	if(name == "")
    	{
    		name = list.get(0);
    		
    		graphResntFood = orderService.getResentFood(store.getStoreId(), date, name);
    	}
    	else
    	{	
    		graphResntFood = orderService.getResentFood(store.getStoreId(), date, name);
    		
    	}
    	
    	map.put("list", list);
    	map.put("graphResntFood", graphResntFood);
    	map.put("name", name);
    	
    	
    	return new ResponseEntity(map, HttpStatus.OK);
    }
    
    //getPayment
    @PostMapping("/getPayment")
    public ResponseEntity getPayment(HttpServletRequest request)
    {
    	int memberId = getMemberIdByRequest(request);
    	
    	Store store = storeService.getStoreById(memberId);
    	
    	Map<String, Object> map = new HashMap<>();
    	
    	map = orderService.getPayment(store.getStoreId());
    	
    	return new ResponseEntity(map, HttpStatus.OK);
    }
    
    
    
    
    
    
    
    
    
}
