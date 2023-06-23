package com.github.jh3nd3rs0n.jargyle.server.configrepo.impl.internal.config.xml.bind;

import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.common.text.Values;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "values")
class ValuesXml extends ValueXml {

	@XmlElement(name = "value")
	protected List<String> values;
	
	public ValuesXml() {
		this.values = new ArrayList<String>();
	}
	
	public ValuesXml(final Values vals) {
		this.values = new ArrayList<String>();
		for (String val : vals.toStringArray()) {
			this.values.add(val);
		}
	}
	
	public Values toValues() {
		List<String> vals = new ArrayList<String>();
		for (String value : this.values) {
			vals.add(value);
		}
		return Values.newInstance(vals.toArray(new String[vals.size()]));
	}
	
	@Override
	public Object toValue() {
		return this.toValues();
	}

}
