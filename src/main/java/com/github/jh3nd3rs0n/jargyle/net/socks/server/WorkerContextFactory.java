package com.github.jh3nd3rs0n.jargyle.net.socks.server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;
import com.github.jh3nd3rs0n.jargyle.net.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.util.Criteria;
import com.github.jh3nd3rs0n.jargyle.util.Criterion;

final class WorkerContextFactory {

	private DtlsDatagramSocketFactory clientFacingDtlsDatagramSocketFactory;
	private SslSocketFactory clientFacingSslSocketFactory;
	private final Configuration configuration;
	private Configuration lastConfiguration;	
	private NetObjectFactory netObjectFactory;
	
	public WorkerContextFactory(final Configuration config) {
		this.clientFacingDtlsDatagramSocketFactory = null;
		this.clientFacingSslSocketFactory = null;
		this.configuration = config;
		this.lastConfiguration = null;		
		this.netObjectFactory = null;
	}

	private void checkIfAllowed(
			final String clientAddress, final Configuration config) {
		Settings settings = config.getSettings();
		Criteria allowedClientAddressCriteria = settings.getLastValue(
				GeneralSettingSpecConstants.ALLOWED_CLIENT_ADDRESS_CRITERIA);
		Criterion criterion = allowedClientAddressCriteria.anyEvaluatesTrue(
				clientAddress);
		if (criterion == null) {
			throw new IllegalArgumentException(String.format(
					"client address %s not allowed",
					clientAddress));
		}
		Criteria blockedClientAddressCriteria = settings.getLastValue(
				GeneralSettingSpecConstants.BLOCKED_CLIENT_ADDRESS_CRITERIA);
		criterion = blockedClientAddressCriteria.anyEvaluatesTrue(
				clientAddress);
		if (criterion != null) {
			throw new IllegalArgumentException(String.format(
					"client address %s blocked based on the following "
							+ "criterion: %s",
							clientAddress,
							criterion));
		}
	}

	private void configureClientFacingSocket(
			final Socket clientFacingSock,
			final Configuration config) throws SocketException {
		Settings settings = config.getSettings();
		SocketSettings socketSettings = settings.getLastValue(
				GeneralSettingSpecConstants.CLIENT_FACING_SOCKET_SETTINGS);
		socketSettings.applyTo(clientFacingSock);
	}
	
	private Configuration newConfiguration() {
		Configuration config = ImmutableConfiguration.newInstance(
				this.configuration);
		synchronized (this) {
			if (!ConfigurationsHelper.equals(this.lastConfiguration, config)) {
				this.clientFacingDtlsDatagramSocketFactory =
						DtlsDatagramSocketFactoryImpl.isDtlsEnabled(config) ?
								new DtlsDatagramSocketFactoryImpl(config) : null;
				this.clientFacingSslSocketFactory =
						SslSocketFactoryImpl.isSslEnabled(config) ?
								new SslSocketFactoryImpl(config) : null;
				this.netObjectFactory = new NetObjectFactoryImpl(config);
				this.lastConfiguration = config;
			}
		}
		return config;
	}
	
	public WorkerContext newWorkerContext(
			final Socket clientFacingSocket) throws IOException {
		Configuration config = this.newConfiguration();
		Socket clientFacingSock = clientFacingSocket;
		this.checkIfAllowed(
				clientFacingSock.getInetAddress().getHostAddress(), config);
		this.configureClientFacingSocket(clientFacingSock, config);
		clientFacingSock = this.wrapClientFacingSocket(clientFacingSock);
		return new WorkerContext(
				clientFacingSock, 
				config, 
				this.netObjectFactory, 
				this.clientFacingDtlsDatagramSocketFactory);
	}
	
	private Socket wrapClientFacingSocket(
			final Socket clientFacingSock) throws IOException {
		Socket clientFacingSck = clientFacingSock;
		if (this.clientFacingSslSocketFactory != null) {
			clientFacingSck = this.clientFacingSslSocketFactory.newSocket(
					clientFacingSck, null, true);
		}		
		return clientFacingSck;
	}
	
}
