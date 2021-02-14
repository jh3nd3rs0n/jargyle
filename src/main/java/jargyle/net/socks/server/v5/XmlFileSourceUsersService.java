package jargyle.net.socks.server.v5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import jargyle.io.FileMonitor;
import jargyle.io.FileStatusListener;

public final class XmlFileSourceUsersService extends UsersService {
	
	private static final class UsersUpdater implements FileStatusListener {
		
		public static final Logger LOGGER = Logger.getLogger(
				UsersUpdater.class.getName());

		private final XmlFileSourceUsersService usersService;

		public UsersUpdater(final XmlFileSourceUsersService service) {
			this.usersService = service;
		}
		
		@Override
		public void fileCreated(final File file) {
			LOGGER.log(
					Level.INFO, 
					String.format(
							"File '%s' created. Updating users...", 
							file));
			if (this.updateFrom(file)) {
				LOGGER.log(
						Level.INFO, 
						"Users updated successfully");
			}
		}
		
		@Override
		public void fileDeleted(final File file) {
			LOGGER.log(
					Level.INFO, 
					String.format(
							"File '%s' deleted (using in-memory copy).", 
							file));
		}

		@Override
		public void fileModfied(final File file) {
			LOGGER.log(
					Level.INFO, 
					String.format(
							"File '%s' modified. Updating users...", 
							file));
			if (this.updateFrom(file)) {
				LOGGER.log(
						Level.INFO, 
						"Users updated successfully");
			}
		}

		private boolean updateFrom(final File file) {
			InputStream in = null;
			Users usrs = null;
			try {
				in = new FileInputStream(file);
				usrs = Users.newInstanceFrom(in);
			} catch (FileNotFoundException e) {
				LOGGER.log(
						Level.WARNING, 
						String.format(
								"File '%s' not found", 
								file), 
						e);
				return false;
			} catch (IOException e) {
				LOGGER.log(
						Level.WARNING, 
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
						LOGGER.log(
								Level.WARNING, 
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
	
	public static XmlFileSourceUsersService newInstance(final File xmlFile) {
		XmlFileSourceUsersService xmlFileSourceUsersService =
				new XmlFileSourceUsersService(xmlFile);
		xmlFileSourceUsersService.startMonitoringXmlFile();
		return xmlFileSourceUsersService;
	}
	
	private ExecutorService executor;
	private Users users;
	private final File xmlFile;
	
	private XmlFileSourceUsersService(final File file) {
		Objects.requireNonNull(file, "XML file must not be null");
		InputStream in = null;
		Users usrs = null;
		try {
			in = new FileInputStream(file);
			usrs = Users.newInstanceFrom(in);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e);
		} catch (IOException e) {
			throw new IllegalArgumentException(String.format(
					"error in reading XML file '%s'", file), 
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
		this.xmlFile = file;		
	}
	
	@Override
	public Users getUsers() {
		return Users.newInstance(this.users.toList());
	}
	
	private void startMonitoringXmlFile() {
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(new FileMonitor(
				this.xmlFile, new UsersUpdater(this)));
	}
	
}
