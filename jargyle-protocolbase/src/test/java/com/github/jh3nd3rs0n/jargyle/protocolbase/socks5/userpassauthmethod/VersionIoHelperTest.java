package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassauthmethod;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;

public class VersionIoHelperTest {

    @Test(expected = IOException.class)
    public void testReadVersionFromInputStreamForIOException() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(
                new byte[] { (byte) 0xfe });
        VersionIoHelper.readVersionFrom(in);
    }

    @Test(expected = EOFException.class)
    public void testReadVersionFromInputStreamForEOFException() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[] { });
        VersionIoHelper.readVersionFrom(in);
    }

}