package com.wisedu.minos.casp.portal.service.impl;

import com.wisedu.minos.casp.portal.service.CommonApiAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 功能描述：线程基类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ExecutorService
 * @Author: jcx
 * @Date: 2020/9/25
 */
@Service
public class ExecutorService {

    private static final Logger logger = LogManager.getLogger(ExecutorService.class);

    @Autowired
    private CommonApiAdapter commonApiAdapter;

    /**
     * @Author jcx
     * @Description 重启系统
     * @Date 17:02 2020/9/25
     * @return void
     **/
    @Async("asyncExecutor")
    public void restartSystem(){
        logger.info("重启系统------------");
        commonApiAdapter.restartService();
    }

    @Async("asyncExecutor")
    public void closeSystem(){
        logger.info("关闭进程------------");
        System.exit(0);
    }

}
