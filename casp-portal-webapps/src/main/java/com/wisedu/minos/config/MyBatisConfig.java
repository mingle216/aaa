package com.wisedu.minos.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.wisedu.minos.casp.portal.handler.MyMetaObjectHandler;
import com.wisedu.minos.util.MinosException;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 配置MyBatis参数的Config，包括如下内容：
 * 1、基础包扫描路径
 * 2、分页插件的加载
 * 3、根据不用的数据库加载不同的sql语句文件
 * @author zhangjian 0116211
 * @create 2019-10-30
 **/

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.wisedu.minos.casp.portal.dao")
public class MyBatisConfig implements TransactionManagementConfigurer {
    private final static Logger LOGGER= LoggerFactory.getLogger(MyBatisConfig.class);

    @Autowired
    private DataSource dataSource;

    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        try {
            return new DataSourceTransactionManager(dataSource);
        } catch (Exception e) {
            LOGGER.error("数据库初始化失败",e);
            throw new MinosException("数据库初始化失败",e);
        }
    }

    @Bean
    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean() {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.setConfiguration(configuration);
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage("com.wisedu.minos.casp.portal.dao");

        // 增加分页插件
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{paginationInterceptor});

        try {
            // 加载数据库的定义需要根据当前数据库驱动进行区别加载
            // 1.mapper/generate下是都需要加载的
            List<Resource> resources = new ArrayList<>();
            resources.addAll(getResource("classpath*:mapper/generate/**/*.xml"));

            sqlSessionFactoryBean.setMapperLocations(resources.stream().toArray(Resource[]::new));

            GlobalConfig globalConfig = new GlobalConfig();
            globalConfig.setMetaObjectHandler(new MyMetaObjectHandler());
            sqlSessionFactoryBean.setGlobalConfig(globalConfig);

            // 导入mybatis配置
//            MybatisConfiguration configuration = new MybatisConfiguration();
            // 配置打印sql语句
//            configuration.setLogImpl(StdOutImpl.class);

            return sqlSessionFactoryBean;
        } catch (Exception e) {
            LOGGER.error("数据库初始化失败",e);
            throw new MinosException("数据库初始化失败");
        }
    }

    /**
     * 根据指定地址获取对应的资源
     * @param locationPattern
     * @return
     */
    private List<Resource> getResource(String locationPattern){
        List<Resource> resources = new ArrayList<>();

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try{
            Resource[] generate = resolver.getResources(locationPattern);
            if (generate != null && generate.length != 0){
                resources.addAll(Arrays.stream(generate).collect(Collectors.toList()));
            }
        }catch (Exception e){
            LOGGER.error("加载"+locationPattern+"下的资源失败",e);
            throw new MinosException("加载sql资源失败");
        }

        return resources;
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
