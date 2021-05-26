package jargyle.net.socks.transport.v5;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import jargyle.net.InetAddressHelper;
import jargyle.util.UnsignedByte;
import jargyle.util.UnsignedShort;

public final class Socks5Reply {

	private static final class Params {
		private Version version;
		private Reply reply;
		private AddressType addressType;
		private String serverBoundAddress;
		private int serverBoundPort;
		private byte[] byteArray;
	}
	
	private static final int RSV = 0x00;
	
	private static final int MIN_BND_ADDR_LENGTH = 1;
	private static final int MAX_BND_ADDR_LENGTH = 255;
	
	public static Socks5Reply newErrorInstance(final Reply reply) {
		if (reply.equals(Reply.SUCCEEDED)) {
			throw new IllegalArgumentException("reply must be of an error");
		}
		return newInstance(
				reply, 
				InetAddressHelper.INET4_ALL_ZEROS_HOST_ADDRESS,
				0);
	}
	
	public static Socks5Reply newInstance(final byte[] b) {
		Socks5Reply socks5Reply;
		try {
			socks5Reply = newInstanceFrom(new ByteArrayInputStream(b));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return socks5Reply;
	}
	
	public static Socks5Reply newInstance(
			final Reply reply,
			final String serverBoundAddress,
			final int serverBoundPort) {
		byte[] serverBoundAddressBytes = serverBoundAddress.getBytes();
		if (serverBoundAddressBytes.length < MIN_BND_ADDR_LENGTH
				|| serverBoundAddressBytes.length > MAX_BND_ADDR_LENGTH) {
			throw new IllegalArgumentException(String.format(
					"server bound address must be no less than %s byte(s) and "
					+ "no more than %s byte(s)", 
					MIN_BND_ADDR_LENGTH,
					MAX_BND_ADDR_LENGTH));
		}
		if (serverBoundPort < UnsignedShort.MIN_INT_VALUE 
				|| serverBoundPort > UnsignedShort.MAX_INT_VALUE) {
			throw new IllegalArgumentException(String.format(
					"server bound port must be no less than %s and no more "
					+ "than %s", 
					UnsignedShort.MIN_INT_VALUE,
					UnsignedShort.MAX_INT_VALUE));
		}
		AddressType addressType = AddressType.valueForString(
				serverBoundAddress);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version version = Version.V5;
		out.write(version.byteValue());
		out.write(reply.byteValue());
		out.write(RSV);
		out.write(addressType.byteValue());
		try {
			out.write(addressType.newAddress(serverBoundAddress).toByteArray());
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		try {
			out.write(UnsignedShort.newInstance(serverBoundPort).toByteArray());
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		Params params = new Params();
		params.version = version;
		params.reply = reply;
		params.addressType = addressType;
		params.serverBoundAddress = serverBoundAddress;
		params.serverBoundPort = serverBoundPort;
		params.byteArray = out.toByteArray();
		return new Socks5Reply(params);
	}
	
	public static Socks5Reply newInstanceFrom(
			final InputStream in) throws IOException {
		int b = -1;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		b = in.read();
		Version ver = null; 
		try {
			ver = Version.valueOfByte(
					(byte) UnsignedByte.newInstance(b).intValue());
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		out.write(b);
		b = in.read();
		Reply rep = null; 
		try {
			rep = Reply.valueOfByte(
					(byte) UnsignedByte.newInstance(b).intValue());
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		out.write(b);
		b = in.read();
		int rsv;
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
			atyp = AddressType.valueOfByte(
					(byte) UnsignedByte.newInstance(b).intValue());
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		out.write(b);
		Address address = atyp.newAddressFrom(in);
		byte[] bytes = address.toByteArray();
		String bndAddr = address.toString();
		out.write(bytes);
		bytes = new byte[UnsignedShort.BYTE_ARRAY_LENGTH];
		int bytesRead = in.read(bytes);
		bytes = Arrays.copyOf(bytes, bytesRead);
		int bndPort; 
		try {
			bndPort = UnsignedShort.newInstance(bytes).intValue();
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		out.write(bytes);
		Params params = new Params();
		params.version = ver;
		params.reply = rep;
		params.addressType = atyp;
		params.serverBoundAddress = bndAddr;
		params.serverBoundPort = bndPort;
		params.byteArray = out.toByteArray();
		return new Socks5Reply(params);
	}
	
	private final Version version;
	private final Reply reply;
	private final AddressType addressType;
	private final String serverBoundAddress;
	private final int serverBoundPort;
	private final byte[] byteArray;
	
	private Socks5Reply(final Params params) {
		this.version = params.version;
		this.reply = params.reply;
		this.addressType = params.addressType;
		this.serverBoundAddress = params.serverBoundAddress;
		this.serverBoundPort = params.serverBoundPort;
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
		Socks5Reply other = (Socks5Reply) obj;
		if (!Arrays.equals(this.byteArray, other.byteArray)) {
			return false;
		}
		return true;
	}

	public AddressType getAddressType() {
		return this.addressType;
	}

	public Reply getReply() {
		return this.reply;
	}

	public String getServerBoundAddress() {
		return this.serverBoundAddress;
	}

	public int getServerBoundPort() {
		return this.serverBoundPort;
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
			.append(", reply=")
			.append(this.reply)
			.append(", addressType=")
			.append(this.addressType)
			.append(", serverBoundAddress=")
			.append(this.serverBoundAddress)
			.append(", serverBoundPort=")
			.append(this.serverBoundPort)
			.append("]");
		return builder.toString();
	}
	
	
}
