package jargyle.net.socks5;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import jargyle.net.Port;
import jargyle.net.PortRanges;
import jargyle.util.Criterion;

@XmlJavaTypeAdapter(Socks5RequestCriterion.Socks5RequestCriterionXmlAdapter.class)
public final class Socks5RequestCriterion {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "socks5RequestCriterion", propOrder = { })
	static class Socks5RequestCriterionXml {
		@XmlElement(name = "clientAddressCriterion")
		protected Criterion clientAddressCriterion;
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
			socks5RequestCriterionXml.clientAddressCriterion = 
					arg.clientAddressCriterion;
			socks5RequestCriterionXml.commandCriterion = arg.commandCriterion;
			socks5RequestCriterionXml.comment = arg.comment;
			socks5RequestCriterionXml.desiredDestinationAddressCriterion = 
					arg.desiredDestinationAddressCriterion;
			socks5RequestCriterionXml.desiredDestinationPortRanges = 
					arg.desiredDestinationPortRanges;
			return socks5RequestCriterionXml;
		}

		@Override
		public Socks5RequestCriterion unmarshal(
				final Socks5RequestCriterionXml arg) throws Exception {
			return new Socks5RequestCriterion(
					arg.clientAddressCriterion,
					arg.commandCriterion,
					arg.desiredDestinationAddressCriterion, 
					arg.desiredDestinationPortRanges, 
					arg.comment);
		}
		
	}
	
	public static Socks5RequestCriterion newInstance(
			final Criterion clientAddrCriterion, 
			final Criterion cmdCriterion,
			final Criterion desiredDestinationAddrCriterion, 
			final PortRanges desiredDestinationPrtRange) {
		return new Socks5RequestCriterion(
				clientAddrCriterion,
				cmdCriterion, 
				desiredDestinationAddrCriterion, 
				desiredDestinationPrtRange);
	}
	
	private final Criterion clientAddressCriterion;
	private final Criterion commandCriterion;
	private final String comment;
	private final Criterion desiredDestinationAddressCriterion;
	private final PortRanges desiredDestinationPortRanges;
	
	private Socks5RequestCriterion(
			final Criterion clientAddrCriterion, 
			final Criterion cmdCriterion,
			final Criterion desiredDestinationAddrCriterion, 
			final PortRanges desiredDestinationPrtRange) {
		this(
				clientAddrCriterion,
				cmdCriterion, 
				desiredDestinationAddrCriterion, 
				desiredDestinationPrtRange, 
				null);
	}

	private Socks5RequestCriterion(
			final Criterion clientAddrCriterion,
			final Criterion cmdCriterion,
			final Criterion desiredDestinationAddrCriterion,
			final PortRanges desiredDestinationPrtRange,
			final String cmmnt) {
		this.clientAddressCriterion = clientAddrCriterion;
		this.commandCriterion = cmdCriterion;
		this.comment = cmmnt;		
		this.desiredDestinationAddressCriterion = desiredDestinationAddrCriterion;
		this.desiredDestinationPortRanges = desiredDestinationPrtRange;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Socks5RequestCriterion other = (Socks5RequestCriterion) obj;
		if (this.clientAddressCriterion == null) {
			if (other.clientAddressCriterion != null) {
				return false;
			}
		} else if (!this.clientAddressCriterion.equals(
				other.clientAddressCriterion)) {
			return false;
		}
		if (this.commandCriterion == null) {
			if (other.commandCriterion != null) {
				return false;
			}
		} else if (!this.commandCriterion.equals(other.commandCriterion)) {
			return false;
		}
		if (this.desiredDestinationAddressCriterion == null) {
			if (other.desiredDestinationAddressCriterion != null) {
				return false;
			}
		} else if (!this.desiredDestinationAddressCriterion.equals(
				other.desiredDestinationAddressCriterion)) {
			return false;
		}
		if (this.desiredDestinationPortRanges == null) {
			if (other.desiredDestinationPortRanges != null) {
				return false;
			}
		} else if (!this.desiredDestinationPortRanges.equals(
				other.desiredDestinationPortRanges)) {
			return false;
		}
		return true;
	}
	
	public boolean evaluatesTrue(
			final String clientAddress, 
			final Socks5Request socks5Req) {
		if (this.clientAddressCriterion != null 
				&& !this.clientAddressCriterion.evaluatesTrue(clientAddress)) {
			return false;
		}
		if (this.commandCriterion != null
				&& !this.commandCriterion.evaluatesTrue(
						socks5Req.getCommand().toString())) {
			return false;
		}
		if (this.desiredDestinationAddressCriterion != null
				&& !this.desiredDestinationAddressCriterion.evaluatesTrue(
						socks5Req.getDesiredDestinationAddress())) {
			return false;
		}
		if (this.desiredDestinationPortRanges != null
				&& !this.desiredDestinationPortRanges.contains(Port.newInstance(
						socks5Req.getDesiredDestinationPort()))) {
			return false;
		}
		return true;
	}

	public Criterion getClientAddressCriterion() {
		return this.clientAddressCriterion;
	}

	public Criterion getCommandCriterion() {
		return this.commandCriterion;
	}
	
	public Criterion getDesiredDestinationAddressCriterion() {
		return this.desiredDestinationAddressCriterion;
	}
	
	public PortRanges getDesiredDestinationPortRanges() {
		return this.desiredDestinationPortRanges;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.clientAddressCriterion == null) ? 
				0 : this.clientAddressCriterion.hashCode());
		result = prime * result + ((this.commandCriterion == null) ? 
				0 : this.commandCriterion.hashCode());
		result = prime * result
				+ ((this.desiredDestinationAddressCriterion == null) ? 
						0 : this.desiredDestinationAddressCriterion.hashCode());
		result = prime * result
				+ ((this.desiredDestinationPortRanges == null) ? 
						0 : this.desiredDestinationPortRanges.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [clientAddressCriterion=")
			.append(this.clientAddressCriterion)
			.append(", commandCriterion=")
			.append(this.commandCriterion)
			.append(", desiredDestinationAddressCriterion=")
			.append(this.desiredDestinationAddressCriterion)
			.append(", desiredDestinationPortRanges=")
			.append(this.desiredDestinationPortRanges)
			.append("]");
		return builder.toString();
	}
	
}
