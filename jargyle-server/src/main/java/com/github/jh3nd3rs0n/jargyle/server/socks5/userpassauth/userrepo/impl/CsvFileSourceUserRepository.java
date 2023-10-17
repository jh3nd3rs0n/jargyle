package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.server.internal.io.FileMonitor;
import com.github.jh3nd3rs0n.jargyle.server.internal.io.FileStatusListener;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.User;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UserRepository;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.Users;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.internal.users.csv.bind.UsersCsvFileConversionHelper;

public final class CsvFileSourceUserRepository extends UserRepository {
	
	private static final class UsersFileStatusListener 
		implements FileStatusListener {

		private final Logger logger;
		private final CsvFileSourceUserRepository userRepository;

		public UsersFileStatusListener(
				final CsvFileSourceUserRepository repository) {
			this.logger = LoggerFactory.getLogger(
					UsersFileStatusListener.class);
			this.userRepository = repository;
		}
		
		@Override
		public void onFileCreated(final File file) {
			this.logger.info(String.format(
					"Created file: %s",
					file));
			this.updateUserRepositoryFrom(file);
		}
		
		@Override
		public void onFileDeleted(final File file) {
			this.logger.info(String.format(
					"Relying on in-memory copy of deleted file: %s",
					file));
		}

		@Override
		public void onFileModified(final File file) {
			this.logger.info(String.format(
					"Modified file: %s",
					file));
			this.updateUserRepositoryFrom(file);
		}
		
		private void updateUserRepositoryFrom(final File file) {
			try {
				this.userRepository.updateUsersFromCsvFile();
				this.logger.info("In-memory copy is up to date");
			} catch (UncheckedIOException e) {
				this.logger.error( 
						String.format(
								"Error in reading file: %s", 
								file), 
						e);
			}
		}
		
	}

	public static CsvFileSourceUserRepository newInstance(
			final String initializationStr) {
		CsvFileSourceUserRepository csvFileSourceUserRepository =
				new CsvFileSourceUserRepository(initializationStr);
		csvFileSourceUserRepository.startMonitoringCsvFile();
		return csvFileSourceUserRepository;
	}
	
	private static Users readUsersFrom(final File csvFile) {
		if (!csvFile.exists()) {
			return Users.newInstance();
		}
		Reader reader = null;
		Users users = null;
		try {
			reader = new InputStreamReader(new FileInputStream(csvFile));
			users = UsersCsvFileConversionHelper.newUsersFrom(reader);
		} catch (FileNotFoundException e) {
			throw new UncheckedIOException(e);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} catch (IllegalArgumentException e) {
			throw new UncheckedIOException(new IOException(e));
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}
		}
		return users;
	}
	
	private static void writeUsersTo(
			final File csvFile, final Users users) {
		File tempCsvFile = new File(csvFile.toString().concat(".tmp"));
		Writer writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(tempCsvFile));
			UsersCsvFileConversionHelper.toCsvFile(users, writer);
		} catch (FileNotFoundException e) {
			throw new UncheckedIOException(e);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} catch (IllegalArgumentException e) {
			throw new UncheckedIOException(new IOException(e));
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}
		}
		try {
			Files.move(
					tempCsvFile.toPath(), 
					csvFile.toPath(),
					StandardCopyOption.ATOMIC_MOVE,
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	private final File csvFile;
	private ExecutorService executor;
	private final AtomicLong lastUpdated;
	private final ReentrantLock lock;	
	private volatile Users users;
	
	private CsvFileSourceUserRepository(final String initializationStr) {
		super(initializationStr);
		File file = new File(initializationStr);
		Users usrs = readUsersFrom(file);
		this.csvFile = file;
		this.executor = null;
		this.lastUpdated = new AtomicLong(System.currentTimeMillis());
		this.lock = new ReentrantLock();
		this.users = usrs;
	}
	
	@Override
	public User get(final String name) {
		User user = null;
		this.lock.lock();
		try {
			user = this.users.get(name);
		} finally {
			this.lock.unlock();
		}
		return user;
	}
	
	@Override
	public Users getAll() {
		Users usrs = null;
		this.lock.lock();
		try {
			usrs = Users.newInstance(this.users);
		} finally {
			this.lock.unlock();
		}
		return usrs;
	}

	@Override
	public void put(final User user) {
		this.lock.lock();
		try {
			Users usrs = Users.newInstance(this.users);
			usrs.put(user);
			this.updateCsvFileFrom(usrs);
			this.updateUsersFrom(usrs);			
		} finally {
			this.lock.unlock();
		}
	}
	
	@Override
	public void putAll(final Users users) {
		this.lock.lock();
		try {
			Users usrs = Users.newInstance(this.users);
			usrs.putAll(users);
			this.updateCsvFileFrom(usrs);
			this.updateUsersFrom(usrs);			
		} finally {
			this.lock.unlock();
		}
	}

	@Override
	public void remove(final String name) {
		this.lock.lock();
		try {
			Users usrs = Users.newInstance(this.users);
			usrs.remove(name);
			this.updateCsvFileFrom(usrs);
			this.updateUsersFrom(usrs);			
		} finally {
			this.lock.unlock();
		}
	}

	private void startMonitoringCsvFile() {
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(FileMonitor.newInstance(
				this.csvFile, 
				new UsersFileStatusListener(this)));
	}
	
	private void updateCsvFileFrom(final Users usrs) {
		writeUsersTo(this.csvFile, usrs);
	}
	
	private void updateUsersFrom(final Users usrs) {
		this.users = usrs;
		this.lastUpdated.set(System.currentTimeMillis());
	}
	
	private void updateUsersFromCsvFile() {
		this.lock.lock();
		try {
			if (this.csvFile.exists() 
					&& this.csvFile.lastModified() > this.lastUpdated.longValue()) {
				Users usrs = readUsersFrom(this.csvFile);
				this.updateUsersFrom(usrs);
			}
		} finally {
			this.lock.unlock();
		}
	}

}
