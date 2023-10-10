package com.wisedu.amp.card.cus.serviceItemCategoryDetail.model;

import java.io.Serializable;

public class ServiceItemCategoryResponse extends AmpBaseResponse implements Serializable {

	/*
	 * private Object data; private String traceId;
	 * 
	 * @Override public Object getData() { return data; }
	 * 
	 * @Override public void setData(Object data) { this.data = data; }
	 * 
	 * public String getTraceId() { return traceId; }
	 * 
	 * public void setTraceId(String traceId) { this.traceId = traceId; }
	 */
	private  int totalSize;

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
	
	
}
