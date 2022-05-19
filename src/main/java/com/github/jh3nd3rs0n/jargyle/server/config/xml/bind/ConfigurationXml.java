package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.io.InputStream;
import java.io.OutputStream;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.ImmutableConfiguration;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
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
			final InputStream in) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(
				ConfigurationXml.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setEventHandler(new DefaultValidationEventHandler());
		return (ConfigurationXml) unmarshaller.unmarshal(in);
	}
	
	@XmlElement(name = "settings")
	protected SettingsXml settingsXml;
	
	public ConfigurationXml() { this.settingsXml = null; }
	
	public ConfigurationXml(final Configuration config) {
		this.settingsXml = new SettingsXml(config.getSettings());
	}
	
	public Configuration toConfiguration() {
		return ImmutableConfiguration.newInstance(
				this.settingsXml.toSettings());
	}
	
	public void toXml(final OutputStream out) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(
				ConfigurationXml.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(this, out);
	}
	
}