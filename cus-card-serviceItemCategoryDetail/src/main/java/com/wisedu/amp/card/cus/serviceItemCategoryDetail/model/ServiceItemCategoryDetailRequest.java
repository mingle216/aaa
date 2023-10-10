package com.wisedu.amp.card.cus.serviceItemCategoryDetail.model;

public class ServiceItemCategoryDetailRequest {

	// 用户id 游客为guest
	private String userId;
	// 用户账号
	private String userAccount;

	// 是否与我相关
	private int isRelate;

	// 服务分类wid
	private String categoryWid;

	// 服务对象wid
	private String roleWid;

	private int pageSize;

	private int pageNumber;

	private String visitOrder;// 访问量排序

	private boolean authority;// 可在线办理

	private String searchKey;// 查询关键词

	private String lang;

	private boolean mobileOnline; // 可移动端在线办理

	public String getUserAccount () {
		return userAccount;
	}

	public void setUserAccount (String userAccount) {
		this.userAccount = userAccount;
	}

	public boolean isAuthority() {
		return authority;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getIsRelate() {
		return isRelate;
	}

	public void setIsRelate(int isRelate) {
		this.isRelate = isRelate;
	}

	public String getCategoryWid() {
		return categoryWid;
	}

	public void setCategoryWid(String categoryWid) {
		this.categoryWid = categoryWid;
	}

	public String getRoleWid() {
		return roleWid;
	}

	public void setRoleWid(String roleWid) {
		this.roleWid = roleWid;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getVisitOrder() {
		return visitOrder;
	}

	public void setVisitOrder(String visitOrder) {
		this.visitOrder = visitOrder;
	}

	public boolean getAuthority() {
		return authority;
	}

	public void setAuthority(boolean authority) {
		this.authority = authority;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public boolean isMobileOnline () {
		return mobileOnline;
	}

	public void setMobileOnline (boolean mobileOnline) {
		this.mobileOnline = mobileOnline;
	}
}
