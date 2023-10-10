package com.wisedu.amp.card.cus.recommendServiceItems.model;

public class HotServiceItemsRequest {
	    private String userId;
	    private String userType;
	    private String type;

		/*
		 * private Integer pageSize; private Integer pageNumber;
		 */
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getUserType() {
			return userType;
		}
		public void setUserType(String userType) {
			this.userType = userType;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		/*
		 * public Integer getPageSize() { return pageSize; } public void
		 * setPageSize(Integer pageSize) { this.pageSize = pageSize; } public Integer
		 * getPageNumber() { return pageNumber; } public void setPageNumber(Integer
		 * pageNumber) { this.pageNumber = pageNumber; }
		 */
	    
}
