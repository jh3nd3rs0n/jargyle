package com.github.jh3nd3rs0n.jargyle.internal.concurrent;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorsHelperTest {

    @Test
    public void testNewVirtualThreadPerTaskExecutorOrDefaultExecutorBuilder01() {
        ExecutorService executorService1 = Executors.newCachedThreadPool();
        ExecutorService executorService2;
        System.setProperty(ExecutorsHelper.USE_VIRTUAL_THREADS_SYSTEM_PROPERTY_NAME, "false");
        try {
            executorService2 = ExecutorsHelper.newVirtualThreadPerTaskExecutorOrDefault(
                    ExecutorsHelper.newCachedThreadPoolBuilder());
        } finally {
            System.clearProperty(ExecutorsHelper.USE_VIRTUAL_THREADS_SYSTEM_PROPERTY_NAME);
        }
        Assert.assertEquals(
                executorService1.getClass(),
                executorService2.getClass());
    }

    @Test
    public void testNewVirtualThreadPerTaskExecutorOrDefaultExecutorBuilder02() {
        ExecutorService executorService1 = Executors.newFixedThreadPool(2);
        ExecutorService executorService2;
        System.setProperty(ExecutorsHelper.USE_VIRTUAL_THREADS_SYSTEM_PROPERTY_NAME, "false");
        try {
            executorService2 = ExecutorsHelper.newVirtualThreadPerTaskExecutorOrDefault(
                    ExecutorsHelper.newFixedThreadPoolBuilder(2));
        } finally {
            System.clearProperty(ExecutorsHelper.USE_VIRTUAL_THREADS_SYSTEM_PROPERTY_NAME);
        }
        Assert.assertEquals(
                executorService1.getClass(),
                executorService2.getClass());
    }

    @Test
    public void testNewVirtualThreadPerTaskExecutorOrDefaultExecutorBuilder03() {
        ExecutorService executorService1 = Executors.newCachedThreadPool();
        ExecutorService executorService2 =
                ExecutorsHelper.newVirtualThreadPerTaskExecutorOrDefault(
                        ExecutorsHelper.newCachedThreadPoolBuilder());
        if (Runtime.version().version().get(0) >= 21) {
            Assert.assertNotEquals(
                    executorService1.getClass(),
                    executorService2.getClass());
        } else {
            Assert.assertEquals(
                    executorService1.getClass(),
                    executorService2.getClass());
        }
    }

    @Test
    public void testNewVirtualThreadPerTaskExecutorOrDefaultExecutorBuilder04() {
        ExecutorService executorService1 = Executors.newFixedThreadPool(2);
        ExecutorService executorService2 =
                ExecutorsHelper.newVirtualThreadPerTaskExecutorOrDefault(
                        ExecutorsHelper.newFixedThreadPoolBuilder(2));
        if (Runtime.version().version().get(0) >= 21) {
            Assert.assertNotEquals(
                    executorService1.getClass(),
                    executorService2.getClass());
        } else {
            Assert.assertEquals(
                    executorService1.getClass(),
                    executorService2.getClass());
        }
    }

}