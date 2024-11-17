package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import org.junit.Assert;
import org.junit.Test;

public class PropertyTest {

    @Test
    public void testNewInstanceStringType01() {
        Assert.assertNotNull(Property.newInstance(
                "socksClient.clientBindHost", null));
    }

    @Test
    public void testNewInstanceStringType02() {
        Assert.assertNotNull(Property.newInstance(
                "socksClient.clientBindHost", Host.newInstance("0.0.0.0")));
    }

    @Test(expected = ClassCastException.class)
    public void testNewInstanceStringTypeForClassCastException() {
        Assert.assertNotNull(Property.newInstance(
                "socksClient.clientBindHost", 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceStringTypeForIllegalArgumentException() {
        Assert.assertNotNull(Property.newInstance(
                "socks_client.client_bind_host",
                Host.newInstance("localhost")));
    }

    @Test
    public void testNewInstanceWithParsedValueStringString() {
        Assert.assertNotNull(Property.newInstanceWithParsedValue(
                "socksClient.clientBindPortRanges", "0"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithParsedValueStringStringForIllegalArgumentException01() {
        Assert.assertNotNull(Property.newInstanceWithParsedValue(
                "socks_client.client_bind_port_ranges", "0"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithParsedValueStringStringForIllegalArgumentException02() {
        Assert.assertNotNull(Property.newInstanceWithParsedValue(
                "socksClient.clientBindPortRanges", "-1"));
    }

    @Test
    public void testEqualsObject01() {
        Property<Object> property = Property.newInstance(
                "socksClient.clientBindHost", Host.newInstance("0.0.0.0"));
        Assert.assertEquals(property, property);
    }

    @Test
    public void testEqualsObject02() {
        Property<Object> property = Property.newInstance(
                "socksClient.clientBindHost", Host.newInstance("0.0.0.0"));
        Assert.assertNotEquals(property, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = Property.newInstance(
                "socksClient.clientBindHost", Host.newInstance("0.0.0.0"));
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        Property<Object> property1 = Property.newInstance(
                "socksClient.clientBindHost", Host.newInstance("0.0.0.0"));
        Property<Object> property2 = Property.newInstance(
                "socksClient.clientBindPortRanges",
                PortRanges.newInstanceFrom("0"));
        Assert.assertNotEquals(property1, property2);
    }

    @Test
    public void testEqualsObject05() {
        Property<Object> property1 = Property.newInstance(
                "socksClient.clientBindHost", Host.newInstance("0.0.0.0"));
        Property<Object> property2 = Property.newInstance(
                "socksClient.clientBindHost", null);
        Assert.assertNotEquals(property1, property2);
    }

    @Test
    public void testEqualsObject06() {
        Property<Object> property1 = Property.newInstance(
                "socksClient.clientBindHost", null);
        Property<Object> property2 = Property.newInstance(
                "socksClient.clientBindHost", Host.newInstance("0.0.0.0"));
        Assert.assertNotEquals(property1, property2);
    }

    @Test
    public void testEqualsObject07() {
        Property<Object> property1 = Property.newInstance(
                "socksClient.clientBindHost", Host.newInstance("0.0.0.0"));
        Property<Object> property2 = Property.newInstance(
                "socksClient.clientBindHost", Host.newInstance("0.0.0.1"));
        Assert.assertNotEquals(property1, property2);
    }

    @Test
    public void testEqualsObject08() {
        Property<Object> property1 = Property.newInstance(
                "socksClient.clientBindHost", null);
        Property<Object> property2 = Property.newInstance(
                "socksClient.clientBindHost", null);
        Assert.assertEquals(property1, property2);
    }

    @Test
    public void testEqualsObject09() {
        Property<Object> property1 = Property.newInstance(
                "socksClient.clientBindHost", Host.newInstance("0.0.0.0"));
        Property<Object> property2 = Property.newInstance(
                "socksClient.clientBindHost", Host.newInstance("0.0.0.0"));
        Assert.assertEquals(property1, property2);
    }

    @Test
    public void testGetName() {
        Assert.assertEquals(
                "socksClient.clientBindHost",
                Property.newInstance(
                        "socksClient.clientBindHost",
                        Host.newInstance("0.0.0.0"))
                        .getName());
    }

    @Test
    public void testGetPropertySpec() {
        Assert.assertEquals(
                GeneralPropertySpecConstants.CLIENT_BIND_HOST,
                GeneralPropertySpecConstants.CLIENT_BIND_HOST.newProperty(
                        Host.newInstance("0.0.0.0"))
                        .getPropertySpec());
    }

    @Test
    public void testGetValue01() {
        Assert.assertNull(
                Property.newInstance(
                                "socksClient.clientBindHost",
                                null)
                        .getValue());
    }

    @Test
    public void testGetValue02() {
        Assert.assertEquals(
                Host.newInstance("0.0.0.0"),
                Property.newInstance(
                                "socksClient.clientBindHost",
                                Host.newInstance("0.0.0.0"))
                        .getValue());
    }

    @Test
    public void testHashCode01() {
        Property<Object> property1 = Property.newInstance(
                "socksClient.clientBindHost", Host.newInstance("0.0.0.0"));
        Property<Object> property2 = Property.newInstance(
                "socksClient.clientBindPortRanges",
                PortRanges.newInstanceFrom("0"));
        Assert.assertNotEquals(property1.hashCode(), property2.hashCode());
    }

    @Test
    public void testHashCode02() {
        Property<Object> property1 = Property.newInstance(
                "socksClient.clientBindHost", Host.newInstance("0.0.0.0"));
        Property<Object> property2 = Property.newInstance(
                "socksClient.clientBindHost", null);
        Assert.assertNotEquals(property1.hashCode(), property2.hashCode());
    }

    @Test
    public void testHashCode03() {
        Property<Object> property1 = Property.newInstance(
                "socksClient.clientBindHost", null);
        Property<Object> property2 = Property.newInstance(
                "socksClient.clientBindHost", Host.newInstance("0.0.0.0"));
        Assert.assertNotEquals(property1.hashCode(), property2.hashCode());
    }

    @Test
    public void testHashCode04() {
        Property<Object> property1 = Property.newInstance(
                "socksClient.clientBindHost", Host.newInstance("0.0.0.0"));
        Property<Object> property2 = Property.newInstance(
                "socksClient.clientBindHost", Host.newInstance("0.0.0.1"));
        Assert.assertNotEquals(property1.hashCode(), property2.hashCode());
    }

    @Test
    public void testHashCode05() {
        Property<Object> property1 = Property.newInstance(
                "socksClient.clientBindHost", null);
        Property<Object> property2 = Property.newInstance(
                "socksClient.clientBindHost", null);
        Assert.assertEquals(property1.hashCode(), property2.hashCode());
    }

    @Test
    public void testHashCode06() {
        Property<Object> property1 = Property.newInstance(
                "socksClient.clientBindHost", Host.newInstance("0.0.0.0"));
        Property<Object> property2 = Property.newInstance(
                "socksClient.clientBindHost", Host.newInstance("0.0.0.0"));
        Assert.assertEquals(property1.hashCode(), property2.hashCode());
    }

    @Test
    public void testToString01() {
        Assert.assertEquals(
                "socksClient.clientBindHost=null",
                Property.newInstance(
                        "socksClient.clientBindHost",
                        null)
                        .toString());
    }

    @Test
    public void testToString02() {
        Assert.assertEquals(
                "socksClient.clientBindHost=0.0.0.0",
                Property.newInstance(
                                "socksClient.clientBindHost",
                                Host.newInstance("0.0.0.0"))
                        .toString());
    }

}