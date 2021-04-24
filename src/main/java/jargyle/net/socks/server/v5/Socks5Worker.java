package jargyle.net.socks.server.v5;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jargyle.logging.LoggerHelper;
import jargyle.net.socks.server.Configuration;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.server.Settings;
import jargyle.net.socks.server.WorkerContext;
import jargyle.net.socks.transport.v5.AuthMethod;
import jargyle.net.socks.transport.v5.AuthMethods;
import jargyle.net.socks.transport.v5.ClientMethodSelectionMessage;
import jargyle.net.socks.transport.v5.Command;
import jargyle.net.socks.transport.v5.Method;
import jargyle.net.socks.transport.v5.Reply;
import jargyle.net.socks.transport.v5.ServerMethodSelectionMessage;
import jargyle.net.socks.transport.v5.Socks5Reply;
import jargyle.net.socks.transport.v5.Socks5Request;
import jargyle.net.socks.transport.v5.Version;

public final class Socks5Worker {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			Socks5Worker.class);

	private InputStream clientInputStream;
	private Socket clientSocket;
	private final Configuration configuration;
	private final Settings settings;
	private Socks5WorkerContext socks5WorkerContext;
	
	public Socks5Worker(final Socks5WorkerContext context) {
		Socket clientSock = context.getClientSocket();
		Configuration config = context.getConfiguration();
		Settings sttngs = config.getSettings();
		this.clientInputStream = null;
		this.clientSocket = clientSock;
		this.configuration = config;
		this.settings = sttngs;
		this.socks5WorkerContext = context;
	}
	
	private Socket authenticateUsing(final Method method) {
		Authenticator authenticator = null;
		try {
			authenticator = Authenticator.valueOfMethod(method);
		} catch (IllegalArgumentException e) {
			LOGGER.warn( 
					LoggerHelper.objectMessage(this, String.format(
							"Unhandled method: %s", 
							method)),
					e);
			return null;
		}
		Socket socket = null;
		try {
			socket = authenticator.authenticate(
					this.clientSocket, this.configuration);
		} catch (IOException e) {
			LOGGER.warn( 
					LoggerHelper.objectMessage(
							this, "Error in authenticating the client"), 
					e);
			return null;
		}
		return socket;
	}
	
	private boolean canAllowSocks5Request(
			final String clientAddress,	final Socks5Request socks5Req) {
		Socks5RequestCriteria allowedSocks5RequestCriteria = 
				this.settings.getLastValue(
						SettingSpec.SOCKS5_ALLOWED_SOCKS5_REQUEST_CRITERIA);
		Socks5RequestCriterion socks5RequestCriterion =
				allowedSocks5RequestCriteria.anyEvaluatesTrue(
						clientAddress, socks5Req);
		if (socks5RequestCriterion == null) {
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"SOCKS5 request from %s not allowed. SOCKS5 request: %s",
					clientAddress,
					socks5Req)));
			try {
				this.socks5WorkerContext.writeThenFlush(
						socks5Rep.toByteArray());
			} catch (IOException e) {
				LOGGER.warn(
						LoggerHelper.objectMessage(
								this, "Error in writing SOCKS5 reply"), 
						e);
			}
			return false;
		}
		Socks5RequestCriteria blockedSocks5RequestCriteria = 
				this.settings.getLastValue(
						SettingSpec.SOCKS5_BLOCKED_SOCKS5_REQUEST_CRITERIA);
		socks5RequestCriterion =
				blockedSocks5RequestCriteria.anyEvaluatesTrue(
						clientAddress, socks5Req);
		if (socks5RequestCriterion != null) {
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"SOCKS5 request from %s blocked based on the following "
					+ "criterion: %s. SOCKS5 request: %s",
					clientAddress,
					socks5RequestCriterion,
					socks5Req)));
			try {
				this.socks5WorkerContext.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, "Error in writing SOCKS5 reply"), 
						e);
			}
			return false;
		}
		return true;
	}
	
	private Socks5Request newSocks5Request() throws IOException {
		Socks5Request socks5Req = null;
		try {
			socks5Req = Socks5Request.newInstanceFrom(this.clientInputStream);
		} catch (IOException e) {
			LOGGER.warn( 
					LoggerHelper.objectMessage(
							this, "Error in parsing the SOCKS5 request"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Sending %s",
					socks5Rep.toString())));
			this.socks5WorkerContext.writeThenFlush(socks5Rep.toByteArray());
			return null;
		}
		LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
				"Received %s",
				socks5Req.toString())));
		return socks5Req;
	}
	
	public void run() {
		try {
			this.clientInputStream = this.clientSocket.getInputStream();
			Method method = this.selectMethod();
			if (method == null) { return; } 
			Socket socket = this.authenticateUsing(method);
			if (socket == null) { return; } 
			this.clientInputStream = socket.getInputStream();
			this.clientSocket = socket;
			this.socks5WorkerContext = new Socks5WorkerContext(new WorkerContext(
					this.clientSocket,
					this.configuration,
					this.socks5WorkerContext.getNetObjectFactory(),
					this.socks5WorkerContext.getClientDtlsDatagramSocketFactory()));
			Socks5Request socks5Req = this.newSocks5Request();
			if (socks5Req == null) { return; }
			if (!this.canAllowSocks5Request(
					this.clientSocket.getInetAddress().getHostAddress(), 
					socks5Req)) {
				return;
			}
			Command command = socks5Req.getCommand();
			CommandWorkerFactory commandWorkerFactory = 
					CommandWorkerFactory.valueOfCommand(command);
			CommandWorkerContext commandWorkerContext = new CommandWorkerContext(
					this.socks5WorkerContext, socks5Req);
			CommandWorker commandWorker = commandWorkerFactory.newCommandWorker(
					commandWorkerContext);
			commandWorker.run();
		} catch (Throwable t) {
			LOGGER.warn( 
					LoggerHelper.objectMessage(this, "Internal server error"), 
					t);
		} finally {
			if (!this.clientSocket.isClosed()) {
				try {
					this.clientSocket.close();
				} catch (IOException e) {
					LOGGER.warn( 
							LoggerHelper.objectMessage(
									this, 
									"Error upon closing connection to the "
									+ "client"), 
							e);
				}
			}
		}
	}
	
	private Method selectMethod() throws IOException {
		InputStream in = new SequenceInputStream(new ByteArrayInputStream(
				new byte[] { Version.V5.byteValue() }),
				this.clientInputStream);
		ClientMethodSelectionMessage cmsm = null;
		try {
			cmsm = ClientMethodSelectionMessage.newInstanceFrom(in); 
		} catch (IllegalArgumentException e) {
			LOGGER.warn( 
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
		AuthMethods authMethods = this.settings.getLastValue(
				SettingSpec.SOCKS5_AUTH_METHODS);
		for (AuthMethod authMethod : authMethods.toList()) {
			Method meth = authMethod.methodValue();
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
		return method;
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
