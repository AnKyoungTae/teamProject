package com.icia.wapoo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminEvent {
	private int num;			//번호
	private	String regDate;		//시작일
	private String dueDate;		//종료기한
	private String couponNumber;//쿠폰번호
	private String couponName;	//쿠폰이름
	private String status;		//상태
	private int couponPrice;	//할인 가격
	private int discountRate;	//할인비율
	private int eventId;		//이벤트 아이다
	
	
}
