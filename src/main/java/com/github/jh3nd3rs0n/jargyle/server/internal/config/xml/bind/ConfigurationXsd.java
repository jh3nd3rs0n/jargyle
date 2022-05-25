package com.github.jh3nd3rs0n.jargyle.server.internal.config.xml.bind;

import java.io.IOException;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.SchemaOutputResolver;

public class ConfigurationXsd {
	
	private static final class CustomSchemaOutputResolver 
		extends SchemaOutputResolver {
	
		private final Result result;
		
		public CustomSchemaOutputResolver(final Result res) {
			this.result = res;
		}
		
		@Override
		public Result createOutput(
				final String namespaceUri, 
				final String suggestedFileName) throws IOException {
			return this.result;
		}
		
	}

	public static void generateXsd(final SchemaOutputResolver resolver) 
			throws JAXBException, IOException {
		JAXBContext jaxbContext = JAXBContext.newInstance(
				ConfigurationXml.class);
		jaxbContext.generateSchema(resolver);			
	}

	public static void main(final String[] args) {
		StreamResult result = new StreamResult(System.out);
		result.setSystemId("");
		try {
			ConfigurationXsd.generateXsd(
					new CustomSchemaOutputResolver(result));
		} catch (JAXBException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.flush();		
	}
	
	private ConfigurationXsd() { }

}
