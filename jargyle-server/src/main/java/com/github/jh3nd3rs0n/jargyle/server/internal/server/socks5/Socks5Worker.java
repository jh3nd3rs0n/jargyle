package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
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
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleArgSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleConditionSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Route;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Routes;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Rules;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Worker;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.AddressTypeNotSupportedException;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.ClientMethodSelectionMessage;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.CommandNotSupportedException;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.MethodSubnegotiationException;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.ServerMethodSelectionMessage;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Version;

public class Socks5Worker extends Worker {

	private InputStream clientInputStream;
	private final Configuration configuration;
	private final Logger logger;
	private final Routes routes;
	private final Rules rules;
	private final Settings settings;
	private final Socks5WorkerContext socks5WorkerContext;
	
	public Socks5Worker(
			final Socket clientSocket, final Socks5WorkerContext context) {
		super(clientSocket);
		Configuration config = context.getConfiguration();
		Routes rtes = context.getRoutes();
		Rules rls = context.getRules();
		Settings sttngs = config.getSettings();
		this.clientInputStream = null;
		this.configuration = config;
		this.logger = LoggerFactory.getLogger(Socks5Worker.class);
		this.routes = rtes;
		this.rules = rls;
		this.settings = sttngs;
		this.socks5WorkerContext = context;
	}

	private boolean canAllowSocks5Request(
			final Rule applicableRule,
			final RuleContext socks5RequestRuleContext) {
		if (applicableRule == null) {
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			this.sendSocks5Reply(socks5Rep);
			return false;
		}
		if (!this.canApplyRule(applicableRule)) {
			return true;
		}
		FirewallAction firewallAction = applicableRule.getLastRuleResultValue(
				GeneralRuleResultSpecConstants.FIREWALL_ACTION);
		if (firewallAction == null) {
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			this.sendSocks5Reply(socks5Rep);
			return false;
		}
		LogAction firewallActionLogAction =
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_LOG_ACTION);
		if (firewallAction.equals(FirewallAction.ALLOW)) {
			if (!this.canAllowSocks5RequestWithinLimit(
					applicableRule, socks5RequestRuleContext)) {
				return false;
			}
			if (firewallActionLogAction != null) {
				firewallActionLogAction.invoke(
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"SOCKS5 request allowed based on the following "
								+ "rule and context: rule: %s context: %s",
								applicableRule,
								socks5RequestRuleContext));
			}
		} else if (firewallAction.equals(FirewallAction.DENY)
				&& firewallActionLogAction != null) {
			firewallActionLogAction.invoke(
					ObjectLogMessageHelper.objectLogMessage(
							this,
							"SOCKS5 request denied based on the following "
							+ "rule and context: rule: %s context: %s",
							applicableRule,
							socks5RequestRuleContext));				
		}
		if (FirewallAction.ALLOW.equals(firewallAction)) {
			return true;
		}
		Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
				Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
		this.sendSocks5Reply(socks5Rep);
		return false;
	}
	
	private boolean canAllowSocks5RequestWithinLimit(
			final Rule applicableRule,
			final RuleContext socks5RequestRuleContext) {
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
							ObjectLogMessageHelper.objectLogMessage(
									this,
									"Allowed limit has been reached based on "
									+ "the following rule and context: rule: "
									+ "%s context: %s",
									applicableRule,
									socks5RequestRuleContext));
				}
				Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
						Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
				this.sendSocks5Reply(socks5Rep);
				return false;
			}
			this.socks5WorkerContext.addBelowAllowLimitRule(applicableRule);
		}
		return true;
	}
	
	private boolean canApplyRule(final Rule applicableRule) {
		if (applicableRule.hasRuleCondition(
				Socks5RuleConditionSpecConstants.SOCKS5_METHOD)) {
			return true;
		}
		if (applicableRule.hasRuleCondition(
				Socks5RuleConditionSpecConstants.SOCKS5_USER)) {
			return true;
		}
		if (applicableRule.hasRuleCondition(
				Socks5RuleConditionSpecConstants.SOCKS5_COMMAND)) {
			return true;
		}
		if (applicableRule.hasRuleCondition(
				Socks5RuleConditionSpecConstants.SOCKS5_DESIRED_DESTINATION_ADDRESS)) {
			return true;
		}
		if (applicableRule.hasRuleCondition(
				Socks5RuleConditionSpecConstants.SOCKS5_DESIRED_DESTINATION_PORT)) {
			return true;
		}
		return false;
	}
	
	private MethodSubnegotiationResults doMethodSubnegotiation(
			final Method method) {
		MethodSubnegotiator methodSubnegotiator = 
				MethodSubnegotiator.getInstance(method);
		MethodSubnegotiationResults methodSubnegotiationResults = null;
		try {
			methodSubnegotiationResults = methodSubnegotiator.subnegotiate(
					this.getClientSocket(), this.configuration);
		} catch (MethodSubnegotiationException e) {
			if (e.getCause() == null) {
				this.logger.debug( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Unable to sub-negotiate with the client "
								+ "using method %s",
								method), 
						e);
				return null;
			}
			this.logger.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in sub-negotiating with the client using "
							+ "method %s",
							method), 
					e);
			return null;				
		} catch (IOException e) {
			this.logClientIoException(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in sub-negotiating with the client using "
							+ "method %s",
							method),
					e);
			return null;
		}
		return methodSubnegotiationResults;		
	}
	
	private Address getDesiredDestinationAddressRedirect(
			final Rule applicableRule) {
		return applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_DESIRED_DESTINATION_ADDRESS_REDIRECT);
	}
	
	private Port getDesiredDestinationPortRedirect(
			final Rule applicableRule) {
		return applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_DESIRED_DESTINATION_PORT_REDIRECT);
	}
	
	private LogAction getDesiredDestinationRedirectLogAction(
			final Rule applicableRule) {
		return applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_DESIRED_DESTINATION_REDIRECT_LOG_ACTION);
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
	
	private LogAction getRouteSelectionLogAction(final Rule applicableRule) {
		LogAction routeSelectionLogAction = 
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.ROUTE_SELECTION_LOG_ACTION);
		if (routeSelectionLogAction != null) {
			return routeSelectionLogAction;
		}
		return settings.getLastValue(
				GeneralSettingSpecConstants.ROUTE_SELECTION_LOG_ACTION);
	}
	
	private Method negotiateMethod() {
		InputStream in = new SequenceInputStream(new ByteArrayInputStream(
				new byte[] { Version.V5.byteValue() }),
				this.clientInputStream);
		ClientMethodSelectionMessage cmsm = null;
		try {
			cmsm = ClientMethodSelectionMessage.newInstanceFrom(in);
		} catch (IOException e) {
			this.logClientIoException(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in parsing the method selection message "
							+ "from the client"),
					e);
			return null;
		}
		this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
				this, 
				"Received %s", 
				cmsm.toString()));
		Method method = null;
		Methods methods = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_METHODS);
		for (Method meth : methods.toList()) {
			if (cmsm.getMethods().contains(meth)) {
				method = meth;
				break;
			}
		}
		if (method == null) {
			method = Method.NO_ACCEPTABLE_METHODS;
		}
		ServerMethodSelectionMessage smsm = 
				ServerMethodSelectionMessage.newInstance(method);
		this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
				this, 
				"Sending %s", 
				smsm.toString()));
		try {
			this.sendToClient(smsm.toByteArray());
		} catch (IOException e) {
			this.logClientIoException(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in writing the method selection message to "
							+ "the client"),
					e);
			return null;
		}
		return smsm.getMethod();
	}
	
	private Socks5Request newSocks5Request() {
		Socks5Request socks5Request = null;
		try {
			socks5Request = Socks5Request.newInstanceFrom(
					this.clientInputStream);
		} catch (AddressTypeNotSupportedException e) {
			this.logger.debug( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Unable to parse the SOCKS5 request"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.ADDRESS_TYPE_NOT_SUPPORTED);
			this.sendSocks5Reply(socks5Rep);
			return null;
		} catch (CommandNotSupportedException e) {
			this.logger.debug( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Unable to parse the SOCKS5 request"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.COMMAND_NOT_SUPPORTED);
			this.sendSocks5Reply(socks5Rep);
			return null;
		} catch (IOException e) {
			this.logClientIoException(
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in parsing the SOCKS5 request"),
					e);
			return null;
		}
		this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
				this, 
				"Received %s", 
				socks5Request.toString()));
		return socks5Request;
	}
	
	private RuleContext newSocks5RequestRuleContext(
			final MethodSubnegotiationResults methSubnegotiationResults, 
			final Socks5Request socks5Req) {
		RuleContext socks5RequestRuleContext = new RuleContext();
		Socket clientSocket = this.getClientSocket();
		socks5RequestRuleContext.putRuleArgValue(
				GeneralRuleArgSpecConstants.CLIENT_ADDRESS, 
				clientSocket.getInetAddress().getHostAddress());
		socks5RequestRuleContext.putRuleArgValue(
				GeneralRuleArgSpecConstants.SOCKS_SERVER_ADDRESS, 
				clientSocket.getLocalAddress().getHostAddress());
		socks5RequestRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_METHOD, 
				methSubnegotiationResults.getMethod().toString());
		socks5RequestRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_USER, 
				methSubnegotiationResults.getUser());
		socks5RequestRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_COMMAND, 
				socks5Req.getCommand().toString());
		socks5RequestRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_DESIRED_DESTINATION_ADDRESS, 
				socks5Req.getDesiredDestinationAddress());
		socks5RequestRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_DESIRED_DESTINATION_PORT, 
				Port.newInstance(socks5Req.getDesiredDestinationPort()));
		return socks5RequestRuleContext;
	}
	
	private Socks5Request redirectSocks5RequestIfSpecified(
			final Socks5Request socks5Req,
			final Rule applicableRule,
			final RuleContext socks5RequestRuleContext) {
		Socks5Request req = socks5Req;
		Address address = this.getDesiredDestinationAddressRedirect(
				applicableRule);
		if (address != null) {
			req = Socks5Request.newInstance(
					req.getCommand(), 
					address.toString(), 
					req.getDesiredDestinationPort());
		}
		Port port = this.getDesiredDestinationPortRedirect(applicableRule);
		if (port != null) {
			req = Socks5Request.newInstance(
					req.getCommand(), 
					req.getDesiredDestinationAddress(), 
					port.intValue());
		}
		LogAction logAction = this.getDesiredDestinationRedirectLogAction(
				applicableRule);
		if ((address != null || port != null) && logAction != null) {
			logAction.invoke(ObjectLogMessageHelper.objectLogMessage(
					this, 
					"Redirecting desired destination based on the following "
					+ "rule and context: rule: %s context: %s", 
					applicableRule,
					socks5RequestRuleContext));
		}
		return req;
	}
	
	@Override
	public void run() {
		try {
			this.clientInputStream = this.getClientSocket().getInputStream();
			Method method = this.negotiateMethod();
			if (method == null) { return; } 
			MethodSubnegotiationResults methodSubnegotiationResults = 
					this.doMethodSubnegotiation(method);
			if (methodSubnegotiationResults == null) { return; }
			Socket socket = methodSubnegotiationResults.getSocket();
			this.clientInputStream = socket.getInputStream();
			this.setClientSocket(socket);
			Socks5Request socks5Request = this.newSocks5Request();
			if (socks5Request == null) { return; }
			RuleContext socks5RequestRuleContext = 
					this.newSocks5RequestRuleContext(
							methodSubnegotiationResults, socks5Request);
			Rule applicableRule = this.rules.firstAppliesTo(
					socks5RequestRuleContext);
			if (!this.canAllowSocks5Request(
					applicableRule, socks5RequestRuleContext)) {
				return;
			}
			Route selectedRoute = this.selectRoute(
					applicableRule, socks5RequestRuleContext);
			this.socks5WorkerContext.setSelectedRoute(selectedRoute);
			socks5Request = this.redirectSocks5RequestIfSpecified(
					socks5Request, applicableRule, socks5RequestRuleContext);
			CommandWorkerFactory commandWorkerFactory =
					CommandWorkerFactory.getInstance(
							socks5Request.getCommand());
			CommandWorkerContext commandWorkerContext =	
					new CommandWorkerContext(
							this.socks5WorkerContext,
							methodSubnegotiationResults,
							socks5Request,
							applicableRule,
							socks5RequestRuleContext);
			CommandWorker commandWorker = commandWorkerFactory.newCommandWorker(
					this.getClientSocket(), commandWorkerContext);
			commandWorker.run();			
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	private Route selectRoute(
			final Rule applicableRule, 
			final RuleContext socks5RequestRuleContext) {
		Route selectedRte = this.socks5WorkerContext.getSelectedRoute();
		if (!this.canApplyRule(applicableRule)) {
			return selectedRte;
		}
		SelectionStrategy rteSelectionStrategy = 
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.ROUTE_SELECTION_STRATEGY);
		if (rteSelectionStrategy == null) {
			return selectedRte;
		}
		Routes rtes = this.getRoutes(applicableRule);
		selectedRte = rteSelectionStrategy.selectFrom(
				rtes.toMap().values().stream().collect(Collectors.toList()));
		LogAction rteSelectionLogAction =
				this.getRouteSelectionLogAction(applicableRule);
		if (rteSelectionLogAction != null) {
			rteSelectionLogAction.invoke(
					ObjectLogMessageHelper.objectLogMessage(
							this,
							"Route '%s' selected based on the following "
							+ "rule and context: rule: %s context: %s",
							selectedRte.getId(),
							applicableRule,
							socks5RequestRuleContext));
		}
		return selectedRte;
	}
	
	protected final boolean sendSocks5Reply(final Socks5Reply socks5Rep) {
		this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
				this, 
				"Sending %s",
				socks5Rep.toString()));		
		try {
			this.sendToClient(socks5Rep.toByteArray());
		} catch (IOException e) {
			this.logClientIoException(
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in writing SOCKS5 reply"), 
					e);
			return false;
		}
		return true;
	}
	
}
