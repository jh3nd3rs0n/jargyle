package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.common.net.AddressRange;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.RuleAction;
import com.github.jh3nd3rs0n.jargyle.server.RuleActionDenyException;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public final class Socks5RequestRule {

	public static final class Builder {

		@HelpText(
				doc = "Specifies the action to take. This field starts a new "
						+ "SOCKS5 request rule.",
				usage = "ruleAction=RULE_ACTION"
		)	
		private final RuleAction ruleAction;
		
		@HelpText(
				doc = "Specifies the address range for the client address",
				usage = "clientAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
		)	
		private AddressRange clientAddressRange;
		
		@HelpText(
				doc = "Specifies the negotiated SOCKS5 method",
				usage = "method=SOCKS5_METHOD"
		)
		private Method method;
		
		@HelpText(
				doc = "Specifies the user if any after the negotiated SOCKS5 "
						+ "method",
				usage = "user=USER"
		)
		private String user;
		
		@HelpText(
				doc = "Specifies the command type of the SOCKS5 request",
				usage = "command=SOCKS5_COMMAND"
		)
		private Command command;
		
		@HelpText(
				doc = "Specifies the address range for the desired destination "
						+ "address of the SOCKS5 request",
				usage = "desiredDestinationAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
		)	
		private AddressRange desiredDestinationAddressRange;
		
		@HelpText(
				doc = "Specifies the port range for the desired destination port "
						+ "of the SOCKS5 request",
				usage = "desiredDestinationPortRange=PORT|PORT1-PORT2"
		)	
		private PortRange desiredDestinationPortRange;
		
		@HelpText(
				doc = "Specifies the logging action to take if the rule applies",
				usage = "logAction=LOG_ACTION"
		)	
		private LogAction logAction;
		
		private String doc;		
		
		public Builder(final RuleAction rlAction) {
			this.ruleAction = Objects.requireNonNull(
					rlAction, "rule action must not be null");
			this.clientAddressRange = null;
			this.method = null;
			this.user = null;
			this.command = null;
			this.desiredDestinationAddressRange = null;
			this.desiredDestinationPortRange = null;
			this.logAction = null;
			this.doc = null;			
		}
		
		public Socks5RequestRule build() {
			return new Socks5RequestRule(this);
		}
		
		public Builder clientAddressRange(
				final AddressRange clientAddrRange) {
			this.clientAddressRange = clientAddrRange;
			return this;
		}
		
		public Builder command(final Command cmd) {
			this.command = cmd;
			return this;
		}
		
		public Builder desiredDestinationAddressRange(
				final AddressRange desiredDestinationAddrRange) {
			this.desiredDestinationAddressRange = desiredDestinationAddrRange;
			return this;
		}
		
		public Builder desiredDestinationPortRange(
				final PortRange desiredDestinationPrtRange) {
			this.desiredDestinationPortRange = desiredDestinationPrtRange;
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
		
		public Builder method(final Method meth) {
			this.method = meth;
			return this;
		}
		
		public Builder user(final String usr) {
			this.user = usr;
			return this;
		}
		
	}

	private static enum Field {
		
		RULE_ACTION("ruleAction") {
			@Override
			public Builder set(final Builder builder, final String value) {
				return new Socks5RequestRule.Builder(RuleAction.valueOfString(
						value));
			}
		},
		
		CLIENT_ADDRESS_RANGE("clientAddressRange") {
			@Override
			public Builder set(final Builder builder, final String value) {
				return builder.clientAddressRange(AddressRange.newInstance(
						value));
			}
		},
		
		METHOD("method") {
			@Override
			public Builder set(final Builder builder, final String value) {
				return builder.method(Method.valueOfString(value));
			}
		},
		
		USER("user") {
			@Override
			public Builder set(final Builder builder, final String value) {
				return builder.user(value);
			}
		},
		
		COMMAND("command") {
			@Override
			public Builder set(final Builder builder, final String value) {
				return builder.command(Command.valueOfString(value));
			}
		},
		
		DESIRED_DESTINATION_ADDRESS_RANGE("desiredDestinationAddressRange") {
			@Override
			public Builder set(final Builder builder, final String value) {
				return builder.desiredDestinationAddressRange(
						AddressRange.newInstance(value));
			}
		},
		
		DESIRED_DESTINATION_PORT_RANGE("desiredDestinationPortRange") {
			@Override
			public Builder set(final Builder builder, final String value) {
				return builder.desiredDestinationPortRange(
						PortRange.newInstance(value));
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

	private static final Socks5RequestRule DEFAULT_INSTANCE = 
			new Socks5RequestRule.Builder(RuleAction.ALLOW).build();
	
	public static Socks5RequestRule getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static List<Socks5RequestRule> newInstances(final String s) {
		List<Socks5RequestRule> socks5RequestRules = 
				new ArrayList<Socks5RequestRule>();
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
				socks5RequestRules.add(recentBuilder.build());
				recentBuilder = builder;
			}
			if (!iterator.hasNext()) {
				socks5RequestRules.add(builder.build());
			}
		}
		return socks5RequestRules;
	}
	
	private final RuleAction ruleAction;
	private final AddressRange clientAddressRange;
	private final Method method;
	private final String user;
	private final Command command;
	private final AddressRange desiredDestinationAddressRange;
	private final PortRange desiredDestinationPortRange;
	private final LogAction logAction;
	private final String doc;
	
	private Socks5RequestRule(final Builder builder) {
		RuleAction rlAction = builder.ruleAction;
		AddressRange clientAddrRange = builder.clientAddressRange;
		Method meth = builder.method;
		String usr = builder.user;
		Command cmd = builder.command;
		AddressRange desiredDestinationAddrRange = 
				builder.desiredDestinationAddressRange;
		PortRange desiredDestinationPrtRange = 
				builder.desiredDestinationPortRange;
		LogAction lgAction = builder.logAction;
		String d = builder.doc;
		this.ruleAction = rlAction;
		this.clientAddressRange = clientAddrRange;
		this.method = meth;
		this.user = usr;
		this.command = cmd;
		this.desiredDestinationAddressRange = desiredDestinationAddrRange;
		this.desiredDestinationPortRange = desiredDestinationPrtRange;
		this.logAction = lgAction;
		this.doc = d;		
	}
	
	public boolean appliesTo(
			final String clientAddress,
			final MethodSubnegotiationResults methSubnegotiationResults,
			final Socks5Request socks5Req) {
		if (this.clientAddressRange != null 
				&& !this.clientAddressRange.contains(clientAddress)) {
			return false;
		}
		if (this.method != null 
				&& !this.method.equals(methSubnegotiationResults.getMethod())) {
			return false;
		}
		if (this.user != null 
				&& !this.user.equals(methSubnegotiationResults.getUser())) {
			return false;
		}
		if (this.command != null
				&& !this.command.equals(socks5Req.getCommand())) {
			return false;
		}
		if (this.desiredDestinationAddressRange != null
				&& !this.desiredDestinationAddressRange.contains(
						socks5Req.getDesiredDestinationAddress())) {
			return false;
		}
		if (this.desiredDestinationPortRange != null
				&& !this.desiredDestinationPortRange.contains(Port.newInstance(
						socks5Req.getDesiredDestinationPort()))) {
			return false;
		}
		return true;
	}

	public void applyTo(
			final String clientAddress,	
			final MethodSubnegotiationResults methSubnegotiationResults,
			final Socks5Request socks5Req) {
		String user = methSubnegotiationResults.getUser();
		String possibleUser = (user != null) ? 
				String.format(" (%s)", user) : "";
		if (this.ruleAction.equals(RuleAction.ALLOW)
				&& this.logAction != null) {
			this.logAction.invoke(String.format(
					"SOCKS5 request from %s%s is allowed based on the "
					+ "following rule: %s. SOCKS5 request: %s",
					clientAddress,
					possibleUser,
					this,
					socks5Req));			
		} else if (this.ruleAction.equals(RuleAction.DENY)) {
			if (this.logAction != null) {
				this.logAction.invoke(String.format(
						"SOCKS5 request from %s%s is denied based on the "
						+ "following rule: %s. SOCKS5 request: %s",
						clientAddress,
						possibleUser,
						this,
						socks5Req));				
			}
			throw new RuleActionDenyException(String.format(
					"SOCKS5 request from %s%s is denied based on the "
					+ "following rule: %s. SOCKS5 request: %s",
					clientAddress,
					possibleUser,
					this,
					socks5Req));
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
		Socks5RequestRule other = (Socks5RequestRule) obj;
		if (this.clientAddressRange == null) {
			if (other.clientAddressRange != null) {
				return false;
			}
		} else if (!this.clientAddressRange.equals(other.clientAddressRange)) {
			return false;
		}
		if (this.command != other.command) {
			return false;
		}
		if (this.desiredDestinationAddressRange == null) {
			if (other.desiredDestinationAddressRange != null) {
				return false;
			}
		} else if (!this.desiredDestinationAddressRange.equals(
				other.desiredDestinationAddressRange)) {
			return false;
		}
		if (this.desiredDestinationPortRange == null) {
			if (other.desiredDestinationPortRange != null) {
				return false;
			}
		} else if (!this.desiredDestinationPortRange.equals(
				other.desiredDestinationPortRange)) {
			return false;
		}
		if (this.logAction != other.logAction) {
			return false;
		}
		if (this.method != other.method) {
			return false;
		}
		if (this.ruleAction != other.ruleAction) {
			return false;
		}
		if (this.user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!this.user.equals(other.user)) {
			return false;
		}
		return true;
	}

	public AddressRange getClientAddressRange() {
		return this.clientAddressRange;
	}
	
	public Command getCommand() {
		return this.command;
	}
	
	public AddressRange getDesiredDestinationAddressRange() {
		return this.desiredDestinationAddressRange;
	}

	public PortRange getDesiredDestinationPortRange() {
		return this.desiredDestinationPortRange;
	}

	public String getDoc() {
		return this.doc;
	}
	
	public LogAction getLogAction() {
		return this.logAction;
	}

	public Method getMethod() {
		return this.method;
	}
	
	public RuleAction getRuleAction() {
		return this.ruleAction;
	}

	public String getUser() {
		return this.user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.clientAddressRange == null) ? 
				0 : this.clientAddressRange.hashCode());
		result = prime * result + ((this.command == null) ? 
				0 : this.command.hashCode());
		result = prime * result
				+ ((this.desiredDestinationAddressRange == null) ? 
						0 : desiredDestinationAddressRange.hashCode());
		result = prime * result + ((this.desiredDestinationPortRange == null) ? 
				0 : desiredDestinationPortRange.hashCode());
		result = prime * result + ((this.logAction == null) ? 
				0 : this.logAction.hashCode());
		result = prime * result + ((this.method == null) ? 
				0 : this.method.hashCode());
		result = prime * result + ((this.ruleAction == null) ? 
				0 : this.ruleAction.hashCode());
		result = prime * result + ((this.user == null) ? 
				0 : this.user.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ruleAction=");
		builder.append(this.ruleAction);
		if (this.clientAddressRange != null) {
			builder.append(" clientAddressRange=");
			builder.append(this.clientAddressRange);			
		}
		if (this.method != null) {
			builder.append(" method=");
			builder.append(this.method);			
		}
		if (this.user != null) {
			builder.append(" user=");
			builder.append(this.user);			
		}
		if (this.command != null) {
			builder.append(" command=");
			builder.append(this.command);
		}
		if (this.desiredDestinationAddressRange != null) {
			builder.append(" desiredDestinationAddressRange=");
			builder.append(this.desiredDestinationAddressRange);			
		}
		if (this.desiredDestinationPortRange != null) {
			builder.append(" desiredDestinationPortRange=");
			builder.append(this.desiredDestinationPortRange);
		}
		if (this.logAction != null) {
			builder.append(" logAction=");
			builder.append(this.logAction);			
		}
		return builder.toString();
	}

}