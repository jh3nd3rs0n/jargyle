package com.github.jh3nd3rs0n.jargyle.server.internal.configrepo.impl;

import java.io.IOException;
import java.io.OutputStream;

import com.github.jh3nd3rs0n.jargyle.server.internal.configrepo.impl.config.xml.bind.ConfigurationXsdHelper;

public final class ConfigurationXmlFileSchemaHelper {

	public static void writeSchemaTo(final OutputStream out) throws IOException {
		ConfigurationXsdHelper.writeXsdTo(out);
	}
	
	private ConfigurationXmlFileSchemaHelper() { }

}
