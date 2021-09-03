package com.icia.wapoo.paging;

import com.icia.wapoo.model.Article;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingA {
	private int listSize ;                //리스트 목록개수

	private int rangeSize ;            		//숫자 페이지수
	
	private int range;						//이전, 다음 버튼 관리자

	private int page;						// 현재 페이지

	private int total;						// 총 게시물의 개수

	private int totalpage;					//총 페이지 수

	private int startPage;					//시작번호
	
	private int endPage;					// 각 페이지 범위 끝 번호

	private int startList;					//DB게시판
	
	private int	DBsStart;					//DB시작 번호

	private boolean prev;					//이전 페이지

	private boolean next;					//다음페이지
	
	

	public void pageInfo(int page, int range,int listSize, int rangeSize, int total) {

		this.page = page;

		this.range = range;

		this.total = total;

		this.listSize = listSize;
		
		this.rangeSize = rangeSize;

		//전체 페이지수 

		this.totalpage = (int) Math.ceil((double)total/listSize);
		
		

		//시작 페이지

		this.startPage = (range-1) * rangeSize + 1 ;

		

		//끝 페이지

		this.endPage = range * rangeSize ;

				

		//DB게시판 시작번호

		this.startList = (page - 1) * listSize;
		
		
		//DB 시작번호
		this.DBsStart = this.total-10* (page);
		
		if(this.total -10*(page) < 0)
		{
			this.DBsStart = 0;
			this.listSize = 10 - (-(this.total-10* (page)));
		}
		
		//이전 버튼 상태

		this.prev = range == 1 ? false : true;
		//this.prev = page == 1 ? false : true;
		
		

		

		//다음 버튼 상태

		this.next = endPage  > totalpage ? false : true;

		if (this.endPage > this.totalpage) {

			this.endPage = this.totalpage;

			this.next = false;

		}

	}

	

	
	
}
