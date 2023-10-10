/**
 * 
 */
package com.wisedu.amp3compatible.model;

import java.io.Serializable;

/**
 * @author 丁窍
 *
 */
public class IdentityToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1269842793264251047L;
	
	/**
	 * 用户ID
	 */
	private String userId;
	
	/**
	 * 用户组ID
	 */
	private String groupId;
	
	/**
	 * @param userId
	 * @param groupId
	 */
	public IdentityToken(String userId, String groupId) {
		this.userId = userId;
		this.groupId = groupId;
	}

	/**
	 * userId == null or userId = "" 可以认为是匿名用户
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Override
	public String toString() {
		return "IdentityToken [userId=" + userId + ", groupId=" + groupId + "]";
	}
	
}
