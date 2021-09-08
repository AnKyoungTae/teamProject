package com.icia.wapoo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphResntFood {
	private String time;		// 시간
	private String name;		//음식 이름
	private int price;			//가격
	private int quantity;		//수량
	private int	payment;		//총 매출
}
