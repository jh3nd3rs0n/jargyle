package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socketSetting", propOrder = { }) 
class SocketSettingXml {
	
	@XmlElement(name = "name", required = true)
	protected String name;
	@XmlElement(name = "value", required = true)
	protected String value;
	@XmlAttribute(name = "comment")
	protected String comment;
	
	public SocketSettingXml() {
		this.name = null;
		this.value = null;
		this.comment = null;
	}
	
	public SocketSettingXml(final SocketSetting<Object> socketSetting) {
		this.comment = socketSetting.getComment();
		this.name = socketSetting.getSocketSettingSpec().toString();
		this.value = socketSetting.getValue().toString();			
	}
	
	public SocketSetting<Object> toSocketSetting() {
		return SocketSetting.newInstance(this.name, this.value, this.comment);
	}
	
}