package com.icia.wapoo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphFood {
	private int rowNum;		//순위
	private String name;	//이름
	private int quantity;	//판매량
	private int price;		//매출
	private int discount;	//할인액
}
