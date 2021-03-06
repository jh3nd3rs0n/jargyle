package jargyle.net.socks.client.propertyspec;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.transport.v5.gssapiauth.GssapiProtectionLevels;

public final class GssapiProtectionLevelsPropertySpec extends PropertySpec {

	public GssapiProtectionLevelsPropertySpec(
			final String s, final GssapiProtectionLevels defaultVal) {
		super(s, GssapiProtectionLevels.class, defaultVal);
	}

	@Override
	public Property newProperty(final String value) {
		return super.newProperty(GssapiProtectionLevels.newInstance(value));
	}

}
