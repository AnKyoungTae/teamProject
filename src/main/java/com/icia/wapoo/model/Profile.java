package com.icia.wapoo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile{
	private int memberId;		// 멤버 아이디
	private String loginId;		// 로그인 아이디
	private String name;		// 이름
	private String tel;			// 전화번호
	private String email;			// 이메일
	private String nickname;	// 닉네임
	private String status;		
	private String profileUrl;	//사진
}
