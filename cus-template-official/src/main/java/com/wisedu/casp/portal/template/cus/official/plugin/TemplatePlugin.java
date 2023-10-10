package com.wisedu.casp.portal.template.cus.official.plugin;

import com.wisedu.casp.portal.template.cus.official.model.*;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.utils.*;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.service.impl.*;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractTemplate;
import com.wisedu.minos.casp.portal.spi.model.TemplateContext;
import com.wisedu.minos.casp.portal.utils.DataSourceUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.util.jdbc.JDBCTools;
import com.wisedu.minos.util.jdbc.bean.JDBCConnectParamBean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import java.util.*;

/**
 * @author kaisir
 */
@MinosSPI
public class TemplatePlugin extends AbstractTemplate {

    private static final Logger logger = LogManager.getLogger(TemplatePlugin.class);
    @Override
    public String getTemplateId() {
        return TemplateVariable.TEMPLATE_ID;
    }

    @Override
    public String getTemplateConfig(String platformType) {
        return StringUtil.isNotEmpty(platformType)&&platformType.equals(String.valueOf(Global.PlatformType.MOBILE.getType()))?TemplateVariable.MOBILE_DEFAULT_CONFIG:TemplateVariable.PC_DEFAULT_CONFIG;
    }
    @Override
    public Object doExecDispatcher(TemplateAjaxRequest request) {
        Object result = null;
        switch (request.getMethod()) {
            case "getMyTaskCount":{
                result = this.getMyTaskCount();
                break;
            }
            case "getZFWL": {
            	JDBCConnectParamBean bean = ApplicationContextUtil.get(DataSourceUtil.class)
        				.getJDBCConnectParamBeanByName("minos");
        		// final DataSource jdbcConnectParamBean =
        		// ((DataSourceUtil)ApplicationContextUtil.get((Class)DataSourceUtil.class)).getDataSource("MIDZSJ");
        		// final JdbcTemplate jdbcTemplate = new
        		// JdbcTemplate(jdbcConnectParamBean);
        		final UserUtil userUtil = (UserUtil) ApplicationContextUtil.get((Class) UserUtil.class);
        		final UserInfo currentUser = userUtil.getCurrentUser();
        		String[][] param = {};
        		
        		final List<Map<String, String>> resultData = JDBCTools.query(bean,
        				"select count(a.WID) ZFWL from t_amp_view_user_login a ", param);
        		result = resultData.get(0).get("zfwl").toString();
    			break;
    		}
            case "getFWL": {
            	JDBCConnectParamBean bean = ApplicationContextUtil.get(DataSourceUtil.class)
        				.getJDBCConnectParamBeanByName("minos");
        		// final DataSource jdbcConnectParamBean =
        		// ((DataSourceUtil)ApplicationContextUtil.get((Class)DataSourceUtil.class)).getDataSource("MIDZSJ");
        		// final JdbcTemplate jdbcTemplate = new
        		// JdbcTemplate(jdbcConnectParamBean);
        		final UserUtil userUtil = (UserUtil) ApplicationContextUtil.get((Class) UserUtil.class);
        		final UserInfo currentUser = userUtil.getCurrentUser();
        		String[][] param = {};
        		
        		final List<Map<String, String>> resultData = JDBCTools.query(bean,
        				" select count(a.WID) FWL from t_amp_view_user_login a "
        				+ "where to_char(a.CREATE_TIME,'yyyy-mm-dd')=to_char(sysdate,'yyyy-mm-dd')", param);
        		result =  resultData.get(0).get("fwl").toString();
    			break;
    		}
            case "getFRS": {
            	JDBCConnectParamBean bean = ApplicationContextUtil.get(DataSourceUtil.class)
        				.getJDBCConnectParamBeanByName("minos");
        		// final DataSource jdbcConnectParamBean =
        		// ((DataSourceUtil)ApplicationContextUtil.get((Class)DataSourceUtil.class)).getDataSource("MIDZSJ");
        		// final JdbcTemplate jdbcTemplate = new
        		// JdbcTemplate(jdbcConnectParamBean);
        		final UserUtil userUtil = (UserUtil) ApplicationContextUtil.get((Class) UserUtil.class);
        		final UserInfo currentUser = userUtil.getCurrentUser();
        		String[][] param = {};
        		
        		final List<Map<String, String>> resultData = JDBCTools.query(bean,
        				"select count(user_account) FWRS from ( "
        				+ "select a.user_account from t_amp_view_user_login a  "
        				+ "where to_char(a.CREATE_TIME,'yyyy-mm-dd')=to_char(sysdate,'yyyy-mm-dd') "
        				+ "group by a.user_account) x", param);
        		TemplatePlugin.logger.info("==getFRS===="+ resultData.get(0).size());
        		for(String s :resultData.get(0).keySet()){
        			TemplatePlugin.logger.info("==getFRS===="+ s);
        		}
        		result = resultData.get(0).get("fwrs").toString();
    			break;
    		}
        }
        return result;
    }


    @Override
    public void destroy() {
        logger.info("destroy..");
    }

    private String getKeywords(Map<String, String> param, String keywords) {
        String keywords1 = param.get(keywords);
        return StringUtil.xssEncode(StringUtil.defaultIfEmpty(keywords1, ""));
    }




    //TODO 迁移到卡片
    private MyTaskCountResponse getMyTaskCount() {
        String taskCenterUrl = ApplicationContextUtil.get(SystemConfigService.class)
                .getSystemConfigValue("taskcenter_url");
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        MyTaskCountResponse response = new MyTaskCountResponse();

        if (user == null) {
            return response;
        }
        UserIdRequest userIdRequest = new UserIdRequest();
        userIdRequest.setUserId(user.getUserAccount());
        HttpEntity entity = new HttpEntity(userIdRequest);

        TaskTodoAndDoneResponse taskTodoAndDoneResponse = RestTemplateUtils
                .post(taskCenterUrl + "/task/getTaskTodoAndDoneCount.do", entity, TaskTodoAndDoneResponse.class).getBody();
        MyProcessCountResponse myProcessCountResponse = RestTemplateUtils
                .post(taskCenterUrl + "/task/getMyProcessCount.do", entity, MyProcessCountResponse.class).getBody();

        if (null != taskTodoAndDoneResponse && null != taskTodoAndDoneResponse.getData()) {
            response.setTodoCount(taskTodoAndDoneResponse.getData().getTodoCount());
            response.setDoneCount(taskTodoAndDoneResponse.getData().getDoneCount());
        }
        if (null != myProcessCountResponse && null != myProcessCountResponse.getData()) {
            response.setRunningCount(myProcessCountResponse.getData().getRunningCount());
            response.setCompleteCount(myProcessCountResponse.getData().getCompleteCount());
        }

        return response;
    }

}
