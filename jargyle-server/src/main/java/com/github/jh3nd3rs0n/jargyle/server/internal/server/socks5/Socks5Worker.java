package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.*;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Route;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Routes;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class Socks5Worker extends Worker {

	private InputStream clientInputStream;
	private final Logger logger;
	
	public Socks5Worker(final Worker worker) {
		super(worker);
		this.clientInputStream = null;
		this.logger = LoggerFactory.getLogger(this.getClass());
	}

	private boolean canAllowRequest() {
		Rule applicableRule = this.getApplicableRule();
		RuleContext ruleContext = this.getRuleContext();
		if (!this.hasRequestRuleCondition()) {
			return true;
		}
		FirewallAction firewallAction = applicableRule.getLastRuleResultValue(
				GeneralRuleResultSpecConstants.FIREWALL_ACTION);
		if (firewallAction == null) {
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
			return false;
		}
		LogAction firewallActionLogAction =	
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_LOG_ACTION);
		if (firewallAction.equals(FirewallAction.ALLOW)) {
			if (!this.canAllowRequestWithinLimit()) {
				return false;
			}
			if (firewallActionLogAction != null) {
				firewallActionLogAction.invoke(
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"SOCKS5 request allowed based on the following "
								+ "rule and context: rule: %s context: %s",
								applicableRule,
								ruleContext));
			}
		} else if (firewallAction.equals(FirewallAction.DENY)
				&& firewallActionLogAction != null) {
			firewallActionLogAction.invoke(
					ObjectLogMessageHelper.objectLogMessage(
							this,
							"SOCKS5 request denied based on the following "
							+ "rule and context: rule: %s context: %s",
							applicableRule,
							ruleContext));				
		}
		if (FirewallAction.ALLOW.equals(firewallAction)) {
			return true;
		}
		this.sendReply(
				Reply.newFailureInstance(ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
		return false;
	}
	
	private boolean canAllowRequestWithinLimit() {
		Rule applicableRule = this.getApplicableRule();
		RuleContext ruleContext = this.getRuleContext();
		NonNegativeIntegerLimit firewallActionAllowLimit =
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
									ruleContext));
				}
				this.sendReply(
						Reply.newFailureInstance(ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
				return false;
			}
			this.addBelowAllowLimitRule(applicableRule);
		}
		return true;
	}
	
	private MethodSubNegotiationResults doMethodSubnegotiation(
			final Method method) {
		MethodSubNegotiator methodSubnegotiator = 
				MethodSubNegotiator.getInstance(method);
		MethodSubNegotiationResults methodSubNegotiationResults = null;
		try {
			methodSubNegotiationResults = methodSubnegotiator.subNegotiate(
					this.getClientSocket(), this.getConfiguration());
		} catch (MethodSubNegotiationException e) {
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
		return methodSubNegotiationResults;		
	}
	
	private Address getDesiredDestinationAddressRedirect() {
		return this.getApplicableRule().getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_ADDRESS_REDIRECT);
	}
	
	private Port getDesiredDestinationPortRedirect() {
		return this.getApplicableRule().getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_PORT_REDIRECT);
	}
	
	private LogAction getDesiredDestinationRedirectLogAction() {
		return this.getApplicableRule().getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_REDIRECT_LOG_ACTION);
	}
	
	private Routes getRequestRoutes() {
		Routes routes = this.getRoutes();
		List<Route> rtes = this.getApplicableRule().getRuleResultValues(
				GeneralRuleResultSpecConstants.SELECTABLE_ROUTE_ID)
				.stream()
				.map(rteId -> routes.get(rteId))
				.filter(rte -> rte != null)
				.collect(Collectors.toList());
		if (rtes.size() > 0) {
			return Routes.of(rtes);
		}
		return routes;
	}
	
	private LogAction getRequestRouteSelectionLogAction() {
		LogAction routeSelectionLogAction = 
				this.getApplicableRule().getLastRuleResultValue(
						GeneralRuleResultSpecConstants.ROUTE_SELECTION_LOG_ACTION);
		if (routeSelectionLogAction != null) {
			return routeSelectionLogAction;
		}
		return this.getSettings().getLastValue(
				GeneralSettingSpecConstants.ROUTE_SELECTION_LOG_ACTION);
	}
	
	private SelectionStrategy getRequestRouteSelectionStrategy() {
		SelectionStrategy routeSelectionStrategy =
				this.getApplicableRule().getLastRuleResultValue(
						GeneralRuleResultSpecConstants.ROUTE_SELECTION_STRATEGY);
		if (routeSelectionStrategy != null) {
			return routeSelectionStrategy;
		}
		return this.getSettings().getLastValue(
				GeneralSettingSpecConstants.ROUTE_SELECTION_STRATEGY);
	}
	
	private boolean hasRequestRuleCondition() {
		Rule applicableRule = this.getApplicableRule();
		if (applicableRule.hasRuleCondition(
				Socks5RuleConditionSpecConstants.SOCKS5_METHOD)) {
			return true;
		}
		if (applicableRule.hasRuleCondition(
				Socks5RuleConditionSpecConstants.SOCKS5_USER)) {
			return true;
		}
		if (applicableRule.hasRuleCondition(
				Socks5RuleConditionSpecConstants.SOCKS5_REQUEST_COMMAND)) {
			return true;
		}
		if (applicableRule.hasRuleCondition(
				Socks5RuleConditionSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_ADDRESS)) {
			return true;
		}
		if (applicableRule.hasRuleCondition(
				Socks5RuleConditionSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_PORT)) {
			return true;
		}
		return false;
	}
	
	private Method negotiateMethod() {
		InputStream in = new SequenceInputStream(
				new ByteArrayInputStream(new byte[] { Version.V5.byteValue() }),
				this.clientInputStream);
		ClientMethodSelectionMessage cmsm = null;
		try {
			cmsm = ClientMethodSelectionMessage.newInstanceFrom(
					in);
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
		Methods methods = this.getSettings().getLastValue(
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
	
	private Request newRequest() {
		Request request = null;
		try {
			request = Request.newInstanceFrom(
					this.clientInputStream);
		} catch (AddressTypeNotSupportedException e) {
			this.logger.debug( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Unable to parse the SOCKS5 request"), 
					e);
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.ADDRESS_TYPE_NOT_SUPPORTED));
			return null;
		} catch (CommandNotSupportedException e) {
			this.logger.debug( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Unable to parse the SOCKS5 request"), 
					e);
			this.sendReply(Reply.newFailureInstance(ReplyCode.COMMAND_NOT_SUPPORTED));
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
				request.toString()));
		return request;
	}
	
	private RuleContext newRequestRuleContext(
			final MethodSubNegotiationResults methSubNegotiationResults, 
			final Request req) {
		RuleContext requestRuleContext = new RuleContext(
				this.getRuleContext());
		requestRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_METHOD, 
				methSubNegotiationResults.getMethod().toString());
		requestRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_USER, 
				methSubNegotiationResults.getUser());
		requestRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_REQUEST_COMMAND,
				req.getCommand().toString());
		requestRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_ADDRESS,
				req.getDesiredDestinationAddress().toString());
		requestRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_PORT,
				req.getDesiredDestinationPort());
		return requestRuleContext;
	}
	
	private Request redirectRequestIfSpecified(
			final Request req) {
		Request r = req;
		Address address = this.getDesiredDestinationAddressRedirect();
		if (address != null) {
			r = Request.newInstance(
					req.getCommand(), 
					address, 
					req.getDesiredDestinationPort());
		}
		Port port = this.getDesiredDestinationPortRedirect();
		if (port != null) {
			r = Request.newInstance(
					req.getCommand(), 
					req.getDesiredDestinationAddress(), 
					port);
		}
		LogAction logAction = this.getDesiredDestinationRedirectLogAction();
		if ((address != null || port != null) && logAction != null) {
			logAction.invoke(ObjectLogMessageHelper.objectLogMessage(
					this, 
					"Redirecting desired destination based on the following "
					+ "rule and context: rule: %s context: %s", 
					this.getApplicableRule(),
					this.getRuleContext()));
		}
		return r;
	}
	
	@Override
	public void run() {
		try {
			this.clientInputStream = this.getClientSocket().getInputStream();
			Method method = this.negotiateMethod();
			if (method == null) { return; } 
			MethodSubNegotiationResults methodSubNegotiationResults = 
					this.doMethodSubnegotiation(method);
			if (methodSubNegotiationResults == null) { return; }
			Socket socket = methodSubNegotiationResults.getSocket();
			this.clientInputStream = socket.getInputStream();
			this.setClientSocket(socket);
			Request request = this.newRequest();
			if (request == null) { return; }
			RuleContext ruleContext = this.newRequestRuleContext(
					methodSubNegotiationResults,
                    request);
			this.setRuleContext(ruleContext);
			Rule applicableRule = this.getRules().firstAppliesTo(
					this.getRuleContext());
			if (applicableRule == null) {
				this.logger.error(ObjectLogMessageHelper.objectLogMessage(
						this, 
						"No applicable rule found based on the following "
						+ "context: %s",
						this.getRuleContext()));				
				this.sendReply(
						Reply.newFailureInstance(ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
				return;
			}
			this.setApplicableRule(applicableRule);			
			if (!this.canAllowRequest()) {
				return;
			}
			request = this.redirectRequestIfSpecified(
                    request);
			Route selectedRoute = this.selectRequestRoute();
			this.setSelectedRoute(selectedRoute);
			CommandWorkerFactory commandWorkerFactory =
					CommandWorkerFactory.getInstance(
							request.getCommand());
			CommandWorker commandWorker = commandWorkerFactory.newCommandWorker(
					this, methodSubNegotiationResults, request);
			commandWorker.run();			
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	private Route selectRequestRoute() {
		Rule applicableRule = this.getApplicableRule();
		RuleContext ruleContext = this.getRuleContext();
		Route selectedRte = this.getSelectedRoute();
		if (!this.hasRequestRuleCondition()) {
			return selectedRte;
		}
		if (!applicableRule.hasRuleResult(
				GeneralRuleResultSpecConstants.ROUTE_SELECTION_LOG_ACTION)
				&& !applicableRule.hasRuleResult(
						GeneralRuleResultSpecConstants.ROUTE_SELECTION_STRATEGY)
				&& !applicableRule.hasRuleResult(
						GeneralRuleResultSpecConstants.SELECTABLE_ROUTE_ID)) {
			return selectedRte;
		}
		SelectionStrategy rteSelectionStrategy = 
				this.getRequestRouteSelectionStrategy();
		Routes rtes = this.getRequestRoutes();
		LogAction rteSelectionLogAction = 
				this.getRequestRouteSelectionLogAction();
		selectedRte = rteSelectionStrategy.selectFrom(
				rtes.toMap().values().stream().collect(Collectors.toList()));
		if (rteSelectionLogAction != null) {
			rteSelectionLogAction.invoke(
					ObjectLogMessageHelper.objectLogMessage(
							this,
							"Route '%s' selected based on the following rule "
							+ "and context: rule: %s context: %s",
							selectedRte.getId(),
							applicableRule,
							ruleContext));

		}
		return selectedRte;
	}
	
	protected final boolean sendReply(final Reply rep) {
		this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
				this, 
				"Sending %s",
				rep.toString()));		
		try {
			this.sendToClient(rep.toByteArray());
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
