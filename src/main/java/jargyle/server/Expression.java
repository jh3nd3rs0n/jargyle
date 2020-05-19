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

@XmlJavaTypeAdapter(Expression.ExpressionXmlAdapter.class)
public final class Expression {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "expression", propOrder = { })
	static class ExpressionXml {
		@XmlAttribute(name = "type", required = true)
		protected ExpressionType type;
		@XmlAttribute(name = "value", required = true)
		protected String value;
	}
	
	static final class ExpressionXmlAdapter extends XmlAdapter<ExpressionXml,Expression> {

		@Override
		public ExpressionXml marshal(final Expression arg) throws Exception {
			ExpressionXml expressionXml = new ExpressionXml();
			expressionXml.type = arg.expressionType;
			expressionXml.value = arg.expression;
			return expressionXml;
		}

		@Override
		public Expression unmarshal(final ExpressionXml arg) throws Exception {
			return arg.type.newExpression(arg.value);
		}
		
	}
	
	public static Expression newInstance(final String s) {
		StringBuilder sb = new StringBuilder();
		List<ExpressionType> list = Arrays.asList(ExpressionType.values());
		for (Iterator<ExpressionType> iterator = list.iterator();
				iterator.hasNext();) {
			ExpressionType value = iterator.next();
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
		String expressionType = sElements[0];
		String expression = sElements[1];
		for (ExpressionType value : ExpressionType.values()) {
			if (value.toString().equals(expressionType)) {
				return new Expression(expression, value);
			}
		}
		throw new IllegalArgumentException(message);
	}
	
	private final String expression;
	private final ExpressionType expressionType;
	
	Expression(final String expr, final ExpressionType exprType) {
		this.expression = expr;
		this.expressionType = exprType;
	}
	
	public boolean matches(final String str) {
		return this.expressionType.matches(str, this.expression);
	}
	
	@Override
	public String toString() {
		return this.expressionType.toString().concat(":").concat(this.expression);
	}
	
}
