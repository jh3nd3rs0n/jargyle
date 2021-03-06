package jargyle.net;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(SocketSetting.SocketSettingXmlAdapter.class)
public final class SocketSetting<V> {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "socketSetting", propOrder = { })
	static class SocketSettingXml {
		@XmlElement(name = "name", required = true)
		protected String name;
		@XmlElement(name = "value", required = true)
		protected String value;
		@XmlAttribute(name = "comment")
		protected String comment;
	}
	
	static final class SocketSettingXmlAdapter 
		extends XmlAdapter<SocketSettingXml, SocketSetting<Object>> {

		@Override
		public SocketSettingXml marshal(
				final SocketSetting<Object> v) throws Exception {
			SocketSettingXml socketSettingXml = new SocketSettingXml();
			socketSettingXml.comment = v.comment;
			socketSettingXml.name = v.getSocketSettingSpec().toString();
			socketSettingXml.value = v.getValue().toString();
			return socketSettingXml;
		}

		@Override
		public SocketSetting<Object> unmarshal(
				final SocketSettingXml v) throws Exception {
			return newInstance(v.name, v.value, v.comment);
		}
		
	}
	
	public static SocketSetting<Object> newInstance(final String s) {
		String[] sElements = s.split("=", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"socket setting must be in the following format: "
					+ "NAME=VALUE");
		}
		String socketSettingSpecString = sElements[0];
		String value = sElements[1];
		return newInstance(socketSettingSpecString, value);
	}
	
	private static SocketSetting<Object> newInstance(
			final String name, final String value) {
		return SocketSettingSpec.valueOfString(
				name).newSocketSettingOfParsableValue(value);
	}
	
	private static SocketSetting<Object> newInstance(
			final String name, 
			final String value, 
			final String comment) {
		SocketSetting<Object> socketSetting = newInstance(name, value);
		return new SocketSetting<Object>(
				socketSetting.getSocketSettingSpec(), 
				socketSetting.getValue(), 
				comment);
	}
	
	private final SocketSettingSpec<V> socketSettingSpec;
	private final V value;
	private final String comment;
	
	SocketSetting(final SocketSettingSpec<V> spec, final V val) {
		this(spec, val, null);
	}
	
	private SocketSetting(
			final SocketSettingSpec<V> spec, 
			final V val, 
			final String cmmnt) {
		V v = spec.getValueType().cast(val);
		this.socketSettingSpec = spec;
		this.value = v;
		this.comment = cmmnt;
	}
	
	public void applyTo(
			final DatagramSocket datagramSocket) throws SocketException {
		this.socketSettingSpec.apply(this.value, datagramSocket);
	}
	
	public void applyTo(
			final ServerSocket serverSocket) throws SocketException {
		this.socketSettingSpec.apply(this.value, serverSocket);
	}
	
	public void applyTo(final Socket socket) throws SocketException {
		this.socketSettingSpec.apply(this.value, socket);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof SocketSetting)) {
			return false;
		}
		SocketSetting<?> other = (SocketSetting<?>) obj;
		if (this.socketSettingSpec != other.socketSettingSpec) {
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

	public SocketSettingSpec<V> getSocketSettingSpec() {
		return this.socketSettingSpec;
	}

	public V getValue() {
		return this.value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.socketSettingSpec == null) ? 
				0 : this.socketSettingSpec.hashCode());
		result = prime * result + ((this.value == null) ? 
				0 : this.value.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return String.format("%s=%s", this.socketSettingSpec, this.value);
	}
	
}
