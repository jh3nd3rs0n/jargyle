package com.github.jh3nd3rs0n.jargyle.server.internal.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class NioFileMonitor extends FileMonitor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(
			NioFileMonitor.class);

	public NioFileMonitor(final File f, final FileStatusListener listener) {
		super(f, listener);
	}
	
	private WatchService newWatchService() {
		WatchService watchService = null;
		try {
			watchService = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			LOGGER.error(
					"Unable to create WatchService", 
					e);
			return null;
		}
		return watchService;
	}
	
	private final void notifyFileStatusListener(
			final WatchEvent.Kind<?> kind, final File file) {
		FileStatusListener fileStatusListener = this.getFileStatusListener();
		if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
			fileStatusListener.onFileCreated(file);
		}
		if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
			fileStatusListener.onFileDeleted(file);
		}
		if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
			fileStatusListener.onFileModified(file);
		}
	}
	
	private WatchKey register(
			final Path path, final WatchService watchService) {
		WatchKey watchKey = null;
		try {
			watchKey = path.register(
					watchService,
					StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY);
		} catch (IOException e) {
			LOGGER.error(
					String.format(
							"Unable to register '%s' to WatchService", 
							path), 
					e);
			return null;
		}
		return watchKey;
	}
	
	@Override
	public void run() {
		WatchService watchService = this.newWatchService();
		if (watchService == null) { return; }
		File file = this.getFile();
		File absoluteFile = file.getAbsoluteFile();
		String parent = absoluteFile.getParent();
		Path dir = Paths.get(parent);
		WatchKey watchKey = this.register(dir, watchService);
		if (watchKey == null) { return; } 
		while (true) {
			WatchKey key = null;
			try {
				key = watchService.take();
			} catch (InterruptedException e) {
				return;
			}
			for (WatchEvent<?> watchEvent : key.pollEvents()) {
				WatchEvent.Kind<?> kind = watchEvent.kind();
				if (kind == StandardWatchEventKinds.OVERFLOW) {
					continue;
				}
				@SuppressWarnings("unchecked")
				WatchEvent<Path> event = (WatchEvent<Path>) watchEvent;
				Path filename = event.context();
				Path child = dir.resolve(filename);
				if (absoluteFile.equals(child.toFile())) {
					this.notifyFileStatusListener(kind, absoluteFile);
				}
			}
			if (!key.reset()) {
				break;
			}
		}
	}

}
