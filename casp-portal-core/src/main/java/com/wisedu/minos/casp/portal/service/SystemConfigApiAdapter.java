package com.wisedu.minos.casp.portal.service;

import com.wisedu.minos.casp.portal.model.CommonResponseResult;
import com.wisedu.minos.casp.portal.model.CommonStringInfo;
import com.wisedu.minos.casp.portal.model.configforconsole.ConsoleConfigInfo;

import java.util.List;


public interface SystemConfigApiAdapter {


    /**
    * searchSysParam
    **/
    public CommonResponseResult<List<ConsoleConfigInfo>> searchSysParam (CommonStringInfo body);

    /**
     * modifySysParam
     **/
    public CommonResponseResult modifySysParam(ConsoleConfigInfo body);


}
