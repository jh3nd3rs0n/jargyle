package jargyle.net.socks.client.propertyspec;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.transport.v5.gssapiauth.GssapiProtectionLevels;

public final class GssapiProtectionLevelsPropertySpec extends PropertySpec {

	public GssapiProtectionLevelsPropertySpec(
			final String s, final GssapiProtectionLevels defaultVal) {
		super(s, defaultVal);
	}

	@Override
	public Property newProperty(final Object value) {
		GssapiProtectionLevels val = GssapiProtectionLevels.class.cast(value);
		return Property.newInstance(this, val);
	}

	@Override
	public Property newProperty(final String value) {
		return Property.newInstance(
				this, GssapiProtectionLevels.newInstance(value));
	}

}
