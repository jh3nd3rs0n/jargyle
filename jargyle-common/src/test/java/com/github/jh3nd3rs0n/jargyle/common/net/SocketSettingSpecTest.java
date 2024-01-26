package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import org.junit.Assert;
import org.junit.Test;

public class SocketSettingSpecTest {

    @Test
    public void testEqualsObject01() {
        SocketSettingSpec<Void> socketSettingSpec =
                new SocketSettingSpecImpl<>(
                        "Void", Void.class, new VoidStringConverter());
        Assert.assertEquals(socketSettingSpec, socketSettingSpec);
    }

    @Test
    public void testEqualsObject02() {
        Assert.assertNotEquals(
                new SocketSettingSpecImpl<>(
                        "Void", Void.class, new VoidStringConverter()),
                null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = new SocketSettingSpecImpl<>(
                "Void", Void.class, new VoidStringConverter());
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        SocketSettingSpec<?> socketSettingSpec1 =
                new SocketSettingSpecImpl<>(
                        "Void", Void.class, new VoidStringConverter());
        SocketSettingSpec<?> socketSettingSpec2 =
                new SocketSettingSpecImpl<>(
                        "Boolean",
                        Boolean.class,
                        new BooleanStringConverter());
        Assert.assertNotEquals(socketSettingSpec1, socketSettingSpec2);
    }

    @Test
    public void testEqualsObject05() {
        SocketSettingSpec<?> socketSettingSpec1 =
                new SocketSettingSpecImpl<>(
                        "Integer",
                        Integer.class,
                        new IntegerStringConverter());
        SocketSettingSpec<?> socketSettingSpec2 =
                new SocketSettingSpecImpl<>(
                        "Integer",
                        NonNegativeInteger.class,
                        new NonnegativeIntegerStringConverter());
        Assert.assertNotEquals(socketSettingSpec1, socketSettingSpec2);
    }

    @Test
    public void testEqualsObject06() {
        Assert.assertEquals(
                new SocketSettingSpecImpl<>(
                        "Void", Void.class, new VoidStringConverter()),
                new SocketSettingSpecImpl<>(
                        "Void", Void.class, new VoidStringConverter()));
    }

    @Test
    public void testHashCode01() {
        Assert.assertEquals(
                new SocketSettingSpecImpl<>(
                        "Void",
                        Void.class,
                        new VoidStringConverter()).hashCode(),
                new SocketSettingSpecImpl<>(
                        "Void",
                        Void.class,
                        new VoidStringConverter()).hashCode());
    }

    @Test
    public void testHashCode02() {
        Assert.assertNotEquals(
                new SocketSettingSpecImpl<>(
                        "Integer",
                        Integer.class,
                        new IntegerStringConverter()).hashCode(),
                new SocketSettingSpecImpl<>(
                        "Integer",
                        NonNegativeInteger.class,
                        new NonnegativeIntegerStringConverter()).hashCode());

    }

    @Test
    public void testNewSocketSettingValue() {
        SocketSettingSpec<Integer> socketSettingSpec =
                new SocketSettingSpecImpl<>(
                        "Integer",
                        Integer.class,
                        new IntegerStringConverter());
        Assert.assertNotNull(socketSettingSpec.newSocketSetting(0));
    }

    @Test
    public void testNewSocketSettingValueString01() {
        SocketSettingSpec<Integer> socketSettingSpec =
                new SocketSettingSpecImpl<>(
                        "Integer",
                        Integer.class,
                        new IntegerStringConverter());
        Assert.assertNotNull(socketSettingSpec.newSocketSetting(
                1, "This is a documentation string"));
    }

    @Test
    public void testNewSocketSettingValueString02() {
        SocketSettingSpec<Integer> socketSettingSpec =
                new SocketSettingSpecImpl<>(
                        "Integer",
                        Integer.class,
                        new IntegerStringConverter());
        Assert.assertNotNull(socketSettingSpec.newSocketSetting(
                2, "This is a documentation string"));
    }

    @Test
    public void testNewSocketSettingWithParsedValueString() {
        SocketSettingSpec<Boolean> socketSettingSpec =
                new SocketSettingSpecImpl<>(
                        "Boolean",
                        Boolean.class,
                        new BooleanStringConverter());
        Assert.assertNotNull(
                socketSettingSpec.newSocketSettingWithParsedValue(
                        "true"));
    }

    @Test
    public void testNewSocketSettingWithParsedValueStringString01() {
        SocketSettingSpec<Boolean> socketSettingSpec =
                new SocketSettingSpecImpl<>(
                        "Boolean",
                        Boolean.class,
                        new BooleanStringConverter());
        Assert.assertNotNull(
                socketSettingSpec.newSocketSettingWithParsedValue(
                        "true",
                        "This is another documentation string"));
    }

    @Test
    public void testNewSocketSettingWithParsedValueStringString02() {
        SocketSettingSpec<Boolean> socketSettingSpec =
                new SocketSettingSpecImpl<>(
                        "Boolean",
                        Boolean.class,
                        new BooleanStringConverter());
        Assert.assertNotNull(
                socketSettingSpec.newSocketSettingWithParsedValue(
                        "true",
                        null));
    }

    static final class BooleanStringConverter implements StringConverter<Boolean> {

        @Override
        public Boolean convert(String value) {
            return Boolean.valueOf(value);
        }

    }

    static final class IntegerStringConverter implements StringConverter<Integer> {

        @Override
        public Integer convert(String value) {
            return Integer.valueOf(value);
        }

    }

    static final class NonnegativeIntegerStringConverter
            implements StringConverter<NonNegativeInteger> {

        @Override
        public NonNegativeInteger convert(String value) {
            return NonNegativeInteger.valueOf(value);
        }

    }

    static final class SocketSettingSpecImpl<V> extends SocketSettingSpec<V> {

        private final StringConverter<V> stringConverter;

        SocketSettingSpecImpl(
                final String name,
                final Class<V> valType,
                final StringConverter<V> strConverter) {
            super(name, valType);
            this.stringConverter = strConverter;
        }

        @Override
        protected V parse(final String value) {
            return this.stringConverter.convert(value);
        }

    }

    interface StringConverter<T> {

        T convert(final String value);

    }

    static final class VoidStringConverter implements StringConverter<Void> {

        @Override
        public Void convert(String value) {
            return null;
        }

    }

}