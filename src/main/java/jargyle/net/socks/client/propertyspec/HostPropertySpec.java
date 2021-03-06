package jargyle.net.socks.client.propertyspec;

import java.net.UnknownHostException;

import jargyle.net.Host;
import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;

public final class HostPropertySpec extends PropertySpec {

	public HostPropertySpec(final String s, final Host defaultVal) {
		super(s, defaultVal);
	}

	@Override
	public Property newProperty(final Object value) {
		Host val = Host.class.cast(value);
		return Property.newInstance(this, val);
	}

	@Override
	public Property newProperty(final String value) {
		Host host = null;
		try {
			host = Host.newInstance(value);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e);
		}
		return Property.newInstance(this, host);
	}

}
