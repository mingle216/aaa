package com.wisedu.minos;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wisedu.minos.api.config.LanguageConfigService;
import com.wisedu.minos.api.constant.MinosCommonConstant;
import com.wisedu.minos.api.model.DubboLanguageConfigResp;
import com.wisedu.minos.casp.portal.PortalManagerProperties;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.redis.RedisUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.OldOssEntity;
import com.wisedu.minos.casp.portal.dao.entity.ShowProgrammeEntity;
import com.wisedu.minos.casp.portal.dao.entity.SiteEntity;
import com.wisedu.minos.casp.portal.dao.entity.SystemConfigEntity;
import com.wisedu.minos.casp.portal.dao.mapper.OldOssMapper;
import com.wisedu.minos.casp.portal.dao.mapper.SiteMapper;
import com.wisedu.minos.casp.portal.i18n.DubboI18nService;
import com.wisedu.minos.casp.portal.service.PersonalDataApiAdapter;
import com.wisedu.minos.casp.portal.service.impl.ProgrammeApiAdapterImpl;
import com.wisedu.minos.casp.portal.service.impl.ShowProgrammeService;
import com.wisedu.minos.casp.portal.service.impl.SystemConfigService;
import com.wisedu.minos.casp.portal.utils.CommunalUtil;
import com.wisedu.minos.timedtask.CalWeeklyRemindTask;
import com.wisedu.minos.util.scheduler.job.BaseJob;
import com.wisedu.minos.util.scheduler.model.MinosQuartzModel;
import com.wisedu.minos.util.scheduler.service.JobAndTriggerService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

import static com.wisedu.minos.casp.portal.common.constant.Global.MASTER_SITE_WID;

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
        MDC.put("type","casp-portal-startLog");
        SpringApplication.run(Application.class, args);
        MDC.remove("type");
    }

    @Component
    @Order(2)
    static class RefreshMybatisCache implements ApplicationRunner {
        private static final Logger logger = LogManager.getLogger(Application.class);
        @Autowired
        private RedisUtil redisUtil;

        @Override
        public void run (ApplicationArguments args) throws Exception {
            Set<String> cacheKeys = redisUtil.scan("com.wisedu.minos.casp.portal.dao.mapper");
            if( !CollectionUtils.isEmpty(cacheKeys) ) {
                logger.info("====================开始清空mybatis二级缓存:{}====================", cacheKeys.size());
                cacheKeys.forEach(item -> {
                    redisUtil.del(item);
                });
                logger.info("====================清空mybatis二级缓存完成====================");
            }
        }
    }

    /**
     * 查询当前门户支持的多语言，并且开启查询多语言定时任务
     */
    @Component
    @Order(1)
    static class AddSupportLanguages implements CommandLineRunner {

        @Autowired
        private JobAndTriggerService jobAndTriggerService;
        @DubboReference
        private LanguageConfigService languageConfigService;
        @Autowired
        private OldOssMapper oldOssMapper;
        @Autowired
        private RedisUtil redisUtil;
        @Value("${cal.weekly.quartz}")
        private String  weeklyRemindQuartz;

        @Override
        public void run (String... args) throws Exception {
            if(null!=languageConfigService){
                DubboLanguageConfigResp resp = languageConfigService.getAllLanguageConfigs(MinosCommonConstant.FUNCTION_PORTAL,"");
                if("0".equals(resp.getErrcode())){
                    DubboI18nService.setResp(resp);
                }
            }
//            //一分钟执行一次
//            initCreateTask(GetSupportLanguagesTask.class, "0 0/1 * * * ?", "查询当前门户支持的多语言信息", Global.JobType.GET_PORTAL_SUPPORT_LANGUAGES_TASK.getId());
            deleteJob("GetSupportLanguagesTask",Global.JobType.GET_PORTAL_SUPPORT_LANGUAGES_TASK.getId());
            // 一小时执行一次 0 0 * * * ? *
            initCreateTask(CalWeeklyRemindTask.class, weeklyRemindQuartz,"日程周提醒", Global.JobType.CAL_WEEKLY_REMIND_TASK.getId());

            //启动先执行一次，之后30分钟执行一次
            PortalManagerProperties.setPersonCenterUrlSelect();
            PortalManagerProperties.setBackendManageUrlSelect();
            PortalManagerProperties.setSystemMenuEnable();
//            initCreateTask(PortalManagerTask.class, "* 0/30 * * * ? ","查询公共属性", Global.JobType.PORTAL_MANAGER_PROPERTIES_TASK.getId());
            deleteJob("PortalManagerTask",Global.JobType.PORTAL_MANAGER_PROPERTIES_TASK.getId());
            List<OldOssEntity> oldOssUrls = oldOssMapper.selectList(null);
            if(!CollectionUtils.isEmpty(oldOssUrls)){
                redisUtil.save(Global.OLD_OSS_URL_DATA,oldOssUrls);
            }
        }

        public void deleteJob (String classSimpleName,String jobGroupName) {
            MinosQuartzModel minosQuartzModel = new MinosQuartzModel();
            minosQuartzModel.setJobGroupName(jobGroupName);
            // 删除操作日志
            minosQuartzModel.setJobTriggerName(classSimpleName);
            jobAndTriggerService.deleteJob(minosQuartzModel);
        }

        /**
         * 创建一个定时任务
         *
         * @param clazz
         * @param cron
         */
        private void initCreateTask (Class<? extends BaseJob> clazz, String cron, String desc, String jobGroupName) {
            MinosQuartzModel minosQuartzModel = new MinosQuartzModel();
            minosQuartzModel.setJobGroupName(jobGroupName);
            // 删除操作日志
            minosQuartzModel.setJobTriggerName(clazz.getSimpleName());
            jobAndTriggerService.deleteJob(minosQuartzModel);
            minosQuartzModel.setCronExpression(cron);
            minosQuartzModel.setJobClass(clazz);
            minosQuartzModel.setDesc(desc);
            jobAndTriggerService.addJob(minosQuartzModel);
        }
    }


    /**
     * 3.3.1.Beta2 设置默认展示方案默认站点
     */
    @Component
    @Order(2)
    static public class ShowProgrammeInitForMasterSite implements CommandLineRunner {

        @Autowired
        private ShowProgrammeService showProgrammeService;

        @Autowired
        private SiteMapper siteMapper;

        @Override
        public void run(String... args) throws Exception {
            //插入主站点
            SiteEntity siteEntity = siteMapper.selectById(MASTER_SITE_WID);
            if(siteEntity == null){
                siteEntity = new SiteEntity();
                siteEntity.setWid(MASTER_SITE_WID);
                siteEntity.setIsMaster(1);
                //获取启用的站点
                siteEntity.setSiteName(getDefaultProgramme().getProgrammeName());
                siteEntity.setSiteNameLangKey(CommunalUtil.getWid());
                siteEntity.setSiteRoute("default");
                siteEntity.setAuthType(2);
                siteEntity.setOrderIndex(1);
                siteEntity.setIsEnabled(1);
                siteEntity.setIsDeleted(0);
                siteMapper.insert(siteEntity);
            }else{
                if(StringUtil.isEmpty(siteEntity.getSiteNameLangKey())){
                    siteEntity.setSiteNameLangKey(CommunalUtil.getWid());
                    siteMapper.updateById(siteEntity);
                }
            }
            LambdaUpdateWrapper<ShowProgrammeEntity> wrapper = new LambdaUpdateWrapper<>();
            wrapper.isNull(ShowProgrammeEntity::getSiteWid)
                    .eq(ShowProgrammeEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                    .set(ShowProgrammeEntity::getSiteWid, MASTER_SITE_WID);
            showProgrammeService.update(wrapper);
        }

        public ShowProgrammeEntity getDefaultProgramme() {
            List<ShowProgrammeEntity> list = showProgrammeService.list(Wrappers.<ShowProgrammeEntity>lambdaQuery()
                    .eq(ShowProgrammeEntity::getPageStatus, Global.PageStatus.ENABLE.getId())
                    .eq(ShowProgrammeEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                    .eq(ShowProgrammeEntity::getPlatformType, Global.PlatformType.PC.getType()));
            if (list.isEmpty()) {
                throw new BusinessException("未获取到默认展示方案");
            }
            return list.get(0);
        }
    }


    /**
     * 启动时，预加载个人数据到redis
     */
    @Component
    @Order(400)
    static public class LoadPersonalInfo implements CommandLineRunner {
        @Autowired
        private PersonalDataApiAdapter personalDataApiAdapter;
        @Override
        public void run(String... args) throws Exception {
            personalDataApiAdapter.loadPersonalDataToRedis();
        }
    }


    /***
     * @Author jcx
     * @Description 迁移数下拉菜单数据到新表
     * @Date 19:01 2022/9/28
     **/
    @Component
    @Order(401)
    static public class LoadSelectMenuInfo implements CommandLineRunner {

        private static final Logger logger = LogManager.getLogger(LoadSelectMenuInfo.class);
        @Autowired
        private SystemConfigService systemConfigService;
        @Autowired
        private ProgrammeApiAdapterImpl programmeApiAdapter;

        private final String loadSelectMenuInfoKey="LOAD_SELECT_MENUINFO_KEY";
        @Override
        public void run(String... args) throws Exception {
            SystemConfigEntity systemConfig = systemConfigService.getSystemConfig(loadSelectMenuInfoKey);
            if(null==systemConfig){
                try {
                    programmeApiAdapter.loadSelectMenu("");
                    systemConfig=new SystemConfigEntity();
                    systemConfig.setConfigKey(loadSelectMenuInfoKey);
                    systemConfig.setConfigDesc("迁移数下拉菜单数据到新表标志位");
                    systemConfigService.save(systemConfig);
                } catch ( Exception e ) {
                    logger.error("迁移数下拉菜单数据到新表标志位异常：{}",e);
                }
            }
        }
    }
}
