package com.wisedu.minos.casp.portal.utils;

import com.wisedu.minos.api.data.DataSourceService;
import com.wisedu.minos.api.model.DubboDataSourceInfo;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.utils.JacksonUtil;
import com.wisedu.minos.util.jdbc.bean.JDBCConnectParamBean;
import com.wisedu.minos.util.jdbc.bean.MybatisDataSourceParamBean;
import com.wisedu.minos.util.jdbc.mybatis.MybatisTools;
import com.wisedu.minos.util.jdbc.mybatis.SqlSessionContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class DataSourceUtil {

    @DubboReference
    private DataSourceService dataSourceService;

    public List<DubboDataSourceInfo> getDataSources() {
        return dataSourceService.getDataSources();
    }

    public JDBCConnectParamBean getJDBCConnectParamBeanByName(String dataSourceName){
        DubboDataSourceInfo dubboDataSourceInfo = this.getDataSources().stream().
                filter(info -> info.getName().equals(dataSourceName))
                .findFirst().orElseThrow(() -> new RuntimeException("未找到对应数据源：" + dataSourceName));

        return new JDBCConnectParamBean(dubboDataSourceInfo.getDbDriver(), dubboDataSourceInfo.getDbUrl(), dubboDataSourceInfo.getUserName(), dubboDataSourceInfo.getUserPwd());
    }

    public SqlSessionContext getSqlSessionContext(String dataSourceName){
        removeDatasource(dataSourceName);

        MybatisDataSourceParamBean mybatisDataSourceParamBean = new MybatisDataSourceParamBean();
        mybatisDataSourceParamBean.setDataSourceId(dataSourceName);
        List<DubboDataSourceInfo> dataSources = this.getDataSources();
        DubboDataSourceInfo dubboDataSourceInfo = dataSources.stream().
                filter(info -> info.getName().equals(dataSourceName))
                .findFirst().orElseThrow(() -> new RuntimeException("未找到对应数据源：" + dataSourceName));
        LOGGER.debug("获取到的外部数据源信息："+ JacksonUtil.toJson(dubboDataSourceInfo));
        mybatisDataSourceParamBean.setDataSourceName(dubboDataSourceInfo.getName());
        mybatisDataSourceParamBean.setUsername(dubboDataSourceInfo.getUserName());
        mybatisDataSourceParamBean.setPassword(dubboDataSourceInfo.getUserPwd());
        mybatisDataSourceParamBean.setJdbcDriver(dubboDataSourceInfo.getDbDriver());
        mybatisDataSourceParamBean.setJdbcUrl(dubboDataSourceInfo.getDbUrl());
        mybatisDataSourceParamBean.setReadOnly(dubboDataSourceInfo.getReadonly());

        return MybatisTools.getSqlSessionContext(mybatisDataSourceParamBean);
    }

    private void removeDatasource(String dataSourceName) {
        Class<MybatisTools> mybatisToolsClass = MybatisTools.class;
        Field sessionContextMap;
        try {
            sessionContextMap = mybatisToolsClass.getDeclaredField("sessionContextMap");
        } catch (NoSuchFieldException e) {
            throw new BusinessException("",e);
        }
        sessionContextMap.setAccessible(true);
        Map<String, SqlSessionContext> fieldValue = (Map<String, SqlSessionContext> ) ReflectionUtils.getField(sessionContextMap, mybatisToolsClass);
        fieldValue.remove(dataSourceName);
    }
}
