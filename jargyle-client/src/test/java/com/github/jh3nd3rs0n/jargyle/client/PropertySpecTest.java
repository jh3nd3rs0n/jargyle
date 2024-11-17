package com.github.jh3nd3rs0n.jargyle.client;

import org.junit.Assert;
import org.junit.Test;

public class PropertySpecTest {

    @Test
    public void testEqualsObject01() {
        PropertySpec<Integer> propertySpec = new PropertySpecImpl<>(
                "Integer",
                Integer.class,
                0,
                new IntegerStringConverter(),
                null);
        Assert.assertEquals(propertySpec, propertySpec);
    }

    @Test
    public void testEqualsObject02() {
        PropertySpec<Integer> propertySpec = new PropertySpecImpl<>(
                "Integer",
                Integer.class,
                0,
                new IntegerStringConverter(),
                null);
        Assert.assertNotEquals(propertySpec, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = new PropertySpecImpl<>(
                "Integer",
                Integer.class,
                0,
                new IntegerStringConverter(),
                null);
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        PropertySpec<Integer> propertySpec1 = new PropertySpecImpl<>(
                "Integer",
                Integer.class,
                0,
                new IntegerStringConverter(),
                null);
        PropertySpec<Integer> propertySpec2 = new PropertySpecImpl<>(
                "NonNegativeInteger",
                Integer.class,
                0,
                new IntegerStringConverter(),
                new NonNegativeIntegerValidator());
        Assert.assertNotEquals(propertySpec1, propertySpec2);
    }

    @Test
    public void testEqualsObject05() {
        PropertySpec<Integer> propertySpec1 = new PropertySpecImpl<>(
                "Integer",
                Integer.class,
                0,
                new IntegerStringConverter(),
                null);
        PropertySpec<Integer> propertySpec2 = new PropertySpecImpl<>(
                "Integer",
                Integer.class,
                0,
                new IntegerStringConverter(),
                null);
        Assert.assertEquals(propertySpec1, propertySpec2);
    }

    @Test
    public void testGetDefaultProperty() {
        PropertySpec<Integer> propertySpec = new PropertySpecImpl<>(
                "Integer",
                Integer.class,
                0,
                new IntegerStringConverter(),
                null);
        Property<Integer> property1 = propertySpec.newProperty(0);
        Property<Integer> property2 = propertySpec.getDefaultProperty();
        Assert.assertEquals(property1, property2);
    }

    @Test
    public void testGetName() {
        PropertySpec<Integer> propertySpec = new PropertySpecImpl<>(
                "Integer",
                Integer.class,
                0,
                new IntegerStringConverter(),
                null);
        Assert.assertEquals("Integer", propertySpec.getName());
    }

    @Test
    public void testGetValueType() {
        PropertySpec<Integer> propertySpec = new PropertySpecImpl<>(
                "Integer",
                Integer.class,
                0,
                new IntegerStringConverter(),
                null);
        Assert.assertEquals(Integer.class, propertySpec.getValueType());
    }

    @Test
    public void testHashCode01() {
        PropertySpec<Integer> propertySpec1 = new PropertySpecImpl<>(
                "Integer",
                Integer.class,
                0,
                new IntegerStringConverter(),
                null);
        PropertySpec<Integer> propertySpec2 = new PropertySpecImpl<>(
                "NonNegativeInteger",
                Integer.class,
                0,
                new IntegerStringConverter(),
                new NonNegativeIntegerValidator());
        Assert.assertNotEquals(
                propertySpec1.hashCode(), propertySpec2.hashCode());        
    }

    @Test
    public void testHashCode02() {
        PropertySpec<Integer> propertySpec1 = new PropertySpecImpl<>(
                "Integer",
                Integer.class,
                0,
                new IntegerStringConverter(),
                null);
        PropertySpec<Integer> propertySpec2 = new PropertySpecImpl<>(
                "Integer",
                Integer.class,
                0,
                new IntegerStringConverter(),
                null);
        Assert.assertEquals(
                propertySpec1.hashCode(), propertySpec2.hashCode());        
    }

    @Test
    public void testNewPropertyValue01() {
        PropertySpec<Integer> propertySpec = new PropertySpecImpl<>(
                "Integer",
                Integer.class,
                0,
                new IntegerStringConverter(),
                null);
        Assert.assertNotNull(propertySpec.newProperty(0));
    }

    @Test
    public void testNewPropertyValue02() {
        PropertySpec<Integer> propertySpec = new PropertySpecImpl<>(
                "Integer",
                Integer.class,
                0,
                new IntegerStringConverter(),
                null);
        Assert.assertNotNull(propertySpec.newProperty(1));
    }

    @Test
    public void testNewPropertyValue03() {
        PropertySpec<Integer> propertySpec = new PropertySpecImpl<>(
                "Integer",
                Integer.class,
                0,
                new IntegerStringConverter(),
                null);
        Assert.assertNotNull(propertySpec.newProperty(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewPropertyValueForIllegalArgumentException01() {
        PropertySpec<Integer> propertySpec = new PropertySpecImpl<>(
                "NonNegativeInteger",
                Integer.class,
                0,
                new IntegerStringConverter(),
                new NonNegativeIntegerValidator());
        propertySpec.newProperty(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewPropertyValueForIllegalArgumentException02() {
        PropertySpec<Integer> propertySpec = new PropertySpecImpl<>(
                "NonNegativeInteger",
                Integer.class,
                0,
                new IntegerStringConverter(),
                new NonNegativeIntegerValidator());
        propertySpec.newProperty(-2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewPropertyValueForIllegalArgumentException03() {
        PropertySpec<Integer> propertySpec = new PropertySpecImpl<>(
                "NonNegativeInteger",
                Integer.class,
                0,
                new IntegerStringConverter(),
                new NonNegativeIntegerValidator());
        propertySpec.newProperty(-3);
    }

    @Test
    public void testNewPropertyWithParsedValueString01() {
        PropertySpec<Integer> propertySpec = new PropertySpecImpl<>(
                "Integer",
                Integer.class,
                0,
                new IntegerStringConverter(),
                null);
        Assert.assertNotNull(propertySpec.newPropertyWithParsedValue("0"));
    }

    @Test
    public void testNewPropertyWithParsedValueString02() {
        PropertySpec<Integer> propertySpec = new PropertySpecImpl<>(
                "Integer",
                Integer.class,
                0,
                new IntegerStringConverter(),
                null);
        Assert.assertNotNull(propertySpec.newPropertyWithParsedValue("1"));
    }

    @Test
    public void testNewPropertyWithParsedValueString03() {
        PropertySpec<Integer> propertySpec = new PropertySpecImpl<>(
                "Integer",
                Integer.class,
                0,
                new IntegerStringConverter(),
                null);
        Assert.assertNotNull(propertySpec.newPropertyWithParsedValue("2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewPropertyWithParsedValueStringForIllegalArgumentException01() {
        PropertySpec<Integer> propertySpec = new PropertySpecImpl<>(
                "NonNegativeInteger",
                Integer.class,
                0,
                new IntegerStringConverter(),
                new NonNegativeIntegerValidator());
        propertySpec.newPropertyWithParsedValue("localhost");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewPropertyWithParsedValueStringForIllegalArgumentException02() {
        PropertySpec<Integer> propertySpec = new PropertySpecImpl<>(
                "NonNegativeInteger",
                Integer.class,
                0,
                new IntegerStringConverter(),
                new NonNegativeIntegerValidator());
        propertySpec.newPropertyWithParsedValue("two");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewPropertyWithParsedValueStringForIllegalArgumentException03() {
        PropertySpec<Integer> propertySpec = new PropertySpecImpl<>(
                "NonNegativeInteger",
                Integer.class,
                0,
                new IntegerStringConverter(),
                new NonNegativeIntegerValidator());
        propertySpec.newPropertyWithParsedValue("-3");
    }

    interface StringConverter<T> {

        T convert(final String value);

    }

    interface Validator<T> {

        T validate(final T value);

    }

    static final class IntegerStringConverter implements StringConverter<Integer> {

        @Override
        public Integer convert(String value) {
            return Integer.valueOf(value);
        }

    }

    static final class NonNegativeIntegerValidator implements Validator<Integer> {

        @Override
        public Integer validate(Integer value) {
            if (value < 0) {
                throw new IllegalArgumentException("value must be at least 0");
            }
            return value;
        }
    }

    static final class PropertySpecImpl<V> extends PropertySpec<V> {

        private final StringConverter<V> stringConverter;
        private final Validator<V> validator;

        PropertySpecImpl(
                final String name,
                final Class<V> valType,
                final V defaultVal,
                final StringConverter<V> strConverter,
                final Validator<V> vldtr) {
            super(name, valType, defaultVal);
            this.stringConverter = strConverter;
            this.validator = vldtr;
        }

        @Override
        protected V parse(final String value) {
            return this.stringConverter.convert(value);
        }

        @Override
        protected V validate(V value) {
            return (this.validator != null) ?
                    this.validator.validate(value) : super.validate(value);
        }
    }

}