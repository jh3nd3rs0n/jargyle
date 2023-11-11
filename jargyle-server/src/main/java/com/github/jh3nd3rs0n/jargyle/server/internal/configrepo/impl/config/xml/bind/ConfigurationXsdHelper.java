package com.github.jh3nd3rs0n.jargyle.server.internal.configrepo.impl.config.xml.bind;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.SchemaOutputResolver;

public final class ConfigurationXsdHelper {

	public static void writeXsdTo(final OutputStream out) throws IOException {
		JAXBContext jaxbContext = null;
		try {
			jaxbContext = JAXBContext.newInstance(ConfigurationXml.class);
		} catch (JAXBException e) {
			throw new AssertionError(e);
		}
		jaxbContext.generateSchema(new SchemaOutputResolver() {
	
			@Override
			public Result createOutput(
					final String namespaceUri, 
					final String suggestedFileName) throws IOException {
				StreamResult result = new StreamResult(out);
				result.setSystemId(suggestedFileName);
				return result;
			}
			
		});		
	}

	private ConfigurationXsdHelper() { }
	
}
