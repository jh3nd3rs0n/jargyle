package com.github.jh3nd3rs0n.jargyle.cli;

import java.io.File;

import com.github.jh3nd3rs0n.jargyle.server.ConfigurationRepository;
import com.github.jh3nd3rs0n.jargyle.server.configrepo.impl.XmlFileSourceConfigurationRepository;

final class ConfigurationRepositoryHelper {

	public static ConfigurationRepository newFileSourceConfigurationRepository(
			final File file) {
		return XmlFileSourceConfigurationRepository.newInstance(file);
	}
	
	private ConfigurationRepositoryHelper() { }
	
}
