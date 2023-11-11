package com.github.jh3nd3rs0n.jargyle.server;

import java.io.IOException;
import java.io.OutputStream;

import com.github.jh3nd3rs0n.jargyle.server.internal.configrepo.impl.ConfigurationXmlFileSchemaHelper;

public final class ConfigurationFileSchemaHelper {

	public static void writeSchemaTo(final OutputStream out) throws IOException {
		ConfigurationXmlFileSchemaHelper.writeSchemaTo(out);
	}
	
	private ConfigurationFileSchemaHelper() { }

}
