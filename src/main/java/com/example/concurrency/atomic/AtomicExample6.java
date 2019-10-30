package com.example.concurrency.atomic;

import com.example.concurrency.annoations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: admin
 * @Date: 2019/10/30 13:08
 * @Description:
 */
@ThreadSafe
@Slf4j
public class AtomicExample6 {


    private static AtomicBoolean isHappended = new AtomicBoolean(false);

    //请求总数
    public static int clientTotal = 5000;

    //同时并发执行的线程数
    public static int threadTotal = 200;


    private static AtomicExample6 atomicExample5 = new AtomicExample6();

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
                    test();
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

        log.info("isHappended:{}", isHappended.get());
    }

    private static void test() {
        //原子性--该段代码只执行一次
        if (isHappended.compareAndSet(false, true)) {
            log.info("excute", isHappended.get());
        }
    }
}

