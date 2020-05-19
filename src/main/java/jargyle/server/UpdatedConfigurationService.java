package jargyle.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;

public final class UpdatedConfigurationService 
	implements ConfigurationService {

	private static final class ConfigurationUpdater 
		implements FileStatusListener {

		private final UpdatedConfigurationService configurationService;
		
		public ConfigurationUpdater(
				final UpdatedConfigurationService service) {
			this.configurationService = service;
		}
		
		@Override
		public void fileCreated(final File file) {
			this.configurationService.logger.log(
					Level.INFO, 
					String.format(
							"File '%s' created. Updating configuration...", 
							file));
			if (this.updateFrom(file)) {
				this.configurationService.logger.log(
						Level.INFO, 
						"Configuration updated successfully");
			}
		}

		@Override
		public void fileDeleted(final File file) {
			this.configurationService.logger.log(
					Level.INFO, 
					String.format(
							"File '%s' deleted (using in-memory copy).", 
							file));
		}

		@Override
		public void fileModfied(final File file) {
			this.configurationService.logger.log(
					Level.INFO, 
					String.format(
							"File '%s' modified. Updating configuration...", 
							file));
			if (this.updateFrom(file)) {
				this.configurationService.logger.log(
						Level.INFO, 
						"Configuration updated successfully");
			}
		}
		
		private boolean updateFrom(final File file) {
			Configuration config = null;
			try {
				config = newConfiguration(file);
			} catch (FileNotFoundException e) {
				this.configurationService.logger.log(
						Level.WARNING, 
						String.format(
								"File '%s' not found", 
								file), 
						e);
				return false;				
			} catch (JAXBException e) {
				this.configurationService.logger.log(
						Level.WARNING, 
						String.format(
								"File '%s' not valid", 
								file), 
						e);
				return false;
			}
			this.configurationService.configuration = config;
			return true;
		}
		
	}
	
	private static Configuration newConfiguration(
			final File file) throws FileNotFoundException, JAXBException {
		InputStream in = new FileInputStream(file);
		Configuration configuration = null;
		try {
			JAXBContext jaxbContext = null;
			try {
				jaxbContext = JAXBContext.newInstance(
						UnmodifiableConfiguration.ConfigurationXml.class);
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
			UnmodifiableConfiguration.ConfigurationXml configurationXml =
					(UnmodifiableConfiguration.ConfigurationXml) unmarshaller.unmarshal(in);
			configuration = UnmodifiableConfiguration.newInstance(configurationXml);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				throw new AssertionError(e);
			}
		}
		return configuration;
	}
	
	private Configuration configuration;
	private final File configurationFile;
	private ExecutorService executor;
	private final Logger logger;
	private boolean started;
	private boolean stopped;
	
	public UpdatedConfigurationService(
			final File configFile, final Logger lggr) {
		if (configFile == null) {
			throw new NullPointerException("configuration file must not be null");
		}
		if (lggr == null) {
			throw new NullPointerException("logger must not be null");
		}
		if (!configFile.exists()) {
			throw new IllegalArgumentException(String.format(
					"'%s' does not exist", configFile));
		}
		if (!configFile.isFile()) {
			throw new IllegalArgumentException(String.format(
					"'%s' is not a file", configFile));
		}
		Configuration config = null;
		try {
			config = newConfiguration(configFile);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(String.format(
					"file '%s' does not exist", configFile));
		} catch (JAXBException e) {
			throw new IllegalArgumentException(String.format(
					"file '%s' not a valid XML file: %s", 
					configFile, e.toString()), e);
		}
		this.configuration = config;
		this.configurationFile = configFile;
		this.executor = null;
		this.logger = lggr;
		this.started = false;
		this.stopped = true;
	}
	
	@Override
	public Configuration getConfiguration() {
		return this.configuration;
	}
	
	public boolean isStarted() {
		return this.started;
	}
	
	public boolean isStopped() {
		return this.stopped;
	}
	
	public void start() {
		if (this.started) {
			throw new IllegalStateException(
					"UpdatableConfigurationFileService is already started");
		}
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(new FileMonitor(
				this.configurationFile, 
				new ConfigurationUpdater(this), 
				LoggerHolder.LOGGER));
		this.started = true;
		this.stopped = false;
	}
	
	public void stop() {
		if (this.stopped) {
			throw new IllegalStateException(
					"UpdatableConfigurationFileService is already stopped");
		}
		this.executor.shutdownNow();
		this.executor = null;
		this.started = false;
		this.stopped = true;
	}

}
