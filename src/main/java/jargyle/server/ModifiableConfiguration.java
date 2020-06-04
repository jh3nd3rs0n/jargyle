package jargyle.server;

import java.util.ArrayList;
import java.util.List;

import jargyle.client.socks5.UsernamePassword;
import jargyle.server.socks5.Socks5RequestCriteria;
import jargyle.server.socks5.Socks5RequestCriterion;
import jargyle.server.socks5.UsernamePasswordAuthenticator;

final class ModifiableConfiguration implements Configuration {
	
	private final List<Criterion> allowedClientAddressCriteria;
	private final List<Criterion> allowedSocks5IncomingTcpAddressCriteria;
	private final List<Criterion> allowedSocks5IncomingUdpAddressCriteria;
	private final List<Socks5RequestCriterion> allowedSocks5RequestCriteria;
	private final List<Criterion> blockedClientAddressCriteria;
	private final List<Criterion> blockedSocks5IncomingTcpAddressCriteria;
	private final List<Criterion> blockedSocks5IncomingUdpAddressCriteria;		
	private final List<Socks5RequestCriterion> blockedSocks5RequestCriteria;
	private UsernamePassword externalClientSocks5UsernamePassword;
	private final List<Setting> settings;
	private UsernamePasswordAuthenticator socks5UsernamePasswordAuthenticator;
	
	public ModifiableConfiguration() {
		this.allowedClientAddressCriteria = new ArrayList<Criterion>();
		this.allowedSocks5IncomingTcpAddressCriteria = 
				new ArrayList<Criterion>();
		this.allowedSocks5IncomingUdpAddressCriteria = 
				new ArrayList<Criterion>();
		this.allowedSocks5RequestCriteria = 
				new ArrayList<Socks5RequestCriterion>();
		this.blockedClientAddressCriteria = new ArrayList<Criterion>();
		this.blockedSocks5IncomingTcpAddressCriteria = 
				new ArrayList<Criterion>();
		this.blockedSocks5IncomingUdpAddressCriteria = 
				new ArrayList<Criterion>();
		this.blockedSocks5RequestCriteria = 
				new ArrayList<Socks5RequestCriterion>();
		this.externalClientSocks5UsernamePassword = null;
		this.settings = new ArrayList<Setting>();
		this.socks5UsernamePasswordAuthenticator = null;
	}
	
	public void add(final Configuration configuration) {
		this.addAllowedClientAddressCriteria(
				configuration.getAllowedClientAddressCriteria());
		this.addAllowedSocks5IncomingTcpAddressCriteria(
				configuration.getAllowedSocks5IncomingTcpAddressCriteria());
		this.addAllowedSocks5IncomingUdpAddressCriteria(
				configuration.getAllowedSocks5IncomingUdpAddressCriteria());
		this.addAllowedSocks5RequestCriteria(
				configuration.getAllowedSocks5RequestCriteria());
		this.addBlockedClientAddressCriteria(
				configuration.getBlockedClientAddressCriteria());
		this.addBlockedSocks5IncomingTcpAddressCriteria(
				configuration.getBlockedSocks5IncomingTcpAddressCriteria());
		this.addBlockedSocks5IncomingUdpAddressCriteria(
				configuration.getBlockedSocks5IncomingUdpAddressCriteria());			
		this.addBlockedSocks5RequestCriteria(
				configuration.getBlockedSocks5RequestCriteria());
		this.setExternalClientSocks5UsernamePassword(
				configuration.getExternalClientSocks5UsernamePassword());
		this.addSettings(configuration.getSettings());
		this.setSocks5UsernamePasswordAuthenticator(
				configuration.getSocks5UsernamePasswordAuthenticator());
	}

	public void addAllowedClientAddressCriteria(
			final Criteria allowedClientAddrCriteria) {
		List<Criterion> allowedClientAddrCriteriaList =
				allowedClientAddrCriteria.toList();
		if (allowedClientAddrCriteriaList.isEmpty()) {
			return;
		}
		this.allowedClientAddressCriteria.addAll(allowedClientAddrCriteriaList);
	}
	
	public void addAllowedSocks5IncomingTcpAddressCriteria(
			final Criteria allowedSocks5IncomingTcpAddrCriteria) {
		List<Criterion> allowedSocks5IncomingTcpAddrCriteriaList =
				allowedSocks5IncomingTcpAddrCriteria.toList();
		if (allowedSocks5IncomingTcpAddrCriteriaList.isEmpty()) {
			return;
		}
		this.allowedSocks5IncomingTcpAddressCriteria.addAll(
				allowedSocks5IncomingTcpAddrCriteriaList);
	}
	
	public void addAllowedSocks5IncomingUdpAddressCriteria(
			final Criteria allowedSocks5IncomingUdpAddrCriteria) {
		List<Criterion> allowedSocks5IncomingUdpAddrCriteriaList = 
				allowedSocks5IncomingUdpAddrCriteria.toList();
		if (allowedSocks5IncomingUdpAddrCriteriaList.isEmpty()) {
			return;
		}
		this.allowedSocks5IncomingUdpAddressCriteria.addAll(
				allowedSocks5IncomingUdpAddrCriteriaList);
	}
	
	public void addAllowedSocks5RequestCriteria(
			final Socks5RequestCriteria allowedSocks5ReqCriteria) {
		List<Socks5RequestCriterion> allowedSocks5ReqCriteriaList =
				allowedSocks5ReqCriteria.toList();
		if (allowedSocks5ReqCriteriaList.isEmpty()) {
			return;
		}
		this.allowedSocks5RequestCriteria.addAll(allowedSocks5ReqCriteriaList);
	}
	
	public void addBlockedClientAddressCriteria(
			final Criteria blockedClientAddrCriteria) {
		List<Criterion> blockedClientAddrCriteriaList =
				blockedClientAddrCriteria.toList();
		if (blockedClientAddrCriteriaList.isEmpty()) {
			return;
		}
		this.blockedClientAddressCriteria.addAll(blockedClientAddrCriteriaList);
	}
	
	public void addBlockedSocks5IncomingTcpAddressCriteria(
			final Criteria blockedSocks5IncomingTcpAddrCriteria) {
		List<Criterion> blockedSocks5IncomingTcpAddrCriteriaList =
				blockedSocks5IncomingTcpAddrCriteria.toList();
		if (blockedSocks5IncomingTcpAddrCriteriaList.isEmpty()) {
			return;
		}
		this.blockedSocks5IncomingTcpAddressCriteria.addAll(
				blockedSocks5IncomingTcpAddrCriteriaList);
	}
	
	public void addBlockedSocks5IncomingUdpAddressCriteria(
			final Criteria blockedSocks5IncomingUdpAddrCriteria) {
		List<Criterion> blockedSocks5IncomingUdpAddrCriteriaList =
				blockedSocks5IncomingUdpAddrCriteria.toList();
		if (blockedSocks5IncomingUdpAddrCriteriaList.isEmpty()) {
			return;
		}
		this.blockedSocks5IncomingUdpAddressCriteria.addAll(
				blockedSocks5IncomingUdpAddrCriteriaList);
	}
	
	public void addBlockedSocks5RequestCriteria(
			final Socks5RequestCriteria blockedSocks5ReqCriteria) {
		List<Socks5RequestCriterion> blockedSocks5ReqCriteriaList =
				blockedSocks5ReqCriteria.toList();
		if (blockedSocks5ReqCriteriaList.isEmpty()) {
			return;
		}
		this.blockedSocks5RequestCriteria.addAll(blockedSocks5ReqCriteriaList);
	}
	
	public void addSettings(final Settings sttngs) {
		List<Setting> sttngsList = sttngs.toList();
		if (sttngsList.isEmpty()) {
			return;
		}
		this.settings.addAll(sttngsList);
	}

	@Override
	public Criteria getAllowedClientAddressCriteria() {
		return Criteria.newInstance(this.allowedClientAddressCriteria);
	}
	
	@Override
	public Criteria getAllowedSocks5IncomingTcpAddressCriteria() {
		return Criteria.newInstance(
				this.allowedSocks5IncomingTcpAddressCriteria);
	}
	
	@Override
	public Criteria getAllowedSocks5IncomingUdpAddressCriteria() {
		return Criteria.newInstance(
				this.allowedSocks5IncomingUdpAddressCriteria);
	}
	
	@Override
	public Socks5RequestCriteria getAllowedSocks5RequestCriteria() {
		return new Socks5RequestCriteria(this.allowedSocks5RequestCriteria);
	}

	@Override
	public Criteria getBlockedClientAddressCriteria() {
		return Criteria.newInstance(this.blockedClientAddressCriteria);
	}
	
	@Override
	public Criteria getBlockedSocks5IncomingTcpAddressCriteria() {
		return Criteria.newInstance(
				this.blockedSocks5IncomingTcpAddressCriteria);
	}
	
	@Override
	public Criteria getBlockedSocks5IncomingUdpAddressCriteria() {
		return Criteria.newInstance(
				this.blockedSocks5IncomingUdpAddressCriteria);
	}
	
	@Override
	public Socks5RequestCriteria getBlockedSocks5RequestCriteria() {
		return new Socks5RequestCriteria(this.blockedSocks5RequestCriteria);
	}
	
	@Override
	public UsernamePassword getExternalClientSocks5UsernamePassword() {
		return this.externalClientSocks5UsernamePassword;
	}
	
	@Override
	public Settings getSettings() {
		return Settings.newInstance(this.settings);
	}

	@Override
	public UsernamePasswordAuthenticator getSocks5UsernamePasswordAuthenticator() {
		return this.socks5UsernamePasswordAuthenticator;
	}
	
	public void setExternalClientSocks5UsernamePassword(
			final UsernamePassword externalClientSocks5UsrnmPsswrd) {
		if (externalClientSocks5UsrnmPsswrd == null) {
			return;
		}
		this.externalClientSocks5UsernamePassword = 
				externalClientSocks5UsrnmPsswrd;
	}

	public void setSocks5UsernamePasswordAuthenticator(
			final UsernamePasswordAuthenticator socks5UsrnmPsswrdAuthenticator) {
		if (socks5UsrnmPsswrdAuthenticator == null) {
			return;
		}
		this.socks5UsernamePasswordAuthenticator = socks5UsrnmPsswrdAuthenticator;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [allowedClientAddressCriteria=")
			.append(this.allowedClientAddressCriteria)
			.append(", allowedSocks5IncomingTcpAddressCriteria=")
			.append(this.allowedSocks5IncomingTcpAddressCriteria)
			.append(", allowedSocks5IncomingUdpAddressCriteria=")
			.append(this.allowedSocks5IncomingUdpAddressCriteria)
			.append(", allowedSocks5RequestCriteria=")
			.append(this.allowedSocks5RequestCriteria)			
			.append(", blockedClientAddressCriteria=")
			.append(this.blockedClientAddressCriteria)
			.append(", blockedSocks5IncomingTcpAddressCriteria=")
			.append(this.blockedSocks5IncomingTcpAddressCriteria)
			.append(", blockedSocks5IncomingUdpAddressCriteria=")
			.append(this.blockedSocks5IncomingUdpAddressCriteria)			
			.append(", blockedSocks5RequestCriteria=")
			.append(this.blockedSocks5RequestCriteria)
			.append(", externalClientSocks5UsernamePassword=")
			.append(this.externalClientSocks5UsernamePassword)
			.append(", settings=")
			.append(this.settings)
			.append(", socks5UsernamePasswordAuthenticator=")
			.append(this.socks5UsernamePasswordAuthenticator)
			.append("]");
		return builder.toString();
	}
	
}