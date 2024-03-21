package com.github.jh3nd3rs0n.jargyle.server.internal.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import jakarta.xml.bind.*;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.helpers.DefaultValidationEventHandler;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "configuration", propOrder = { })
@XmlRootElement(name = "configuration") 
public class ConfigurationXml {

	public static ConfigurationXml newInstanceFrom(
			final InputStream in) throws IOException {
		JAXBContext jaxbContext = null;
		try {
			jaxbContext = JAXBContext.newInstance(ConfigurationXml.class);
		} catch (JAXBException e) {
			throw new AssertionError(e);
		}
		Unmarshaller unmarshaller = null;
		try {
			unmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			throw new AssertionError(e);
		}
		try {
			unmarshaller.setEventHandler(new DefaultValidationEventHandler());
		} catch (JAXBException e) {
			throw new AssertionError(e);
		}
		try {
			return (ConfigurationXml) unmarshaller.unmarshal(in);
		} catch (JAXBException e) {
			throw new IOException(e);
		}
	}

	public static void writeSchemaTo(final OutputStream out) throws IOException {
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

	@XmlElement(name = "settings")
	protected SettingsXml settingsXml;
	
	public ConfigurationXml() { this.settingsXml = null; }
	
	public ConfigurationXml(final Configuration config) {
		this.settingsXml = new SettingsXml(config.getSettings());
	}

    public Configuration toConfiguration() {
		return Configuration.newUnmodifiableInstance(
				this.settingsXml.toSettings());
	}
	
	public void toOutput(final OutputStream out) throws IOException {
		JAXBContext jaxbContext = null;
		try {
			jaxbContext = JAXBContext.newInstance(ConfigurationXml.class);
		} catch (JAXBException e) {
			throw new AssertionError(e);
		}
		Marshaller marshaller = null;
		try {
			marshaller = jaxbContext.createMarshaller();
		} catch (JAXBException e) {
			throw new AssertionError(e);
		}
		try {
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		} catch (PropertyException e) {
			throw new AssertionError(e);
		}
		try {
			marshaller.marshal(this, out);
		} catch (JAXBException e) {
			throw new IOException(e);
		}
	}

}