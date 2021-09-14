package com.icia.wapoo.service;

import com.icia.wapoo.dao.LoginInfoDao;
import com.icia.wapoo.dao.MemberDao;
import com.icia.wapoo.login.SHA256;
import com.icia.wapoo.model.LoginInfo;
import com.icia.wapoo.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("memberService")
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    @Autowired
    private final MemberDao memberDao;

    @Autowired
    private final LoginInfoDao loginInfoDao;
    
    @Autowired
    private SHA256 sHA256;

    @Override
    public Member getMemberByLoginInfo(String loginId, String password) {
     
        LoginInfo loginInfo = null;
        //비밀번호 해쉬변화
        String pwd = null;
        try 
        {
        	pwd = sHA256.hashCode(password);
        	
        	loginInfo = loginInfoDao.selectloginInfo(loginId, pwd);
        }catch(Exception e)
        {
        	System.out.println("비밀번호 해쉬중 오류발생!!");
        	e.printStackTrace();
        	
        }


        if(loginInfo == null) {
        
            // 아이디, 비밀번호로 조회한 로그인가능한 정보가 없음.
            return null;
        }
       
        return memberDao.selectMemberById(loginInfo.getMember_id());
    }

    @Override
    public Member getMemberBymemberId(int memberId) {
        return memberDao.selectMemberById(memberId);
    }

    /**
     * Member 테이블을 먼저 삽입하고, 성공적( return 1 ) 이면, loginInfo 테이블에 로그인아이디 비밀번호를 저장한다.
     * @param userData
     * @return
     */
    @Override
    public boolean registerMember(Map<String, Object> userData) {
        Member member = new Member();
        member.setPhone((String) userData.get("phone"));
        member.setName((String) userData.get("name"));
        member.setEmail((String) userData.get("email"));
        member.setNickname((String) userData.get("nickname"));
        member.setRole((String) userData.get("role"));

        System.out.println("Member 테이블에 데이터를 삽입합니다.");

        int result = memberDao.insertMember(member);
        if(result > 0){
            System.out.println("결과값 : " + member.getMemberId());
            System.out.println("member테이블에 데이터 삽입완료 생성된 id -> " + member.getMemberId());
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setLoginId((String) userData.get("loginId"));
            //비밀번호 해쉬변화
            String pwd = null;
            result = 0;
            try 
            {
            	pwd = sHA256.hashCode((String) userData.get("password"));
            	
            	loginInfo.setPassword(pwd);
                loginInfo.setMember_id(member.getMemberId());
               
                result = loginInfoDao.insertLoginInfo(loginInfo);
            }catch(Exception e)
            {
            	System.out.println("비밀번호 해쉬중 오류발생!!");
            	e.printStackTrace();
            	
            }
            
            
            if(result > 0) {
                System.out.println("LoginInfo 데이터 삽입 완료");
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkLoginId(String loginId) {
        int result = loginInfoDao.checkLoginId(loginId);
     
        if(result > 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkEmail(String email) {
        int result = memberDao.checkEmail(email);
        System.out.println("email 중복체크 .. 중복된 이메일 : " + result);
        if(result > 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkNickname(String nickname) {
        int result = memberDao.checkNickname(nickname);
        
        if(result > 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkPhone(String phone) {
        int result = memberDao.checkPhone(phone);

        if(result > 0) {
            return false;
        }
        return true;
    }
    
    
    public List<Map<String, Object>> getMemberList(int listPerPage, int requestPage, String option) {
        int StartLimit = (requestPage-1)* listPerPage;
        int EndLimit = listPerPage;
        
        List<Map<String, Object>> list = memberDao.selectMemberList(StartLimit, EndLimit, option);// 여기서에러

        return list;
    }
    
    public int getMemberListCount(String option) {
    	return memberDao.selectMemberListCount(option);
    }
    
    public void updateMemberStatus(int memberId, String status) {
  
        memberDao.updateMemberStatus(memberId, status);
    }
    
}
