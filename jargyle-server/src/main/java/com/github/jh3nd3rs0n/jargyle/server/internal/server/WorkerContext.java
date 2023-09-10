package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.FirewallAction;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.NonnegativeIntegerLimit;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.Settings;

public class WorkerContext {

	private Rule applicableRule;
	private final Set<Rule> belowAllowLimitRules;
	private final DtlsDatagramSocketFactory clientFacingDtlsDatagramSocketFactory;
	private final Configuration configuration;
	private final Routes routes;
	private final Rules rules;
	private Route selectedRoute;	
	private final WorkerContext workerContext;
	
	WorkerContext(
			final Configuration config,
			final Rules rls,
			final Routes rtes,
			final DtlsDatagramSocketFactory clientFacingDtlsDatagramSockFactory) {
		Objects.requireNonNull(config);
		Objects.requireNonNull(rls);
		Objects.requireNonNull(rtes);
		this.applicableRule = null;
		this.belowAllowLimitRules = new HashSet<Rule>();
		this.clientFacingDtlsDatagramSocketFactory = 
				clientFacingDtlsDatagramSockFactory;
		this.configuration = config;
		this.routes = rtes;
		this.rules = rls;
		this.selectedRoute = null;		
		this.workerContext = null;
	}
	
	protected WorkerContext(final WorkerContext context) {
		Objects.requireNonNull(context);
		this.applicableRule = null;
		this.belowAllowLimitRules = null;
		this.clientFacingDtlsDatagramSocketFactory = null;
		this.configuration = null;
		this.routes = null;
		this.rules = null;
		this.selectedRoute = null;		
		this.workerContext = context;
	}

	public final void addBelowAllowLimitRule(final Rule belowAllowLimitRl) {
		if (this.workerContext != null) {
			this.workerContext.addBelowAllowLimitRule(belowAllowLimitRl);
			return;
		}
		FirewallAction firewallAction = 
				belowAllowLimitRl.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION);
		if (firewallAction == null 
				|| !firewallAction.equals(FirewallAction.ALLOW)) {
			throw new IllegalArgumentException(String.format(
					"rule must have a rule result of a firewall action of %s", 
					FirewallAction.ALLOW));
		}
		NonnegativeIntegerLimit firewallActionAllowLimit =
				belowAllowLimitRl.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT);
		if (firewallActionAllowLimit == null) {
			throw new IllegalArgumentException(
					"rule must have a rule result of a firewall action allow "
					+ "limit");
		}
		this.belowAllowLimitRules.add(Objects.requireNonNull(
				belowAllowLimitRl));
	}
	
	public final void decrementCurrentAllowCounts() {
		if (this.workerContext != null) {
			this.workerContext.decrementCurrentAllowCounts();
			return;
		}
		for (Rule belowAllowLimitRule : this.belowAllowLimitRules) {
			FirewallAction firewallAction = 
					belowAllowLimitRule.getLastRuleResultValue(
							GeneralRuleResultSpecConstants.FIREWALL_ACTION);
			NonnegativeIntegerLimit firewallActionAllowLimit =
					belowAllowLimitRule.getLastRuleResultValue(
							GeneralRuleResultSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT);
			if (firewallAction != null 
					&& firewallAction.equals(FirewallAction.ALLOW)
					&& firewallActionAllowLimit != null) {
				firewallActionAllowLimit.decrementCurrentCount();
			}
		}		
	}
	
	public final Rule getApplicableRule() {
		if (this.workerContext != null) {
			return this.workerContext.getApplicableRule();
		}
		return this.applicableRule;
	}
	
	public final Set<Rule> getBelowAllowLimitRules() {
		if (this.workerContext != null) {
			return this.workerContext.getBelowAllowLimitRules();
		}
		return Collections.unmodifiableSet(this.belowAllowLimitRules);
	}
	
	public final DtlsDatagramSocketFactory getClientFacingDtlsDatagramSocketFactory() {
		if (this.workerContext != null) {
			return this.workerContext.getClientFacingDtlsDatagramSocketFactory();
		}
		return this.clientFacingDtlsDatagramSocketFactory;
	}
	
	public final Configuration getConfiguration() {
		if (this.workerContext != null) {
			return this.workerContext.getConfiguration();
		}
		return this.configuration;
	}

	public final Routes getRoutes() {
		if (this.workerContext != null) {
			return this.workerContext.getRoutes();
		}
		return this.routes;
	}

	public final Rules getRules() {
		if (this.workerContext != null) {
			return this.workerContext.getRules();
		}
		return this.rules;
	}
	
	public final Route getSelectedRoute() {
		if (this.workerContext != null) {
			return this.workerContext.getSelectedRoute();
		}
		return this.selectedRoute;
	}
	
	public final Settings getSettings() {
		if (this.workerContext != null) {
			return this.workerContext.getSettings();
		}
		return this.configuration.getSettings();
	}
	
	public final void setApplicableRule(final Rule applicableRl) {
		if (this.workerContext != null) {
			this.workerContext.setApplicableRule(applicableRl);
			return;
		}
		this.applicableRule = Objects.requireNonNull(applicableRl);
	}
	
	public final void setSelectedRoute(final Route selectedRte) {
		if (this.workerContext != null) {
			this.workerContext.setSelectedRoute(selectedRte);
			return;
		}
		this.selectedRoute = Objects.requireNonNull(selectedRte);
	}
	
}
