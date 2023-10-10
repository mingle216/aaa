package com.wisedu.minos;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
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
    private static final String[] tables = new String[]{"T_AMP_VIEW_PLUGINS"};
    // 忽略的表格前缀，生成代码时忽略
    private static final String TABLE_PREFIX = "T_AMP_VIEW";

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
        gc.setAuthor("jcx");
        gc.setOpen(false);
        gc.setEntityName("%sEntity");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(JDBCURL);
        dsc.setDriverName(JDBC_DRIVER_NAME);
        dsc.setUsername(JDBCNAME);
        dsc.setPassword(JDBCPASSWORD);
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
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String dir = projectPath + "/" + MODULE_NAME + "/src/main/resources/mapper/generate/";
                createDir(dir);
                // 手写的数据库脚本不需要区分数据库类型的
                return projectPath + "/" + MODULE_NAME + "/src/main/resources/mapper/generate/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(focList);
        // 忽略生成的controller和Service的java类
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 如果本地的sql的xml文件已经存在，则不创建
                if (filePath.endsWith(StringPool.DOT_XML)){
                    File file = new File(filePath);
                    return !file.exists();
                }

                // 如果本地已经生成过Mapper.java，则不需要再次生成
                if (filePath.endsWith("Mapper.java")){
                    File file = new File(filePath);
                    return !file.exists();
                }

                return !fileType.equals(FileType.CONTROLLER) && !fileType.equals(FileType.SERVICE) && !fileType.equals(FileType.SERVICE_IMPL);
            }
        });
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(false);
        strategy.setInclude(tables);
        strategy.setSuperEntityColumns("id");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setEntityTableFieldAnnotationEnable(true);
        strategy.setTablePrefix(TABLE_PREFIX);
        mpg.setStrategy(strategy);
        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        TemplateConfig templateConfig = new TemplateConfig()
                .setEntity("generator/templates/entity.java");
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
