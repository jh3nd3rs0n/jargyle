package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
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
	
	private static final class RuleHolder {
		private Rule rule;
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(
			WorkerContextFactory.class);
	
	private DtlsDatagramSocketFactory clientFacingDtlsDatagramSocketFactory;
	private SslSocketFactory clientSslSocketFactory;
	private final Configuration configuration;
	private Configuration lastConfiguration;
	private final ReentrantLock lock;
	private Routes routes;
	private Rules rules;
	
	public WorkerContextFactory(final Configuration config) {
		this.clientFacingDtlsDatagramSocketFactory = null;
		this.clientSslSocketFactory = null;
		this.configuration = config;
		this.lastConfiguration = null;
		this.lock = new ReentrantLock();
		this.routes = null;
		this.rules = null;
	}
	
	private boolean canAllowClientSocket(
			final Rule applicableRule,
			final RuleContext clientRuleContext,
			final RuleHolder belowAllowLimitRuleHolder) {
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
		if (firewallAction.equals(FirewallAction.ALLOW)) {
			if (!this.canAllowClientSocketWithinLimit(
					applicableRule, 
					clientRuleContext, 
					belowAllowLimitRuleHolder)) {
				return false;
			}
			if (firewallActionLogAction != null) {
				firewallActionLogAction.invoke(LOGGER, String.format(
						"Client allowed based on the following rule and "
						+ "context: rule: %s context: %s",
						applicableRule,
						clientRuleContext));
			}
		} else if (firewallAction.equals(FirewallAction.DENY)
				&& firewallActionLogAction != null) {
			firewallActionLogAction.invoke(LOGGER, String.format(
					"Client denied based on the following rule and context: "
					+ "rule: %s context: %s",
					applicableRule,
					clientRuleContext));				
		}
		return FirewallAction.ALLOW.equals(firewallAction);
	}
	
	private boolean canAllowClientSocketWithinLimit(
			final Rule applicableRule,
			final RuleContext clientRuleContext,
			final RuleHolder belowAllowLimitRuleHolder) {
		NonnegativeIntegerLimit firewallActionAllowLimit =
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT);
		LogAction firewallActionAllowLimitReachedLogAction =
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT_REACHED_LOG_ACTION);
		if (firewallActionAllowLimit != null) {
			if (!firewallActionAllowLimit.tryIncrementCurrentCount()) {
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
			belowAllowLimitRuleHolder.rule = applicableRule;
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
			return SocketSettings.newInstance(
					socketSettings.stream().collect(Collectors.toList()));
		}
		socketSettings = applicableRule.getRuleResultValues(
				GeneralRuleResultSpecConstants.SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.newInstance(
					socketSettings.stream().collect(Collectors.toList()));
		}
		Settings settings = config.getSettings();
		SocketSettings socketSttngs = settings.getLastValue(
				GeneralSettingSpecConstants.CLIENT_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		socketSttngs = settings.getLastValue(
				GeneralSettingSpecConstants.SOCKET_SETTINGS);
		return socketSttngs;
	}
	
	private Routes getRoutes(final Rule applicableRule) {
		List<Route> rtes = applicableRule.getRuleResultValues(
				GeneralRuleResultSpecConstants.SELECTABLE_ROUTE_ID)
				.stream()
				.map(rteId -> this.routes.get(rteId))
				.filter(rte -> rte != null)
				.collect(Collectors.toList());
		if (rtes.size() > 0) {
			return Routes.newInstance(rtes);
		}
		return this.routes;
	}
	
	private LogAction getRouteSelectionLogAction(
			final Rule applicableRule,
			final Configuration config) {
		LogAction routeSelectionLogAction = 
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.ROUTE_SELECTION_LOG_ACTION);
		if (routeSelectionLogAction != null) {
			return routeSelectionLogAction;
		}
		return config.getSettings().getLastValue(
				GeneralSettingSpecConstants.ROUTE_SELECTION_LOG_ACTION);
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
		this.lock.lock();
		try {
			if (!ConfigurationsHelper.equals(this.lastConfiguration, config)) {
				this.clientFacingDtlsDatagramSocketFactory =
						DtlsDatagramSocketFactoryImpl.isDtlsEnabled(config) ?
								new DtlsDatagramSocketFactoryImpl(config) : null;
				this.clientSslSocketFactory =
						SslSocketFactoryImpl.isSslEnabled(config) ?
								new SslSocketFactoryImpl(config) : null;
				this.routes = Routes.newInstance(config);
				this.rules = Rules.newInstance(config);
				this.lastConfiguration = config;
			}			
		} finally {
			this.lock.unlock();
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
		RuleHolder belowAllowLimitRuleHolder = new RuleHolder();
		if (!this.canAllowClientSocket(
				applicableRule, clientRuleContext, belowAllowLimitRuleHolder)) {
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
		Rule belowAllowLimitRule = belowAllowLimitRuleHolder.rule;
		if (belowAllowLimitRule != null) {
			workerContext.addBelowAllowLimitRule(belowAllowLimitRule);
		}
		return workerContext;
	}
	
	private Route selectRoute(
			final Rule applicableRule,
			final RuleContext clientRuleContext,
			final Configuration config) {
		SelectionStrategy rteSelectionStrategy = 
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.ROUTE_SELECTION_STRATEGY);
		Routes rtes = null;
		LogAction rteSelectionLogAction = null;
		String rteSelectionLogMessageFormat = null;
		if (rteSelectionStrategy != null) {
			rtes = this.getRoutes(applicableRule);
			rteSelectionLogAction =	this.getRouteSelectionLogAction(
					applicableRule, config);
			rteSelectionLogMessageFormat = String.format(
					"Route '%s' selected based on the following rule "
					+ "and context: rule: %s context: %s",
					"%s",
					applicableRule,
					clientRuleContext);
		} else {
			Settings settings = config.getSettings();
			rteSelectionStrategy = settings.getLastValue(
					GeneralSettingSpecConstants.ROUTE_SELECTION_STRATEGY);
			rtes = this.routes;
			rteSelectionLogAction = settings.getLastValue(
					GeneralSettingSpecConstants.ROUTE_SELECTION_LOG_ACTION);
			rteSelectionLogMessageFormat = "Route '%s' selected";
		}
		Route selectedRte = rteSelectionStrategy.selectFrom(
				rtes.toMap().values().stream().collect(Collectors.toList()));
		if (rteSelectionLogAction != null) {
			rteSelectionLogAction.invoke(LOGGER, String.format(
					rteSelectionLogMessageFormat, 
					selectedRte.getId()));
		}
		return selectedRte;		
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
