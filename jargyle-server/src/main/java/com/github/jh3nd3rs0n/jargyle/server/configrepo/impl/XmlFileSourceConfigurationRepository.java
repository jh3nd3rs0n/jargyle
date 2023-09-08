package com.github.jh3nd3rs0n.jargyle.server.configrepo.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.ConfigurationRepository;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.configrepo.impl.internal.config.xml.bind.ConfigurationXml;
import com.github.jh3nd3rs0n.jargyle.server.internal.io.FileMonitor;
import com.github.jh3nd3rs0n.jargyle.server.internal.io.FileStatusListener;

public final class XmlFileSourceConfigurationRepository 
	extends ConfigurationRepository {

	private static final class ConfigurationFileStatusListener 
		implements FileStatusListener {

		private final XmlFileSourceConfigurationRepository configurationRepository;
		private final Logger logger;
		
		public ConfigurationFileStatusListener(
				final XmlFileSourceConfigurationRepository repository) {
			this.configurationRepository = repository;
			this.logger = LoggerFactory.getLogger(
					ConfigurationFileStatusListener.class);
		}
		
		@Override
		public void onFileCreated(final File file) {
			this.logger.info(String.format(
					"Created file: %s",
					file));
			this.updateConfigurationRepositoryFrom(file);
		}

		@Override
		public void onFileDeleted(final File file) {
			this.logger.info(String.format(
					"Relying on in-memory copy of deleted file: %s",
					file));
		}

		@Override
		public void onFileModified(final File file) {
			this.logger.info(String.format(
					"Modified file: %s",
					file));
			this.updateConfigurationRepositoryFrom(file);
		}
		
		private void updateConfigurationRepositoryFrom(final File file) {
			try {
				this.configurationRepository.updateConfigurationFromXmlFile();
				this.logger.info("In-memory copy is up to date");
			} catch (UncheckedIOException e) {
				this.logger.error(
						String.format(
								"Error in reading file: %s", 
								file), 
						e);
			}
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
			return Configuration.newUnmodifiableInstance(
					Settings.getEmptyInstance());
		}
		FileInputStream in = null;
		ConfigurationXml configurationXml = null;
		try {
			in = new FileInputStream(xmlFile);
			configurationXml = ConfigurationXml.newInstanceFromXml(in);
		} catch (FileNotFoundException e) {
			throw new UncheckedIOException(e);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
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
		} catch (IOException e) {
			throw new UncheckedIOException(e);
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
			Files.move(
					tempXmlFile.toPath(), 
					xmlFile.toPath(),
					StandardCopyOption.ATOMIC_MOVE, 
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	public static void writeXsdTo(final OutputStream out) throws IOException {
		ConfigurationXml.writeXsdTo(out);
		return;
	}
	
	private volatile Configuration configuration;
	private ExecutorService executor;
	private final AtomicLong lastUpdated;
	private final ReentrantLock lock;	
	private final File xmlFile;
	
	private XmlFileSourceConfigurationRepository(final File file) {
		Objects.requireNonNull(file, "XML file must not be null");
		Configuration config = readConfigurationFrom(file);
		this.configuration = config;
		this.executor = null;
		this.lastUpdated = new AtomicLong(System.currentTimeMillis());
		this.lock = new ReentrantLock();
		this.xmlFile = file;
	}
	
	@Override
	public Configuration get() {
		Configuration config = null;
		this.lock.lock();
		try {
			config = Configuration.newUnmodifiableInstance(this.configuration);
		} finally {
			this.lock.unlock();
		}
		return config;
	}
	
	@Override
	public void set(final Configuration config) {
		this.lock.lock();
		try {
			this.updateXmlFileFrom(config);
			this.updateConfigurationFrom(config);
		} finally {
			this.lock.unlock();
		}
	}
	
	private void startMonitoringXmlFile() {
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(FileMonitor.newInstance(
				this.xmlFile, 
				new ConfigurationFileStatusListener(this)));
	}

	private void updateConfigurationFrom(final Configuration config) {
		this.configuration = config;
		this.lastUpdated.set(System.currentTimeMillis());
	}
	
	private void updateConfigurationFromXmlFile() {
		this.lock.lock();
		try {
			if (this.xmlFile.exists() 
					&& this.xmlFile.lastModified() > this.lastUpdated.longValue()) {
				Configuration config = readConfigurationFrom(this.xmlFile);
				this.updateConfigurationFrom(config);
			}
		} finally {
			this.lock.unlock();
		}
	}

	private void updateXmlFileFrom(final Configuration config) {
		writeConfigurationTo(this.xmlFile, config);		
	}
}
