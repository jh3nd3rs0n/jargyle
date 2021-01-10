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
		System.out.println("OS Name: " + System.getProperty("os.name"));
		File absoluteFile = this.file.getAbsoluteFile();
		System.out.println("Absolute file: " + absoluteFile.toString());
		String parent = absoluteFile.getParent();
		System.out.println("Parent: " + parent);
		Path dir = Paths.get(parent);
		System.out.println("Dir: " + dir);
		WatchService watchService = this.newWatchService();
		System.out.println("WatchService: " + watchService);
		if (watchService == null) { return; } 
		WatchKey watchKey = this.register(dir, watchService);
		System.out.println("WatchKey: " + watchKey);
		if (watchKey == null) { return; } 
		while (true) {
			WatchKey key = null;
			try {
				key = watchService.take();
			} catch (InterruptedException e) {
				return;
			}
			System.out.println("Key: " + key);
			for (WatchEvent<?> watchEvent : key.pollEvents()) {
				WatchEvent.Kind<?> kind = watchEvent.kind();
				System.out.println("WatchEvent.Kind: " + kind);
				if (kind == StandardWatchEventKinds.OVERFLOW) {
					continue;
				}
				@SuppressWarnings("unchecked")
				WatchEvent<Path> event = (WatchEvent<Path>) watchEvent;
				Path filename = event.context();
				Path child = dir.resolve(filename);
				System.out.println("Child: " + child);
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