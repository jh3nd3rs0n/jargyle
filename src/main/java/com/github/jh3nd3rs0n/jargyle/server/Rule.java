package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.common.net.AddressRange;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;

public final class Rule {

	public static final class Builder {
		
		@HelpText(
				doc = "Specifies the action to take. This field starts a new "
						+ "rule.",
				usage = "ruleAction=RULE_ACTION"
		)
		private final RuleAction ruleAction;
		
		@HelpText(
				doc = "Specifies the address range for the source address",
				usage = "sourceAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
		)	
		private AddressRange sourceAddressRange;
		
		@HelpText(
				doc = "Specifies the address range for the destination address",
				usage = "destinationAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
		)	
		private AddressRange destinationAddressRange;
		
		@HelpText(
				doc = "Specifies the logging action to take if the rule applies",
				usage = "logAction=LOG_ACTION"
		)	
		private LogAction logAction;
		
		private String doc;
		
		public Builder(final RuleAction rlAction) {
			this.ruleAction = Objects.requireNonNull(
					rlAction, "rule action must not be null");
			this.sourceAddressRange = null;
			this.destinationAddressRange = null;
			this.logAction = null;
			this.doc = null;
		}
		
		public Rule build() {
			return new Rule(this);
		}
		
		public Builder destinationAddressRange(
				final AddressRange destinationAddrRange) {
			this.destinationAddressRange = destinationAddrRange;
			return this;
		}
		
		public Builder doc(final String d) {
			this.doc = d;
			return this;
		}
		
		public Builder logAction(final LogAction lgAction) {
			this.logAction = lgAction;
			return this;
		}
		
		public Builder sourceAddressRange(
				final AddressRange sourceAddrRange) {
			this.sourceAddressRange = sourceAddrRange;
			return this;
		}
	}
	
	private static enum Field {
		
		RULE_ACTION("ruleAction") {
			@Override
			public Builder set(final Builder builder, final String value) {
				return new Rule.Builder(RuleAction.valueOfString(value));
			}
		},
		
		SOURCE_ADDRESS_RANGE("sourceAddressRange") {
			@Override
			public Builder set(final Builder builder, final String value) {
				return builder.sourceAddressRange(AddressRange.newInstance(
						value));
			}
		},
		
		DESTINATION_ADDRESS_RANGE("destinationAddressRange") {
			@Override
			public Builder set(final Builder builder, final String value) {
				return builder.destinationAddressRange(AddressRange.newInstance(
						value));
			}
		},
		
		LOG_ACTION("logAction") {
			@Override
			public Builder set(final Builder builder, final String value) {
				return builder.logAction(LogAction.valueOfString(value));
			}
		};
		
		public static Field valueOfString(final String s) {
			for (Field value : Field.values()) {
				if (value.toString().equals(s)) {
					return value;
				}
			}
			StringBuilder sb = new StringBuilder();
			List<Field> list = Arrays.asList(Field.values());
			for (Iterator<Field> iterator = list.iterator(); 
					iterator.hasNext();) {
				Field value = iterator.next();
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
		
		private final String string;
		
		private Field(final String str) {
			this.string = str;
		}
		
		public abstract Builder set(final Builder builder, final String value);
		
		@Override
		public String toString() {
			return this.string;
		}
		
	}
	
	public static List<Rule> newInstances(final String s) {
		List<Rule> rules = new ArrayList<Rule>();
		String[] words = s.split(" ");
		Builder recentBuilder = null;
		Builder builder = null;
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
			Field field = Field.valueOfString(wordElements[0]);
			try {
				builder = field.set(builder, wordElements[1]);
			} catch (NullPointerException e) {
				throw new IllegalArgumentException(String.format(
						"expected field '%s'. actual value is %s", 
						Field.RULE_ACTION,
						word));				
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
	
	private static final Rule DEFAULT_INSTANCE = new Rule.Builder(
			RuleAction.ALLOW).build();
	
	public static Rule getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	private final RuleAction ruleAction;
	private final AddressRange sourceAddressRange;
	private final AddressRange destinationAddressRange;
	private final LogAction logAction;
	private final String doc;
	
	private Rule(final Builder builder) {
		RuleAction rlAction = builder.ruleAction;
		AddressRange sourceAddrRange = builder.sourceAddressRange;
		AddressRange destinationAddrRange = builder.destinationAddressRange;
		LogAction lgAction = builder.logAction;
		String d = builder.doc;
		this.ruleAction = rlAction;
		this.sourceAddressRange = sourceAddrRange;
		this.destinationAddressRange = destinationAddrRange;
		this.logAction = lgAction;
		this.doc = d;		
	}

	public boolean appliesTo(
			final String sourceAddress, final String destinationAddress) {
		if (this.sourceAddressRange != null
				&& !this.sourceAddressRange.contains(sourceAddress)) {
			return false;
		}
		if (this.destinationAddressRange != null
				&& !this.destinationAddressRange.contains(destinationAddress)) {
			return false;
		}
		return true;
	}
	
	public void applyTo(
			final String sourceAddress, final String destinationAddress) {
		if (this.ruleAction.equals(RuleAction.ALLOW) 
				&& this.logAction != null) {
			this.logAction.log(String.format(
					"Source address %s to destination address %s is allowed "
					+ "based on the following rule: %s", 
					sourceAddress,
					destinationAddress,
					this));
		} else if (this.ruleAction.equals(RuleAction.DENY)) {
			if (this.logAction != null) {
				this.logAction.log(String.format(
						"Source address %s to destination address %s is denied "
						+ "based on the following rule: %s", 
						sourceAddress,
						destinationAddress,
						this));				
			}
			throw new RuleActionDenyException(String.format(
					"source address %s to destination address %s is denied "
					+ "based on the following rule: %s", 
					sourceAddress,
					destinationAddress,
					this));
		}		
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
		Rule other = (Rule) obj;
		if (this.destinationAddressRange == null) {
			if (other.destinationAddressRange != null) {
				return false;
			}
		} else if (!this.destinationAddressRange.equals(
				other.destinationAddressRange)) {
			return false;
		}
		if (this.ruleAction != other.ruleAction) {
			return false;
		}
		if (this.sourceAddressRange == null) {
			if (other.sourceAddressRange != null) {
				return false;
			}
		} else if (!this.sourceAddressRange.equals(
				other.sourceAddressRange)) {
			return false;
		}
		return true;
	}

	public AddressRange getDestinationAddressRange() {
		return this.destinationAddressRange;
	}

	public String getDoc() {
		return this.doc;
	}

	public LogAction getLogAction() {
		return this.logAction;
	}
	
	public RuleAction getRuleAction() {
		return this.ruleAction;
	}
	
	public AddressRange getSourceAddressRange() {
		return this.sourceAddressRange;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.destinationAddressRange == null) ? 
				0 : this.destinationAddressRange.hashCode());
		result = prime * result + ((this.ruleAction == null) ? 
				0 : this.ruleAction.hashCode());
		result = prime * result + ((this.sourceAddressRange == null) ? 
				0 : this.sourceAddressRange.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ruleAction=");
		builder.append(this.ruleAction);
		if (this.sourceAddressRange != null) {
			builder.append(" sourceAddressRange=");
			builder.append(this.sourceAddressRange);
		}
		if (this.destinationAddressRange != null) {
			builder.append(" destinationAddressRange=");
			builder.append(this.destinationAddressRange);
		}
		if (this.logAction != null) {
			builder.append(" logAction=");
			builder.append(this.logAction);
		}
		return builder.toString();
	}

}
