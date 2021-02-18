package jargyle.net.socks.server;

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

public final class XmlFileSourceConfigurationService 
	extends ConfigurationService {

	private static final class ConfigurationUpdater 
		implements FileStatusListener {
		
		private static final Logger LOGGER = LoggerFactory.getLogger(
				ConfigurationUpdater.class);

		private final XmlFileSourceConfigurationService configurationService;
		
		public ConfigurationUpdater(
				final XmlFileSourceConfigurationService service) {
			this.configurationService = service;
		}
		
		@Override
		public void fileCreated(final File file) {
			LOGGER.info(String.format(
					"File '%s' created. Updating configuration...",
					file));
			if (this.updateFrom(file)) {
				LOGGER.info("Configuration updated successfully");
			}
		}

		@Override
		public void fileDeleted(final File file) {
			LOGGER.info(String.format(
					"File '%s' deleted (using in-memory copy).",
					file));
		}

		@Override
		public void fileModfied(final File file) {
			LOGGER.info(String.format(
					"File '%s' modified. Updating configuration...",
					file));
			if (this.updateFrom(file)) {
				LOGGER.info("Configuration updated successfully");
			}
		}
		
		private boolean updateFrom(final File file) {
			InputStream in = null;
			Configuration config = null;
			try {
				in = new FileInputStream(file);
				config = ImmutableConfiguration.newInstanceFrom(in);
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
			this.configurationService.configuration = config;
			return true;
		}
		
	}
	
	public static XmlFileSourceConfigurationService newInstance(
			final File xmlFile) {
		XmlFileSourceConfigurationService configurationService = 
				new XmlFileSourceConfigurationService(xmlFile);
		configurationService.startMonitoringXmlFile();
		return configurationService;
	}
	
	private Configuration configuration;
	private ExecutorService executor;
	private final File xmlFile;
	
	private XmlFileSourceConfigurationService(final File file) {
		Objects.requireNonNull(file, "XML file must not be null");
		InputStream in = null;
		Configuration config = null;
		try {
			in = new FileInputStream(file);
			config = ImmutableConfiguration.newInstanceFrom(in);
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
		this.configuration = config;
		this.executor = null;
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
				new ConfigurationUpdater(this)));
	}

}
