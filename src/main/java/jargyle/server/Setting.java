package jargyle.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import jargyle.client.socks5.UsernamePassword;
import jargyle.common.net.SocketSettings;
import jargyle.server.socks5.Socks5RequestCriteria;
import jargyle.server.socks5.UsernamePasswordAuthenticator;

@XmlJavaTypeAdapter(Setting.SettingXmlAdapter.class)
public final class Setting {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "criteriaValue")
	static class CriteriaValue {
		
		@XmlElement(name = "criteria", required = true)
		protected Criteria value;
		
	}
	
	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "setting", propOrder = { })
	static class SettingXml {
		@XmlElement(name = "name", required = true)
		protected String name;
		@XmlElements({
			@XmlElement(name = "value", type = CriteriaValue.class),
			@XmlElement(name = "value", type = SocketSettingsValue.class),
			@XmlElement(name = "value", type = Socks5RequestCriteriaValue.class),
			@XmlElement(name = "value", type = StringValue.class),
			@XmlElement(name = "value", type = UsernamePasswordAuthenticatorValue.class),
			@XmlElement(name = "value", type = UsernamePasswordValue.class)
		})
		protected Object value;
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
			Object val = v.getValue();
			if (val instanceof Criteria) {
				CriteriaValue newVal = new CriteriaValue();
				newVal.value = (Criteria) val;
				settingXml.value = newVal;
			} else if (val instanceof SocketSettings) {
				SocketSettingsValue newVal = new SocketSettingsValue();
				newVal.value = (SocketSettings) val;
				settingXml.value = newVal;
			} else if (val instanceof Socks5RequestCriteria) {
				Socks5RequestCriteriaValue newVal = new Socks5RequestCriteriaValue();
				newVal.value = (Socks5RequestCriteria) val;
				settingXml.value = newVal;
			} else if (val instanceof UsernamePasswordAuthenticator) {
				UsernamePasswordAuthenticatorValue newVal =
						new UsernamePasswordAuthenticatorValue();
				newVal.value = (UsernamePasswordAuthenticator) val;
				settingXml.value = newVal;
			} else if (val instanceof UsernamePassword) {
				UsernamePasswordValue newVal = new UsernamePasswordValue();
				newVal.value = (UsernamePassword) val;
				settingXml.value = newVal;
			} else {
				StringValue newVal = new StringValue();
				newVal.value = val.toString();
				settingXml.value = newVal;
			}
			return settingXml;
		}

		@Override
		public Setting unmarshal(final SettingXml v) throws Exception {
			Object val = v.value;
			if (val instanceof CriteriaValue) {
				CriteriaValue newVal = (CriteriaValue) val;
				return newInstance(v.name, newVal.value, v.comment);
			}
			if (val instanceof SocketSettingsValue) {
				SocketSettingsValue newVal = (SocketSettingsValue) val;
				return newInstance(v.name, newVal.value, v.comment);
			}
			if (val instanceof Socks5RequestCriteriaValue) {
				Socks5RequestCriteriaValue newVal = 
						(Socks5RequestCriteriaValue) val;
				return newInstance(v.name, newVal.value, v.comment);
			}
			if (val instanceof UsernamePasswordAuthenticatorValue) {
				UsernamePasswordAuthenticatorValue newVal =
						(UsernamePasswordAuthenticatorValue) val;
				return newInstance(v.name, newVal.value, v.comment);
			}
			if (val instanceof UsernamePasswordValue) {
				UsernamePasswordValue newVal = (UsernamePasswordValue) val;
				return newInstance(v.name, newVal.value, v.comment);
			} 
			StringValue newVal = (StringValue) val;
			return newInstance(v.name, newVal.value, v.comment);
		}
		
	}

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "socketSettingsValue")
	static class SocketSettingsValue {
		
		@XmlElement(name = "socketSettings", required = true)
		protected SocketSettings value;
		
	}
	
	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "socks5RequestCriteriaValue")
	static class Socks5RequestCriteriaValue {
		
		@XmlElement(name = "socks5RequestCriteria", required = true)
		protected Socks5RequestCriteria value;
		
	}
	
	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "stringValue")
	static class StringValue {
		
		@XmlValue
		protected String value;
		
	}
	
	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "usernamePasswordAuthenticatorValue")
	static class UsernamePasswordAuthenticatorValue {
		
		@XmlElement(name = "usernamePasswordAuthenticator", required = true)
		protected UsernamePasswordAuthenticator value;
		
	}
	
	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "usernamePasswordValue")
	static class UsernamePasswordValue {
		
		@XmlElement(name = "usernamePassword", required = true)
		protected UsernamePassword value;
		
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
	
	private static Setting newInstance(final String name, final Object value) {
		return SettingSpec.getInstance(name).newSetting(value);
	}

	private static Setting newInstance(
			final String name, final Object value, final String comment) {
		Setting setting = newInstance(name, value);
		return new Setting(setting.getName(), setting.getValue(), comment);
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
