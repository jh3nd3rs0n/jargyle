package com.github.jh3nd3rs0n.jargyle.server.internal.configrepo.impl;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.ConfigurationRepository;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.internal.config.xml.bind.ConfigurationXml;
import com.github.jh3nd3rs0n.jargyle.server.internal.io.FileMonitor;
import com.github.jh3nd3rs0n.jargyle.server.internal.io.FileStatusListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public final class FileSourceConfigurationRepository
	extends ConfigurationRepository {

	private static final class ConfigurationFileStatusListener 
		implements FileStatusListener {

		private final FileSourceConfigurationRepository configurationRepository;
		private final Logger logger;
		
		public ConfigurationFileStatusListener(
				final FileSourceConfigurationRepository repository) {
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
				this.configurationRepository.updateConfigurationFromFile();
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
	
	public static FileSourceConfigurationRepository newInstance(
			final File file) {
		FileSourceConfigurationRepository fileSourceConfigurationRepository =
				new FileSourceConfigurationRepository(file);
		fileSourceConfigurationRepository.startMonitoringFile();
		return fileSourceConfigurationRepository;
	}
	
	private static Configuration readConfigurationFrom(final File file) {
		if (!file.exists()) {
			return Configuration.newUnmodifiableInstance(
					Settings.getEmptyInstance());
		}
		FileInputStream in = null;
		ConfigurationXml configurationXml = null;
		try {
			in = new FileInputStream(file);
			configurationXml = ConfigurationXml.newInstanceFrom(in);
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
			final File file, final Configuration configuration) {
		File tempFile = new File(file.toString().concat(".tmp"));
		FileOutputStream out = null;
		try {
			ConfigurationXml configurationXml = new ConfigurationXml(
					configuration);			
			out = new FileOutputStream(tempFile);
			configurationXml.toOutput(out);
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
					tempFile.toPath(),
					file.toPath(),
					StandardCopyOption.ATOMIC_MOVE, 
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	private volatile Configuration configuration;
	private ExecutorService executor;
	private final File file;
	private final AtomicLong lastUpdated;
	private final ReentrantLock lock;	

	private FileSourceConfigurationRepository(final File f) {
		Objects.requireNonNull(f, "file must not be null");
		this.configuration = readConfigurationFrom(f);
		this.executor = null;
		this.file = f;
		this.lastUpdated = new AtomicLong(System.currentTimeMillis());
		this.lock = new ReentrantLock();
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
			this.updateFileFrom(config);
			this.updateConfigurationFrom(config);
		} finally {
			this.lock.unlock();
		}
	}
	
	private void startMonitoringFile() {
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(FileMonitor.newInstance(
				this.file,
				new ConfigurationFileStatusListener(this)));
	}

	private void updateConfigurationFrom(final Configuration config) {
		this.configuration = config;
		this.lastUpdated.set(System.currentTimeMillis());
	}
	
	private void updateConfigurationFromFile() {
		this.lock.lock();
		try {
			if (this.file.exists()
					&& this.file.lastModified() > this.lastUpdated.longValue()) {
				Configuration config = readConfigurationFrom(this.file);
				this.updateConfigurationFrom(config);
			}
		} finally {
			this.lock.unlock();
		}
	}

	private void updateFileFrom(final Configuration config) {
		writeConfigurationTo(this.file, config);
	}
}
