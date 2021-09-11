package com.icia.wapoo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminEvent {
	private int num;			//번호
	private int eventId;		//이벤트 아이다
	private	String eventRegDate;		//event시작일
	private String eventDueDate;		//event종료기한
	private String status;		//상태
	private String body;		//event 내용
	private String title;		//event 제목
	private String couponNumber;//쿠폰번호
	private String couponName;	//쿠폰이름
	
	private int couponPrice;	//할인 가격
	private String publishedDate;//쿠폰 등록일
	private String couponEnd;	//쿠폰 만료일
	
	private int discountRate;	//할인비율
	
	
	
}
