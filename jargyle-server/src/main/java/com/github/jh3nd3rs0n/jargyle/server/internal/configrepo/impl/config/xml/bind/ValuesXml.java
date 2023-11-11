package com.github.jh3nd3rs0n.jargyle.server.internal.configrepo.impl.config.xml.bind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;

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
	
	public ValuesXml(final CommaSeparatedValues vals) {
		this.values = new ArrayList<String>(Arrays.asList(vals.toArray()));
	}
	
	public CommaSeparatedValues toCommaSeparatedValues() {
		return CommaSeparatedValues.newInstance(this.values.toArray(
				new String[this.values.size()]));
	}
	
	@Override
	public Object toValue() {
		return this.toCommaSeparatedValues();
	}

}
