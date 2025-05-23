package com.github.jh3nd3rs0n.jargyle.server.internal.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.common.net.HostAddressTypes;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.UserRepository;
import jakarta.xml.bind.annotation.*;

import java.util.Objects;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "setting", propOrder = { }) 
class SettingXml {
	
	private static ValueXml newValueXml(final Object val) {
		Objects.requireNonNull(val);
		if (val instanceof CommaSeparatedValues) {
			return new ValuesXml((CommaSeparatedValues) val);
		}
		if (val instanceof EncryptedPassword) {
			return new EncryptedPasswordXml((EncryptedPassword) val);
		}
		if (val instanceof HostAddressTypes) {
			return new HostAddressTypesXml((HostAddressTypes) val);
		}
		if (val instanceof Methods) {
			return new Socks5MethodsXml((Methods) val);
		}
		if (val instanceof PortRanges) {
			return new PortRangesXml((PortRanges) val);
		}
		if (val instanceof ProtectionLevels) {
			return new Socks5GssapiMethodProtectionLevelsXml((ProtectionLevels) val);
		}
		if (val instanceof Rule) {
			return new RuleXml((Rule) val);
		}
		if (val instanceof SocketSettings) {
			return new SocketSettingsXml((SocketSettings) val);
		}
		if (val instanceof UserRepository) {
			return new Socks5UserpassMethodUserRepositoryXml((UserRepository) val);
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
				name = "encryptedPassword",
				required = true,
				type = EncryptedPasswordXml.class),
		@XmlElement(
				name = "hostAddressTypes",
				required = true,
				type = HostAddressTypesXml.class),
		@XmlElement(
				name = "portRanges",
				required = true,
				type = PortRangesXml.class),		
		@XmlElement(
				name = "rule",
				required = true,
				type = RuleXml.class),
		@XmlElement(
				name = "socketSettings", 
				required = true, 
				type = SocketSettingsXml.class),
		@XmlElement(
				name = "socks5.gssapimethod.protectionLevels", 
				required = true, 
				type = Socks5GssapiMethodProtectionLevelsXml.class),
		@XmlElement(
				name = "socks5.methods", 
				required = true, 
				type = Socks5MethodsXml.class),
		@XmlElement(
				name = "socks5.userpassmethod.userRepository", 
				required = true, 
				type = Socks5UserpassMethodUserRepositoryXml.class),
		@XmlElement(
				name = "value", 
				required = true, 
				type = String.class),
		@XmlElement(
				name = "values", 
				required = true, 
				type = ValuesXml.class)
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
		this.name = setting.getName();
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
		return Setting.newInstanceWithParsedValue(this.name, newVal, this.doc);
	}
	
}