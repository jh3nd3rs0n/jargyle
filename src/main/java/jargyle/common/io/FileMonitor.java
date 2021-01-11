package jargyle.common.io;

import java.io.File;
/*
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
*/

public final class FileMonitor implements Runnable {
	
	/*
	private static final Logger LOGGER = Logger.getLogger(
			FileMonitor.class.getName());
	*/

	private static final class FileCreatedState extends FileState {

		public FileCreatedState(final long time) {
			super(time);
		}
		
	}
	
	private static final class FileDeletedState extends FileState {

		public FileDeletedState(final long time) {
			super(time);
		}
		
	}
	
	private static final class FileModifiedState extends FileState {

		public FileModifiedState(final long time) {
			super(time);
		}
		
	}
	
	private static final class FileNonexistentState extends FileState {

		public FileNonexistentState(final long time) {
			super(time);
		}
		
	}
	
	private static abstract class FileState {
		
		public static FileState get(
				final File file, final FileState lastFileState) {
			if (lastFileState == null && !file.exists()) {
				return new FileNonexistentState(System.currentTimeMillis());
			}
			if ((lastFileState == null 
					|| lastFileState instanceof FileDeletedState
					|| lastFileState instanceof FileNonexistentState) 
					&& file.exists()) {
				return new FileCreatedState(System.currentTimeMillis());
			}
			if (lastFileState != null 
					&& (lastFileState instanceof FileCreatedState
							|| lastFileState instanceof FileModifiedState) 
					&& !file.exists()) {
				return new FileDeletedState(System.currentTimeMillis());
			}
			if (lastFileState != null
					&& (lastFileState instanceof FileCreatedState
							|| lastFileState instanceof FileModifiedState)
					&& file.exists()
					&& file.lastModified() > lastFileState.since()) {
				return new FileModifiedState(file.lastModified());
			}
			return null;
		}
		
		private final long since;
		
		public FileState(final long time) {
			this.since = time;
		}
		
		public long since() {
			return this.since;
		}
		
	}
	
	private final File file;
	private final FileStatusListener fileStatusListener;
	
	private FileState lastFileState;
		
	public FileMonitor(
			final File f, 
			final FileStatusListener listener) {
		this.file = f;
		this.fileStatusListener = listener;
		this.lastFileState = FileState.get(f, null);
	}
	
	/*
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
	*/
	
	@Override
	public void run() {
		/*
		WatchService watchService = this.newWatchService();
		if (watchService == null) { return; }
		File absoluteFile = this.file.getAbsoluteFile();
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
		*/
		while (true) {
			FileState newFileState = FileState.get(
					this.file, this.lastFileState);
			if (newFileState instanceof FileCreatedState) {
				this.fileStatusListener.fileCreated(this.file);
				this.lastFileState = newFileState;
				continue;
			}
			if (newFileState instanceof FileDeletedState) {
				this.fileStatusListener.fileDeleted(this.file);
				this.lastFileState = newFileState;
				continue;
			}
			if (newFileState instanceof FileModifiedState) {
				this.fileStatusListener.fileModfied(this.file);
				this.lastFileState = newFileState;
				continue;
			}
		}
	}
	
}