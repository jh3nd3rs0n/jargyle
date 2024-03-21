package com.github.jh3nd3rs0n.jargyle.server.internal.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;
import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import jakarta.xml.bind.annotation.*;

import java.util.Objects;

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
		this.name = ruleResult.getName();
		this.value = newVal;
	}
	
	public RuleResult<Object> toRuleResult() {
		Object val = this.value;
		if (val instanceof ValueXml) {
			ValueXml newVal = (ValueXml) val;
			return RuleResult.newInstance(this.name, newVal.toValue());
		}
		String newVal = (String) val;
		return RuleResult.newInstanceWithParsedValue(this.name, newVal);
	}
	
}
