package com.icia.wapoo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberCoupon {
	 private int id;			//테이블 번호
	 private String couponNumber;		//쿠폰 아이디
	

	 private String usedDate;	//사용날짜
	 private int couponPrice; //할인금액
	 private int discountRate;	//할인율
	 
	 private String couponName;		//쿠폰이름
	 private String couponEnd;		//쿠폰 종료일
	 
	 
}
