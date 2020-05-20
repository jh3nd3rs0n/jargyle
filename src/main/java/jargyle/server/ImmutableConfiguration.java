package jargyle.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import jargyle.client.socks5.UsernamePassword;
import jargyle.server.socks5.Socks5RequestRules;
import jargyle.server.socks5.UsernamePasswordAuthenticator;

public final class ImmutableConfiguration implements Configuration {

	public static final class Builder {
		
		private Expressions allowedClientAddressExpressions;
		private Socks5RequestRules allowedSocks5RequestRules;
		private Expressions blockedClientAddressExpressions;
		private Socks5RequestRules blockedSocks5RequestRules;
		private UsernamePassword externalClientSocks5UsernamePassword;
		private Settings settings;
		private UsernamePasswordAuthenticator socks5UsernamePasswordAuthenticator;
				
		public Builder() {
			this.allowedClientAddressExpressions = null;
			this.allowedSocks5RequestRules = null;
			this.blockedClientAddressExpressions = null;
			this.blockedSocks5RequestRules = null;
			this.externalClientSocks5UsernamePassword = null;
			this.settings = null;
			this.socks5UsernamePasswordAuthenticator = null;
		}
		
		public Builder allowedClientAddressExpressions(
				final Expressions allowedClientAddressExprs) {
			this.allowedClientAddressExpressions = allowedClientAddressExprs;
			return this;
		}
		
		public Builder allowedSocks5RequestRules(
				final Socks5RequestRules allowedSocks5ReqRules) {
			this.allowedSocks5RequestRules = allowedSocks5ReqRules;
			return this;
		}
		
		public Builder blockedClientAddressExpressions(
				final Expressions blockedClientAddressExprs) {
			this.blockedClientAddressExpressions = blockedClientAddressExprs;
			return this;
		}
		
		public Builder blockedSocks5RequestRules(
				final Socks5RequestRules blockedSocks5ReqRules) {
			this.blockedSocks5RequestRules = blockedSocks5ReqRules;
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
	public static class ConfigurationXml {
		@XmlElement(name = "allowedClientAddressExpressions")
		protected Expressions allowedClientAddressExpressions;
		@XmlElement(name = "allowedSocks5RequestRules")
		protected Socks5RequestRules allowedSocks5RequestRules;
		@XmlElement(name = "blockedClientAddressExpressions")
		protected Expressions blockedClientAddressExpressions;
		@XmlElement(name = "blockedSocks5RequestRules")
		protected Socks5RequestRules blockedSocks5RequestRules;
		@XmlElement(name = "externalClientSocks5UsernamePassword")
		protected UsernamePassword externalClientSocks5UsernamePassword;
		@XmlElement(name = "settings")
		protected Settings settings;
		@XmlElement(name = "socks5UsernamePasswordAuthenticator")
		protected UsernamePasswordAuthenticator socks5UsernamePasswordAuthenticator;
	}
	
	public static Configuration newInstance(
			final ConfigurationXml configurationXml) {
		ImmutableConfiguration.Builder builder = 
				new ImmutableConfiguration.Builder();
		if (configurationXml.allowedClientAddressExpressions != null) {
			builder.allowedClientAddressExpressions(
					configurationXml.allowedClientAddressExpressions);
		}
		if (configurationXml.allowedSocks5RequestRules != null) {
			builder.allowedSocks5RequestRules(
					configurationXml.allowedSocks5RequestRules);
		}
		if (configurationXml.blockedClientAddressExpressions != null) {
			builder.blockedClientAddressExpressions(
					configurationXml.blockedClientAddressExpressions);
		}
		if (configurationXml.blockedSocks5RequestRules != null) {
			builder.blockedSocks5RequestRules(
					configurationXml.blockedSocks5RequestRules);
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
	
	private final Expressions allowedClientAddressExpressions;
	private final Socks5RequestRules allowedSocks5RequestRules;
	private final Expressions blockedClientAddressExpressions;
	private final Socks5RequestRules blockedSocks5RequestRules;
	private final UsernamePassword externalClientSocks5UsernamePassword;
	private final Settings settings;
	private final UsernamePasswordAuthenticator socks5UsernamePasswordAuthenticator;
	
	private ImmutableConfiguration(final Builder builder) {
		Expressions allowedClientAddressExprs = builder.allowedClientAddressExpressions;
		Socks5RequestRules allowedSocks5ReqRules = 
				builder.allowedSocks5RequestRules;
		Expressions blockedClientAddressExprs = builder.blockedClientAddressExpressions;
		Socks5RequestRules blockedSocks5ReqRules =
				builder.blockedSocks5RequestRules;
		UsernamePassword externalClientSocks5UsrnmPsswrd = 
				builder.externalClientSocks5UsernamePassword;
		Settings sttngs = builder.settings;
		UsernamePasswordAuthenticator socks5UsrnmPsswrdAuthenticator = 
				builder.socks5UsernamePasswordAuthenticator;
		this.allowedClientAddressExpressions = allowedClientAddressExprs;
		this.allowedSocks5RequestRules = allowedSocks5ReqRules;
		this.blockedClientAddressExpressions = blockedClientAddressExprs;
		this.blockedSocks5RequestRules = blockedSocks5ReqRules;
		this.externalClientSocks5UsernamePassword = 
				externalClientSocks5UsrnmPsswrd;
		this.settings = sttngs;
		this.socks5UsernamePasswordAuthenticator = 
				socks5UsrnmPsswrdAuthenticator;
	}
	
	@Override
	public Expressions getAllowedClientAddressExpressions() {
		if (this.allowedClientAddressExpressions == null) {
			return Expressions.EMPTY_INSTANCE;
		}
		return this.allowedClientAddressExpressions;
	}
	
	@Override
	public Socks5RequestRules getAllowedSocks5RequestRules() {
		if (this.allowedSocks5RequestRules == null) {
			return Socks5RequestRules.EMPTY_INSTANCE;
		}
		return this.allowedSocks5RequestRules;
	}
	
	@Override
	public Expressions getBlockedClientAddressExpressions() {
		if (this.blockedClientAddressExpressions == null) {
			return Expressions.EMPTY_INSTANCE;
		}
		return this.blockedClientAddressExpressions;
	}

	@Override
	public Socks5RequestRules getBlockedSocks5RequestRules() {
		if (this.blockedSocks5RequestRules == null) {
			return Socks5RequestRules.EMPTY_INSTANCE;
		}
		return this.blockedSocks5RequestRules;
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

	public ConfigurationXml toConfigurationXml() {
		ConfigurationXml configurationXml = new ConfigurationXml();
		if (this.allowedClientAddressExpressions != null) {
			configurationXml.allowedClientAddressExpressions = 
					this.allowedClientAddressExpressions;
		}
		if (this.allowedSocks5RequestRules != null) {
			configurationXml.allowedSocks5RequestRules =
					this.allowedSocks5RequestRules;
		}
		if (this.blockedClientAddressExpressions != null) {
			configurationXml.blockedClientAddressExpressions = 
					this.blockedClientAddressExpressions;
		}
		if (this.blockedSocks5RequestRules != null) {
			configurationXml.blockedSocks5RequestRules = 
					this.blockedSocks5RequestRules;
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
			.append(" [allowedClientAddressExpressions=")
			.append(this.allowedClientAddressExpressions)
			.append(", allowedSocks5RequestRules=")
			.append(this.allowedSocks5RequestRules)			
			.append(", blockedClientAddressExpressions=")
			.append(this.blockedClientAddressExpressions)
			.append(", blockedSocks5RequestRules=")
			.append(this.blockedSocks5RequestRules)
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
