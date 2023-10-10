package com.wisedu.minos.casp.portal.common.constant;

/**
 * 功能描述： 全局错误码 修改记录:
 *
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title GlobalEnum
 * @Author: jcx
 * @Date: 2020/7/20
 */
public enum GlobalEnum {
	SUCCESS("200", "操作成功"),
	PARAM_FAIL("1003", "参数异常"),
	VALIDATION("1002", "参数校验不通过"),
	DUPLICATE_KEY("1001", "数据重复，请检查后提交"),
	NO_TREAD_ABLE("1007", "请求参数与定义的参数的传递方式不一致，请检查后再重试"),
	ERROR("999", "系统异常，请稍后访问"),
	NO_HANDLER_FOUND("404", "路径不存在，请检查路径是否正确"),
	FILE_TO_BYTE_ERROR("1004", "文件转换文件流失败"),
	UPLOAD_FILE_ERROR("1005", "上传文件失败"),
	DIRECTORY_ERROR("1006", "文件目录不存在"),
	DOWNLOAD_FILE_ERROR("1008", "下载文件失败"),
	EXPORT_EXCEL_ERROR("1009", "导出数据失败"),
	MENU_NOT_FOUND("2000", "菜单不存在"),
	LOGIN_TIME_OUT("2106040302", "登录超时"),
	FILE_ERROR("999", "附件上传失败"),
	FILE_ILLEGAL("999", "附件不合法，请重新上传！"),
	FILE_DOWNLOAD_ERROR("500", "附件下载失败"),
	DELETE_CARD_NULL_PARAM_ERROR("1010", "删除卡片传参不能为空！"),
	DELETE_CARD_NOT_PARAM_ERROR("1011", "删除卡片传参不能为空！"),
	DELETE_CARD_INFO_PARAM_ERROR("1012", "获取卡片信息参数异常！"),
	GET_SIDEBAR_ERROR("1013", "获取侧边栏信息失败！")

	;
	private String code;

	private String msg;

	GlobalEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
