package jargyle.common.net;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public final class SocketSetting {

	public static SocketSetting newInstance(final String s) {
		String[] sElements = s.split("=", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"socket setting must be in the following format: "
					+ "SOCKET_SETTING_SPEC=VALUE");
		}
		String socketSettingSpecString = sElements[0];
		String value = sElements[1];
		return newInstance(socketSettingSpecString, value);
	}
	
	private static SocketSetting newInstance(
			final String socketSettingSpecString, final String value) {
		return SocketSettingSpec.getInstance(socketSettingSpecString).newSocketSetting(value);
	}
	
	private final SocketSettingSpec socketSettingSpec;
	private final Object value;
	
	SocketSetting(final SocketSettingSpec spec, final Object val) {
		this.socketSettingSpec = spec;
		this.value = val;
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
		SocketSetting other = (SocketSetting) obj;
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

	public SocketSettingSpec getSocketSettingSpec() {
		return this.socketSettingSpec;
	}

	public Object getValue() {
		return this.value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.socketSettingSpec == null) ? 0 : this.socketSettingSpec.hashCode());
		result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return String.format("%s=%s", this.socketSettingSpec, this.value);
	}
	
}
