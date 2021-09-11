package com.icia.wapoo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphDay {
	private String week;		//요일
	private int totalOrder;		//요일별 주문 수
	private int payment;		//요일별 매출 합계
	private int discount;	//할인액
}
