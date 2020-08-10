package jargyle.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import jargyle.client.socks5.UsernamePassword;
import jargyle.server.socks5.Socks5RequestCriteria;
import jargyle.server.socks5.UsernamePasswordAuthenticator;

public final class ImmutableConfiguration extends Configuration {

	public static final class Builder {
		
		private Criteria allowedClientAddressCriteria;
		private Criteria allowedSocks5IncomingTcpAddressCriteria;
		private Criteria allowedSocks5IncomingUdpAddressCriteria;
		private Socks5RequestCriteria allowedSocks5RequestCriteria;
		private Criteria blockedClientAddressCriteria;
		private Criteria blockedSocks5IncomingTcpAddressCriteria;
		private Criteria blockedSocks5IncomingUdpAddressCriteria;
		private Socks5RequestCriteria blockedSocks5RequestCriteria;
		private UsernamePassword externalClientSocks5UsernamePassword;
		private Settings settings;
		private UsernamePasswordAuthenticator socks5UsernamePasswordAuthenticator;
				
		public Builder() {
			this.allowedClientAddressCriteria = null;
			this.allowedSocks5IncomingTcpAddressCriteria = null;
			this.allowedSocks5IncomingUdpAddressCriteria = null;
			this.allowedSocks5RequestCriteria = null;
			this.blockedClientAddressCriteria = null;
			this.blockedSocks5IncomingTcpAddressCriteria = null;
			this.blockedSocks5IncomingUdpAddressCriteria = null;
			this.blockedSocks5RequestCriteria = null;
			this.externalClientSocks5UsernamePassword = null;
			this.settings = null;
			this.socks5UsernamePasswordAuthenticator = null;
		}
		
		public Builder allowedClientAddressCriteria(
				final Criteria allowedClientAddrCriteria) {
			this.allowedClientAddressCriteria = allowedClientAddrCriteria;
			return this;
		}
		
		public Builder allowedSocks5IncomingTcpAddressCriteria(
				final Criteria allowedSocks5IncomingTcpAddrCriteria) {
			this.allowedSocks5IncomingTcpAddressCriteria = 
					allowedSocks5IncomingTcpAddrCriteria;
			return this;
		}
		
		public Builder allowedSocks5IncomingUdpAddressCriteria(
				final Criteria allowedSocks5IncomingUdpAddrCriteria) {
			this.allowedSocks5IncomingUdpAddressCriteria =
					allowedSocks5IncomingUdpAddrCriteria;
			return this;
		}
		
		public Builder allowedSocks5RequestCriteria(
				final Socks5RequestCriteria allowedSocks5ReqCriteria) {
			this.allowedSocks5RequestCriteria = allowedSocks5ReqCriteria;
			return this;
		}
		
		public Builder blockedClientAddressCriteria(
				final Criteria blockedClientAddrCriteria) {
			this.blockedClientAddressCriteria = blockedClientAddrCriteria;
			return this;
		}
		
		public Builder blockedSocks5IncomingTcpAddressCriteria(
				final Criteria blockedSocks5IncomingTcpAddrCriteria) {
			this.blockedSocks5IncomingTcpAddressCriteria =
					blockedSocks5IncomingTcpAddrCriteria;
			return this;
		}
		
		public Builder blockedSocks5IncomingUdpAddressCriteria(
				final Criteria blockedSocks5IncomingUdpAddrCriteria) {
			this.blockedSocks5IncomingUdpAddressCriteria = 
					blockedSocks5IncomingUdpAddrCriteria;
			return this;
		}
		
		public Builder blockedSocks5RequestCriteria(
				final Socks5RequestCriteria blockedSocks5ReqCriteria) {
			this.blockedSocks5RequestCriteria = blockedSocks5ReqCriteria;
			return this;
		}
		
		public ImmutableConfiguration build() {
			return new ImmutableConfiguration(this);
		}
		
		public Builder externalClientSocks5UsernamePassword(
				final UsernamePassword externalClientSocks5UsrnmPsswrd) {
			this.externalClientSocks5UsernamePassword = 
					externalClientSocks5UsrnmPsswrd;
			return this;
		}
		
		public Builder settings(final Settings sttngs) {
			this.settings = sttngs;
			return this;
		}
		
		public Builder socks5UsernamePasswordAuthenticator(
				final UsernamePasswordAuthenticator socks5UsrnmPsswrdAuthenticator) {
			this.socks5UsernamePasswordAuthenticator = 
					socks5UsrnmPsswrdAuthenticator;
			return this;
		}
	}
	
	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "configuration", propOrder = { })
	@XmlRootElement(name = "configuration")
	static class ConfigurationXml {
		@XmlElement(name = "allowedClientAddressCriteria")
		protected Criteria allowedClientAddressCriteria;
		@XmlElement(name = "allowedSocks5IncomingTcpAddressCriteria")
		protected Criteria allowedSocks5IncomingTcpAddressCriteria;
		@XmlElement(name = "allowedSocks5IncomingUdpAddressCriteria")
		protected Criteria allowedSocks5IncomingUdpAddressCriteria;
		@XmlElement(name = "allowedSocks5RequestCriteria")
		protected Socks5RequestCriteria allowedSocks5RequestCriteria;
		@XmlElement(name = "blockedClientAddressCriteria")
		protected Criteria blockedClientAddressCriteria;
		@XmlElement(name = "blockedSocks5IncomingTcpAddressCriteria")
		protected Criteria blockedSocks5IncomingTcpAddressCriteria;
		@XmlElement(name = "blockedSocks5IncomingUdpAddressCriteria")
		protected Criteria blockedSocks5IncomingUdpAddressCriteria;		
		@XmlElement(name = "blockedSocks5RequestCriteria")
		protected Socks5RequestCriteria blockedSocks5RequestCriteria;
		@XmlElement(name = "externalClientSocks5UsernamePassword")
		protected UsernamePassword externalClientSocks5UsernamePassword;
		@XmlElement(name = "settings")
		protected Settings settings;
		@XmlElement(name = "socks5UsernamePasswordAuthenticator")
		protected UsernamePasswordAuthenticator socks5UsernamePasswordAuthenticator;
	}
	
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
	
	public static byte[] getXsd() throws JAXBException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JAXBContext jaxbContext = JAXBContext.newInstance(
				ConfigurationXml.class);
		StreamResult result = new StreamResult(out);
		result.setSystemId("");
		try {
			jaxbContext.generateSchema(new CustomSchemaOutputResolver(result));
		} catch (IOException e) {
			throw new AssertionError(e.toString(), e);
		}
		return out.toByteArray();
	}
	
	public static ImmutableConfiguration newInstance(
			final Configuration config) {
		Builder builder = new Builder();
		if (!config.getAllowedClientAddressCriteria().toList().isEmpty()) {
			builder.allowedClientAddressCriteria(
					config.getAllowedClientAddressCriteria());
		}
		if (!config.getAllowedSocks5IncomingTcpAddressCriteria().toList().isEmpty()) {
			builder.allowedSocks5IncomingTcpAddressCriteria(
					config.getAllowedSocks5IncomingTcpAddressCriteria());
		}
		if (!config.getAllowedSocks5IncomingUdpAddressCriteria().toList().isEmpty()) {
			builder.allowedSocks5IncomingUdpAddressCriteria(
					config.getAllowedSocks5IncomingUdpAddressCriteria());
		}
		if (!config.getAllowedSocks5RequestCriteria().toList().isEmpty()) {
			builder.allowedSocks5RequestCriteria(
					config.getAllowedSocks5RequestCriteria());
		}
		if (!config.getBlockedClientAddressCriteria().toList().isEmpty()) {
			builder.blockedClientAddressCriteria(
					config.getBlockedClientAddressCriteria());
		}
		if (!config.getBlockedSocks5IncomingTcpAddressCriteria().toList().isEmpty()) {
			builder.blockedSocks5IncomingTcpAddressCriteria(
					config.getBlockedSocks5IncomingTcpAddressCriteria());
		}
		if (!config.getBlockedSocks5IncomingUdpAddressCriteria().toList().isEmpty()) {
			builder.blockedSocks5IncomingUdpAddressCriteria(
					config.getBlockedSocks5IncomingUdpAddressCriteria());
		}		
		if (!config.getBlockedSocks5RequestCriteria().toList().isEmpty()) {
			builder.blockedSocks5RequestCriteria(
					config.getBlockedSocks5RequestCriteria());
		}
		if (config.getExternalClientSocks5UsernamePassword() != null) {
			builder.externalClientSocks5UsernamePassword(
					config.getExternalClientSocks5UsernamePassword());
		}
		if (!config.getSettings().toList().isEmpty()) {
			builder.settings(config.getSettings());
		}
		if (config.getSocks5UsernamePasswordAuthenticator() != null) {
			builder.socks5UsernamePasswordAuthenticator(
					config.getSocks5UsernamePasswordAuthenticator());
		}
		return builder.build();
	}
	
	private static ImmutableConfiguration newInstance(
			final ConfigurationXml configurationXml) {
		Builder builder = new Builder();
		if (configurationXml.allowedClientAddressCriteria != null) {
			builder.allowedClientAddressCriteria(
					configurationXml.allowedClientAddressCriteria);
		}
		if (configurationXml.allowedSocks5IncomingTcpAddressCriteria != null) {
			builder.allowedSocks5IncomingTcpAddressCriteria(
					configurationXml.allowedSocks5IncomingTcpAddressCriteria);
		}
		if (configurationXml.allowedSocks5IncomingUdpAddressCriteria != null) {
			builder.allowedSocks5IncomingUdpAddressCriteria(
					configurationXml.allowedSocks5IncomingUdpAddressCriteria);
		}
		if (configurationXml.allowedSocks5RequestCriteria != null) {
			builder.allowedSocks5RequestCriteria(
					configurationXml.allowedSocks5RequestCriteria);
		}
		if (configurationXml.blockedClientAddressCriteria != null) {
			builder.blockedClientAddressCriteria(
					configurationXml.blockedClientAddressCriteria);
		}
		if (configurationXml.blockedSocks5IncomingTcpAddressCriteria != null) {
			builder.blockedSocks5IncomingTcpAddressCriteria(
					configurationXml.blockedSocks5IncomingTcpAddressCriteria);
		}
		if (configurationXml.blockedSocks5IncomingUdpAddressCriteria != null) {
			builder.blockedSocks5IncomingUdpAddressCriteria(
					configurationXml.blockedSocks5IncomingUdpAddressCriteria);
		}		
		if (configurationXml.blockedSocks5RequestCriteria != null) {
			builder.blockedSocks5RequestCriteria(
					configurationXml.blockedSocks5RequestCriteria);
		}
		if (configurationXml.externalClientSocks5UsernamePassword != null) {
			builder.externalClientSocks5UsernamePassword(
					configurationXml.externalClientSocks5UsernamePassword);
		}
		if (configurationXml.settings != null) {
			builder.settings(configurationXml.settings);
		}
		if (configurationXml.socks5UsernamePasswordAuthenticator != null) {
			builder.socks5UsernamePasswordAuthenticator(
					configurationXml.socks5UsernamePasswordAuthenticator);
		}
		return builder.build();
	}
	
	public static ImmutableConfiguration newInstanceFrom(
			final InputStream in) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(
				ConfigurationXml.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setEventHandler(new DefaultValidationEventHandler());
		ConfigurationXml configurationXml = 
				(ConfigurationXml) unmarshaller.unmarshal(in);
		return newInstance(configurationXml);
	}
	
	private final Criteria allowedClientAddressCriteria;
	private final Criteria allowedSocks5IncomingTcpAddressCriteria;
	private final Criteria allowedSocks5IncomingUdpAddressCriteria;
	private final Socks5RequestCriteria allowedSocks5RequestCriteria;
	private final Criteria blockedClientAddressCriteria;
	private final Criteria blockedSocks5IncomingTcpAddressCriteria;
	private final Criteria blockedSocks5IncomingUdpAddressCriteria;
	private final Socks5RequestCriteria blockedSocks5RequestCriteria;
	private final UsernamePassword externalClientSocks5UsernamePassword;
	private final Settings settings;
	private final UsernamePasswordAuthenticator socks5UsernamePasswordAuthenticator;
	
	private ImmutableConfiguration(final Builder builder) {
		Criteria allowedClientAddrCriteria = 
				builder.allowedClientAddressCriteria;
		Criteria allowedSocks5IncomingTcpAddrCriteria = 
				builder.allowedSocks5IncomingTcpAddressCriteria;
		Criteria allowedSocks5IncomingUdpAddrCriteria =
				builder.allowedSocks5IncomingUdpAddressCriteria;
		Socks5RequestCriteria allowedSocks5ReqCriteria = 
				builder.allowedSocks5RequestCriteria;
		Criteria blockedClientAddrCriteria = 
				builder.blockedClientAddressCriteria;
		Criteria blockedSocks5IncomingTcpAddrCriteria = 
				builder.blockedSocks5IncomingTcpAddressCriteria;
		Criteria blockedSocks5IncomingUdpAddrCriteria =
				builder.blockedSocks5IncomingUdpAddressCriteria;		
		Socks5RequestCriteria blockedSocks5ReqCriteria =
				builder.blockedSocks5RequestCriteria;
		UsernamePassword externalClientSocks5UsrnmPsswrd = 
				builder.externalClientSocks5UsernamePassword;
		Settings sttngs = builder.settings;
		UsernamePasswordAuthenticator socks5UsrnmPsswrdAuthenticator = 
				builder.socks5UsernamePasswordAuthenticator;
		this.allowedClientAddressCriteria = allowedClientAddrCriteria;
		this.allowedSocks5IncomingTcpAddressCriteria = 
				allowedSocks5IncomingTcpAddrCriteria;
		this.allowedSocks5IncomingUdpAddressCriteria = 
				allowedSocks5IncomingUdpAddrCriteria;
		this.allowedSocks5RequestCriteria = allowedSocks5ReqCriteria;
		this.blockedClientAddressCriteria = blockedClientAddrCriteria;
		this.blockedSocks5IncomingTcpAddressCriteria = 
				blockedSocks5IncomingTcpAddrCriteria;
		this.blockedSocks5IncomingUdpAddressCriteria = 
				blockedSocks5IncomingUdpAddrCriteria;
		this.blockedSocks5RequestCriteria = blockedSocks5ReqCriteria;
		this.externalClientSocks5UsernamePassword = 
				externalClientSocks5UsrnmPsswrd;
		this.settings = sttngs;
		this.socks5UsernamePasswordAuthenticator = 
				socks5UsrnmPsswrdAuthenticator;
	}
	
	@Override
	public Criteria getAllowedClientAddressCriteria() {
		if (this.allowedClientAddressCriteria == null) {
			return Criteria.EMPTY_INSTANCE;
		}
		return this.allowedClientAddressCriteria;
	}
	
	@Override
	public Criteria getAllowedSocks5IncomingTcpAddressCriteria() {
		if (this.allowedSocks5IncomingTcpAddressCriteria == null) {
			return Criteria.EMPTY_INSTANCE;
		}
		return this.allowedSocks5IncomingTcpAddressCriteria;
	}

	@Override
	public Criteria getAllowedSocks5IncomingUdpAddressCriteria() {
		if (this.allowedSocks5IncomingUdpAddressCriteria == null) {
			return Criteria.EMPTY_INSTANCE;
		}
		return this.allowedSocks5IncomingUdpAddressCriteria;
	}
	
	@Override
	public Socks5RequestCriteria getAllowedSocks5RequestCriteria() {
		if (this.allowedSocks5RequestCriteria == null) {
			return Socks5RequestCriteria.EMPTY_INSTANCE;
		}
		return this.allowedSocks5RequestCriteria;
	}
	
	@Override
	public Criteria getBlockedClientAddressCriteria() {
		if (this.blockedClientAddressCriteria == null) {
			return Criteria.EMPTY_INSTANCE;
		}
		return this.blockedClientAddressCriteria;
	}
	
	@Override
	public Criteria getBlockedSocks5IncomingTcpAddressCriteria() {
		if (this.blockedSocks5IncomingTcpAddressCriteria == null) {
			return Criteria.EMPTY_INSTANCE;
		}
		return this.blockedSocks5IncomingTcpAddressCriteria;
	}

	@Override
	public Criteria getBlockedSocks5IncomingUdpAddressCriteria() {
		if (this.blockedSocks5IncomingUdpAddressCriteria == null) {
			return Criteria.EMPTY_INSTANCE;
		}
		return this.blockedSocks5IncomingUdpAddressCriteria;
	}

	@Override
	public Socks5RequestCriteria getBlockedSocks5RequestCriteria() {
		if (this.blockedSocks5RequestCriteria == null) {
			return Socks5RequestCriteria.EMPTY_INSTANCE;
		}
		return this.blockedSocks5RequestCriteria;
	}

	@Override
	public UsernamePassword getExternalClientSocks5UsernamePassword() {
		return this.externalClientSocks5UsernamePassword;
	}

	@Override
	public Settings getSettings() {
		if (this.settings == null) {
			return Settings.EMPTY_INSTANCE;
		}
		return this.settings;
	}

	@Override
	public UsernamePasswordAuthenticator getSocks5UsernamePasswordAuthenticator() {
		return this.socks5UsernamePasswordAuthenticator;
	}

	private ConfigurationXml toConfigurationXml() {
		ConfigurationXml configurationXml = new ConfigurationXml();
		if (this.allowedClientAddressCriteria != null) {
			configurationXml.allowedClientAddressCriteria = 
					this.allowedClientAddressCriteria;
		}
		if (this.allowedSocks5IncomingTcpAddressCriteria != null) {
			configurationXml.allowedSocks5IncomingTcpAddressCriteria =
					this.allowedSocks5IncomingTcpAddressCriteria;
		}
		if (this.allowedSocks5IncomingUdpAddressCriteria != null) {
			configurationXml.allowedSocks5IncomingUdpAddressCriteria =
					this.allowedSocks5IncomingUdpAddressCriteria;
		}
		if (this.allowedSocks5RequestCriteria != null) {
			configurationXml.allowedSocks5RequestCriteria =
					this.allowedSocks5RequestCriteria;
		}
		if (this.blockedClientAddressCriteria != null) {
			configurationXml.blockedClientAddressCriteria = 
					this.blockedClientAddressCriteria;
		}
		if (this.blockedSocks5IncomingTcpAddressCriteria != null) {
			configurationXml.blockedSocks5IncomingTcpAddressCriteria =
					this.blockedSocks5IncomingTcpAddressCriteria;
		}
		if (this.blockedSocks5IncomingUdpAddressCriteria != null) {
			configurationXml.blockedSocks5IncomingUdpAddressCriteria =
					this.blockedSocks5IncomingUdpAddressCriteria;
		}		
		if (this.blockedSocks5RequestCriteria != null) {
			configurationXml.blockedSocks5RequestCriteria = 
					this.blockedSocks5RequestCriteria;
		}
		if (this.externalClientSocks5UsernamePassword != null) {
			configurationXml.externalClientSocks5UsernamePassword = 
					this.externalClientSocks5UsernamePassword;
		}
		if (this.settings != null) {
			configurationXml.settings = this.settings;
		}
		if (this.socks5UsernamePasswordAuthenticator != null) {
			configurationXml.socks5UsernamePasswordAuthenticator = 
					this.socks5UsernamePasswordAuthenticator;
		}
		return configurationXml;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [allowedClientAddressCriteria=")
			.append(this.allowedClientAddressCriteria)
			.append(", allowedSocks5IncomingTcpAddressCriteria=")
			.append(this.allowedSocks5IncomingTcpAddressCriteria)
			.append(", allowedSocks5IncomingUdpAddressCriteria=")
			.append(this.allowedSocks5IncomingUdpAddressCriteria)
			.append(", allowedSocks5RequestCriteria=")
			.append(this.allowedSocks5RequestCriteria)			
			.append(", blockedClientAddressCriteria=")
			.append(this.blockedClientAddressCriteria)
			.append(", blockedSocks5IncomingTcpAddressCriteria=")
			.append(this.blockedSocks5IncomingTcpAddressCriteria)
			.append(", blockedSocks5IncomingUdpAddressCriteria=")
			.append(this.blockedSocks5IncomingUdpAddressCriteria)			
			.append(", blockedSocks5RequestCriteria=")
			.append(this.blockedSocks5RequestCriteria)
			.append(", externalClientSocks5UsernamePassword=")
			.append(this.externalClientSocks5UsernamePassword)
			.append(", settings=")
			.append(this.settings)
			.append(", socks5UsernamePasswordAuthenticator=")
			.append(this.socks5UsernamePasswordAuthenticator)
			.append("]");
		return builder.toString();
	}
	
	public byte[] toXml() throws JAXBException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JAXBContext jaxbContext = JAXBContext.newInstance(
				ConfigurationXml.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(this.toConfigurationXml(), out);
		return out.toByteArray();
	}
}
