package com.icia.wapoo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.icia.wapoo.model.LoginInfo;
import com.icia.wapoo.model.Member;
import com.icia.wapoo.model.MemberCoupon;
import com.icia.wapoo.model.Order;
import com.icia.wapoo.model.Profile;
import com.icia.wapoo.model.Review;
import com.icia.wapoo.model.Store;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ProfileDao {

	//profileMapper랑 연결됨 
	//@Param = #{memberId}
	//Profile = resultType
	//parameterType = 여러개 가져와야할때 지정
	Profile checkProfile(@Param("memberId") int memberId);
	//프로필 변경
	int insertProfile(Profile profile);
	//비밀번호 변경
	int insertPassWord(LoginInfo loginInfo);
	//회원 확인 하기
	LoginInfo deleteProfileCheck(int member_id,String password);
	//탈퇴하기
	int deleteProfile(int member_id);
	//쿠폰 정보가져오기
	List<Map<String, Object>> getCoupon(int memberId);
	//주문정보 가저오기
	List<Map<String, Object>> selectAllOrders(@Param("memberId") Integer memberId);
	//이미지 업로드
	int insertImage(String fileURL, int memberId);

	String selectFileUrl(@Param("memberId") Integer memberId);
	//계정 확인
	Member getMember(String name, String email) throws Exception;
	//계정확인2
	LoginInfo getLoginInfo(String loginId) throws Exception;
	//아이디 계정확인
	LoginInfo getMemberId(int memberId);
	
	//리뷰 가져오기
	List<Map<String, Object>> selectMyReview(@Param("memberId") Integer memberId);
	//리뷰 삭제
	int updateReviewStatus(@Param("reviewId") Integer reviewId, @Param("status") String state);
}
