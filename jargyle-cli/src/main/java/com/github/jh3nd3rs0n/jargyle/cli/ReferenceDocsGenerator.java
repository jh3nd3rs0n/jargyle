package com.github.jh3nd3rs0n.jargyle.cli;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.*;
import com.github.jh3nd3rs0n.jargyle.server.ConfigurationSchema;
import com.github.jh3nd3rs0n.jargyle.server.RuleCondition;
import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.Setting;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

final class ReferenceDocsGenerator {

	public ReferenceDocsGenerator() { }
	
	public void generateReferenceDocs() throws IOException {
		Map<String, Class<?>> valueTypeMap = new TreeMap<String, Class<?>>(
				(x, y) -> x.compareToIgnoreCase(y));
		this.putFromRootNameValuePairValueType(valueTypeMap, Property.class);
		this.putFromRootNameValuePairValueType(
				valueTypeMap, RuleCondition.class);
		this.putFromRootNameValuePairValueType(valueTypeMap, RuleResult.class);
		this.putFromRootNameValuePairValueType(valueTypeMap, Setting.class);
		String valueSyntaxesFilename = "value-syntaxes.md";
		String clientPropertiesFilename = "client-properties.md";
		String ruleConditionsFilename = "rule-conditions.md";
		String ruleResultsFilename = "rule-results.md";
		String serverConfigurationSettingsFilename = 
				"server-configuration-settings.md";
		String serverConfigurationFileSchemaFilename = 
				"server-configuration-file-schema.md";
		String cliHelpInfoFilename = "cli-help-info.md";
		System.out.printf("Creating '%s'...", valueSyntaxesFilename);
		PrintWriter valueSyntaxesWriter = new PrintWriter(
				new File(valueSyntaxesFilename), "UTF-8");
		try {
			this.printValueSyntaxes(valueTypeMap, valueSyntaxesWriter);
		} finally {
			valueSyntaxesWriter.close();
		}
		System.out.println("Done.");
		System.out.printf("Creating '%s'...", clientPropertiesFilename);
		PrintWriter clientPropertiesWriter = new PrintWriter(
				new File(clientPropertiesFilename), "UTF-8");
		try {
			this.printClientProperties(
					valueSyntaxesFilename, clientPropertiesWriter);
		} finally {
			clientPropertiesWriter.close();
		}
		System.out.println("Done.");
		System.out.printf("Creating '%s'...", ruleConditionsFilename);
		PrintWriter ruleConditionsWriter = new PrintWriter(
				new File(ruleConditionsFilename), "UTF-8");
		try {
			this.printRuleConditions(
					valueSyntaxesFilename, ruleConditionsWriter);
		} finally {
			ruleConditionsWriter.close();
		}		
		System.out.println("Done.");
		System.out.printf("Creating '%s'...", ruleResultsFilename);
		PrintWriter ruleResultsWriter = new PrintWriter(
				new File(ruleResultsFilename), "UTF-8");
		try {
			this.printRuleResults(valueSyntaxesFilename, ruleResultsWriter);
		} finally {
			ruleResultsWriter.close();
		}		
		System.out.println("Done.");
		System.out.printf(
				"Creating '%s'...", serverConfigurationSettingsFilename);
		PrintWriter serverConfigurationSettingsWriter = new PrintWriter(
				new File(serverConfigurationSettingsFilename), "UTF-8");
		try {
			this.printServerConfigurationSettings(
					valueSyntaxesFilename, 
					serverConfigurationSettingsWriter);
		} finally {
			serverConfigurationSettingsWriter.close();
		}		
		System.out.println("Done.");
		System.out.printf(
				"Creating '%s'...", serverConfigurationFileSchemaFilename);
		PrintStream serverConfigurationFileSchemaStream = new PrintStream(
				new File(serverConfigurationFileSchemaFilename), "UTF-8");
		try {
			this.printServerConfigurationFileSchema(
					serverConfigurationFileSchemaStream);
		} finally {
			serverConfigurationFileSchemaStream.close();
		}
		System.out.println("Done.");
		System.out.printf("Creating '%s'...", cliHelpInfoFilename);
		PrintWriter cliHelpInfoWriter = new PrintWriter(
				new File(cliHelpInfoFilename), "UTF-8");
		try {
			this.printCliHelpInfo(cliHelpInfoWriter);
		} finally {
			cliHelpInfoWriter.close();
		}
		System.out.println("Done.");
	}
	
	private String getLink(final String header, final String filename) {
		return String.format(
				"[%s](%s#%s)", 
				header,
				filename,
				header.toLowerCase().replaceAll("[^a-z0-9_]", "-"));
	}
	
	private String getValueInfo(
			final Class<?> cls, final String valueSyntaxesFilename) {
		EnumValueTypeDoc enumValueTypeDoc = cls.getAnnotation(
				EnumValueTypeDoc.class);
		if (enumValueTypeDoc != null) {
			return this.getLink(enumValueTypeDoc.name(), valueSyntaxesFilename);
		}
		NameValuePairValueTypeDoc nameValuePairValueTypeDoc =
				cls.getAnnotation(NameValuePairValueTypeDoc.class);
		if (nameValuePairValueTypeDoc != null) {
			return this.getLink(
					nameValuePairValueTypeDoc.name(), valueSyntaxesFilename);
		}
		SingleValueTypeDoc singleValueTypeDoc = cls.getAnnotation(
				SingleValueTypeDoc.class);
		if (singleValueTypeDoc != null) {
			return this.getLink(singleValueTypeDoc.name(), valueSyntaxesFilename);
		}
		ValuesValueTypeDoc valuesValueTypeDoc = cls.getAnnotation(
				ValuesValueTypeDoc.class);
		if (valuesValueTypeDoc != null) {
			return this.getLink(valuesValueTypeDoc.name(), valueSyntaxesFilename);
		}
		return cls.getName();
	}
	
	private void printClientProperties(
			final String valueSyntaxesFilename, final PrintWriter pw) {
		pw.println("# Client Properties");
		pw.println();
		pw.println("## Page Contents");
		pw.println();
		this.printTocFromRootNameValuePairValueType(
				Property.class, pw);
		pw.println();
		this.printContentFromRootNameValuePairValueType(
				Property.class, valueSyntaxesFilename, pw);
	}
	
	private void printCliHelpInfo(final PrintWriter pw) {
		pw.println("# Command Line Interface Help Information");
		pw.println();
		pw.println("## Page Contents");
		pw.println();
		pw.println("-   [Help Information](#help-information)");
		pw.println("    -   [Help Information for manage-socks5-users](#help-information-for-manage-socks5-users)");
		pw.println("    -   [Help Information for new-server-config-file](#help-information-for-new-server-config-file)");
		pw.println("    -   [Help Information for start-server](#help-information-for-start-server)");
		pw.println();
		pw.println("## Help Information");
		pw.println();
		pw.println("```text");
		new JargyleCLI(
				"jargyle", 
				"jargyle", 
				new String[] { "--help" }, 
				false).printProgramHelp(pw);
		pw.println("```");
		pw.println();
		pw.println("### Help Information for manage-socks5-users");
		pw.println();
		pw.println("```text");
		new Socks5UserManagerCLI(
				"manage-socks5-users", 
				"jargyle manage-socks5-users", 
				new String[] { "--help" }, 
				false).printProgramHelp(pw);
		pw.println("```");
		pw.println();
		pw.println("### Help Information for new-server-config-file");
		pw.println();
		pw.println("```text");
		new ServerConfigurationFileCreatorCLI(
				"new-server-config-file", 
				"jargyle new-server-config-file", 
				new String[] { "--help" }, 
				false).printProgramHelp(pw);
		pw.println("```");
		pw.println();
		pw.println("### Help Information for start-server");
		pw.println();
		pw.println("```text");
		new ServerStarterCLI(
				"start-server", 
				"jargyle start-server", 
				new String[] { "--help" }, 
				false).printProgramHelp(pw);
		pw.println("```");
		pw.println();
	}
	
	private void printContentFrom(
			final EnumValueTypeDoc enumValueTypeDoc, 
			final Class<?> cls, 
			final PrintWriter pw) {
		pw.printf("## %s%n", enumValueTypeDoc.name());
		pw.println();
		pw.println("**Syntax:**");
		pw.println();
		pw.println("```text");
		pw.println(enumValueTypeDoc.syntax());
		pw.println("```");
		pw.println();
		String description = enumValueTypeDoc.description();
		if (!description.isEmpty()) {
			pw.println("**Description:**");
			pw.println();
			pw.println(description);
			pw.println();
		}
		boolean printingValues = false;
		for (Field field : cls.getDeclaredFields()) {
			EnumValueDoc enumValueDoc = field.getAnnotation(
					EnumValueDoc.class);
			if (enumValueDoc != null) {
				if (!printingValues) {
					pw.println("**Values:**");
					pw.println();
					printingValues = true; 
				}
				pw.printf("-   `%s`", enumValueDoc.value());
				String desc = enumValueDoc.description();
				if (!desc.isEmpty()) {
					pw.printf(": %s%n", desc);
				} else {
					pw.println();
				}
				pw.println();
			}
		}		
	}
	
	private void printContentFrom(
			final NameValuePairValueTypeDoc nameValuePairValueTypeDoc, 
			final Class<?> cls, 
			final PrintWriter pw) {
		pw.printf("## %s%n", nameValuePairValueTypeDoc.name());
		pw.println();
		pw.println("**Syntax:**");
		pw.println();
		pw.println("```text");
		pw.println(nameValuePairValueTypeDoc.syntax());
		pw.println("```");
		pw.println();
		String description = nameValuePairValueTypeDoc.description();
		if (!description.isEmpty()) {
			pw.println("**Description:**");
			pw.println();
			pw.println(description);
			pw.println();
		}
		for (Class<?> c : nameValuePairValueTypeDoc.nameValuePairValueSpecs()) {
			NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc = 
					c.getAnnotation(NameValuePairValueSpecsDoc.class);
			if (nameValuePairValueSpecsDoc != null) {
				pw.printf("### %s%n", nameValuePairValueSpecsDoc.name());
				pw.println();
				String desc = nameValuePairValueSpecsDoc.description();
				if (!desc.isEmpty()) {
					pw.println("**Description:**");
					pw.println();
					pw.println(desc);
					pw.println();
				}
				for (Field field : c.getDeclaredFields()) {
					NameValuePairValueSpecDoc nameValuePairValueSpecDoc = 
							field.getAnnotation(NameValuePairValueSpecDoc.class);
					if (nameValuePairValueSpecDoc != null) {
						pw.printf("#### %s%n", nameValuePairValueSpecDoc.name());
						pw.println();
						pw.println("**Syntax:**");
						pw.println();
						pw.println("```text");
						pw.println(nameValuePairValueSpecDoc.syntax());
						pw.println("```");
						pw.println();
						String d = nameValuePairValueSpecDoc.description();
						if (!d.isEmpty()) {
							pw.println("**Description:**");
							pw.println();
							pw.println(d);
							pw.println();
						}
						pw.printf(
								"**Value:** %s%n", 
								this.getValueInfo(
										nameValuePairValueSpecDoc.valueType(), 
										""));
						pw.println();
					}
				}
			}
		}		
	}
	
	private void printContentFrom(
			final SingleValueTypeDoc singleValueTypeDoc, 
			final Class<?> cls, 
			final PrintWriter pw) {
		pw.printf("## %s%n", singleValueTypeDoc.name());
		pw.println();
		pw.println("**Syntax:**");
		pw.println();
		pw.println("```text");
		pw.println(singleValueTypeDoc.syntax());
		pw.println("```");
		pw.println();
		String description = singleValueTypeDoc.description();
		if (!description.isEmpty()) {
			pw.println("**Description:**");
			pw.println();
			pw.println(description);
			pw.println();
		}
		for (Class<?> c : singleValueTypeDoc.singleValueSpecs()) {
			SingleValueSpecsDoc singleValueSpecsDoc = c.getAnnotation(
					SingleValueSpecsDoc.class);
			if (singleValueSpecsDoc != null) {
				pw.printf("### %s%n", singleValueSpecsDoc.name());
				pw.println();
				String desc = singleValueSpecsDoc.description();
				if (!desc.isEmpty()) {
					pw.println("**Description:**");
					pw.println();
					pw.println(desc);
					pw.println();
				}
				for (Field field : c.getDeclaredFields()) {
					SingleValueSpecDoc singleValueSpecDoc = 
							field.getAnnotation(SingleValueSpecDoc.class);
					if (singleValueSpecDoc != null) {
						pw.printf("#### %s%n", singleValueSpecDoc.name());
						pw.println();
						pw.println("**Syntax:**");
						pw.println();
						pw.println("```text");
						pw.println(singleValueSpecDoc.syntax());
						pw.println("```");
						pw.println();
						String d = singleValueSpecDoc.description();
						if (!d.isEmpty()) {
							pw.println("**Description:**");
							pw.println();
							pw.println(d);
							pw.println();
						}
					}
				}
			}
		}		
	}
	
	private void printContentFrom(
			final ValuesValueTypeDoc valuesValueTypeDoc, 
			final Class<?> cls, 
			final PrintWriter pw) {
		pw.printf("## %s%n", valuesValueTypeDoc.name());
		pw.println();
		pw.println("**Syntax:**");
		pw.println();
		pw.println("```text");
		pw.println(valuesValueTypeDoc.syntax());
		pw.println("```");
		pw.println();
		String description = valuesValueTypeDoc.description();
		if (!description.isEmpty()) {
			pw.println("**Description:**");
			pw.println();
			pw.println(description);
			pw.println();
		}
		pw.printf("**Element value:** %s%n", 
				this.getValueInfo(valuesValueTypeDoc.elementValueType(), ""));
		pw.println();
	}
	
	private void printContentFromRootNameValuePairValueType(
			final Class<?> cls,
			final String valueSyntaxesFilename,
			final PrintWriter pw) {
		NameValuePairValueTypeDoc nameValuePairValueTypeDoc = cls.getAnnotation(
				NameValuePairValueTypeDoc.class);
		if (nameValuePairValueTypeDoc != null) {
			Class<?>[] classes = 
					nameValuePairValueTypeDoc.nameValuePairValueSpecs();
			for (Class<?> c : classes) {
				NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc = 
						c.getAnnotation(NameValuePairValueSpecsDoc.class);
				if (nameValuePairValueSpecsDoc != null) {
					pw.printf("## %s%n", nameValuePairValueSpecsDoc.name());
					pw.println();
					String description = nameValuePairValueSpecsDoc.description();
					if (!description.isEmpty()) {
						pw.println("**Description:**");
						pw.println();
						pw.println(description);
						pw.println();
					}
					for (Field field : c.getDeclaredFields()) {
						NameValuePairValueSpecDoc nameValuePairValueSpecDoc = 
								field.getAnnotation(
										NameValuePairValueSpecDoc.class);
						if (nameValuePairValueSpecDoc != null) {
							pw.printf(
									"### %s%n", 
									nameValuePairValueSpecDoc.name());
							pw.println();
							pw.println("**Syntax:**");
							pw.println();
							pw.println("```text");
							pw.println(nameValuePairValueSpecDoc.syntax());
							pw.println("```");
							pw.println();
							String desc = nameValuePairValueSpecDoc.description();
							if (!desc.isEmpty()) {
								pw.println("**Description:**");
								pw.println();
								pw.println(desc);
								pw.println();
							}
							pw.printf(
									"**Value:** %s%n",
									this.getValueInfo(
											nameValuePairValueSpecDoc.valueType(), 
											valueSyntaxesFilename));
							pw.println();
						}						
					}
				}
			}
		}		
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
			this.printContentFrom(nameValuePairValueTypeDoc, cls, pw);
		}
		SingleValueTypeDoc singleValueTypeDoc = cls.getAnnotation(
				SingleValueTypeDoc.class);
		if (singleValueTypeDoc != null) {
			this.printContentFrom(singleValueTypeDoc, cls, pw);
		}
		ValuesValueTypeDoc valuesValueTypeDoc = cls.getAnnotation(
				ValuesValueTypeDoc.class);
		if (valuesValueTypeDoc != null) {
			this.printContentFrom(valuesValueTypeDoc, cls, pw);
		}
	}
	
	private void printRuleConditions(
			final String valueSyntaxesFilename, final PrintWriter pw) {
		pw.println("# Rule Conditions");
		pw.println();
		pw.println("## Page Contents");
		pw.println();
		this.printTocFromRootNameValuePairValueType(
				RuleCondition.class, pw);
		pw.println();
		this.printContentFromRootNameValuePairValueType(
				RuleCondition.class, valueSyntaxesFilename, pw);
	}

	private void printRuleResults(
			final String valueSyntaxesFilename, final PrintWriter pw) {
		pw.println("# Rule Results");
		pw.println();
		pw.println("## Page Contents");
		pw.println();
		this.printTocFromRootNameValuePairValueType(
				RuleResult.class, pw);
		pw.println();
		this.printContentFromRootNameValuePairValueType(
				RuleResult.class, valueSyntaxesFilename, pw);
	}
	
	private void printServerConfigurationFileSchema(
			final PrintStream ps) throws IOException {
		ps.println("# Server Configuration File Schema");
		ps.println();
		ps.println("```xml");
		ConfigurationSchema.newGeneratedInstance().toOutput(ps);
		ps.println("```");
		ps.println();
	}
	
	private void printServerConfigurationSettings(
			final String valueSyntaxesFilename, final PrintWriter pw) {
		pw.println("# Server Configuration Settings");
		pw.println();
		pw.println("## Page Contents");
		pw.println();
		this.printTocFromRootNameValuePairValueType(
				Setting.class, pw);
		pw.println();
		this.printContentFromRootNameValuePairValueType(
				Setting.class, valueSyntaxesFilename, pw);
	}

	private void printTocFrom(
			final EnumValueTypeDoc enumValueTypeDoc, 
			final Class<?> cls, 
			final PrintWriter pw) {
		pw.printf("-   %s%n", this.getLink(enumValueTypeDoc.name(), ""));
	}
	
	private void printTocFrom(
			final NameValuePairValueTypeDoc nameValuePairValueTypeDoc, 
			final Class<?> cls, 
			final PrintWriter pw) {
		pw.printf(
				"-   %s%n",	this.getLink(nameValuePairValueTypeDoc.name(), ""));
		for (Class<?> c : nameValuePairValueTypeDoc.nameValuePairValueSpecs()) {
			NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc = 
					c.getAnnotation(NameValuePairValueSpecsDoc.class);
			if (nameValuePairValueSpecsDoc != null) {
				pw.printf(
						"    -   %s%n", 
						this.getLink(nameValuePairValueSpecsDoc.name(), ""));
				for (Field field : c.getDeclaredFields()) {
					NameValuePairValueSpecDoc nameValuePairValueSpecDoc = 
							field.getAnnotation(NameValuePairValueSpecDoc.class);
					if (nameValuePairValueSpecDoc != null) {
						pw.printf(
								"        -   %s%n", 
								this.getLink(nameValuePairValueSpecDoc.name(), ""));
					}
				}
			}
		}		
	}
	
	private void printTocFrom(
			final SingleValueTypeDoc singleValueTypeDoc, 
			final Class<?> cls, 
			final PrintWriter pw) {
		pw.printf("-   %s%n", this.getLink(singleValueTypeDoc.name(), ""));
		for (Class<?> c : singleValueTypeDoc.singleValueSpecs()) {
			SingleValueSpecsDoc singleValueSpecsDoc = c.getAnnotation(
					SingleValueSpecsDoc.class);
			if (singleValueSpecsDoc != null) {
				pw.printf(
						"    -   %s:%n", 
						this.getLink(singleValueSpecsDoc.name(), ""));
				for (Field field : c.getDeclaredFields()) {
					SingleValueSpecDoc singleValueSpecDoc = 
							field.getAnnotation(SingleValueSpecDoc.class);
					if (singleValueSpecDoc != null) {
						pw.printf(
								"        -   %s%n", 
								this.getLink(singleValueSpecDoc.name(), ""));
					}
				}
			}
		}		
	}

	private void printTocFrom(
			final ValuesValueTypeDoc valuesValueTypeDoc, 
			final Class<?> cls, 
			final PrintWriter pw) {
		pw.printf("-   %s%n", this.getLink(valuesValueTypeDoc.name(), ""));
	}
	
	private void printTocFromRootNameValuePairValueType(
			final Class<?> cls, 
			final PrintWriter pw) {
		NameValuePairValueTypeDoc nameValuePairValueTypeDoc = cls.getAnnotation(
				NameValuePairValueTypeDoc.class);
		if (nameValuePairValueTypeDoc != null) {
			Class<?>[] classes = 
					nameValuePairValueTypeDoc.nameValuePairValueSpecs();
			for (Class<?> c : classes) {
				NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc = 
						c.getAnnotation(NameValuePairValueSpecsDoc.class);
				if (nameValuePairValueSpecsDoc != null) {
					pw.printf(
							"-   %s%n", 
							this.getLink(nameValuePairValueSpecsDoc.name(), ""));
					for (Field field : c.getDeclaredFields()) {
						NameValuePairValueSpecDoc nameValuePairValueSpecDoc = 
								field.getAnnotation(
										NameValuePairValueSpecDoc.class);
						if (nameValuePairValueSpecDoc != null) {
							pw.printf(
									"    -   %s%n",
									this.getLink(nameValuePairValueSpecDoc.name(), ""));
						}						
					}
				}
			}
		}		
	}
	
	private void printTocFromValueType(
			final Class<?> cls, final PrintWriter pw) {
		EnumValueTypeDoc enumValueTypeDoc = cls.getAnnotation(
				EnumValueTypeDoc.class);
		if (enumValueTypeDoc != null) {
			this.printTocFrom(enumValueTypeDoc, cls, pw);
		}
		NameValuePairValueTypeDoc nameValuePairValueTypeDoc = 
				cls.getAnnotation(NameValuePairValueTypeDoc.class);
		if (nameValuePairValueTypeDoc != null) {
			this.printTocFrom(nameValuePairValueTypeDoc, cls, pw);
		}
		SingleValueTypeDoc singleValueTypeDoc = cls.getAnnotation(
				SingleValueTypeDoc.class);
		if (singleValueTypeDoc != null) {
			this.printTocFrom(singleValueTypeDoc, cls, pw);
		}
		ValuesValueTypeDoc valuesValueTypeDoc = cls.getAnnotation(
				ValuesValueTypeDoc.class);
		if (valuesValueTypeDoc != null) {
			this.printTocFrom(valuesValueTypeDoc, cls, pw);
		}
	}

	private void printValueSyntaxes(
			final Map<String, Class<?>> valueTypeMap, final PrintWriter pw) {
		pw.println("# Value Syntaxes");
		pw.println();
		pw.println("## Page Contents");
		pw.println();
		for (Class<?> cls : valueTypeMap.values()) {
			this.printTocFromValueType(cls, pw);
		}
		pw.println();
		for (Class<?> cls : valueTypeMap.values()) {
			this.printContentFromValueType(cls, pw);
		}
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
