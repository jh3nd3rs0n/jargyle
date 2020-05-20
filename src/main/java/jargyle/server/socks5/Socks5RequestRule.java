package jargyle.server.socks5;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import jargyle.common.net.socks5.Socks5Request;
import jargyle.server.Expression;
import jargyle.server.ExpressionType;
import jargyle.server.Port;
import jargyle.server.PortRange;

@XmlJavaTypeAdapter(Socks5RequestRule.Socks5RequestRuleXmlAdapter.class)
public final class Socks5RequestRule {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "socks5RequestRule", propOrder = { })
	static class Socks5RequestRuleXml {
		@XmlAttribute(name = "command", required = true)
		protected Socks5Command command;
		@XmlElement(name = "desiredDestinationAddressExpression")
		protected Expression desiredDestinationAddressExpression;
		@XmlElement(name = "desiredDestinationPortRange")
		protected PortRange desiredDestinationPortRange;
	}
	
	static final class Socks5RequestRuleXmlAdapter 
		extends XmlAdapter<Socks5RequestRuleXml, Socks5RequestRule> {

		@Override
		public Socks5RequestRuleXml marshal(
				final Socks5RequestRule arg) throws Exception {
			Socks5RequestRuleXml socks5RequestRuleXml = 
					new Socks5RequestRuleXml();
			socks5RequestRuleXml.command = arg.command;
			socks5RequestRuleXml.desiredDestinationAddressExpression = 
					arg.desiredDestinationAddressExpression;
			socks5RequestRuleXml.desiredDestinationPortRange = 
					arg.desiredDestinationPortRange;
			return socks5RequestRuleXml;
		}

		@Override
		public Socks5RequestRule unmarshal(
				final Socks5RequestRuleXml arg) throws Exception {
			return new Socks5RequestRule(
					arg.command, 
					arg.desiredDestinationAddressExpression, 
					arg.desiredDestinationPortRange);
		}
		
	}
	
	private final Socks5Command command;
	private final Expression desiredDestinationAddressExpression;
	private final PortRange desiredDestinationPortRange;
	
	public Socks5RequestRule(
			final Socks5Command cmd, 
			final Expression desiredDestinationAddressExpr,
			final PortRange desiredDestinationPrtRange) {
		if (cmd == null) {
			throw new NullPointerException("command must not be null");
		}
		this.command = cmd;
		this.desiredDestinationAddressExpression = 
				desiredDestinationAddressExpr;
		this.desiredDestinationPortRange = desiredDestinationPrtRange;
	}

	public boolean appliesTo(final Socks5Request socks5Req) {
		if (!this.command.commandValue().equals(socks5Req.getCommand())) {
			return false;
		}
		Expression desiredDestinationAddressExpr = 
				this.desiredDestinationAddressExpression;
		if (desiredDestinationAddressExpr == null) {
			desiredDestinationAddressExpr = 
					ExpressionType.REGULAR.newExpression(".*");
		}
		if (!desiredDestinationAddressExpr.matches(
				socks5Req.getDesiredDestinationAddress())) {
			return false;
		}
		PortRange desiredDestinationPrtRange = this.desiredDestinationPortRange;
		if (desiredDestinationPrtRange == null) {
			desiredDestinationPrtRange = PortRange.DEFAULT_INSTANCE;
		}
		if (!desiredDestinationPrtRange.contains(
				Port.newInstance(socks5Req.getDesiredDestinationPort()))) {
			return false;
		}
		return true;
	}
	
	public Socks5Command getCommand() {
		return this.command;
	}

	public Expression getDesiredDestinationAddressExpression() {
		return this.desiredDestinationAddressExpression;
	}

	public PortRange getDesiredDestinationPortRange() {
		return this.desiredDestinationPortRange;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [command=")
			.append(this.command)
			.append(", desiredDestinationAddressExpression=")
			.append(this.desiredDestinationAddressExpression)
			.append(", desiredDestinationPortRange=")
			.append(this.desiredDestinationPortRange)
			.append("]");
		return builder.toString();
	}
	
	
}
