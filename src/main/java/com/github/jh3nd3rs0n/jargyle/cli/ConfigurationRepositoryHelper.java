package com.github.jh3nd3rs0n.jargyle.cli;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.github.jh3nd3rs0n.jargyle.server.ConfigurationRepository;
import com.github.jh3nd3rs0n.jargyle.server.configrepo.impl.XmlFileSourceConfigurationRepository;

final class ConfigurationRepositoryHelper {

	public static ConfigurationRepository newConfigurationRepository(
			final File file) {
		return XmlFileSourceConfigurationRepository.newInstance(file);
	}
	
	public static void writeConfigurationSchemaTo(
			final OutputStream out) throws IOException {
		XmlFileSourceConfigurationRepository.writeSchemaTo(out);
	}
	
	private ConfigurationRepositoryHelper() { }
	
}
