package com.github.jh3nd3rs0n.jargyle.server.internal.io;

import java.io.File;

final class IoFileMonitor extends FileMonitor implements Runnable {

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
		
		public static FileStatus getInstanceFrom(
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
	
	private static final int HALF_SECOND = 500;
	
	private FileStatus lastFileStatus;
		
	public IoFileMonitor(final File f, final FileStatusListener listener) {
		super(f, listener);
		this.lastFileStatus = FileStatus.getInstanceFrom(f, null);
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(HALF_SECOND);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			File file = this.getFile();
			FileStatus fileStatus = FileStatus.getInstanceFrom(
					file, this.lastFileStatus);
			if (this.lastFileStatus.equals(fileStatus)) {
				continue;
			}
			FileStatusListener fileStatusListener = this.getFileStatusListener();
			if (fileStatus instanceof CreatedFileStatus) {
				fileStatusListener.onFileCreated(file);
			}
			if (fileStatus instanceof DeletedFileStatus) {
				fileStatusListener.onFileDeleted(file);
			}
			if (fileStatus instanceof ModifiedFileStatus) {
				fileStatusListener.onFileModified(file);
			}
			this.lastFileStatus = fileStatus;
		}
	}
	
}
