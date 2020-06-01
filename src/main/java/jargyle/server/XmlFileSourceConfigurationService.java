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
			InputStream in = null;
			Configuration config = null;
			try {
				in = new FileInputStream(file);
				config = ImmutableConfiguration.newInstanceFrom(in);
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
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						this.configurationService.logger.log(
								Level.WARNING, 
								String.format(
										"Unable to close input stream of file '%s'", 
										file), 
								e);
					}
				}
			}
			this.configurationService.configuration = config;
			return true;
		}
		
	}
	
	public static XmlFileSourceConfigurationService newInstance(
			final File xmlFile, final Logger logger) {
		XmlFileSourceConfigurationService configurationService = 
				new XmlFileSourceConfigurationService(xmlFile, logger);
		configurationService.startMonitoringXmlFile();
		return configurationService;
	}
	
	private Configuration configuration;
	private ExecutorService executor;
	private final Logger logger;
	private final File xmlFile;
	
	private XmlFileSourceConfigurationService(
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
		InputStream in = null;
		Configuration config = null;
		try {
			in = new FileInputStream(file);
			config = ImmutableConfiguration.newInstanceFrom(in);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(String.format(
					"file '%s' does not exist", file));
		} catch (JAXBException e) {
			throw new IllegalArgumentException(String.format(
					"file '%s' not a valid XML file: %s", 
					file, e.toString()), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw new AssertionError(e);
				}
			}
		}
		this.configuration = config;
		this.executor = null;
		this.logger = lggr;
		this.xmlFile = file;
	}
	
	@Override
	public Configuration getConfiguration() {
		return this.configuration;
	}
	
	private void startMonitoringXmlFile() {
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(new FileMonitor(
				this.xmlFile, 
				new ConfigurationUpdater(this), 
				this.logger));
	}

}
