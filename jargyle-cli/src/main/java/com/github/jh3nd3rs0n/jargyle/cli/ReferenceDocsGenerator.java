package com.github.jh3nd3rs0n.jargyle.cli;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.UserInfo;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.*;
import com.github.jh3nd3rs0n.jargyle.server.ConfigurationSchema;
import com.github.jh3nd3rs0n.jargyle.server.RuleCondition;
import com.github.jh3nd3rs0n.jargyle.server.RuleAction;
import com.github.jh3nd3rs0n.jargyle.server.Setting;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.*;

final class ReferenceDocsGenerator {

    private static final String CLIENT_PROPERTIES_FILENAME =
            "client-properties.md";
    private static final String CLI_HELP_INFO_FILENAME =
            "cli-help-info.md";
    private static final String RULE_ACTIONS_FILENAME =
            "rule-actions.md";
    private static final String RULE_CONDITIONS_FILENAME =
            "rule-conditions.md";
    private static final String SERVER_CONFIGURATION_FILE_SCHEMA_FILENAME =
            "server-configuration-file-schema.md";
    private static final String SERVER_CONFIGURATION_SETTINGS_FILENAME =
            "server-configuration-settings.md";
    private static final String VALUE_TYPES_FILENAME =
            "value-types.md";

    public ReferenceDocsGenerator() {
    }

    public void generateReferenceDocs(
            final File destinationDirectory) throws IOException {
        if (destinationDirectory != null) {
            if (!destinationDirectory.exists()) {
                throw new IllegalArgumentException(String.format(
                        "`%s' not found",
                        destinationDirectory));
            }
            if (!destinationDirectory.isDirectory()) {
                throw new IllegalArgumentException(String.format(
                        "`%s' not a directory",
                        destinationDirectory));
            }
        }
        Path destinationDir = (destinationDirectory == null) ?
                new File("").toPath() : destinationDirectory.toPath();
        this.writeClientProperties(destinationDir);
        this.writeCliHelpInfo(destinationDir);
        this.writeRuleActions(destinationDir);
        this.writeRuleConditions(destinationDir);
        this.writeServerConfigurationFileSchema(destinationDir);
        this.writeServerConfigurationSettings(destinationDir);
        this.writeValueTypes(destinationDir);
    }

    private void writeClientProperties(
            final Path destinationDir) throws IOException {
        String pathString = destinationDir.resolve(
                CLIENT_PROPERTIES_FILENAME).toString();
        System.out.printf("Creating '%s'...", pathString);
        try (PrintWriter pw = new PrintWriter(pathString, "UTF-8")) {
            MarkdownWriter mw = new MarkdownWriter(pw);
            mw.printHeader(1, "Client Properties");
            mw.println();
            mw.println();
            this.writeNameValuePairValueSpecs(
                    Arrays.asList(
                            Property.class.getAnnotation(
                                            NameValuePairValueTypeDoc.class)
                                    .nameValuePairValueSpecs()),
                    mw);
        }
        System.out.println("Done.");
    }

    private void writeCliHelpInfo(
            final Path destinationDir) throws IOException {
        String pathString = destinationDir.resolve(
                CLI_HELP_INFO_FILENAME).toString();
        System.out.printf("Creating '%s'...", pathString);
        try (PrintWriter pw = new PrintWriter(pathString, "UTF-8")) {
            MarkdownWriter mw = new MarkdownWriter(pw);
            mw.printHeader(1, "Command Line Interface Help Information");
            mw.println();
            mw.println();
            mw.printHeader(2, "Page Contents");
            mw.println();
            mw.println();
            mw.printUnorderedListItemStart();
            mw.printLinkToHeader("Help Information");
            mw.println();
            mw.incrementIndentationCount();
            mw.printIndentations();
            mw.printUnorderedListItemStart();
            mw.printLinkToHeader(
                    "Help Information for generate-reference-docs");
            mw.println();
            mw.printIndentations();
            mw.printUnorderedListItemStart();
            mw.printLinkToHeader(
                    "Help Information for manage-socks5-users");
            mw.println();
            mw.printIndentations();
            mw.printUnorderedListItemStart();
            mw.printLinkToHeader(
                    "Help Information for new-server-config-file");
            mw.println();
            mw.printIndentations();
            mw.printUnorderedListItemStart();
            mw.printLinkToHeader("Help Information for start-server");
            mw.println();
            mw.printIndentations();
            mw.printUnorderedListItemStart();
            mw.printLinkToHeader("Settings Help Information");
            mw.println();
            mw.decrementIndentationCount();
            mw.println();
            mw.printHeader(2, "Help Information");
            mw.println();
            mw.println();
            mw.printPreformattedTextStart("text");
            mw.println();
            try (StringWriter stringWriter = new StringWriter();
                 PrintWriter printWriter = new PrintWriter(stringWriter)) {
                new JargyleCLI(
                        "jargyle",
                        "jargyle",
                        new String[]{"--help"},
                        false).printProgramHelp(printWriter);
                printWriter.flush();
                mw.print(stringWriter.toString());
            }
            mw.printPreformattedTextEnd();
            mw.println();
            mw.println();
            mw.printHeader(3, "Help Information for generate-reference-docs");
            mw.println();
            mw.println();
            mw.printPreformattedTextStart("text");
            mw.println();
            try (StringWriter stringWriter = new StringWriter();
                 PrintWriter printWriter = new PrintWriter(stringWriter)) {
                new ReferenceDocsGeneratorCLI(
                        "generate-reference-docs",
                        "jargyle generate-reference-docs",
                        new String[]{"--help"},
                        false).printProgramHelp(printWriter);
                printWriter.flush();
                mw.print(stringWriter.toString());
            }
            mw.printPreformattedTextEnd();
            mw.println();
            mw.println();
            mw.printHeader(3, "Help Information for manage-socks5-users");
            mw.println();
            mw.println();
            mw.printPreformattedTextStart("text");
            mw.println();
            try (StringWriter stringWriter = new StringWriter();
                 PrintWriter printWriter = new PrintWriter(stringWriter)) {
                new Socks5UserManagerCLI(
                        "manage-socks5-users",
                        "jargyle manage-socks5-users",
                        new String[]{"--help"},
                        false).printProgramHelp(printWriter);
                printWriter.flush();
                mw.print(stringWriter.toString());
            }
            mw.printPreformattedTextEnd();
            mw.println();
            mw.println();
            mw.printHeader(3, "Help Information for new-server-config-file");
            mw.println();
            mw.println();
            mw.printPreformattedTextStart("text");
            mw.println();
            try (StringWriter stringWriter = new StringWriter();
                 PrintWriter printWriter = new PrintWriter(stringWriter)) {
                new ServerConfigurationFileCreatorCLI(
                        "new-server-config-file",
                        "jargyle new-server-config-file",
                        new String[]{"--help"},
                        false).printProgramHelp(printWriter);
                printWriter.flush();
                mw.print(stringWriter.toString());
            }
            mw.printPreformattedTextEnd();
            mw.println();
            mw.println();
            mw.printHeader(3, "Help Information for start-server");
            mw.println();
            mw.println();
            mw.printPreformattedTextStart("text");
            mw.println();
            try (StringWriter stringWriter = new StringWriter();
                 PrintWriter printWriter = new PrintWriter(stringWriter)) {
                new ServerStarterCLI(
                        "start-server",
                        "jargyle start-server",
                        new String[]{"--help"},
                        false).printProgramHelp(printWriter);
                printWriter.flush();
                mw.print(stringWriter.toString());
            }
            mw.printPreformattedTextEnd();
            mw.println();
            mw.println();
            mw.printHeader(3, "Settings Help Information");
            mw.println();
            mw.println();
            mw.printPreformattedTextStart("text");
            mw.println();
            try (StringWriter stringWriter = new StringWriter();
                 PrintWriter printWriter = new PrintWriter(stringWriter)) {
                new SettingsHelpPrinter().printSettingsHelp(printWriter);
                printWriter.flush();
                mw.print(stringWriter.toString());
            }
            mw.printPreformattedTextEnd();
            mw.println();
            mw.println();
        }
        System.out.println("Done.");
    }

    private void writeNameValuePairValueSpecs(
            final List<Class<?>> nameValuePairValueSpecsDocAnnotatedClasses,
            final MarkdownWriter mw) {
        List<NameValuePairValueSpecDoc> nameValuePairValueSpecDocs =
                new ArrayList<>();
        ValueTypeNameMapFactory valueTypeNameMapFactory =
                new ValueTypeNameMapFactory();
        Map<Class<?>, String> valueTypeNameMap =
                valueTypeNameMapFactory.newValueTypeNameMap(
                        nameValuePairValueSpecsDocAnnotatedClasses);
        DocAnnotatedElementsProcessor docAnnotatedElementsProcessor =
                new DocAnnotatedElementsProcessorToNameValuePairValueSpecsLinkTablesMarkdown(
                        mw, nameValuePairValueSpecDocs, valueTypeNameMap);
        docAnnotatedElementsProcessor.process(
                nameValuePairValueSpecsDocAnnotatedClasses);
        nameValuePairValueSpecDocs.sort(Comparator.comparing(
                NameValuePairValueSpecDoc::name));
        for (NameValuePairValueSpecDoc nameValuePairValueSpecDoc :
                nameValuePairValueSpecDocs) {
            mw.printHeader(3, nameValuePairValueSpecDoc.name());
            mw.println();
            mw.println();
            mw.printBoldText("Description:");
            mw.print(" ");
            mw.println(nameValuePairValueSpecDoc.description());
            mw.println();
            mw.printBoldText("Value Type:");
            mw.print(" ");
            mw.printLinkToHeader(
                    valueTypeNameMap.get(
                            nameValuePairValueSpecDoc.valueType()),
                    VALUE_TYPES_FILENAME);
            mw.println();
            mw.println();
            String defaultValue = nameValuePairValueSpecDoc.defaultValue();
            if (!defaultValue.isEmpty()) {
                mw.printBoldText("Default Value:");
                mw.print(" ");
                mw.printCodeText(defaultValue);
                mw.println();
                mw.println();
            }
        }
    }

    private void writeRuleActions(
            final Path destinationDir) throws IOException {
        String pathString = destinationDir.resolve(
                RULE_ACTIONS_FILENAME).toString();
        System.out.printf("Creating '%s'...", pathString);
        try (PrintWriter pw = new PrintWriter(pathString, "UTF-8")) {
            MarkdownWriter mw = new MarkdownWriter(pw);
            mw.printHeader(1, "Rule Actions");
            mw.println();
            mw.println();
            this.writeNameValuePairValueSpecs(
                    Arrays.asList(
                            RuleAction.class.getAnnotation(
                                    NameValuePairValueTypeDoc.class)
                            .nameValuePairValueSpecs()),
                    mw);
        }
        System.out.println("Done.");
    }

    private void writeRuleConditions(
            final Path destinationDir) throws IOException {
        String pathString = destinationDir.resolve(
                RULE_CONDITIONS_FILENAME).toString();
        System.out.printf("Creating '%s'...", pathString);
        try (PrintWriter pw = new PrintWriter(pathString, "UTF-8")) {
            MarkdownWriter mw = new MarkdownWriter(pw);
            mw.printHeader(1, "Rule Conditions");
            mw.println();
            mw.println();
            this.writeNameValuePairValueSpecs(
                    Arrays.asList(
                            RuleCondition.class.getAnnotation(
                                            NameValuePairValueTypeDoc.class)
                                    .nameValuePairValueSpecs()),
                    mw);
        }
        System.out.println("Done.");
    }

    private void writeServerConfigurationFileSchema(
            final Path destinationDir) throws IOException {
        String pathString = destinationDir.resolve(
                SERVER_CONFIGURATION_FILE_SCHEMA_FILENAME).toString();
        System.out.printf("Creating '%s'...", pathString);
        try (PrintWriter pw = new PrintWriter(pathString, "UTF-8")) {
            MarkdownWriter mw = new MarkdownWriter(pw);
            mw.printHeader(1, "Server Configuration File Schema");
            mw.println();
            mw.println();
            mw.printPreformattedTextStart("xml");
            mw.println();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ConfigurationSchema.newGeneratedInstance().toOutput(out);
            Reader reader = new InputStreamReader(new ByteArrayInputStream(
                    out.toByteArray()));
            int ch;
            while ((ch = reader.read()) != -1) {
                mw.print((char) ch);
            }
            mw.printPreformattedTextEnd();
            mw.println();
            mw.println();
        }
        System.out.println("Done.");
    }

    private void writeServerConfigurationSettings(
            final Path destinationDir) throws IOException {
        String pathString = destinationDir.resolve(
                SERVER_CONFIGURATION_SETTINGS_FILENAME).toString();
        System.out.printf("Creating '%s'...", pathString);
        try (PrintWriter pw = new PrintWriter(pathString, "UTF-8")) {
            MarkdownWriter mw = new MarkdownWriter(pw);
            mw.printHeader(1, "Server Configuration Settings");
            mw.println();
            mw.println();
            this.writeNameValuePairValueSpecs(
                    Arrays.asList(
                            Setting.class.getAnnotation(
                                            NameValuePairValueTypeDoc.class)
                                    .nameValuePairValueSpecs()),
                    mw);
        }
        System.out.println("Done.");
    }

    private void writeValueTypes(
            final Path destinationDir) throws IOException {
        List<Class<?>> classes = new ArrayList<>();
        classes.addAll(Arrays.asList(
                Property.class.getAnnotation(NameValuePairValueTypeDoc.class)
                        .nameValuePairValueSpecs()));
        classes.addAll(Arrays.asList(
                RuleAction.class.getAnnotation(NameValuePairValueTypeDoc.class)
                        .nameValuePairValueSpecs()));
        classes.addAll(Arrays.asList(
                RuleCondition.class.getAnnotation(
                                NameValuePairValueTypeDoc.class)
                        .nameValuePairValueSpecs()));
        classes.addAll(Arrays.asList(
                Setting.class.getAnnotation(NameValuePairValueTypeDoc.class)
                        .nameValuePairValueSpecs()));
        classes.add(Scheme.class);
        classes.add(UserInfo.class);
        AllDocAnnotatedClassesSortedByNameFactory factory =
                new AllDocAnnotatedClassesSortedByNameFactory();
        List<Class<?>> allDocAnnotatedClasses =
                factory.newAllDocAnnotatedClassesSortedByName(classes);
        String pathString = destinationDir.resolve(
                VALUE_TYPES_FILENAME).toString();
        System.out.printf("Creating '%s'...", pathString);
        try (PrintWriter pw = new PrintWriter(pathString, "UTF-8")) {
            MarkdownWriter mw = new MarkdownWriter(pw);
            mw.printHeader(1, "Value Types");
            mw.println();
            mw.println();
            mw.printHeader(2, "Page Contents");
            mw.println();
            mw.println();
            DocAnnotatedElementsProcessor docAnnotatedElementsProcessor1 =
                    new DocAnnotatedElementsProcessorToValueTypeLinkListMarkdown(
                            mw);
            docAnnotatedElementsProcessor1.process(allDocAnnotatedClasses);
            mw.println();
            ValueTypeNameMapFactory valueTypeNameMapFactory =
                    new ValueTypeNameMapFactory();
            Map<Class<?>, String> valueTypeNameMap =
                    valueTypeNameMapFactory.newValueTypeNameMap(
                            allDocAnnotatedClasses);
            DocAnnotatedElementsProcessor docAnnotatedElementsProcessor2 =
                    new DocAnnotatedElementsProcessorToValueTypesMarkdown(
                            mw, valueTypeNameMap, 2);
            docAnnotatedElementsProcessor2.process(allDocAnnotatedClasses);
        }
        System.out.println("Done.");
    }

    private static final class AllDocAnnotatedClassesSortedByNameFactory {

        public AllDocAnnotatedClassesSortedByNameFactory() {
        }

        public List<Class<?>> newAllDocAnnotatedClassesSortedByName(
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
                this.docAnnotatedClassMap.put(valuesValueTypeDoc.name(), cls);
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
                this.docAnnotatedClassMap.put(enumValueTypeDoc.name(), cls);
            }

            @Override
            protected void processBefore(
                    final Class<?> cls,
                    final NameValuePairValueTypeDoc nameValuePairValueTypeDoc) {
                this.docAnnotatedClassMap.put(
                        nameValuePairValueTypeDoc.name(), cls);
            }

            @Override
            protected void processBefore(
                    final Class<?> cls,
                    final SingleValueTypeDoc singleValueTypeDoc) {
                this.docAnnotatedClassMap.put(singleValueTypeDoc.name(), cls);
            }

            @Override
            protected void processNonDocAnnotatedClass(final Class<?> cls) {
                this.docAnnotatedClassMap.put(cls.getSimpleName(), cls);
            }

        }

    }

    private static final class DocAnnotatedElementsProcessorToNameValuePairValueSpecsLinkTablesMarkdown
            extends DocAnnotatedElementsProcessor {

        private final MarkdownWriter mw;
        private final List<NameValuePairValueSpecDoc> nameValuePairValueSpecDocs;
        private final Map<Class<?>, String> valueTypeNameMap;

        public DocAnnotatedElementsProcessorToNameValuePairValueSpecsLinkTablesMarkdown(
                final MarkdownWriter m,
                final List<NameValuePairValueSpecDoc> docs,
                final Map<Class<?>, String> nameMap) {
            this.mw = m;
            this.nameValuePairValueSpecDocs = docs;
            this.valueTypeNameMap = nameMap;
        }

        @Override
        protected void process(
                final Field field,
                final NameValuePairValueSpecDoc nameValuePairValueSpecDoc) {
            String name = nameValuePairValueSpecDoc.name();
            String valueTypeName = this.valueTypeNameMap.get(
                    nameValuePairValueSpecDoc.valueType());
            String description = nameValuePairValueSpecDoc.description();
            String defaultValue = nameValuePairValueSpecDoc.defaultValue();
            if (!defaultValue.isEmpty()) {
                description = description.concat(String.format(
                        "<br/><b>Default Value:</b> <code>%s</code>",
                        defaultValue));
            }
            this.mw.printf(
                    "<tr><td><a href=\"#%s\"><code>%s</code></a></td><td>%s</td><td>%s</td></tr>%n",
                    MarkdownWriter.toId(name),
                    name,
                    valueTypeName,
                    description);
            this.nameValuePairValueSpecDocs.add(nameValuePairValueSpecDoc);
        }

        @Override
        protected void processAfter(
                final Class<?> cls,
                final NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc) {
            this.mw.println("</table>");
            this.mw.println();
        }

        @Override
        protected void processBefore(
                final Class<?> cls,
                final NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc) {
            this.mw.printHeader(2, nameValuePairValueSpecsDoc.name());
            this.mw.println();
            this.mw.println();
            this.mw.println("<table>");
            this.mw.println("<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>");
        }

    }

    private static final class DocAnnotatedElementsProcessorToValueTypeLinkListMarkdown
            extends DocAnnotatedElementsProcessor {

        private final MarkdownWriter mw;

        public DocAnnotatedElementsProcessorToValueTypeLinkListMarkdown(
                final MarkdownWriter m) {
            this.mw = m;
        }

        @Override
        protected void process(
                final Class<?> cls,
                final ValuesValueTypeDoc valuesValueTypeDoc) {
            this.mw.printIndentations();
            this.mw.printUnorderedListItemStart();
            this.mw.printLinkToHeader(valuesValueTypeDoc.name());
            this.mw.println();
        }

        @Override
        protected void process(
                final Field field,
                final NameValuePairValueSpecDoc nameValuePairValueSpecDoc) {
            this.mw.printIndentations();
            this.mw.printUnorderedListItemStart();
            this.mw.printLinkToHeader(nameValuePairValueSpecDoc.name());
            this.mw.println();
        }

        @Override
        protected void process(
                final Field field,
                final SingleValueSpecDoc singleValueSpecDoc) {
            this.mw.printIndentations();
            this.mw.printUnorderedListItemStart();
            this.mw.printLinkToHeader(singleValueSpecDoc.name());
            this.mw.println();
        }

        @Override
        protected void processBefore(
                final Class<?> cls, final EnumValueTypeDoc enumValueTypeDoc) {
            this.mw.printIndentations();
            this.mw.printUnorderedListItemStart();
            this.mw.printLinkToHeader(enumValueTypeDoc.name());
            this.mw.println();
        }

        @Override
        protected void processAfter(
                final Class<?> cls,
                final NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc) {
            this.mw.decrementIndentationCount();
        }

        @Override
        protected void processBefore(
                final Class<?> cls,
                final NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc) {
            this.mw.printIndentations();
            this.mw.printUnorderedListItemStart();
            this.mw.printLinkToHeader(nameValuePairValueSpecsDoc.name());
            this.mw.println();
            this.mw.incrementIndentationCount();
        }

        @Override
        protected void processAfter(
                final Class<?> cls,
                final NameValuePairValueTypeDoc nameValuePairValueTypeDoc) {
            this.mw.decrementIndentationCount();
        }

        @Override
        protected void processBefore(
                final Class<?> cls,
                final NameValuePairValueTypeDoc nameValuePairValueTypeDoc) {
            this.mw.printIndentations();
            this.mw.printUnorderedListItemStart();
            this.mw.printLinkToHeader(nameValuePairValueTypeDoc.name());
            this.mw.println();
            this.mw.incrementIndentationCount();
        }

        @Override
        protected void processAfter(
                final Class<?> cls,
                final SingleValueSpecsDoc singleValueSpecsDoc) {
            this.mw.decrementIndentationCount();
        }

        @Override
        protected void processBefore(
                final Class<?> cls,
                final SingleValueSpecsDoc singleValueSpecsDoc) {
            this.mw.printIndentations();
            this.mw.printUnorderedListItemStart();
            this.mw.printLinkToHeader(singleValueSpecsDoc.name());
            this.mw.println();
            this.mw.incrementIndentationCount();
        }

        @Override
        protected void processAfter(
                final Class<?> cls,
                final SingleValueTypeDoc singleValueTypeDoc) {
            this.mw.decrementIndentationCount();
        }

        @Override
        protected void processBefore(
                final Class<?> cls,
                final SingleValueTypeDoc singleValueTypeDoc) {
            this.mw.printIndentations();
            this.mw.printUnorderedListItemStart();
            this.mw.printLinkToHeader(singleValueTypeDoc.name());
            this.mw.println();
            this.mw.incrementIndentationCount();
        }

        @Override
        protected void processNonDocAnnotatedClass(final Class<?> cls) {
            this.mw.printIndentations();
            this.mw.printUnorderedListItemStart();
            this.mw.printLinkToHeader(cls.getSimpleName());
            this.mw.println();
        }

    }

    private static final class DocAnnotatedElementsProcessorToValueTypesMarkdown
            extends DocAnnotatedElementsProcessor {

        private final MarkdownWriter mw;
        private final Map<Class<?>, String> valueTypeNameMap;
        private int headingLevel;

        public DocAnnotatedElementsProcessorToValueTypesMarkdown(
                final MarkdownWriter m,
                final Map<Class<?>, String> valTypeNameMap,
                final int headerLevel) {
            this.mw = m;
            this.valueTypeNameMap = valTypeNameMap;
            this.headingLevel = headerLevel;
        }

        @Override
        protected void process(
                final Class<?> cls,
                final ValuesValueTypeDoc valuesValueTypeDoc) {
            this.mw.printHeader(this.headingLevel, valuesValueTypeDoc.name());
            this.mw.println();
            this.mw.println();
            this.mw.printBoldText("Syntax:");
            this.mw.println();
            this.mw.println();
            this.mw.printPreformattedTextStart("text");
            this.mw.println();
            this.mw.println(valuesValueTypeDoc.syntax());
            this.mw.printPreformattedTextEnd();
            this.mw.println();
            this.mw.println();
            this.mw.printBoldText("Description:");
            this.mw.print(" ");
            this.mw.println(valuesValueTypeDoc.description());
            this.mw.println();
            this.mw.printBoldText("Element Value Type:");
            this.mw.print(" ");
            this.mw.printLinkToHeader(this.valueTypeNameMap.get(
                    valuesValueTypeDoc.elementValueType()));
            this.mw.println();
            this.mw.println();
            this.mw.printBoldText("Class:");
            this.mw.print(" ");
            this.mw.printCodeText(cls.getName());
            this.mw.println();
            this.mw.println();
        }

        @Override
        protected void process(
                final Field field, final EnumValueDoc enumValueDoc) {
            this.mw.printUnorderedListItemStart();
            this.mw.printCodeText(enumValueDoc.value());
            this.mw.print(" : ");
            this.mw.println(enumValueDoc.description());
        }

        @Override
        protected void process(
                final Field field,
                final NameValuePairValueSpecDoc nameValuePairValueSpecDoc) {
            this.mw.printHeader(
                    this.headingLevel, nameValuePairValueSpecDoc.name());
            this.mw.println();
            this.mw.println();
            this.mw.printBoldText("Syntax:");
            this.mw.println();
            this.mw.println();
            this.mw.printPreformattedTextStart("text");
            this.mw.println();
            this.mw.println(nameValuePairValueSpecDoc.syntax());
            this.mw.printPreformattedTextEnd();
            this.mw.println();
            this.mw.println();
            this.mw.printBoldText("Description:");
            this.mw.print(" ");
            this.mw.println(nameValuePairValueSpecDoc.description());
            this.mw.println();
            this.mw.printBoldText("Value Type:");
            this.mw.print(" ");
            this.mw.printLinkToHeader(this.valueTypeNameMap.get(
                    nameValuePairValueSpecDoc.valueType()));
            this.mw.println();
            this.mw.println();
            String defaultValue = nameValuePairValueSpecDoc.defaultValue();
            if (!defaultValue.isEmpty()) {
                this.mw.printBoldText("Default Value:");
                this.mw.print(" ");
                this.mw.printCodeText(defaultValue);
                this.mw.println();
                this.mw.println();
            }
        }

        @Override
        protected void process(
                final Field field,
                final SingleValueSpecDoc singleValueSpecDoc) {
            this.mw.printHeader(
                    this.headingLevel, singleValueSpecDoc.name());
            this.mw.println();
            this.mw.println();
            this.mw.printBoldText("Syntax:");
            this.mw.println();
            this.mw.println();
            this.mw.printPreformattedTextStart("text");
            this.mw.println();
            this.mw.println(singleValueSpecDoc.syntax());
            this.mw.printPreformattedTextEnd();
            this.mw.println();
            this.mw.println();
            this.mw.printBoldText("Description:");
            this.mw.print(" ");
            this.mw.println(singleValueSpecDoc.description());
            this.mw.println();
        }

        @Override
        protected void processAfter(
                final Class<?> cls, final EnumValueTypeDoc enumValueTypeDoc) {
            this.mw.println();
            this.mw.printBoldText("Class:");
            this.mw.print(" ");
            this.mw.printCodeText(cls.getName());
            this.mw.println();
            this.mw.println();
        }

        @Override
        protected void processBefore(
                final Class<?> cls, final EnumValueTypeDoc enumValueTypeDoc) {
            this.mw.printHeader(
                    this.headingLevel, enumValueTypeDoc.name());
            this.mw.println();
            this.mw.println();
            this.mw.printBoldText("Syntax:");
            this.mw.println();
            this.mw.println();
            this.mw.printPreformattedTextStart("text");
            this.mw.println();
            this.mw.println(enumValueTypeDoc.syntax());
            this.mw.printPreformattedTextEnd();
            this.mw.println();
            this.mw.println();
            this.mw.printBoldText("Description:");
            this.mw.print(" ");
            this.mw.println(enumValueTypeDoc.description());
            this.mw.println();
            this.mw.printBoldText("Values:");
            this.mw.println();
            this.mw.println();
        }

        @Override
        protected void processAfter(
                final Class<?> cls,
                final NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc) {
            this.headingLevel--;
        }

        @Override
        protected void processBefore(
                final Class<?> cls,
                final NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc) {
            this.mw.printHeader(
                    this.headingLevel, nameValuePairValueSpecsDoc.name());
            this.mw.println();
            this.mw.println();
            this.mw.printBoldText("Description:");
            this.mw.print(" ");
            this.mw.println(nameValuePairValueSpecsDoc.description());
            this.mw.println();
            this.headingLevel++;
        }

        @Override
        protected void processAfter(
                final Class<?> cls,
                final NameValuePairValueTypeDoc nameValuePairValueTypeDoc) {
            this.headingLevel--;
        }

        @Override
        protected void processBefore(
                final Class<?> cls,
                final NameValuePairValueTypeDoc nameValuePairValueTypeDoc) {
            this.mw.printHeader(
                    this.headingLevel, nameValuePairValueTypeDoc.name());
            this.mw.println();
            this.mw.println();
            this.mw.printBoldText("Syntax:");
            this.mw.println();
            this.mw.println();
            this.mw.printPreformattedTextStart("text");
            this.mw.println();
            this.mw.println(nameValuePairValueTypeDoc.syntax());
            this.mw.printPreformattedTextEnd();
            this.mw.println();
            this.mw.println();
            this.mw.printBoldText("Description:");
            this.mw.print(" ");
            this.mw.println(nameValuePairValueTypeDoc.description());
            this.mw.println();
            this.mw.printBoldText("Class:");
            this.mw.print(" ");
            this.mw.printCodeText(cls.getName());
            this.mw.println();
            this.mw.println();
            this.headingLevel++;
        }

        @Override
        protected void processAfter(
                final Class<?> cls,
                final SingleValueSpecsDoc singleValueSpecsDoc) {
            this.headingLevel--;
        }

        @Override
        protected void processBefore(
                final Class<?> cls,
                final SingleValueSpecsDoc singleValueSpecsDoc) {
            this.mw.printHeader(
                    this.headingLevel, singleValueSpecsDoc.name());
            this.mw.println();
            this.mw.println();
            this.mw.printBoldText("Description:");
            this.mw.print(" ");
            this.mw.println(singleValueSpecsDoc.description());
            this.mw.println();
            this.headingLevel++;
        }

        @Override
        protected void processAfter(
                final Class<?> cls,
                final SingleValueTypeDoc singleValueTypeDoc) {
            this.headingLevel--;
        }

        @Override
        protected void processBefore(
                final Class<?> cls,
                final SingleValueTypeDoc singleValueTypeDoc) {
            this.mw.printHeader(
                    this.headingLevel, singleValueTypeDoc.name());
            this.mw.println();
            this.mw.println();
            this.mw.printBoldText("Syntax:");
            this.mw.println();
            this.mw.println();
            this.mw.printPreformattedTextStart("text");
            this.mw.println();
            this.mw.println(singleValueTypeDoc.syntax());
            this.mw.printPreformattedTextEnd();
            this.mw.println();
            this.mw.println();
            this.mw.printBoldText("Description:");
            this.mw.print(" ");
            this.mw.println(singleValueTypeDoc.description());
            this.mw.println();
            this.mw.printBoldText("Class:");
            this.mw.print(" ");
            this.mw.printCodeText(cls.getName());
            this.mw.println();
            this.mw.println();
            this.headingLevel++;
        }

        @Override
        protected void processNonDocAnnotatedClass(final Class<?> cls) {
            this.mw.printHeader(this.headingLevel, cls.getSimpleName());
            this.mw.println();
            this.mw.println();
            if (String.class.isAssignableFrom(cls)) {
                this.mw.printBoldText("Syntax:");
                this.mw.println();
                this.mw.println();
                this.mw.printPreformattedTextStart("text");
                this.mw.println();
                this.mw.println("[CHARACTER1[CHARACTER2[...]]]");
                this.mw.printPreformattedTextEnd();
                this.mw.println();
                this.mw.println();
                this.mw.printBoldText("Description:");
                this.mw.print(" ");
                this.mw.println("A list of characters");
                this.mw.println();
                this.mw.printBoldText("Class:");
                this.mw.print(" ");
                this.mw.printCodeText(cls.getName());
                this.mw.println();
                this.mw.println();
            } else {
                this.mw.printBoldText("Syntax:");
                this.mw.println();
                this.mw.println();
                this.mw.println(
                        "Please see the Javadocs for the following "
                                + "member(s) for the possible syntax "
                                + "for the String parameter:");
                this.mw.println();
                for (Constructor<?> ctor : cls.getDeclaredConstructors()) {
                    int modifiers = ctor.getModifiers();
                    if (!ctor.isAnnotationPresent(Deprecated.class)
                            && Modifier.isPublic(modifiers)
                            && Arrays.equals(
                            ctor.getParameterTypes(),
                            new Class<?>[] { String.class })) {
                        this.mw.printUnorderedListItemStart();
                        this.mw.printCodeText(ctor.toString());
                        this.mw.println();
                    }
                }
                for (Method method : cls.getDeclaredMethods()) {
                    int modifiers = method.getModifiers();
                    if (!method.isAnnotationPresent(Deprecated.class)
                            && Modifier.isPublic(modifiers)
                            && Modifier.isStatic(modifiers)
                            && method.getReturnType().equals(cls)
                            && Arrays.equals(
                            method.getParameterTypes(),
                            new Class<?>[] { String.class })) {
                        this.mw.printUnorderedListItemStart();
                        this.mw.printCodeText(method.toString());
                        this.mw.println();
                    }
                }
                this.mw.println();
                this.mw.printBoldText("Description:");
                this.mw.print(" ");
                this.mw.print(
                        "Please see the Javadocs for the following class "
                                + "for its description: ");
                this.mw.printCodeText(cls.getName());
                this.mw.println();
                this.mw.println();
                this.mw.printBoldText("Class:");
                this.mw.print(" ");
                this.mw.printCodeText(cls.getName());
                this.mw.println();
                this.mw.println();
            }
        }

    }

    private static final class MarkdownWriter {

        private static final String INDENTATION = "    ";
        private final PrintWriter pw;
        private int indentationCount;

        public MarkdownWriter(final PrintWriter p) {
            this.indentationCount = 0;
            this.pw = p;
        }

        public static String toId(final String s) {
            return s.toLowerCase().replaceAll("[^a-z0-9_]", "-");
        }

        public void decrementIndentationCount() {
            this.indentationCount--;
        }

        public void incrementIndentationCount() {
            this.indentationCount++;
        }

        public void print(final String s) {
            this.pw.print(s);
        }

        public void print(final char c) {
            this.pw.print(c);
        }

        public void printBoldText(final String text) {
            this.pw.printf("**%s**", text);
        }

        public void printCodeText(final String text) {
            this.pw.printf("`%s`", text);
        }

        public void printf(final String format, final Object... args) {
            this.pw.printf(format, args);
        }

        public void printHeader(final int level, final String text) {
            int i = 0;
            for (; i < level; i++) {
                this.pw.print('#');
            }
            if (i > 0) {
                this.pw.print(' ');
            }
            this.pw.print(text);
        }

        public void printIndentations() {
            for (int i = 0; i < this.indentationCount; i++) {
                this.pw.print(INDENTATION);
            }
        }

        public void printLink(final String text, final String ref) {
            this.pw.printf("[%s](%s)", text, ref);
        }

        public void printLinkToHeader(final String headerText) {
            this.printLink(headerText, "#".concat(toId(headerText)));
        }

        public void printLinkToHeader(
                final String headerText, final String location) {
            this.printLink(
                    headerText, location.concat("#").concat(toId(headerText)));
        }

        public void println() {
            this.pw.println();
        }

        public void println(final String s) {
            this.pw.println(s);
        }

        public void printPreformattedTextEnd() {
            this.pw.print("```");
        }

        public void printPreformattedTextStart(final String language) {
            this.pw.printf("```%s", language);
        }

        public void printUnorderedListItemStart() {
            this.pw.print("-   ");
        }

    }

    private static final class ValueTypeNameMapFactory {

        public ValueTypeNameMapFactory() {
        }

        public Map<Class<?>, String> newValueTypeNameMap(
                final List<Class<?>> classes) {
            Map<Class<?>, String> valueTypeNameMap = new HashMap<>();
            DocAnnotatedElementsProcessor docAnnotatedElementsProcessor =
                    new DocAnnotatedElementsProcessorToValueTypeNameMap(
                            valueTypeNameMap);
            docAnnotatedElementsProcessor.process(classes);
            return valueTypeNameMap;
        }

        private static final class DocAnnotatedElementsProcessorToValueTypeNameMap
                extends DocAnnotatedElementsProcessor {

            private final Map<Class<?>, String> valueTypeNameMap;

            public DocAnnotatedElementsProcessorToValueTypeNameMap(
                    final Map<Class<?>, String> valTypeNameMap) {
                this.valueTypeNameMap = valTypeNameMap;
            }

            @Override
            protected void process(
                    final Class<?> cls,
                    final ValuesValueTypeDoc valuesValueTypeDoc) {
                this.valueTypeNameMap.put(cls, valuesValueTypeDoc.name());
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
                this.valueTypeNameMap.put(cls, enumValueTypeDoc.name());
            }

            @Override
            protected void processBefore(
                    final Class<?> cls,
                    final NameValuePairValueTypeDoc nameValuePairValueTypeDoc) {
                this.valueTypeNameMap.put(cls, nameValuePairValueTypeDoc.name());
            }

            @Override
            protected void processBefore(
                    final Class<?> cls,
                    final SingleValueTypeDoc singleValueTypeDoc) {
                this.valueTypeNameMap.put(cls, singleValueTypeDoc.name());
            }

            @Override
            protected void processNonDocAnnotatedClass(final Class<?> cls) {
                this.valueTypeNameMap.put(cls, cls.getSimpleName());
            }

        }

    }

}
