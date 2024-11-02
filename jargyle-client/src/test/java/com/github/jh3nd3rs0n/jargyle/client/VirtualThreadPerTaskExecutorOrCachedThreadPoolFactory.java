package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.internal.concurrent.ExecutorsHelper;
import com.github.jh3nd3rs0n.test.help.net.ExecutorFactory;

import java.util.concurrent.ExecutorService;

public final class VirtualThreadPerTaskExecutorOrCachedThreadPoolFactory
        extends ExecutorFactory {

    public VirtualThreadPerTaskExecutorOrCachedThreadPoolFactory() {
    }

    @Override
    public ExecutorService newExecutor() {
        return ExecutorsHelper.newVirtualThreadPerTaskExecutorOrDefault(
                ExecutorsHelper.newCachedThreadPoolBuilder());
    }

}
