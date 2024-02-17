package com.github.jh3nd3rs0n.jargyle.server.internal.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public final class ThreadFactoryHelper {

    private static final ReentrantLock REENTRANT_LOCK = new ReentrantLock();

    private static final Logger LOGGER = LoggerFactory.getLogger(
            ThreadFactoryHelper.class);

    private static final String THREAD_OF_VIRTUAL_METHOD_NAME = "ofVirtual";

    private static final String THREAD_BUILDER_OF_VIRTUAL_CLASS_NAME =
            "java.lang.Thread$Builder$OfVirtual";

    private static final String THREAD_BUILDER_OF_VIRTUAL_FACTORY_METHOD_NAME =
            "factory";

    private static final String USE_VIRTUAL_THREADS_SYSTEM_PROPERTY_NAME =
            "com.github.jh3nd3rs0n.jargyle.server.useVirtualThreads";

    private static final AtomicBoolean CREATED_VIRTUAL_THREAD_FACTORY =
            new AtomicBoolean(false);

    private static boolean initialized = false;

    private static Method threadOfVirtualMethod = null;

    private static Class<?> threadBuilderOfVirtualClass = null;

    private static Method threadBuilderOfVirtualFactoryMethod = null;

    private static boolean canUseVirtualThreads = false;

    private ThreadFactoryHelper() {
    }

    public static ThreadFactory getThreadFactory() {
        if ("false".equals(System.getProperty(
                USE_VIRTUAL_THREADS_SYSTEM_PROPERTY_NAME))) {
            return Executors.defaultThreadFactory();
        }
        initializeIfNotInitialized();
        if (!canUseVirtualThreads) {
            return Executors.defaultThreadFactory();
        }
        Object threadBuilder;
        try {
            threadBuilder = threadOfVirtualMethod.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.debug(
                    String.format(
                            "Error occurred when attempting to invoke method %s.%s()",
                            Thread.class.getName(),
                            THREAD_OF_VIRTUAL_METHOD_NAME),
                    e);
            return Executors.defaultThreadFactory();
        }
        ThreadFactory threadFactory;
        try {
            threadFactory =
                    (ThreadFactory) threadBuilderOfVirtualFactoryMethod.invoke(
                            threadBuilder);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.debug(
                    String.format(
                            "Error occurred when attempting to invoke method %s.%s()",
                            THREAD_BUILDER_OF_VIRTUAL_CLASS_NAME,
                            THREAD_BUILDER_OF_VIRTUAL_FACTORY_METHOD_NAME),
                    e);
            return Executors.defaultThreadFactory();
        }
        if (threadFactory == null) {
            return Executors.defaultThreadFactory();
        }
        if (CREATED_VIRTUAL_THREAD_FACTORY.compareAndSet(
                false, true)) {
            LOGGER.info(String.format(
                    "Using virtual threads. (Use -D%s=false to "
                            + "disable.)",
                    USE_VIRTUAL_THREADS_SYSTEM_PROPERTY_NAME));
        }
        return threadFactory;
    }

    private static void initializeIfNotInitialized() {
        REENTRANT_LOCK.lock();
        try {
            if (initialized) {
                return;
            }
            try {
                threadOfVirtualMethod = Thread.class.getMethod(
                        THREAD_OF_VIRTUAL_METHOD_NAME);
            } catch (NoSuchMethodException e) {
                LOGGER.debug(
                        String.format(
                                "No such method: %s.%s()",
                                Thread.class.getName(),
                                THREAD_OF_VIRTUAL_METHOD_NAME),
                        e);
            }
            try {
                threadBuilderOfVirtualClass = Class.forName(
                        THREAD_BUILDER_OF_VIRTUAL_CLASS_NAME);
            } catch (ClassNotFoundException e) {
                LOGGER.debug(
                        String.format(
                                "Class not found: %s",
                                THREAD_BUILDER_OF_VIRTUAL_CLASS_NAME),
                        e);
            }
            if (threadBuilderOfVirtualClass != null) {
                try {
                    threadBuilderOfVirtualFactoryMethod =
                            threadBuilderOfVirtualClass.getMethod(
                                    THREAD_BUILDER_OF_VIRTUAL_FACTORY_METHOD_NAME);
                } catch (NoSuchMethodException e) {
                    LOGGER.debug(
                            String.format(
                                    "No such method: %s.%s()",
                                    THREAD_BUILDER_OF_VIRTUAL_CLASS_NAME,
                                    THREAD_BUILDER_OF_VIRTUAL_FACTORY_METHOD_NAME),
                            e);
                }
            }
            canUseVirtualThreads = threadOfVirtualMethod != null
                    && threadBuilderOfVirtualClass != null
                    && threadBuilderOfVirtualFactoryMethod != null;
            initialized = true;
        } finally {
            REENTRANT_LOCK.unlock();
        }
    }

}
