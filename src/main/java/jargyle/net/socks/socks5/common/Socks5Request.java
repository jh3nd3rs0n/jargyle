package jargyle.net.socks.socks5.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import jargyle.util.UnsignedByte;
import jargyle.util.UnsignedShort;

public final class Socks5Request {

	private static final class Params {
		private Version version;
		private Command command;
		private AddressType addressType;
		private String desiredDestinationAddress;
		private int desiredDestinationPort;
		private byte[] byteArray;
	}
	
	private static final int RSV = 0x00;
	
	private static final int MIN_DST_ADDR_LENGTH = 1;
	private static final int MAX_DST_ADDR_LENGTH = 255;
	
	public static Socks5Request newInstance(final byte[] b) {
		Socks5Request socks5Request;
		try {
			socks5Request = newInstanceFrom(new ByteArrayInputStream(b));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return socks5Request;
	}
	
	public static Socks5Request newInstance(
			final Command command,
			final AddressType addressType,
			final String desiredDestinationAddress,
			final int desiredDestinationPort) {
		byte[] desiredDestinationAddressBytes = desiredDestinationAddress.getBytes();
		if (desiredDestinationAddressBytes.length < MIN_DST_ADDR_LENGTH
				|| desiredDestinationAddressBytes.length > MAX_DST_ADDR_LENGTH) {
			throw new IllegalArgumentException(String.format(
					"desired destination address must be no less than %s byte(s) and no more than %s byte(s)", 
					MIN_DST_ADDR_LENGTH,
					MAX_DST_ADDR_LENGTH));
		}
		if (desiredDestinationPort < UnsignedShort.MIN_INT_VALUE 
				|| desiredDestinationPort > UnsignedShort.MAX_INT_VALUE) {
			throw new IllegalArgumentException(String.format(
					"desired destination port must be no less than %s and no more than %s", 
					UnsignedShort.MIN_INT_VALUE,
					UnsignedShort.MAX_INT_VALUE));
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version version = Version.V5;
		out.write(version.byteValue());
		out.write(command.byteValue());
		out.write(RSV);
		out.write(addressType.byteValue());
		try {
			out.write(addressType.writeAddress(desiredDestinationAddress));
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		try {
			out.write(UnsignedShort.newInstance(
					desiredDestinationPort).toByteArray());
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		Params params = new Params();
		params.version = version;
		params.command = command;
		params.addressType = addressType;
		params.desiredDestinationAddress = desiredDestinationAddress;
		params.desiredDestinationPort = desiredDestinationPort;
		params.byteArray = out.toByteArray();
		return new Socks5Request(params);
	}
	
	public static Socks5Request newInstanceFrom(
			final InputStream in) throws IOException {
		int b = -1;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		b = in.read();
		Version ver = null; 
		try {
			ver = Version.valueOf(
					(byte) UnsignedByte.newInstance(b).intValue());
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		out.write(b);
		b = in.read();
		Command cmd = null; 
		try {
			cmd = Command.valueOf(
					(byte) UnsignedByte.newInstance(b).intValue());
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		out.write(b);
		b = in.read();
		int rsv = -1;
		try {
			rsv = UnsignedByte.newInstance(b).intValue();
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		if (rsv != RSV) {
			throw new IOException(String.format(
					"expected RSV is %s, actual RSV is %s", RSV, rsv));
		}
		out.write(b);
		b = in.read();
		AddressType atyp = null; 
		try {
			atyp = AddressType.valueOf(
					(byte) UnsignedByte.newInstance(b).intValue());
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		out.write(b);
		b = in.read();
		int dstAddrLength;
		try {
			dstAddrLength = atyp.getAddressLength(
					(byte) UnsignedByte.newInstance(b).intValue());
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		byte[] bytes = new byte[dstAddrLength];
		bytes[0] = (byte) UnsignedByte.newInstance(b).intValue();
		int bytesRead = in.read(bytes, 1, dstAddrLength - 1);
		bytes = Arrays.copyOf(bytes, bytesRead + 1);
		String dstAddr = null; 
		try {
			dstAddr = atyp.readAddress(bytes);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		out.write(bytes);
		bytes = new byte[UnsignedShort.BYTE_ARRAY_LENGTH];
		bytesRead = in.read(bytes);
		bytes = Arrays.copyOf(bytes, bytesRead);
		int dstPort; 
		try {
			dstPort = UnsignedShort.newInstance(bytes).intValue();
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		out.write(bytes);
		Params params = new Params();
		params.version = ver;
		params.command = cmd;
		params.addressType = atyp;
		params.desiredDestinationAddress = dstAddr;
		params.desiredDestinationPort = dstPort;
		params.byteArray = out.toByteArray();
		return new Socks5Request(params);
	}
	
	private final Version version;
	private final Command command;
	private final AddressType addressType;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final byte[] byteArray;
	
	private Socks5Request(final Params params) {
		this.version = params.version;
		this.command = params.command;
		this.addressType = params.addressType;
		this.desiredDestinationAddress = params.desiredDestinationAddress;
		this.desiredDestinationPort = params.desiredDestinationPort;
		this.byteArray = params.byteArray;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Socks5Request other = (Socks5Request) obj;
		if (!Arrays.equals(this.byteArray, other.byteArray)) {
			return false;
		}
		return true;
	}

	public AddressType getAddressType() {
		return this.addressType;
	}

	public Command getCommand() {
		return this.command;
	}

	public String getDesiredDestinationAddress() {
		return this.desiredDestinationAddress;
	}

	public int getDesiredDestinationPort() {
		return this.desiredDestinationPort;
	}

	public Version getVersion() {
		return this.version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(this.byteArray);
		return result;
	}

	public byte[] toByteArray() {
		return Arrays.copyOf(this.byteArray, this.byteArray.length);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [version=")
			.append(this.version)
			.append(", command=")
			.append(this.command)
			.append(", addressType=")
			.append(this.addressType)
			.append(", desiredDestinationAddress=")
			.append(this.desiredDestinationAddress)
			.append(", desiredDestinationPort=")
			.append(this.desiredDestinationPort)
			.append("]");
		return builder.toString();
	}
	
	
}
