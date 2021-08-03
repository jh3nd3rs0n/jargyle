package com.github.jh3nd3rs0n.jargyle.net;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(PortRanges.PortRangesXmlAdapter.class)
public final class PortRanges {
	
	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "portRanges", propOrder = { "portRanges" })
	static class PortRangesXml {
		@XmlElement(name = "portRange", required = true)
		protected List<PortRange> portRanges = new ArrayList<PortRange>();
		@XmlAttribute(name = "comment")
		protected String comment;		
	}
	
	static final class PortRangesXmlAdapter 
		extends XmlAdapter<PortRangesXml,PortRanges> {

		@Override
		public PortRangesXml marshal(final PortRanges v) throws Exception {
			if (v == null) { return null; } 
			PortRangesXml portRangesXml = new PortRangesXml();
			portRangesXml.comment = v.comment;
			portRangesXml.portRanges = new ArrayList<PortRange>(v.portRanges);
			return portRangesXml;
		}

		@Override
		public PortRanges unmarshal(final PortRangesXml v) throws Exception {
			if (v == null) { return null; } 
			return new PortRanges(v.portRanges, v.comment);
		}
		
	}
	
	public static final PortRanges DEFAULT_INSTANCE = new PortRanges(
			Arrays.asList(PortRange.getDefault()), null);
	
	public static PortRanges getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static PortRanges newInstance(final List<PortRange> prtRanges) {
		return new PortRanges(prtRanges, null);
	}
	
	public static PortRanges newInstance(final PortRange... prtRanges) {
		return newInstance(Arrays.asList(prtRanges));
	}
	
	public static PortRanges newInstance(final String s) {
		List<PortRange> prtRanges = new ArrayList<PortRange>();
		if (s.isEmpty()) {
			return new PortRanges(prtRanges, null);
		}
		String[] sElements = s.split(" ");
		for (String sElement : sElements) {
			prtRanges.add(PortRange.newInstance(sElement));
		}
		return new PortRanges(prtRanges, null);
	}
	
	private final String comment;
	private final List<PortRange> portRanges;
	
	private PortRanges(final List<PortRange> prtRanges, final String cmmnt) {
		this.comment = cmmnt;
		this.portRanges = new ArrayList<PortRange>(prtRanges);
	}

	public boolean contains(final Port port) {
		for (PortRange portRange : this.portRanges) {
			if (portRange.contains(port)) {
				return true;
			}
		}
		return false;
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
		PortRanges other = (PortRanges) obj;
		if (this.portRanges == null) {
			if (other.portRanges != null) {
				return false;
			}
		} else if (!this.portRanges.equals(other.portRanges)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.portRanges == null) ? 0 : this.portRanges.hashCode());
		return result;
	}

	public List<PortRange> toList() {
		return Collections.unmodifiableList(this.portRanges);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Iterator<PortRange> iterator = this.portRanges.iterator();
				iterator.hasNext();) {
			PortRange portRange = iterator.next();
			builder.append(portRange.toString());
			if (iterator.hasNext()) {
				builder.append(' ');
			}
		}
		return builder.toString();
	}
}
