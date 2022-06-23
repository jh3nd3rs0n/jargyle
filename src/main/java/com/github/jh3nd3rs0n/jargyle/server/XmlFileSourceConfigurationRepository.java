package com.github.jh3nd3rs0n.jargyle.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.server.internal.config.xml.bind.ConfigurationXml;
import com.github.jh3nd3rs0n.jargyle.server.internal.io.FileMonitor;
import com.github.jh3nd3rs0n.jargyle.server.internal.io.FileStatusListener;

import jakarta.xml.bind.JAXBException;

public final class XmlFileSourceConfigurationRepository 
	extends ConfigurationRepository {

	private static final class ConfigurationFileStatusListener 
		implements FileStatusListener {
		
		private static final Logger LOGGER = LoggerFactory.getLogger(
				ConfigurationFileStatusListener.class);

		private final XmlFileSourceConfigurationRepository configurationRepository;
		
		public ConfigurationFileStatusListener(
				final XmlFileSourceConfigurationRepository repository) {
			this.configurationRepository = repository;
		}
		
		@Override
		public void onFileCreated(final File file) {
			LOGGER.info(String.format(
					"File '%s' created",
					file));
			if (this.updateConfigurationFrom(file)) {
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
			if (this.updateConfigurationFrom(file)) {
				LOGGER.info("In-memory copy updated successfully");
			}
		}
		
		private boolean updateConfigurationFrom(final File file) {
			try {
				this.configurationRepository.update();
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
	
	public static XmlFileSourceConfigurationRepository newInstance(
			final File xmlFile) {
		XmlFileSourceConfigurationRepository xmlFileSourceConfigurationRepository = 
				new XmlFileSourceConfigurationRepository(xmlFile);
		xmlFileSourceConfigurationRepository.startMonitoringXmlFile();
		return xmlFileSourceConfigurationRepository;
	}
	
	private static Configuration readConfigurationFrom(final File xmlFile) {
		if (!xmlFile.exists()) {
			return ImmutableConfiguration.newInstance(
					Settings.getEmptyInstance());
		}
		FileInputStream in = null;
		ConfigurationXml configurationXml = null;
		try {
			in = new FileInputStream(xmlFile);
			configurationXml = ConfigurationXml.newInstanceFromXml(in);
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
		return configurationXml.toConfiguration();
	}
	
	private static void writeConfigurationTo(
			final File xmlFile, final Configuration configuration) {
		File tempXmlFile = new File(xmlFile.toString().concat(".tmp"));
		FileOutputStream out = null;
		try {
			ConfigurationXml configurationXml = new ConfigurationXml(
					configuration);			
			out = new FileOutputStream(tempXmlFile);
			configurationXml.toXml(out);
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
		try {
			Files.move(tempXmlFile.toPath(), xmlFile.toPath());
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	private volatile Configuration configuration;
	private ExecutorService executor;
	private final File xmlFile;
	
	private XmlFileSourceConfigurationRepository(final File file) {
		Objects.requireNonNull(file, "XML file must not be null");
		Configuration config = readConfigurationFrom(file);
		this.configuration = config;
		this.executor = null;
		this.xmlFile = file;
	}
	
	@Override
	public Configuration get() {
		return this.configuration;
	}
	
	@Override
	public void set(final Configuration config) {
		writeConfigurationTo(this.xmlFile, config);
	}
	
	private void startMonitoringXmlFile() {
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(FileMonitor.newInstance(
				this.xmlFile, 
				new ConfigurationFileStatusListener(this)));
	}

	private void update() {
		Configuration config = readConfigurationFrom(this.xmlFile);
		this.configuration = config;
	}

}
