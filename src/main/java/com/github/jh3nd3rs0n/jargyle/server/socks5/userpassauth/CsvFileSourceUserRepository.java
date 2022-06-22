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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
			if (this.updateUsersFrom(file)) {
				LOGGER.info("In-memory copy updated successfully");
			}
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
			if (this.updateUsersFrom(file)) {
				LOGGER.info("In-memory copy updated successfully");
			}
		}
		
		private boolean updateUsersFrom(final File file) {
			try {
				this.userRepository.update();
			} catch (UncheckedIOException e) {
				LOGGER.error( 
						String.format(
								"Error in reading file '%s'", 
								file), 
						e);
				return false;				
			}
			return true;
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
		Writer writer = null;
		try {
			UsersCsvTable usersCsvTable = UsersCsvTable.newInstance(users);
			writer = new OutputStreamWriter(new FileOutputStream(csvFile));
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
	}
	
	private ExecutorService executor;
	private volatile Users users;
	private final File csvFile;
	
	private CsvFileSourceUserRepository(final String initializationVal) {
		super(initializationVal);
		File file = new File(initializationVal);
		Users usrs = readUsersFrom(file);
		this.executor = null;
		this.users = usrs;
		this.csvFile = file;
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
		writeUsersTo(this.csvFile, usrs);
	}
	
	@Override
	public void putAll(final Users users) {
		Users usrs = Users.newInstance(this.users);
		usrs.putAll(users);
		writeUsersTo(this.csvFile, usrs);
	}

	@Override
	public void remove(final String name) {
		Users usrs = Users.newInstance(this.users);
		usrs.remove(name);
		writeUsersTo(this.csvFile, usrs);		
	}

	private void startMonitoringCsvFile() {
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(FileMonitor.newInstance(
				this.csvFile, 
				new UsersFileStatusListener(this)));
	}
	
	private void update() {
		Users usrs = readUsersFrom(this.csvFile);
		if (!this.users.equals(usrs)) {
			this.users = usrs;
		}
	}

}
