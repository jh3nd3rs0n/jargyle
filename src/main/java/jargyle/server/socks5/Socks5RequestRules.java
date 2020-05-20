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

@XmlJavaTypeAdapter(Socks5RequestRules.Socks5RequestRulesXmlAdapter.class)
public final class Socks5RequestRules {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "socks5RequestRules", propOrder = { "socks5RequestRules" })
	static class Socks5RequestRulesXml {
		@XmlElement(name = "socks5RequestRule")
		protected List<Socks5RequestRule> socks5RequestRules = 
				new ArrayList<Socks5RequestRule>();
	}
	
	static final class Socks5RequestRulesXmlAdapter 
		extends XmlAdapter<Socks5RequestRulesXml,Socks5RequestRules> {

		@Override
		public Socks5RequestRulesXml marshal(
				final Socks5RequestRules v) throws Exception {
			if (v == null) { return null; } 
			Socks5RequestRulesXml socks5RequestRulesXml = 
					new Socks5RequestRulesXml();
			socks5RequestRulesXml.socks5RequestRules = 
					new ArrayList<Socks5RequestRule>(v.socks5RequestRules);
			return socks5RequestRulesXml;
		}

		@Override
		public Socks5RequestRules unmarshal(
				final Socks5RequestRulesXml v) throws Exception {
			if (v == null) { return null; }
			return new Socks5RequestRules(v.socks5RequestRules);
		}
		
	}
	
	public static final Socks5RequestRules EMPTY_INSTANCE = 
			new Socks5RequestRules(Collections.emptyList());
	
	private List<Socks5RequestRule> socks5RequestRules;
	
	public Socks5RequestRules(final List<Socks5RequestRule> socks5ReqRules) {
		this.socks5RequestRules = new ArrayList<Socks5RequestRule>(
				socks5ReqRules);
	}
	
	public Socks5RequestRules(final Socks5RequestRule... socks5ReqRules) {
		this(Arrays.asList(socks5ReqRules));
	}
	
	public Socks5RequestRule anyAppliesTo(final Socks5Request socks5Req) {
		for (Socks5RequestRule socks5RequestRule : this.socks5RequestRules) {
			if (socks5RequestRule.appliesTo(socks5Req)) {
				return socks5RequestRule;
			}
		}
		return null;
	}
	
	public List<Socks5RequestRule> toList() {
		return Collections.unmodifiableList(this.socks5RequestRules);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [socks5RequestRules=")
			.append(this.socks5RequestRules)
			.append("]");
		return builder.toString();
	}
	
}
