package jargyle.server;

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

	private final File file;
	private final FileStatusListener fileStatusListener;
	private final Logger logger;
	
	public FileMonitor(
			final File f, 
			final FileStatusListener listener, 
			final Logger lggr) {
		this.file = f;
		this.fileStatusListener = listener;
		this.logger = lggr;
	}
	
	@Override
	public void run() {
		File absoluteFile = this.file.getAbsoluteFile();
		String parent = absoluteFile.getParent();
		Path dir = Paths.get(parent);
		WatchService watcher = null;
		try {
			watcher = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			this.logger.log(
					Level.WARNING, 
					"Unable to create WatchService", 
					e);
			return;
		}
		WatchKey key = null;
		try {
			key = dir.register(
					watcher,
					StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY);
		} catch (IOException e) {
			this.logger.log(
					Level.WARNING, 
					String.format(
							"Unable to register '%s' to WatchService", 
							parent), 
					e);
			return;
		}
		while (true) {
			WatchKey k = null;
			try {
				k = watcher.take();
			} catch (InterruptedException e) {
				return;
			}
			if (key != k) {
				continue;
			}
			for (WatchEvent<?> event : k.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();
				if (kind == StandardWatchEventKinds.OVERFLOW) {
					continue;
				}
				@SuppressWarnings("unchecked")
				WatchEvent<Path> ev = (WatchEvent<Path>) event;
				Path filename = ev.context();
				Path child = dir.resolve(filename);
				if (absoluteFile.equals(child.toFile())) {
					if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
						this.fileStatusListener.fileCreated(absoluteFile);
					}
					if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
						this.fileStatusListener.fileDeleted(absoluteFile);
					}
					if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
						this.fileStatusListener.fileModfied(absoluteFile);
					}
				}
			}
			boolean valid = k.reset();
			if (!valid) {
				break;
			}
		}
	}
	
}