package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.server.internal.io.FileMonitor;
import com.github.jh3nd3rs0n.jargyle.server.internal.io.FileStatusListener;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.users.xml.bind.UsersXml;

import jakarta.xml.bind.JAXBException;

public final class XmlFileSourceUserRepository extends UserRepository {
	
	private static final class UsersFileStatusListener 
		implements FileStatusListener {
		
		public static final Logger LOGGER = LoggerFactory.getLogger(
				UsersFileStatusListener.class);

		private final XmlFileSourceUserRepository userRepository;

		public UsersFileStatusListener(
				final XmlFileSourceUserRepository repository) {
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

	public static XmlFileSourceUserRepository newInstance(
			final String initializationVal) {
		XmlFileSourceUserRepository xmlFileSourceUserRepository =
				new XmlFileSourceUserRepository(initializationVal);
		xmlFileSourceUserRepository.startMonitoringXmlFile();
		return xmlFileSourceUserRepository;
	}
	
	private static Users readUsersFrom(final File xmlFile) {
		if (!xmlFile.exists()) {
			return Users.newInstance();
		}
		FileInputStream in = null;
		UsersXml usersXml = null;
		try {
			in = new FileInputStream(xmlFile);
			usersXml = UsersXml.newInstanceFromXml(in);
		} catch (FileNotFoundException e) {
			throw new UncheckedIOException(e);
		} catch (JAXBException e) {
			throw new UncheckedIOException(new IOException(e));
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}
		}
		return usersXml.toUsers();
	}
	
	private static void writeUsersTo(
			final File xmlFile, final Users users) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(xmlFile);			
			UsersXml usersXml = new UsersXml(users);
			usersXml.toXml(out);
		} catch (FileNotFoundException e) {
			throw new UncheckedIOException(e);
		} catch (JAXBException e) {
			throw new UncheckedIOException(new IOException(e));
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}
		}
	}
	
	private ExecutorService executor;
	private Users users;
	private final File xmlFile;
	
	private XmlFileSourceUserRepository(
			final String initializationVal) {
		super(initializationVal);
		File file = new File(initializationVal);
		Users usrs = readUsersFrom(file);
		this.executor = null;
		this.users = usrs;
		this.xmlFile = file;
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
		this.users.put(user);
		writeUsersTo(this.xmlFile, this.users);
	}
	
	@Override
	public void putAll(final Users users) {
		this.users.putAll(users);
		writeUsersTo(this.xmlFile, this.users);		
	}

	@Override
	public void remove(final String name) {
		this.users.remove(name);
		writeUsersTo(this.xmlFile, this.users);		
	}

	private void startMonitoringXmlFile() {
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(FileMonitor.newInstance(
				this.xmlFile, 
				new UsersFileStatusListener(this)));
	}
	
	private void update() {
		Users usrs = readUsersFrom(this.xmlFile);
		if (!this.users.equals(usrs)) {
			this.users = usrs;
		}
	}

}
