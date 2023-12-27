package com.github.jh3nd3rs0n.jargyle.internal.logging;

import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import org.junit.Test;

import java.util.IllegalFormatException;

import static org.junit.Assert.assertEquals;

public class ObjectLogMessageHelperTest {

    @Test
    public void testObjectLogMessageObjectStringObjectVarArgs01() {
        String expected = "String: Hello, World";
        String actual = ObjectLogMessageHelper.objectLogMessage(
                "String", "Hello, World");
        assertEquals(expected, actual);
    }

    @Test
    public void testObjectLogMessageObjectStringObjectVarArgs02() {
        String expected = "String: Hello, World";
        String actual = ObjectLogMessageHelper.objectLogMessage(
                "String", "Hello, %s", "World");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalFormatException.class)
    public void testObjectLogMessageObjectStringObjectVarArgsForIllegalFormatException() {
        String expected = "String: Hello, World";
        String actual = ObjectLogMessageHelper.objectLogMessage(
                "String", "Hello, %s");
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectLogMessageObjectStringObjectVarArgsForNullPointerException01() {
        String expected = "String: null";
        String actual = ObjectLogMessageHelper.objectLogMessage(
                "String", null);
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectLogMessageObjectStringObjectVarArgsForNullPointerException02() {
        String expected = "null: Hello, World";
        String actual = ObjectLogMessageHelper.objectLogMessage(
                null, "Hello, World");
        assertEquals(expected, actual);
    }

}
