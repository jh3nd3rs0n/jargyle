package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "portRange", propOrder = { }) 
class PortRangeXml {
	
	@XmlAttribute(name = "minPort", required = true)
	protected int minPort;
	@XmlAttribute(name = "maxPort", required = true)
	protected int maxPort;
	
	public PortRangeXml() {	}
	
	public PortRangeXml(final PortRange portRange) {
		this.minPort = portRange.getMinPort().intValue();
		this.maxPort = portRange.getMaxPort().intValue();
	}
	
	public PortRange toPortRange() {
		return PortRange.newInstance(
				Port.newInstance(this.minPort),
				Port.newInstance(this.maxPort));
	}
}