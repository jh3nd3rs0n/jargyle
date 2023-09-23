package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.common.lang.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.internal.lang.ThrowableHelper;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.FirewallAction;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleArgSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.NonnegativeIntegerLimit;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.SelectionStrategy;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5.Socks5Worker;
import com.github.jh3nd3rs0n.jargyle.transport.SocksException;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Version;

public class Worker implements Runnable {
	
	private Rule applicableRule;
	private final Set<Rule> belowAllowLimitRules;	
	private final DtlsDatagramSocketFactory clientFacingDtlsDatagramSocketFactory;
	private Socket clientSocket;
	private final SslSocketFactory clientSslSocketFactory;
	private final Configuration configuration;
	private final Logger logger;
	private final Worker otherWorker;	
	private final Routes routes;
	private RuleContext ruleContext;
	private final Rules rules;
	private Route selectedRoute;	
	private final AtomicInteger totalWorkerCount;
	
	Worker(
			final Socket clientSock, 
			final AtomicInteger workerCount,
			final Configuration config) {
		this.applicableRule = null;
		this.belowAllowLimitRules = new HashSet<Rule>();
		this.clientFacingDtlsDatagramSocketFactory = 
				DtlsDatagramSocketFactoryImpl.isDtlsEnabled(config) ? 
						new DtlsDatagramSocketFactoryImpl(config) : null;
		this.clientSocket = clientSock;
		this.clientSslSocketFactory =
				SslSocketFactoryImpl.isSslEnabled(config) ?
						new SslSocketFactoryImpl(config) : null;
		this.configuration = config;
		this.logger = LoggerFactory.getLogger(Worker.class);
		this.otherWorker = null;
		this.routes = Routes.newInstance(config);
		this.ruleContext = null;
		this.rules = Rules.newInstance(config);
		this.selectedRoute = null;
		this.totalWorkerCount = workerCount;
	}
	
	protected Worker(final Worker other) {
		this.applicableRule = null;
		this.belowAllowLimitRules = null;
		this.clientFacingDtlsDatagramSocketFactory = null;
		this.clientSocket = null;
		this.clientSslSocketFactory = null;
		this.configuration = null;
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.otherWorker = other;
		this.routes = null;
		this.ruleContext = null;
		this.rules = null;
		this.selectedRoute = null;
		this.totalWorkerCount = null;
	}
	
	protected final void addBelowAllowLimitRule(final Rule belowAllowLimitRl) {
		if (this.otherWorker != null) {
			this.otherWorker.addBelowAllowLimitRule(belowAllowLimitRl);
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
	
	private boolean canAllowClientSocket() {
		FirewallAction firewallAction = 
				this.applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION);
		if (firewallAction == null) {
			return false;
		}
		LogAction firewallActionLogAction = 
				this.applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_LOG_ACTION);
		if (firewallAction.equals(FirewallAction.ALLOW)) {
			if (!this.canAllowClientSocketWithinLimit()) {
				return false;
			}
			if (firewallActionLogAction != null) {
				firewallActionLogAction.invoke(String.format(
						"Client allowed based on the following rule and "
						+ "context: rule: %s context: %s",
						this.applicableRule,
						this.ruleContext));
			}
		} else if (firewallAction.equals(FirewallAction.DENY)
				&& firewallActionLogAction != null) {
			firewallActionLogAction.invoke(String.format(
					"Client denied based on the following rule and context: "
					+ "rule: %s context: %s",
					this.applicableRule,
					this.ruleContext));				
		}
		return FirewallAction.ALLOW.equals(firewallAction);
	}
	
	private boolean canAllowClientSocketWithinLimit() {
		NonnegativeIntegerLimit firewallActionAllowLimit =
				this.applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT);
		LogAction firewallActionAllowLimitReachedLogAction =
				this.applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT_REACHED_LOG_ACTION);
		if (firewallActionAllowLimit != null) {
			if (!firewallActionAllowLimit.tryIncrementCurrentCount()) {
				if (firewallActionAllowLimitReachedLogAction != null) {
					firewallActionAllowLimitReachedLogAction.invoke(
							String.format(
									"Allowed limit has been reached based on "
									+ "the following rule and context: rule: "
									+ "%s context: %s",
									this.applicableRule,
									this.ruleContext));
				}
				return false;
			}
			this.addBelowAllowLimitRule(this.applicableRule);
		}		
		return true;
	}
	
	private boolean configureClientSocket(final Socket clientSock) {
		SocketSettings socketSettings = this.getClientSocketSettings();
		try {
			socketSettings.applyTo(clientSock);
		} catch (UnsupportedOperationException e) {
			this.logger.error(
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in setting the client socket"), 
					e);
			return false;			
		} catch (SocketException e) {
			this.logger.error(
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in setting the client socket"), 
					e);
			return false;
		}
		return true;
	}
	
	
	private void decrementAllCurrentAllowedCounts() {
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
	
	protected final Rule getApplicableRule() {
		if (this.otherWorker != null) {
			return this.otherWorker.getApplicableRule();
		}
		return this.applicableRule;
	}
	
	protected final Set<Rule> getBelowAllowLimitRules() {
		if (this.otherWorker != null) {
			return this.otherWorker.getBelowAllowLimitRules();
		}
		return Collections.unmodifiableSet(this.belowAllowLimitRules);
	}
	
	protected final DtlsDatagramSocketFactory getClientFacingDtlsDatagramSocketFactory() {
		if (this.otherWorker != null) {
			return this.otherWorker.getClientFacingDtlsDatagramSocketFactory();
		}
		return this.clientFacingDtlsDatagramSocketFactory;
	}
	
	private Routes getClientRoutes() {
		List<Route> rtes = this.applicableRule.getRuleResultValues(
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
	
	private LogAction getClientRouteSelectionLogAction() {
		LogAction routeSelectionLogAction = 
				this.applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.ROUTE_SELECTION_LOG_ACTION);
		if (routeSelectionLogAction != null) {
			return routeSelectionLogAction;
		}
		return this.configuration.getSettings().getLastValue(
				GeneralSettingSpecConstants.ROUTE_SELECTION_LOG_ACTION);
	}
	
	private SelectionStrategy getClientRouteSelectionStrategy() {
		SelectionStrategy routeSelectionStrategy =
				this.applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.ROUTE_SELECTION_STRATEGY);
		if (routeSelectionStrategy != null) {
			return routeSelectionStrategy;
		}
		return this.configuration.getSettings().getLastValue(
				GeneralSettingSpecConstants.ROUTE_SELECTION_STRATEGY);
	}
	
	protected final Socket getClientSocket() {
		if (this.otherWorker != null) {
			return this.otherWorker.getClientSocket();
		}
		return this.clientSocket;
	}
	
	private SocketSettings getClientSocketSettings() {
		List<SocketSetting<Object>> socketSettings = 
				this.applicableRule.getRuleResultValues(
						GeneralRuleResultSpecConstants.CLIENT_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.newInstance(
					socketSettings.stream().collect(Collectors.toList()));
		}
		socketSettings = this.applicableRule.getRuleResultValues(
				GeneralRuleResultSpecConstants.SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.newInstance(
					socketSettings.stream().collect(Collectors.toList()));
		}
		Settings settings = this.configuration.getSettings();
		SocketSettings socketSttngs = settings.getLastValue(
				GeneralSettingSpecConstants.CLIENT_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		socketSttngs = settings.getLastValue(
				GeneralSettingSpecConstants.SOCKET_SETTINGS);
		return socketSttngs;
	}
	
	protected final Configuration getConfiguration() {
		if (this.otherWorker != null) {
			return this.otherWorker.getConfiguration();
		}
		return this.configuration;
	}
	
	protected final Routes getRoutes() {
		if (this.otherWorker != null) {
			return this.otherWorker.getRoutes();
		}
		return this.routes;
	}
	
	protected final RuleContext getRuleContext() {
		if (this.otherWorker != null) {
			return this.otherWorker.getRuleContext();
		}
		return this.ruleContext;
	}
	
	protected final Rules getRules() {
		if (this.otherWorker != null) {
			return this.otherWorker.getRules();
		}
		return this.rules;
	}
	
	protected final Route getSelectedRoute() {
		if (this.otherWorker != null) {
			return this.otherWorker.getSelectedRoute();
		}
		return this.selectedRoute;
	}
	
	protected final Settings getSettings() {
		if (this.otherWorker != null) {
			return this.otherWorker.getSettings();
		}
		return this.configuration.getSettings();
	}
	
	protected final void logClientIoException(
			final String message, final IOException e) {
		if (ThrowableHelper.isOrHasInstanceOf(e, EOFException.class)) {
			this.logger.debug(message, e);
			return;
		}
		if (ThrowableHelper.isOrHasInstanceOf(e, SocketException.class)) {
			this.logger.debug(message, e);
			return;
		}
		if (ThrowableHelper.isOrHasInstanceOf(
				e, SocketTimeoutException.class)) {
			this.logger.debug(message, e);
			return;
		}
		if (ThrowableHelper.isOrHasInstanceOf(e, SocksException.class)) {
			this.logger.debug(message, e);
			return;
		}
		this.logger.error(message, e);		
	}
	
	private RuleContext newClientRuleContext() {
		RuleContext clientRuleContext = new RuleContext();
		Socket clientSocket = this.getClientSocket();
		clientRuleContext.putRuleArgValue(
				GeneralRuleArgSpecConstants.CLIENT_ADDRESS, 
				clientSocket.getInetAddress().getHostAddress());
		clientRuleContext.putRuleArgValue(
				GeneralRuleArgSpecConstants.SOCKS_SERVER_ADDRESS, 
				clientSocket.getLocalAddress().getHostAddress());
		return clientRuleContext;
	}
	
	public void run() {
		long startTime = System.currentTimeMillis();
		try {
			this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
					this, 
					"Started. Total Worker count: %s",
					this.totalWorkerCount.incrementAndGet()));
			this.ruleContext = this.newClientRuleContext();
			this.applicableRule = this.rules.firstAppliesTo(this.ruleContext);
			if (this.applicableRule == null) {
				this.logger.error(ObjectLogMessageHelper.objectLogMessage(
						this, 
						"No applicable rule found based on the following "
						+ "context: %s",
						this.ruleContext));
				return;
			}
			if (!this.canAllowClientSocket()) {
				return;
			}
			Socket clientSocket = this.getClientSocket();
			if (!this.configureClientSocket(clientSocket)) {
				return;
			}
			clientSocket = this.wrapClientSocket(clientSocket);
			if (clientSocket == null) {
				return;
			}
			this.setClientSocket(clientSocket);
			InputStream clientInputStream =	
					this.getClientSocket().getInputStream();
			UnsignedByte version = null;
			try {
				version = UnsignedByte.newInstanceFrom(clientInputStream);
			} catch (IOException e) {
				this.logClientIoException(
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in getting the SOCKS version from the "
								+ "client"), 
						e);
				return;
			}
			this.selectedRoute = this.selectClientRoute();
			if (version.byteValue() == Version.V5.byteValue()) {
				Socks5Worker socks5Worker = new Socks5Worker(this);
				socks5Worker.run();
			} else {
				this.logger.error(ObjectLogMessageHelper.objectLogMessage(
						this, 
						"Unknown SOCKS version: %s",
						version.intValue()));
			}
		} catch (Throwable t) {
			this.logger.error(
					ObjectLogMessageHelper.objectLogMessage(
							this, "Internal server error"), 
					t);
		} finally {
			this.decrementAllCurrentAllowedCounts();
			if (!this.getClientSocket().isClosed()) {
				try {
					this.getClientSocket().close();
				} catch (IOException e) {
					this.logger.error(
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error upon closing connection to the "
									+ "client"), 
							e);
				}
			}
			this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
					this, 
					"Finished in %s ms. Total Worker count: %s",
					System.currentTimeMillis() - startTime,
					this.totalWorkerCount.decrementAndGet()));
		}
	}

	private Route selectClientRoute() {
		SelectionStrategy rteSelectionStrategy = 
				this.getClientRouteSelectionStrategy();
		Routes rtes = this.getClientRoutes();
		LogAction rteSelectionLogAction = 
				this.getClientRouteSelectionLogAction();
		Route selectedRte = rteSelectionStrategy.selectFrom(
				rtes.toMap().values().stream().collect(Collectors.toList()));
		if (rteSelectionLogAction != null) {
			if (this.applicableRule.hasRuleResult(
					GeneralRuleResultSpecConstants.ROUTE_SELECTION_LOG_ACTION)
					|| this.applicableRule.hasRuleResult(
							GeneralRuleResultSpecConstants.ROUTE_SELECTION_STRATEGY)
					|| this.applicableRule.hasRuleResult(
							GeneralRuleResultSpecConstants.SELECTABLE_ROUTE_ID)) {
				rteSelectionLogAction.invoke(
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"Route '%s' selected based on the following "
								+ "rule and context: rule: %s context: %s",
								selectedRte.getId(),
								this.applicableRule,
								this.ruleContext));
			} else {
				rteSelectionLogAction.invoke(
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"Route '%s' selected",
								selectedRte.getId()));
			}
		}
		return selectedRte;
	}

	protected final void sendToClient(
			final byte[] b) throws IOException {
		if (this.otherWorker != null) {
			this.otherWorker.sendToClient(b);
			return;
		}
		OutputStream clientFacingOutputStream = 
				this.clientSocket.getOutputStream();
		clientFacingOutputStream.write(b);
		clientFacingOutputStream.flush();
	}
	
	protected final void setApplicableRule(final Rule applicableRl) {
		if (this.otherWorker != null) {
			this.otherWorker.setApplicableRule(applicableRl);
			return;
		}
		this.applicableRule = Objects.requireNonNull(applicableRl);
	}
	
	protected final void setClientSocket(final Socket clientSock) {
		if (this.otherWorker != null) {
			this.otherWorker.setClientSocket(clientSock);
			return;
		}
		this.clientSocket = clientSock;
	}
	
	protected final void setRuleContext(final RuleContext rlContext) {
		if (this.otherWorker != null) {
			this.otherWorker.setRuleContext(rlContext);
			return;
		}
		this.ruleContext = Objects.requireNonNull(rlContext);
	}
	
	protected final void setSelectedRoute(final Route selectedRte) {
		if (this.otherWorker != null) {
			this.otherWorker.setSelectedRoute(selectedRte);
			return;
		}
		this.selectedRoute = Objects.requireNonNull(selectedRte);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [getClientSocket()=")
			.append(this.getClientSocket())
			.append("]");
		return builder.toString();
	}
	
	private Socket wrapClientSocket(final Socket clientSock) {
		if (this.clientSslSocketFactory != null) {
			try {
				return this.clientSslSocketFactory.newSocket(
						clientSock, null, true);
			} catch (IOException e) {
				this.logger.error(
						ObjectLogMessageHelper.objectLogMessage(
								this, "Error in wrapping the client socket"), 
						e);
				return null;
			}
		}
		return clientSock;
	}
	
}
