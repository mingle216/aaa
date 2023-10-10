package com.wisedu.amp.card.cus.serviceItemCategoryDetail.model;


import java.io.Serializable;

public class ConfigInfo implements Serializable {

    private String[] tabConfigure;

    private Integer[] sortType;

    private Integer hasIcon;

    private Integer notLogin;
    private Integer logined;

	private String[] displayInfo;

	private String [] quickConfig;

	private Integer otherClassIcon;

	private Integer departClassIcon;

	public String[] getQuickConfig () {
		return quickConfig;
	}

	public void setQuickConfig (String[] quickConfig) {
		this.quickConfig = quickConfig;
	}

	public Integer getNotLogin () {
		return notLogin;
	}

	public void setNotLogin (Integer notLogin) {
		this.notLogin = notLogin;
	}

	public Integer getLogined () {
		return logined;
	}

	public void setLogined (Integer logined) {
		this.logined = logined;
	}

	public String[] getTabConfigure() {
		return tabConfigure;
	}

	public void setTabConfigure(String[] tabConfigure) {
		this.tabConfigure = tabConfigure;
	}

	public Integer[] getSortType() {
		return sortType;
	}

	public void setSortType(Integer[] sortType) {
		this.sortType = sortType;
	}

	public Integer getHasIcon() {
		return hasIcon;
	}

	public void setHasIcon(Integer hasIcon) {
		this.hasIcon = hasIcon;
	}

	public String[] getDisplayInfo() {
		return displayInfo;
	}

	public void setDisplayInfo(String[] displayInfo) {
		this.displayInfo = displayInfo;
	}

	public Integer getOtherClassIcon () {
		return otherClassIcon;
	}

	public void setOtherClassIcon (Integer otherClassIcon) {
		this.otherClassIcon = otherClassIcon;
	}

	public Integer getDepartClassIcon () {
		return departClassIcon;
	}

	public void setDepartClassIcon (Integer departClassIcon) {
		this.departClassIcon = departClassIcon;
	}
}
