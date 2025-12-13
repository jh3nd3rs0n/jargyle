package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauthmethod.internal.userrepo.impl;

import com.github.jh3nd3rs0n.jargyle.server.internal.io.FileMonitor;
import com.github.jh3nd3rs0n.jargyle.server.internal.io.FileStatusListener;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauthmethod.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public final class FileSourceUserRepository extends UserRepository {
	
	private static final class UsersFileStatusListener 
		implements FileStatusListener {

		private final Logger logger;
		private final FileSourceUserRepository userRepository;

		public UsersFileStatusListener(
				final FileSourceUserRepository repository) {
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
				this.userRepository.updateUsersFromFile();
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

	private static User newUserFrom(
			final String s) {
		String[] sElements = s.split(":");
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"username hashed password pair must be in the following "
							+ "format: USERNAME:HASHED_PASSWORD");
		}
		String name;
		try {
			name = URLDecoder.decode(sElements[0], "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
		String hashedPassword;
		try {
			hashedPassword = URLDecoder.decode(sElements[1], "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
		return User.newInstance(name, HashedPassword.newInstanceFrom(hashedPassword));
	}

	public static FileSourceUserRepository newInstance(
			final UserRepositorySpec userRepositorySpec,
			final String initializationStr) {
		FileSourceUserRepository fileSourceUserRepository =
				new FileSourceUserRepository(
						userRepositorySpec, initializationStr);
		fileSourceUserRepository.startMonitoringFile();
		return fileSourceUserRepository;
	}

	private static Users readUsersFrom(
			final InputStream in) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(in));
		List<User> users = new ArrayList<User>();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			User user;
			try {
				user = newUserFrom(line);
			} catch (IllegalArgumentException e) {
				throw new IOException(e);
			}
			users.add(user);
		}
		return Users.of(users);
	}

	private static Users readUsersFrom(final File file) {
		if (!file.exists()) {
			return Users.of();
		}
		FileInputStream fileInputStream = null;
		Users users = null;
		try {
			fileInputStream = new FileInputStream(file);
			users = readUsersFrom(fileInputStream);
		} catch (FileNotFoundException e) {
			throw new UncheckedIOException(e);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} catch (IllegalArgumentException e) {
			throw new UncheckedIOException(new IOException(e));
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}
		}
		return users;
	}

	private static void writeUsersTo(
			final OutputStream out, final Users users) throws IOException {
		try (BufferedWriter bufferedWriter = new BufferedWriter(
				new OutputStreamWriter(out))) {
			for (User user : users.toMap().values()) {
				bufferedWriter.write(toString(user));
				bufferedWriter.newLine();
				bufferedWriter.flush();
			}
		}
	}

	private static void writeUsersTo(final File file, final Users users) {
		File tempFile = new File(file.toString().concat(".tmp"));
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(tempFile);
			writeUsersTo(fileOutputStream, users);
		} catch (FileNotFoundException e) {
			throw new UncheckedIOException(e);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} catch (IllegalArgumentException e) {
			throw new UncheckedIOException(new IOException(e));
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}
		}
		try {
			Files.move(
					tempFile.toPath(), 
					file.toPath(),
					StandardCopyOption.ATOMIC_MOVE,
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private static String toString(final User user) {
		String encodedName;
		try {
			encodedName = URLEncoder.encode(user.getName(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
		String encodedHashedPassword;
		try {
			encodedHashedPassword = URLEncoder.encode(
					user.getHashedPassword().toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
		return String.format(
				"%s:%s",
				encodedName,
				encodedHashedPassword);
	}

	private ExecutorService executor;
	private final File file;
	private final AtomicLong lastUpdated;
	private final ReentrantLock lock;	
	private volatile Users users;
	
	private FileSourceUserRepository(
			final UserRepositorySpec userRepositorySpec, 
			final String initializationStr) {
		super(userRepositorySpec, initializationStr);
		File file = new File(initializationStr);
		Users usrs = readUsersFrom(file);
		this.executor = null;
		this.file = file;		
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
			usrs = Users.of(this.users);
		} finally {
			this.lock.unlock();
		}
		return usrs;
	}

	@Override
	public void put(final User user) {
		this.lock.lock();
		try {
			Users usrs = Users.of(this.users);
			usrs.put(user);
			this.updateFileFrom(usrs);
			this.updateUsersFrom(usrs);			
		} finally {
			this.lock.unlock();
		}
	}
	
	@Override
	public void putAll(final Users users) {
		this.lock.lock();
		try {
			Users usrs = Users.of(this.users);
			usrs.putAll(users);
			this.updateFileFrom(usrs);
			this.updateUsersFrom(usrs);			
		} finally {
			this.lock.unlock();
		}
	}

	@Override
	public void remove(final String name) {
		this.lock.lock();
		try {
			Users usrs = Users.of(this.users);
			usrs.remove(name);
			this.updateFileFrom(usrs);
			this.updateUsersFrom(usrs);			
		} finally {
			this.lock.unlock();
		}
	}

	private void startMonitoringFile() {
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(FileMonitor.newInstance(
				this.file, 
				new UsersFileStatusListener(this)));
	}
	
	private void updateFileFrom(final Users usrs) {
		writeUsersTo(this.file, usrs);
	}
	
	private void updateUsersFrom(final Users usrs) {
		this.users = usrs;
		this.lastUpdated.set(System.currentTimeMillis());
	}
	
	private void updateUsersFromFile() {
		this.lock.lock();
		try {
			if (this.file.exists() 
					&& this.file.lastModified() > this.lastUpdated.longValue()) {
				Users usrs = readUsersFrom(this.file);
				this.updateUsersFrom(usrs);
			}
		} finally {
			this.lock.unlock();
		}
	}

}
