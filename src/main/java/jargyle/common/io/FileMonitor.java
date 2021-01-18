package jargyle.common.io;

import java.io.File;

/*
Method java.nio.file.WatchService.take() does not receive a WatchKey under the 
following conditions:

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

Related WatchService code is commented out and kept for historical purposes. 
*/
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

	private static final class CreatedFileStatus extends FileStatus {
		
		public static boolean isCreated(
				final File file, final FileStatus lastFileStatus) {
			return (lastFileStatus == null 
					|| lastFileStatus instanceof DeletedFileStatus
					|| lastFileStatus instanceof NonexistentFileStatus) 
					&& file.exists();
		}

		public CreatedFileStatus(final long time) {
			super(time);
		}
		
	}
	
	private static final class DeletedFileStatus extends FileStatus {

		public static boolean isDeleted(
				final File file, final FileStatus lastFileStatus) {
			return lastFileStatus != null 
					&& (lastFileStatus instanceof CreatedFileStatus
							|| lastFileStatus instanceof ModifiedFileStatus) 
					&& !file.exists();
		}
		
		public DeletedFileStatus(final long time) {
			super(time);
		}
		
	}
	
	private static abstract class FileStatus {
		
		public static FileStatus get(
				final File file, final FileStatus lastFileStatus) {
			if (CreatedFileStatus.isCreated(file, lastFileStatus)) {
				return new CreatedFileStatus(System.currentTimeMillis());
			}
			if (DeletedFileStatus.isDeleted(file, lastFileStatus)) {
				return new DeletedFileStatus(System.currentTimeMillis());
			}
			if (ModifiedFileStatus.isExisting(file, lastFileStatus)) {
				long lastModified = file.lastModified();
				if (lastModified > lastFileStatus.since()) {
					return new ModifiedFileStatus(lastModified);
				}
			}
			if (NonexistentFileStatus.isNonexistent(file, lastFileStatus)) {
				return new NonexistentFileStatus(System.currentTimeMillis());
			}
			return lastFileStatus;
		}
		
		private final long since;
		
		public FileStatus(final long time) {
			this.since = time;
		}
		
		public long since() {
			return this.since;
		}
		
	}
	
	private static final class ModifiedFileStatus extends FileStatus {

		public static boolean isExisting(
				final File file, final FileStatus lastFileStatus) {
			return lastFileStatus != null
					&& (lastFileStatus instanceof CreatedFileStatus
							|| lastFileStatus instanceof ModifiedFileStatus)
					&& file.exists();
		}
		
		public ModifiedFileStatus(final long time) {
			super(time);
		}
		
	}
	
	private static final class NonexistentFileStatus extends FileStatus {

		public static boolean isNonexistent(
				final File file, final FileStatus lastFileStatus) {
			return lastFileStatus == null && !file.exists();
		}
		
		public NonexistentFileStatus(final long time) {
			super(time);
		}
		
	}
	
	private final File file;
	private final FileStatusListener fileStatusListener;
	
	private FileStatus lastFileStatus;
		
	public FileMonitor(final File f, final FileStatusListener listener) {
		this.file = f;
		this.fileStatusListener = listener;
		this.lastFileStatus = FileStatus.get(f, null);
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
			FileStatus fileStatus = FileStatus.get(
					this.file, this.lastFileStatus);
			if (this.lastFileStatus.equals(fileStatus)) {
				continue;
			}
			if (fileStatus instanceof CreatedFileStatus) {
				this.fileStatusListener.fileCreated(this.file);
			}
			if (fileStatus instanceof DeletedFileStatus) {
				this.fileStatusListener.fileDeleted(this.file);
			}
			if (fileStatus instanceof ModifiedFileStatus) {
				this.fileStatusListener.fileModfied(this.file);
			}
			this.lastFileStatus = fileStatus;
		}
	}
	
}