package com.github.jh3nd3rs0n.jargyle.server.internal.io;

import java.io.File;

public abstract class FileMonitor implements Runnable {
	
	public static FileMonitor newInstance(
			final File f, final FileStatusListener listener) {
		String osName = System.getProperty("os.name").toLowerCase();
		/*
		Method java.nio.file.WatchService.take() in NioFileMonitor does not 
		receive a WatchKey under the following conditions:

		OS: Mac OS X
		JDK: openjdk 11
		OS: Mac OS X
		JDK: openjdk 10
		OS: Mac OS X
		JDK: openjdk 9
		OS: Mac OS X
		JDK: oraclejdk 11
		OS: Mac OS X
		JDK: oraclejdk 9

		Therefore Mac OS X systems will use IoFileMonitor. 
		*/
		if (osName.contains("mac")) {
			return new IoFileMonitor(f, listener);
		}
		return new NioFileMonitor(f, listener);
	}
	
	private final File file;
	private final FileStatusListener fileStatusListener;
	
	protected FileMonitor(final File f, final FileStatusListener listener) {
		super();
		this.file = f;
		this.fileStatusListener = listener;
	}
	
	protected File getFile() {
		return this.file;
	}
	
	protected FileStatusListener getFileStatusListener() {
		return this.fileStatusListener;
	}

}