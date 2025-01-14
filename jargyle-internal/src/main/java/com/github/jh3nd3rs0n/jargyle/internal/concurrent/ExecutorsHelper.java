package com.github.jh3nd3rs0n.jargyle.internal.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Helper class for {@code Executor}s.
 */
public final class ExecutorsHelper {

    /**
     * The name of the system property to use virtual threads instead of
     * platform threads.
     */
    static final String USE_VIRTUAL_THREADS_SYSTEM_PROPERTY_NAME =
            "com.github.jh3nd3rs0n.jargyle.internal.concurrent.useVirtualThreads";

    /**
     * The {@code ReentrantLock} used for controlling access to
     * initialization.
     */
    private static final ReentrantLock REENTRANT_LOCK = new ReentrantLock();

    /**
     * The {@code Logger}.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(
            ExecutorsHelper.class);

    /**
     * The name of the static method in
     * {@code Executors.newVirtualThreadPerTaskExecutor()}.
     */
    private static final String EXECUTORS_NEW_VIRTUAL_THREAD_PER_TASK_EXECUTOR_METHOD_NAME =
            "newVirtualThreadPerTaskExecutor";

    /**
     * The {@code AtomicBoolean} value to indicate if virtual threads are being
     * used instead of platform threads.
     */
    private static final AtomicBoolean USING_VIRTUAL_THREADS =
            new AtomicBoolean(false);

    /**
     * The {@code boolean} value to indicate if initialization has been
     * completed.
     */
    private static boolean initialized = false;

    /**
     * The static {@code Method} of
     * {@code Executors.newVirtualThreadPerTaskExecutor()}.
     */
    private static Method executorsNewVirtualThreadPerTaskExecutorMethod = null;

    /**
     * The {@code boolean} value to indicate if virtual threads can be used
     * instead of platform threads.
     */
    private static boolean canUseVirtualThreads = false;

    /**
     * Prevents the construction of unnecessary instances.
     */
    private ExecutorsHelper() {
    }

    /**
     * Returns a {@code boolean} value to indicate if virtual threads can be
     * used instead of platform threads.
     *
     * @return a {@code boolean} value to indicate if virtual threads can be
     * used instead of platform threads
     */
    static boolean canUseVirtualThreads() {
        initializeIfNotInitialized();
        return canUseVirtualThreads;
    }

    /**
     * Initialize static fields if they are not initialized.
     */
    private static void initializeIfNotInitialized() {
        REENTRANT_LOCK.lock();
        try {
            if (initialized) {
                return;
            }
            try {
                executorsNewVirtualThreadPerTaskExecutorMethod =
                        Executors.class.getMethod(
                                EXECUTORS_NEW_VIRTUAL_THREAD_PER_TASK_EXECUTOR_METHOD_NAME);
            } catch (NoSuchMethodException e) {
                LOGGER.debug(
                        String.format(
                                "No such method: %s::%s",
                                Executors.class.getName(),
                                EXECUTORS_NEW_VIRTUAL_THREAD_PER_TASK_EXECUTOR_METHOD_NAME),
                        e);
            }
            canUseVirtualThreads =
                    executorsNewVirtualThreadPerTaskExecutorMethod != null;
            initialized = true;
        } finally {
            REENTRANT_LOCK.unlock();
        }
    }

    /**
     * Returns a new {@code CachedThreadPoolBuilder}.
     *
     * @return a new {@code CachedThreadPoolBuilder}
     */
    public static ExecutorBuilder newCachedThreadPoolBuilder() {
        return new CachedThreadPoolBuilder();
    }

    /**
     * Returns a new {@code FixedThreadPoolBuilder} with the provided fixed
     * number of threads for a fixed thread pool.
     *
     * @param nThreads the provided fixed number of threads for a fixed
     *                 thread pool
     * @return a new {@code FixedThreadPoolBuilder} with the provided fixed
     * number of threads for a fixed thread pool
     */
    public static ExecutorBuilder newFixedThreadPoolBuilder(
            final int nThreads) {
        return new FixedThreadPoolBuilder(nThreads);
    }

    /**
     * Returns a new virtual-thread-per-task-executor or a new
     * {@code ExecutorService} provided by the provided
     * {@code ExecutorBuilder}. If the runtime supports virtual threads or if
     * the system property with the name of
     * {@link #USE_VIRTUAL_THREADS_SYSTEM_PROPERTY_NAME} is not set to
     * {@code false}, a new virtual-thread-per-task-executor is returned.
     * Otherwise, a new {@code ExecutorService} is returned by the provided
     * {@code ExecutorBuilder}.
     *
     * @param executorBuilder the provided {@code ExecutorBuilder}
     * @return a new virtual-thread-per-task-executor or a new
     * {@code ExecutorService} provided by the provided
     * {@code ExecutorBuilder}
     */
    public static ExecutorService newVirtualThreadPerTaskExecutorOrElse(
            final ExecutorBuilder executorBuilder) {
        if ("false".equals(System.getProperty(
                USE_VIRTUAL_THREADS_SYSTEM_PROPERTY_NAME))) {
            return executorBuilder.build();
        }
        if (!canUseVirtualThreads()) {
            return executorBuilder.build();
        }
        ExecutorService executorService;
        try {
            executorService =
                    (ExecutorService) executorsNewVirtualThreadPerTaskExecutorMethod.invoke(
                            null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.debug(
                    String.format(
                            "Error occurred when attempting to invoke method %s.%s()",
                            Executors.class.getName(),
                            EXECUTORS_NEW_VIRTUAL_THREAD_PER_TASK_EXECUTOR_METHOD_NAME),
                    e);
            return executorBuilder.build();
        }
        if (USING_VIRTUAL_THREADS.compareAndSet(
                false, true)) {
            LOGGER.info(String.format(
                    "Using virtual threads. (Use -D%s=false to "
                            + "disable.)",
                    USE_VIRTUAL_THREADS_SYSTEM_PROPERTY_NAME));
        }
        return executorService;
    }

    /**
     * A builder for an {@code Executor}.
     */
    public static abstract class ExecutorBuilder {

        /**
         * Allows the construction of subclass instances.
         */
        public ExecutorBuilder() {
        }

        /**
         * Builds and returns a new {@code Executor}.
         *
         * @return a new {@code Executor}
         */
        public abstract ExecutorService build();

    }

    /**
     * A builder for a cached thread pool.
     */
    public static final class CachedThreadPoolBuilder extends ExecutorBuilder {

        /**
         * Constructs a {@code CachedThreadPoolBuilder}.
         */
        private CachedThreadPoolBuilder() {
        }

        @Override
        public ExecutorService build() {
            return Executors.newCachedThreadPool();
        }

    }

    /**
     * A builder for a fixed thread pool.
     */
    public static final class FixedThreadPoolBuilder extends ExecutorBuilder {

        /**
         * The fixed number of threads for a fixed thread pool.
         */
        private final int numOfThreads;

        /**
         * Constructs a {@code FixedThreadPoolBuilder} with the provided fixed
         * number of threads for a fixed thread pool.
         *
         * @param nThreads the provided fixed number of threads for a fixed
         *                 thread pool
         */
        private FixedThreadPoolBuilder(final int nThreads) {
            this.numOfThreads = nThreads;
        }

        @Override
        public ExecutorService build() {
            return Executors.newFixedThreadPool(this.numOfThreads);
        }

    }

}
