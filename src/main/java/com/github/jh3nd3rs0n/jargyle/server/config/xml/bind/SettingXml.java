package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.server.Setting;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "setting", propOrder = { }) 
class SettingXml {
	
	@XmlElement(name = "name", required = true)
	protected String name;
	@XmlElements({
		@XmlElement(
				name = "criteria", 
				required = true, 
				type = CriteriaXml.class),
		@XmlElement(
				name = "encryptedPassword",
				required = true,
				type = EncryptedPasswordXml.class),
		@XmlElement(
				name = "socketSettings", 
				required = true, 
				type = SocketSettingsXml.class),
		@XmlElement(
				name = "socks5RequestCriteria", 
				required = true, 
				type = Socks5RequestCriteriaXml.class),
		@XmlElement(
				name = "socks5RequestWorkerFactory",
				required = true,
				type = Socks5RequestWorkerFactoryXml.class),
		@XmlElement(
				name = "usernamePasswordAuthenticator", 
				required = true, 
				type = UsernamePasswordAuthenticatorXml.class),
		@XmlElement(
				name = "usernamePassword", 
				required = true, 
				type = UsernamePasswordXml.class),
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
			newVal = ValueXml.newInstance(val);  
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
		return Setting.newInstance(this.name, newVal, this.doc);			
	}
	
}