package com.example.concurrency.Atomic;

import com.example.concurrency.annoations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: admin
 * @Date: 2019/10/29 17:39
 * @Description: 每次执行的结果可能都不同，这是一个线程不安全的类，使用 @NotThreadSafe 进行标识
 */
@ThreadSafe
@Slf4j
public class AtomicExample1 {
    //请求总数
    public static int clientTotal = 5000;

    //同时并发执行的线程数
    public static int threadTotal = 200;

    private static AtomicInteger count = new AtomicInteger(0);


//    public static int count = 0;

    public static void main(String[] args) throws Exception {

        //线程池
        ExecutorService executorService = Executors.newCachedThreadPool();

        //信号量
        final Semaphore semaphore = new Semaphore(threadTotal);

        //计数器
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);

        for (int i = 0; i < clientTotal; i++) {
            executorService.execute(() -> {
                try {
                    //引入信号量，是否可以被执行
                    semaphore.acquire();
                    add();
                    //释放当前进程
                    semaphore.release();

                } catch (Exception e) {
                    log.error("error", e);
                }
                //计数器减一
                countDownLatch.countDown();
            });
        }

        //闭锁，等待所有线程执行完成，唤醒后续操作
        countDownLatch.await();
        //关闭线程池
        executorService.shutdown();

        log.info("count:{}", count.get());
    }

    private static void add() {
//        count++;
//        count.getAndIncrement();
        count.incrementAndGet();
    }

}
