package com.github.jh3nd3rs0n.jargyle.server;

import java.io.IOException;

@FunctionalInterface
public interface IoRunnable {

	void run() throws IOException;
	
}
