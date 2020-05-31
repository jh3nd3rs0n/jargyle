package jargyle.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

public final class XmlFileSourceConfigurationService 
	implements ConfigurationService {

	private static final class ConfigurationUpdater 
		implements FileStatusListener {

		private final XmlFileSourceConfigurationService configurationService;
		
		public ConfigurationUpdater(
				final XmlFileSourceConfigurationService service) {
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
				config = ImmutableConfiguration.newInstanceFrom(
						new FileInputStream(file));
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
	
	private Configuration configuration;
	private final Logger logger;
	private final File xmlFile;
	
	public XmlFileSourceConfigurationService(
			final File file, final Logger lggr) {
		if (file == null) {
			throw new NullPointerException("XML file must not be null");
		}
		if (lggr == null) {
			throw new NullPointerException("logger must not be null");
		}
		if (!file.exists()) {
			throw new IllegalArgumentException(String.format(
					"'%s' does not exist", file));
		}
		if (!file.isFile()) {
			throw new IllegalArgumentException(String.format(
					"'%s' is not a file", file));
		}
		Configuration config = null;
		try {
			config = ImmutableConfiguration.newInstanceFrom(
					new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(String.format(
					"file '%s' does not exist", file));
		} catch (JAXBException e) {
			throw new IllegalArgumentException(String.format(
					"file '%s' not a valid XML file: %s", 
					file, e.toString()), e);
		}
		this.configuration = config;
		this.logger = lggr;
		this.xmlFile = file;
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(new FileMonitor(
				this.xmlFile, 
				new ConfigurationUpdater(this), 
				this.logger));
	}
	
	@Override
	public Configuration getConfiguration() {
		return this.configuration;
	}

}
