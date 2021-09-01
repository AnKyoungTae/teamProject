package com.icia.wapoo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	int orderId;			//번호
	String orderDate;		//날짜
	char status;			//주문상태
	String storeName;		//가게 이름
	String phone;			//전화번호
	String foodName;		//음식이름
	int quantity;			//음식 수량
	String image;			//가게 이미지
	String orgName;			//이미지 본이름
	
	int member_coupon_id;	

}
