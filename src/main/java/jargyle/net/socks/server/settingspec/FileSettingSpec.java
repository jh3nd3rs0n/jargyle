package jargyle.net.socks.server.settingspec;

import java.io.File;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;

public final class FileSettingSpec extends SettingSpec {

	public FileSettingSpec(final String s, final File defaultVal) {
		super(s, defaultVal);
	}

	@Override
	public Setting newSetting(final Object value) {
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
		return super.newSetting(val);
	}

	@Override
	public Setting newSetting(final String value) {
		return newSetting(new File(value));
	}

}
