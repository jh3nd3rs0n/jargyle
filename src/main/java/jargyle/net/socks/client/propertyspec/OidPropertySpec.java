package jargyle.net.socks.client.propertyspec;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;

public final class OidPropertySpec extends PropertySpec {

	private static Oid newOid(final String s) {
		Oid oid = null;
		try {
			oid = new Oid(s);
		} catch (GSSException e) {
			throw new IllegalArgumentException(e);
		}
		return oid;
	}
	
	public OidPropertySpec(final String s, final String defaultVal) {
		super(s, newOid(defaultVal));
	}

	@Override
	public Property getDefaultProperty() {
		return Property.newInstance(this, Oid.class.cast(this.defaultValue));
	}

	@Override
	public Property newProperty(final Object value) {
		Oid val = Oid.class.cast(value);
		return Property.newInstance(this, val);
	}

	@Override
	public Property newProperty(final String value) {
		Oid oid = null;
		try {
			oid = new Oid(value);
		} catch (GSSException e) {
			throw new IllegalArgumentException(e);
		}
		return Property.newInstance(this, oid);
	}

}
