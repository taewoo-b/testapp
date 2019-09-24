package com.test.myapp.domain;

public class PageMaker {
	private int totalCount;
	private int startPage;
	private int endPage;
	private boolean prev;
	private boolean next;
	
	private int displayPageNum = 10;
	
	private Criteria cri;
	
	public void setCri(Criteria cri){
		this.cri = cri;
	}
	public Criteria getCri(){
		return cri;
	}
	
	private void calculateData(){
		endPage = (int)(Math.ceil(cri.getPage() / (double) displayPageNum) * displayPageNum);
		startPage = (endPage - displayPageNum) + 1;
		
		int tempEndPage = (int) (Math.ceil(totalCount / (double)cri.getPerPageNum()));
		
		if(endPage > tempEndPage){
			endPage = tempEndPage;
		}
		prev = startPage == 1 ? false : true;
		next = endPage * cri.getPerPageNum() >= totalCount ? false : true;
	}
	
	public void setTotalCount(int totalCount){
		this.totalCount = totalCount;
		calculateData();
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	public boolean isPrev() {
		return prev;
	}
	public void setPrev(boolean prev) {
		this.prev = prev;
	}
	public boolean isNext() {
		return next;
	}
	public void setNext(boolean next) {
		this.next = next;
	}
	public int getDisplayPageNum() {
		return displayPageNum;
	}
	public void setDisplayPageNum(int displayPageNum) {
		this.displayPageNum = displayPageNum;
	}
	public int getTotalCount() {
		return totalCount;
	}
	
	@Override
	public String toString(){
		return "PageMaker[totalCount=" + totalCount + ", startPage=" + startPage +
				", endPage=" + endPage + ", prev=" + prev + ", next=" + next + "]";
	}
	
	public String makeQuery(int page){
		String returnStr = "" + page;
		
		if (cri.getPerPageNum() != 10){
			returnStr += "" + cri.getPerPageNum();
		}
		
		return returnStr;
	}

	public String makeSearch(int page){
		String returnStr = makeQuery(page);
		String sType = ((SearchCriteria)cri).getSearchType();
		String keyword = ((SearchCriteria)cri).getKeyword();
		if(sType != null)
			returnStr += "?searchType=" + sType;
		if(keyword != null)
			returnStr += "&keyword=" + keyword;
		
		return returnStr;
	}


	
}
