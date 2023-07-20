package com.github.jh3nd3rs0n.jargyle.clientserver;

import java.io.IOException;

@FunctionalInterface
public interface IoRunnable {

	void run() throws IOException;
	
}
