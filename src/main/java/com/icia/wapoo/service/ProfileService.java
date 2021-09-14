package com.icia.wapoo.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.icia.wapoo.S3.S3Service;
import com.icia.wapoo.dao.MemberDao;
import com.icia.wapoo.dao.ProfileDao;

import com.icia.wapoo.login.SHA256;

import com.icia.wapoo.model.LoginInfo;
import com.icia.wapoo.model.Member;
import com.icia.wapoo.model.Profile;

@Service
public class ProfileService {
	
	@Autowired
	private ProfileDao profileDao;
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private final S3Service s3Service = null;
	
	@Autowired
    private SHA256 sHA256;
	
	
	
	//프로필 조회 profileDao에서 가져옴
	public Profile profileSelect(int memberId)
	{
		Profile profile = profileDao.checkProfile(memberId);
		return profile;
	}
	
	//프로필 수정하기
	public int insertProfile(Profile profile)
	{
		int count = 0;
		
		try
		{
			count = profileDao.insertProfile(profile);
		}
		catch(Exception e)
		{
			System.out.println("insertProfile 오류: " + e);
		}
		return count;
	}
	
	//비밀번호 수정하기
	public int insertPassWord(LoginInfo loginInfo)
	{
		int count = 0;
		
		//비밀번호 해쉬변화
        String pwd = null;
        try 
        {
        	
        	pwd = sHA256.hashCode(loginInfo.getPassword());
        	
        	try
    		{
        		
            	loginInfo.setPassword(pwd);
            	
    			count = profileDao.insertPassWord(loginInfo);
    		}
    		catch(Exception e)
    		{
    			System.out.println("insertPassWord 오류: " + e);
    		}
        	
        }catch(Exception e)
        {
        	System.out.println("비밀번호 해쉬중 오류발생!!");
        }
		
		return count;
	}
	
	//회원 확인
	public LoginInfo deleteProfileCheck(int member_id,String password)
	{
		LoginInfo loginInfo = null;
		
		try
		{
			loginInfo = profileDao.deleteProfileCheck(member_id, password);
		}
		catch(Exception e)
		{
			System.out.println("deleteProfileCheck 오류: " + e);
		}
		
		return loginInfo;
	}
	
	//탈퇴하기
	public int deleteProfile(int member_id)
	{
		int count = 0;
		
		try
		{
			count = profileDao.deleteProfile(member_id);
		}
		catch(Exception e)
		{
			System.out.println("deleteProfile 오류: " + e);
		}
		
		return count;
	}
	
	//쿠폰정보 가져오기
	public List<Map<String, Object>> getCoupon (int memberId)
	{
		List<Map<String, Object>>  memberCoupon = null;
		
		try
		{
			memberCoupon = profileDao.getCoupon(memberId);
		}
		catch(Exception e)
		{
		
			System.out.println("getCoupon 오류: " + e);
		}
		return memberCoupon;
	}
	
	//멤버 정보 가져오기
	public Member getMember(int memberId)
	{
		Member member = null;
		
		try
		{
			member = memberDao.selectMemberById(memberId);
		}
		catch(Exception e)
		{
			
			System.out.println("getCoupon 오류: " + e);
		}
		
		return member;
	}
	
	//주문내용 가져오기
	public List<Map<String, Object>> getOrder(Integer memberId)
	{
		List<Map<String, Object>> list = null;
		
		try
		{
			list = profileDao.selectAllOrders(memberId);
		}
		catch(Exception e)
		{
		
			System.out.println("getOrder 오류: " + e);
		}
		
		return list;
	}
	
	//이미지 업로드
	public int insertImage(MultipartFile image, int memberId)
	{
		int count = 0;
		String fileURL = null;
        try {
            fileURL = s3Service.upload(image, "profile_image_" + memberId);
        }
        catch (IOException e) 
        {
            throw new RuntimeException("S3 업로드중 오류발생!");
        }
        
        if(fileURL != null)
        {
        	try
        	{
        		count = profileDao.insertImage(fileURL, memberId);
        	}
        	catch(Exception e)
    		{
    			System.out.println("insertImage 오류: " + e);
    		}
        }
		
		
		return count;
	}

	//이미지 찾기
    public String getProfileUrl(Integer memberId) {
		return profileDao.selectFileUrl(memberId);
    }
    
    //내가 작성한 리뷰가져오기
    public List<Map<String, Object>> getMyReview(Integer memberId)
	{
		List<Map<String, Object>> list = null;
		
		try
		{
			list = profileDao.selectMyReview(memberId);
		}
		catch(Exception e)
		{
		
			System.out.println("getMyReview 오류: " + e);
		}
		
		return list;
	}
    
    public int updateReviewState(Integer reviewId, String state) {
    	
        return profileDao.updateReviewStatus(reviewId, state);
    }
}
