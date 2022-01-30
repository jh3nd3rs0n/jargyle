package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.ClientFirewallRules;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.ClientRoutingRules;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestWorkerFactory;
import com.github.jh3nd3rs0n.jargyle.server.socks5.rules.impl.Socks5ReplyFirewallRules;
import com.github.jh3nd3rs0n.jargyle.server.socks5.rules.impl.Socks5RequestFirewallRules;
import com.github.jh3nd3rs0n.jargyle.server.socks5.rules.impl.Socks5RequestRoutingRules;
import com.github.jh3nd3rs0n.jargyle.server.socks5.rules.impl.Socks5UdpFirewallRules;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UsernamePasswordAuthenticator;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "setting", propOrder = { }) 
class SettingXml {
	
	private static ValueXml newValueXml(final Object val) {
		Objects.requireNonNull(val);
		if (val instanceof ClientFirewallRules) {
			return new ClientFirewallRulesXml((ClientFirewallRules) val);
		}
		if (val instanceof ClientRoutingRules) {
			return new ClientRoutingRulesXml((ClientRoutingRules) val);
		}
		if (val instanceof EncryptedPassword) {
			return EncryptedPasswordXml.newInstance((EncryptedPassword) val);
		}
		if (val instanceof SocketSettings) {
			return new SocketSettingsXml((SocketSettings) val);
		}
		if (val instanceof Socks5ReplyFirewallRules) {
			return new Socks5ReplyFirewallRulesXml(
					(Socks5ReplyFirewallRules) val);
		}
		if (val instanceof Socks5RequestFirewallRules) {
			return new Socks5RequestFirewallRulesXml(
					(Socks5RequestFirewallRules) val);
		}
		if (val instanceof Socks5RequestRoutingRules) {
			return new Socks5RequestRoutingRulesXml(
					(Socks5RequestRoutingRules) val);
		}
		if (val instanceof Socks5RequestWorkerFactory) {
			return new Socks5RequestWorkerFactoryXml(
					(Socks5RequestWorkerFactory) val);
		}
		if (val instanceof Socks5UdpFirewallRules) {
			return new Socks5UdpFirewallRulesXml((Socks5UdpFirewallRules) val);
		}
		if (val instanceof UsernamePasswordAuthenticator) {
			return new UsernamePasswordAuthenticatorXml(
					(UsernamePasswordAuthenticator) val);
		}
		throw new IllegalArgumentException(String.format(
				"no %s for %s", 
				ValueXml.class.getName(),
				val.getClass().getName()));
	}
	
	@XmlElement(name = "name", required = true)
	protected String name;
	@XmlElements({
		@XmlElement(
				name = "clientFirewallRules", 
				required = true, 
				type = ClientFirewallRulesXml.class),
		@XmlElement(
				name = "clientRoutingRules", 
				required = true, 
				type = ClientRoutingRulesXml.class),
		@XmlElement(
				name = "encryptedPassword",
				required = true,
				type = EncryptedPasswordXml.class),
		@XmlElement(
				name = "socketSettings", 
				required = true, 
				type = SocketSettingsXml.class),
		@XmlElement(
				name = "socks5ReplyFirewallRules", 
				required = true, 
				type = Socks5ReplyFirewallRulesXml.class),		
		@XmlElement(
				name = "socks5RequestFirewallRules", 
				required = true, 
				type = Socks5RequestFirewallRulesXml.class),
		@XmlElement(
				name = "socks5RequestRoutingRules", 
				required = true, 
				type = Socks5RequestRoutingRulesXml.class),		
		@XmlElement(
				name = "socks5RequestWorkerFactory",
				required = true,
				type = Socks5RequestWorkerFactoryXml.class),
		@XmlElement(
				name = "socks5UdpFirewallRules", 
				required = true, 
				type = Socks5UdpFirewallRulesXml.class),
		@XmlElement(
				name = "usernamePasswordAuthenticator", 
				required = true, 
				type = UsernamePasswordAuthenticatorXml.class),
		@XmlElement(
				name = "value", 
				required = true, 
				type = String.class)
	})
	protected Object value;
	
	@XmlElement(name = "doc")
	protected String doc;
	
	public SettingXml() { 
		this.name = null;
		this.value = null;
		this.doc = null;
	}
	
	public SettingXml(final Setting<Object> setting) {
		Object val = setting.getValue();
		Object newVal = null;
		try {
			newVal = newValueXml(val);  
		} catch (IllegalArgumentException e) {
			newVal = val.toString();
		}
		this.name = setting.getSettingSpec().toString();
		this.value = newVal;
		this.doc = setting.getDoc();		
	}

	public Setting<Object> toSetting() {
		Object val = this.value;
		if (val instanceof ValueXml) {
			ValueXml newVal = (ValueXml) val;
			return Setting.newInstance(this.name, newVal.toValue(), this.doc);
		}
		String newVal = (String) val;
		return Setting.newInstanceOfParsableValue(this.name, newVal, this.doc);			
	}
	
}