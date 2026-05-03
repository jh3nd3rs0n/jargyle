package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketOption;

public class SocketOptionValueGetterSetterTest {

    @Test
    public void testGetSocketOption01() {
        Assert.assertEquals(
                BogusSocketOption.INSTANCE, 
                new BogusSocketOptionValueGetterSetter().getSocketOption());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSocketOptionValueServerSocketForUnsupportedOperationException01() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket()) {
            new BogusSocketOptionValueGetterSetter().getSocketOptionValue(serverSocket);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetSocketOptionValueValueServerSocketForUnsupportedOperationException01() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket()) {
            new BogusSocketOptionValueGetterSetter().setSocketOptionValue(Boolean.TRUE, serverSocket);
        }
    }

    private static final class BogusSocketOption implements SocketOption<Boolean> {

        public static final BogusSocketOption INSTANCE = new BogusSocketOption();
        
        private BogusSocketOption() { 
        }
        
        @Override
        public String name() {
            return "BOGUS";
        }

        @Override
        public Class<Boolean> type() {
            return Boolean.class;
        }
        
    }
    
    private static final class BogusSocketOptionValueGetterSetter extends SocketOptionValueGetterSetter<Boolean> {

        public BogusSocketOptionValueGetterSetter() {
            super(BogusSocketOption.INSTANCE);
        }
        
    }

}