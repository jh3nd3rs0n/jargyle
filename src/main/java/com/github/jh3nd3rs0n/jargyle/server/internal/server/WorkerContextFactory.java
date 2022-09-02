package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.ConfigurationsHelper;
import com.github.jh3nd3rs0n.jargyle.server.FirewallAction;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleArgSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ImmutableConfiguration;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.NonnegativeIntegerLimit;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.SelectionStrategy;
import com.github.jh3nd3rs0n.jargyle.server.Settings;

final class WorkerContextFactory {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(
			WorkerContextFactory.class);
	
	private DtlsDatagramSocketFactory clientFacingDtlsDatagramSocketFactory;
	private SslSocketFactory clientSslSocketFactory;
	private final Configuration configuration;
	private Configuration lastConfiguration;
	private SelectionStrategy routeIdSelectionStrategy;
	private Routes routes;
	private Rules rules;
	
	public WorkerContextFactory(final Configuration config) {
		this.clientFacingDtlsDatagramSocketFactory = null;
		this.clientSslSocketFactory = null;
		this.configuration = config;
		this.lastConfiguration = null;
		this.routeIdSelectionStrategy = null;
		this.routes = null;
		this.rules = null;
	}
	
	private boolean canAllowClientSocket(
			final Rule applicableRule,
			final RuleContext clientRuleContext,
			final Set<Rule> belowAllowLimitRules) {
		if (applicableRule == null) {
			return false;
		}
		FirewallAction firewallAction = applicableRule.getLastRuleResultValue(
				GeneralRuleResultSpecConstants.FIREWALL_ACTION);
		if (firewallAction == null) {
			return false;
		}
		LogAction firewallActionLogAction = 
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_LOG_ACTION);
		String clientAddress = clientRuleContext.getRuleArgValue(
				GeneralRuleArgSpecConstants.CLIENT_ADDRESS);
		String socksServerAddress = clientRuleContext.getRuleArgValue(
				GeneralRuleArgSpecConstants.SOCKS_SERVER_ADDRESS);		
		if (firewallAction.equals(FirewallAction.ALLOW)) {
			if (!this.canAllowClientSocketWithinLimit(
					applicableRule, clientRuleContext, belowAllowLimitRules)) {
				return false;
			}
			if (firewallActionLogAction != null) {
				firewallActionLogAction.invoke(LOGGER, String.format(
						"Client (%s) to SOCKS server (%s) allowed based on "
						+ "the following rule and context: rule: %s context: "
						+ "%s",
						clientAddress,
						socksServerAddress,
						applicableRule,
						clientRuleContext));
			}
		} else if (firewallAction.equals(FirewallAction.DENY)
				&& firewallActionLogAction != null) {
			firewallActionLogAction.invoke(LOGGER, String.format(
					"Client (%s) to SOCKS server (%s) denied based on the "
					+ "following rule and context: rule: %s context: %s",
					clientAddress,
					socksServerAddress,
					applicableRule,
					clientRuleContext));				
		}
		return FirewallAction.ALLOW.equals(firewallAction);
	}
	
	private boolean canAllowClientSocketWithinLimit(
			final Rule applicableRule,
			final RuleContext clientRuleContext,
			final Set<Rule> belowAllowLimitRules) {
		NonnegativeIntegerLimit firewallActionAllowLimit =
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT);
		LogAction firewallActionAllowLimitReachedLogAction =
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT_REACHED_LOG_ACTION);
		if (firewallActionAllowLimit != null) {
			if (firewallActionAllowLimit.hasBeenReached()) {
				if (firewallActionAllowLimitReachedLogAction != null) {
					firewallActionAllowLimitReachedLogAction.invoke(
							LOGGER, 
							String.format(
									"Allowed limit has been reached based on "
									+ "the following rule and context: rule: "
									+ "%s context: %s",
									applicableRule,
									clientRuleContext));
				}
				return false;
			}
			firewallActionAllowLimit.incrementCurrentCount();
			belowAllowLimitRules.add(applicableRule);				
		}		
		return true;
	}
	
	private void configureClientSocket(
			final Socket clientSock,
			final Rule applicableRule,
			final Configuration config) throws SocketException {
		SocketSettings socketSettings = this.getClientSocketSettings(
				applicableRule, config);
		socketSettings.applyTo(clientSock);
	}
	
	private SocketSettings getClientSocketSettings(
			final Rule applicableRule, final Configuration config) {
		List<SocketSetting<Object>> socketSettings = 
				applicableRule.getRuleResultValues(
						GeneralRuleResultSpecConstants.CLIENT_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			List<SocketSetting<? extends Object>> socketSttngs = 
					new ArrayList<SocketSetting<? extends Object>>(
							socketSettings); 
			return SocketSettings.newInstance(socketSttngs);
		}
		Settings settings = config.getSettings();
		return settings.getLastValue(
				GeneralSettingSpecConstants.CLIENT_SOCKET_SETTINGS);
	}
	
	private RuleContext newClientRuleContext(final Socket clientSock) {
		RuleContext clientRuleContext = new RuleContext();
		clientRuleContext.putRuleArgValue(
				GeneralRuleArgSpecConstants.CLIENT_ADDRESS, 
				clientSock.getInetAddress().getHostAddress());
		clientRuleContext.putRuleArgValue(
				GeneralRuleArgSpecConstants.SOCKS_SERVER_ADDRESS, 
				clientSock.getLocalAddress().getHostAddress());
		return clientRuleContext;
	}
	
	private Configuration newConfiguration() {
		Configuration config = ImmutableConfiguration.newInstance(
				this.configuration);
		synchronized (this) {
			if (!ConfigurationsHelper.equals(this.lastConfiguration, config)) {
				this.clientFacingDtlsDatagramSocketFactory =
						DtlsDatagramSocketFactoryImpl.isDtlsEnabled(config) ?
								new DtlsDatagramSocketFactoryImpl(config) : null;
				this.clientSslSocketFactory =
						SslSocketFactoryImpl.isSslEnabled(config) ?
								new SslSocketFactoryImpl(config) : null;
				this.routeIdSelectionStrategy = 
						config.getSettings().getLastValue(
								GeneralSettingSpecConstants.ROUTE_ID_SELECTION_STRATEGY);
				this.routes = Routes.newInstance(config);
				this.rules = Rules.newInstance(config);
				this.lastConfiguration = config;
			}
		}
		return config;
	}
	
	public WorkerContext newWorkerContext(
			final Socket clientSocket) throws IOException {
		Configuration config = this.newConfiguration();
		Socket clientSock = clientSocket;
		RuleContext clientRuleContext = this.newClientRuleContext(
				clientSock);
		Rule applicableRule = this.rules.firstAppliesTo(clientRuleContext);
		Set<Rule> belowAllowLimitRules = new HashSet<Rule>();
		if (!this.canAllowClientSocket(
				applicableRule, clientRuleContext, belowAllowLimitRules)) {
			throw new IllegalArgumentException(
					"client socket not allowed");
		}
		this.configureClientSocket(clientSock, applicableRule, config);
		clientSock = this.wrapClientSocket(clientSock);
		Route selectedRoute = this.selectRoute(
				applicableRule, clientRuleContext, config);
		WorkerContext workerContext = new WorkerContext(
				clientSock, 
				config,
				this.rules,
				this.routes,
				selectedRoute,
				this.clientFacingDtlsDatagramSocketFactory);
		// should only be one below allow limit rule
		for (Rule belowAllowLimitRule : belowAllowLimitRules) {
			workerContext.addBelowAllowLimitRule(belowAllowLimitRule);
		}
		return workerContext;
	}
	
	private Route selectRoute(
			final Rule applicableRule,
			final RuleContext clientRuleContext,
			final Configuration config) {
		List<String> rteIds = applicableRule.getRuleResultValues(
				GeneralRuleResultSpecConstants.ROUTE_ID);
		SelectionStrategy rteIdSelectionStrategy = 
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.ROUTE_ID_SELECTION_STRATEGY);
		LogAction rteIdSelectionLogAction = 
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.ROUTE_ID_SELECTION_LOG_ACTION);
		Route selectedRte = null;
		if (rteIds.size() > 0 && rteIdSelectionStrategy != null) {
			String selectedRteId = rteIdSelectionStrategy.selectFrom(rteIds);
			selectedRte = this.routes.get(selectedRteId);
			if (selectedRte != null && rteIdSelectionLogAction != null) {
				rteIdSelectionLogAction.invoke(LOGGER, String.format(
						"Route '%s' selected based on the following rule "
						+ "and context: rule: %s context: %s",
						selectedRteId,
						applicableRule,
						clientRuleContext));				
			}
		}
		if (selectedRte != null) {
			return selectedRte;
		}
		List<String> routeIds = this.routes.toMap().keySet().stream().collect(
				Collectors.toList());
		String selectedRouteId = this.routeIdSelectionStrategy.selectFrom(
				routeIds);
		Route selectedRoute = this.routes.get(selectedRouteId);
		Settings settings = config.getSettings();
		LogAction routeIdSelectionLogAction = settings.getLastValue(
				GeneralSettingSpecConstants.ROUTE_ID_SELECTION_LOG_ACTION);
		if (routeIdSelectionLogAction != null) {
			routeIdSelectionLogAction.invoke(LOGGER, String.format(
					"Route '%s' selected", 
					selectedRouteId));
		}
		return selectedRoute;
	}
	
	private Socket wrapClientSocket(
			final Socket clientSock) throws IOException {
		Socket clientSck = clientSock;
		if (this.clientSslSocketFactory != null) {
			clientSck = this.clientSslSocketFactory.newSocket(
					clientSck, null, true);
		}		
		return clientSck;
	}
	
}
