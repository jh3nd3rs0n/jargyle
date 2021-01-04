package jargyle.server.socks5;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import jargyle.common.net.Port;
import jargyle.common.net.PortRanges;
import jargyle.common.net.socks5.Socks5Request;
import jargyle.server.Criterion;
import jargyle.server.CriterionMethod;

@XmlJavaTypeAdapter(Socks5RequestCriterion.Socks5RequestCriterionXmlAdapter.class)
public final class Socks5RequestCriterion {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "socks5RequestCriterion", propOrder = { })
	static class Socks5RequestCriterionXml {
		@XmlElement(name = "sourceAddressCriterion")
		protected Criterion sourceAddressCriterion;
		@XmlElement(name = "commandCriterion")
		protected Criterion commandCriterion;
		@XmlElement(name = "desiredDestinationAddressCriterion")
		protected Criterion desiredDestinationAddressCriterion;
		@XmlElement(name = "desiredDestinationPortRanges")
		protected PortRanges desiredDestinationPortRanges;
		@XmlAttribute(name = "comment")
		protected String comment;		
	}
	
	static final class Socks5RequestCriterionXmlAdapter 
		extends XmlAdapter<Socks5RequestCriterionXml, Socks5RequestCriterion> {

		@Override
		public Socks5RequestCriterionXml marshal(
				final Socks5RequestCriterion arg) throws Exception {
			Socks5RequestCriterionXml socks5RequestCriterionXml = 
					new Socks5RequestCriterionXml();
			socks5RequestCriterionXml.commandCriterion = arg.commandCriterion;
			socks5RequestCriterionXml.comment = arg.comment;
			socks5RequestCriterionXml.desiredDestinationAddressCriterion = 
					arg.desiredDestinationAddressCriterion;
			socks5RequestCriterionXml.desiredDestinationPortRanges = 
					arg.desiredDestinationPortRanges;
			socks5RequestCriterionXml.sourceAddressCriterion = 
					arg.sourceAddressCriterion;
			return socks5RequestCriterionXml;
		}

		@Override
		public Socks5RequestCriterion unmarshal(
				final Socks5RequestCriterionXml arg) throws Exception {
			return new Socks5RequestCriterion(
					arg.sourceAddressCriterion,
					arg.commandCriterion,
					arg.desiredDestinationAddressCriterion, 
					arg.desiredDestinationPortRanges, 
					arg.comment);
		}
		
	}
	
	public static final Criterion DEFAULT_SOURCE_ADDRESS_CRITERION =
			Criterion.newInstance(CriterionMethod.MATCHES, ".*");
	public static final Criterion DEFAULT_COMMAND_CRITERION =
			Criterion.newInstance(CriterionMethod.MATCHES, ".*");
	public static final Criterion DEFAULT_DESIRED_DESTINATION_ADDRESS_CRITERION =
			Criterion.newInstance(CriterionMethod.MATCHES, ".*");
	public static final PortRanges DEFAULT_DESIRED_DESTINATION_PORT_RANGES =
			PortRanges.DEFAULT_INSTANCE;
	
	private final Criterion sourceAddressCriterion;
	private final Criterion commandCriterion;
	private final String comment;
	private final Criterion desiredDestinationAddressCriterion;
	private final PortRanges desiredDestinationPortRanges;
	
	public Socks5RequestCriterion(
			final Criterion sourceAddrCriterion, 
			final Criterion cmdCriterion,
			final Criterion desiredDestinationAddrCriterion, 
			final PortRanges desiredDestinationPrtRange) {
		this(
				sourceAddrCriterion,
				cmdCriterion, 
				desiredDestinationAddrCriterion, 
				desiredDestinationPrtRange, 
				null);
	}

	private Socks5RequestCriterion(
			final Criterion sourceAddrCriterion,
			final Criterion cmdCriterion,
			final Criterion desiredDestinationAddrCriterion,
			final PortRanges desiredDestinationPrtRange,
			final String cmmnt) {
		this.sourceAddressCriterion = sourceAddrCriterion;
		this.commandCriterion = cmdCriterion;
		this.comment = cmmnt;		
		this.desiredDestinationAddressCriterion = 
				desiredDestinationAddrCriterion;
		this.desiredDestinationPortRanges = desiredDestinationPrtRange;
	}
	
	public boolean evaluatesTrue(
			final String sourceAddress, 
			final Socks5Request socks5Req) {
		if (!this.getSourceAddressCriterion().evaluatesTrue(
				sourceAddress)) {
			return false;
		}
		if (!this.getCommandCriterion().evaluatesTrue(
				socks5Req.getCommand().toString())) {
			return false;
		}
		if (!this.getDesiredDestinationAddressCriterion().evaluatesTrue(
				socks5Req.getDesiredDestinationAddress())) {
			return false;
		}
		if (!this.getDesiredDestinationPortRanges().contains(
				Port.newInstance(socks5Req.getDesiredDestinationPort()))) {
			return false;
		}
		return true;
	}
	
	public Criterion getCommandCriterion() {
		if (this.commandCriterion == null) {
			return DEFAULT_COMMAND_CRITERION;
		}
		return this.commandCriterion;
	}

	public Criterion getDesiredDestinationAddressCriterion() {
		if (this.desiredDestinationAddressCriterion == null) {
			return DEFAULT_DESIRED_DESTINATION_ADDRESS_CRITERION;
		}
		return this.desiredDestinationAddressCriterion;
	}

	public PortRanges getDesiredDestinationPortRanges() {
		if (this.desiredDestinationPortRanges == null) {
			return DEFAULT_DESIRED_DESTINATION_PORT_RANGES;
		}
		return this.desiredDestinationPortRanges;
	}
	
	public Criterion getSourceAddressCriterion() {
		if (this.sourceAddressCriterion == null) {
			return DEFAULT_SOURCE_ADDRESS_CRITERION;
		}
		return this.sourceAddressCriterion;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [sourceAddressCriterion=")
			.append(this.getSourceAddressCriterion())
			.append(", commandCriterion=")
			.append(this.getCommandCriterion())
			.append(", desiredDestinationAddressCriterion=")
			.append(this.getDesiredDestinationAddressCriterion())
			.append(", desiredDestinationPortRanges=")
			.append(this.getDesiredDestinationPortRanges())
			.append("]");
		return builder.toString();
	}
	
}
