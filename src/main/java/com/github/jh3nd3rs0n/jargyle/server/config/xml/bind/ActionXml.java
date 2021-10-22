package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.Action;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "action")
@XmlEnum(String.class) 
enum ActionXml {

	@XmlEnumValue("allow")
	ALLOW(Action.ALLOW),

	@XmlEnumValue("block")
	BLOCK(Action.BLOCK);

	public static ActionXml valueOfAction(final Action action) {
		for (ActionXml value : ActionXml.values()) {
			if (value.actionValue().equals(action)) {
				return value;
			}
		}
		StringBuilder sb = new StringBuilder();
		List<ActionXml> list = Arrays.asList(ActionXml.values());
		for (Iterator<ActionXml> iterator = list.iterator();
				iterator.hasNext();) {
			ActionXml value = iterator.next();
			Action actionValue = value.actionValue();
			sb.append(actionValue.toString());
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		throw new IllegalArgumentException(String.format(
				"expected action must be one of the following values: %s. "
				+ "actual value is %s",
				sb.toString(),
				action.toString()));
	}
	
	private final Action actionValue;
	
	private ActionXml(final Action actnValue) {
		this.actionValue = actnValue;
	}
	
	public Action actionValue() {
		return this.actionValue;
	}

}
