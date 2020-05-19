package jargyle.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(Expressions.ExpressionsXmlAdapter.class)
public final class Expressions {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "expressions", propOrder = { "expressions" })
	static class ExpressionsXml {
		@XmlElement(name = "expression")
		protected List<Expression> expressions = new ArrayList<Expression>();
	}
	
	static final class ExpressionsXmlAdapter 
		extends XmlAdapter<ExpressionsXml,Expressions> {

		@Override
		public ExpressionsXml marshal(final Expressions v) throws Exception {
			if (v == null) { return null; }
			ExpressionsXml expressionsXml = new ExpressionsXml();
			expressionsXml.expressions = new ArrayList<Expression>(
					v.expressions);
			return expressionsXml;
		}

		@Override
		public Expressions unmarshal(final ExpressionsXml v) throws Exception {
			if (v == null) { return null; }
			return new Expressions(v.expressions);
		}
		
	}

	public static final Expressions EMPTY_INSTANCE = new Expressions(
			Collections.emptyList());
	
	public static Expressions newInstance(final Expression... exprs) {
		return newInstance(Arrays.asList(exprs));
	}
	
	public static Expressions newInstance(final List<Expression> exprs) {
		return new Expressions(exprs);
	}
	
	public static Expressions newInstance(final String s) {
		List<Expression> exprs = new ArrayList<Expression>();
		String[] sElements = s.split("\\s");
		for (String sElement : sElements) {
			exprs.add(Expression.newInstance(sElement));
		}
		return new Expressions(exprs);
	}
	
	private final List<Expression> expressions;
	
	private Expressions(final List<Expression> exprs) {
		this.expressions = new ArrayList<Expression>(exprs);
	}
	
	public Expression anyMatches(final String str) {
		for (Expression expression : this.expressions) {
			if (expression.matches(str)) {
				return expression;
			}
		}
		return null;
	}
	
	public List<Expression> toList() {
		return Collections.unmodifiableList(this.expressions);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Iterator<Expression> iterator = this.expressions.iterator();
				iterator.hasNext();) {
			Expression expression = iterator.next();
			builder.append(expression.toString());
			if (iterator.hasNext()) {
				builder.append(' ');
			}
		}
		return builder.toString();
	}
}
