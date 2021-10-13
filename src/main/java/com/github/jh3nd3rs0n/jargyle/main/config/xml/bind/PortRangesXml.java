package com.github.jh3nd3rs0n.jargyle.main.config.xml.bind;

import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "portRanges", propOrder = { "portRangesXml" }) 
class PortRangesXml {
	
	@XmlElement(name = "portRange", required = true)
	protected List<PortRangeXml> portRangesXml;
	
	public PortRangesXml() {
		this.portRangesXml = new ArrayList<PortRangeXml>();
	}
	
	public PortRangesXml(final PortRanges portRanges) {
		this.portRangesXml = new ArrayList<PortRangeXml>();
		for (PortRange portRange : portRanges.toList()) {
			this.portRangesXml.add(new PortRangeXml(portRange));
		}
	}
	
	public PortRanges toPortRanges() {
		List<PortRange> portRanges = new ArrayList<PortRange>();
		for (PortRangeXml portRangeXml : this.portRangesXml) {
			portRanges.add(portRangeXml.toPortRange());
		}
		return PortRanges.newInstance(portRanges);
	}
	
}