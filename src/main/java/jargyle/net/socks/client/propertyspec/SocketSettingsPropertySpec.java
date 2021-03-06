package jargyle.net.socks.client.propertyspec;

import jargyle.net.SocketSettings;
import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;

public final class SocketSettingsPropertySpec extends PropertySpec {

	public SocketSettingsPropertySpec(
			final String s, final SocketSettings defaultVal) {
		super(s, defaultVal);
	}

	@Override
	public Property newProperty(final Object value) {
		SocketSettings val = SocketSettings.class.cast(value);
		return super.newProperty(val);
	}

	@Override
	public Property newProperty(final String value) {
		return super.newProperty(SocketSettings.newInstance(value));
	}

}
