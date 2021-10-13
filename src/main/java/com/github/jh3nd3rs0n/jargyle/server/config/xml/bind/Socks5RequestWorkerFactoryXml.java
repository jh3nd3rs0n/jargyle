package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestWorkerFactory;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socks5RequestWorkerFactory", propOrder = { }) 
class Socks5RequestWorkerFactoryXml extends ValueXml {
	
	@XmlElement(name = "className", required = true)
	protected String className;
	@XmlElement(name = "value")
	protected String value;
	
	public Socks5RequestWorkerFactoryXml() {
		this.className = null;
		this.value = null;
	}
	
	public Socks5RequestWorkerFactoryXml(
			final Socks5RequestWorkerFactory socks5RequestWorkerFactory) {
		this.className = socks5RequestWorkerFactory.getClass().getName();
		this.value = socks5RequestWorkerFactory.getValue();			
	}
	
	public Socks5RequestWorkerFactory toSocks5RequestWorkerFactory() {
		return Socks5RequestWorkerFactory.newInstance(
				this.className, this.value); 
	}

	@Override
	public Object toValue() {
		return this.toSocks5RequestWorkerFactory();
	}
	
}