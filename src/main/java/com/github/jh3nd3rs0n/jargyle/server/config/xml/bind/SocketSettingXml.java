package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socketSetting", propOrder = { }) 
class SocketSettingXml extends ValueXml {
	
	@XmlElement(name = "name", required = true)
	protected String name;
	@XmlElement(name = "value", required = true)
	protected String value;
	@XmlElement(name = "doc")
	protected String doc;
	
	public SocketSettingXml() {
		this.name = null;
		this.value = null;
		this.doc = null;
	}
	
	public SocketSettingXml(final SocketSetting<Object> socketSetting) {
		this.name = socketSetting.getSocketSettingSpec().toString();
		this.value = socketSetting.getValue().toString();
		this.doc = socketSetting.getDoc();		
	}
	
	public SocketSetting<Object> toSocketSetting() {
		return SocketSetting.newInstanceOfParsableValue(this.name, this.value, this.doc);
	}

	@Override
	public Object toValue() {
		return this.toSocketSetting();
	}
	
}