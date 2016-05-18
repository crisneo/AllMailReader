package com.allMailReader.core;

public class PagingInfo {
	
	private int pageSize;
	private int startIndex;
	public int getNumberOfPages() {
		return numberOfPages;
	}
	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getLastMailRecovered() {
		return lastMailRecovered;
	}
	public void setLastMailRecovered(int lastMailRecovered) {
		this.lastMailRecovered = lastMailRecovered;
	}
	private int endIndex;
	
	//values to get inversed dates
	private int numberOfPages;
	private int currentPage;
	private int lastMailRecovered;
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

}


