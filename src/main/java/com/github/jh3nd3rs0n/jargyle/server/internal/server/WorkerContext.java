package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
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

public class WorkerContext implements Closeable {

	private final Set<Rule> belowAllowLimitRules;
	private final DtlsDatagramSocketFactory clientFacingDtlsDatagramSocketFactory;
	private Socket clientFacingSocket;
	private boolean closed;
	private final Configuration configuration;
	private final Routes routes;
	private final Rules rules;
	private Route selectedRoute;	
	private final WorkerContext workerContext;
	
	WorkerContext(
			final Socket clientFacingSock,
			final Configuration config,
			final Rules rls,
			final Routes rtes,
			final Route selectedRte,
			final DtlsDatagramSocketFactory clientFacingDtlsDatagramSockFactory) {
		Objects.requireNonNull(clientFacingSock);
		Objects.requireNonNull(config);
		Objects.requireNonNull(rls);
		Objects.requireNonNull(rtes);
		Objects.requireNonNull(selectedRte);
		this.belowAllowLimitRules = new HashSet<Rule>();
		this.clientFacingDtlsDatagramSocketFactory = 
				clientFacingDtlsDatagramSockFactory;
		this.clientFacingSocket = clientFacingSock;
		this.closed = false;
		this.configuration = config;
		this.routes = rtes;
		this.rules = rls;
		this.selectedRoute = selectedRte;		
		this.workerContext = null;
	}
	
	protected WorkerContext(final WorkerContext context) {
		Objects.requireNonNull(context);
		this.belowAllowLimitRules = null;
		this.clientFacingDtlsDatagramSocketFactory = null;
		this.clientFacingSocket = null;
		this.closed = false;
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
	
	@Override
	public final void close() throws IOException {
		if (this.workerContext != null) {
			this.workerContext.close();
			return;
		}
		if (this.closed) {
			throw new IllegalStateException("already closed");
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
		this.clientFacingSocket.close();
		this.closed = true;
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
	
	public final Socket getClientFacingSocket() {
		if (this.workerContext != null) {
			return this.workerContext.getClientFacingSocket();
		}
		return this.clientFacingSocket;
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
	
	public final boolean isClosed() {
		if (this.workerContext != null) {
			return this.workerContext.isClosed();
		}
		return this.closed;
	}
	
	public final void setClientFacingSocket(final Socket clientFacingSock) {
		if (this.workerContext != null) {
			this.workerContext.setClientFacingSocket(clientFacingSock);
			return;
		}
		this.clientFacingSocket = Objects.requireNonNull(clientFacingSock);
	}
	
	public final void setSelectedRoute(final Route selectedRte) {
		if (this.workerContext != null) {
			this.workerContext.setSelectedRoute(selectedRte);
			return;
		}
		this.selectedRoute = Objects.requireNonNull(selectedRte);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [getClientFacingSocket()=")
			.append(this.getClientFacingSocket())
			.append("]");
		return builder.toString();
	}
	
	public final void writeThenFlush(final byte[] b) throws IOException {
		if (this.workerContext != null) {
			this.workerContext.writeThenFlush(b);
			return;
		}
		OutputStream clientFacingOutputStream = 
				this.clientFacingSocket.getOutputStream();
		clientFacingOutputStream.write(b);
		clientFacingOutputStream.flush();
	}
	
}
