package com.wisedu.minos;


import com.wisedu.minos.api.config.LanguageConfigService;
import com.wisedu.minos.api.constant.MinosCommonConstant;
import com.wisedu.minos.api.model.DubboLanguageConfigResp;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.i18n.DubboI18nService;
import com.wisedu.minos.timedtask.GetSupportLanguagesTask;
import com.wisedu.minos.util.scheduler.job.BaseJob;
import com.wisedu.minos.util.scheduler.model.MinosQuartzModel;
import com.wisedu.minos.util.scheduler.service.JobAndTriggerService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Minos启动工具类li
 *
 * @author zhangjian 0116211
 * @create 2019-10-30
 **/
@SpringBootApplication(scanBasePackages = { "com.wisedu.minos", "com.wisedu.casp", "com.wisedu.amp3compatible" })
@ServletComponentScan //Servlet、Filter、Listener可以直接通过@WebServlet、@WebFilter、@WebListener注解自动注册，无需其他代码
@MapperScan({ "com.wisedu.minos.casp.portal.dao" })
@EnableTransactionManagement //开启事务管理注解模式
@EnableDubbo
@EnableAsync
public class Application {

    public static void main (String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * 查询当前门户支持的多语言，并且开启查询多语言定时任务
     */
    @Component
    @Order(1)
    static public class addSupportLanguages implements CommandLineRunner {

        @Autowired
        private JobAndTriggerService jobAndTriggerService;
        @DubboReference
        private LanguageConfigService languageConfigService;

        @Override
        public void run (String... args) throws Exception {
            if(null!=languageConfigService){
                DubboLanguageConfigResp resp = languageConfigService.getAllLanguageConfigs(MinosCommonConstant.FUNCTION_PORTAL,"");
                if("0".equals(resp.getErrcode())){
                    DubboI18nService.resp=resp;
                }
            }
            //一分钟执行一次
            initCreateTask(GetSupportLanguagesTask.class, "0 0/1 * * * ?", "查询当前门户支持的多语言信息");
        }

        /**
         * 创建一个定时任务
         *
         * @param clazz
         * @param cron
         */
        private void initCreateTask (Class<? extends BaseJob> clazz, String cron, String desc) {
            MinosQuartzModel minosQuartzModel = new MinosQuartzModel();
            minosQuartzModel.setJobGroupName(Global.JobType.GET_PORTAL_SUPPORT_LANGUAGES_TASK.getId());
            // 删除操作日志
            minosQuartzModel.setJobTriggerName(clazz.getSimpleName());
            jobAndTriggerService.deleteJob(minosQuartzModel);
            minosQuartzModel.setCronExpression(cron);
            minosQuartzModel.setJobClass(clazz);
            minosQuartzModel.setDesc(desc);
            jobAndTriggerService.addJob(minosQuartzModel);
        }
    }
}
