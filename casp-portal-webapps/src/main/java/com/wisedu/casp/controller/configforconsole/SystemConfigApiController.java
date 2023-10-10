package com.wisedu.casp.controller.configforconsole;

import com.wisedu.minos.casp.portal.model.CommonResponseResult;
import com.wisedu.minos.casp.portal.model.CommonStringInfo;
import com.wisedu.minos.casp.portal.model.configforconsole.ConsoleConfigInfo;
import com.wisedu.minos.casp.portal.service.SystemConfigApi;
import com.wisedu.minos.casp.portal.service.SystemConfigApiAdapter;
import com.wisedu.minos.gateway.client.annotation.ManagerGatewayApi;
import com.wisedu.minos.gateway.client.util.ApiOperationTypeEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class SystemConfigApiController implements SystemConfigApi {

    @Autowired
    private SystemConfigApiAdapter adapter;


    /**
    * searchSysParam
    **/
    @Override
    @ManagerGatewayApi(name = "管控台查询系统参数列表", realPath = "/sysParam/searchSysParam", agentPath = "/casp-portal/systemConfig/advancedOptions/sysParam/searchSysParam")
    public CommonResponseResult<List<ConsoleConfigInfo>> searchSysParam(CommonStringInfo body ) {
        return adapter.searchSysParam(body);
    }

    /**
     * modifySysParam
     **/
    @Override
    @ManagerGatewayApi(name = "管控台编辑系统参数配置", realPath = "/sysParam/modifySysParam", agentPath = "/casp-portal/systemConfig/advancedOptions/sysParam/modifySysParam")
    public CommonResponseResult modifySysParam( ConsoleConfigInfo body ) {
        return adapter.modifySysParam(body);
    }

}
