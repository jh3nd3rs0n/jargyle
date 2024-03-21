package com.github.jh3nd3rs0n.jargyle.server.internal.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "portRanges")
class PortRangesXml extends ValueXml {

	@XmlElement(name = "portRange")
	protected List<String> portRanges;
	
	public PortRangesXml() {
		this.portRanges = new ArrayList<String>();
	}
	
	public PortRangesXml(final PortRanges prtRanges) {
		this.portRanges = new ArrayList<String>();
		for (PortRange prtRange : prtRanges.toList()) {
			this.portRanges.add(prtRange.toString());
		}
	}
	
	public PortRanges toPortRanges() {
		List<PortRange> prtRanges = new ArrayList<PortRange>();
		for (String portRange : this.portRanges) {
			prtRanges.add(PortRange.newInstanceFrom(portRange));
		}
		return PortRanges.of(prtRanges);
	}
	
	@Override
	public Object toValue() {
		return this.toPortRanges();
	}

}
