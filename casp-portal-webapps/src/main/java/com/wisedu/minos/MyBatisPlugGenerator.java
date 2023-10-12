package com.wisedu.minos;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.OracleTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.util.*;

/**
 * <p>
 * mysql 代码生成器演示例子
 * </p>
 *
 * @author jobob
 * @since 2018-09-12
 */
public class MyBatisPlugGenerator {
    // 生成代码的基础包路径
    private static final String BASE_PACKAGE = "com.wisedu.minos.casp.portal.dao";
    // 当前工程的模块名称
    private static final String MODULE_NAME = "casp-portal-core";
    // 连接的数据库信息
    private static final String JDBCURL = "jdbc:oracle:thin:@172.16.29.51:1521/orcl?Unicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String JDBCNAME = "casp_portal_61";
    private static final String JDBCPASSWORD = "casp_portal_61";
    private static final String JDBC_DRIVER_NAME = "oracle.jdbc.OracleDriver";
    // 需要生成的表
    // t_amp_开头的表
//     private static final String[] tables = new String[]{"T_AMP_SYS_ICON", "T_AMP_ATTACH_ENTITY_REL", "T_AMP_ATTACH_INFO", "T_AMP_PERSONAL_DATA_AUTH", "T_AMP_PERSONAL_DATA", "T_AMP_SERVICE_LATEST_USE", "T_AMP_USER_EMAIL"};
//     private static final String TABLE_PREFIX = "T_AMP_";
    // t_amp_view_开头的表
//    private static final String[] tables = new String[]{"T_AMP_VIEW_SYSTEM_CONFIG", "T_AMP_VIEW_SITE_AUTH", "T_AMP_VIEW_SITE", "T_AMP_VIEW_SITE_MAG", "T_AMP_VIEW_TEMPLATE", "T_AMP_VIEW_VERSION_MANAGEMENT", "T_AMP_VIEW_CARD", "T_AMP_VIEW_MENU_AUTH", "T_AMP_VIEW_MENU", "T_AMP_VIEW_PAGE_CARD_CONFIG", "T_AMP_VIEW_PAGE_INFO", "T_AMP_VIEW_POPUP_WINDOW", "T_AMP_VIEW_PRO_LOCAL_STYLE", "T_AMP_VIEW_SHOW_PROGRAMME", "T_AMP_VIEW_SIDEBAR"};
    private static final String[] tables = new String[]{"T_AMP_VIEW_POPUP_WINDOW"};
    private static final String TABLE_PREFIX = "T_AMP_VIEW_";
    // 是否开启二级缓存
    private static final boolean ENABLE_CACHE = true;

    /**
     * RUN THIS
     */
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/"+ MODULE_NAME + "/src/main/java");
        gc.setAuthor("wisedu");
        gc.setOpen(false);
        gc.setEntityName("%sEntity");
        gc.setEnableCache(ENABLE_CACHE);
        gc.setDateType(DateType.ONLY_DATE);
        gc.setFileOverride(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(JDBCURL);
        dsc.setDriverName(JDBC_DRIVER_NAME);
        dsc.setUsername(JDBCNAME);
        dsc.setPassword(JDBCPASSWORD);
        dsc.setTypeConvert(new OracleTypeConvert() {
            @Override
            public DbColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType){
                String t = fieldType.toLowerCase();
                if (t.contains("char")) {
                    return DbColumnType.STRING;
                } else {
                    if (!t.contains("date") && !t.contains("timestamp")) {
                        if (t.contains("number")) {
                            if (t.matches("number\\(+\\d\\)")) {
                                return DbColumnType.INTEGER;
                            }

                            if (t.matches("number\\(+\\d{2}+\\)")) {
                                return DbColumnType.LONG;
                            }

                            return DbColumnType.DOUBLE;
                        }

                        if (t.contains("float")) {
                            return DbColumnType.FLOAT;
                        }

                        if (t.contains("binary")) {
                            return DbColumnType.BYTE_ARRAY;
                        }

                        if (t.contains("raw")) {
                            return DbColumnType.BYTE_ARRAY;
                        }
                    } else {
                        switch(globalConfig.getDateType()) {
                            case ONLY_DATE:
                                return DbColumnType.DATE;
                            case SQL_PACK:
                                return DbColumnType.TIMESTAMP;
                            case TIME_PACK:
                                return DbColumnType.LOCAL_DATE_TIME;
                        }
                    }

                    return DbColumnType.STRING;
                }
            }
        });
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(BASE_PACKAGE);
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        // 用于生成数据库的模板文件，基础表的不需要，用于增加不同数据库类型的数据库脚本
        focList.add(new FileOutConfig("generator/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String dir = projectPath + "/" + MODULE_NAME + "/src/main/resources/mapper/generate/";
                createDir(dir);
                // 手写的数据库脚本不需要区分数据库类型的
                return projectPath + "/" + MODULE_NAME + "/src/main/resources/mapper/generate/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setInclude(tables);
        strategy.setSuperEntityColumns("id");
        strategy.setSuperEntityClass("com.wisedu.minos.casp.portal.base.BasicEntity");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setEntityTableFieldAnnotationEnable(true);
        strategy.setSuperEntityColumns("wid", "updateTime", "createTime");
        strategy.setTablePrefix(TABLE_PREFIX);
        mpg.setStrategy(strategy);
        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        TemplateConfig templateConfig = new TemplateConfig()
                .setEntity("generator/templates/entity.java")
                .setMapper("generator/templates/mapper.java")
                .setXml(null)
                .setController(null)
                .setService(null)
                .setServiceImpl(null);
        mpg.setTemplate(templateConfig);
        mpg.execute();

    }

    // 生成sql语句的xml时需要先创建一个目录
    private static void createDir(String path){
        File dir = new File(path);
        if (!dir.exists()){
            dir.mkdirs();
        }
    }

}
