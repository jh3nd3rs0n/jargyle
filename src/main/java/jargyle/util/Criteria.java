package jargyle.util;

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

	private static final Criteria EMPTY_INSTANCE = new Criteria(
			Collections.emptyList());
	
	public static Criteria getEmptyInstance() {
		return EMPTY_INSTANCE;
	}
	
	public static Criteria newInstance(final Criterion... c) {
		return newInstance(Arrays.asList(c));
	}
	
	public static Criteria newInstance(final List<Criterion> c) {
		return new Criteria(c);
	}
	
	public static Criteria newInstance(final String s) {
		List<Criterion> criteria = new ArrayList<Criterion>();
		if (s.isEmpty()) {
			return new Criteria(criteria);
		}
		String[] sElements = s.split(" ");
		for (String sElement : sElements) {
			criteria.add(Criterion.newInstance(sElement));
		}
		return new Criteria(criteria);
	}
	
	private final List<Criterion> criteria;
	
	private Criteria(final List<Criterion> c) {
		this.criteria = new ArrayList<Criterion>(c);
	}
	
	public Criterion anyEvaluatesTrue(final String str) {
		for (Criterion criterion : this.criteria) {
			if (criterion.evaluatesTrue(str)) {
				return criterion;
			}
		}
		return null;
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
		Criteria other = (Criteria) obj;
		if (this.criteria == null) {
			if (other.criteria != null) {
				return false;
			}
		} else if (!this.criteria.equals(other.criteria)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.criteria == null) ? 0 : this.criteria.hashCode());
		return result;
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
