package com.example.concurrency.atomic;

import com.example.concurrency.annoations.ThreadSafe;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @Author: admin
 * @Date: 2019/10/30 13:08
 * @Description:
 */
@ThreadSafe
@Slf4j
public class AtomicExample5 {
    private static AtomicIntegerFieldUpdater<AtomicExample5> updater = AtomicIntegerFieldUpdater.newUpdater(AtomicExample5.class, "count");

    @Getter
    private volatile int count = 100;

    private static AtomicExample5 atomicExample5 = new AtomicExample5();

    public static void main(String[] args) {
        if (updater.compareAndSet(atomicExample5, 100, 120)) {
            log.info("update success1:{}", atomicExample5.getCount());
        }

        if (updater.compareAndSet(atomicExample5, 100, 120)) {
            log.info("update success2:{}", atomicExample5.getCount());
        } else {
            log.info("update failed:{}", atomicExample5.getCount());
        }

    }
}

