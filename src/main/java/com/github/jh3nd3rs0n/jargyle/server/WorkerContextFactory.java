package com.github.jh3nd3rs0n.jargyle.server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.ClientFirewallRule;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.ClientFirewallRules;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.ClientRoutingRule;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.ClientRoutingRules;

final class WorkerContextFactory {
	
	private DtlsDatagramSocketFactory clientFacingDtlsDatagramSocketFactory;
	private SslSocketFactory clientFacingSslSocketFactory;
	private final Configuration configuration;
	private Configuration lastConfiguration;
	private Routes routes;
	private Selector<Route> routeSelector;
	
	public WorkerContextFactory(final Configuration config) {
		this.clientFacingDtlsDatagramSocketFactory = null;
		this.clientFacingSslSocketFactory = null;
		this.configuration = config;
		this.lastConfiguration = null;
		this.routes = null;
		this.routeSelector = null;		
	}
	
	private void checkIfClientFacingSocketAllowed(
			final Rule.Context context, final Configuration config) {
		Settings settings = config.getSettings();
		ClientFirewallRules clientFirewallRules = settings.getLastValue(
				GeneralSettingSpecConstants.CLIENT_FIREWALL_RULES);
		ClientFirewallRule clientFirewallRule = 
				clientFirewallRules.anyAppliesBasedOn(context);
		clientFirewallRule.applyBasedOn(context);
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
				this.routes = Routes.newInstance(config);
				SelectionStrategy routeSelectionStrategy = 
						config.getSettings().getLastValue(
								GeneralSettingSpecConstants.ROUTE_SELECTION_STRATEGY);
				this.routeSelector = routeSelectionStrategy.newSelector(
						new ArrayList<Route>(this.routes.toMap().values()));
				this.lastConfiguration = config;
			}
		}
		return config;
	}
	
	public WorkerContext newWorkerContext(
			final Socket clientFacingSocket) throws IOException {
		Configuration config = this.newConfiguration();
		Socket clientFacingSock = clientFacingSocket;
		String clientAddress = 
				clientFacingSock.getInetAddress().getHostAddress();
		String socksServerAddress =
				clientFacingSock.getLocalAddress().getHostAddress();
		this.checkIfClientFacingSocketAllowed(
				new ClientFirewallRule.Context(clientAddress, socksServerAddress), 
				config);
		this.configureClientFacingSocket(clientFacingSock, config);
		clientFacingSock = this.wrapClientFacingSocket(clientFacingSock);
		Route route = this.selectRoute(
				new ClientRoutingRule.Context(
						clientAddress, socksServerAddress, this.routes),
				config);
		if (route == null) {
			route = this.selectRoute(config);
		}
		return new WorkerContext(
				clientFacingSock, 
				config, 
				route,
				this.routes,
				this.clientFacingDtlsDatagramSocketFactory);
	}

	private Route selectRoute(final Configuration config) {
		Route route = this.routeSelector.select();
		Settings settings = config.getSettings();
		LogAction routeSelectionLogAction = settings.getLastValue(
				GeneralSettingSpecConstants.ROUTE_SELECTION_LOG_ACTION);
		if (routeSelectionLogAction != null) {
			routeSelectionLogAction.invoke(String.format(
					"Route '%s' selected", 
					route.getRouteId()));
		}
		return route;
	}
	
	private Route selectRoute(
			final Rule.Context context, final Configuration config) {
		Route route = null;
		Settings settings = config.getSettings();		
		ClientRoutingRules clientRoutingRules = settings.getLastValue(
				GeneralSettingSpecConstants.CLIENT_ROUTING_RULES);
		ClientRoutingRule clientRoutingRule = 
				clientRoutingRules.anyAppliesBasedOn(context);
		if (clientRoutingRule != null) {
			route = clientRoutingRule.selectRoute(context);
		}
		return route;		
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
