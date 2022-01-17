package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.Socket;
import java.net.SocketException;

import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.internal.logging.LoggerHelper;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.FirewallRuleActionDenyException;
import com.github.jh3nd3rs0n.jargyle.server.FirewallRuleNotFoundException;
import com.github.jh3nd3rs0n.jargyle.server.Route;
import com.github.jh3nd3rs0n.jargyle.server.RouteNotFoundException;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.WorkerContext;
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
	
	private boolean canAllowSocks5Request(final Rule.Context context) {
		Socks5RequestFirewallRules socks5RequestFirewallRules = 
				this.settings.getLastValue(
						Socks5SettingSpecConstants.SOCKS5_SOCKS5_REQUEST_FIREWALL_RULES);
		Socks5RequestFirewallRule socks5RequestFirewallRule = null;
		try {
			socks5RequestFirewallRule = 
					socks5RequestFirewallRules.anyAppliesBasedOn(context);
		} catch (FirewallRuleNotFoundException e) {
			LOGGER.error(
					LoggerHelper.objectMessage(this, String.format(
							"Firewall rule not found for the following "
							+ "context: %s",
							context)),
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			this.socks5WorkerContext.sendSocks5Reply(this, socks5Rep);
			return false;			
		}
		try {
			socks5RequestFirewallRule.applyBasedOn(context);
		} catch (FirewallRuleActionDenyException e) {
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			this.socks5WorkerContext.sendSocks5Reply(this, socks5Rep);
			return false;
		}
		return true;
	}
	
	private MethodSubnegotiationResults doMethodSubnegotiation(
			final Method method) {
		MethodSubnegotiator methodSubnegotiator = 
				MethodSubnegotiator.valueOfMethod(method);
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
		} catch (SocketException e) {
			// socket closed
			return null;
		} catch (SSLException e) {
			Throwable cause = e.getCause();
			if (cause != null && cause instanceof SocketException) {
				// socket closed
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
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, 
							String.format(
									"Error in sub-negotiating with the client "
									+ "using method %s", 
									method)), 
					e);			
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
		} catch (SocketException e) {
			// socket closed
			return null;
		} catch (SSLException e) {
			Throwable cause = e.getCause();
			if (cause != null && cause instanceof SocketException) {
				// socket closed
				return null;
			}
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, 
							"Error in parsing the method selection message "
							+ "from the client"), 
					e);
			return null;
		} catch (IOException e) {
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, 
							"Error in parsing the method selection message "
							+ "from the client"), 
					e);
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
		} catch (SocketException e) {
			// socket closed
		} catch (SSLException e) {
			Throwable cause = e.getCause();
			if (cause != null && cause instanceof SocketException) {
				// socket closed
				return null;
			}
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, 
							"Error in writing the method selection message to "
							+ "the client"), 
					e);
			return null;
		} catch (IOException e) {
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, 
							"Error in writing the method selection message to "
							+ "the client"), 
					e);
		}
		return smsm.getMethod();
	}
	
	private Socks5Request newSocks5Request() {
		Socks5Request socks5Request = null;
		try {
			socks5Request = Socks5Request.newInstanceFrom(
					this.clientFacingInputStream);
		} catch (SocketException e) {
			// socket closed
			return null;
		} catch (SSLException e) {
			Throwable cause = e.getCause();
			if (cause != null && cause instanceof SocketException) {
				// socket closed
				return null;
			}
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, "Error in parsing the SOCKS5 request"), 
					e);
			return null;
		} catch (IOException e) {
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, "Error in parsing the SOCKS5 request"), 
					e);
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
		Route route = null;
		try {
			route = this.selectRoute(new Socks5RequestRoutingRule.Context(
					this.clientFacingSocket.getInetAddress().getHostAddress(),
					this.clientFacingSocket.getLocalAddress().getHostAddress(),
					methodSubnegotiationResults,
					socks5Request,
					this.socks5WorkerContext.getRoutes()));
		} catch (RouteNotFoundException e) {
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, 
							"Error in finding a particular SOCKS5 request route"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.socks5WorkerContext.sendSocks5Reply(this, socks5Rep);
			return;
		}
		if (route == null) {
			route = this.socks5WorkerContext.getRoute();
		}
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
	
	private Route selectRoute(final Rule.Context context) {
		Route route = null;
		Socks5RequestRoutingRules socks5RequestRoutingRules = 
				this.settings.getLastValue(
						Socks5SettingSpecConstants.SOCKS5_SOCKS5_REQUEST_ROUTING_RULES);
		Socks5RequestRoutingRule socks5RequestRoutingRule = 
				socks5RequestRoutingRules.anyAppliesBasedOn(context);
		if (socks5RequestRoutingRule != null) {
			route = socks5RequestRoutingRule.selectRoute(context);
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
