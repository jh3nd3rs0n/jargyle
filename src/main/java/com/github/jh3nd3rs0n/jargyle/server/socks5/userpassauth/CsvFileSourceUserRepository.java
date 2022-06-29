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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.server.internal.io.FileMonitor;
import com.github.jh3nd3rs0n.jargyle.server.internal.io.FileStatusListener;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.users.csv.bind.UsersCsvTable;

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
		UsersCsvTable usersCsvTable = null;
		try {
			reader = new InputStreamReader(new FileInputStream(csvFile));
			usersCsvTable = UsersCsvTable.newInstanceFrom(reader);
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
		return usersCsvTable.toUsers();
	}
	
	private static void writeUsersTo(
			final File csvFile, final Users users) {
		File tempCsvFile = new File(csvFile.toString().concat(".tmp"));
		Writer writer = null;
		try {
			UsersCsvTable usersCsvTable = UsersCsvTable.newInstance(users);
			writer = new OutputStreamWriter(new FileOutputStream(tempCsvFile));
			usersCsvTable.toCsv(writer);
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
	private volatile Users users;
	
	private CsvFileSourceUserRepository(final String initializationVal) {
		super(initializationVal);
		File file = new File(initializationVal);
		Users usrs = readUsersFrom(file);
		this.csvFile = file;
		this.executor = null;
		this.lastUpdated = new AtomicLong(System.currentTimeMillis());
		this.users = usrs;
	}
	
	@Override
	public User get(final String name) {
		return this.users.get(name);
	}
	
	@Override
	public Users getAll() {
		return Users.newInstance(this.users);
	}

	@Override
	public void put(final User user) {
		Users usrs = Users.newInstance(this.users);
		usrs.put(user);
		this.updateFrom(usrs);
	}
	
	@Override
	public void putAll(final Users users) {
		Users usrs = Users.newInstance(this.users);
		usrs.putAll(users);
		this.updateFrom(usrs);
	}

	@Override
	public void remove(final String name) {
		Users usrs = Users.newInstance(this.users);
		usrs.remove(name);
		this.updateFrom(usrs);
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
