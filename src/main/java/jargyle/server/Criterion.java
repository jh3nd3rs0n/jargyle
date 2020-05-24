package jargyle.server;

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
		@XmlAttribute(name = "operator", required = true)
		protected CriterionOperator operator;
		@XmlAttribute(name = "operand", required = true)
		protected String operand;
	}
	
	static final class CriterionXmlAdapter 
		extends XmlAdapter<CriterionXml, Criterion> {

		@Override
		public CriterionXml marshal(final Criterion arg) throws Exception {
			CriterionXml criterionXml = new CriterionXml();
			criterionXml.operator = arg.criterionOperator;
			criterionXml.operand = arg.operand;
			return criterionXml;
		}

		@Override
		public Criterion unmarshal(final CriterionXml arg) throws Exception {
			return arg.operator.newCriterion(arg.operand);
		}
		
	}
	
	public static Criterion newInstance(final String s) {
		StringBuilder sb = new StringBuilder();
		List<CriterionOperator> list = Arrays.asList(CriterionOperator.values());
		for (Iterator<CriterionOperator> iterator = list.iterator();
				iterator.hasNext();) {
			CriterionOperator value = iterator.next();
			String prefix = value.toString().concat(":");
			sb.append(prefix);
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		String message = String.format(
				"string is expected to have one of the following "
				+ "prefixes: %s. actual string is %s",
				sb.toString(),
				s);
		String[] sElements = s.split(":", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(message);
		}
		String criterionOperator = sElements[0];
		String operand = sElements[1];
		for (CriterionOperator value : CriterionOperator.values()) {
			if (value.toString().equals(criterionOperator)) {
				return new Criterion(value, operand);
			}
		}
		throw new IllegalArgumentException(message);
	}
	
	private final CriterionOperator criterionOperator;
	private final String operand;
	
	Criterion(final CriterionOperator operator, final String oprnd) {
		this.criterionOperator = operator;
		this.operand = oprnd;
	}
	
	public boolean evaluatesTrue(final String oprnd) {
		return this.criterionOperator.evaluatesTrue(oprnd, this.operand);
	}
	
	public CriterionOperator getCriterionOperator() {
		return this.criterionOperator;
	}
	
	public String getOperand() {
		return this.operand;
	}
	
	@Override
	public String toString() {
		return this.criterionOperator.toString().concat(":").concat(this.operand);
	}
	
}
