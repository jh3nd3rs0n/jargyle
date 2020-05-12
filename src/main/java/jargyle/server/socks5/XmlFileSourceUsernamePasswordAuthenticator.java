package jargyle.server.socks5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;

public final class XmlFileSourceUsernamePasswordAuthenticator 
	extends UsernamePasswordAuthenticator {
	
	private static final class FileMonitor implements Runnable {

		private final File file;
		private final FileStatusListener fileStatusListener;
		
		public FileMonitor(final File f, final FileStatusListener listener) {
			this.file = f;
			this.fileStatusListener = listener;
		}
		
		@Override
		public void run() {
			File absoluteFile = this.file.getAbsoluteFile();
			String parent = absoluteFile.getParent();
			Path dir = Paths.get(parent);
			WatchService watcher = null;
			try {
				watcher = FileSystems.getDefault().newWatchService();
			} catch (IOException e) {
				LoggerHolder.LOGGER.log(
						Level.WARNING, 
						"Unable to create WatchService", 
						e);
				return;
			}
			WatchKey key = null;
			try {
				key = dir.register(
						watcher,
						StandardWatchEventKinds.ENTRY_CREATE,
						StandardWatchEventKinds.ENTRY_DELETE,
						StandardWatchEventKinds.ENTRY_MODIFY);
			} catch (IOException e) {
				LoggerHolder.LOGGER.log(
						Level.WARNING, 
						String.format(
								"Unable to register '%s' to WatchService", 
								parent), 
						e);
				return;
			}
			while (true) {
				WatchKey k = null;
				try {
					k = watcher.take();
				} catch (InterruptedException e) {
					return;
				}
				if (key != k) {
					continue;
				}
				for (WatchEvent<?> event : k.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();
					if (kind == StandardWatchEventKinds.OVERFLOW) {
						continue;
					}
					@SuppressWarnings("unchecked")
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path filename = ev.context();
					Path child = dir.resolve(filename);
					if (absoluteFile.equals(child.toFile())) {
						if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
							this.fileStatusListener.fileCreated(absoluteFile);
						}
						if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
							this.fileStatusListener.fileDeleted(absoluteFile);
						}
						if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
							this.fileStatusListener.fileModfied(absoluteFile);
						}
					}
				}
				boolean valid = k.reset();
				if (!valid) {
					break;
				}
			}
		}
		
	}
	
	private static interface FileStatusListener {
		
		void fileCreated(final File file);
		
		void fileDeleted(final File file);
		
		void fileModfied(final File file);
		
	}
	
	private static final class LoggerHolder {
		
		public static final Logger LOGGER = Logger.getLogger(
				XmlFileSourceUsernamePasswordAuthenticator.class.getName());
		
		private LoggerHolder() { }
		
	}
	
	private static final class UsersUpdater implements FileStatusListener {

		private final XmlFileSourceUsernamePasswordAuthenticator authenticator;

		public UsersUpdater(
				final XmlFileSourceUsernamePasswordAuthenticator auth) {
			this.authenticator = auth;
		}
		
		@Override
		public void fileCreated(final File file) {
			LoggerHolder.LOGGER.log(
					Level.INFO, 
					String.format(
							"File '%s' created. Updating users...", 
							file));
			if (this.updateFrom(file)) {
				LoggerHolder.LOGGER.log(
						Level.INFO, 
						"Users updated successfully");
			}
		}
		
		@Override
		public void fileDeleted(final File file) {
			LoggerHolder.LOGGER.log(
					Level.INFO, 
					String.format(
							"File '%s' deleted (using in-memory copy).", 
							file));
		}

		@Override
		public void fileModfied(final File file) {
			LoggerHolder.LOGGER.log(
					Level.INFO, 
					String.format(
							"File '%s' modified. Updating users...", 
							file));
			if (this.updateFrom(file)) {
				LoggerHolder.LOGGER.log(
						Level.INFO, 
						"Users updated successfully");
			}
		}

		private boolean updateFrom(final File file) {
			Users usrs = null;
			try {
				usrs = newUsers(this.authenticator.xmlFile);
			} catch (FileNotFoundException e) {
				LoggerHolder.LOGGER.log(
						Level.WARNING, 
						String.format(
								"File '%s' not found", 
								file), 
						e);
				return false;
			} catch (JAXBException e) {
				LoggerHolder.LOGGER.log(
						Level.WARNING, 
						String.format(
								"File '%s' not valid", 
								file), 
						e);
				return false;
			}
			this.authenticator.users = usrs;
			return true;
		}
		
	}
	
	private static Users newUsers(
			final File file) throws FileNotFoundException, JAXBException {
		InputStream in = new FileInputStream(file);
		Users users = null;
		try {
			JAXBContext jaxbContext = null;
			try {
				jaxbContext = JAXBContext.newInstance(Users.UsersXml.class);
			} catch (JAXBException e) {
				throw new AssertionError(e);
			}
			Unmarshaller unmarshaller = null;
			try {
				unmarshaller = jaxbContext.createUnmarshaller();
			} catch (JAXBException e) {
				throw new AssertionError(e);
			}
			try {
				unmarshaller.setEventHandler(new DefaultValidationEventHandler());
			} catch (JAXBException e) {
				throw new AssertionError(e);
			}
			Users.UsersXml usersXml = 
					(Users.UsersXml) unmarshaller.unmarshal(in);
			users = Users.newInstance(usersXml);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				throw new AssertionError(e);
			}
		}
		return users;
	}
	
	private Users users;
	private final File xmlFile;
	
	public XmlFileSourceUsernamePasswordAuthenticator(
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
		Users usrs = null;
		try {
			usrs = newUsers(file);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(String.format(
					"file '%s' does not exist", paramString));
		} catch (JAXBException e) {
			throw new IllegalArgumentException(String.format(
					"file '%s' not a valid XML file: %s", 
					paramString, e.toString()), e);
		}
		this.users = usrs;
		this.xmlFile = file;
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(new FileMonitor(this.xmlFile, new UsersUpdater(this)));
	}
	
	@Override
	protected boolean authenticate(
			final String username, final char[] password) {
		if (this.users.toList().size() == 0) { return false; }
		User user = this.users.getLast(username);
		if (user == null) { return false; }
		PasswordHash passwordHash = user.getPasswordHash();
		PasswordHash otherPasswordHash = PasswordHash.newInstance(
				password, passwordHash.getSalt());
		if (!passwordHash.equals(otherPasswordHash)) { return false; }
		return true;
	}

}
