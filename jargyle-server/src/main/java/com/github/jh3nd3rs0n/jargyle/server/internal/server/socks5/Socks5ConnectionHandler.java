package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.*;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Route;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Routes;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.ServerEventLogger;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class Socks5ConnectionHandler {

	private InputStream clientInputStream;
	private final ServerEventLogger serverEventLogger;
    private final Socks5ConnectionHandlerContext socks5ConnectionHandlerContext;
	
	public Socks5ConnectionHandler(
            final Socks5ConnectionHandlerContext handlerContext) {
		this.clientInputStream = null;
		this.serverEventLogger = handlerContext.getServerEventLogger();
        this.socks5ConnectionHandlerContext = handlerContext;
        this.socks5ConnectionHandlerContext.setLogMessageAuthor(this);
	}

	private boolean canAllowRequest() {
		Rule applicableRule =
                this.socks5ConnectionHandlerContext.getApplicableRule();
		RuleContext ruleContext =
                this.socks5ConnectionHandlerContext.getRuleContext();
		if (!this.hasRequestRuleCondition()) {
			return true;
		}
		FirewallAction firewallAction = applicableRule.getLastRuleActionValue(
				GeneralRuleActionSpecConstants.FIREWALL_ACTION);
		if (firewallAction == null) {
			this.socks5ConnectionHandlerContext.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
			return false;
		}
		LogAction firewallActionLogAction =	
				applicableRule.getLastRuleActionValue(
						GeneralRuleActionSpecConstants.FIREWALL_ACTION_LOG_ACTION);
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
		this.socks5ConnectionHandlerContext.sendReply(
				Reply.newFailureInstance(
                        ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
		return false;
	}
	
	private boolean canAllowRequestWithinLimit() {
		Rule applicableRule =
                this.socks5ConnectionHandlerContext.getApplicableRule();
		RuleContext ruleContext =
                this.socks5ConnectionHandlerContext.getRuleContext();
		NonNegativeIntegerLimit firewallActionAllowLimit =
				applicableRule.getLastRuleActionValue(
						GeneralRuleActionSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT);
		LogAction firewallActionAllowLimitReachedLogAction =
				applicableRule.getLastRuleActionValue(
						GeneralRuleActionSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT_REACHED_LOG_ACTION);
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
				this.socks5ConnectionHandlerContext.sendReply(
                        Reply.newFailureInstance(
                                ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
				return false;
			}
			this.socks5ConnectionHandlerContext.addBelowAllowLimitRule(
                    applicableRule);
		}
		return true;
	}
	
	private MethodSubNegotiationResults doMethodSubNegotiation(
			final Method method) {
		MethodSubNegotiator methodSubNegotiator =
				MethodSubNegotiator.getInstance(method);
		MethodSubNegotiationResults methodSubNegotiationResults;
		try {
			methodSubNegotiationResults = methodSubNegotiator.subNegotiate(
					this.socks5ConnectionHandlerContext.getClientSocket(),
                    this.socks5ConnectionHandlerContext.getConfiguration());
		} catch (MethodSubNegotiationException e) {
			if (e.getCause() == null) {
				this.serverEventLogger.debug(
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Unable to sub-negotiate with the client "
								+ "using method %s",
								method), 
						e);
				return null;
			}
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in sub-negotiating with the client using "
							+ "method %s",
							method), 
					e);
			return null;				
		} catch (IOException e) {
			this.serverEventLogger.logClientIoException(
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
		return this.socks5ConnectionHandlerContext.getApplicableRule().getLastRuleActionValue(
				Socks5RuleActionSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_ADDRESS_REDIRECT);
	}
	
	private Port getDesiredDestinationPortRedirect() {
		return this.socks5ConnectionHandlerContext.getApplicableRule().getLastRuleActionValue(
				Socks5RuleActionSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_PORT_REDIRECT);
	}
	
	private LogAction getDesiredDestinationRedirectLogAction() {
		return this.socks5ConnectionHandlerContext.getApplicableRule().getLastRuleActionValue(
				Socks5RuleActionSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_REDIRECT_LOG_ACTION);
	}
	
	private Routes getRequestRoutes() {
		Routes routes = this.socks5ConnectionHandlerContext.getRoutes();
		List<Route> rtes = this.socks5ConnectionHandlerContext.getApplicableRule().getRuleActionValues(
				GeneralRuleActionSpecConstants.SELECTABLE_ROUTE_ID)
				.stream()
				.map(routes::get)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		if (!rtes.isEmpty()) {
			return Routes.of(rtes);
		}
		return routes;
	}
	
	private LogAction getRequestRouteSelectionLogAction() {
		LogAction routeSelectionLogAction = 
				this.socks5ConnectionHandlerContext.getApplicableRule().getLastRuleActionValue(
						GeneralRuleActionSpecConstants.ROUTE_SELECTION_LOG_ACTION);
		if (routeSelectionLogAction != null) {
			return routeSelectionLogAction;
		}
		return this.socks5ConnectionHandlerContext.getSettings().getLastValue(
				GeneralSettingSpecConstants.ROUTE_SELECTION_LOG_ACTION);
	}
	
	private SelectionStrategy getRequestRouteSelectionStrategy() {
		SelectionStrategy routeSelectionStrategy =
				this.socks5ConnectionHandlerContext.getApplicableRule().getLastRuleActionValue(
						GeneralRuleActionSpecConstants.ROUTE_SELECTION_STRATEGY);
		if (routeSelectionStrategy != null) {
			return routeSelectionStrategy;
		}
		return this.socks5ConnectionHandlerContext.getSettings().getLastValue(
				GeneralSettingSpecConstants.ROUTE_SELECTION_STRATEGY);
	}

    public void handleSocks5Connection() throws IOException {
        this.clientInputStream =
                this.socks5ConnectionHandlerContext.getClientSocket().getInputStream();
        Method method = this.negotiateMethod();
        if (method == null) { return; }
        MethodSubNegotiationResults methodSubNegotiationResults =
                this.doMethodSubNegotiation(method);
        if (methodSubNegotiationResults == null) { return; }
        Socket socket = methodSubNegotiationResults.getSocket();
        this.clientInputStream = socket.getInputStream();
        this.socks5ConnectionHandlerContext.setClientSocket(socket);
        Request request = this.newRequest();
        if (request == null) { return; }
        RuleContext ruleContext = this.newRequestRuleContext(
                methodSubNegotiationResults,
                request);
        this.socks5ConnectionHandlerContext.setRuleContext(ruleContext);
        Rule applicableRule =
                this.socks5ConnectionHandlerContext.getRules().firstAppliesTo(
                        this.socks5ConnectionHandlerContext.getRuleContext());
        if (applicableRule == null) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "No applicable rule found based on the following "
                                    + "context: %s",
                            this.socks5ConnectionHandlerContext.getRuleContext()));
            this.socks5ConnectionHandlerContext.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
            return;
        }
        this.socks5ConnectionHandlerContext.setApplicableRule(applicableRule);
        if (!this.canAllowRequest()) {
            return;
        }
        request = this.redirectRequestIfSpecified(request);
        Route selectedRoute = this.selectRequestRoute();
        this.socks5ConnectionHandlerContext.setSelectedRoute(selectedRoute);
        RequestHandlerFactory requestHandlerFactory =
                RequestHandlerFactory.getInstance(request.getCommand());
        RequestHandler requestHandler = requestHandlerFactory.newRequestHandler(
                this.socks5ConnectionHandlerContext,
                methodSubNegotiationResults,
                request);
        requestHandler.handleRequest();
    }

	private boolean hasRequestRuleCondition() {
		Rule applicableRule = this.socks5ConnectionHandlerContext.getApplicableRule();
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
        return applicableRule.hasRuleCondition(
                Socks5RuleConditionSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_PORT);
    }
	
	private Method negotiateMethod() {
		InputStream in = new SequenceInputStream(
				new ByteArrayInputStream(new byte[] { Version.V5.byteValue() }),
				this.clientInputStream);
		ClientMethodSelectionMessage cmsm;
		try {
			cmsm = ClientMethodSelectionMessage.newInstanceFrom(
					in);
		} catch (IOException e) {
			this.serverEventLogger.logClientIoException(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in parsing the method selection message "
							+ "from the client"),
					e);
			return null;
		}
		this.serverEventLogger.debug(
                ObjectLogMessageHelper.objectLogMessage(
                        this,
                        "Received %s",
                        cmsm.toString()));
		Method method = null;
		Methods methods = this.socks5ConnectionHandlerContext.getSettings().getLastValue(
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
		this.serverEventLogger.debug(ObjectLogMessageHelper.objectLogMessage(
				this, 
				"Sending %s", 
				smsm.toString()));
		try {
			this.socks5ConnectionHandlerContext.sendToClient(
                    smsm.toByteArray());
		} catch (IOException e) {
			this.serverEventLogger.logClientIoException(
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
		Request request;
		try {
			request = Request.newInstanceFrom(
					this.clientInputStream);
		} catch (AddressTypeNotSupportedException e) {
			this.serverEventLogger.debug(
					ObjectLogMessageHelper.objectLogMessage(
							this, "Unable to parse the SOCKS5 request"), 
					e);
			this.socks5ConnectionHandlerContext.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.ADDRESS_TYPE_NOT_SUPPORTED));
			return null;
		} catch (CommandNotSupportedException e) {
			this.serverEventLogger.debug(
					ObjectLogMessageHelper.objectLogMessage(
							this, "Unable to parse the SOCKS5 request"), 
					e);
			this.socks5ConnectionHandlerContext.sendReply(
                    Reply.newFailureInstance(ReplyCode.COMMAND_NOT_SUPPORTED));
			return null;
		} catch (IOException e) {
			this.serverEventLogger.logClientIoException(
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in parsing the SOCKS5 request"),
					e);
			return null;
		}
		this.serverEventLogger.debug(ObjectLogMessageHelper.objectLogMessage(
				this, 
				"Received %s", 
				request.toString()));
		return request;
	}
	
	private RuleContext newRequestRuleContext(
			final MethodSubNegotiationResults methSubNegotiationResults, 
			final Request req) {
		RuleContext requestRuleContext = new RuleContext(
				this.socks5ConnectionHandlerContext.getRuleContext());
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
					this.socks5ConnectionHandlerContext.getApplicableRule(),
					this.socks5ConnectionHandlerContext.getRuleContext()));
		}
		return r;
	}

	private Route selectRequestRoute() {
		Rule applicableRule =
                this.socks5ConnectionHandlerContext.getApplicableRule();
		RuleContext ruleContext =
                this.socks5ConnectionHandlerContext.getRuleContext();
		Route selectedRte =
                this.socks5ConnectionHandlerContext.getSelectedRoute();
		if (!this.hasRequestRuleCondition()) {
			return selectedRte;
		}
		if (!applicableRule.hasRuleAction(
				GeneralRuleActionSpecConstants.ROUTE_SELECTION_LOG_ACTION)
				&& !applicableRule.hasRuleAction(
						GeneralRuleActionSpecConstants.ROUTE_SELECTION_STRATEGY)
				&& !applicableRule.hasRuleAction(
						GeneralRuleActionSpecConstants.SELECTABLE_ROUTE_ID)) {
			return selectedRte;
		}
		SelectionStrategy rteSelectionStrategy = 
				this.getRequestRouteSelectionStrategy();
		Routes rtes = this.getRequestRoutes();
		LogAction rteSelectionLogAction = 
				this.getRequestRouteSelectionLogAction();
		selectedRte = rteSelectionStrategy.selectFrom(
                new ArrayList<>(rtes.toMap().values()));
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

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [socks5ConnectionHandlerContext=" +
                this.socks5ConnectionHandlerContext +
                "]";
    }

}
