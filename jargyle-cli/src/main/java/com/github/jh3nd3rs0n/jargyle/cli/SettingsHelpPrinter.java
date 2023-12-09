package com.github.jh3nd3rs0n.jargyle.cli;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.EnumValueDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.EnumValueTypeDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueTypeDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueTypeDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.ValuesValueTypeDoc;
import com.github.jh3nd3rs0n.jargyle.server.RuleCondition;
import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.Setting;

final class SettingsHelpPrinter {
	
	public SettingsHelpPrinter() { }

	private void printFrom(
			final EnumValueTypeDoc enumValueTypeDoc, 
			final Class<?> cls, 
			final PrintWriter pw) {
		pw.printf(
				"  %s: %s%n", 
				enumValueTypeDoc.syntaxName(), 
				enumValueTypeDoc.syntax());
		pw.println();
		for (Field field : cls.getDeclaredFields()) {
			EnumValueDoc enumValueDoc = field.getAnnotation(
					EnumValueDoc.class);
			if (enumValueDoc != null) {
				pw.printf("    %s%n", enumValueDoc.value());
				String description = enumValueDoc.description();
				if (!description.isEmpty()) {
					pw.printf("        %s%n", description);
				}
				pw.println();
			}
		}		
	}
	
	private void printFrom(
			final NameValuePairValueTypeDoc nameValuePairValueTypeDoc, 
			final Class<?> cls, 
			final PrintWriter pw) {
		pw.printf(
				"  %s: %s%n", 
				nameValuePairValueTypeDoc.syntaxName(), 
				nameValuePairValueTypeDoc.syntax());
		pw.println();
		for (Class<?> c : nameValuePairValueTypeDoc.nameValuePairValueSpecs()) {
			NameValuePairValueSpecsDoc nameValuePairValueSpecsDoc = 
					c.getAnnotation(NameValuePairValueSpecsDoc.class);
			if (nameValuePairValueSpecsDoc != null) {
				pw.printf(
						"    %s:%n", 
						nameValuePairValueSpecsDoc.name().toUpperCase());
				pw.println();
				for (Field field : c.getDeclaredFields()) {
					NameValuePairValueSpecDoc nameValuePairValueSpecDoc = 
							field.getAnnotation(NameValuePairValueSpecDoc.class);
					if (nameValuePairValueSpecDoc != null) {
						pw.printf(
								"        %s%n", 
								nameValuePairValueSpecDoc.syntax());
						String description = 
								nameValuePairValueSpecDoc.description();
						if (!description.isEmpty()) {
							pw.printf("            %s%n", description);
						}
						pw.println();
					}
				}
			}
		}		
	}
	
	private void printFrom(
			final SingleValueTypeDoc singleValueTypeDoc, 
			final Class<?> cls, 
			final PrintWriter pw) {
		pw.printf(
				"  %s: %s%n", 
				singleValueTypeDoc.syntaxName(), 
				singleValueTypeDoc.syntax());
		pw.println();
		for (Class<?> c : singleValueTypeDoc.singleValueSpecs()) {
			SingleValueSpecsDoc singleValueSpecsDoc = c.getAnnotation(
					SingleValueSpecsDoc.class);
			if (singleValueSpecsDoc != null) {
				pw.printf(
						"    %s:%n", 
						singleValueSpecsDoc.name().toUpperCase());
				pw.println();
				for (Field field : c.getDeclaredFields()) {
					SingleValueSpecDoc singleValueSpecDoc = 
							field.getAnnotation(SingleValueSpecDoc.class);
					if (singleValueSpecDoc != null) {
						pw.printf(
								"        %s%n", 
								singleValueSpecDoc.syntax());
						String desc = singleValueSpecDoc.description();
						if (!desc.isEmpty()) {
							pw.printf("            %s%n", desc);
						}
						pw.println();
					}
				}
			}
		}		
	}

	private void printFrom(
			final ValuesValueTypeDoc valuesValueTypeDoc, 
			final Class<?> cls, 
			final PrintWriter pw) {
		pw.printf(
				"  %s: %s%n", 
				valuesValueTypeDoc.syntaxName(), 
				valuesValueTypeDoc.syntax());
		pw.println();
	}
	
	private void printFromRootNameValuePairValueType(
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
							"  %s:%n", 
							nameValuePairValueSpecsDoc.name().toUpperCase());
					pw.println();
					for (Field field : c.getDeclaredFields()) {
						NameValuePairValueSpecDoc nameValuePairValueSpecDoc = 
								field.getAnnotation(
										NameValuePairValueSpecDoc.class);
						if (nameValuePairValueSpecDoc != null) {
							pw.print("    ");
							pw.println(
									nameValuePairValueSpecDoc.syntax());
							String description = 
									nameValuePairValueSpecDoc.description();
							if (!description.isEmpty()) {
								pw.print("        ");
								pw.println(description);
							}
							pw.println();
						}						
					}
				}
			}
		}		
	}
	
	private void printFromValueType(
			final Class<?> cls, final PrintWriter pw) {
		EnumValueTypeDoc enumValueTypeDoc = cls.getAnnotation(
				EnumValueTypeDoc.class);
		if (enumValueTypeDoc != null) {
			this.printFrom(enumValueTypeDoc, cls, pw);
		}
		NameValuePairValueTypeDoc nameValuePairValueTypeDoc = 
				cls.getAnnotation(NameValuePairValueTypeDoc.class);
		if (nameValuePairValueTypeDoc != null) {
			this.printFrom(nameValuePairValueTypeDoc, cls, pw);
		}
		SingleValueTypeDoc singleValueTypeDoc = cls.getAnnotation(
				SingleValueTypeDoc.class);
		if (singleValueTypeDoc != null) {
			this.printFrom(singleValueTypeDoc, cls, pw);
		}
		ValuesValueTypeDoc valuesValueTypeDoc = cls.getAnnotation(
				ValuesValueTypeDoc.class);
		if (valuesValueTypeDoc != null) {
			this.printFrom(valuesValueTypeDoc, cls, pw);
		}
	}
	
	public void printSettingsHelp(final PrintWriter pw) {
		Map<String, Class<?>> valueTypeMap = new TreeMap<String, Class<?>>();
		this.putFromRootNameValuePairValueType(valueTypeMap, Setting.class);
		this.putFromValueType(valueTypeMap, RuleCondition.class);
		this.putFromValueType(valueTypeMap, RuleResult.class);
		this.putFromValueType(valueTypeMap, Scheme.class);
		pw.println("SETTINGS:");
		pw.println();
		this.printFromRootNameValuePairValueType(Setting.class, pw);
		pw.println("SETTING VALUE SYNTAXES:");
		pw.println();
		for (Class<?> cls : valueTypeMap.values()) {
			this.printFromValueType(cls, pw);
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
