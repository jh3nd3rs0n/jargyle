package com.github.jh3nd3rs0n.jargyle.cli;

import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.UserInfo;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.*;
import com.github.jh3nd3rs0n.jargyle.server.RuleAction;
import com.github.jh3nd3rs0n.jargyle.server.RuleCondition;
import com.github.jh3nd3rs0n.jargyle.server.Setting;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.*;

final class SettingsHelpPrinter {

    public SettingsHelpPrinter() {
    }

    public void printSettingsHelp(final PrintWriter pw) {
        TextWriter tw = new TextWriter(0, pw);
        tw.println("SETTINGS:");
        tw.println();
        tw.incrementIndentationCount();
        DocAnnotatedElementsProcessor docAnnotatedElementsProcessor1 =
                new DocAnnotatedElementsProcessorToNameValuePairValueSpecsText(
                        tw);
        docAnnotatedElementsProcessor1.process(
                Setting.class.getAnnotation(NameValuePairValueTypeDoc.class)
                        .nameValuePairValueSpecs());
        tw.decrementIndentationCount();
        tw.println("VALUE TYPES:");
        tw.println();
        tw.incrementIndentationCount();
        List<Class<?>> classes = new ArrayList<>(Arrays.asList(
                Setting.class.getAnnotation(NameValuePairValueTypeDoc.class)
                        .nameValuePairValueSpecs()));
        classes.add(RuleAction.class);
        classes.add(RuleCondition.class);
        classes.add(Scheme.class);
        classes.add(UserInfo.class);
        AllDocAnnotatedClassesSortedBySyntaxNameFactory factory =
                new AllDocAnnotatedClassesSortedBySyntaxNameFactory();
        List<Class<?>> allDocAnnotatedClasses =
                factory.newAllDocAnnotatedClassesSortedBySyntaxName(
                        classes);
        DocAnnotatedElementsProcessor docAnnotatedElementsProcessor2 =
                new DocAnnotatedElementsProcessorToValueTypesText(tw);
        docAnnotatedElementsProcessor2.process(allDocAnnotatedClasses);
        tw.decrementIndentationCount();
    }

    private static final class AllDocAnnotatedClassesSortedBySyntaxNameFactory {

        public AllDocAnnotatedClassesSortedBySyntaxNameFactory() {
        }

        public List<Class<?>> newAllDocAnnotatedClassesSortedBySyntaxName(
                final List<Class<?>> classes) {
            Map<String, Class<?>> docAnnotatedClassMap = new TreeMap<>(
                    String::compareToIgnoreCase);
            DocAnnotatedElementsProcessor docAnnotatedElementsProcessor =
                    new DocAnnotatedElementsProcessorToDocAnnotatedClassMap(
                            docAnnotatedClassMap);
            docAnnotatedElementsProcessor.process(classes);
            return new ArrayList<>(docAnnotatedClassMap.values());
        }

        private static final class DocAnnotatedElementsProcessorToDocAnnotatedClassMap
                extends DocAnnotatedElementsProcessor {

            private final Map<String, Class<?>> docAnnotatedClassMap;

            public DocAnnotatedElementsProcessorToDocAnnotatedClassMap(
                    final Map<String, Class<?>> docAnnotatedClsMap) {
                this.docAnnotatedClassMap = docAnnotatedClsMap;
            }

            @Override
            protected void process(
                    final Class<?> cls,
                    final ValuesValueTypeDoc valuesValueTypeDoc) {
                this.docAnnotatedClassMap.put(
                        valuesValueTypeDoc.syntaxName(), cls);
                this.process(valuesValueTypeDoc.elementValueType());
            }

            @Override
            protected void process(
                    final Field field,
                    final NameValuePairValueSpecDoc nameValuePairValueSpecDoc) {
                this.process(nameValuePairValueSpecDoc.valueType());
            }

            @Override
            protected void processBefore(
                    final Class<?> cls, final EnumValueTypeDoc enumValueTypeDoc) {
                this.docAnnotatedClassMap.put(enumValueTypeDoc.syntaxName(), cls);
            }

            @Override
            protected void processBefore(
                    final Class<?> cls,
                    final NameValuePairValueTypeDoc nameValuePairValueTypeDoc) {
                this.docAnnotatedClassMap.put(
                        nameValuePairValueTypeDoc.syntaxName(), cls);
            }

            @Override
            protected void processBefore(
                    final Class<?> cls,
                    final SingleValueTypeDoc singleValueTypeDoc) {
                this.docAnnotatedClassMap.put(singleValueTypeDoc.syntaxName(), cls);
            }

        }

    }

    private static final class DocAnnotatedElementsProcessorToNameValuePairValueSpecsText
            extends DocAnnotatedElementsProcessor {

        private final TextWriter tw;

        public DocAnnotatedElementsProcessorToNameValuePairValueSpecsText(
                final TextWriter t) {
            this.tw = t;
        }

        @Override
        protected void process(
                final Field field,
                final NameValuePairValueSpecDoc nameValuePairValueSpecDoc) {
            this.tw.printIndentations(this.tw.getIndentationCount());
            this.tw.println(nameValuePairValueSpecDoc.syntax());
            String description = nameValuePairValueSpecDoc.description();
            if (!description.isEmpty()) {
                this.tw.printIndentations(this.tw.getIndentationCount() + 2);
                this.tw.print(description);
                String defaultValue = nameValuePairValueSpecDoc.defaultValue();
                if (!defaultValue.isEmpty()) {
                    this.tw.printf(" (default value is %s)", defaultValue);
                }
                this.tw.println();
            }
            this.tw.println();
        }

        @Override
        protected void processAfter(
                final Class<?> cls,
                final NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc) {
            this.tw.decrementIndentationCount();
        }

        @Override
        protected void processBefore(
                final Class<?> cls,
                final NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc) {
            this.tw.printIndentations(this.tw.getIndentationCount());
            this.tw.printf("%s:%n",
                    nameValuePairValueSpecsDoc.name().toUpperCase());
            this.tw.println();
            this.tw.incrementIndentationCount();
        }

    }

    private static final class DocAnnotatedElementsProcessorToValueTypesText
            extends DocAnnotatedElementsProcessor {

        private final TextWriter tw;

        public DocAnnotatedElementsProcessorToValueTypesText(final TextWriter t) {
            this.tw = t;
        }

        @Override
        protected void process(
                final Class<?> cls,
                final ValuesValueTypeDoc valuesValueTypeDoc) {
            this.tw.printIndentations(this.tw.getIndentationCount());
            this.tw.printf(
                    "%s: %s%n",
                    valuesValueTypeDoc.syntaxName(),
                    valuesValueTypeDoc.syntax());
            this.tw.println();
        }

        @Override
        protected void process(
                final Field field, final EnumValueDoc enumValueDoc) {
            this.tw.printIndentations(this.tw.getIndentationCount());
            this.tw.println(enumValueDoc.value());
            String description = enumValueDoc.description();
            if (!description.isEmpty()) {
                this.tw.printIndentations(this.tw.getIndentationCount() + 2);
                this.tw.println(description);
            }
            this.tw.println();
        }

        @Override
        protected void process(
                final Field field,
                final NameValuePairValueSpecDoc nameValuePairValueSpecDoc) {
            this.tw.printIndentations(this.tw.getIndentationCount());
            this.tw.println(nameValuePairValueSpecDoc.syntax());
            String description = nameValuePairValueSpecDoc.description();
            if (!description.isEmpty()) {
                this.tw.printIndentations(this.tw.getIndentationCount() + 2);
                this.tw.print(description);
                String defaultValue = nameValuePairValueSpecDoc.defaultValue();
                if (!defaultValue.isEmpty()) {
                    this.tw.printf(" (default value is %s)", defaultValue);
                }
                this.tw.println();
            }
            this.tw.println();
        }

        @Override
        protected void process(
                final Field field,
                final SingleValueSpecDoc singleValueSpecDoc) {
            this.tw.printIndentations(this.tw.getIndentationCount());
            this.tw.println(singleValueSpecDoc.syntax());
            String description = singleValueSpecDoc.description();
            if (!description.isEmpty()) {
                this.tw.printIndentations(this.tw.getIndentationCount() + 2);
                this.tw.println(description);
            }
            this.tw.println();
        }

        @Override
        protected void processAfter(
                final Class<?> cls, final EnumValueTypeDoc enumValueTypeDoc) {
            this.tw.decrementIndentationCount();
        }

        @Override
        protected void processBefore(
                final Class<?> cls, final EnumValueTypeDoc enumValueTypeDoc) {
            this.tw.printIndentations(this.tw.getIndentationCount());
            this.tw.printf(
                    "%s: %s%n",
                    enumValueTypeDoc.syntaxName(),
                    enumValueTypeDoc.syntax());
            this.tw.println();
            this.tw.incrementIndentationCount();
        }

        @Override
        protected void processAfter(
                final Class<?> cls,
                final NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc) {
            this.tw.decrementIndentationCount();
        }

        @Override
        protected void processBefore(
                final Class<?> cls,
                final NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc) {
            this.tw.printIndentations(this.tw.getIndentationCount());
            this.tw.printf(
                    "%s:%n",
                    nameValuePairValueSpecsDoc.name().toUpperCase());
            this.tw.println();
            this.tw.incrementIndentationCount();
        }

        @Override
        protected void processAfter(
                final Class<?> cls,
                final NameValuePairValueTypeDoc nameValuePairValueTypeDoc) {
            this.tw.decrementIndentationCount();
        }

        @Override
        protected void processBefore(
                final Class<?> cls,
                final NameValuePairValueTypeDoc nameValuePairValueTypeDoc) {
            this.tw.printIndentations(this.tw.getIndentationCount());
            this.tw.printf(
                    "%s: %s%n",
                    nameValuePairValueTypeDoc.syntaxName(),
                    nameValuePairValueTypeDoc.syntax());
            this.tw.println();
            this.tw.incrementIndentationCount();
        }

        @Override
        protected void processAfter(
                final Class<?> cls,
                final SingleValueSpecsDoc singleValueSpecsDoc) {
            this.tw.decrementIndentationCount();
        }

        @Override
        protected void processBefore(
                final Class<?> cls,
                final SingleValueSpecsDoc singleValueSpecsDoc) {
            this.tw.printIndentations(this.tw.getIndentationCount());
            this.tw.printf(
                    "%s:%n",
                    singleValueSpecsDoc.name().toUpperCase());
            this.tw.println();
            this.tw.incrementIndentationCount();
        }

        @Override
        protected void processAfter(
                final Class<?> cls,
                final SingleValueTypeDoc singleValueTypeDoc) {
            this.tw.decrementIndentationCount();
        }

        @Override
        protected void processBefore(
                final Class<?> cls,
                final SingleValueTypeDoc singleValueTypeDoc) {
            this.tw.printIndentations(this.tw.getIndentationCount());
            this.tw.printf(
                    "%s: %s%n",
                    singleValueTypeDoc.syntaxName(),
                    singleValueTypeDoc.syntax());
            this.tw.println();
            this.tw.incrementIndentationCount();
        }

    }

    private static final class TextWriter {

        private static final String INDENTATION = "  ";
        private final PrintWriter pw;
        private int indentationCount;

        public TextWriter(final int indentCount, final PrintWriter p) {
            this.indentationCount = indentCount;
            this.pw = p;
        }

        public void decrementIndentationCount() {
            this.indentationCount--;
        }

        public int getIndentationCount() {
            return this.indentationCount;
        }

        public void incrementIndentationCount() {
            this.indentationCount++;
        }

        public void print(final String s) {
            this.pw.print(s);
        }

        public void printf(final String format, final Object... args) {
            this.pw.printf(format, args);
        }

        public void printIndentations(final int count) {
            for (int i = 0; i < count; i++) {
                this.pw.print(INDENTATION);
            }
        }
        public void println() {
            this.pw.println();
        }

        public void println(final String s) {
            this.pw.println(s);
        }

    }

}
