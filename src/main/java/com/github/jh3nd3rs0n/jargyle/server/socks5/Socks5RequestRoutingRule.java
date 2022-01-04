package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.util.Arrays;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.AddressRange;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.PortRange;
import com.github.jh3nd3rs0n.jargyle.server.Route;
import com.github.jh3nd3rs0n.jargyle.server.RouteNotFoundException;
import com.github.jh3nd3rs0n.jargyle.server.Routes;
import com.github.jh3nd3rs0n.jargyle.server.RoutingRule;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.SelectionStrategy;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public final class Socks5RequestRoutingRule extends RoutingRule {

	public static final class Builder 
		extends RoutingRule.Builder<Builder, Socks5RequestRoutingRule> {

		public static enum Field 
			implements RoutingRule.Builder.Field<Builder, Socks5RequestRoutingRule> {
			
			@HelpText(
					doc = "This field starts a new SOCKS5 request routing rule",
					usage = "routingRule="
			)			
			ROUTING_RULE("routingRule") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return new Builder();
				}
			},			
			
			@HelpText(
					doc = "Specifies the address range for the client address",
					usage = "clientAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
			)	
			CLIENT_ADDRESS_RANGE("clientAddressRange") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return builder.clientAddressRange(AddressRange.newInstance(
							value));
				}
			},
			
			@HelpText(
					doc = "Specifies the address range for the SOCKS server "
							+ "address",
					usage = "socksServerAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
			)	
			SOCKS_SERVER_ADDRESS_RANGE("socksServerAddressRange") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return builder.socksServerAddressRange(
							AddressRange.newInstance(value));
				}
			},

			@HelpText(
					doc = "Specifies the negotiated SOCKS5 method",
					usage = "method=SOCKS5_METHOD"
			)
			METHOD("method") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return builder.method(Method.valueOfString(value));
				}
			},
			
			@HelpText(
					doc = "Specifies the user if any after the negotiated "
							+ "SOCKS5 method",
					usage = "user=USER"
			)
			USER("user") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return builder.user(value);
				}
			},
			
			@HelpText(
					doc = "Specifies the SOCKS5 command of the SOCKS5 request",
					usage = "command=SOCKS5_COMMAND"
			)
			COMMAND("command") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return builder.command(Command.valueOfString(value));
				}
			},
			
			@HelpText(
					doc = "Specifies the address range for the desired "
							+ "destination address of the SOCKS5 request",
					usage = "desiredDestinationAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
			)	
			DESIRED_DESTINATION_ADDRESS_RANGE("desiredDestinationAddressRange") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return builder.desiredDestinationAddressRange(
							AddressRange.newInstance(value));
				}
			},
			
			@HelpText(
					doc = "Specifies the port range for the desired "
							+ "destination port of the SOCKS5 request",
					usage = "desiredDestinationPortRange=PORT|PORT1-PORT2"
			)	
			DESIRED_DESTINATION_PORT_RANGE("desiredDestinationPortRange") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return builder.desiredDestinationPortRange(
							PortRange.newInstance(value));
				}
			},
			
			@HelpText(
					doc = "Specifies the ID for a route. This field can be "
							+ "specified multiple times for each ID for a "
							+ "route.",
					usage = "routeId=ROUTE_ID"
			)			
			ROUTE_ID("routeId") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return builder.addRouteId(value);
				}
			},
			
			@HelpText(
					doc = "Specifies the selection strategy for the next "
							+ "route ID to use from the list of route IDs",
					usage = "routeIdSelectionStrategy=SELECTION_STRATEGY"
			)			
			ROUTE_ID_SELECTION_STRATEGY("routeIdSelectionStrategy") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return builder.routeIdSelectionStrategy(
							SelectionStrategy.valueOfString(value));
				}
			},
			
			@HelpText(
					doc = "Specifies the logging action to take if a route is "
							+ "selected from the list of route IDs",
					usage = "logAction=LOG_ACTION"
			)	
			LOG_ACTION("logAction") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return builder.logAction(LogAction.valueOfString(value));
				}
			};
			
			private final String string;
			
			private Field(final String str) {
				this.string = str;
			}
			
			@Override
			public boolean isRepresentedBy(final String s) {
				return this.string.equals(s);
			}
			
			@Override
			public String toString() {
				return this.string;
			}
			
		}

		private AddressRange clientAddressRange;
		private AddressRange socksServerAddressRange;
		private Method method;
		private String user;
		private Command command;
		private AddressRange desiredDestinationAddressRange;
		private PortRange desiredDestinationPortRange;
		
		public Builder() {
			this.clientAddressRange = null;
			this.socksServerAddressRange = null;
			this.method = null;
			this.user = null;
			this.command = null;
			this.desiredDestinationAddressRange = null;
			this.desiredDestinationPortRange = null;
		}

		@Override
		public Builder addRouteId(final String id) {
			super.addRouteId(id);
			return this;
		}
		
		@Override
		public Socks5RequestRoutingRule build() {
			return new Socks5RequestRoutingRule(this);
		}
		
		public Builder clientAddressRange(final AddressRange clientAddrRange) {
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
		
		@Override
		public Builder doc(final String d) {
			super.doc(d);
			return this;
		}
		
		@Override
		public Builder logAction(final LogAction lgAction) {
			super.logAction(lgAction);
			return this;
		}

		public Builder method(final Method meth) {
			this.method = meth;
			return this;
		}
		
		@Override
		public Builder routeIdSelectionStrategy(
				final SelectionStrategy selectionStrategy) {
			super.routeIdSelectionStrategy(selectionStrategy);
			return this;
		}
		
		public Builder socksServerAddressRange(
				final AddressRange socksServerAddrRange) {
			this.socksServerAddressRange = socksServerAddrRange;
			return this;
		}
		
		public Builder user(final String usr) {
			this.user = usr;
			return this;
		}
 
	}

	public static final class Context extends RoutingRule.Context {
		
		private final String clientAddress;
		private final String socksServerAddress;
		private final MethodSubnegotiationResults methodSubnegotiationResults;
		private final Socks5Request socks5Request;
		
		public Context(
				final String clientAddr,
				final String socksServerAddr,
				final MethodSubnegotiationResults methSubnegotiationResults,
				final Socks5Request socks5Req,
				final Routes routes) {
			super(routes);
			this.clientAddress = clientAddr;
			this.socksServerAddress = socksServerAddr;
			this.methodSubnegotiationResults = methSubnegotiationResults;
			this.socks5Request = socks5Req;
		}

		public String getClientAddress() {
			return this.clientAddress;
		}

		public MethodSubnegotiationResults getMethodSubnegotiationResults() {
			return this.methodSubnegotiationResults;
		}

		public Socks5Request getSocks5Request() {
			return this.socks5Request;
		}

		public String getSocksServerAddress() {
			return this.socksServerAddress;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getSimpleName())
				.append(" [clientAddress=")
				.append(this.clientAddress)
				.append(", socksServerAddress=")
				.append(this.socksServerAddress)
				.append(", methodSubnegotiationResults=")
				.append(this.methodSubnegotiationResults)
				.append(", socks5Request=")
				.append(this.socks5Request)
				.append(", routes=")
				.append(this.getRoutes())
				.append("]");
			return builder.toString();
		}
		
	}
	
	public static List<Socks5RequestRoutingRule> newInstances(final String s) {
		return Rule.newInstances(
				s, 
				Builder.Field.ROUTING_RULE, 
				Arrays.asList(Builder.Field.values()));
	}
	
	private final AddressRange clientAddressRange;
	private final AddressRange socksServerAddressRange;
	private final Method method;
	private final String user;
	private final Command command;
	private final AddressRange desiredDestinationAddressRange;
	private final PortRange desiredDestinationPortRange;

	private Socks5RequestRoutingRule(final Builder builder) {
		super(builder);
		AddressRange clientAddrRange = builder.clientAddressRange;
		AddressRange socksServerAddrRange = builder.socksServerAddressRange;
		Method meth = builder.method;
		String usr = builder.user;
		Command cmd = builder.command;
		AddressRange desiredDestinationAddrRange = 
				builder.desiredDestinationAddressRange;
		PortRange desiredDestinationPrtRange = 
				builder.desiredDestinationPortRange;
		this.clientAddressRange = clientAddrRange;
		this.socksServerAddressRange = socksServerAddrRange;
		this.method = meth;
		this.user = usr;
		this.command = cmd;
		this.desiredDestinationAddressRange = desiredDestinationAddrRange;
		this.desiredDestinationPortRange = desiredDestinationPrtRange;
	}

	@Override
	public boolean appliesBasedOn(final Rule.Context context) {
		if (!(context instanceof Context)) {
			return false;
		}
		Context cntxt = (Context) context;
		MethodSubnegotiationResults methSubnegotiationResults =
				cntxt.getMethodSubnegotiationResults();
		Socks5Request socks5Req = cntxt.getSocks5Request();
		if (this.clientAddressRange != null 
				&& !this.clientAddressRange.contains(
						cntxt.getClientAddress())) {
			return false;
		}
		if (this.socksServerAddressRange != null
				&& !this.socksServerAddressRange.contains(
						cntxt.getSocksServerAddress())) {
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
		Socks5RequestRoutingRule other = (Socks5RequestRoutingRule) obj;
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
		if (this.getLogAction() != other.getLogAction()) {
			return false;
		}
		if (this.method != other.method) {
			return false;
		}
		if (this.getRouteIds() == null) {
			if (other.getRouteIds() != null) {
				return false;
			}
		} else if (!this.getRouteIds().equals(other.getRouteIds())) {
			return false;
		}
		if (this.getRouteIdSelectionStrategy() != other.getRouteIdSelectionStrategy()) {
			return false;
		}
		if (this.socksServerAddressRange == null) {
			if (other.socksServerAddressRange != null) {
				return false;
			}
		} else if (!this.socksServerAddressRange.equals(
				other.socksServerAddressRange)) {
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

	public Method getMethod() {
		return this.method;
	}
	
	public AddressRange getSocksServerAddressRange() {
		return this.socksServerAddressRange;
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
						0 : this.desiredDestinationAddressRange.hashCode());
		result = prime * result + ((this.desiredDestinationPortRange == null) ? 
				0 : this.desiredDestinationPortRange.hashCode());
		result = prime * result + ((this.getLogAction() == null) ? 
				0 : this.getLogAction().hashCode());
		result = prime * result + ((this.method == null) ? 
				0 : this.method.hashCode());
		result = prime * result + ((this.getRouteIds() == null) ? 
				0 : this.getRouteIds().hashCode());
		result = prime * result + ((this.getRouteIdSelectionStrategy() == null) ? 
				0 : this.getRouteIdSelectionStrategy().hashCode());
		result = prime * result + ((this.socksServerAddressRange == null) ? 
				0 : this.socksServerAddressRange.hashCode());
		result = prime * result + ((this.user == null) ? 
				0 : this.user.hashCode());
		return result;
	}

	@Override
	public Route selectRoute(final Rule.Context context) {
		String routeId = this.getRouteIdSelector().select();
		Route route = null;
		if (routeId != null) {
			Context cntxt = (Context) context;
			Routes routes = cntxt.getRoutes();
			route = routes.get(routeId);
			if (route == null) {
				throw new RouteNotFoundException(routeId);
			}
			LogAction logAction = this.getLogAction();
			if (logAction != null) {
				String clientAddress = cntxt.getClientAddress();
				MethodSubnegotiationResults methSubnegotiationResults =
						cntxt.getMethodSubnegotiationResults();
				String user = methSubnegotiationResults.getUser();
				String possibleUser = (user != null) ? 
						String.format(" (%s)", user) : "";
				logAction.invoke(String.format(
						"SOCKS5 request route '%s' for %s%s is selected from "
						+ "the following routing rule and context: %s, %s", 
						routeId,
						clientAddress,
						possibleUser,
						this,
						context));
			}
		}
		return route;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("routingRule=");
		if (this.clientAddressRange != null) {
			builder.append(" clientAddressRange=");
			builder.append(this.clientAddressRange);			
		}
		if (this.socksServerAddressRange != null) {
			builder.append(" socksServerAddressRange=");
			builder.append(this.socksServerAddressRange);
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
		List<String> routeIds = this.getRouteIds();
		if (routeIds.size() > 0) {
			for (String routeId : routeIds) {
				builder.append(" routeId=");
				builder.append(routeId);
			}
		}
		SelectionStrategy routeIdSelectionStrategy = 
				this.getRouteIdSelectionStrategy();
		if (routeIdSelectionStrategy != null) {
			builder.append(" routeIdSelectionStrategy=");
			builder.append(routeIdSelectionStrategy);
		}
		LogAction logAction = this.getLogAction();
		if (logAction != null) {
			builder.append(" logAction=");
			builder.append(logAction);			
		}
		return builder.toString();
	}
	
}
