package com.wisedu.minos.casp.portal.spi;

import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.utils.FileUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import org.apache.dubbo.common.utils.ClassUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * JarRegisterBeanFactoryPostProcessor
 * <p/>
 * 扫描模板卡片jar并注册spring bean
 * 此版本先屏蔽加载外部包 所有产品包全部内置
 * @author hyluan
 * @date 2020/9/16 20:18
 * Copyright (c) 2018 wisedu
 */
//@Component
public class JarRegisterBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private static final Logger logger = LogManager.getLogger(JarRegisterBeanFactoryPostProcessor.class);
    private static final String CLASS_SUFFIX = ".class";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String jarPath = beanFactory.getBean(Environment.class).getProperty("system.loadOtherJarPath");
        if (StringUtil.isEmpty(jarPath)) {
            logger.error("==============因未配置外部模板卡片与包包绝对路径，不注册模板与卡片包=============");
            return;
        }
        List<URL> urls = new ArrayList<>();
        Arrays.stream(jarPath.split(",")).forEach(path -> {
            List<URL> allJars = getAllJars(path);
            urls.addAll(allJars);
        });
        URLClassLoader urlClassLoader = loadJar(urls);
        List<String> allClassName = getAllClassName(urls.toArray(new URL[0]));
        logger.info("准备注册外部模板卡片包");
        allClassName.forEach(clazzName -> {
            Class<?> aClass = null;
            try {
                logger.debug("加载类：" + clazzName);
                aClass = urlClassLoader.loadClass(clazzName);
            } catch (Exception e) {
                logger.error("==============加载类失败=============:" + clazzName, e);
            }
            try {
                if (isSpringBean(aClass)) {
                    registerBean((BeanDefinitionRegistry) beanFactory, aClass);
                }
            } catch (Exception e) {
                logger.error("==============注册spring bean失败=============:" + clazzName, e);
            }
        });
    }


    private boolean isSpringBean(Class<?> aClass) {
        Service annotation1 = AnnotationUtils.findAnnotation(aClass, Service.class);
        Component annotation2 = AnnotationUtils.findAnnotation(aClass, Component.class);
        Configuration annotation3 = AnnotationUtils.findAnnotation(aClass, Configuration.class);
        return annotation1 != null || annotation2 != null || annotation3 != null;
    }

    private List<String> getAllClassName(URL[] urls) {
        List<String> classes = new ArrayList<>();
        Arrays.stream(urls).forEach(url -> {
            logger.info("获取到的模板卡片包：" + url.getPath());
            JarFile jarFile = null;
            try {
                if ("jar".equals(url.getFile().substring(url.getFile().lastIndexOf(".") + 1).toLowerCase())) {
                    jarFile = new JarFile(url.getFile());
                }
            } catch (IOException e) {
                throw new BusinessException("JarFile加载失败", e);
            }
            if (null != jarFile) {
                Enumeration<JarEntry> entrys = Objects.requireNonNull(jarFile).entries();
                while (entrys.hasMoreElements()) {
                    JarEntry jarEntry = entrys.nextElement();
                    String name = jarEntry.getName();
                    if (name.endsWith(CLASS_SUFFIX)) {
                        String className = name.replaceAll("/", ".").substring(0, name.length() - CLASS_SUFFIX.length());
                        classes.add(className);
                    }
                }
            }
        });
        return classes;
    }

    private URLClassLoader loadJar(List<URL> urls) {
        try {
            //自定义一个类加载器，加载class
            URLClassLoader classLoader = (URLClassLoader) ClassUtils.getClassLoader(MinosExtensionLoader.class);
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            for (URL url : urls) {
                method.invoke(classLoader, new Object[]{url});
            }
            //这里很重要，需要设置当前classloader为父加载器，否则会加载重复的类进来
            return classLoader;
        } catch (Exception e) {
            throw new RuntimeException("加载jar失败", e);
        }
    }


    private List<URL> getAllJars(String jarPath) {
        return FileUtil.listFile(jarPath)
                .stream().filter(file -> file.getName().endsWith(".jar")).map(this::toUrl).collect(Collectors.toList());
    }

    private URL toUrl(File f) {
        try {
            return f.toURI().toURL();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void registerBean(BeanDefinitionRegistry registry, Class<?> beanClass) {
        if (beanClass == null) {
            return;
        }
        //将加载的class设置为bean definition，然后注入到spring容器中
        String name = beanClass.getName();
        if (registry.containsBeanDefinition(name)) {
            logger.debug("bean已存在，删除" + name);
            registry.removeBeanDefinition(name);
        }
        logger.debug("注册bean:" + name);
        AnnotatedGenericBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(beanClass);
        registry.registerBeanDefinition(beanClass.getSimpleName(), beanDefinition);
    }


}