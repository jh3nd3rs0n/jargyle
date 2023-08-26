package com.github.jh3nd3rs0n.jargyle.server.configrepo.impl.internal.config.xml.bind;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.PropertyException;
import jakarta.xml.bind.SchemaOutputResolver;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.helpers.DefaultValidationEventHandler;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "configuration", propOrder = { })
@XmlRootElement(name = "configuration") 
public class ConfigurationXml {

	public static ConfigurationXml newInstanceFromXml(
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
	
	public static void xsd(final OutputStream out) throws IOException {
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
	
	public void toXml(final OutputStream out) throws IOException {
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