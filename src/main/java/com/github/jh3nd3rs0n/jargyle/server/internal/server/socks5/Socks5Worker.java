package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.Socket;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.AddressAndPortHelper;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.FirewallAction;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleArgSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleResultSpecConstants;
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
import com.github.jh3nd3rs0n.jargyle.server.internal.server.ClientIOExceptionLoggingHelper;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Route;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Rules;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.AddressTypeNotSupportedException;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.ClientMethodSelectionMessage;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.CommandNotSupportedException;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.MethodSubnegotiationException;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.ServerMethodSelectionMessage;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Version;

public final class Socks5Worker {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			Socks5Worker.class);

	private InputStream clientInputStream;
	private Socket clientSocket;
	private final Configuration configuration;
	private final Rules rules;
	private final Settings settings;
	private Socks5WorkerContext socks5WorkerContext;
	
	public Socks5Worker(final Socks5WorkerContext context) {
		Socket clientSock = context.getClientSocket();
		Configuration config = context.getConfiguration();
		Rules rls = context.getRules();
		Settings sttngs = config.getSettings();
		this.clientInputStream = null;
		this.clientSocket = clientSock;
		this.configuration = config;
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
			this.socks5WorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
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
			this.socks5WorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return false;
		}
		LogAction firewallActionLogAction =
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_LOG_ACTION);
		Command command = Command.valueOfString(
				socks5RequestRuleContext.getRuleArgValue(
						Socks5RuleArgSpecConstants.SOCKS5_COMMAND));
		String desiredDestinationAddress = 
				socks5RequestRuleContext.getRuleArgValue(
						Socks5RuleArgSpecConstants.SOCKS5_DESIRED_DESTINATION_ADDRESS);
		Port desiredDestinationPort =
				socks5RequestRuleContext.getRuleArgValue(
						Socks5RuleArgSpecConstants.SOCKS5_DESIRED_DESTINATION_PORT);			
		if (firewallAction.equals(FirewallAction.ALLOW)) {
			if (!this.canAllowSocks5RequestWithinLimit(
					applicableRule, socks5RequestRuleContext)) {
				return false;
			}
			if (firewallActionLogAction != null) {
				firewallActionLogAction.invoke(
						LOGGER, 
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"'%s %s' allowed based on the following rule "
								+ "and context: rule: %s context: %s",
								command,
								AddressAndPortHelper.toString(
										desiredDestinationAddress,
										desiredDestinationPort.intValue()),
								applicableRule,
								socks5RequestRuleContext));
			}
		} else if (firewallAction.equals(FirewallAction.DENY)
				&& firewallActionLogAction != null) {
			firewallActionLogAction.invoke(
					LOGGER, 
					ObjectLogMessageHelper.objectLogMessage(
							this,
							"'%s %s' denied based on the following rule and "
							+ "context: rule: %s context: %s",
							command,
							AddressAndPortHelper.toString(
									desiredDestinationAddress,
									desiredDestinationPort.intValue()),
							applicableRule,
							socks5RequestRuleContext));				
		}
		if (FirewallAction.ALLOW.equals(firewallAction)) {
			return true;
		}
		Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
				Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
		this.socks5WorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
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
			if (firewallActionAllowLimit.hasBeenReached()) {
				if (firewallActionAllowLimitReachedLogAction != null) {
					firewallActionAllowLimitReachedLogAction.invoke(
							LOGGER, 
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
				this.socks5WorkerContext.sendSocks5Reply(
						this, socks5Rep, LOGGER);
				return false;
			}
			firewallActionAllowLimit.incrementCurrentCount();
			this.socks5WorkerContext.addBelowAllowLimitRule(applicableRule);
		}
		return true;
	}
	
	private boolean canApplyRule(final Rule applicableRule) {
		boolean hasMethodRuleCondition =
				applicableRule.hasRuleCondition(
						Socks5RuleConditionSpecConstants.SOCKS5_METHOD);
		boolean hasUserRuleCondition =
				applicableRule.hasRuleCondition(
						Socks5RuleConditionSpecConstants.SOCKS5_USER);
		boolean hasCommandRuleCondition =
				applicableRule.hasRuleCondition(
						Socks5RuleConditionSpecConstants.SOCKS5_COMMAND);
		boolean hasDesiredDestinationAddressRuleCondition =
				applicableRule.hasRuleCondition(
						Socks5RuleConditionSpecConstants.SOCKS5_DESIRED_DESTINATION_ADDRESS);
		boolean hasDesiredDestinationPortRuleCondition =
				applicableRule.hasRuleCondition(
						Socks5RuleConditionSpecConstants.SOCKS5_DESIRED_DESTINATION_PORT);
		return hasMethodRuleCondition
				|| hasUserRuleCondition
				|| hasCommandRuleCondition
				|| hasDesiredDestinationAddressRuleCondition
				|| hasDesiredDestinationPortRuleCondition;
	}
	
	private MethodSubnegotiationResults doMethodSubnegotiation(
			final Method method) {
		MethodSubnegotiator methodSubnegotiator = 
				MethodSubnegotiator.getInstance(method);
		MethodSubnegotiationResults methodSubnegotiationResults = null;
		try {
			methodSubnegotiationResults = methodSubnegotiator.subnegotiate(
					this.clientSocket, this.configuration);
		} catch (MethodSubnegotiationException e) {
			if (e.getCause() == null) {
				LOGGER.debug( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Unable to sub-negotiate with the client "
								+ "using method %s",
								method), 
						e);
				return null;
			}
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in sub-negotiating with the client using "
							+ "method %s",
							method), 
					e);
			return null;				
		} catch (IOException e) {
			ClientIOExceptionLoggingHelper.log(
					LOGGER,
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
	
	private Method negotiateMethod() {
		InputStream in = new SequenceInputStream(new ByteArrayInputStream(
				new byte[] { Version.V5.byteValue() }),
				this.clientInputStream);
		ClientMethodSelectionMessage cmsm = null;
		try {
			cmsm = ClientMethodSelectionMessage.newInstanceFrom(in);
		} catch (IOException e) {
			ClientIOExceptionLoggingHelper.log(
					LOGGER,
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in parsing the method selection message "
							+ "from the client"),
					e);
			return null;
		}
		LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
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
		LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
				this, 
				"Sending %s", 
				smsm.toString()));
		try {
			this.socks5WorkerContext.writeThenFlush(smsm.toByteArray());
		} catch (IOException e) {
			ClientIOExceptionLoggingHelper.log(
					LOGGER,
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
			LOGGER.debug( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Unable to parse the SOCKS5 request"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.ADDRESS_TYPE_NOT_SUPPORTED);
			this.socks5WorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return null;
		} catch (CommandNotSupportedException e) {
			LOGGER.debug( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Unable to parse the SOCKS5 request"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.COMMAND_NOT_SUPPORTED);
			this.socks5WorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return null;
		} catch (IOException e) {
			ClientIOExceptionLoggingHelper.log(
					LOGGER, 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in parsing the SOCKS5 request"),
					e);
			return null;
		}
		LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
				this, 
				"Received %s", 
				socks5Request.toString()));
		return socks5Request;
	}
	
	private RuleContext newSocks5RequestRuleContext(
			final Socket clientSock, 
			final MethodSubnegotiationResults methSubnegotiationResults, 
			final Socks5Request socks5Req) {
		RuleContext socks5RequestRuleContext = new RuleContext();
		socks5RequestRuleContext.putRuleArgValue(
				GeneralRuleArgSpecConstants.CLIENT_ADDRESS, 
				clientSock.getInetAddress().getHostAddress());
		socks5RequestRuleContext.putRuleArgValue(
				GeneralRuleArgSpecConstants.SOCKS_SERVER_ADDRESS, 
				clientSock.getLocalAddress().getHostAddress());
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
		Address address = applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_DESIRED_DESTINATION_ADDRESS_REDIRECT);
		if (address != null) {
			req = Socks5Request.newInstance(
					req.getCommand(), 
					address.toString(), 
					req.getDesiredDestinationPort());
		}
		Port port = applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_DESIRED_DESTINATION_PORT_REDIRECT);
		if (port != null) {
			req = Socks5Request.newInstance(
					req.getCommand(), 
					req.getDesiredDestinationAddress(), 
					port.intValue());
		}
		LogAction logAction = applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_DESIRED_DESTINATION_REDIRECT_LOG_ACTION);
		if ((address != null || port != null) && logAction != null) {
			logAction.invoke(LOGGER, ObjectLogMessageHelper.objectLogMessage(
					this, 
					"Redirecting desired destination %s to %s based on the "
					+ "following rule and context: rule: %s context: %s", 
					AddressAndPortHelper.toString(
							socks5Req.getDesiredDestinationAddress(), 
							socks5Req.getDesiredDestinationPort()),
					AddressAndPortHelper.toString(
							req.getDesiredDestinationAddress(), 
							req.getDesiredDestinationPort()),
					applicableRule,
					socks5RequestRuleContext));
		}
		return req;
	}
	
	public void run() throws IOException {
		this.clientInputStream = this.clientSocket.getInputStream();
		Method method = this.negotiateMethod();
		if (method == null) { return; } 
		MethodSubnegotiationResults methodSubnegotiationResults = 
				this.doMethodSubnegotiation(method);
		if (methodSubnegotiationResults == null) { return; }
		Socket socket = methodSubnegotiationResults.getSocket();
		this.clientInputStream = socket.getInputStream();
		this.clientSocket = socket;
		this.socks5WorkerContext.setClientSocket(this.clientSocket);
		Socks5Request socks5Request = this.newSocks5Request();
		if (socks5Request == null) { return; }
		RuleContext socks5RequestRuleContext = this.newSocks5RequestRuleContext(
				this.clientSocket,
				methodSubnegotiationResults,
				socks5Request);
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
		CommandWorkerContext commandWorkerContext =	new CommandWorkerContext(
				this.socks5WorkerContext,
				methodSubnegotiationResults,
				socks5Request,
				applicableRule,
				socks5RequestRuleContext);
		CommandWorker commandWorker = commandWorkerFactory.newCommandWorker(
				commandWorkerContext);
		commandWorker.run();			
	}
	
	private Route selectRoute(
			final Rule applicableRule, 
			final RuleContext socks5RequestRuleContext) {
		if (!this.canApplyRule(applicableRule)) {
			return this.socks5WorkerContext.getSelectedRoute();
		}
		List<String> routeIds = applicableRule.getRuleResultValues(
				GeneralRuleResultSpecConstants.ROUTE_ID);
		SelectionStrategy routeIdSelectionStrategy = 
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.ROUTE_ID_SELECTION_STRATEGY);
		LogAction routeIdSelectionLogAction = 
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.ROUTE_ID_SELECTION_LOG_ACTION);
		Route selectedRoute = null;
		if (routeIds.size() > 0 && routeIdSelectionStrategy != null) {
			String selectedRouteId = routeIdSelectionStrategy.selectFrom(
					routeIds);
			selectedRoute = this.socks5WorkerContext.getRoutes().get(
					selectedRouteId);
			if (selectedRoute != null && routeIdSelectionLogAction != null) {
				routeIdSelectionLogAction.invoke(
						LOGGER, 
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"Route '%s' selected based on the following "
								+ "rule and context: rule: %s context: %s",
								selectedRouteId,
								applicableRule,
								socks5RequestRuleContext));				
			}
		}
		if (selectedRoute != null) {
			return selectedRoute;
		}
		return this.socks5WorkerContext.getSelectedRoute();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [socks5WorkerContext=")
			.append(this.socks5WorkerContext)
			.append("]");
		return builder.toString();
	}
	
}
