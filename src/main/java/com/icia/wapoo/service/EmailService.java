package com.icia.wapoo.service;

import lombok.AllArgsConstructor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.icia.wapoo.dao.MemberDao;
import com.icia.wapoo.dao.ProfileDao;
import com.icia.wapoo.login.RandomNumber;
import com.icia.wapoo.login.SHA256;
import com.icia.wapoo.model.LoginInfo;
import com.icia.wapoo.model.Member;

@Service
@AllArgsConstructor
public class EmailService  {
	
	@Autowired
	private ProfileDao profileDao;
    
    @Autowired
    private SHA256 sHA256;
	
	@Autowired
	private RandomNumber randomNumber;

    private JavaMailSender mailSender;
    


    
    //이메일 보내기
    public void sendEmail(Member member, String pwd, String id) throws Exception
    {
    	// 받는 사람 E-Mail 주소
    	String to = member.getEmail();
    	String subject = "";
    	String msg = "";
    	
    	final String FROM = "kkw982698@gmail.com";
    	
    	if(pwd == "")
    	{
    		//이메일 폼
    		subject = "왓푸 아이디 입니다.";
    		
    		msg += member.getName() + "님의 아이디 입니다. ";
    		msg += "아이디 : ";
    		msg += id ;
    	}
    	else if(id == "")
    	{
    		//이메일 폼
    		subject = "왓푸 임시 비밀번호 입니다.";
    		msg += member.getName() + "님의 임시 비밀번호 입니다. 비밀번호를 변경하여 사용하세요.";
    		msg += "임시 비밀번호 : ";
    		msg += pwd + "";
    	}
    	
    	SimpleMailMessage message = new SimpleMailMessage();

        // 업데이트하고 아이디와 새로운비밀번호를 전송한다.
        message.setTo(to);
        message.setFrom(FROM);
        message.setSubject(subject);
        message.setText(msg);

        mailSender.send(message);
    		
    	
    }
    
    //비밀번호 찾기   
    public String findPwd(Map<String, Object> param)
    {
    	String loginId = (String) param.get("loginId");
    	String name = (String) param.get("name");
    	String email = (String) param.get("email");
    	
    	Member member = null;
    	LoginInfo  loginInfo=null;
    	
    	try 
    	{
    		loginInfo = profileDao.getLoginInfo(loginId);
    		member = profileDao.getMember(name, email);
    		
    		
    	}catch(Exception e)
    	{
    		System.out.println("getMember DB 오류: " + e);
    	}
    	
    	
    	if(loginInfo == null)
    	{
    		
    		//아이디 없음
    		return "id";
    	}
    	else if(member == null)
    	{
    		
    		//이름 이메일 다름
    		return "email";
    	}
    	else
    	{
    		
    		// 임시 비밀번호 생성
    		String pwd = "";
    		pwd = randomNumber.getPwd();
    		String hashPwd = pwd;
    		
    		try {
    			hashPwd = sHA256.hashCode(pwd);
			} catch (Exception e) 
    		{
				System.out.println("난수 비밀번호 실패 :  " + e);
				return "not";
			}
    		
    		loginInfo.setPassword(hashPwd);
    		// 비밀번호 변경
    		profileDao.insertPassWord(loginInfo);
    		// 비밀번호 변경 메일 발송
    		try 
    		{
				sendEmail(member, pwd, "");
			} catch (Exception e) 
    		{
				e.printStackTrace();
				
				return "no";
			}
    		
    		return "ok";
    	}
    		
    }
    
    
    //아이디 찾기   
    public String findId(Map<String, Object> param)
    {
    	String name = (String) param.get("name");
    	String email = (String) param.get("email");
    	
    	Member member = null;
    	LoginInfo  loginInfo=null;
    	
    	
    	try 
    	{
    		member = profileDao.getMember(name, email);
    		
    		loginInfo = profileDao.getMemberId(member.getMemberId());
    		
    	}catch(Exception e)
    	{
    		System.out.println("getMember DB 오류: " + e);
    	}
    	
    	
    	if(member == null)
    	{
    		
    		//이름 이메일 다름
    		return "email";
    	}
    	else
    	{
    		
    		//  아이디 발송
    		try 
    		{
    		
				sendEmail(member, "", loginInfo.getLoginId());
			} catch (Exception e) 
    		{
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				return "no";
			}
    	}
    	
    	return "ok";
    }
    
}