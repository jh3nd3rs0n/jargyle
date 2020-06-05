package jargyle.server.socks5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import jargyle.server.FileMonitor;
import jargyle.server.FileStatusListener;

public final class XmlFileSourceUsernamePasswordAuthenticator 
	extends UsernamePasswordAuthenticator {
	
	private static final class UsersUpdater implements FileStatusListener {
		
		public static final Logger LOGGER = Logger.getLogger(
				UsersUpdater.class.getName());

		private final XmlFileSourceUsernamePasswordAuthenticator authenticator;

		public UsersUpdater(
				final XmlFileSourceUsernamePasswordAuthenticator auth) {
			this.authenticator = auth;
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
			} catch (JAXBException e) {
				LOGGER.log(
						Level.WARNING, 
						String.format(
								"File '%s' not valid", 
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
			this.authenticator.users = usrs;
			return true;
		}
		
	}
	
	public static XmlFileSourceUsernamePasswordAuthenticator newInstance(
			final String parameterString) {
		XmlFileSourceUsernamePasswordAuthenticator usernamePasswordAuthenticator =
				new XmlFileSourceUsernamePasswordAuthenticator(parameterString);
		usernamePasswordAuthenticator.startMonitoringXmlFile();
		return usernamePasswordAuthenticator;
	}
	
	private ExecutorService executor;
	private Users users;
	private final File xmlFile;
	
	private XmlFileSourceUsernamePasswordAuthenticator(
			final String paramString) {
		super(paramString);
		File file = new File(paramString);
		if (!file.exists()) {
			throw new IllegalArgumentException(String.format(
					"'%s' does not exist", paramString));
		}
		if (!file.isFile()) {
			throw new IllegalArgumentException(String.format(
					"'%s' is not a file", paramString));
		}
		InputStream in = null;
		Users usrs = null;
		try {
			in = new FileInputStream(file);
			usrs = Users.newInstanceFrom(in);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(String.format(
					"file '%s' does not exist", paramString));
		} catch (JAXBException e) {
			throw new IllegalArgumentException(String.format(
					"file '%s' not a valid XML file: %s", 
					paramString, e.toString()), e);
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
	public boolean authenticate(
			final String username, final char[] password) {
		if (this.users.toList().size() == 0) { return false; }
		User user = this.users.getLast(username);
		if (user == null) { return false; }
		HashedPassword hashedPassword = user.getHashedPassword();
		HashedPassword otherHashedPassword = HashedPassword.newInstance(
				password, hashedPassword);
		if (!hashedPassword.equals(otherHashedPassword)) { return false; }
		return true;
	}
	
	private void startMonitoringXmlFile() {
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(new FileMonitor(
				this.xmlFile, new UsersUpdater(this)));
	}

}
