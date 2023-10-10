package com.wisedu.minos.timedtask;

import com.wisedu.minos.casp.portal.PortalManagerProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title PortalManagerJob
 * @Author: 01120034
 * @Date: 2022/11/1
 */
@Component
public class PortalManagerJob {

    /***
     * @Author jcx
     * @Description 30分钟执行一次  查询公共属性
     * @Date 17:14 2022/10/28
     * @return void
     **/
    @Scheduled(cron = "* 0/30 * * * ?")
    public void execute(){
        PortalManagerProperties.setPersonCenterUrlSelect();
        PortalManagerProperties.setBackendManageUrlSelect();
        PortalManagerProperties.setSystemMenuEnable();
    }
}
