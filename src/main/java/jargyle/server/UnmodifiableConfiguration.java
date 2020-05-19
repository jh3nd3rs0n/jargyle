package jargyle.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import jargyle.client.socks5.UsernamePassword;
import jargyle.server.socks5.UsernamePasswordAuthenticator;

public final class UnmodifiableConfiguration implements Configuration {

	public static final class Builder {
		
		private Expressions allowedClientAddresses;
		private Expressions blockedClientAddresses;
		private UsernamePassword externalClientSocks5UsernamePassword;
		private Settings settings;
		private UsernamePasswordAuthenticator socks5UsernamePasswordAuthenticator;
				
		public Builder() {
			this.allowedClientAddresses = null;
			this.blockedClientAddresses = null;
			this.externalClientSocks5UsernamePassword = null;
			this.settings = null;
			this.socks5UsernamePasswordAuthenticator = null;
		}
		
		public Builder allowedClientAddresses(
				final Expressions allowedClientAddrs) {
			this.allowedClientAddresses = allowedClientAddrs;
			return this;
		}
		
		public Builder blockedClientAddresses(
				final Expressions blockedClientAddrs) {
			this.blockedClientAddresses = blockedClientAddrs;
			return this;
		}
		
		public UnmodifiableConfiguration build() {
			return new UnmodifiableConfiguration(this);
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
	public static class ConfigurationXml {
		@XmlElement(name = "allowedClientAddresses")
		protected Expressions allowedClientAddresses;
		@XmlElement(name = "blockedClientAddresses")
		protected Expressions blockedClientAddresses;
		@XmlElement(name = "externalClientSocks5UsernamePassword")
		protected UsernamePassword externalClientSocks5UsernamePassword;
		@XmlElement(name = "settings")
		protected Settings settings;
		@XmlElement(name = "socks5UsernamePasswordAuthenticator")
		protected UsernamePasswordAuthenticator socks5UsernamePasswordAuthenticator;
	}
	
	public static Configuration newInstance(
			final ConfigurationXml configurationXml) {
		UnmodifiableConfiguration.Builder builder = 
				new UnmodifiableConfiguration.Builder();
		if (configurationXml.allowedClientAddresses != null) {
			builder.allowedClientAddresses(
					configurationXml.allowedClientAddresses);
		}
		if (configurationXml.blockedClientAddresses != null) {
			builder.blockedClientAddresses(
					configurationXml.blockedClientAddresses);
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
	
	private final Expressions allowedClientAddresses;
	private final Expressions blockedClientAddresses;
	private final UsernamePassword externalClientSocks5UsernamePassword;
	private final Settings settings;
	private final UsernamePasswordAuthenticator socks5UsernamePasswordAuthenticator;
	
	private UnmodifiableConfiguration(final Builder builder) {
		Expressions allowedClientAddrs = builder.allowedClientAddresses;
		Expressions blockedClientAddrs = builder.blockedClientAddresses;
		UsernamePassword externalClientSocks5UsrnmPsswrd = 
				builder.externalClientSocks5UsernamePassword;
		Settings sttngs = builder.settings;
		UsernamePasswordAuthenticator socks5UsrnmPsswrdAuthenticator = 
				builder.socks5UsernamePasswordAuthenticator;
		this.allowedClientAddresses = allowedClientAddrs;
		this.blockedClientAddresses = blockedClientAddrs;
		this.externalClientSocks5UsernamePassword = 
				externalClientSocks5UsrnmPsswrd;
		this.settings = sttngs;
		this.socks5UsernamePasswordAuthenticator = 
				socks5UsrnmPsswrdAuthenticator;
	}
	
	/* (non-Javadoc)
	 * @see jargyle.server.IConfiguration#getAllowedClientAddresses()
	 */
	@Override
	public Expressions getAllowedClientAddresses() {
		if (this.allowedClientAddresses == null) {
			return Expressions.EMPTY_INSTANCE;
		}
		return this.allowedClientAddresses;
	}
	
	/* (non-Javadoc)
	 * @see jargyle.server.IConfiguration#getBlockedClientAddresses()
	 */
	@Override
	public Expressions getBlockedClientAddresses() {
		if (this.blockedClientAddresses == null) {
			return Expressions.EMPTY_INSTANCE;
		}
		return this.blockedClientAddresses;
	}
	
	/* (non-Javadoc)
	 * @see jargyle.server.IConfiguration#getExternalClientSocks5UsernamePassword()
	 */
	@Override
	public UsernamePassword getExternalClientSocks5UsernamePassword() {
		return this.externalClientSocks5UsernamePassword;
	}
	
	/* (non-Javadoc)
	 * @see jargyle.server.IConfiguration#getSettings()
	 */
	@Override
	public Settings getSettings() {
		if (this.settings == null) {
			return Settings.EMPTY_INSTANCE;
		}
		return this.settings;
	}
	
	/* (non-Javadoc)
	 * @see jargyle.server.IConfiguration#getSocks5UsernamePasswordAuthenticator()
	 */
	@Override
	public UsernamePasswordAuthenticator getSocks5UsernamePasswordAuthenticator() {
		return this.socks5UsernamePasswordAuthenticator;
	}

	public ConfigurationXml toConfigurationXml() {
		ConfigurationXml configurationXml = new ConfigurationXml();
		if (this.allowedClientAddresses != null) {
			configurationXml.allowedClientAddresses = 
					this.allowedClientAddresses;
		}
		if (this.blockedClientAddresses != null) {
			configurationXml.blockedClientAddresses = 
					this.blockedClientAddresses;
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
			.append(" [allowedClientAddresses=")
			.append(this.allowedClientAddresses)
			.append(", blockedClientAddresses=")
			.append(this.blockedClientAddresses)
			.append(", externalClientSocks5UsernamePassword=")
			.append(this.externalClientSocks5UsernamePassword)
			.append(", settings=")
			.append(this.settings)
			.append(", socks5UsernamePasswordAuthenticator=")
			.append(this.socks5UsernamePasswordAuthenticator)
			.append("]");
		return builder.toString();
	}
	
}
