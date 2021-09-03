package com.icia.wapoo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article{
	private long articleId;		// 게시물 PK
	private String regDate;			// 날짜
	private String updateDate;		// 수정 날짜
	private String title;			// 제목
	private String body;			// 내용
	private int hit;				// 조회수
	private String status;			// 상태
	private long children;			//댓글 수
	private long parantId;			// 부모 게시글
	private long boardId;			// 게시판 종류
	private long writerId;			// 작성자
	private String suspend;		//신고내용
	
	private String nickname;		//닉네임

}
