package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.internal.logging.LoggerHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.IOExceptionHandler;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Route;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.WorkerContext;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.FirewallRule;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.FirewallRuleAction;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.RoutingRule;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.Socks5RequestFirewallRule;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.Socks5RequestFirewallRules;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.Socks5RequestRoutingRule;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.Socks5RequestRoutingRules;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.ClientMethodSelectionMessage;
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

	private InputStream clientFacingInputStream;
	private Socket clientFacingSocket;
	private final Configuration configuration;
	private final Settings settings;
	private Socks5WorkerContext socks5WorkerContext;
	
	public Socks5Worker(final Socks5WorkerContext context) {
		Socket clientFacingSock = context.getClientFacingSocket();
		Configuration config = context.getConfiguration();
		Settings sttngs = config.getSettings();
		this.clientFacingInputStream = null;
		this.clientFacingSocket = clientFacingSock;
		this.configuration = config;
		this.settings = sttngs;
		this.socks5WorkerContext = context;
	}
	
	private boolean canAllowSocks5Request(final FirewallRule.Context context) {
		Socks5RequestFirewallRules socks5RequestFirewallRules = 
				this.settings.getLastValue(
						Socks5SettingSpecConstants.SOCKS5_SOCKS5_REQUEST_FIREWALL_RULES);
		socks5RequestFirewallRules.applyTo(context);
		if (FirewallRuleAction.ALLOW.equals(context.getFirewallRuleAction())) {
			return true;
		}
		Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
				Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
		this.socks5WorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
		return false;
	}
	
	private MethodSubnegotiationResults doMethodSubnegotiation(
			final Method method) {
		MethodSubnegotiator methodSubnegotiator = 
				MethodSubnegotiator.getInstance(method);
		MethodSubnegotiationResults methodSubnegotiationResults = null;
		try {
			methodSubnegotiationResults = methodSubnegotiator.subnegotiate(
					this.clientFacingSocket, this.configuration);
		} catch (MethodSubnegotiationException e) {
			if (e.getCause() == null) {
				LOGGER.debug( 
						LoggerHelper.objectMessage(
								this, 
								String.format(
										"Unable to sub-negotiate with the "
										+ "client using method %s", 
										method)), 
						e);
				return null;
			}
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, 
							String.format(
									"Error in sub-negotiating with the client "
									+ "using method %s", 
									method)), 
					e);
			return null;				
		} catch (IOException e) {
			IOExceptionHandler.INSTANCE.handle(
					e,
					LOGGER,
					LoggerHelper.objectMessage(
							this, 
							String.format(
									"Error in sub-negotiating with the client "
									+ "using method %s", 
									method)));
			return null;
		}
		return methodSubnegotiationResults;		
	}
	
	private Method negotiateMethod() {
		InputStream in = new SequenceInputStream(new ByteArrayInputStream(
				new byte[] { Version.V5.byteValue() }),
				this.clientFacingInputStream);
		ClientMethodSelectionMessage cmsm = null;
		try {
			cmsm = ClientMethodSelectionMessage.newInstanceFrom(in);
		} catch (IOException e) {
			IOExceptionHandler.INSTANCE.handle(
					e,
					LOGGER,
					LoggerHelper.objectMessage(
							this, 
							"Error in parsing the method selection message "
							+ "from the client"));
			return null;
		}
		LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
				"Received %s", 
				cmsm.toString())));
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
		LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
				"Sending %s", 
				smsm.toString())));
		try {
			this.socks5WorkerContext.writeThenFlush(smsm.toByteArray());
		} catch (IOException e) {
			IOExceptionHandler.INSTANCE.handle(
					e,
					LOGGER,
					LoggerHelper.objectMessage(
							this, 
							"Error in writing the method selection message to "
							+ "the client"));
			return null;
		}
		return smsm.getMethod();
	}
	
	private Socks5Request newSocks5Request() {
		Socks5Request socks5Request = null;
		try {
			socks5Request = Socks5Request.newInstanceFrom(
					this.clientFacingInputStream);
		} catch (IOException e) {
			IOExceptionHandler.INSTANCE.handle(
					e, 
					LOGGER,
					LoggerHelper.objectMessage(
							this, "Error in parsing the SOCKS5 request"));
			return null;
		}
		LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
				"Received %s",
				socks5Request.toString())));
		return socks5Request;
	}
	
	public void run() throws IOException {
		this.clientFacingInputStream = this.clientFacingSocket.getInputStream();
		Method method = this.negotiateMethod();
		if (method == null) { return; } 
		MethodSubnegotiationResults methodSubnegotiationResults = 
				this.doMethodSubnegotiation(method);
		if (methodSubnegotiationResults == null) { return; }
		Socket socket = methodSubnegotiationResults.getSocket();
		this.clientFacingInputStream = socket.getInputStream();
		this.clientFacingSocket = socket;
		this.socks5WorkerContext = new Socks5WorkerContext(new WorkerContext(
				this.clientFacingSocket,
				this.socks5WorkerContext.getConfiguration(),
				this.socks5WorkerContext.getRoute(),
				this.socks5WorkerContext.getRoutes(),
				this.socks5WorkerContext.getClientFacingDtlsDatagramSocketFactory()));
		Socks5Request socks5Request = this.newSocks5Request();
		if (socks5Request == null) { return; }
		if (!this.canAllowSocks5Request(new Socks5RequestFirewallRule.Context(
				this.clientFacingSocket.getInetAddress().getHostAddress(),
				this.clientFacingSocket.getLocalAddress().getHostAddress(),
				methodSubnegotiationResults,
				socks5Request))) {
			return;
		}
		Route route = this.selectRoute(new Socks5RequestRoutingRule.Context(
				this.clientFacingSocket.getInetAddress().getHostAddress(),
				this.clientFacingSocket.getLocalAddress().getHostAddress(),
				methodSubnegotiationResults,
				socks5Request,
				this.socks5WorkerContext.getRoutes()));
		if (route == null) { return; }
		this.socks5WorkerContext = new Socks5WorkerContext(new WorkerContext(
				this.socks5WorkerContext.getClientFacingSocket(),
				this.socks5WorkerContext.getConfiguration(),
				route,
				this.socks5WorkerContext.getRoutes(),
				this.socks5WorkerContext.getClientFacingDtlsDatagramSocketFactory()));		
		Socks5RequestWorkerContext socks5RequestWorkerContext = 
				new Socks5RequestWorkerContext(
						this.socks5WorkerContext, 
						methodSubnegotiationResults, 
						socks5Request);
		Socks5RequestWorkerFactory socks5RequestWorkerFactory =
				this.settings.getLastValue(
						Socks5SettingSpecConstants.SOCKS5_SOCKS5_REQUEST_WORKER_FACTORY);
		if (socks5RequestWorkerFactory == null) {
			socks5RequestWorkerFactory =
					Socks5RequestWorkerFactory.newInstance(); 
		}
		Socks5RequestWorker socks5RequestWorker = 
				socks5RequestWorkerFactory.newSocks5RequestWorker(
						socks5RequestWorkerContext);
		socks5RequestWorker.run();
	}
	
	private Route selectRoute(final RoutingRule.Context context) {
		Socks5RequestRoutingRules socks5RequestRoutingRules = 
				this.settings.getLastValue(
						Socks5SettingSpecConstants.SOCKS5_SOCKS5_REQUEST_ROUTING_RULES);
		socks5RequestRoutingRules.applyTo(context);
		Route route = context.getRoute();
		if (route == null) {
			route = this.socks5WorkerContext.getRoute();
		}
		return route;
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
