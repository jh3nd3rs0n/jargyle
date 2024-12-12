package com.github.jh3nd3rs0n.jargyle.test.help.net;

import java.util.concurrent.ExecutorService;

/**
 * A factory that creates {@code Executor}s.
 */
public abstract class ExecutorFactory {

    /**
     * Allows the construction of subclass instances.
     */
    public ExecutorFactory() {
    }

    /**
     * Returns a new {@code Executor}.
     *
     * @return a new {@code Executor}
     */
    public abstract ExecutorService newExecutor();

}