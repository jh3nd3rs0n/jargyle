package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import java.util.Arrays;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.Route;
import com.github.jh3nd3rs0n.jargyle.server.Routes;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.SelectionStrategy;

public final class ClientRoutingRule extends RoutingRule {

	public static final class Builder
		extends RoutingRule.Builder<Builder, ClientRoutingRule> {

		public static enum Field 
			implements RoutingRule.Builder.Field<Builder, ClientRoutingRule> {
			
			@HelpText(
					doc = "This field starts a new client routing rule",
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
		
		public Builder() {
			this.clientAddressRange = null;
			this.socksServerAddressRange = null;
		}

		@Override
		public Builder addRouteId(final String id) {
			super.addRouteId(id);
			return this;
		}
		
		@Override
		public ClientRoutingRule build() {
			return new ClientRoutingRule(this);
		}
		
		public Builder clientAddressRange(final AddressRange clientAddrRange) {
			this.clientAddressRange = clientAddrRange;
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
	}

	public static final class Context extends RoutingRule.Context {

		private final String clientAddress;
		private final String socksServerAddress;
		
		public Context(
				final String clientAddr,
				final String socksServerAddr,
				final Routes rtes) {
			super(rtes);
			this.clientAddress = clientAddr;			
			this.socksServerAddress = socksServerAddr;
		}

		public String getClientAddress() {
			return this.clientAddress;
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
				.append(", routes=")
				.append(this.getRoutes())
				.append("]");
			return builder.toString();
		}
		
	}
	
	public static List<ClientRoutingRule> newInstances(final String s) {
		return Rule.newInstances(
				s, 
				Builder.Field.ROUTING_RULE, 
				Arrays.asList(Builder.Field.values()));
	}
	
	private final AddressRange clientAddressRange;
	private final AddressRange socksServerAddressRange;
	
	private ClientRoutingRule(final Builder builder) {
		super(builder);
		AddressRange clientAddrRange = builder.clientAddressRange;		
		AddressRange socksServerAddrRange = builder.socksServerAddressRange;
		this.clientAddressRange = clientAddrRange;		
		this.socksServerAddressRange = socksServerAddrRange;
	}

	@Override
	public boolean appliesBasedOn(final Rule.Context context) {
		if (!(context instanceof Context)) {
			return false;
		}
		ClientRoutingRule.Context cntxt = (ClientRoutingRule.Context) context;
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
		ClientRoutingRule other = (ClientRoutingRule) obj;
		if (this.clientAddressRange == null) {
			if (other.clientAddressRange != null) {
				return false;
			}
		} else if (!this.clientAddressRange.equals(
				other.clientAddressRange)) {
			return false;
		}
		if (this.getLogAction() != other.getLogAction()) {
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
		return true;
	}

	public AddressRange getClientAddressRange() {
		return this.clientAddressRange;
	}
	
	public AddressRange getSocksServerAddressRange() {
		return this.socksServerAddressRange;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.clientAddressRange == null) ? 
				0 : this.clientAddressRange.hashCode());
		result = prime * result + ((this.getLogAction() == null) ? 
				0 : this.getLogAction().hashCode());
		result = prime * result + ((this.getRouteIds() == null) ? 
				0 : this.getRouteIds().hashCode());
		result = prime * result + ((this.getRouteIdSelectionStrategy() == null) ? 
				0 : this.getRouteIdSelectionStrategy().hashCode());
		result = prime * result + ((this.socksServerAddressRange == null) ? 
				0 : this.socksServerAddressRange.hashCode());
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
				logAction.invoke(String.format(
						"Client route '%s' for %s is selected from the "
						+ "following routing rule and context: %s, %s", 
						routeId,
						clientAddress,
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
