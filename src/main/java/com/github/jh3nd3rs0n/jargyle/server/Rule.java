package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class Rule {
	
	public static abstract class Builder<B extends Builder<B, R>, R extends Rule> {

		public static interface Field<B extends Builder<B, R>, R extends Rule> {
			
			public boolean isRepresentedBy(final String s);
			
			public B set(final B builder, final String value);
			
			@Override
			public String toString();
			
		}

		private static <B extends Builder<B, R>, F extends Builder.Field<B, R>, R extends Rule> F getFieldRepresentedBy(
				final String s,
				final List<F> fields) {
			for (F field : fields) {
				if (field.isRepresentedBy(s)) {
					return field;
				}
			}
			StringBuilder sb = new StringBuilder();
			for (Iterator<F> iterator = fields.iterator(); iterator.hasNext();) {
				F value = iterator.next();
				sb.append(value);
				if (iterator.hasNext()) {
					sb.append(", ");
				}
			}
			throw new IllegalArgumentException(
					String.format(
							"expected field must be one of the following "
							+ "values: %s. actual value is %s",
							sb.toString(),
							s));			

		}
		
		private LogAction logAction;
		private String doc;
		
		public Builder() {
			this.logAction = null;
			this.doc = null;
		}
		
		public abstract R build();

		public final String doc() {
			return this.doc;
		}
		
		public B doc(final String d) {
			this.doc = d;
			@SuppressWarnings("unchecked")
			B builder = (B) this;
			return builder;		
		}
		
		public final LogAction logAction() {
			return this.logAction;
		}
		
		public B logAction(final LogAction lgAction) {
			this.logAction = lgAction;
			@SuppressWarnings("unchecked")
			B builder = (B) this;
			return builder;
		}
		
	}
	
	public static abstract class Context {
		
		@Override
		public abstract String toString();
		
	}
	
	public static <B extends Builder<B, R>, F extends Builder.Field<B, R>, R extends Rule> List<R> newInstances(
			final String s, 
			final F firstField, 
			final List<F> fields) {
		List<R> rules = new ArrayList<R>();
		String[] words = s.split(" ");
		B recentBuilder = null;
		B builder = null;
		for (Iterator<String> iterator = Arrays.asList(words).iterator();
				iterator.hasNext();) {
			String word = iterator.next();
			String[] wordElements = word.split("=", 2);
			if (wordElements.length != 2) {
				throw new IllegalArgumentException(String.format(
						"field must be in the following format: "
						+ "NAME=VALUE. actual value is %s",
						word));
			}
			F field = Builder.getFieldRepresentedBy(wordElements[0], fields);
			try {
				builder = field.set(builder, wordElements[1]);
			} catch (NullPointerException e) {
				throw new IllegalArgumentException(String.format(
						"expected field '%s'. actual field is '%s'", 
						firstField,
						wordElements[0]));				
			}
			if (recentBuilder == null) {
				recentBuilder = builder;
			}
			if (!recentBuilder.equals(builder)) {
				rules.add(recentBuilder.build());
				recentBuilder = builder;
			}
			if (!iterator.hasNext()) {
				rules.add(builder.build());
			}
		}
		return rules;		
	}
	
	private final LogAction logAction;
	private final String doc;
	
	public Rule(final Builder<?, ?> builder) {
		LogAction lgAction = builder.logAction();
		String d = builder.doc();
		this.logAction = lgAction;
		this.doc = d;
	}
	
	public abstract boolean appliesBasedOn(final Context context);

	public final String getDoc() {
		return this.doc;
	}

	public final LogAction getLogAction() {
		return this.logAction;
	}

}
