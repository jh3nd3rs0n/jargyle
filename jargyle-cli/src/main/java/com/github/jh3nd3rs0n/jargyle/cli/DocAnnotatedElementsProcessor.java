package com.github.jh3nd3rs0n.jargyle.cli;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

abstract class DocAnnotatedElementsProcessor {

    public DocAnnotatedElementsProcessor() {
    }

    protected final void process(final Class<?> cls) {
        boolean classDocAnnotated = false;
        EnumValueTypeDoc enumValueTypeDoc = cls.getAnnotation(
                EnumValueTypeDoc.class);
        if (enumValueTypeDoc != null) {
            classDocAnnotated = true;
            this.processBefore(cls, enumValueTypeDoc);
            this.processEnumValueTypeDocAnnotatedClass(cls);
            this.processAfter(cls, enumValueTypeDoc);
        }
        NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc =
                cls.getAnnotation(NameValuePairValueSpecsDoc.class);
        if (nameValuePairValueSpecsDoc != null) {
            classDocAnnotated = true;
            this.processBefore(cls, nameValuePairValueSpecsDoc);
            this.processNameValuePairValueSpecsDocAnnotatedClass(cls);
            this.processAfter(cls, nameValuePairValueSpecsDoc);
        }
        NameValuePairValueTypeDoc nameValuePairValueTypeDoc =
                cls.getAnnotation(NameValuePairValueTypeDoc.class);
        if (nameValuePairValueTypeDoc != null) {
            classDocAnnotated = true;
            this.processBefore(cls, nameValuePairValueTypeDoc);
            this.process(nameValuePairValueTypeDoc);
            this.processAfter(cls, nameValuePairValueTypeDoc);
        }
        SingleValueSpecsDoc singleValueSpecsDoc = cls.getAnnotation(
                SingleValueSpecsDoc.class);
        if (singleValueSpecsDoc != null) {
            classDocAnnotated = true;
            this.processBefore(cls, singleValueSpecsDoc);
            this.processSingleValueSpecsDocAnnotatedClass(cls);
            this.processAfter(cls, singleValueSpecsDoc);
        }
        SingleValueTypeDoc singleValueTypeDoc = cls.getAnnotation(
                SingleValueTypeDoc.class);
        if (singleValueTypeDoc != null) {
            classDocAnnotated = true;
            this.processBefore(cls, singleValueTypeDoc);
            this.process(singleValueTypeDoc);
            this.processAfter(cls, singleValueTypeDoc);
        }
        ValuesValueTypeDoc valuesValueTypeDoc = cls.getAnnotation(
                ValuesValueTypeDoc.class);
        if (valuesValueTypeDoc != null) {
            classDocAnnotated = true;
            this.process(cls, valuesValueTypeDoc);
        }
        if (!classDocAnnotated) {
            this.processNonDocAnnotatedClass(cls);
        }
    }

    protected void process(
            final Class<?> cls, final ValuesValueTypeDoc valuesValueTypeDoc) {
    }

    public final void process(final Class<?>... classes) {
        this.process(Arrays.asList(classes));
    }

    protected void process(
            final Field field, final EnumValueDoc enumValueDoc) {
    }

    protected void process(
            final Field field,
            final NameValuePairValueSpecDoc nameValuePairValueSpecDoc) {
    }

    protected void process(
            final Field field, final SingleValueSpecDoc singleValueSpecDoc) {
    }

    public final void process(final List<Class<?>> classes) {
        for (Class<?> cls : classes) {
            this.process(cls);
        }
    }

    private void process(
            final NameValuePairValueTypeDoc nameValuePairValueTypeDoc) {
        this.process(nameValuePairValueTypeDoc.nameValuePairValueSpecs());
    }

    private void process(final SingleValueTypeDoc singleValueTypeDoc) {
        this.process(singleValueTypeDoc.singleValueSpecs());
    }

    protected void processAfter(
            final Class<?> cls, final EnumValueTypeDoc enumValueTypeDoc) {
    }

    protected void processAfter(
            final Class<?> cls,
            final NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc) {
    }

    protected void processAfter(
            final Class<?> cls,
            final NameValuePairValueTypeDoc nameValuePairValueTypeDoc) {
    }

    protected void processAfter(
            final Class<?> cls,
            final SingleValueSpecsDoc singleValueSpecsDoc) {
    }

    protected void processAfter(
            final Class<?> cls, final SingleValueTypeDoc singleValueTypeDoc) {
    }

    protected void processBefore(
            final Class<?> cls, final EnumValueTypeDoc enumValueTypeDoc) {
    }

    protected void processBefore(
            final Class<?> cls,
            final NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc) {
    }

    protected void processBefore(
            final Class<?> cls,
            final NameValuePairValueTypeDoc nameValuePairValueTypeDoc) {
    }

    protected void processBefore(
            final Class<?> cls,
            final SingleValueSpecsDoc singleValueSpecsDoc) {
    }

    protected void processBefore(
            final Class<?> cls, final SingleValueTypeDoc singleValueTypeDoc) {
    }

    private void processEnumValueTypeDocAnnotatedClass(final Class<?> cls) {
        for (Field field : cls.getDeclaredFields()) {
            EnumValueDoc enumValueDoc = field.getAnnotation(
                    EnumValueDoc.class);
            if (enumValueDoc != null) {
                this.process(field, enumValueDoc);
            }
        }
    }

    private void processNameValuePairValueSpecsDocAnnotatedClass(
            final Class<?> cls) {
        for (Field field : cls.getDeclaredFields()) {
            NameValuePairValueSpecDoc nameValuePairValueSpecDoc =
                    field.getAnnotation(NameValuePairValueSpecDoc.class);
            if (nameValuePairValueSpecDoc != null) {
                this.process(field, nameValuePairValueSpecDoc);
            }
        }
    }

    protected void processNonDocAnnotatedClass(final Class<?> cls) {
    }

    private void processSingleValueSpecsDocAnnotatedClass(final Class<?> cls) {
        for (Field field : cls.getDeclaredFields()) {
            SingleValueSpecDoc singleValueSpecDoc = field.getAnnotation(
                    SingleValueSpecDoc.class);
            if (singleValueSpecDoc != null) {
                this.process(field, singleValueSpecDoc);
            }
        }
    }

}
