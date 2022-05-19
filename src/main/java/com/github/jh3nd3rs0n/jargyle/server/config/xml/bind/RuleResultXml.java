package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;
import com.github.jh3nd3rs0n.jargyle.server.RuleResult;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "ruleResult", propOrder = { }) 
class RuleResultXml {
	
	private static ValueXml newValueXml(final Object val) {
		Objects.requireNonNull(val);
		if (val instanceof SocketSetting) {
			@SuppressWarnings("unchecked")
			SocketSetting<Object> socketSetting = (SocketSetting<Object>) val;  
			return new SocketSettingXml(socketSetting);
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
				name = "socketSetting", 
				required = true, 
				type = SocketSettingXml.class),
		@XmlElement(
				name = "value", 
				required = true, 
				type = String.class)
	})
	protected Object value;
	
	public RuleResultXml() {
		this.name = null;
		this.value = null;
	}

	public RuleResultXml(final RuleResult<? extends Object> ruleResult) {
		Object val = ruleResult.getValue();
		Object newVal = null;
		try {
			newVal = newValueXml(val);  
		} catch (IllegalArgumentException e) {
			newVal = val.toString();
		}
		this.name = ruleResult.getRuleResultSpec().toString();
		this.value = newVal;
	}
	
	public RuleResult<Object> toRuleResult() {
		Object val = this.value;
		if (val instanceof ValueXml) {
			ValueXml newVal = (ValueXml) val;
			return RuleResult.newInstance(this.name, newVal.toValue());
		}
		String newVal = (String) val;
		return RuleResult.newInstanceOfParsableValue(this.name, newVal);
	}
	
}
