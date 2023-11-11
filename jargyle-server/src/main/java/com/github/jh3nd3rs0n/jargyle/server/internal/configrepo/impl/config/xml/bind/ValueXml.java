package com.github.jh3nd3rs0n.jargyle.server.internal.configrepo.impl.config.xml.bind;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "value")
abstract class ValueXml {
	
	public abstract Object toValue();
	
}