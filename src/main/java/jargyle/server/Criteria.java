package jargyle.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(Criteria.CriteriaXmlAdapter.class)
public final class Criteria {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "criteria", propOrder = { "criteria" })
	static class CriteriaXml {
		@XmlElement(name = "criterion")
		protected List<Criterion> criteria = new ArrayList<Criterion>();
	}
	
	static final class CriteriaXmlAdapter
		extends XmlAdapter<CriteriaXml,Criteria> {

		@Override
		public CriteriaXml marshal(final Criteria v) throws Exception {
			if (v == null) { return null; }
			CriteriaXml criteriaXml = new CriteriaXml();
			criteriaXml.criteria = new ArrayList<Criterion>(v.criteria);
			return criteriaXml;
		}

		@Override
		public Criteria unmarshal(final CriteriaXml v) throws Exception {
			if (v == null) { return null; }
			return new Criteria(v.criteria);
		}
		
	}

	public static final Criteria EMPTY_INSTANCE = new Criteria(
			Collections.emptyList());
	
	public static Criteria newInstance(final Criterion... criteria) {
		return newInstance(Arrays.asList(criteria));
	}
	
	public static Criteria newInstance(final List<Criterion> criteria) {
		return new Criteria(criteria);
	}
	
	public static Criteria newInstance(final String s) {
		List<Criterion> criteria = new ArrayList<Criterion>();
		String[] sElements = s.split("\\s");
		for (String sElement : sElements) {
			criteria.add(Criterion.newInstance(sElement));
		}
		return new Criteria(criteria);
	}
	
	private final List<Criterion> criteria;
	
	private Criteria(final List<Criterion> crit) {
		this.criteria = new ArrayList<Criterion>(crit);
	}
	
	public Criterion anyEvaluatesToTrue(final String str) {
		for (Criterion criterion : this.criteria) {
			if (criterion.evaluate(str)) {
				return criterion;
			}
		}
		return null;
	}
	
	public List<Criterion> toList() {
		return Collections.unmodifiableList(this.criteria);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Iterator<Criterion> iterator = this.criteria.iterator();
				iterator.hasNext();) {
			Criterion criterion = iterator.next();
			builder.append(criterion.toString());
			if (iterator.hasNext()) {
				builder.append(' ');
			}
		}
		return builder.toString();
	}
}
