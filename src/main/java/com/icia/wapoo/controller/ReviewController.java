package com.icia.wapoo.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icia.wapoo.jwt.service.JwtService;
import com.icia.wapoo.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

	@Autowired
    private JwtService jwtService;
	
	
	private int getMemberIdByRequest(HttpServletRequest request) 

	{
        System.out.println("받은 토큰으로 멤버를 검색합니다");
        
        int memberId = 0;
        
        try
        {
        	String token = jwtService.resolveToken(request);
            Map<String, Object> claims = jwtService.getUserInfo(token);
            memberId = ((Integer) claims.get("memberId")).intValue();
        }
        catch(Exception e)
        {
        	System.out.println("getMemberIdByRequest 오류 : " + e);
        }
        
        
         return memberId;
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
