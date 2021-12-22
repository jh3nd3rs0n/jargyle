package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class RoutingRule extends Rule {

	public static abstract class Builder<B extends Builder<B, R>, R extends RoutingRule> 
		extends Rule.Builder<B, R> {
		
		private final List<String> routeIds;
		private SelectionStrategy routeIdSelectionStrategy;
		
		public Builder() {
			this.routeIds = new ArrayList<String>();
			this.routeIdSelectionStrategy = null;
		}
		
		public B addRouteId(final String id) {
			this.routeIds.add(id);
			@SuppressWarnings("unchecked")
			B builder = (B) this;
			return builder;			
		}

		public abstract R build();
		
		public B doc(final String d) {
			super.doc(d);
			@SuppressWarnings("unchecked")
			B builder = (B) this;
			return builder;		
		}

		public B logAction(final LogAction lgAction) {
			super.logAction(lgAction);
			@SuppressWarnings("unchecked")
			B builder = (B) this;
			return builder;
		}
		
		public final List<String> routeIds() {
			return Collections.unmodifiableList(this.routeIds);
		}
		
		public final SelectionStrategy routeIdSelectionStrategy() {
			return this.routeIdSelectionStrategy;
		}
		
		public B routeIdSelectionStrategy(
				final SelectionStrategy selectionStrategy) {
			this.routeIdSelectionStrategy = selectionStrategy;
			@SuppressWarnings("unchecked")
			B builder = (B) this;
			return builder;
		}
		
	}
	
	public static abstract class Context extends Rule.Context {
		
		private final Routes routes;
		
		public Context(final Routes rtes) {
			this.routes = rtes;
		}
		
		public final Routes getRoutes() {
			return this.routes;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getSimpleName())
				.append(" [routes=")
				.append(this.routes)
				.append("]");
			return builder.toString();
		}
		
	}
	
	private final List<String> routeIds;
	private final SelectionStrategy routeIdSelectionStrategy;
	private final Selector<String> routeIdSelector;	
	
	protected RoutingRule(final Builder<?, ?> builder) {
		super(builder);
		List<String> ids = new ArrayList<String>(builder.routeIds);
		SelectionStrategy selectionStrategy = builder.routeIdSelectionStrategy;		
		this.routeIds = ids;
		this.routeIdSelectionStrategy = selectionStrategy;
		this.routeIdSelector = (selectionStrategy != null) ? 
				selectionStrategy.newSelector(ids) : SelectionStrategy.CYCLICAL.newSelector(ids);
	}
	
	public final List<String> getRouteIds() {
		return Collections.unmodifiableList(this.routeIds);
	}
	
	public final SelectionStrategy getRouteIdSelectionStrategy() {
		return this.routeIdSelectionStrategy;
	}

	protected final Selector<String> getRouteIdSelector() {
		return this.routeIdSelector;
	}
	
	public abstract Route selectRoute(final Rule.Context context);
	
}
