package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth;

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
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.users.csv.bind.UsersCsvTableConversionHelper;

public final class CsvFileSourceUserRepository extends UserRepository {
	
	private static final class UsersFileStatusListener 
		implements FileStatusListener {
		
		public static final Logger LOGGER = LoggerFactory.getLogger(
				UsersFileStatusListener.class);

		private final CsvFileSourceUserRepository userRepository;

		public UsersFileStatusListener(
				final CsvFileSourceUserRepository repository) {
			this.userRepository = repository;
		}
		
		@Override
		public void onFileCreated(final File file) {
			LOGGER.info(String.format(
					"File '%s' created",
					file));
			this.updateUserRepositoryFrom(file);
		}
		
		@Override
		public void onFileDeleted(final File file) {
			LOGGER.info(String.format(
					"File '%s' deleted (using in-memory copy)",
					file));
		}

		@Override
		public void onFileModified(final File file) {
			LOGGER.info(String.format(
					"File '%s' modified",
					file));
			this.updateUserRepositoryFrom(file);
		}
		
		private void updateUserRepositoryFrom(final File file) {
			try {
				this.userRepository.updateFromCsvFile();
				LOGGER.info("In-memory copy is up to date");
			} catch (UncheckedIOException e) {
				LOGGER.error( 
						String.format(
								"Error in reading file '%s'", 
								file), 
						e);
			}
		}
		
	}

	public static CsvFileSourceUserRepository newInstance(
			final String initializationVal) {
		CsvFileSourceUserRepository csvFileSourceUserRepository =
				new CsvFileSourceUserRepository(initializationVal);
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
			users = UsersCsvTableConversionHelper.newUsersFrom(reader);
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
			UsersCsvTableConversionHelper.toCsvTable(users, writer);
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
	
	private CsvFileSourceUserRepository(final String initializationVal) {
		super(initializationVal);
		File file = new File(initializationVal);
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
		Users users = null;
		this.lock.lock();
		try {
			users = Users.newInstance(this.users);
		} finally {
			this.lock.unlock();
		}
		return users;
	}

	@Override
	public void put(final User user) {
		this.lock.lock();
		try {
			Users usrs = Users.newInstance(this.users);
			usrs.put(user);
			this.updateFrom(usrs);
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
			this.updateFrom(usrs);			
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
			this.updateFrom(usrs);			
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
	
	private synchronized void updateFrom(final Users usrs) {
		writeUsersTo(this.csvFile, usrs);
		this.updateUsers(usrs);
	}
	
	private synchronized void updateFromCsvFile() {
		if (this.csvFile.exists() 
				&& this.csvFile.lastModified() > this.lastUpdated.longValue()) {
			Users usrs = readUsersFrom(this.csvFile);
			this.updateUsers(usrs);
		}
	}
	
	private void updateUsers(final Users usrs) {
		this.users = usrs;
		this.lastUpdated.set(System.currentTimeMillis());
	}

}
