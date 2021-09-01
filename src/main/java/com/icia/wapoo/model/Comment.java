package com.icia.wapoo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
	private int commentId;	//id
	private String regDate;	//날짜
	private String updateDate; //수정 날
	private String status; //상태
	private String body; //내용
	private int articleId; //작성자글
	private long writerId; //작성자 id
	
	private String nickname;//닉네임
}
