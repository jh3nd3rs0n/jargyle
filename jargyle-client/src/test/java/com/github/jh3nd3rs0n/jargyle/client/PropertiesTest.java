package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import org.junit.Assert;
import org.junit.Test;

public class PropertiesTest {

    @Test
    public void testOfProperties() {
        Assert.assertNotNull(Properties.of(Properties.of(
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindHost", "0.0.0.0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindPortRanges", "0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientConnectTimeout", "1"))));
    }

    @Test
    public void testOfPropertyVarargs() {
        Assert.assertNotNull(Properties.of(
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindHost", "0.0.0.0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindPortRanges", "0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientConnectTimeout", "1")));
    }

    @Test
    public void testEqualsObject01() {
        Properties properties = Properties.of(
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindHost", "0.0.0.0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindPortRanges", "0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientConnectTimeout", "1"));
        Assert.assertEquals(properties, properties);
    }

    @Test
    public void testEqualsObject02() {
        Properties properties = Properties.of(
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindHost", "0.0.0.0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindPortRanges", "0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientConnectTimeout", "1"));
        Assert.assertNotEquals(properties, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = Properties.of(
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindHost", "0.0.0.0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindPortRanges", "0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientConnectTimeout", "1"));
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        Properties properties1 = Properties.of(
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindHost", "0.0.0.0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindPortRanges", "0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientConnectTimeout", "1"));
        Properties properties2 = Properties.of(
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindPortRanges", "0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientConnectTimeout", "1"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientSocketSettings", ""));
        Assert.assertNotEquals(properties1, properties2);
    }

    @Test
    public void testEqualsObject05() {
        Properties properties1 = Properties.of(
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindHost", "0.0.0.0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindPortRanges", "0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientConnectTimeout", "1"));
        Properties properties2 = Properties.of(
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindHost", "0.0.0.0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindPortRanges", "0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientConnectTimeout", "1"));
        Assert.assertEquals(properties1, properties2);
    }

    @Test
    public void testGetValue01() {
        PropertySpec<Host> propertySpec =
                GeneralPropertySpecConstants.CLIENT_BIND_HOST;
        Property<Host> defaultProperty = propertySpec.getDefaultProperty();
        Properties properties = Properties.of();
        Assert.assertEquals(
                defaultProperty.getValue(),
                properties.getValue(propertySpec));
    }

    @Test
    public void testGetValue02() {
        PropertySpec<Host> propertySpec =
                GeneralPropertySpecConstants.CLIENT_BIND_HOST;
        Property<Host> property = propertySpec.newProperty(Host.newInstance(
                "0.0.0.1"));
        Properties properties = Properties.of(property);
        Assert.assertEquals(
                property.getValue(),
                properties.getValue(property.getPropertySpec()));
    }

    @Test
    public void testHashCode01() {
        Properties properties1 = Properties.of(
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindHost", "0.0.0.0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindPortRanges", "0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientConnectTimeout", "1"));
        Properties properties2 = Properties.of(
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindPortRanges", "0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientConnectTimeout", "1"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientSocketSettings", ""));
        Assert.assertNotEquals(properties1.hashCode(), properties2.hashCode());
    }

    @Test
    public void testHashCode02() {
        Properties properties1 = Properties.of(
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindHost", "0.0.0.0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindPortRanges", "0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientConnectTimeout", "1"));
        Properties properties2 = Properties.of(
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindHost", "0.0.0.0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientBindPortRanges", "0"),
                Property.newInstanceWithParsedValue(
                        "socksClient.clientConnectTimeout", "1"));
        Assert.assertEquals(properties1.hashCode(), properties2.hashCode());
    }

}