package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.internal.logging.LoggerHelper;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.RuleActionDenyException;
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
	
	private boolean canAllow(
			final String clientAddress,
			final MethodSubnegotiationResults methSubnegotiationResults,
			final Socks5Request socks5Req) {
		String user = methSubnegotiationResults.getUser();
		String possibleUser = (user != null) ? 
				String.format(" (%s)", user) : "";		
		Socks5RequestRules socks5RequestRules = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_SOCKS5_REQUEST_RULES);
		Socks5RequestRule socks5RequestRule = null;
		try {
			socks5RequestRule = socks5RequestRules.anyAppliesTo(
					clientAddress, methSubnegotiationResults, socks5Req);
		} catch (IllegalArgumentException e) {
			LOGGER.error(
					LoggerHelper.objectMessage(this, String.format(
							"Error regarding SOCKS5 request from %s%s. "
							+ "SOCKS5 request: %s",
							clientAddress,
							possibleUser,
							socks5Req)),
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Sending %s",
					socks5Rep.toString())));			
			try {
				this.socks5WorkerContext.writeThenFlush(
						socks5Rep.toByteArray());
			} catch (IOException ex) {
				LOGGER.error(
						LoggerHelper.objectMessage(
								this, "Error in writing SOCKS5 reply"), 
						e);
			}
			return false;			
		}
		try {
			socks5RequestRule.applyTo(
					clientAddress, methSubnegotiationResults, socks5Req);
		} catch (RuleActionDenyException e) {
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Sending %s",
					socks5Rep.toString())));			
			try {
				this.socks5WorkerContext.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException ex) {
				LOGGER.error( 
						LoggerHelper.objectMessage(
								this, "Error in writing SOCKS5 reply"), 
						e);
			}
			return false;
		}
		return true;
	}
	
	private MethodSubnegotiationResults negotiateMethod() throws IOException {
		InputStream in = new SequenceInputStream(new ByteArrayInputStream(
				new byte[] { Version.V5.byteValue() }),
				this.clientFacingInputStream);
		ClientMethodSelectionMessage cmsm = null;
		try {
			cmsm = ClientMethodSelectionMessage.newInstanceFrom(in); 
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
		this.socks5WorkerContext.writeThenFlush(smsm.toByteArray());
		MethodSubnegotiator methodSubnegotiator = 
				MethodSubnegotiator.valueOfMethod(method);
		MethodSubnegotiationResults methodSubnegotiationResults = null;
		try {
			methodSubnegotiationResults = methodSubnegotiator.subnegotiate(
					this.clientFacingSocket, this.configuration);
		} catch (MethodSubnegotiationException e) {
			LOGGER.debug( 
					LoggerHelper.objectMessage(
							this, "Unable to sub-negotiate with the client"), 
					e);
			return null;			
		} catch (IOException e) {
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, "Error in sub-negotiating with the client"), 
					e);
			return null;
		}
		return methodSubnegotiationResults;		
	}
	
	private Socks5Request newSocks5Request() throws IOException {
		Socks5Request socks5Request = null;
		try {
			socks5Request = Socks5Request.newInstanceFrom(
					this.clientFacingInputStream);
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
		MethodSubnegotiationResults methodSubnegotiationResults = 
				this.negotiateMethod();
		if (methodSubnegotiationResults == null) { return; }
		Socket socket = methodSubnegotiationResults.getSocket();
		this.clientFacingInputStream = socket.getInputStream();
		this.clientFacingSocket = socket;
		this.socks5WorkerContext = new Socks5WorkerContext(new WorkerContext(
				this.clientFacingSocket,
				this.configuration,
				this.socks5WorkerContext.getNetObjectFactory(),
				this.socks5WorkerContext.getClientFacingDtlsDatagramSocketFactory()));
		Socks5Request socks5Request = this.newSocks5Request();
		if (socks5Request == null) { return; }
		if (!this.canAllow(
				this.clientFacingSocket.getInetAddress().getHostAddress(),
				methodSubnegotiationResults,
				socks5Request)) {
			return;
		}
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
