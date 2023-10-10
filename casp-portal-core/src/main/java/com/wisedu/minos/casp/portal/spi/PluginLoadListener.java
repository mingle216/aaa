package com.wisedu.minos.casp.portal.spi;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wisedu.minos.casp.portal.common.constant.DbFieldConstant;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.utils.FileUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.common.utils.ZipUtil;
import com.wisedu.minos.casp.portal.dao.entity.AttachInfoEntity;
import com.wisedu.minos.casp.portal.dao.entity.PluginInstallEntity;
import com.wisedu.minos.casp.portal.dao.entity.PluginsEntity;
import com.wisedu.minos.casp.portal.i18n.I18nService;
import com.wisedu.minos.casp.portal.CasProperties;
import com.wisedu.minos.casp.portal.service.impl.*;
import com.wisedu.minos.casp.portal.utils.AuthUtil;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.MailUtil;
import com.wisedu.minos.casp.portal.utils.TemplateUtil;
import com.wisedu.minos.gateway.client.util.WecIpUtil;
import com.wisedu.minos.oss.client.bean.OSSFileBean;
import com.wisedu.minos.oss.client.util.OSSClientUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


/**
 * PluginLoadListener
 * <p/>
 * spi插件加载监听器
 * @author hyluan
 * @date 2020/10/13 11:50
 * Copyright (c) 2018 wisedu
 */
@Component
public class PluginLoadListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LogManager.getLogger(PluginLoadListener.class);

    @Autowired
    ServerProperties serverProperties;

    @Autowired
    CasProperties casProperties;

    @Autowired
    private Environment environment;
    @Autowired
    private ConfigurableApplicationContext context;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Map<String, Object> param = new HashMap<>();
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        param.put("ctxPath", StringUtil.defaultIfEmpty(serverProperties.getServlet().getContextPath(), ""));
        param.put("ctxInternalPath", casProperties.getModule().getDomain());
        param.put("environment", environment);
        param.put("i18nService", applicationContext.getBean(I18nService.class));
        param.put("applicationContext", applicationContext);


        PluginService pluginService = applicationContext.getBean(PluginService.class);
        PluginInstallService pluginInstallService = applicationContext.getBean(PluginInstallService.class);
        List<PluginsEntity> plugins = pluginService.list(new QueryWrapper<PluginsEntity>().eq(DbFieldConstant.IS_DELETED, Global.DeleteStatus.NO.getId()));
        logger.info("plugins----------------"+ JSON.toJSONString(plugins));
        List<PluginInstallEntity> pluginInstalls = pluginInstallService.list(new QueryWrapper<PluginInstallEntity>());
        //本地IP
        String localIpAddress = WecIpUtil.getLocalIpAddress("");
        logger.info("localIpAddress----------------"+ JSON.toJSONString(localIpAddress));
        List<PluginInstallEntity> pluginInstallResult = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(pluginInstalls)){
            //过滤出有本机ip安装的包
            pluginInstallResult = pluginInstalls.stream().filter(pluginInstall -> localIpAddress.equals(pluginInstall.getIpAddress())).collect(Collectors.toList());
            logger.info("pluginInstallResult----------------"+ JSON.toJSONString(pluginInstallResult));
        }
        if(CollectionUtils.isNotEmpty(pluginInstallResult)){
            List<PluginInstallEntity> finalPluginInstallResult = pluginInstallResult;
            //查看是否本机有没安装的发布包
            plugins=plugins.stream().filter(plugin -> !finalPluginInstallResult.stream().map(PluginInstallEntity::getPluginWid).collect(Collectors.toList()).contains(plugin.getWid())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(plugins)){
                logger.info("plugins1----------------"+ JSON.toJSONString(plugins));
                publish(plugins,pluginInstallService,localIpAddress,applicationContext);
            }
        }else{
            logger.info("plugins2----------------"+ JSON.toJSONString(plugins));
            publish(plugins,pluginInstallService,localIpAddress,applicationContext);
        }

        logger.info("==============卡片插件初始化开始===============");
        applicationContext.getBean(CardUtil.class).getCardMap().forEach((key, value) -> {
            logger.info("==============加载到卡片===============" + key);
            value.initialize(param);
        });
        logger.info("==============卡片插件初始化完成===============");

        logger.info("==============模板插件初始化开始===============");
        applicationContext.getBean(TemplateUtil.class).getTemplateMap().forEach((key, value) -> {
            logger.info("==============加载到模板===============" + key);
            value.initialize(param);
        });
        logger.info("==============模板插件初始化完成===============");

        logger.info("==============邮箱插件初始化开始===============");
        applicationContext.getBean(MailUtil.class).getMailMap().forEach((key, value) -> {
            logger.info("==============加载到邮箱===============" + key);
            value.initialize(param);
        });
        logger.info("==============邮箱插件初始化完成===============");

        logger.info("==============认证插件初始化开始===============");
        applicationContext.getBean(AuthUtil.class).getAuthMap().forEach((key, value) -> {
            logger.info("==============加载到认证插件===============" + key);
            value.initialize(param);
        });
        logger.info("==============认证插件初始化完成===============");
    }

    /***
     * @Author jcx
     * @Description 发布
     * @Date 18:43 2021/6/7
     * @Param plugins:
     * @Param pluginInstallService:
     * @Param localIpAddress:
     * @return void
     **/
    private void publish(List<PluginsEntity> plugins,PluginInstallService pluginInstallService,String localIpAddress,ApplicationContext applicationContext) {
        List<PluginInstallEntity> pluginInstallTools = new ArrayList<>();
        boolean flag=true;
        List<String> delPaths = new ArrayList<>();
        try {
            String templateUploadPath = environment.getProperty("file.templateUploadPath");
            String cardUploadPath = environment.getProperty("file.cardUploadPath");
            logger.info("templateUploadPath----------------"+ templateUploadPath);
            logger.info("cardUploadPath----------------"+ cardUploadPath);
            //有没安装的发布包
            for (PluginsEntity plugin : plugins) {
                String jarName=plugin.getPluginId().toLowerCase().replace("_", "-") + ".jar";
                if (Global.PluginType.TEMPLATE.getType().equals(plugin.getPluginType())) {
                    List<String> path = copyJar(plugin, "package/templates/" + jarName, templateUploadPath + "/" + jarName, templateUploadPath, jarName, applicationContext);
                    if(CollectionUtils.isNotEmpty(path)){
                        delPaths.addAll(path);
                    }
                } else if (Global.PluginType.CARD.getType().equals(plugin.getPluginType())) {
                    List<String> path = copyJar(plugin, "package/cards/" + jarName, cardUploadPath + "/" + jarName, cardUploadPath, jarName, applicationContext);
                    if(CollectionUtils.isNotEmpty(path)){
                        delPaths.addAll(path);
                    }
                } else if (Global.PluginType.MAIL_PLUGIN.getType().equals(plugin.getPluginType())) {
                    List<String> path = copyJar(plugin, "package/plugins/" + jarName, templateUploadPath + "/" + jarName, templateUploadPath, jarName, applicationContext);
                    if(CollectionUtils.isNotEmpty(path)){
                        delPaths.addAll(path);
                    }
                }
                PluginInstallEntity pluginInstall = new PluginInstallEntity();
                pluginInstall.setPluginWid(plugin.getWid());
                pluginInstall.setIpAddress(localIpAddress);
                pluginInstallTools.add(pluginInstall);
            }
        } catch (Exception e) {
            flag=false;
            logger.error("发布Jar发生异常："+e);
        }finally {
            finalDelete(delPaths);
        }
        if(flag){
            pluginInstallService.saveBatch(pluginInstallTools);
            logger.info("------------需要保存发布表的信息-----------"+JSON.toJSONString(pluginInstallTools));
            logger.info("-----------------卡片安装完成，尝试重新启动-------------");
            ExecutorService executorService = applicationContext.getBean(ExecutorService.class);
            executorService.closeSystem();
        }

    }

    private void  finalDelete(List<String> delPaths){
        if(CollectionUtils.isNotEmpty(delPaths)){
            delPaths.forEach(delPath->{
                if(delPath.contains(".")){
                    FileUtil.deletePathFile(Arrays.asList(delPath));
                }else{
                    FileUtil.deleteDir(new File(delPath));
                }
            });
        }
    }

    private List<String> copyJar(PluginsEntity plugin,String resourcePath,String targetFilePath,String targetFileUrl,String jarName,ApplicationContext applicationContext) throws Exception {
        boolean flag=true;
        String jarZipPath="";
        String jarZipUnCompressPath="";
        if(StringUtil.isEmpty(plugin.getDownloadAttWid())){
            logger.info("需要安装的jar列表路径-------------"+resourcePath);
            InputStream resourceAsStream = PluginLoadListener.class.getClassLoader().getResourceAsStream(resourcePath);
            flag=FileUtil.copyByInputStream(resourceAsStream, targetFilePath);
        }else{
            AttachService attachService = applicationContext.getBean(AttachService.class);
            AttachInfoEntity attachInfo = attachService.getById(plugin.getDownloadAttWid());
            if(null==attachInfo){
                logger.info("Jar包下载地址丢失，会对某些功能造成影响，请排查");
            }else{
                OSSClientUtil ossClientUtil = applicationContext.getBean(OSSClientUtil.class);
                Map<String, Object> map = copyDownJar(ossClientUtil, attachInfo, jarName, targetFileUrl);
                flag= (boolean) map.get("flag");
                jarZipPath= String.valueOf(map.get("jarZipPath"));
                jarZipUnCompressPath= String.valueOf(map.get("jarZipUnCompressPath"));
            }
        }
        if(!flag){
            throw new BusinessException("copyJar 发生错误！");
        }
        if(StringUtil.isEmpty(jarZipPath)||StringUtil.isEmpty(jarZipUnCompressPath)){
            return Collections.emptyList();
        }
        return Arrays.asList(jarZipPath,jarZipUnCompressPath);
    }

    private Map<String,Object> copyDownJar(OSSClientUtil ossClientUtil,AttachInfoEntity attachInfo,String jarName,
                                String targetFileUrl) throws IOException {
        String jarZipPath="";
        String jarZipUnCompressPath="";
        boolean flag=true;
        Map<String, Object> map = new HashMap<>();
        OSSFileBean ossFileBean = new OSSFileBean();
        ossFileBean.setDir(attachInfo.getFileUrl().substring(attachInfo.getFileUrl().indexOf("/"),attachInfo.getFileUrl().lastIndexOf("/")+1));
        ossFileBean.setObjectName(attachInfo.getFileUrl().substring(attachInfo.getFileUrl().lastIndexOf("/")+1,attachInfo.getFileUrl().lastIndexOf("?")));
        List<OSSFileBean> fileUrlsWithLocal = ossClientUtil.getFileUrlsWithLocal(Global.OSS_FILE_BACKET_USERICON, Arrays.asList(ossFileBean));
        String jarZipName=jarName.replace("jar","zip");
        jarZipPath=targetFileUrl+"/"+jarZipName;
        jarZipUnCompressPath=targetFileUrl+"/"+jarZipName.substring(0,jarZipName.lastIndexOf("."));
        flag=FileUtil.downloadFileFromURL(fileUrlsWithLocal.get(0).getIconUrl(),targetFileUrl+"/",jarZipName);
        if(flag){
            String jarPath="";
            List<String> fileNamePathArray = ZipUtil.unCompress(new File(jarZipPath), targetFileUrl+"/", true);
            for (int i = 0; i < fileNamePathArray.size(); i++) {
                String filePath = fileNamePathArray.get(i);
                if ("jar".equals(filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length()).toLowerCase())) {
                    jarPath = filePath;
                    break;
                }
            }
            if(StringUtil.isNotEmpty(jarPath)){
                FileUtil.copy(jarPath, targetFileUrl+"/"+jarName);
            }
        }
        map.put("flag",flag);
        map.put("jarZipPath",jarZipPath);
        map.put("jarZipUnCompressPath",jarZipUnCompressPath);
        return map;
    }

}
