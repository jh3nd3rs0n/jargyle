package com.github.jh3nd3rs0n.jargyle.common.text;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(Criterion.CriterionXmlAdapter.class)
public final class Criterion {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "criterion", propOrder = { })
	static class CriterionXml {
		@XmlAttribute(name = "method", required = true)
		protected CriterionMethod method;
		@XmlAttribute(name = "value", required = true)
		protected String value;
		@XmlAttribute(name = "comment")
		protected String comment;
	}
	
	static final class CriterionXmlAdapter 
		extends XmlAdapter<CriterionXml, Criterion> {

		@Override
		public CriterionXml marshal(final Criterion arg) throws Exception {
			if (arg == null) { return null; }
			CriterionXml criterionXml = new CriterionXml();
			criterionXml.comment = arg.comment;
			criterionXml.method = arg.criterionMethod;
			criterionXml.value = arg.value;
			return criterionXml;
		}

		@Override
		public Criterion unmarshal(final CriterionXml arg) throws Exception {
			if (arg == null) { return null; }
			return new Criterion(arg.method, arg.value, arg.comment);
		}
		
	}
	
	public static Criterion newInstance(
			final CriterionMethod criterionMethod, final String value) {
		return new Criterion(criterionMethod, value);
	}
	
	public static Criterion newInstance(final String s) {
		StringBuilder sb = new StringBuilder();
		List<CriterionMethod> list = Arrays.asList(CriterionMethod.values());
		for (Iterator<CriterionMethod> iterator = list.iterator();
				iterator.hasNext();) {
			CriterionMethod value = iterator.next();
			String prefix = value.toString().concat(":");
			sb.append(prefix);
			if (iterator.hasNext()) {
				sb.append(" , ");
			}
		}
		String message = String.format(
				"string is expected to have one of the following "
				+ "prefixes: %s . actual string is %s",
				sb.toString(),
				s);
		String[] sElements = s.split(":", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(message);
		}
		String criterionOperator = sElements[0];
		String operand = sElements[1];
		for (CriterionMethod value : CriterionMethod.values()) {
			if (value.toString().equals(criterionOperator)) {
				return new Criterion(value, operand);
			}
		}
		throw new IllegalArgumentException(message);
	}
	
	private final String comment;
	private final CriterionMethod criterionMethod;
	private final String value;
	
	private Criterion(final CriterionMethod method, final String val) {
		this(method, val, null);
	}
	
	private Criterion(
			final CriterionMethod method,
			final String val, 
			final String cmmnt) {
		this.comment = cmmnt;
		this.criterionMethod = method;
		this.value = val;
	} 

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Criterion other = (Criterion) obj;
		if (this.criterionMethod != other.criterionMethod) {
			return false;
		}
		if (this.value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!this.value.equals(other.value)) {
			return false;
		}
		return true;
	}
	
	public boolean evaluatesTrue(final String arg) {
		return this.criterionMethod.evaluatesTrue(arg, this.value);
	}
	
	public CriterionMethod getCriterionMethod() {
		return this.criterionMethod;
	}
	
	public String getValue() {
		return this.value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.criterionMethod == null) ? 0 : this.criterionMethod.hashCode());
		result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return this.criterionMethod.toString().concat(":").concat(this.value);
	}
	
}
