package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.bytes.Bytes;
import org.junit.Assert;
import org.junit.Test;

public class BytesPropertySpecTest {

    @Test
    public void testParseString01() {
        Assert.assertEquals(
                Bytes.newInstanceFrom("YQ=="),
                new BytesPropertySpec("bytesProperty", null)
                        .parse("YQ=="));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException01() {
        new BytesPropertySpec("bytesProperty", null)
                .parse(" ");
    }

}