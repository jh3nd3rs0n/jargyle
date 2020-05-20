package jargyle.server;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "expressionType")
@XmlEnum(String.class)
public enum ExpressionType {
	
	@XmlEnumValue("lit")
	LITERAL("lit") {

		@Override
		public boolean matches(
				final String string, final String expression) {
			return string.equals(expression);
		}
		
	},
	
	@XmlEnumValue("regex")
	REGULAR("regex") {

		@Override
		public boolean matches(
				final String string, final String expression) {
			return string.matches(expression);
		}
		
	};
	
	private final String string;
	
	private ExpressionType(final String str) {
		this.string = str;
	}
	
	public abstract boolean matches(
			final String string, final String expression);
	
	public Expression newExpression(final String expression) {
		return new Expression(expression, this);
	}
	
	@Override
	public String toString() {
		return this.string;
	}
	
}