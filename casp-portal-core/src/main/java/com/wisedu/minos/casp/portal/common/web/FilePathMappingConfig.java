package com.wisedu.minos.casp.portal.common.web;

import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 功能描述：设置虚拟路径，映射服务器文件绝对路径
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title FilePathMappingConfig
 * @Author: jcx
 * @Date: 2020/8/14
 */
@Configuration
public class FilePathMappingConfig implements WebMvcConfigurer {

    private static final Logger logger = LogManager.getLogger(FilePathMappingConfig.class);

    //这里之所以不用@Value注解直接获取配置信息，目的是作为开发底座，如果二次开发人员未配置该信息，@Value注解直接会启动报错
    @Autowired
    private Environment environment;
    //卡片上传路径
    private String cardUploadPath;
    //模板上传路径
    private String templateUploadPath;
    //图标库上传路径
    private String uploadLocalIncoPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        this.cardUploadPath=environment.getProperty("file.cardUploadPath")!=null?environment.getProperty("file.cardUploadPath"):"";
        this.templateUploadPath=environment.getProperty("file.templateUploadPath")!=null?environment.getProperty("file.templateUploadPath"):"";
        this.uploadLocalIncoPath=environment.getProperty("file.uploadLocalIncoPath")!=null?environment.getProperty("file.uploadLocalIncoPath"):"";
        if( StringUtil.isEmpty(cardUploadPath) ){
            logger.error("未配置卡片包上传路径，请按【file.cardUploadPath=XXX/XXX】的格式配置卡片包上传路径!");
        }else{
            registry.addResourceHandler("/"+ Global.accessFilePrefix.CARD_ACCESS_PREFIX.getType()+"/**").addResourceLocations("file:"+cardUploadPath+"/");
        }

        if( StringUtil.isEmpty(templateUploadPath) ){
            logger.error("未配置模板包上传路径，请按【file.templateUploadPath=XXX/XXX】的格式配置模板包上传路径!");
        }else{
            registry.addResourceHandler("/"+ Global.accessFilePrefix.TEMPLATE_ACCESS_PREFIX.getType()+"/**").addResourceLocations("file:"+templateUploadPath+"/");
        }

        if( StringUtil.isEmpty(uploadLocalIncoPath) ){
            logger.error("未配置图标库上传路径，请按【file.uploadLocalIncoPath=XXX/XXX】的格式配置图标库上传路径!");
        }else{
            registry.addResourceHandler("/"+ Global.accessFilePrefix.ICON_ACCESS_PREFIX.getType()+"/**").addResourceLocations("file:"+uploadLocalIncoPath+"/");
        }

//        registry.addResourceHandler("/**").addResourceLocations("classpath:/public/");
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/templates/");

    }
}
