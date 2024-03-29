package com.github.jh3nd3rs0n.jargyle.internal.throwable;

import org.junit.Test;

import javax.net.ssl.SSLException;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ThrowableHelperTest {

    @Test
    public void testIsOrHasInstanceOfThrowableClass01() {
        assertTrue(ThrowableHelper.isOrHasInstanceOf(
                new Throwable(), Throwable.class));
    }

    @Test
    public void testIsOrHasInstanceOfThrowableClass02() {
        assertTrue(ThrowableHelper.isOrHasInstanceOf(
                new IOException(new IOException(new SSLException(""))), SSLException.class));
    }

    @Test
    public void testIsOrHasInstanceOfThrowableClass03() {
        assertFalse(ThrowableHelper.isOrHasInstanceOf(
                new IOException(new IOException()), SSLException.class));
    }

    @Test(expected = NullPointerException.class)
    public void testIsOrHasInstanceOfThrowableClassForNullPointerException() {
        assertTrue(ThrowableHelper.isOrHasInstanceOf(null, Throwable.class));
    }

}
