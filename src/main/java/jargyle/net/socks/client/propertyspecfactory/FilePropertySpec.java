package jargyle.net.socks.client.propertyspecfactory;

import java.io.File;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;

final class FilePropertySpec extends PropertySpec<File> {

	public FilePropertySpec(final String s, final File defaultVal) {
		super(s, File.class, defaultVal);
	}

	@Override
	public Property<File> newProperty(final File value) {
		if (!value.exists()) {
			throw new IllegalArgumentException(String.format(
					"file `%s' does not exist", 
					value));
		}
		if (!value.isFile()) {
			throw new IllegalArgumentException(String.format(
					"file `%s' must be a file", 
					value));
		}
		return super.newProperty(value);
	}

	@Override
	public Property<File> newPropertyOfParsableValue(final String value) {
		return newProperty(new File(value));
	}

}
