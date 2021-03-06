package jargyle.net.socks.client.propertyspec;

import jargyle.net.Port;
import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;

public final class PortPropertySpec extends PropertySpec {

	public PortPropertySpec(final String s, final Port defaultVal) {
		super(s, Port.class, defaultVal);
	}

	@Override
	public Property newProperty(final String value) {
		return super.newProperty(Port.newInstance(value));
	}

}
