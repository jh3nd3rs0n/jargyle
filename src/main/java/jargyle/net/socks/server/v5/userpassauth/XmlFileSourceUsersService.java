package jargyle.net.socks.server.v5.userpassauth;

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

import jargyle.io.FileMonitor;
import jargyle.io.FileStatusListener;

final class XmlFileSourceUsersService extends UsersService {
	
	private static final class UsersUpdater implements FileStatusListener {
		
		public static final Logger LOGGER = LoggerFactory.getLogger(
				UsersUpdater.class);

		private final XmlFileSourceUsersService usersService;

		public UsersUpdater(final XmlFileSourceUsersService service) {
			this.usersService = service;
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
				usrs = Users.newInstanceFrom(in);
			} catch (FileNotFoundException e) {
				LOGGER.warn( 
						String.format(
								"File '%s' not found", 
								file), 
						e);
				return false;
			} catch (IOException e) {
				LOGGER.warn( 
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
						LOGGER.warn( 
								String.format(
										"Unable to close input stream of file '%s'", 
										file), 
								e);
					}
				}
			}
			this.usersService.users = usrs;
			return true;
		}
		
	}
	
	public static XmlFileSourceUsersService newInstance(final String xmlFile) {
		XmlFileSourceUsersService xmlFileSourceUsersService =
				new XmlFileSourceUsersService(xmlFile);
		xmlFileSourceUsersService.startMonitoringXmlFile();
		return xmlFileSourceUsersService;
	}
	
	private ExecutorService executor;
	private Users users;
	private final File xmlFile;
	
	private XmlFileSourceUsersService(final String file) {
		super(file);
		Objects.requireNonNull(file, "XML file must not be null");
		File f = new File(file);
		InputStream in = null;
		Users usrs = null;
		try {
			in = new FileInputStream(f);
			usrs = Users.newInstanceFrom(in);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e);
		} catch (IOException e) {
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
	public Users getUsers() {
		return Users.newInstance(this.users);
	}
	
	private void startMonitoringXmlFile() {
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(new FileMonitor(
				this.xmlFile, new UsersUpdater(this)));
	}
	
}
