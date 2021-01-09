package jargyle.common.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class FileMonitor implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(
			FileMonitor.class.getName());
	
	private final File file;
	private final FileStatusListener fileStatusListener;
	
	public FileMonitor(
			final File f, 
			final FileStatusListener listener) {
		this.file = f;
		this.fileStatusListener = listener;
	}
	
	private WatchService newWatchService() {
		WatchService watchService = null;
		try {
			watchService = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			LOGGER.log(
					Level.WARNING, 
					"Unable to create WatchService", 
					e);
			return null;
		}
		return watchService;
	}
	
	private final void notifyFileStatusListener(
			final WatchEvent.Kind<?> kind, final File file) {
		if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
			this.fileStatusListener.fileCreated(file);
		}
		if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
			this.fileStatusListener.fileDeleted(file);
		}
		if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
			this.fileStatusListener.fileModfied(file);
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
			LOGGER.log(
					Level.WARNING, 
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
		File absoluteFile = this.file.getAbsoluteFile();
		String parent = absoluteFile.getParent();
		Path dir = Paths.get(parent);
		WatchService watchService = this.newWatchService();
		if (watchService == null) { return; } 
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