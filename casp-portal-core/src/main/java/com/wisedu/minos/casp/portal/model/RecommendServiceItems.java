package com.wisedu.minos.casp.portal.model;


public class RecommendServiceItems {
	private String itemWid;
	private String itemName;
	private String iconLink;
	private String itemCategory;
	private String itemDept;
	private int visitCount;
	private double score;
	private boolean favorite;

	public String getItemWid () {
		return itemWid;
	}

	public void setItemWid (String itemWid) {
		this.itemWid = itemWid;
	}

	public String getItemName () {
		return itemName;
	}

	public void setItemName (String itemName) {
		this.itemName = itemName;
	}

	public String getIconLink () {
		return iconLink;
	}

	public void setIconLink (String iconLink) {
		this.iconLink = iconLink;
	}


	public String getItemCategory () {
		return itemCategory;
	}

	public void setItemCategory (String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getItemDept () {
		return itemDept;
	}

	public void setItemDept (String itemDept) {
		this.itemDept = itemDept;
	}

	public int getVisitCount () {
		return visitCount;
	}

	public void setVisitCount (int visitCount) {
		this.visitCount = visitCount;
	}

	public double getScore () {
		return score;
	}

	public void setScore (double score) {
		this.score = score;
	}

	public boolean isFavorite () {
		return favorite;
	}

	public void setFavorite (boolean favorite) {
		this.favorite = favorite;
	}
}
