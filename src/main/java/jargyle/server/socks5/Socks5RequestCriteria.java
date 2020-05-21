package jargyle.server.socks5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import jargyle.common.net.socks5.Socks5Request;

@XmlJavaTypeAdapter(Socks5RequestCriteria.Socks5RequestCriteriaXmlAdapter.class)
public final class Socks5RequestCriteria {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "socks5RequestCriteria", propOrder = { "socks5RequestCriteria" })
	static class Socks5RequestCriteriaXml {
		@XmlElement(name = "socks5RequestCriterion")
		protected List<Socks5RequestCriterion> socks5RequestCriteria = 
				new ArrayList<Socks5RequestCriterion>();
	}
	
	static final class Socks5RequestCriteriaXmlAdapter 
		extends XmlAdapter<Socks5RequestCriteriaXml,Socks5RequestCriteria> {

		@Override
		public Socks5RequestCriteriaXml marshal(
				final Socks5RequestCriteria v) throws Exception {
			if (v == null) { return null; } 
			Socks5RequestCriteriaXml socks5RequestCriteriaXml = 
					new Socks5RequestCriteriaXml();
			socks5RequestCriteriaXml.socks5RequestCriteria = 
					new ArrayList<Socks5RequestCriterion>(
							v.socks5RequestCriteria);
			return socks5RequestCriteriaXml;
		}

		@Override
		public Socks5RequestCriteria unmarshal(
				final Socks5RequestCriteriaXml v) throws Exception {
			if (v == null) { return null; }
			return new Socks5RequestCriteria(v.socks5RequestCriteria);
		}
		
	}
	
	public static final Socks5RequestCriteria EMPTY_INSTANCE = 
			new Socks5RequestCriteria(Collections.emptyList());
	
	private List<Socks5RequestCriterion> socks5RequestCriteria;
	
	public Socks5RequestCriteria(
			final List<Socks5RequestCriterion> socks5ReqCriteria) {
		this.socks5RequestCriteria = new ArrayList<Socks5RequestCriterion>(
				socks5ReqCriteria);
	}
	
	public Socks5RequestCriteria(
			final Socks5RequestCriterion... socks5ReqCriteria) {
		this(Arrays.asList(socks5ReqCriteria));
	}
	
	public Socks5RequestCriterion anyEvaluatesTrue(final Socks5Request socks5Req) {
		for (Socks5RequestCriterion socks5RequestCriterion : this.socks5RequestCriteria) {
			if (socks5RequestCriterion.evaluate(socks5Req)) {
				return socks5RequestCriterion;
			}
		}
		return null;
	}
	
	public List<Socks5RequestCriterion> toList() {
		return Collections.unmodifiableList(this.socks5RequestCriteria);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [socks5RequestCriteria=")
			.append(this.socks5RequestCriteria)
			.append("]");
		return builder.toString();
	}
	
}
