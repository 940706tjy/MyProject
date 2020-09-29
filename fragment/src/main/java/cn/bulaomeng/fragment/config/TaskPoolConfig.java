package cn.bulaomeng.fragment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class TaskPoolConfig {
	@Bean("tjyTaskExecutor")
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(100);//核心线程数目
		executor.setMaxPoolSize(100);//指定最大线程数
		executor.setQueueCapacity(200);//队列中最大的数目
		executor.setKeepAliveSeconds(60);//线程空闲后的最大存活时间
		executor.setThreadNamePrefix("taskExecutor-");//线程名称前缀
		executor.setWaitForTasksToCompleteOnShutdown(true);//设置 线程池关闭 的时候 等待 所有任务都完成后，再继续 销毁 其他的 Bean，确保 异步任务 的 销毁 就会先于 数据库连接池对象 的销毁。
		executor.setAwaitTerminationSeconds(60);//设置线程池中 任务的等待时间
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}
}