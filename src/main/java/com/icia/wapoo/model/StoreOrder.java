package com.icia.wapoo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreOrder {
	private String name;		//메뉴이름
	private int storeId;		//가게 번호
	private int price;			//음식 가격
	private int quantity;		//음식 수량
	private String status;		//주문 상태
	private String orderDate;	//주문 날짜
	private int totalPrice;		//총 금액
	private int discount;		//할인 금액
	private int payment;		//결재된 금액,
	private String address;		//주소
	private String phone;		//전화번호
	private String requests;	//요청사항
	private int orderId;		//주문번호
	private int orderInfoId;	//orderInfo 키값
}
