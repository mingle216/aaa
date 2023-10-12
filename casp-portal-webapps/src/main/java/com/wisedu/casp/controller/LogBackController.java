package com.wisedu.casp.controller;

import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.exception.NoLoginException;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title LogBackController
 * @Author: 01120034
 * @Date: 2021/9/15
 */
@RestController
public class LogBackController {

    @Autowired
    UserUtil userUtil;
    /**
     * logback动态修改包名的日志级别
     * @param level 日志级别
     * @param packageName 包名
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/setLogLevel")
    public ResultData updateLogbackLevel(@RequestParam(value="level",defaultValue = "info") String level,
                                         @RequestParam(value="packageName",defaultValue = "com.wisedu") String packageName)throws Exception{

        UserInfo currentUser = userUtil.getCurrentUser();

        if(null==currentUser){
            throw new NoLoginException("请登录！");
        }
        ch.qos.logback.classic.LoggerContext loggerContext =(ch.qos.logback.classic.LoggerContext) LoggerFactory.getILoggerFactory();
        if("-1".equals(packageName)) {
            // 默认值-1，更改全局日志级别；否则按传递的包名或类名修改日志级别。
            loggerContext.getLogger("root").setLevel(ch.qos.logback.classic.Level.toLevel(level));
        } else {
            loggerContext.getLogger(packageName).setLevel(ch.qos.logback.classic.Level.valueOf(level));
        }
        return ResultData.success("修改日志级别成功");
    }
}
