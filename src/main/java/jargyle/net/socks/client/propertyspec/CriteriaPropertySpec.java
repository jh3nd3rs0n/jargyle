package jargyle.net.socks.client.propertyspec;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;
import jargyle.util.Criteria;

public final class CriteriaPropertySpec extends PropertySpec<Criteria> {

	public CriteriaPropertySpec(final String s, final Criteria defaultVal) {
		super(s, Criteria.class, defaultVal);
	}

	@Override
	public Property<Criteria> newPropertyOfParsableValue(final String value) {
		return super.newProperty(Criteria.newInstance(value));
	}
	
}
