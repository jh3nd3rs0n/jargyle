package com.github.jh3nd3rs0n.jargyle.server.internal.configrepo.impl.config.xml.bind;

import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socks5.methods")
class Socks5MethodsXml extends ValueXml {

	@XmlElement(name = "socks5.method")
	protected List<String> socks5Methods;
	
	public Socks5MethodsXml() {
		this.socks5Methods = new ArrayList<String>();
	}
	
	public Socks5MethodsXml(final Methods methods) {
		this.socks5Methods = new ArrayList<String>();
		for (Method method : methods.toList()) {
			this.socks5Methods.add(method.toString());
		}
	}
	
	public Methods toMethods() {
		List<Method> methods = new ArrayList<Method>();
		for (String socks5Method : this.socks5Methods) {
			methods.add(Method.valueOfString(socks5Method));
		}
		return Methods.of(methods);
	}
	
	@Override
	public Object toValue() {
		return this.toMethods();
	}

}
