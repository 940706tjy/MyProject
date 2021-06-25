package cn.bulaomeng.fragment.test.delayed;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 延迟阻塞队列
 *
 * @author tjy
 * @date 2021/6/25
 **/
@Slf4j
//@Component
public class TaskQueueDaemonThread {

    private volatile static TaskQueueDaemonThread taskQueueDaemonThread;

    private TaskQueueDaemonThread() {
    }

    /**
     * 单例
     */
    public static TaskQueueDaemonThread getInstance() {
        if (taskQueueDaemonThread == null) {
            synchronized (TaskQueueDaemonThread.class) {
                if (taskQueueDaemonThread == null) {
                    taskQueueDaemonThread = new TaskQueueDaemonThread();
                }
            }
        }
        return taskQueueDaemonThread;
    }

    /**
     * 创建一个自适应服务器配置的线程池
     */
    private ThreadPoolExecutor eventQueueService = new ThreadPoolExecutor(
            20,
            20 * Runtime.getRuntime().availableProcessors(),
            60L,
            TimeUnit.SECONDS,
            // 这里可以根据实际业务设定队列数量，默认大小是Integer.MAX_VALUE
            new LinkedBlockingDeque<>(),
            new ThreadFactoryBuilder().setNameFormat("delayed-pool-%d").setDaemon(true).build(),
            new ThreadPoolExecutor.DiscardOldestPolicy()
    );


    /**
     * 创建一个最初为空的新 DelayQueue
     */
    private DelayQueue<Task<Runnable>> t = new DelayQueue<>();


    /**
     * 初始化守护线程
     */
    // @PostConstruct
    public void init() {
        // 初始化时，创建一个监听任务线程，避免主线程阻塞
        Thread daemonThread = new Thread(() -> execute());
        daemonThread.setDaemon(true);
        daemonThread.setName("delayed-pool-main");
        daemonThread.start();
    }

    /**
     *  初始化轮询执行任务
     */
    private void execute() {
        while (true) {
            try {
                // 加入执行间隔，每隔1s执行一次
                TimeUnit.SECONDS.sleep(1);

                // 从延迟队列中取值,如果没有对象过期则队列一直等待，
                Task<Runnable> t1 = t.take();

                //修改问题的状态
                Runnable task = t1.getTask();
                if (task == null) {
                    continue;
                }

                eventQueueService.execute(task);
                log.info("result ==> [{}]", Thread.currentThread().getName());

            } catch (Exception e) {
                log.error(e.getMessage(), e);
                break;
            }
        }
    }


    /**
     * 添加任务，
     * time 延迟时间
     * task 任务
     * 用户为问题设置延迟时间
     */
    public void put(long time, Runnable task) {
        //转换成ns
        long nanoTime = TimeUnit.NANOSECONDS.convert(time, TimeUnit.MILLISECONDS);
        //创建一个任务
        Task<Runnable> k = new Task<>(nanoTime, task);
        //将任务放在延迟的队列中
        t.put(k);
    }

    /**
     * 结束任务，清空队列
     *
     * @param task
     */
    public boolean endTask(Task<Runnable> task) {
        return t.remove(task);
    }
}
