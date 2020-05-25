package jargyle.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(Setting.SettingXmlAdapter.class)
public final class Setting {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "setting", propOrder = { })
	static class SettingXml {
		@XmlAttribute(name = "name", required = true)
		protected String name;
		@XmlAttribute(name = "value", required = true)
		protected String value;
		@XmlAttribute(name = "comment")
		protected String comment;		
	}
	
	static final class SettingXmlAdapter 
		extends XmlAdapter<SettingXml, Setting> {

		@Override
		public SettingXml marshal(final Setting v) throws Exception {
			SettingXml settingXml = new SettingXml();
			settingXml.comment = v.comment;
			settingXml.name = v.getName();
			settingXml.value = v.getValue().toString();
			return settingXml;
		}

		@Override
		public Setting unmarshal(final SettingXml v) throws Exception {
			return newInstance(v.name, v.value, v.comment);
		}
		
	}
	
	public static Setting newInstance(final String s) {
		String[] sElements = s.split("=", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"setting must be in the following format: NAME=VALUE");
		}
		String name = sElements[0];
		String value = sElements[1];
		return newInstance(name, value);
	}
	
	private static Setting newInstance(final String name, final String value) {
		return SettingSpec.getInstance(name).newSetting(value);
	}
	
	private static Setting newInstance(
			final String name, final String value, final String comment) {
		Setting setting = newInstance(name, value);
		return new Setting(setting.getName(), setting.getValue(), comment);
	}
	
	private final String comment;
	private final String name;
	private final Object value;
	
	Setting(final String n, final Object val) {
		this(n, val, null);
	}
	
	private Setting(final String n, final Object val, final String cmmnt) {
		this.comment = cmmnt;
		this.name = n;
		this.value = val;
	}
		
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Setting)) {
			return false;
		}
		Setting other = (Setting) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!this.value.equals(other.value)) {
			return false;
		}
		return true;
	}

	public String getName() {
		return this.name;
	}

	public Object getValue() {
		return this.value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return String.format("%s=%s", this.name, this.value);
	}
}
