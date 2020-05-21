package jargyle.server;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "criterionOperator")
@XmlEnum(String.class)
public enum CriterionOperator {
	
	@XmlEnumValue("equals")
	EQUALS("equals") {

		@Override
		public boolean evaluate(
				final String operand1, final String operand2) {
			return operand1.equals(operand2);
		}
		
	},
	
	@XmlEnumValue("matches")
	MATCHES("matches") {

		@Override
		public boolean evaluate(
				final String operand1, final String operand2) {
			return operand1.matches(operand2);
		}
		
	};
	
	private final String string;
	
	private CriterionOperator(final String str) {
		this.string = str;
	}
	
	public abstract boolean evaluate(
			final String operand1, final String operand2);
	
	public Criterion newCriterion(final String operand2) {
		return new Criterion(this, operand2);
	}
	
	@Override
	public String toString() {
		return this.string;
	}
	
}