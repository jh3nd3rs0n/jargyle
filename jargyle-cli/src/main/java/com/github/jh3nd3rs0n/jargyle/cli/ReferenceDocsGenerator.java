package com.github.jh3nd3rs0n.jargyle.cli;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.*;
import com.github.jh3nd3rs0n.jargyle.server.ConfigurationSchema;
import com.github.jh3nd3rs0n.jargyle.server.RuleCondition;
import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.Setting;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

final class ReferenceDocsGenerator {

    private static final String VALUE_SYNTAXES_FILENAME =
            "value-syntaxes.xhtml";
    private static final String NEW_VALUE_SYNTAXES_FILENAME =
            "value-syntaxes.html";
    private static final String CLIENT_PROPERTIES_FILENAME =
            "client-properties.xhtml";
    private static final String RULE_CONDITIONS_FILENAME =
            "rule-conditions.xhtml";
    private static final String RULE_RESULTS_FILENAME =
            "rule-results.xhtml";
    private static final String SERVER_CONFIGURATION_SETTINGS_FILENAME =
            "server-configuration-settings.xhtml";
    private static final String SERVER_CONFIGURATION_FILE_SCHEMA_FILENAME =
            "server-configuration-file-schema.xhtml";
    private static final String CLI_HELP_INFO_FILENAME =
            "cli-help-info.xhtml";

    private static final String DOCUMENT_START_TAGS =
            "<!DOCTYPE html><html><head><title></title></head><body>";
    private static final String DOCUMENT_END_TAGS = "</body></html>";
    private static final String BOLD_TEXT_START_TAG = "<b>";
    private static final String BOLD_TEXT_END_TAG = "</b>";
    private static final String LIST_ITEM_START_TAG = "<li>";
    private static final String LIST_ITEM_END_TAG = "</li>";
    private static final String PARAGRAPH_START_TAG = "<p>";
    private static final String PARAGRAPH_END_TAG = "</p>";
    private static final String PREFORMATTED_TEXT_START_TAGS =
            "<pre><code class=\"language-text\">";
    private static final String PREFORMATTED_TEXT_END_TAGS = "</code></pre>";
    private static final String UNORDERED_LIST_START_TAG = "<ul>";
    private static final String UNORDERED_LIST_END_TAG = "</ul>";

    public ReferenceDocsGenerator() {
    }

    public void generateReferenceDocs() throws IOException {
        Map<String, Class<?>> valueTypeMap = new TreeMap<>(
                String::compareToIgnoreCase);
        this.putFromRootNameValuePairValueType(valueTypeMap, Property.class);
        this.putFromRootNameValuePairValueType(
                valueTypeMap, RuleCondition.class);
        this.putFromRootNameValuePairValueType(valueTypeMap, RuleResult.class);
        this.putFromRootNameValuePairValueType(valueTypeMap, Setting.class);
        System.out.printf("Creating '%s'...", VALUE_SYNTAXES_FILENAME);
        try (PrintWriter valueSyntaxesWriter = new PrintWriter(
                VALUE_SYNTAXES_FILENAME, "UTF-8")) {
            this.printValueSyntaxes(valueTypeMap, valueSyntaxesWriter);
        }
        System.out.println("Done.");
        System.out.printf("Creating '%s'...", CLIENT_PROPERTIES_FILENAME);
        try (PrintWriter clientPropertiesWriter = new PrintWriter(
                CLIENT_PROPERTIES_FILENAME, "UTF-8")) {
            this.printClientProperties(clientPropertiesWriter);
        }
        System.out.println("Done.");
        System.out.printf("Creating '%s'...", RULE_CONDITIONS_FILENAME);
        try (PrintWriter ruleConditionsWriter = new PrintWriter(
                RULE_CONDITIONS_FILENAME, "UTF-8")) {
            this.printRuleConditions(ruleConditionsWriter);
        }
        System.out.println("Done.");
        System.out.printf("Creating '%s'...", RULE_RESULTS_FILENAME);
        try (PrintWriter ruleResultsWriter = new PrintWriter(
                RULE_RESULTS_FILENAME, "UTF-8")) {
            this.printRuleResults(ruleResultsWriter);
        }
        System.out.println("Done.");
        System.out.printf(
                "Creating '%s'...", SERVER_CONFIGURATION_SETTINGS_FILENAME);
        try (PrintWriter serverConfigurationSettingsWriter = new PrintWriter(
                SERVER_CONFIGURATION_SETTINGS_FILENAME, "UTF-8")) {
            this.printServerConfigurationSettings(
                    serverConfigurationSettingsWriter);
        }
        System.out.println("Done.");
        System.out.printf(
                "Creating '%s'...", SERVER_CONFIGURATION_FILE_SCHEMA_FILENAME);
        try (PrintStream serverConfigurationFileSchemaStream = new PrintStream(
                SERVER_CONFIGURATION_FILE_SCHEMA_FILENAME, "UTF-8")) {
            this.printServerConfigurationFileSchema(
                    serverConfigurationFileSchemaStream);
        }
        System.out.println("Done.");
        System.out.printf("Creating '%s'...", CLI_HELP_INFO_FILENAME);
        try (PrintWriter cliHelpInfoWriter = new PrintWriter(
                CLI_HELP_INFO_FILENAME, "UTF-8")) {
            this.printCliHelpInfo(cliHelpInfoWriter);
        }
        System.out.println("Done.");
    }

    private String getAid(final String id) {
        return String.format("<a id=\"%s\"></a>", id);
    }

    private String getBoldText(final String text) {
        return String.format(
                "%s%s%s",
                BOLD_TEXT_START_TAG,
                text,
                BOLD_TEXT_END_TAG);
    }

    private String getCodeText(final String text) {
        return String.format("<code>%s</code>", text);
    }

    private String getHeader1(final String text) {
        return String.format(
                "%s<h1>%s</h1>",
                this.getAid(this.getId(text)),
                text);
    }

    private String getHeader2(final String text) {
        return String.format(
                "%s<h2>%s</h2>",
                this.getAid(this.getId(text)),
                text);
    }

    private String getHeader3(final String text) {
        return String.format(
                "%s<h3>%s</h3>",
                this.getAid(this.getId(text)),
                text);
    }

    private String getHeader4(final String text) {
        return String.format(
                "%s<h4>%s</h4>",
                this.getAid(this.getId(text)),
                text);
    }

    private String getId(final String s) {
        return s.toLowerCase().replaceAll("[^a-z0-9_]", "-");
    }

    private String getLinkToHeader(final String headerText) {
        return String.format(
                "<a href=\"#%s\">%s</a>",
                this.getId(headerText),
                headerText);
    }

    private String getLinkToHeader(
            final String filename, final String headerText) {
        return String.format(
                "<a href=\"%s#%s\">%s</a>",
                filename,
                this.getId(headerText),
                headerText);
    }

    private String getLinkToValueInfo(final Class<?> cls) {
        EnumValueTypeDoc enumValueTypeDoc = cls.getAnnotation(
                EnumValueTypeDoc.class);
        if (enumValueTypeDoc != null) {
            return this.getLinkToHeader(
                    NEW_VALUE_SYNTAXES_FILENAME, enumValueTypeDoc.name());
        }
        NameValuePairValueTypeDoc nameValuePairValueTypeDoc =
                cls.getAnnotation(NameValuePairValueTypeDoc.class);
        if (nameValuePairValueTypeDoc != null) {
            return this.getLinkToHeader(
                    NEW_VALUE_SYNTAXES_FILENAME,
                    nameValuePairValueTypeDoc.name());
        }
        SingleValueTypeDoc singleValueTypeDoc = cls.getAnnotation(
                SingleValueTypeDoc.class);
        if (singleValueTypeDoc != null) {
            return this.getLinkToHeader(
                    NEW_VALUE_SYNTAXES_FILENAME, singleValueTypeDoc.name());
        }
        ValuesValueTypeDoc valuesValueTypeDoc = cls.getAnnotation(
                ValuesValueTypeDoc.class);
        if (valuesValueTypeDoc != null) {
            return this.getLinkToHeader(
                    NEW_VALUE_SYNTAXES_FILENAME, valuesValueTypeDoc.name());
        }
        if (!cls.equals(String.class)) {
            return cls.getName().concat(".toString()");
        }
        return cls.getName();
    }

    private String getTextAsListItem(final String text) {
        return String.format(
                "%s%s%s",
                LIST_ITEM_START_TAG,
                text,
                LIST_ITEM_END_TAG);
    }

    private String getTextAsParagraph(final String text) {
        return String.format(
                "%s%s%s",
                PARAGRAPH_START_TAG,
                text,
                PARAGRAPH_END_TAG);
    }

    private void printClientProperties(final PrintWriter pw) {
        pw.println(DOCUMENT_START_TAGS);
        pw.println(this.getHeader1("Client Properties"));
        this.printTableFromRootNameValuePairValueType(Property.class, pw);
        pw.println(DOCUMENT_END_TAGS);
    }

    private void printCliHelpInfo(final PrintWriter pw) {
        pw.println(DOCUMENT_START_TAGS);
        pw.println(this.getHeader1("Command Line Interface Help Information"));
        pw.println(this.getHeader2("Page Contents"));
        pw.println(UNORDERED_LIST_START_TAG);
        pw.println(LIST_ITEM_START_TAG);
        pw.println(this.getLinkToHeader("Help Information"));
        pw.println(UNORDERED_LIST_START_TAG);
        pw.println(this.getTextAsListItem(this.getLinkToHeader(
                "Help Information for manage-socks5-users")));
        pw.println(this.getTextAsListItem(this.getLinkToHeader(
                "Help Information for new-server-config-file")));
        pw.println(this.getTextAsListItem(this.getLinkToHeader(
                "Help Information for start-server")));
        pw.println(this.getTextAsListItem(this.getLinkToHeader(
                "Settings Help Information")));
        pw.println(UNORDERED_LIST_END_TAG);
        pw.println(LIST_ITEM_END_TAG);
        pw.println(UNORDERED_LIST_END_TAG);
        pw.println(this.getHeader2("Help Information"));
        pw.print(PREFORMATTED_TEXT_START_TAGS);
        new JargyleCLI(
                "jargyle",
                "jargyle",
                new String[]{"--help"},
                false).printProgramHelp(pw);
        pw.println(PREFORMATTED_TEXT_END_TAGS);
        pw.println(this.getHeader3("Help Information for manage-socks5-users"));
        pw.print(PREFORMATTED_TEXT_START_TAGS);
        new Socks5UserManagerCLI(
                "manage-socks5-users",
                "jargyle manage-socks5-users",
                new String[]{"--help"},
                false).printProgramHelp(pw);
        pw.println(PREFORMATTED_TEXT_END_TAGS);
        pw.println(this.getHeader3("Help Information for new-server-config-file"));
        pw.print(PREFORMATTED_TEXT_START_TAGS);
        new ServerConfigurationFileCreatorCLI(
                "new-server-config-file",
                "jargyle new-server-config-file",
                new String[]{"--help"},
                false).printProgramHelp(pw);
        pw.println(PREFORMATTED_TEXT_END_TAGS);
        pw.println(this.getHeader3("Help Information for start-server"));
        pw.print(PREFORMATTED_TEXT_START_TAGS);
        new ServerStarterCLI(
                "start-server",
                "jargyle start-server",
                new String[]{"--help"},
                false).printProgramHelp(pw);
        pw.println(PREFORMATTED_TEXT_END_TAGS);
        pw.println(this.getHeader3("Settings Help Information"));
        pw.print(PREFORMATTED_TEXT_START_TAGS);
        new SettingsHelpPrinter().printSettingsHelp(pw);
        pw.println(PREFORMATTED_TEXT_END_TAGS);
        pw.println(DOCUMENT_END_TAGS);
    }

    private void printContentFrom(
            final EnumValueTypeDoc enumValueTypeDoc,
            final Class<?> cls,
            final PrintWriter pw) {
        pw.println(this.getHeader2(enumValueTypeDoc.name()));
        pw.println(this.getTextAsParagraph(this.getBoldText(
                "Syntax:")));
        pw.print(PREFORMATTED_TEXT_START_TAGS);
        pw.println(enumValueTypeDoc.syntax());
        pw.println(PREFORMATTED_TEXT_END_TAGS);
        String description = enumValueTypeDoc.description();
        if (!description.isEmpty()) {
            pw.println(this.getTextAsParagraph(this.getBoldText(
                    "Description:")));
            pw.println(this.getTextAsParagraph(description));
        }
        List<EnumValueDoc> enumValueDocList = Arrays.stream(cls.getDeclaredFields())
                .map((field) -> field.getAnnotation(EnumValueDoc.class))
                .filter((Objects::nonNull))
                .collect(Collectors.toList());
        if (!enumValueDocList.isEmpty()) {
            pw.println(this.getTextAsParagraph(this.getBoldText(
                    "Values:")));
            pw.println(UNORDERED_LIST_START_TAG);
            for (EnumValueDoc enumValueDoc : enumValueDocList) {
                pw.print(LIST_ITEM_START_TAG);
                pw.print(this.getCodeText(enumValueDoc.value()));
                String desc = enumValueDoc.description();
                if (!desc.isEmpty()) {
                    pw.printf(": %s", desc);
                }
                pw.println(LIST_ITEM_END_TAG);
            }
            pw.println(UNORDERED_LIST_END_TAG);
        }
    }

    private void printContentFrom(
            final NameValuePairValueTypeDoc nameValuePairValueTypeDoc,
            final PrintWriter pw) {
        pw.println(this.getHeader2(nameValuePairValueTypeDoc.name()));
        pw.println(this.getTextAsParagraph(this.getBoldText(
                "Syntax:")));
        pw.print(PREFORMATTED_TEXT_START_TAGS);
        pw.println(nameValuePairValueTypeDoc.syntax());
        pw.println(PREFORMATTED_TEXT_END_TAGS);
        String description = nameValuePairValueTypeDoc.description();
        if (!description.isEmpty()) {
            pw.println(this.getTextAsParagraph(this.getBoldText(
                    "Description:")));
            pw.println(this.getTextAsParagraph(description));
        }
        for (Class<?> c : nameValuePairValueTypeDoc.nameValuePairValueSpecs()) {
            NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc =
                    c.getAnnotation(NameValuePairValueSpecsDoc.class);
            if (nameValuePairValueSpecsDoc != null) {
                pw.println(this.getHeader3(nameValuePairValueSpecsDoc.name()));
                pw.println();
                String desc = nameValuePairValueSpecsDoc.description();
                if (!desc.isEmpty()) {
                    pw.println(this.getTextAsParagraph(this.getBoldText(
                            "Description:")));
                    pw.println(this.getTextAsParagraph(desc));
                }
                for (Field field : c.getDeclaredFields()) {
                    NameValuePairValueSpecDoc nameValuePairValueSpecDoc =
                            field.getAnnotation(NameValuePairValueSpecDoc.class);
                    if (nameValuePairValueSpecDoc != null) {
                        pw.println(this.getHeader4(nameValuePairValueSpecDoc.name()));
                        pw.println(this.getTextAsParagraph(this.getBoldText(
                                "Syntax:")));
                        pw.print(PREFORMATTED_TEXT_START_TAGS);
                        pw.println(nameValuePairValueSpecDoc.syntax());
                        pw.println(PREFORMATTED_TEXT_END_TAGS);
                        String d = nameValuePairValueSpecDoc.description();
                        if (!d.isEmpty()) {
                            pw.println(this.getTextAsParagraph(this.getBoldText(
                                    "Description:")));
                            pw.println(this.getTextAsParagraph(d));
                        }
                        pw.println(this.getTextAsParagraph(String.format(
                                "%s %s",
                                this.getBoldText("Value:"),
                                this.getLinkToValueInfo(
                                        nameValuePairValueSpecDoc.valueType()
                                ))));
                    }
                }
            }
        }
    }

    private void printContentFrom(
            final SingleValueTypeDoc singleValueTypeDoc,
            final PrintWriter pw) {
        pw.println(this.getHeader2(singleValueTypeDoc.name()));
        pw.println(this.getTextAsParagraph(this.getBoldText(
                "Syntax:")));
        pw.print(PREFORMATTED_TEXT_START_TAGS);
        pw.println(singleValueTypeDoc.syntax());
        pw.println(PREFORMATTED_TEXT_END_TAGS);
        String description = singleValueTypeDoc.description();
        if (!description.isEmpty()) {
            pw.println(this.getTextAsParagraph(this.getBoldText(
                    "Description:")));
            pw.println(this.getTextAsParagraph(description));
        }
        for (Class<?> c : singleValueTypeDoc.singleValueSpecs()) {
            SingleValueSpecsDoc singleValueSpecsDoc = c.getAnnotation(
                    SingleValueSpecsDoc.class);
            if (singleValueSpecsDoc != null) {
                pw.println(this.getHeader3(singleValueSpecsDoc.name()));
                String desc = singleValueSpecsDoc.description();
                if (!desc.isEmpty()) {
                    pw.println(this.getTextAsParagraph(this.getBoldText(
                            "Description:")));
                    pw.println(this.getTextAsParagraph(desc));
                }
                for (Field field : c.getDeclaredFields()) {
                    SingleValueSpecDoc singleValueSpecDoc =
                            field.getAnnotation(SingleValueSpecDoc.class);
                    if (singleValueSpecDoc != null) {
                        pw.println(this.getHeader4(singleValueSpecDoc.name()));
                        pw.println(this.getTextAsParagraph(this.getBoldText(
                                "Syntax:")));
                        pw.print(PREFORMATTED_TEXT_START_TAGS);
                        pw.println(singleValueSpecDoc.syntax());
                        pw.println(PREFORMATTED_TEXT_END_TAGS);
                        String d = singleValueSpecDoc.description();
                        if (!d.isEmpty()) {
                            pw.println(this.getTextAsParagraph(this.getBoldText(
                                    "Description:")));
                            pw.println(this.getTextAsParagraph(d));
                        }
                    }
                }
            }
        }
    }

    private void printContentFrom(
            final ValuesValueTypeDoc valuesValueTypeDoc,
            final PrintWriter pw) {
        pw.println(this.getHeader2(valuesValueTypeDoc.name()));
        pw.println(this.getTextAsParagraph(this.getBoldText(
                "Syntax:")));
        pw.print(PREFORMATTED_TEXT_START_TAGS);
        pw.println(valuesValueTypeDoc.syntax());
        pw.println(PREFORMATTED_TEXT_END_TAGS);
        pw.println();
        String description = valuesValueTypeDoc.description();
        if (!description.isEmpty()) {
            pw.println(this.getTextAsParagraph(this.getBoldText(
                    "Description:")));
            pw.println(this.getTextAsParagraph(description));
        }
        pw.println(this.getTextAsParagraph(String.format(
                "%s %s",
                this.getBoldText("Element value:"),
                this.getLinkToValueInfo(valuesValueTypeDoc.elementValueType()))));
    }

    private void printContentFromValueType(
            final Class<?> cls, final PrintWriter pw) {
        EnumValueTypeDoc enumValueTypeDoc = cls.getAnnotation(
                EnumValueTypeDoc.class);
        if (enumValueTypeDoc != null) {
            this.printContentFrom(enumValueTypeDoc, cls, pw);
        }
        NameValuePairValueTypeDoc nameValuePairValueTypeDoc =
                cls.getAnnotation(NameValuePairValueTypeDoc.class);
        if (nameValuePairValueTypeDoc != null) {
            this.printContentFrom(nameValuePairValueTypeDoc, pw);
        }
        SingleValueTypeDoc singleValueTypeDoc = cls.getAnnotation(
                SingleValueTypeDoc.class);
        if (singleValueTypeDoc != null) {
            this.printContentFrom(singleValueTypeDoc, pw);
        }
        ValuesValueTypeDoc valuesValueTypeDoc = cls.getAnnotation(
                ValuesValueTypeDoc.class);
        if (valuesValueTypeDoc != null) {
            this.printContentFrom(valuesValueTypeDoc, pw);
        }
    }

    private void printRuleConditions(final PrintWriter pw) {
        pw.println(DOCUMENT_START_TAGS);
        pw.println(this.getHeader1("Rule Conditions"));
        this.printTableFromRootNameValuePairValueType(
                RuleCondition.class, pw);
        pw.println(DOCUMENT_END_TAGS);
    }

    private void printRuleResults(final PrintWriter pw) {
        pw.println(DOCUMENT_START_TAGS);
        pw.println(this.getHeader1("Rule Results"));
        this.printTableFromRootNameValuePairValueType(
                RuleResult.class, pw);
        pw.println(DOCUMENT_END_TAGS);
    }

    private void printServerConfigurationFileSchema(
            final PrintStream ps) throws IOException {
        ps.println(DOCUMENT_START_TAGS);
        ps.println(this.getHeader1("Server Configuration File Schema"));
        ps.print("<pre><code class=\"language-xml\">");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConfigurationSchema.newGeneratedInstance().toOutput(out);
        Reader reader = new InputStreamReader(new ByteArrayInputStream(
                out.toByteArray()));
        int ch;
        while ((ch = reader.read()) != -1) {
            char c = (char) ch;
            if (c == '<') {
                ps.print("&lt;");
                continue;
            }
            if (c == '>') {
                ps.print("&gt;");
                continue;
            }
            if (c == '"') {
                ps.print("&quot;");
                continue;
            }
            ps.print(c);
        }
        ps.println("</code></pre>");
        ps.println(DOCUMENT_END_TAGS);
    }

    private void printServerConfigurationSettings(final PrintWriter pw) {
        pw.println(DOCUMENT_START_TAGS);
        pw.println(this.getHeader1("Server Configuration Settings"));
        this.printTableFromRootNameValuePairValueType(Setting.class, pw);
        pw.println(DOCUMENT_END_TAGS);
    }

    private void printListItemOfLinkToContentFrom(
            final EnumValueTypeDoc enumValueTypeDoc,
            final PrintWriter pw) {
        pw.println(this.getTextAsListItem(this.getLinkToHeader(
                enumValueTypeDoc.name())));
    }

    private void printListItemOfLinkToContentFrom(
            final NameValuePairValueTypeDoc nameValuePairValueTypeDoc,
            final PrintWriter pw) {
        pw.println(LIST_ITEM_START_TAG);
        pw.println(this.getLinkToHeader(nameValuePairValueTypeDoc.name()));
        pw.println(UNORDERED_LIST_START_TAG);
        for (Class<?> c : nameValuePairValueTypeDoc.nameValuePairValueSpecs()) {
            NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc =
                    c.getAnnotation(NameValuePairValueSpecsDoc.class);
            if (nameValuePairValueSpecsDoc != null) {
                pw.println(LIST_ITEM_START_TAG);
                pw.println(this.getLinkToHeader(
                        nameValuePairValueSpecsDoc.name()));
                pw.println(UNORDERED_LIST_START_TAG);
                for (Field field : c.getDeclaredFields()) {
                    NameValuePairValueSpecDoc nameValuePairValueSpecDoc =
                            field.getAnnotation(NameValuePairValueSpecDoc.class);
                    if (nameValuePairValueSpecDoc != null) {
                        pw.println(this.getTextAsListItem(this.getLinkToHeader(
                                nameValuePairValueSpecDoc.name())));
                    }
                }
                pw.println(UNORDERED_LIST_END_TAG);
                pw.println(LIST_ITEM_END_TAG);
            }
        }
        pw.println(UNORDERED_LIST_END_TAG);
        pw.println(LIST_ITEM_END_TAG);
    }

    private void printListItemOfLinkToContentFrom(
            final SingleValueTypeDoc singleValueTypeDoc,
            final PrintWriter pw) {
        pw.println(LIST_ITEM_START_TAG);
        pw.println(this.getLinkToHeader(singleValueTypeDoc.name()));
        pw.println(UNORDERED_LIST_START_TAG);
        for (Class<?> c : singleValueTypeDoc.singleValueSpecs()) {
            SingleValueSpecsDoc singleValueSpecsDoc = c.getAnnotation(
                    SingleValueSpecsDoc.class);
            if (singleValueSpecsDoc != null) {
                pw.println(LIST_ITEM_START_TAG);
                pw.println(this.getLinkToHeader(singleValueSpecsDoc.name()));
                pw.println(UNORDERED_LIST_START_TAG);
                for (Field field : c.getDeclaredFields()) {
                    SingleValueSpecDoc singleValueSpecDoc =
                            field.getAnnotation(SingleValueSpecDoc.class);
                    if (singleValueSpecDoc != null) {
                        pw.println(this.getTextAsListItem(this.getLinkToHeader(
                                singleValueSpecDoc.name())));
                    }
                }
                pw.println(UNORDERED_LIST_END_TAG);
                pw.println(LIST_ITEM_END_TAG);
            }
        }
        pw.println(UNORDERED_LIST_END_TAG);
        pw.println(LIST_ITEM_END_TAG);
    }

    private void printListItemOfLinkToContentFrom(
            final ValuesValueTypeDoc valuesValueTypeDoc,
            final PrintWriter pw) {
        pw.println(this.getTextAsListItem(this.getLinkToHeader(
                valuesValueTypeDoc.name())));
    }

    private void printTableFromRootNameValuePairValueType(
            final Class<?> cls, final PrintWriter pw) {
        NameValuePairValueTypeDoc nameValuePairValueTypeDoc =
                cls.getAnnotation(NameValuePairValueTypeDoc.class);
        if (nameValuePairValueTypeDoc != null) {
            Class<?>[] classes =
                    nameValuePairValueTypeDoc.nameValuePairValueSpecs();
            pw.println("<table>");
            for (Class<?> c : classes) {
                NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc =
                        c.getAnnotation(NameValuePairValueSpecsDoc.class);
                if (nameValuePairValueSpecsDoc != null) {
                    pw.printf(
                            "<tr><th colspan=\"3\">%s</th></tr>%n",
                            nameValuePairValueSpecsDoc.name());
                    pw.println("<tr><th>Name</th><th>Value</th><th>Description</th></tr>");
                    for (Field field : c.getDeclaredFields()) {
                        NameValuePairValueSpecDoc nameValuePairValueSpecDoc =
                                field.getAnnotation(
                                        NameValuePairValueSpecDoc.class);
                        if (nameValuePairValueSpecDoc != null) {
                            pw.printf(
                                    "<tr><td><code>%s</code></td><td>%s</td><td>%s</td></tr>%n",
                                    nameValuePairValueSpecDoc.name(),
                                    this.getLinkToValueInfo(
                                            nameValuePairValueSpecDoc.valueType()
                                    ),
                                    nameValuePairValueSpecDoc.description());
                        }
                    }
                }
            }
            pw.println("</table>");
        }
    }

    private void printListItemOfLinkToContentFromValueType(
            final Class<?> cls, final PrintWriter pw) {
        EnumValueTypeDoc enumValueTypeDoc = cls.getAnnotation(
                EnumValueTypeDoc.class);
        if (enumValueTypeDoc != null) {
            this.printListItemOfLinkToContentFrom(enumValueTypeDoc, pw);
        }
        NameValuePairValueTypeDoc nameValuePairValueTypeDoc =
                cls.getAnnotation(NameValuePairValueTypeDoc.class);
        if (nameValuePairValueTypeDoc != null) {
            this.printListItemOfLinkToContentFrom(nameValuePairValueTypeDoc, pw);
        }
        SingleValueTypeDoc singleValueTypeDoc = cls.getAnnotation(
                SingleValueTypeDoc.class);
        if (singleValueTypeDoc != null) {
            this.printListItemOfLinkToContentFrom(singleValueTypeDoc, pw);
        }
        ValuesValueTypeDoc valuesValueTypeDoc = cls.getAnnotation(
                ValuesValueTypeDoc.class);
        if (valuesValueTypeDoc != null) {
            this.printListItemOfLinkToContentFrom(valuesValueTypeDoc, pw);
        }
    }

    private void printValueSyntaxes(
            final Map<String, Class<?>> valueTypeMap, final PrintWriter pw) {
        pw.println(DOCUMENT_START_TAGS);
        pw.println(this.getHeader1("Value Syntaxes"));
        pw.println(this.getHeader2("Page Contents"));
        pw.println(UNORDERED_LIST_START_TAG);
        for (Class<?> cls : valueTypeMap.values()) {
            this.printListItemOfLinkToContentFromValueType(cls, pw);
        }
        pw.println(UNORDERED_LIST_END_TAG);
        for (Class<?> cls : valueTypeMap.values()) {
            this.printContentFromValueType(cls, pw);
        }
        pw.println(DOCUMENT_END_TAGS);
    }

    private void putFromRootNameValuePairValueType(
            final Map<String, Class<?>> valueTypeMap, final Class<?> cls) {
        NameValuePairValueTypeDoc nameValuePairValueTypeDoc = cls.getAnnotation(
                NameValuePairValueTypeDoc.class);
        if (nameValuePairValueTypeDoc != null) {
            Class<?>[] classes =
                    nameValuePairValueTypeDoc.nameValuePairValueSpecs();
            for (Class<?> c : classes) {
                NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc =
                        c.getAnnotation(NameValuePairValueSpecsDoc.class);
                if (nameValuePairValueSpecsDoc != null) {
                    for (Field field : c.getDeclaredFields()) {
                        NameValuePairValueSpecDoc nameValuePairValueSpecDoc =
                                field.getAnnotation(
                                        NameValuePairValueSpecDoc.class);
                        if (nameValuePairValueSpecDoc != null) {
                            this.putFromValueType(
                                    valueTypeMap,
                                    nameValuePairValueSpecDoc.valueType());
                        }
                    }
                }
            }
        }
    }

    private void putFromValueType(
            final Map<String, Class<?>> valueTypeMap, final Class<?> cls) {
        EnumValueTypeDoc enumValueTypeDoc = cls.getAnnotation(
                EnumValueTypeDoc.class);
        if (enumValueTypeDoc != null) {
            valueTypeMap.put(enumValueTypeDoc.syntaxName(), cls);
        }
        NameValuePairValueTypeDoc nameValuePairValueTypeDoc = cls.getAnnotation(
                NameValuePairValueTypeDoc.class);
        if (nameValuePairValueTypeDoc != null) {
            valueTypeMap.put(nameValuePairValueTypeDoc.syntaxName(), cls);
            for (Class<?> c :
                    nameValuePairValueTypeDoc.nameValuePairValueSpecs()) {
                NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc =
                        c.getAnnotation(NameValuePairValueSpecsDoc.class);
                if (nameValuePairValueSpecsDoc != null) {
                    for (Field field : c.getDeclaredFields()) {
                        NameValuePairValueSpecDoc nameValuePairValueSpecDoc =
                                field.getAnnotation(
                                        NameValuePairValueSpecDoc.class);
                        if (nameValuePairValueSpecDoc != null) {
                            this.putFromValueType(
                                    valueTypeMap,
                                    nameValuePairValueSpecDoc.valueType());
                        }
                    }
                }
            }
        }
        SingleValueTypeDoc singleValueTypeDoc = cls.getAnnotation(
                SingleValueTypeDoc.class);
        if (singleValueTypeDoc != null) {
            valueTypeMap.put(singleValueTypeDoc.syntaxName(), cls);
        }
        ValuesValueTypeDoc valuesValueTypeDoc = cls.getAnnotation(
                ValuesValueTypeDoc.class);
        if (valuesValueTypeDoc != null) {
            valueTypeMap.put(valuesValueTypeDoc.syntaxName(), cls);
            this.putFromValueType(
                    valueTypeMap, valuesValueTypeDoc.elementValueType());
        }
    }

}
