package jargyle.net.socks.client.propertyspec;

import java.net.UnknownHostException;

import jargyle.net.Host;
import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;

public final class HostPropertySpec extends PropertySpec<Host> {

	public HostPropertySpec(final String s, final Host defaultVal) {
		super(s, Host.class, defaultVal);
	}

	@Override
	public Property<Host> newPropertyOfParsableValue(final String value) {
		Host host = null;
		try {
			host = Host.newInstance(value);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e);
		}
		return super.newProperty(host);
	}

}
