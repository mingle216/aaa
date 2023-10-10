package com.wisedu.amp3compatible.model;

/**
 * Created by glhan on 2016/6/21.
 */
public class Status {
    public static final int OK_CODE = 200;

    public static final int TOO_MANY_RESULT = 201;

    public static final int NO_RESULT_CODE = 202;

    public static final String OK = "success";

    public static final String NO_RESULT = "该操作无相应结果!";

    public static final int PARAM_ERROR_CODE = 400;

    public static final String PARAM_ERROR = "参数错误!请检查";

    public static final int SYSTEM_ERROR_CODE = 500;

    public static final String SYSTEM_ERROR = "系统错误~~~";

    public static final int APPSHORTNAME_ERROR_CODE = 601;

    public static final String ERROR_MSG_ILLEGALC = "搜索条件包含非法字符";

    public static final String PARAM_BATCHIDISNULL = "存在错误数据！(未指定选房批次)";

    public static final String PARAM_GROUPIDISNULL = "存在错误数据！(组团编号为空)";

    public static final String PARAM_STUDENTIDISNULL = "存在错误数据！(学生编号为空)";

    public static final String PARAM_DORMIDISNULL = "存在错误数据！(宿舍编号为空)";

    public static final String PARAM_DATE_ERROR = "日期格式有误！(应为'yyyy-MM-dd')";

    public static final int SYSTEM_FORBIDDEN_IP = 403;

    public static final String SYSTEM_FORBIDDEN = "非法的IP访问!";

    public static final int SYSTEM_DATA_EXIST = 402;

    public static final String SYSTEM_DATA_EXIST_CODE = "accounts already exist";

    public static final String MAIL_ACCOUNT_NOT_CURUSER = "mailAccountNotCurUser";

    private Status () {
    }
}
