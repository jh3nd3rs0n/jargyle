package com.github.jh3nd3rs0n.jargyle.common.text;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "criterionMethod")
@XmlEnum(String.class)
public enum CriterionMethod {
	
	@XmlEnumValue("equals")
	EQUALS("equals") {

		@Override
		public boolean evaluatesTrue(
				final String arg0, final String arg1) {
			return arg0.equals(arg1);
		}
		
	},
	
	@XmlEnumValue("matches")
	MATCHES("matches") {

		@Override
		public boolean evaluatesTrue(
				final String arg0, final String arg1) {
			return arg0.matches(arg1);
		}
		
	};
	
	private final String string;
	
	private CriterionMethod(final String str) {
		this.string = str;
	}
	
	public abstract boolean evaluatesTrue(
			final String arg0, final String arg1);
	
	@Override
	public String toString() {
		return this.string;
	}
	
}