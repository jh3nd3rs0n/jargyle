package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.server.internal.io.FileMonitor;
import com.github.jh3nd3rs0n.jargyle.server.internal.io.FileStatusListener;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.users.xml.bind.UsersXml;

import jakarta.xml.bind.JAXBException;

public final class XmlFileSourceUsersProvider extends UsersProvider {
	
	private static final class UsersUpdater implements FileStatusListener {
		
		public static final Logger LOGGER = LoggerFactory.getLogger(
				UsersUpdater.class);

		private final XmlFileSourceUsersProvider usersProvider;

		public UsersUpdater(final XmlFileSourceUsersProvider provider) {
			this.usersProvider = provider;
		}
		
		@Override
		public void onFileCreated(final File file) {
			LOGGER.info(String.format(
					"File '%s' created. Updating users...",
					file));
			if (this.updateFrom(file)) {
				LOGGER.info("Users updated successfully");
			}
		}
		
		@Override
		public void onFileDeleted(final File file) {
			LOGGER.info(String.format(
					"File '%s' deleted (using in-memory copy).",
					file));
		}

		@Override
		public void onFileModified(final File file) {
			LOGGER.info(String.format(
					"File '%s' modified. Updating users...",
					file));
			if (this.updateFrom(file)) {
				LOGGER.info("Users updated successfully");
			}
		}

		private boolean updateFrom(final File file) {
			InputStream in = null;
			Users usrs = null;
			try {
				in = new FileInputStream(file);
				usrs = UsersXml.newInstanceFromXml(in).toUsers();
			} catch (FileNotFoundException e) {
				LOGGER.error( 
						String.format(
								"File '%s' not found", 
								file), 
						e);
				return false;
			} catch (JAXBException e) {
				LOGGER.error( 
						String.format(
								"Error in reading file '%s'", 
								file), 
						e);
				return false;
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						LOGGER.error( 
								String.format(
										"Unable to close input stream of file '%s'", 
										file), 
								e);
					}
				}
			}
			this.usersProvider.setUsers(usrs);
			return true;
		}
		
	}
	
	public static XmlFileSourceUsersProvider newInstance(final String xmlFile) {
		XmlFileSourceUsersProvider xmlFileSourceUsersProvider =
				new XmlFileSourceUsersProvider(xmlFile);
		xmlFileSourceUsersProvider.startMonitoringXmlFile();
		return xmlFileSourceUsersProvider;
	}
	
	private ExecutorService executor;
	private Users users;
	private final File xmlFile;
	
	private XmlFileSourceUsersProvider(final String file) {
		super(file);
		Objects.requireNonNull(file, "XML file must not be null");
		File f = new File(file);
		InputStream in = null;
		Users usrs = null;
		try {
			in = new FileInputStream(f);
			usrs = UsersXml.newInstanceFromXml(in).toUsers();
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e);
		} catch (JAXBException e) {
			throw new IllegalArgumentException(String.format(
					"error in reading XML file '%s'", f), 
					e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw new AssertionError(e);
				}
			}
		}
		this.executor = null;
		this.users = usrs;
		this.xmlFile = f;		
	}
	
	@Override
	public synchronized Users getUsers() {
		return Users.newInstance(this.users);
	}
	
	private synchronized void setUsers(final Users usrs) {
		this.users = Users.newInstance(usrs);
	}
	
	private void startMonitoringXmlFile() {
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(FileMonitor.newInstance(
				this.xmlFile, new UsersUpdater(this)));
	}
	
}
