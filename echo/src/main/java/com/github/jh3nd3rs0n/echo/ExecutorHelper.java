package com.github.jh3nd3rs0n.echo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ExecutorHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(
			ExecutorHelper.class);
	
	private static final AtomicBoolean CAN_CREATE_VIRTUAL_THREAD_PER_TASK_EXECUTOR;
	private static final AtomicBoolean CREATED_VIRTUAL_THREAD_PER_TASK_EXECUTOR;
	private static final Method NEW_VIRTUAL_THREAD_PER_TASK_EXECUTOR_METHOD;
	private static final String USE_VIRTUAL_THREADS_SYSTEM_PROPERTY_NAME = 
			"com.github.jh3nd3rs0n.echo.useVirtualThreads";
	
	static {
		Method newVirtualThreadPerTaskExecutorMethod = null;
		if (!"false".equals(System.getProperty(
				USE_VIRTUAL_THREADS_SYSTEM_PROPERTY_NAME))) {
			try {
				newVirtualThreadPerTaskExecutorMethod = Executors.class.getMethod(
						"newVirtualThreadPerTaskExecutor", new Class[] { });
			} catch (NoSuchMethodException e) {
			} catch (SecurityException e) {
			}
		}
		CAN_CREATE_VIRTUAL_THREAD_PER_TASK_EXECUTOR = 
				new AtomicBoolean(newVirtualThreadPerTaskExecutorMethod != null);
		CREATED_VIRTUAL_THREAD_PER_TASK_EXECUTOR = new AtomicBoolean(false);
		NEW_VIRTUAL_THREAD_PER_TASK_EXECUTOR_METHOD = 
				newVirtualThreadPerTaskExecutorMethod;
	}
	
	public static ExecutorService newExecutor() {
		if (CAN_CREATE_VIRTUAL_THREAD_PER_TASK_EXECUTOR.get()) {
			try {
				ExecutorService executor = 
						(ExecutorService) NEW_VIRTUAL_THREAD_PER_TASK_EXECUTOR_METHOD.invoke(
								null, new Object[] { });
				if (CREATED_VIRTUAL_THREAD_PER_TASK_EXECUTOR.compareAndSet(
						false, true)) {
					LOGGER.info(String.format(
							"Using virtual threads. (Use -D%s=false to "
							+ "disable.)", 
							USE_VIRTUAL_THREADS_SYSTEM_PROPERTY_NAME));
				}
				return executor;
			} catch (IllegalAccessException e) {
				throw new AssertionError(e);
			} catch (InvocationTargetException e) {
				if (e.getCause() instanceof UnsupportedOperationException) {
					if (CAN_CREATE_VIRTUAL_THREAD_PER_TASK_EXECUTOR.compareAndSet(
							true, false)) {
						LOGGER.warn(String.format(
								"Using platform threads instead of virtual "
								+ "threads. Use the JVM option "
								+ "--enable-preview to enable virtual threads. "
								+ "(Or use -D%s=false to disable this warning.)",
								USE_VIRTUAL_THREADS_SYSTEM_PROPERTY_NAME));
					}
				} else {
					throw new AssertionError(e);
				}
			}
		}
		return Executors.newCachedThreadPool();
	}
	
	private ExecutorHelper() { }
	
}
