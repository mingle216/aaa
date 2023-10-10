package com.wisedu.minos.casp.portal.common.executor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 功能描述：线程池配置类
 * 如下方式会使@Async失效
 * 一、异步方法使用static修饰
 * 二、异步类没有使用@Component注解（或其他注解）导致spring无法扫描到异步类
 * 三、异步方法不能与异步方法在同一个类中
 * 四、类中需要使用@Autowired或@Resource等注解自动注入，不能自己手动new对象
 * 五、如果使用SpringBoot框架必须在启动类中增加@EnableAsync注解
 * 六、在Async 方法上标注@Transactional是没用的。 在Async 方法调用的方法上标注@Transactional 有效。
 * 七、调用被@Async标记的方法的调用者不能和被调用的方法在同一类中不然不会起作用！！！！！！！
 * 八、使用@Async时要求是不能有返回值的不然会报错的 因为异步要求是不关心结果的
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ExecutorConfig
 * @Author: jcx
 * @Date: 2020/7/28
 */
@Configuration
@EnableAsync
public class ExecutorConfig {

    private static final Logger logger = LogManager.getLogger(ExecutorConfig.class);

    @Bean(name = "asyncExecutor")
    public Executor asyncServiceExecutor(TaskExecutionProperties taskExecutionProperties) {

        logger.info("start asyncServiceExecutor");
        TaskExecutionProperties.Pool pool = taskExecutionProperties.getPool();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(pool.getCoreSize());
        //配置最大线程数
        executor.setMaxPoolSize(pool.getMaxSize());
        //配置队列大小
        executor.setQueueCapacity(pool.getQueueCapacity());
        //配置允许线程空闲时间
        executor.setKeepAliveSeconds(pool.getKeepAlive().getNano());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(taskExecutionProperties.getThreadNamePrefix());
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }


}
