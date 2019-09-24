package com.test.myapp.domain;

public class Criteria {
	private int page; //페이지수
	private int perPageNum; //페이지당 글의 수
	
	public Criteria(){
		//기본 생성자
		this.page = 1;
		this.perPageNum = 10; 
	}
	
	public void setPage(int page){
		if(page <= 0){
			this.page = 1;
			return;
		}
		this.page = page;
	}
	
	public void setPerPageNum(int perPageNum){
		if(perPageNum <= 0 || perPageNum > 100){
			this.perPageNum = 10;
			return;
		}
		this.perPageNum = perPageNum;
	}

	public int getPage(){
		return page;
	}
	
	//MyBatis Mapper를 위한 매서드
	public int getPageStart(){
		return (page - 1) * perPageNum; 
	}
	
	//MyBatis Mapper를 위한 매서드
	public int getPerPageNum(){
		return perPageNum;
	}
	
	@Override
	public String toString(){
		return "조건[page=" + page + ", perPageNum=" + perPageNum + "]"; 
	}
}
