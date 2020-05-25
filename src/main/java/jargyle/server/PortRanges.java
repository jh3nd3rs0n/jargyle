package jargyle.server;

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
			Arrays.asList(PortRange.DEFAULT_INSTANCE), null);
	
	public static PortRanges newInstance(
			final PortRange prtRange, final List<PortRange> prtRanges) {
		List<PortRange> list = new ArrayList<PortRange>();
		list.add(prtRange);
		list.addAll(prtRanges);
		return new PortRanges(list, null);
	}
	
	public static PortRanges newInstance(
			final PortRange prtRange, final PortRange... prtRanges) {
		return newInstance(prtRange, Arrays.asList(prtRanges));
	}
	
	public static PortRanges newInstance(final String s) {
		List<PortRange> prtRanges = new ArrayList<PortRange>();
		String[] sElements = s.split("\\s");
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
