package com.github.jh3nd3rs0n.jargyle.common.string;

import org.junit.Assert;
import org.junit.Test;

public class CommaSeparatedValuesTest {

    @Test
    public void testNewInstanceFromString01() {
        Assert.assertNotNull(CommaSeparatedValues.newInstanceFrom(""));
    }

    @Test
    public void testNewInstanceFromString02() {
        Assert.assertNotNull(
                CommaSeparatedValues.newInstanceFrom("Tom"));
    }

    @Test
    public void testNewInstanceFromString03() {
        Assert.assertNotNull(
                CommaSeparatedValues.newInstanceFrom("Tom,Dick,Harry"));
    }

    @Test
    public void testOfStringVarargs01() {
        Assert.assertNotNull(CommaSeparatedValues.of());
    }

    @Test
    public void testOfStringVarargs02() {
        Assert.assertNotNull(CommaSeparatedValues.of("Tom"));
    }

    @Test
    public void testOfStringVarargs03() {
        Assert.assertNotNull(CommaSeparatedValues.of("Tom", "Dick", "Harry"));
    }

    @Test
    public void testEqualsObject01() {
        CommaSeparatedValues values = CommaSeparatedValues.newInstanceFrom(
                "Tom,Dick,Harry");
        Assert.assertEquals(values, values);
    }

    @Test
    public void testEqualsObject02() {
        CommaSeparatedValues values = CommaSeparatedValues.newInstanceFrom(
                "Tom,Dick,Harry");
        Assert.assertNotEquals(values, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = CommaSeparatedValues.newInstanceFrom(
                "Tom,Dick,Harry");
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        CommaSeparatedValues values1 = CommaSeparatedValues.newInstanceFrom(
                "Tom,Dick,Harry");
        CommaSeparatedValues values2 = CommaSeparatedValues.newInstanceFrom(
                "Larry,Curly,Moe");
        Assert.assertNotEquals(values1, values2);
    }

    @Test
    public void testEqualsObject05() {
        CommaSeparatedValues values1 = CommaSeparatedValues.newInstanceFrom(
                "Tom,Dick,Harry");
        CommaSeparatedValues values2 = CommaSeparatedValues.newInstanceFrom(
                "Tom,Dick,Harry");
        Assert.assertEquals(values1, values2);
    }

    @Test
    public void testHashCode01() {
        CommaSeparatedValues values1 = CommaSeparatedValues.newInstanceFrom(
                "Tom,Dick,Harry");
        CommaSeparatedValues values2 = CommaSeparatedValues.newInstanceFrom(
                "Tom,Dick,Harry");
        Assert.assertEquals(values1.hashCode(), values2.hashCode());
    }

    @Test
    public void testHashCode02() {
        CommaSeparatedValues values1 = CommaSeparatedValues.newInstanceFrom(
                "Tom,Dick,Harry");
        CommaSeparatedValues values2 = CommaSeparatedValues.newInstanceFrom(
                "Larry,Curly,Moe");
        Assert.assertNotEquals(values1.hashCode(), values2.hashCode());
    }

    @Test
    public void testToArray01() {
        CommaSeparatedValues values = CommaSeparatedValues.newInstanceFrom("");
        Assert.assertArrayEquals(new String[]{}, values.toArray());
    }

    @Test
    public void testToArray02() {
        CommaSeparatedValues values = CommaSeparatedValues.newInstanceFrom(
                "Tom");
        Assert.assertArrayEquals(new String[]{"Tom"}, values.toArray());
    }

    @Test
    public void testToArray03() {
        CommaSeparatedValues values = CommaSeparatedValues.newInstanceFrom(
                "Tom,Dick,Harry");
        Assert.assertArrayEquals(
                new String[]{"Tom", "Dick", "Harry"}, values.toArray());
    }

    @Test
    public void testToString01() {
        CommaSeparatedValues values = CommaSeparatedValues.of();
        Assert.assertEquals("", values.toString());
    }

    @Test
    public void testToString02() {
        CommaSeparatedValues values = CommaSeparatedValues.of("Tom");
        Assert.assertEquals("Tom", values.toString());
    }

    @Test
    public void testToString03() {
        CommaSeparatedValues values = CommaSeparatedValues.of(
                "Tom", "Dick", "Harry");
        Assert.assertEquals("Tom,Dick,Harry", values.toString());
    }

}