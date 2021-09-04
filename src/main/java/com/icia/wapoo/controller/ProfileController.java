package com.icia.wapoo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.icia.wapoo.jwt.service.JwtService;
import com.icia.wapoo.model.LoginInfo;
import com.icia.wapoo.model.Member;
import com.icia.wapoo.model.MemberCoupon;
import com.icia.wapoo.model.Order;
import com.icia.wapoo.model.Profile;
import com.icia.wapoo.model.Review;
import com.icia.wapoo.service.MemberService;
import com.icia.wapoo.service.ProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

	private static final String String = null;
	@Autowired
    private JwtService jwtService;
    @Autowired
    private ProfileService profileService;
	
    private int getMemberIdByRequest(HttpServletRequest request) {
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
    
	// 프로필 조회
	@GetMapping("/myProfile")
	public ResponseEntity profileSelect(HttpServletRequest request)
	{
		int memberId = getMemberIdByRequest(request);
		Profile profile = profileService.profileSelect(memberId);
		if(profile == null) {
		    return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
		return new ResponseEntity(profile, HttpStatus.OK);
	}
	
	// 프로필 변경
	@PostMapping("/insertProfile")
	public ResponseEntity myNickname(@RequestBody Map<String, Object> params, HttpServletRequest request)
	{
		System.out.println("프로필 수정하기");
		int memberId = getMemberIdByRequest(request);
		
		Map<String, Object> param = (Map<String, Object>) params.get("params");
		System.out.println("프로필 수정하기:" + params);
		System.out.println("프로필 수정하기:" + param);
		
		if(memberId > 0)
		{
			Profile profile = profileService.profileSelect(memberId);
			
			if(profile != null)
			{
				LoginInfo loginInfo = new LoginInfo();
				
				profile.setNickname((String) param.get("nickname"));	// 닉네임
				profile.setTel((String) param.get("tel"));			//전화번호
				profile.setEmail((String) param.get("email"));		//email
				
				String pwd = (String) param.get("password");
				
				loginInfo.setPassword((String) param.get("password"));	//비밀 번호
				loginInfo.setMember_id(memberId);
				
				if(profileService.insertProfile(profile) > 0)
				{
					System.out.println("프로필 수정완료1");
					System.out.println("pwd : " + pwd);
					
					if(pwd == null)
					{
						System.out.println("프로필만 있고 완료");
						return new ResponseEntity("ok", HttpStatus.OK);
					}
					
					if(profileService.insertPassWord(loginInfo) > 0)
					{
						System.out.println("프로필, 비밀번호 수정 완료");
						return new ResponseEntity("ok", HttpStatus.OK);
					}
					else
					{
						System.out.println("비밀번호 수정 실패");
					}
				}
				else
				{
					System.out.println("프로필 수정 실패");
				}
			}
			
		}
		return new ResponseEntity("no", HttpStatus.OK);
	}
	
	
	//프로필 삭제
	@PostMapping(value = "/deleteprofile")
	public ResponseEntity profileDelete(@RequestBody String password, HttpServletRequest request)
	{
		System.out.println("프로필 탈퇴하기");
		
		int member_id = getMemberIdByRequest(request);
		
		if(member_id > 0)
		{
			LoginInfo loginInfo = null;
			
			System.out.println("member_id: " + member_id);
			System.out.println("password: " + password);
			
			loginInfo = profileService.deleteProfileCheck(member_id, password);
			
			
			
			System.out.println("loginInfo: " + loginInfo);
			
			if(loginInfo != null)
			{
				if(profileService.deleteProfile(member_id) > 0)
				{
					return new ResponseEntity("ok", HttpStatus.OK);
				}
			}
		}
		return new ResponseEntity("no", HttpStatus.OK);
	}
	
	//쿠폰 get리스트
	@PostMapping(value = "/getCoupon")
	public ResponseEntity getCoupon(HttpServletRequest request)
	{
		System.out.println("프로필 쿠폰 정보 가져옵니다");
		int memberId = getMemberIdByRequest(request);
		
		List<Map<String, Object>> memberCoupon = null;
		
		if(memberId > 0)
		{
			memberCoupon = profileService.getCoupon(memberId);
		}
		
		System.out.println("member: " + memberCoupon);


		
		return new ResponseEntity( memberCoupon , HttpStatus.OK);
	}
	

	//주문내용
	@GetMapping(value = "/getOrder")
	public ResponseEntity getOrder(@RequestParam("memberId") Integer memberId, HttpServletRequest request)
	{
		System.out.println("주문 정보 가져옵니다");
		int memberIdCheck = getMemberIdByRequest(request);
		if(memberIdCheck != memberId) {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		if(memberId > 0)
		{
			List<Order> list = profileService.getOrder(memberId);
			return new ResponseEntity(list, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//이미지 변경
	@PostMapping(value = "/insertImage")
	public ResponseEntity updateIamge(@RequestPart(value="image", required = false) MultipartFile image,HttpServletRequest request)
	{
		System.out.println("프로필 이미지를 업로드 합니다");
		int memberId = getMemberIdByRequest(request);
		
		System.out.println("image: " + image);
		if(memberId > 0 && image != null)
		{
			Member member = profileService.getMember(memberId);
			
			if(member != null)
			{
				if(profileService.insertImage(image, memberId) > 0)
				{
					System.out.println("프로필 이미지 업로드 성공");
					return new ResponseEntity("ok", HttpStatus.OK);
				}
			}
		}
		
		System.out.println("프로필 이미지 업로드 실패");
		return new ResponseEntity("no", HttpStatus.OK);
	}
	
	@GetMapping("/getMemberProfilePicture")
	public ResponseEntity getMemberProfilePicture(@RequestParam("memberId") Integer memberId) {
    	String url = profileService.getProfileUrl(memberId);
    	return new ResponseEntity(url, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/getMyReview")
	public ResponseEntity getMyReview(@RequestParam("memberId") Integer memberId, HttpServletRequest request)
	{
		System.out.println("작성했던 리뷰를 가져옵니다");
		int memberIdCheck = getMemberIdByRequest(request);
		if(memberIdCheck != memberId) {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		if(memberId > 0)
		{
			System.out.println("회원넘버" +memberId);
			List<Map<String, Object>> list = profileService.getMyReview(memberId);
			return new ResponseEntity(list, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/ReviewStatus")
    public ResponseEntity modifyReviewStatus(@RequestBody Map<String, Object> data) {
		System.out.println("ReviewStatus Controller");
        Integer reviewId = (Integer) data.get("reviewId");
        String status = (String) data.get("status");
        System.out.println("reviewId" + reviewId);
        System.out.println("status" + status);
        int result = profileService.updateReviewState(reviewId, status);
        if(result > 0){
        	System.out.println(result);
            return new ResponseEntity(HttpStatus.OK);
        }
        System.out.println("error");
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	
}