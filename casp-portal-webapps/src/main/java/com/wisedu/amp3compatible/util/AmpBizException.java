/**
 * 
 */
package com.wisedu.amp3compatible.util;

/**
 * 应用管理平台业务异常类
 * <p>应用管理平台</p>
 * <p>江苏金智教育信息技术有限公司</p>
 * <p>功能说明：在Service层应该统一抛出改异常</p>	
 * @author 丁窍
 * @version 1.0	创建时间：2016年3月23日下午4:24:39	丁窍	发布
 */
public class AmpBizException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5601445246752934372L;

	/**
	 * 
	 */
	public AmpBizException() {
		super();
	}

	/**
	 * @param message
	 */
	public AmpBizException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public AmpBizException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AmpBizException(String message, Throwable cause) {
		super(message, cause);
	}

}
