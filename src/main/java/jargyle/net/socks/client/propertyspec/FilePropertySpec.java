package jargyle.net.socks.client.propertyspec;

import java.io.File;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;

public final class FilePropertySpec extends PropertySpec {

	public FilePropertySpec(final String s, final File defaultVal) {
		super(s, defaultVal);
	}

	@Override
	public Property getDefaultProperty() {
		return Property.newInstance(this, File.class.cast(this.defaultValue));
	}

	@Override
	public Property newProperty(final Object value) {
		File val = File.class.cast(value);
		if (!val.exists()) {
			throw new IllegalArgumentException(String.format(
					"file `%s' does not exist", 
					val));
		}
		if (!val.isFile()) {
			throw new IllegalArgumentException(String.format(
					"file `%s' must be a file", 
					val));
		}
		return Property.newInstance(this, val);
	}

	@Override
	public Property newProperty(final String value) {
		return newProperty(new File(value));
	}

}
