/*
Copyright (c) 2016 Jonathan K. Henderson

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the “Software”), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE. 
*/

package argmatey;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * Provides interfaces and classes for parsing command line arguments and 
 * for displaying the program usage and help.
 */
public final class ArgMatey {

	static abstract class AbstractArgHandler implements ArgHandler {

		private final ArgHandler argHandler;
		
		protected AbstractArgHandler(final ArgHandler handler) {
			ArgHandler h = handler;
			if (h == null) {
				h = DefaultArgHandler.INSTANCE;
			}
			this.argHandler = h;
		}
		
		public final ArgHandler getArgHandler() {
			return this.argHandler;
		}
		
		@Override
		public abstract void handle(
				final String arg, final ArgHandlerContext context);

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(this.getClass().getSimpleName())
				.append(" [argHandler=")
				.append(this.argHandler)
				.append("]");
			return sb.toString();
		}	
		
	}

	public static final class Annotations {

		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.METHOD})
		static @interface DisplaysProgramHelp { }

		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.METHOD})
		static @interface DisplaysProgramVersion { }
		
		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.METHOD})
		public static @interface HelpText {
			
			String value();
			
		}
		
		@Repeatable(Options.class)
		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.METHOD})
		public static @interface Option {
			
			OptionalBoolean displayable() default OptionalBoolean.UNSPECIFIED;
			
			String doc() default "";
			
			String name();
			
			Annotations.OptionArgSpec optionArgSpec() 
				default @OptionArgSpec(allowed = OptionalBoolean.UNSPECIFIED);
			
			Class<? extends ArgMatey.OptionUsageProvider> optionUsageProvider()
				default ArgMatey.OptionUsageProvider.class;
					
			OptionType type();
			
			String usage() default "";
			
		}

		@Retention(RetentionPolicy.RUNTIME)
		public static @interface OptionArgSpec {
			
			OptionalBoolean allowed() default OptionalBoolean.TRUE;
			
			String name() default "";
			
			OptionalBoolean required() default OptionalBoolean.UNSPECIFIED;
			
			String separator() default ArgMatey.OptionArgSpec.DEFAULT_SEPARATOR;
			
			Class<? extends ArgMatey.StringConverter> stringConverter() 
				default ArgMatey.StringConverter.class;
			
		}

		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.METHOD})
		public static @interface OptionGroupHelpTextProvider {
			
			Class<? extends ArgMatey.OptionGroupHelpTextProvider> value();
			
		}

		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.METHOD})
		public static @interface Options {
			
			Annotations.Option[] value();
			
		}
		
		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.METHOD})
		public static @interface Ordinal {
			
			int value();
			
		}
		
		private Annotations() { }
		
	}
		
	static interface ArgHandler {
		
		void handle(String arg, ArgHandlerContext context);
		
	}
	
	static final class ArgHandlerContext {
		
		private int argCharIndex;
		private int argIndex;
		private final String[] args;
		private final Map<String, Object> properties;
		
		public ArgHandlerContext(final ArgHandlerContext other) {
			this.argCharIndex = other.argCharIndex;
			this.argIndex = other.argIndex;
			this.args = Arrays.copyOf(other.args, other.args.length);
			this.properties = new HashMap<String, Object>(other.properties);
		}
		
		public ArgHandlerContext(final String[] arguments) {
			for (String argument : arguments) {
				Objects.requireNonNull(
						argument, "argument(s) must not be null");
			}
			this.argCharIndex = -1;
			this.argIndex = -1;
			this.args = Arrays.copyOf(arguments, arguments.length);
			this.properties = new HashMap<String, Object>();
		}
		
		public int getArgCharIndex() {
			return this.argCharIndex;
		}
		
		public int getArgIndex() {
			return this.argIndex;
		}
		
		public String[] getArgs() {
			return Arrays.copyOf(this.args, this.args.length);
		}
		
		public Map<String, Object> getProperties() {
			return Collections.unmodifiableMap(this.properties);
		}
		
		public Object getProperty(final String name) {
			return this.properties.get(name);
		}
		
		public int incrementArgCharIndex() {
			if (this.argIndex == -1) {
				throw new ArrayIndexOutOfBoundsException();
			}
			if (this.argCharIndex == this.args[this.argIndex].length() - 1) {
				throw new StringIndexOutOfBoundsException();
			}
			return ++this.argCharIndex;
		}
		
		public int incrementArgIndex() {
			if (this.argIndex == this.args.length - 1) {
				throw new ArrayIndexOutOfBoundsException();
			}
			return ++this.argIndex;
		}
		
		public Object putProperty(final String name, final Object value) {
			return this.properties.put(name, value);
		}
		
		public Object removeProperty(final String name) {
			return this.properties.remove(name);
		}
		
		public int resetArgCharIndex() {
			this.argCharIndex = -1;
			return this.argCharIndex;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(this.getClass().getSimpleName())
				.append(" [argCharIndex=")
				.append(this.argCharIndex)
				.append(", argIndex=")
				.append(this.argIndex)
				.append(", args=")
				.append(Arrays.toString(this.args))
				.append(", properties=")
				.append(this.properties)
				.append("]");
			return sb.toString();
		}
		
	}
	
	static final class ArgHandlerContextProperties {
		
		private static final class PropertyNameConstants {
			
			public static final String OPTION_HANDLING_ENABLED = 
					"OPTION_HANDLING_ENABLED";
			
			public static final String OPTION_MAP = "OPTION_MAP";
			
			public static final String PARSE_RESULT_HOLDER =
					"PARSE_RESULT_HOLDER";
			
			private PropertyNameConstants() { }

		}
		
		private final ArgHandlerContext argHandlerContext;
		
		public ArgHandlerContextProperties(
				final ArgHandlerContext handlerContext) {
			this.argHandlerContext = handlerContext;
		}
		
		public Map<String, Option> getOptionMap() {
			Map<String, Option> optionMap = null;
			@SuppressWarnings("unchecked")
			Map<String, Option> value = 
					(Map<String, Option>) this.argHandlerContext.getProperty(
							PropertyNameConstants.OPTION_MAP);
			if (value != null) {
				optionMap = Collections.unmodifiableMap(
						new HashMap<String, Option>(value));
			}
			return optionMap;
		}
		
		public ParseResultHolder getParseResultHolder() {
			ParseResultHolder parseResult = null;
			ParseResultHolder value = 
					(ParseResultHolder) this.argHandlerContext.getProperty(
							PropertyNameConstants.PARSE_RESULT_HOLDER);
			if (value != null) {
				parseResult = value;
			}
			return parseResult;
		}
		
		public boolean isOptionHandlingEnabled() {
			boolean optionHandlingEnabled = false;
			Boolean value = (Boolean) this.argHandlerContext.getProperty(
					PropertyNameConstants.OPTION_HANDLING_ENABLED);
			if (value != null) {
				optionHandlingEnabled = value.booleanValue();
			}
			return optionHandlingEnabled;
		}
		
		public void setOptionHandlingEnabled(
				final boolean optHandlingEnabled) {
			this.argHandlerContext.putProperty(
					PropertyNameConstants.OPTION_HANDLING_ENABLED, 
					Boolean.valueOf(optHandlingEnabled));
		}
		
		public void setOptionMap(final Map<String, Option> optMap) {
			this.argHandlerContext.putProperty(
					PropertyNameConstants.OPTION_MAP, optMap);
		}
		
		public void setParseResultHolder(
				final ParseResultHolder parseResultHolder) {
			this.argHandlerContext.putProperty(
					PropertyNameConstants.PARSE_RESULT_HOLDER, 
					parseResultHolder);
		}
	}
	
	static final class ArgsParser {
		
		public static ArgsParser newInstance(
				final String[] args, 
				final OptionGroups optionGroups, 
				final boolean posixlyCorrect) {
			ArgHandler argHandler = (posixlyCorrect) ? 
					new EndOfOptionsArgHandler(null) : null;
			argHandler = new EndOfOptionsDelimiterHandler(argHandler);
			argHandler = OptionTypeProfile.LONG.newOptionHandler(argHandler);
			argHandler = OptionTypeProfile.GNU_LONG.newOptionHandler(argHandler);
			return new ArgsParser(args, optionGroups, argHandler);
		}
		
		private final ArgHandler argHandler;
		private ArgHandlerContext argHandlerContext;
		private final OptionGroups optionGroups;
		private ParseResultHolder parseResultHolder;
			
		private ArgsParser(
				final String[] args, 
				final OptionGroups optGroups, 
				final ArgHandler handler) {
			for (String arg : args) {
				Objects.requireNonNull(arg, "argument(s) must not be null");
			}
			Objects.requireNonNull(optGroups, "OptionGroups must not be null");
			ArgHandlerContext handlerContext = new ArgHandlerContext(args);
			List<OptionGroup> optGroupList = optGroups.toList();
			if (optGroupList.size() > 0) {
				Map<String, Option> optMap = new HashMap<String, Option>();
				for (OptionGroup optGroup : optGroupList) {
					for (Option opt : optGroup.toList()) {
						optMap.put(opt.toString(), opt);
					}
				}
				ArgHandlerContextProperties properties = 
						new ArgHandlerContextProperties(handlerContext);
				properties.setOptionHandlingEnabled(true);
				properties.setOptionMap(optMap);
			}
			this.argHandler = handler;
			this.argHandlerContext = handlerContext;
			this.optionGroups = optGroups;
			this.parseResultHolder = null;
		}

		public int getArgCharIndex() {
			return this.argHandlerContext.getArgCharIndex();
		}
		
		public int getArgIndex() {
			return this.argHandlerContext.getArgIndex();
		}
		
		public String[] getArgs() {
			return this.argHandlerContext.getArgs();
		}
		
		public OptionGroups getOptionGroups() {
			return this.optionGroups;
		}
		
		public ParseResultHolder getParseResultHolder() {
			return this.parseResultHolder;
		}
		
		public boolean hasNext() {
			String[] args = this.getArgs();
			int argIndex = this.getArgIndex();
			int argCharIndex = this.getArgCharIndex();
			return (argIndex > -1 && argCharIndex > -1 
					&& argCharIndex < args[argIndex].length() - 1) 
					|| argIndex < args.length - 1;
		}
		
		public String next() {
			String next = null;
			int argIndex = this.getArgIndex();
			int argCharIndex = this.getArgCharIndex();
			String[] args = this.getArgs();
			ArgHandlerContext recentArgHandlerContext = 
					new ArgHandlerContext(this.argHandlerContext);
			if (argIndex > -1 && argCharIndex > -1) {
				/* 
				 * The argument character index was incremented by this method 
				 * or another ArgHandler
				 */
				if (argCharIndex < args[argIndex].length() - 1) {
					/* 
					 * if the argument character index is not the last index, 
					 * increment it 
					 */
					argCharIndex = 
							this.argHandlerContext.incrementArgCharIndex();
					next = Character.toString(args[argIndex].charAt(
							argCharIndex)); 
				} else {
					/* 
					 * if the argument character index is the last index, 
					 * reset it 
					 */
					argCharIndex = this.argHandlerContext.resetArgCharIndex();
				}
			}
			if (argCharIndex == -1) {
				/* 
				 * The argument character index was not incremented or was 
				 * reset by this method or another ArgHandler
				 */
				if (argIndex < args.length - 1) {
					/* 
					 * if the argument index is not the last index, 
					 * increment it
					 */
					argIndex = this.argHandlerContext.incrementArgIndex();
					next = args[argIndex];
				} else {
					/* 
					 * failure atomicity (return back to most recent working 
					 * state) 
					 */
					this.argHandlerContext = recentArgHandlerContext;
					throw new NoSuchElementException();
				}
			}
			this.parseResultHolder = null;
			return next;
		}
		
		public ParseResultHolder parseNext() {
			ArgHandlerContext recentArgHandlerContext = 
					new ArgHandlerContext(this.argHandlerContext);
			this.next();
			try {
				this.argHandler.handle(this.getArgs()[this.getArgIndex()], 
						this.argHandlerContext);
			} catch (RuntimeException e) {
				/* 
				 * failure atomicity (return back to most recent working state) 
				 */
				this.argHandlerContext = recentArgHandlerContext;
				throw e;
			} catch (Error e) {
				/* 
				 * failure atomicity (return back to most recent working state) 
				 */
				this.argHandlerContext = recentArgHandlerContext;
				throw e;
			}
			ArgHandlerContextProperties properties = 
					new ArgHandlerContextProperties(this.argHandlerContext);
			ParseResultHolder resultHolder = properties.getParseResultHolder();
			this.parseResultHolder = resultHolder;
			properties.setParseResultHolder(null);
			return resultHolder;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(this.getClass().getSimpleName())
				.append(" [optionGroups=")
				.append(this.optionGroups)
				.append(", getArgCharIndex()=")
				.append(this.getArgCharIndex())
				.append(", getArgIndex()=")
				.append(this.getArgIndex())
				.append(", getArgs()=")
				.append(Arrays.toString(this.getArgs()))
				.append("]");
			return sb.toString();
		}

	}

	public static abstract class CLI {
		
		private ArgsParser argsParser;
		private final CLIClass cliClass;
		private final boolean posixlyCorrect;
		protected String programDoc;
		private boolean programHelpDisplayed;
		protected String programName;
		protected String programOperandsUsage;
		protected String programVersion;
		private boolean programVersionDisplayed;
		
		public CLI(final String[] args, final boolean posixCorrect) {
			CLIClass cls = CLIClass.newInstance(this.getClass());
			ArgsParser parser = ArgsParser.newInstance(
					args, cls.getOptionGroups(), posixCorrect);
			this.argsParser = parser;
			this.cliClass = cls;
			this.posixlyCorrect = posixCorrect;
			this.programDoc = null;
			this.programHelpDisplayed = false;
			this.programName = null;
			this.programOperandsUsage = null;
			this.programVersion = null;
			this.programVersionDisplayed = false;
		}
		
		protected Optional<Integer> afterHandleArgs() {
			return Optional.empty();
		}
		
		protected Optional<Integer> afterHandleNext() {
			return Optional.empty();
		}
		
		protected Optional<Integer> beforeHandleArgs() {
			return Optional.empty();
		}
		
		protected Optional<Integer> beforeHandleNext() {
			return Optional.empty();
		}
		
		@Annotations.DisplaysProgramHelp
		@Annotations.Option(
				doc = "Display this help and exit",
				name = "help",
				type = OptionType.GNU_LONG
		)
		protected void displayProgramHelp() {
			this.displayProgramUsage();
			if (this.programDoc != null) {
				System.out.println(this.programDoc);
			}
			System.out.println();
			int displayableOptionCount = 0;
			for (OptionGroup optionGroup : this.getOptionGroups().toList()) {
				displayableOptionCount += optionGroup.toDisplayableList().size();
			}
			if (displayableOptionCount > 0) {
				if (displayableOptionCount == 1) {
					System.out.println("OPTION:");
				} else {
					System.out.println("OPTIONS:");
				}
				this.getOptionGroups().printHelpText();
				System.out.println();
			}
		}
		
		protected void displayProgramUsage() {
			String progName = this.programName;
			if (progName == null) {
				progName = this.getClass().getName();
			}
			System.out.printf("Usage: %s", progName);
			int displayableOptionCount = 0;
			for (OptionGroup optionGroup : this.getOptionGroups().toList()) {
				displayableOptionCount += optionGroup.toDisplayableList().size();
			}
			if (displayableOptionCount > 0) {
				System.out.print(" [OPTION]...");
			}
			if (this.programOperandsUsage != null) {
				System.out.printf(" %s", this.programOperandsUsage);
			}
			System.out.println();			
		}
		
		@Annotations.DisplaysProgramVersion
		@Annotations.Option(
				doc = "Display version information and exit",
				name = "version",
				type = OptionType.GNU_LONG
		)
		protected void displayProgramVersion() {
			String progVersion = this.programVersion;
			if (progVersion == null) {
				progVersion = this.programName;
				if (progVersion == null) {
					progVersion = this.getClass().getName();
				}
			}
			System.out.println(progVersion);
		}
		
		protected final int getArgCharIndex() {
			return this.argsParser.getArgCharIndex();
		}
		
		protected final int getArgIndex() {
			return this.argsParser.getArgIndex();
		}
		
		protected final String[] getArgs() {
			return this.argsParser.getArgs();
		}
		
		protected final OptionGroups getOptionGroups() {
			return this.argsParser.getOptionGroups();
		}
		
		protected final ParseResultHolder getParseResultHolder() {
			return this.argsParser.getParseResultHolder();
		}
		
		public final Optional<Integer> handleArgs() {
			this.argsParser = ArgsParser.newInstance(
					this.argsParser.getArgs(), 
					this.argsParser.getOptionGroups(), 
					this.posixlyCorrect);
			this.programHelpDisplayed = false;
			this.programVersionDisplayed = false;
			Optional<Integer> status = this.beforeHandleArgs();
			if (status.isPresent()) {
				return status;
			}
			while (this.hasNext()) {
				status = this.beforeHandleNext();
				if (status.isPresent()) {
					return status;
				}
				try {
					this.handleNext();
				} catch (Throwable t) {
					status = this.handleThrowable(t);
					if (status.isPresent()) {
						return status;
					}
				}
				if (this.programHelpDisplayed || this.programVersionDisplayed) {
					return Optional.of(Integer.valueOf(0));
				}
				status = this.afterHandleNext();
				if (status.isPresent()) {
					return status;
				}
			}
			return this.afterHandleArgs();
		}
		
		private final void handleNext() {
			this.argsParser.parseNext();
			ParseResultHolder parseResultHolder = 
					this.argsParser.getParseResultHolder();
			if (parseResultHolder.hasNonparsedArg()) {
				String nonparsedArg = parseResultHolder.getNonparsedArg();
				try {
					this.handleNonparsedArg(nonparsedArg);
				} catch (IllegalArgumentException e) {
					throw new IllegalArgException(nonparsedArg, e);
				}
			}
			if (parseResultHolder.hasOptionOccurrence()) {
				OptionOccurrence optionOccurrence = 
						parseResultHolder.getOptionOccurrence();
				Option option = optionOccurrence.getOption();
				Map<String, OptionGroupMethod> optionGroupMethodMap = 
						this.cliClass.getOptionGroupMethodMap();
				OptionGroupMethod optionGroupMethod = optionGroupMethodMap.get(
						option.toString());
				if (optionGroupMethod != null) {
					optionGroupMethod.invoke(this, optionOccurrence);
					if (optionGroupMethod.toMethod().equals(
							this.cliClass.getDisplayProgramHelpMethod())) {
						this.programHelpDisplayed = true;
					}
					if (optionGroupMethod.toMethod().equals(
							this.cliClass.getDisplayProgramVersionMethod())) {
						this.programVersionDisplayed = true;
					}
				}
			}
		}
		
		protected void handleNonparsedArg(final String nonparsedArg) { }
		
		protected Optional<Integer> handleThrowable(final Throwable t) {
			String progName = this.programName;
			if (progName == null) {
				progName = this.getClass().getName();
			}
			System.err.printf("%s: %s%n", progName, t);
			t.printStackTrace(System.err);
			return Optional.of(Integer.valueOf(-1));
		}
		
		protected final boolean hasNext() {
			return this.argsParser.hasNext();
		}
		
		public final boolean isProgramHelpDisplayed() {
			return this.programHelpDisplayed;
		}
		
		public final boolean isProgramVersionDisplayed() {
			return this.programVersionDisplayed;
		}
		
		protected final String next() {
			return this.argsParser.next();
		}
		
		@Override
		public String toString() {
			return this.getClass().getName();
		}
		
	}
	
	static final class CLIClass {
		
		private static final class MethodComparator 
			implements Comparator<Method> {

			public MethodComparator() { }
			
			@Override
			public int compare(final Method arg0, final Method arg1) {
				int value = arg0.getName().compareTo(arg1.getName());
				if (value != 0) {return value; }
				Class<?>[] parameterTypes0 = arg0.getParameterTypes();
				Class<?>[] parameterTypes1 = arg1.getParameterTypes();
				int length0 = parameterTypes0.length;
				int length1 = parameterTypes1.length;
				int maxLength = (length0 >= length1) ? length0 : length1;
				for (int i = 0; i < maxLength; i++) {
					if (i == length0 || i == length1) {
						return length0 - length1;
					}
					Class<?> parameterType0 = parameterTypes0[i];
					Class<?> parameterType1 = parameterTypes1[i];
					value = parameterType0.getName().compareTo(
							parameterType1.getName());
					if (value != 0) { return value; } 
				}
				return 0;
			}
			
		}
		
		private static final class OptionGroupMethodComparator 
			implements Comparator<OptionGroupMethod> {

			public OptionGroupMethodComparator() { }
			
			@Override
			public int compare(
					final OptionGroupMethod arg0, final OptionGroupMethod arg1) {
				int diff = arg0.getOrdinal() - arg1.getOrdinal();
				if (diff != 0) { return diff; }
				List<Option> options0 = arg0.getOptionGroup().toDisplayableList();
				List<Option> options1 = arg1.getOptionGroup().toDisplayableList();
				int size0 = options0.size();
				int size1 = options1.size();
				int maxSize = (size0 >= size1) ? size0 : size1;
				for (int i = 0; i < maxSize; i++) {
					if (i == size0 || i == size1) {
						return size0 - size1;
					}
					Option option0 = options0.get(i);
					Option option1 = options1.get(i);
					int value = option0.getName().compareTo(option1.getName());
					if (value != 0) { return value; } 
				}
				return 0;
			}
			
		}
		
		private static List<Class<?>> getHierarchy(final Class<?> cls) {
			List<Class<?>> hierarchy = new ArrayList<Class<?>>();
			if (CLI.class.isAssignableFrom(cls)) {
				hierarchy.add(cls);
				hierarchy.addAll(getHierarchy(cls.getSuperclass()));
			}
			return hierarchy;
		}
		
		public static CLIClass newInstance(final Class<? extends CLI> cls) {
			return new CLIClass(cls);
		}
		
		private final Class<? extends CLI> cls;
		private final Method displayProgramHelpMethod;
		private final Method displayProgramVersionMethod;
		private final Map<String, OptionGroupMethod> optionGroupMethodMap;
		private final List<OptionGroupMethod> optionGroupMethods;
		private final OptionGroups optionGroups;
		
		private CLIClass(final Class<? extends CLI> c) {
			Map<String, OptionGroupMethod> optGroupMethodMap = 
					new HashMap<String, OptionGroupMethod>();
			List<OptionGroupMethod> optGroupMethods = 
					new ArrayList<OptionGroupMethod>();
			Method displayProgHelpMethod = null;
			Method displayProgVersionMethod = null;
			List<Method> cliClassDeclaredMethods = Arrays.asList(
					CLI.class.getDeclaredMethods());
			for (Method cliClassDeclaredMethod : cliClassDeclaredMethods) {
				if (cliClassDeclaredMethod.isAnnotationPresent(
						Annotations.DisplaysProgramHelp.class)) {
					displayProgHelpMethod = cliClassDeclaredMethod;
				}
				if (cliClassDeclaredMethod.isAnnotationPresent(
						Annotations.DisplaysProgramVersion.class)) {
					displayProgVersionMethod = cliClassDeclaredMethod;
				}
			}			
			List<Class<?>> hierarchy = getHierarchy(c);
			Comparator<Method> methodComparator = new MethodComparator();
			Set<Method> methods = new TreeSet<Method>(methodComparator);
			for (Class<?> clazz : hierarchy) {
				methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
			}
			for (Method method : methods) {
				if (methodComparator.compare(
						displayProgHelpMethod, method) == 0) {
					displayProgHelpMethod = method;
				}
				if (methodComparator.compare(
						displayProgVersionMethod, method) == 0) {
					displayProgVersionMethod = method;
				}
				if (method.isAnnotationPresent(Annotations.Option.class)
						|| method.isAnnotationPresent(Annotations.Options.class)) {
					method.setAccessible(true);
					OptionGroupMethod mthd = OptionGroupMethod.newInstance(
							method);
					OptionGroup optGroup = mthd.getOptionGroup();
					for (Option opt : optGroup.toList()) {
						optGroupMethodMap.put(opt.toString(), mthd);
					}
					optGroupMethods.add(mthd);
				}
			}
			Collections.sort(optGroupMethods, new OptionGroupMethodComparator());
			List<OptionGroup> optGroups = new ArrayList<OptionGroup>();
			for (OptionGroupMethod optGroupMethod : optGroupMethods) {
				optGroups.add(optGroupMethod.getOptionGroup());
			}
			this.cls = c;
			this.displayProgramHelpMethod = displayProgHelpMethod;
			this.displayProgramVersionMethod = displayProgVersionMethod;
			this.optionGroupMethodMap = new HashMap<String, OptionGroupMethod>(
					optGroupMethodMap);
			this.optionGroupMethods = new ArrayList<OptionGroupMethod>(
					optGroupMethods);
			this.optionGroups = OptionGroups.newInstance(optGroups);
		}
		
		public Method getDisplayProgramHelpMethod() {
			return this.displayProgramHelpMethod;
		}
		
		public Method getDisplayProgramVersionMethod() {
			return this.displayProgramVersionMethod;
		}
		
		public Map<String, OptionGroupMethod> getOptionGroupMethodMap() {
			return Collections.unmodifiableMap(this.optionGroupMethodMap);
		}
		
		public List<OptionGroupMethod> getOptionGroupMethods() {
			return Collections.unmodifiableList(this.optionGroupMethods);
		}
		
		public OptionGroups getOptionGroups() {
			return this.optionGroups;
		}
		
		public Class<?> toClass() {
			return this.cls;
		}
		
	}
	
	static enum DefaultArgHandler implements ArgHandler {

		INSTANCE;
		
		@Override
		public void handle(final String arg, final ArgHandlerContext context) {
			ArgHandlerContextProperties properties =
					new ArgHandlerContextProperties(context);
			properties.setParseResultHolder(new ParseResultHolder(arg));
		}
		
		@Override
		public String toString() {
			return DefaultArgHandler.class.getSimpleName();
		}
		
	}
	
	public static final class DefaultGnuLongOptionUsageProvider 
		extends OptionUsageProvider {

		public DefaultGnuLongOptionUsageProvider() { }
		
		@Override
		public String getOptionUsage(final OptionUsageParams params) {
			String usage = null;
			String option = params.getOption();
			OptionArgSpec optionArgSpec = params.getOptionArgSpec();
			if (optionArgSpec == null) {
				usage = option;
			} else {
				String optionArgSeparator = optionArgSpec.getSeparator();
				String optionArgName = optionArgSpec.getName();
				if (optionArgSpec.isRequired()) {
					if (optionArgSeparator.equals(
							OptionArgSpec.DEFAULT_SEPARATOR)) {
						usage = String.format("%s=%s", option, optionArgName);
					} else {
						usage = String.format(
								"%1$s=%2$s1[%3$s%2$s2]...", 
								option, 
								optionArgName,
								optionArgSeparator);
					}
				} else {
					if (optionArgSeparator.equals(
							OptionArgSpec.DEFAULT_SEPARATOR)) {
						usage = String.format("%s[=%s]", option, optionArgName);
					} else {
						usage = String.format(
								"%1$s[=%2$s1[%3$s%2$s2]...]", 
								option, 
								optionArgName,
								optionArgSeparator);
					}
				}
			}
			return usage;
		}

		@Override
		public String toString() {
			return DefaultGnuLongOptionUsageProvider.class.getSimpleName();
		}
		
	}
	
	public static final class DefaultLongOptionUsageProvider 
		extends OptionUsageProvider {

		public DefaultLongOptionUsageProvider() { }
		
		@Override
		public String getOptionUsage(final OptionUsageParams params) {
			String usage = null;
			String option = params.getOption();
			OptionArgSpec optionArgSpec = params.getOptionArgSpec();
			if (optionArgSpec == null) {
				usage = option;
			} else {
				String optionArgSeparator = optionArgSpec.getSeparator();
				String optionArgName = optionArgSpec.getName();
				if (optionArgSpec.isRequired()) {
					if (optionArgSeparator.equals(
							OptionArgSpec.DEFAULT_SEPARATOR)) {
						usage = String.format("%s %s", option, optionArgName);
					} else {
						usage = String.format(
								"%1$s %2$s1[%3$s%2$s2]...", 
								option,
								optionArgName,
								optionArgSeparator);
					}
				} else {
					usage = option;
				}
			}		
			return usage;
		}
		
		@Override
		public String toString() {
			return DefaultLongOptionUsageProvider.class.getSimpleName();
		}

	}
	
	public static final class DefaultOptionGroupHelpTextProvider
		extends OptionGroupHelpTextProvider {
		
		public DefaultOptionGroupHelpTextProvider() { }

		@Override
		public String getOptionGroupHelpText(
				final OptionGroupHelpTextParams params) {
			String optionGroupHelpText = null;
			StringBuilder sb = null;
			String doc = null;
			for (Option option : params.getDisplayableOptions()) {
				if (sb == null) {
					sb = new StringBuilder();
					sb.append("  ");
				} else {
					sb.append(", ");
				}
				sb.append(option.getUsage());
				if (doc == null) {
					doc = option.getDoc();
				}
			}
			if (sb != null) {
				if (doc != null) {
					sb.append(System.getProperty("line.separator"));
					sb.append("      ");
					sb.append(doc);
				}
				optionGroupHelpText = sb.toString();
			}
			return optionGroupHelpText;
		}
		
	}

	public static final class DefaultPosixOptionUsageProvider 
		extends OptionUsageProvider {

		public DefaultPosixOptionUsageProvider() { }
		
		@Override
		public String getOptionUsage(final OptionUsageParams params) {
			String usage = null;
			String option = params.getOption();
			OptionArgSpec optionArgSpec = params.getOptionArgSpec();
			if (optionArgSpec == null) {
				usage = option;
			} else {
				String optionArgSeparator = optionArgSpec.getSeparator();
				String optionArgName = optionArgSpec.getName();
				if (optionArgSpec.isRequired()) {
					if (optionArgSeparator.equals(
							OptionArgSpec.DEFAULT_SEPARATOR)) {
						usage = String.format("%s %s", option, optionArgName);
					} else {
						usage = String.format(
								"%1$s %2$s1[%3$s%2$s2]...", 
								option, 
								optionArgName,
								optionArgSeparator);
					}
				} else {
					if (optionArgSeparator.equals(
							OptionArgSpec.DEFAULT_SEPARATOR)) {
						usage = String.format("%s[%s]", option, optionArgName);
					} else {
						usage = String.format(
								"%1$s[%2$s1[%3$s%2$s2]...]", 
								option, 
								optionArgName,
								optionArgSeparator);
					}
				}
			}
			return usage;
		}

		@Override
		public String toString() {
			return DefaultPosixOptionUsageProvider.class.getSimpleName();
		}
	}
	
	/**
	 * Default {@code StringConverter} that converts the provided 
	 * {@code String} to an {@code Object} of the provided type. This 
	 * {@code StringConverter} uses the provided type's public static method 
	 * that has only one method parameter of type {@code String} and a method 
	 * return type of the provided type. If the provided type does not have 
	 * that type of method, this {@code StringConverter} uses the provided 
	 * type's public instantiatable constructor that has only one constructor 
	 * parameter of type {@code String}. If the provided type has neither, a 
	 * {@code IllegalArgumentException} is thrown.
	 */
	public static final class DefaultStringConverter extends StringConverter {
		
		/**
		 * Returns the provided type's {@code Method} that is public and static 
		 * and has only one method parameter of type {@code String} and a method 
		 * return type of the provided type. If the provided type has no such 
		 * method, {@code null} is returned.
		 *  
		 * @param type the provided type
		 * 
		 * @return the provided type's {@code Method} that is public and static 
		 * and has only one method parameter of type {@code String} and a method 
		 * return type of the provided type or {@code null} if no such method 
		 * is found in the provided type
		 */
		private static Method getStaticStringConversionMethod(final Class<?> type) {
			for (Method method : type.getDeclaredMethods()) {
				int modifiers = method.getModifiers();
				Class<?> returnType = method.getReturnType();
				Class<?>[] parameterTypes = method.getParameterTypes();
				boolean isPublic = Modifier.isPublic(modifiers);
				boolean isStatic = Modifier.isStatic(modifiers);
				boolean isReturnTypeClass = returnType.equals(type);
				boolean isParameterTypeString = parameterTypes.length == 1 
						&& parameterTypes[0].equals(String.class);
				if (isPublic 
						&& isStatic 
						&& isReturnTypeClass 
						&& isParameterTypeString) {
					method.setAccessible(true);
					return method;
				}
			}
			return null;
		}
		
		/**
		 * Returns the provided type's {@code Constructor} that is public and 
		 * instantiatable and has only one constructor parameter of type 
		 * {@code String}. If the provided type has no such constructor, 
		 * {@code null} is returned.
		 * 
		 * @param type the provided type
		 * 
		 * @return the provided type's {@code Constructor} that is public and 
		 * instantiatable and has only one constructor parameter of type 
		 * {@code String} or {@code null} if no such constructor is found
		 */
		private static <T> Constructor<T> getStringParameterConstructor(
				final Class<T> type) {
			for (Constructor<?> constructor : type.getDeclaredConstructors()) {
				int modifiers = constructor.getModifiers();
				Class<?>[] parameterTypes = constructor.getParameterTypes();
				boolean isPublic = Modifier.isPublic(modifiers);
				boolean isInstantiatable = !Modifier.isAbstract(modifiers)
						&& !Modifier.isInterface(modifiers);
				boolean isParameterTypeString = parameterTypes.length == 1 
						&& parameterTypes[0].equals(String.class);
				if (isPublic && isInstantiatable && isParameterTypeString) {
					@SuppressWarnings("unchecked")
					Constructor<T> ctor = (Constructor<T>) constructor;
					ctor.setAccessible(true);
					return ctor;
				}
			}
			return null;
		}

		/** 
		 * The provided type of the {@code Object} to which the {@code String} 
		 * is converted. 
		 */
		private final Class<?> convertedType;
		
		/** 
		 * The {@code Method} that is public and static and has only one method 
		 * parameter of type {@code String} and a method return type of the 
		 * converted type. 
		 */
		private final Method staticStringConversionMethod;
		
		/**
		 * The {@code Constructor} that is public and instantiatable and has 
		 * only one constructor parameter of type {@code String}.
		 */
		private final Constructor<?> stringParameterConstructor;
		
		/**
		 * Constructs a {@code DefaultStringConverter} with the provided type.
		 * 
		 * @param type the provided type
		 * 
		 * @throws IllegalArgumentException if the provided type does not have 
		 * either a public static method that has only one method parameter of 
		 * type {@code String} and a method return type of the provided type 
		 * nor a public instantiatable constructor that has only one 
		 * constructor parameter of type {@code String}
		 */
		public DefaultStringConverter(final Class<?> type) {
			Objects.requireNonNull(type, "type must not be null");
			Method method = null;
			Constructor<?> constructor = null;
			if (!type.equals(String.class)) {
				method = getStaticStringConversionMethod(type);
				if (method == null) {
					constructor = getStringParameterConstructor(type);
				}
				if (method == null && constructor == null) {
					throw new IllegalArgumentException(String.format(
							"type %1$s does not have either a public static "
							+ "method that has one method parameter of type "
							+ "%2$s and a method return type of the provided "
							+ "type nor a public instantiatable constructor "
							+ "that has one constructor parameter of type %2$s", 
							type.getName(),
							String.class.getName()));
				}
			}
			this.convertedType = type;
			this.staticStringConversionMethod = method;
			this.stringParameterConstructor = constructor;
		}
		
		/**
		 * Converts the provided {@code String} to an {@code Object} of the 
		 * provided type. If the provided type is {@code String}, the provided 
		 * {@code String} is returned.
		 * 
		 * @param string the provided {@code String}
		 * 
		 * @return the converted {@code Object} of the provided type or the 
		 * provided {@code String} if the provided type is {@code String}
		 */
		@Override
		public Object convert(final String string) {
			Object object = null;
			if (this.convertedType.equals(String.class)) {
				object = string;
			} else {
				if (this.staticStringConversionMethod != null) {
					try {
						object = this.staticStringConversionMethod.invoke(
								null, string);
					} catch (IllegalAccessException e) {
						throw new AssertionError(e);
					} catch (IllegalArgumentException e) {
						throw new AssertionError(e);
					} catch (InvocationTargetException e) {
						Throwable cause = e.getCause();
						if (cause instanceof IllegalArgumentException) {
							IllegalArgumentException iae = 
									(IllegalArgumentException) cause;
							throw iae;
						}						
						throw new AssertionError(e);
					}
				} else if (this.stringParameterConstructor != null) {
					try {
						object = this.stringParameterConstructor.newInstance(
								string);
					} catch (InstantiationException e) {
						throw new AssertionError(e);
					} catch (IllegalAccessException e) {
						throw new AssertionError(e);
					} catch (IllegalArgumentException e) {
						throw new AssertionError(e);
					} catch (InvocationTargetException e) {
						Throwable cause = e.getCause();
						if (cause instanceof IllegalArgumentException) {
							IllegalArgumentException iae = 
									(IllegalArgumentException) cause;
							throw iae;
						}
						throw new AssertionError(e);
					}
				}
			}
			return object;
		}

		/**
		 * Returns the provided type of the {@code Object} to which the 
		 * {@code String} is converted.
		 * 
		 * @return the provided type of the {@code Object} to which the 
		 * {@code String} is converted
		 */
		public Class<?> getConvertedType() {
			return this.convertedType;
		}
		
		/**
		 * Returns the {@code String} representation of this 
		 * {@code DefaultStringConverter}.
		 * 
		 * @return the {@code String} representation of this 
		 * {@code DefaultStringConverter}
		 */
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(this.getClass().getSimpleName())
				.append(" [convertedType=")
				.append(this.convertedType)
				.append("]");
			return sb.toString();
		}

	}

	static final class EndOfOptionsArgHandler extends AbstractArgHandler {
		
		public EndOfOptionsArgHandler(final ArgHandler handler) {
			super(handler);
		}
		
		@Override
		public void handle(final String arg, final ArgHandlerContext context) {
			ArgHandlerContextProperties properties = 
					new ArgHandlerContextProperties(context); 
			if (properties.isOptionHandlingEnabled()) {
				properties.setOptionHandlingEnabled(false);
			}
			this.getArgHandler().handle(arg, context);
		}
		
	}

	/**
	 * Represents the delimiter signaling the end of the command line options.
	 */
	public static enum EndOfOptionsDelimiter {
		
		INSTANCE;  
		
		/** 
		 * The {@code String} representation of this 
		 * {@code EndOfOptionsDelimiter} as it would appear on the command line.
		 */
		private static final String STRING = "--";
		
		/**
		 * Returns the {@code String} representation of this 
		 * {@code EndOfOptionsDelimiter} as it would appear on the command line 
		 * ("--").
		 * 
		 * @return the {@code String} representation of this 
		 * {@code EndOfOptionsDelimiter} as it would appear on the command line 
		 * ("--")
		 */
		@Override
		public String toString() {
			return STRING;
		}

	}
	
	static final class EndOfOptionsDelimiterHandler	extends AbstractArgHandler {
		
		public EndOfOptionsDelimiterHandler(final ArgHandler argHandler) {
			super(argHandler);
		}
		
		@Override
		public void handle(final String arg, final ArgHandlerContext context) {
			ArgHandlerContextProperties properties = 
					new ArgHandlerContextProperties(context);
			if (!(properties.isOptionHandlingEnabled() && arg.equals(
					EndOfOptionsDelimiter.INSTANCE.toString()))) {
				this.getArgHandler().handle(arg, context);
				return;
			}
			properties.setOptionHandlingEnabled(false);
			properties.setParseResultHolder(new ParseResultHolder(
					EndOfOptionsDelimiter.INSTANCE));
		}

	}

	public static final class GnuLongOption extends Option {
		
		GnuLongOption(final GnuLongOptionBuilder gnuLongOptionBuilder) {
			super(gnuLongOptionBuilder);
		}

	}

	static final class GnuLongOptionBuilder extends OptionBuilder {

		public GnuLongOptionBuilder(final String optName) {
			super(optName, "--".concat(optName));
		}

		@Override
		public GnuLongOption build() {
			return new GnuLongOption(this);
		}
		
		@Override
		public GnuLongOptionBuilder getOptionArgSpec(
				final OptionArgSpec optArgSpec) {
			super.getOptionArgSpec(optArgSpec);
			return this;
		}
		
		@Override
		public GnuLongOptionBuilder setDisplayable(final boolean b) {
			super.setDisplayable(b);
			return this;
		}
		
		@Override
		public GnuLongOptionBuilder setDoc(final String d) {
			super.setDoc(d);
			return this;
		}
		
		@Override
		public GnuLongOptionBuilder setOptionUsageProvider(
				final OptionUsageProvider optUsageProvider) {
			super.setOptionUsageProvider(optUsageProvider);
			return this;
		}
		
		@Override
		public GnuLongOptionBuilder setUsage(final String u) {
			super.setUsage(u);
			return this;
		}
		
	}

	static final class GnuLongOptionHandler extends OptionHandler {
		
		public GnuLongOptionHandler(final ArgHandler handler) {
			super(handler, GnuLongOption.class);
		}

		@Override
		protected void handleOption(
				final String arg, final ArgHandlerContext context) {
			String option = arg;
			String optionArg = null;
			String[] argElements = arg.split("=", 2);
			if (argElements.length == 2) {
				option = argElements[0];
				optionArg = argElements[1];
			}
			ArgHandlerContextProperties properties = 
					new ArgHandlerContextProperties(context);
			Map<String, Option> optionMap = properties.getOptionMap();
			Option opt = optionMap.get(option);
			if (opt == null) {
				throw new UnknownOptionException(option);
			}
			OptionArgSpec optionArgSpec = opt.getOptionArgSpec();
			if (optionArg == null && optionArgSpec != null 
					&& optionArgSpec.isRequired()) {
				String[] args = context.getArgs();
				int argIndex = context.getArgIndex();
				if (argIndex < args.length - 1) {
					argIndex = context.incrementArgIndex();
					optionArg = args[argIndex];
				}
			}
			properties.setParseResultHolder(new ParseResultHolder(
					new OptionOccurrence(opt, opt.newOptionArg(optionArg))));
		}

		@Override
		protected boolean isOption(
				final String arg, final ArgHandlerContext context) {
			return arg.startsWith("--") && arg.length() > 2;
		}
		
	}

	/**
	 * Thrown to indicate that a provided command line argument is illegal or 
	 * inappropriate. 
	 */
	public static final class IllegalArgException extends RuntimeException {
		
		/** The default serial version UID. */
		private static final long serialVersionUID = 1L;
		
		/**
		 * Returns the detail message based on the provided illegal or 
		 * inappropriate command line argument, the specified detail message, 
		 * and the provided cause.
		 * 
		 * If the specified detail message is not {@code null}, the specified 
		 * detail message is returned. Otherwise, a detail message is created. 
		 * If the provided cause is not {@code null}, its {@code String} 
		 * representation is appended to that created detail message.  
		 * 
		 * @param arg the provided illegal or inappropriate command line 
		 * argument
		 * @param message the specified detail message (can be {@code null})
		 * @param cause the provided cause (can be {@code null})
		 * @return the specified detail message or a created detail message 
		 * appended with or without a {@code String} representation of the 
		 * provided cause 
		 */
		private static String getMessage(
				final String arg,
				final String message, 
				final Throwable cause) {
			if (message != null) {
				return message;
			}
			StringBuilder sb = new StringBuilder(String.format(
					"illegal argument `%s'", arg));
			if (cause != null) {
				sb.append(": ");
				sb.append(cause.toString());
			}
			return sb.toString();
		}
		
		/** The illegal or inappropriate command line argument. */
		private final String arg;

		/**
		 * Constructs an {@code IllegalArgException} with the provided illegal 
		 * or inappropriate command line argument.
		 * 
		 * @param a the provided illegal or inappropriate command line argument
		 */
		public IllegalArgException(final String a) {
			this(a, null, null);
		}
		
		/**
		 * Constructs an {@code IllegalArgException} with the provided illegal 
		 * or inappropriate command line argument and the specified detail 
		 * message.
		 * 
		 * @param a the provided illegal or inappropriate command line argument
		 * @param message the specified detail message (can be {@code null})
		 */
		public IllegalArgException(final String a, final String message) {
			this(a, message, null);
		}
		
		/**
		 * Constructs an {@code IllegalArgException} with the provided illegal 
		 * or inappropriate command line argument, the specified detail 
		 * message, and the provided cause.
		 * 
		 * @param a the provided illegal or inappropriate command line argument
		 * @param message the specified detail message (can be {@code null})
		 * @param cause the provided cause (can be {@code null})
		 */
		public IllegalArgException(
				final String a,
				final String message,
				final Throwable cause) {
			super(getMessage(a, message, cause), cause);
			this.arg = a;
		}
		
		/**
		 * Constructs an {@code IllegalArgException} with the provided illegal 
		 * or inappropriate command line argument and the provided cause.
		 * 
		 * @param a the provided illegal or inappropriate command line argument
		 * @param cause the provided cause (can be {@code null})
		 */
		public IllegalArgException(final String a, final Throwable cause) {
			this(a, null, cause);
		}
		
		/**
		 * Returns the illegal or inappropriate command line argument.
		 * 
		 * @return the illegal or inappropriate command line argument
		 */
		public String getArg() {
			return this.arg;
		}
		
	}
	
	/**
	 * Thrown when an {@code Option} is provided with a command line option 
	 * argument that is illegal or inappropriate.
	 */
	public static final class IllegalOptionArgException 
		extends RuntimeException {

		/** The default serial version UID. */
		private static final long serialVersionUID = 1L;

		/**
		 * Returns the detail message based on the provided {@code Option} of 
		 * the illegal or inappropriate command line option argument, the 
		 * provided illegal or inappropriate command line option argument, the 
		 * specified detail message, and the provided cause.
		 * 
		 * If the specified detail message is not {@code null}, the specified 
		 * detail message is returned. Otherwise, a detail message is created. 
		 * If the provided cause is not {@code null}, its {@code String} 
		 * representation is appended to that created detail message.  
		 * 
		 * @param option the provided {@code Option} of the illegal or 
		 * inappropriate command line option argument
		 * @param optionArg the provided illegal or inappropriate command line 
		 * option argument
		 * @param message the specified detail message (can be {@code null})
		 * @param cause the provided cause (can be {@code null})
		 * @return the specified detail message or a created detail message 
		 * appended with or without a {@code String} representation of the 
		 * provided cause 
		 */
		private static String getMessage(
				final Option option, 
				final String optionArg, 
				final String message, 
				final Throwable cause) {
			if (message != null) {
				return message;
			}
			StringBuilder sb = new StringBuilder(String.format(
					"illegal option argument `%s' for option `%s'", 
					optionArg, 
					option));
			if (cause != null) {
				sb.append(": ");
				sb.append(cause.toString());
			}
			return sb.toString();
		}
		
		/** 
		 * The {@code Option} of the illegal or inappropriate command line 
		 * option argument.
		 */
		private final Option option;
		
		/** The illegal or inappropriate command line option argument. */
		private final String optionArg;
		
		/**
		 * Constructs an {@code IllegalOptionArgException} with the provided 
		 * {@code Option} of the illegal or inappropriate command line option 
		 * argument and the provided illegal or inappropriate command line 
		 * option argument
		 * 
		 * @param opt the provided {@code Option} of the illegal or 
		 * inappropriate command line option argument
		 * @param optArg the provided illegal or inappropriate command line 
		 * option argument
		 */
		public IllegalOptionArgException(
				final Option opt, 
				final String optArg) {
			this(opt, optArg, null, null);
		}
		
		/**
		 * Constructs an {@code IllegalOptionArgException} with the provided 
		 * {@code Option} of the illegal or inappropriate command line option 
		 * argument, the provided illegal or inappropriate command line option 
		 * argument, and the specified detail message. 
		 * 
		 * @param opt the provided {@code Option} of the illegal or 
		 * inappropriate command line option argument
		 * @param optArg the provided illegal or inappropriate command line 
		 * option argument
		 * @param message the specified detail message (can be {@code null})
		 */
		public IllegalOptionArgException(
				final Option opt, 
				final String optArg, 
				final String message) {
			this(opt, optArg, message, null);
		}
		
		/**
		 * Constructs an {@code IllegalOptionArgException} with the provided 
		 * {@code Option} of the illegal or inappropriate command line option 
		 * argument, the provided illegal or inappropriate command line option 
		 * argument, the specified detail message, and the provided cause.
		 * 
		 * @param opt the provided {@code Option} of the illegal or 
		 * inappropriate command line option argument
		 * @param optArg the provided illegal or inappropriate command line 
		 * option argument
		 * @param message the specified detail message (can be {@code null})
		 * @param cause the provided cause (can be {@code null})
		 */
		public IllegalOptionArgException(
				final Option opt, 
				final String optArg, 
				final String message, 
				final Throwable cause) {
			super(getMessage(opt, optArg, message, cause), cause);
			this.option = opt;
			this.optionArg = optArg;
		}
		
		/**
		 * Constructs an {@code IllegalOptionArgException} with the provided 
		 * {@code Option} of the illegal or inappropriate command line option 
		 * argument, the provided illegal or inappropriate command line option 
		 * argument, and the provided cause.
		 * 
		 * @param opt the provided {@code Option} of the illegal or 
		 * inappropriate command line option argument
		 * @param optArg the provided illegal or inappropriate command line 
		 * option argument
		 * @param cause the provided cause (can be {@code null})
		 */
		public IllegalOptionArgException(
				final Option opt, 
				final String optArg, 
				final Throwable cause) {
			this(opt, optArg, null, cause);
		}

		/**
		 * Returns the {@code Option} of the illegal or inappropriate command 
		 * line option argument.
		 *  
		 * @return the {@code Option} of the illegal or inappropriate command 
		 * line option argument
		 */
		public Option getOption() {
			return this.option;
		}
		
		/**
		 * Returns the illegal or inappropriate command line option argument.
		 * 
		 * @return the illegal or inappropriate command line option argument
		 */
		public String getOptionArg() {
			return this.optionArg;
		}
		
	}

	static final class InterpolatedOptionGroupHelpTextProvider 
		extends OptionGroupHelpTextProvider {

		private final String string;
		
		public InterpolatedOptionGroupHelpTextProvider(final String str) {
			this.string = str;
		}
		
		@Override
		public String getOptionGroupHelpText(
				final OptionGroupHelpTextParams params) {
			Properties properties = new Properties();
			int index = -1;
			for (Option option : params.getDisplayableOptions()) {
				String optionPropertyName = String.format("option%s", ++index);
				properties.setProperty(optionPropertyName, option.toString());
				String doc = option.getDoc();
				if (doc != null) {
					properties.setProperty(
							optionPropertyName.concat(".doc"), doc);
				}
				properties.setProperty(
						optionPropertyName.concat(".name"),	option.getName());
				OptionArgSpec optionArgSpec = option.getOptionArgSpec();
				if (optionArgSpec != null) {
					properties.setProperty(
							optionPropertyName.concat(".optionArgSpec.name"), 
							optionArgSpec.getName());
					properties.setProperty(
							optionPropertyName.concat(".optionArgSpec.separator"), 
							optionArgSpec.getSeparator());
				}
				properties.setProperty(
						optionPropertyName.concat(".usage"), option.getUsage());
			}
			Properties props = new Properties(System.getProperties());
			PropertiesHelper.copy(properties, props);
			return StringInterpolator.interpolate(this.string, props);
		}
		
	}
	
	static final class InterpolatedOptionUsageProvider 
		extends OptionUsageProvider {

		private final String string;
		
		public InterpolatedOptionUsageProvider(final String str) {
			this.string = str;
		}
		
		@Override
		public String getOptionUsage(final OptionUsageParams params) {
			Properties properties = new Properties();
			properties.setProperty("option", params.getOption());
			OptionArgSpec optionArgSpec = params.getOptionArgSpec();
			if (optionArgSpec != null) {
				properties.setProperty(
						"option.optionArgSpec.name", 
						optionArgSpec.getName());
				properties.setProperty(
						"option.optionArgSpec.separator", 
						optionArgSpec.getSeparator());
			}
			Properties props = new Properties(System.getProperties());
			PropertiesHelper.copy(properties, props);
			return StringInterpolator.interpolate(this.string, props);
		}
		
	}
	
	public static final class LongOption extends Option {
		
		LongOption(final LongOptionBuilder longOptionBuilder) {
			super(longOptionBuilder);
		}
		
	}
	
	static final class LongOptionBuilder extends OptionBuilder {

		public LongOptionBuilder(final String optName) {
			super(optName, "-".concat(optName));
		}

		@Override
		public LongOption build() {
			return new LongOption(this);
		}
		
		@Override
		public LongOptionBuilder getOptionArgSpec(
				final OptionArgSpec optArgSpec) {
			super.getOptionArgSpec(optArgSpec);
			return this;
		}
		
		@Override
		public LongOptionBuilder setDisplayable(final boolean b) {
			super.setDisplayable(b);
			return this;
		}

		@Override
		public LongOptionBuilder setDoc(final String d) {
			super.setDoc(d);
			return this;
		}
		
		@Override
		public LongOptionBuilder setOptionUsageProvider(
				final OptionUsageProvider optUsageProvider) {
			super.setOptionUsageProvider(optUsageProvider);
			return this;
		}
		
		@Override
		public LongOptionBuilder setUsage(final String u) {
			super.setUsage(u);
			return this;
		}
		
	}
	
	static final class LongOptionHandler extends OptionHandler {
		
		public LongOptionHandler(final ArgHandler handler) {
			super(new PosixOptionHandler(handler), LongOption.class);
		}

		@Override
		protected void handleOption(
				final String arg, final ArgHandlerContext context) {
			String option = arg;
			ArgHandlerContextProperties properties = 
					new ArgHandlerContextProperties(context);
			Map<String, Option> optionMap = properties.getOptionMap();
			Option opt = optionMap.get(option);
			if (opt == null) {
				OptionHandler posixOptionHandler = 
						(PosixOptionHandler) this.getArgHandler();
				boolean hasPosixOption = false;
				for (Option o : optionMap.values()) {
					if (posixOptionHandler.getOptionClass().isInstance(o)) {
						hasPosixOption = true;
						break;
					}
				}
				if (hasPosixOption) {
					posixOptionHandler.handleOption(arg, context);
					return;
				}
				throw new UnknownOptionException(option);
			}
			String optionArg = null;
			OptionArgSpec optionArgSpec = opt.getOptionArgSpec();
			if (optionArgSpec != null && optionArgSpec.isRequired()) {
				String[] args = context.getArgs();
				int argIndex = context.getArgIndex();
				if (argIndex < args.length - 1) {
					argIndex = context.incrementArgIndex();
					optionArg = args[argIndex];
				}
			}
			properties.setParseResultHolder(new ParseResultHolder(
					new OptionOccurrence(
							opt, opt.newOptionArg(optionArg))));
		}

		@Override
		protected boolean isOption(
				final String arg, final ArgHandlerContext context) {
			return arg.length() > 1 
					&& arg.startsWith("-") 
					&& !arg.startsWith("--");
		}
		
	}

	public static abstract class Option {
		
		private final boolean displayable;
		private final String doc;
		private final String name;
		private final OptionArgSpec optionArgSpec;
		private final OptionUsageProvider optionUsageProvider;
		private final String string;

		Option(final OptionBuilder optionBuilder) {
			boolean display = optionBuilder.isDisplayable();
			String d = optionBuilder.getDoc();
			String n = optionBuilder.getName();
			OptionArgSpec optArgSpec = optionBuilder.getOptionArgSpec();
			OptionUsageProvider optUsageProvider = 
					optionBuilder.getOptionUsageProvider();
			boolean optUsageProviderSet = optionBuilder.hasOptionUsageProviderSet();
			String str = optionBuilder.getString();
			String u = optionBuilder.getUsage();
			if (d != null) {
				d = StringInterpolator.interpolate(d, System.getProperties());
			}
			if (!optUsageProviderSet) {
				optUsageProvider = OptionUsageProvider.getDefault(
						this.getClass());
			}
			if (u != null) {
				optUsageProvider = new InterpolatedOptionUsageProvider(u);
			}
			this.displayable = display;
			this.doc = d;
			this.name = n;
			this.optionArgSpec = optArgSpec;
			this.optionUsageProvider = optUsageProvider;
			this.string = str;
		}
		
		public final String getDoc() {
			return this.doc;
		}
		
		public final String getName() {
			return this.name;
		}
		
		public final OptionArgSpec getOptionArgSpec() {
			return this.optionArgSpec;
		}

		public final OptionUsageProvider getOptionUsageProvider() {
			return this.optionUsageProvider;
		}

		public final String getUsage() {
			if (this.optionUsageProvider != null) {
				return this.optionUsageProvider.getOptionUsage(
						new OptionUsageParams(this));
			}
			return null;
		}
		
		public final boolean isDisplayable() {
			return this.displayable;
		}
		
		public final boolean isOf(final String opt) {
			return this.string.equals(opt);
		}
		
		public final boolean isOfAnyOf(final List<String> opts) {
			for (String opt : opts) {
				if (this.isOf(opt)) {
					return true;
				}
			}
			return false;
		}
		
		public final boolean isOfAnyOf(final String opt1, final String opt2) {
			return this.isOfAnyOf(Arrays.asList(opt1, opt2));
		}
		
		public final boolean isOfAnyOf(final String opt1, final String opt2, 
				final String opt3) {
			return this.isOfAnyOf(Arrays.asList(opt1, opt2, opt3));
		}
		
		public final boolean isOfAnyOf(final String opt1, final String opt2, 
				final String opt3, final String... additionalOpts) {
			List<String> opts = new ArrayList<String>();
			opts.add(opt1);
			opts.add(opt2);
			opts.add(opt3);
			opts.addAll(Arrays.asList(additionalOpts));
			return this.isOfAnyOf(opts);
		}
		
		public final OptionArg newOptionArg(final String optionArg) {
			if (this.optionArgSpec == null && optionArg != null) {
				throw new OptionArgNotAllowedException(this);
			}
			OptionArg optArg = null;
			if (this.optionArgSpec != null && optionArg != null) {
				try {
					optArg = this.optionArgSpec.newOptionArg(optionArg);
				} catch (NullPointerException e) {
					throw new OptionArgRequiredException(this);
				} catch (IllegalArgumentException e) {
					throw new IllegalOptionArgException(this, optionArg, e);
				}
			}
			return optArg;
		}
		
		@Override
		public final String toString() {
			return this.string;
		}

	}

	/**
	 * Represents all values of {@code Boolean} including an unspecified value 
	 * ({@code null}). It is used primarily for annotations in 
	 * {@link ArgMatey.Annotations}.
	 */
	public static enum OptionalBoolean {
		
		/** {@code Boolean} value of {@code false}. */
		FALSE(Boolean.FALSE),
		
		/** {@code Boolean} value of {@code true}. */
		TRUE(Boolean.TRUE),
		
		/** Unspecified {@code Boolean} value ({@code null}). */
		UNSPECIFIED(null);
		
		/** The optional {@code Boolean} value. */
		private final Optional<Boolean> optionalBooleanValue;
		
		/**
		 * Constructs an {@code OptionalBoolean} based on the provided 
		 * {@code Boolean}.
		 * 
		 * @param booleanValue the provided {@code Boolean} (can be 
		 * {@code null})
		 */
		private OptionalBoolean(final Boolean booleanValue) {
			this.optionalBooleanValue = Optional.ofNullable(booleanValue);
		}
		
		/**
		 * Returns the optional {@code Boolean} value.
		 * 
		 * @return the optional {@code Boolean} value
		 */
		public Optional<Boolean> optionalBooleanValue() {
			return this.optionalBooleanValue;
		}
		
	}
	
	/**
	 * Represents a command line option argument.
	 */
	public static final class OptionArg {
		
		/** The {@code Object} value of this {@code OptionArg}. */
		private final Object objectValue;
		
		/** 
		 * The {@code boolean} value that determines if the {@code Object} 
		 * value of this {@code OptionArg} is set.
		 */
		private final boolean objectValueSet;
		
		/** 
		 * The {@code List} of sub {@code OptionArg}s if any.
		 */
		private final List<OptionArg> optionArgs;
		
		/** 
		 * The {@code String} representation of this {@code OptionArg} as it 
		 * appears in the command line. 
		 */
		private final String string;
		
		/**
		 * Constructs a node {@code OptionArg} based on the provided command 
		 * line option argument and the provided {@code List} of sub 
		 * {@code OptionArg}s.
		 * 	
		 * @param optArg the provided command line option argument
		 * @param optArgs the provided {@code List} of sub {@code OptionArg}s
		 */
		OptionArg(final String optArg, final List<OptionArg> optArgs) {
			this.objectValue = null;
			this.objectValueSet = false;
			this.optionArgs = new ArrayList<OptionArg>(optArgs);
			this.string = optArg;
		}
		
		/**
		 * Constructs a leaf {@code OptionArg} based on the provided command 
		 * line option argument and the provided {@code Object} value.
		 * 
		 * @param optArg the provided command line option argument
		 * @param objValue the provided {@code Object} value
		 */
		OptionArg(final String optArg, final Object objValue) {
			this.objectValue = objValue;
			this.objectValueSet = true;
			this.optionArgs = new ArrayList<OptionArg>();
			this.string = optArg;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof OptionArg)) {
				return false;
			}
			OptionArg other = (OptionArg) obj;
			if (this.objectValue == null) {
				if (other.objectValue != null) {
					return false;
				}
			} else if (!this.objectValue.equals(other.objectValue)) {
				return false;
			}
			if (this.optionArgs == null) {
				if (other.optionArgs != null) {
					return false;
				}
			} else if (!this.optionArgs.equals(other.optionArgs)) {
				return false;
			}
			return true;
		}

		/**
		 * Return the {@code Object} value of this {@code OptionArg}. If there 
		 * are more than one {@code Object} value, the first {@code Object} 
		 * value is returned.
		 * 
		 * @return the {@code Object} value of this {@code OptionArg} or the 
		 * first {@code Object} value if there are more than one {@code Object} 
		 * value
		 */
		public Object getObjectValue() {
			if (this.objectValueSet) {
				return this.objectValue;
			}
			return this.getObjectValues().get(0);
		}

		/**
		 * Returns an unmodifiable {@code List} of {@code Object} values of 
		 * this {@code OptionArg}.
		 * 
		 * @return an unmodifiable {@code List} of {@code Object} values of 
		 * this {@code OptionArg}
		 */
		public List<Object> getObjectValues() {
			List<Object> objectValues = new ArrayList<Object>();
			if (this.objectValueSet) {
				objectValues.add(this.objectValue);
			} else {
				for (OptionArg optionArg : this.optionArgs) {
					objectValues.addAll(optionArg.getObjectValues());
				}
			}
			return Collections.unmodifiableList(objectValues);
		}
		
		/**
		 * Returns an unmodifiable {@code List} of sub {@code OptionArg}s.
		 * 
		 * @return an unmodifiable {@code List} of sub {@code OptionArg}s
		 */
		public List<OptionArg> getOptionArgs() {
			return Collections.unmodifiableList(this.optionArgs);
		}
		
		/**
		 * Returns the {@code Object} value of this {@code OptionArg} as the 
		 * specified type. If there are more than one {@code Object} values, 
		 * the first {@code Object} value is returned as the specified type.
		 * 
		 * @param type the specified type
		 * 
		 * @throws ClassCastException if the {@code Object} value of this 
		 * {@code OptionArg} is not of the specified type
		 * 
		 * @return the {@code Object} value of this {@code OptionArg} as the 
		 * specified type or the the first {@code Object} value as the 
		 * specified type if there are more than one {@code Object} value
		 */
		public <T> T getTypeValue(final Class<T> type) {
			return type.cast(this.getObjectValue());
		}
		
		/**
		 * Returns an unmodifiable {@code List} of the specified type of 
		 * {@code Object} values of this {@code OptionArg}.
		 *  
		 * @param type the specified type
		 * 
		 * @throws ClassCastException if any of the {@code Object} values of 
		 * this {@code OptionArg} are not of the specified type  
		 * 
		 * @return an unmodifiable {@code List} of the specified type of 
		 * {@code Object} values of this {@code OptionArg}
		 */
		public <T> List<T> getTypeValues(final Class<T> type) {
			List<T> typeValues = new ArrayList<T>();
			for (Object objectValue : this.getObjectValues()) {
				typeValues.add(type.cast(objectValue));
			}
			return Collections.unmodifiableList(typeValues);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((this.objectValue == null) ? 
					0 : objectValue.hashCode());
			result = prime * result + ((this.optionArgs == null) ? 
					0 : optionArgs.hashCode());
			return result;
		}

		/**
		 * Returns the {@code String} representation of this {@code OptionArg} 
		 * as it appears in the command line.
		 * 
		 * @return the {@code String} representation of this {@code OptionArg} 
		 * as it appears in the command line
		 */
		@Override
		public String toString() {
			return this.string;
		}
		
	}

	/**
	 * Thrown when an {@code Option} is provided with a command line option 
	 * argument that is not allowed.
	 */
	public static final class OptionArgNotAllowedException 
		extends RuntimeException {
		
		/** The default serial version UID. */
		private static final long serialVersionUID = 1L;
		
		/** 
		 * The {@code Option} that was provided with a command line option 
		 * argument that is not allowed.
		 */
		private final Option option;
		
		/**
		 * Constructs an {@code OptionArgNotAllowedException} with the provided 
		 * {@code Option} that was provided with a command line option argument 
		 * that is not allowed.
		 *  
		 * @param opt the provided {@code Option} that was provided with a 
		 * command line option argument that is not allowed
		 */
		OptionArgNotAllowedException(final Option opt) {
			super(String.format(
					"option `%s' does not allow an argument", 
					opt.toString()));
			this.option = opt;
		}
		
		/**
		 * Returns the {@code Option} that was provided with a command line 
		 * option argument that is not allowed.
		 * 
		 * @return the {@code Option} that was provided with a command line 
		 * option argument that is not allowed
		 */
		public Option getOption() {
			return this.option;
		}

	}
	
	/**
	 * Thrown when an {@code Option} is not provided with a required 
	 * command line option argument.
	 */
	public static final class OptionArgRequiredException 
		extends RuntimeException {
		
		/** The default serial version UID. */
		private static final long serialVersionUID = 1L;
		
		/** 
		 * The {@code Option} that was not provided with a required command 
		 * line option argument. 
		 */
		private final Option option;
		
		/**
		 * Constructs an {@code OptionArgRequiredException} with the provided 
		 * {@code Option} that was not provided with a required command line 
		 * option argument.
		 * 
		 * @param opt the provided {@code Option} that was not provided with a 
		 * required command line option argument
		 */
		OptionArgRequiredException(final Option opt) {
			super(String.format(
					"option `%s' requires an argument", 
					opt.toString()));
			this.option = opt;
		}
		
		/**
		 * Returns the {@code Option} that was not provided with a required 
		 * command line option argument.
		 * 
		 * @return the {@code Option} that was not provided with a required 
		 * command line option argument
		 */
		public Option getOption() {
			return this.option;
		}

	}

	public static final class OptionArgSpec {
		
		public static final String DEFAULT_SEPARATOR = "[^\\w\\W]";
		public static final Class<?> DEFAULT_TYPE = String.class;
			
		private final String name;
		private final boolean required;
		private final String separator;
		private final StringConverter stringConverter;
		private final Class<?> type;
		
		OptionArgSpec(final OptionArgSpecBuilder optionArgSpecBuilder) {
			String n = optionArgSpecBuilder.getName();
			boolean r = optionArgSpecBuilder.isRequired();
			String s = optionArgSpecBuilder.getSeparator();
			StringConverter sc = optionArgSpecBuilder.getStringConverter();
			Class<?> t = optionArgSpecBuilder.getType();
			if (s == null) { s = DEFAULT_SEPARATOR;	}
			if (t == null) { t = DEFAULT_TYPE; }
			if (n == null) { n = t.getName(); }
			if (sc == null) { sc = new DefaultStringConverter(t); }
			this.name = n;
			this.required = r;
			this.separator = s;
			this.stringConverter = sc;
			this.type = t;
		}
		
		public String getName() {
			return this.name;
		}
		
		public String getSeparator() {
			return this.separator;
		}
		
		public StringConverter getStringConverter() {
			return this.stringConverter;
		}
		
		public Class<?> getType() {
			return this.type;
		}

		public boolean isRequired() {
			return this.required;
		}

		public OptionArg newOptionArg(final String optionArg) {
			if (this.required && optionArg == null) {
				Objects.requireNonNull(
						optionArg, "option argument must not be null");
			}
			if (!this.required && optionArg == null) {
				return null;
			}
			List<String> optArgs = Arrays.asList(optionArg.split(
					this.separator));
			if (optArgs.size() == 1) {
				Object objectValue = this.stringConverter.convert(optionArg);
				return new OptionArg(optionArg, objectValue);
			} else {
				List<OptionArg> list = new ArrayList<OptionArg>();
				for (String optArg : optArgs) {
					Object objectValue = this.stringConverter.convert(optArg);
					list.add(new OptionArg(optArg, objectValue));
				}
				return new OptionArg(optionArg, list);
			}
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(this.getClass().getSimpleName())
				.append(" [name=")
				.append(this.name)
				.append(", required=")
				.append(this.required)
				.append(", separator=")
				.append(this.separator)
				.append(", stringConverter=")
				.append(this.stringConverter)			
				.append(", type=")
				.append(this.type)
				.append("]");
			return sb.toString();
		}

	}

	static final class OptionArgSpecBuilder {
				
		private String name;
		private boolean required;
		private String separator;
		private StringConverter stringConverter;
		private Class<?> type;
		
		public OptionArgSpecBuilder() {
			this.name = null;
			this.required = true;
			this.separator = null;
			this.stringConverter = null;
			this.type = null;
		}
		
		public OptionArgSpec build() {
			return new OptionArgSpec(this); 
		}
		
		public final String getName() {
			return this.name;
		}
		
		public final String getSeparator() {
			return this.separator;
		}
		
		public final StringConverter getStringConverter() {
			return this.stringConverter;
		}
		
		public final Class<?> getType() {
			return this.type;
		}
		
		public final boolean isRequired() {
			return this.required;
		}
		
		public OptionArgSpecBuilder setName(final String n) {
			this.name = n;
			return this;
		}
		
		public OptionArgSpecBuilder setRequired(final boolean b) {
			this.required = b;
			return this;
		}
		
		public OptionArgSpecBuilder setSeparator(final String s) {
			this.separator = s;
			return this;
		}
		
		public OptionArgSpecBuilder setStringConverter(
				final StringConverter sc) {
			this.stringConverter = sc;
			return this;
		}
		
		public OptionArgSpecBuilder setType(final Class<?> t) {
			this.type = t;
			return this;
		}
		
	}
	
	static abstract class OptionBuilder {

		private boolean displayable;
		private boolean displayableSet;
		private String doc;
		private final String name;
		private OptionArgSpec optionArgSpec;
		private boolean optionArgSpecSet;
		private OptionUsageProvider optionUsageProvider;
		private boolean optionUsageProviderSet;
		private String string;
		private String usage;
		
		OptionBuilder(final String optName, final String opt) {
			Objects.requireNonNull(optName, "option name must not be null");
			if (optName.matches("\\s")) {
				throw new IllegalArgumentException(
						"option name must not contain any whitespace "
						+ "characters");
			}
			if (optName.isEmpty()) {
				throw new IllegalArgumentException(
						"option name must not be empty");
			}
			Objects.requireNonNull(opt, "option must not be null");
			if (opt.matches("\\s")) {
				throw new IllegalArgumentException(
						"option must not contain any whitespace "
						+ "characters");
			}
			if (opt.isEmpty()) {
				throw new IllegalArgumentException(
						"option must not be empty");
			}
			this.displayable = true;
			this.displayableSet = false;
			this.doc = null;
			this.name = optName;
			this.optionArgSpec = null;
			this.optionArgSpecSet = false;
			this.optionUsageProvider = null;
			this.optionUsageProviderSet = false;
			this.string = opt;
			this.usage = null;
		}
		
		public abstract Option build();
		
		public final String getDoc() {
			return this.doc;
		}
		
		public final String getName() {
			return this.name;
		}
		
		public final OptionArgSpec getOptionArgSpec() {
			return this.optionArgSpec;
		}
		
		public OptionBuilder getOptionArgSpec(final OptionArgSpec optArgSpec) {
			this.optionArgSpec = optArgSpec;
			this.optionArgSpecSet = true;
			return this;
		}
		
		public final OptionUsageProvider getOptionUsageProvider() {
			return this.optionUsageProvider;
		}
		
		public final String getString() {
			return this.string;
		}
		
		public final String getUsage() {
			return this.usage;
		}
		
		public final boolean hasDisplayableSet() {
			return this.displayableSet;
		}
		
		public final boolean hasOptionArgSpecSet() {
			return this.optionArgSpecSet;
		}
		
		public final boolean hasOptionUsageProviderSet() {
			return this.optionUsageProviderSet;
		}
		
		public final boolean isDisplayable() {
			return this.displayable;
		}
		
		public OptionBuilder setDisplayable(final boolean b) {
			this.displayable = b;
			this.displayableSet = true;
			return this;
		}
		
		public OptionBuilder setDoc(final String d) {
			this.doc = d;
			return this;
		}
		
		public OptionBuilder setOptionUsageProvider(
				final OptionUsageProvider optUsageProvider) {
			this.optionUsageProvider = optUsageProvider;
			this.optionUsageProviderSet = true;
			return this;
		}
		
		public OptionBuilder setUsage(final String u) {
			this.usage = u;
			return this;
		}
		
	}

	public static final class OptionGroup {
		
		private final List<Option> displayableOptions;
		private final List<Option> options;
		private final OptionGroupHelpTextProvider optionGroupHelpTextProvider;
		
		OptionGroup(final OptionGroupBuilder optionGroupBuilder) {
			String h = optionGroupBuilder.getHelpText();
			List<OptionBuilder> optBuilders = new ArrayList<OptionBuilder>(
					optionGroupBuilder.getOptionBuilders());
			OptionGroupHelpTextProvider optGroupHelpTextProvider =
					optionGroupBuilder.getOptionGroupHelpTextProvider();
			boolean optGroupHelpTextProviderSet = 
					optionGroupBuilder.hasOptionGroupHelpTextProviderSet();
			List<Option> displayableOpts = new ArrayList<Option>();
			List<Option> opts = new ArrayList<Option>();
			Iterator<OptionBuilder> iterator = optBuilders.iterator();
			OptionBuilder firstOptBuilder = null;
			while (iterator.hasNext()) {
				OptionBuilder optBuilder = iterator.next();
				if (firstOptBuilder == null) {
					firstOptBuilder = optBuilder;
				} else {
					if (firstOptBuilder.hasDisplayableSet() 
							&& !optBuilder.hasDisplayableSet()) {
						optBuilder.setDisplayable(firstOptBuilder.isDisplayable());
					}
					if (firstOptBuilder.hasOptionArgSpecSet() 
							&& !optBuilder.hasOptionArgSpecSet()) {
						optBuilder.getOptionArgSpec(firstOptBuilder.getOptionArgSpec());
					}
				}
				Option opt = optBuilder.build();
				opts.add(opt);
				if (opt.isDisplayable()) {
					String usage = opt.getUsage();
					if (usage != null) {
						displayableOpts.add(opt);
					}
				}
			}
			if (!optGroupHelpTextProviderSet) {
				optGroupHelpTextProvider = 
						OptionGroupHelpTextProvider.getDefault();
			}
			if (h != null) {
				optGroupHelpTextProvider = 
						new InterpolatedOptionGroupHelpTextProvider(h);
			}
			this.displayableOptions = new ArrayList<Option>(displayableOpts);
			this.options = new ArrayList<Option>(opts);
			this.optionGroupHelpTextProvider = optGroupHelpTextProvider;
		}
		
		public Option get(final int index) {
			return this.options.get(index);
		}
		
		public String getHelpText() {
			if (this.optionGroupHelpTextProvider != null) {
				return this.optionGroupHelpTextProvider.getOptionGroupHelpText(
								new OptionGroupHelpTextParams(this));
			}
			return null;
		}
		
		public OptionGroupHelpTextProvider getOptionGroupHelpTextProvider() {
			return this.optionGroupHelpTextProvider;
		}
		
		public List<Option> toDisplayableList() {
			return Collections.unmodifiableList(this.displayableOptions);
		}
		
		public List<Option> toList() {
			return Collections.unmodifiableList(this.options);
		}
		
		@Override
		public final String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(this.getClass().getSimpleName())
				.append(" [options=")
				.append(this.options)
				.append("]");
			return sb.toString();
		}
		
	}

	static final class OptionGroupBuilder {
		
		private String helpText;
		private final List<OptionBuilder> optionBuilders;
		private OptionGroupHelpTextProvider optionGroupHelpTextProvider;
		private boolean optionGroupHelpTextProviderSet;
					
		public OptionGroupBuilder() {
			this.helpText = null;
			this.optionBuilders = new ArrayList<OptionBuilder>();
			this.optionGroupHelpTextProvider = null;
			this.optionGroupHelpTextProviderSet = false;
		}
		
		public OptionGroup build() {
			return new OptionGroup(this); 
		}
		
		public final String getHelpText() {
			return this.helpText;
		}
		
		public final List<OptionBuilder> getOptionBuilders() {
			return Collections.unmodifiableList(this.optionBuilders);
		}
		
		public final OptionGroupHelpTextProvider getOptionGroupHelpTextProvider() {
			return this.optionGroupHelpTextProvider;
		}
		
		public final boolean hasOptionGroupHelpTextProviderSet() {
			return this.optionGroupHelpTextProviderSet;
		}
		
		public OptionGroupBuilder setHelpText(final String h) {
			this.helpText = h;
			return this;
		}
		
		public OptionGroupBuilder setOptionBuilders(
				final List<OptionBuilder> optBuilders) {
			for (OptionBuilder otherOptBuilder : optBuilders) {
				Objects.requireNonNull(
						otherOptBuilder, 
						"other Option.Builder(s) must not be null");
			}
			this.optionBuilders.clear();
			this.optionBuilders.addAll(optBuilders);
			return this;
		}
		
		public OptionGroupBuilder setOptionBuilders(final OptionBuilder optBuilder) {
			return this.setOptionBuilders(Arrays.asList(optBuilder));
		}
		
		public OptionGroupBuilder setOptionBuilders(
				final OptionBuilder optBuilder1,
				final OptionBuilder optBuilder2) {
			return this.setOptionBuilders(Arrays.asList(
					optBuilder1, optBuilder2));
		}
		
		public OptionGroupBuilder setOptionBuilders(
				final OptionBuilder optBuilder1,
				final OptionBuilder optBuilder2,
				final OptionBuilder... additionalOptBuilders) {
			List<OptionBuilder> list = new ArrayList<OptionBuilder>();
			list.add(optBuilder1);
			list.add(optBuilder2);
			list.addAll(Arrays.asList(additionalOptBuilders));
			return this.setOptionBuilders(list);
		}
		
		public OptionGroupBuilder setOptionGroupHelpTextProvider(
				final OptionGroupHelpTextProvider optGroupHelpTextProvider) {
			this.optionGroupHelpTextProvider = optGroupHelpTextProvider;
			this.optionGroupHelpTextProviderSet = true;
			return this;
		}
		
	}
	
	public static final class OptionGroupHelpTextParams {
		
		private final OptionGroup optionGroup;
		
		OptionGroupHelpTextParams(final OptionGroup optGroup) {
			this.optionGroup = optGroup;
		}
		
		public List<Option> getDisplayableOptions() {
			return this.optionGroup.toDisplayableList();
		}
		
	}
	
	public static abstract class OptionGroupHelpTextProvider {
		
		private static OptionGroupHelpTextProvider defaultInstance = 
				new DefaultOptionGroupHelpTextProvider();
		
		public static OptionGroupHelpTextProvider getDefault() {
			return defaultInstance;
		}
		
		public static void setDefault(
				final OptionGroupHelpTextProvider optGroupHelpTextProvider) {
			defaultInstance = optGroupHelpTextProvider;
		}
		
		public OptionGroupHelpTextProvider() { }
		
		public abstract String getOptionGroupHelpText(
				final OptionGroupHelpTextParams params);
		
	}
	
	static final class OptionGroupMethod {
		
		private static enum TargetMethodParameterTypesType {
			
			BOOLEAN_PRIMITIVE {

				@Override
				public Class<?> getTargetClass(final Method method) {
					return null;
				}

				@Override
				public String getTargetMethodParameterTypesString() {
					return String.format("(%s)", boolean.class.getName());
				}

				@Override
				public void invoke(
						final Object obj, 
						final Method method, 
						final OptionOccurrence optionOccurrence) {
					try {
						method.invoke(obj, Boolean.TRUE.booleanValue());
					} catch (IllegalAccessException e) {
						throw new AssertionError(e);
					} catch (IllegalArgumentException e) {
						throw new AssertionError(e);
					} catch (InvocationTargetException e) {
						throw new AssertionError(e);
					}
				}

				@Override
				public boolean isValueForMethodParameterTypes(
						final Class<?>[] types) {
					if (types.length != 1) { return false; }
					Class<?> type = types[0];
					return type.equals(boolean.class);
				}
				
			},
			
			LIST {

				@Override
				public Class<?> getTargetClass(final Method method) {
					Type[] genericParameterTypes = 
							method.getGenericParameterTypes();
					Type genericParameterType = genericParameterTypes[0];
					if (genericParameterType instanceof ParameterizedType) {
						ParameterizedType parameterizedType = 
								(ParameterizedType) genericParameterType;
						Type[] typeArguments = 
								parameterizedType.getActualTypeArguments();
						Type typeArgument = typeArguments[0];
						if (typeArgument instanceof Class) {
							return (Class<?>) typeArgument;
						}
					}
					return Object.class;
				}

				@Override
				public String getTargetMethodParameterTypesString() {
					return String.format("(%s)", List.class.getName());
				}

				@Override
				public void invoke(
						final Object obj, 
						final Method method, 
						final OptionOccurrence optionOccurrence) {
					Option option = optionOccurrence.getOption();
					OptionArg optionArg = optionOccurrence.getOptionArg();
					List<Object> objectValues = Collections.emptyList();
					String optArg = null;
					if (optionArg != null) {
						objectValues = optionArg.getObjectValues();
						optArg = optionArg.toString();
					}
					try {
						method.invoke(obj, objectValues);
					} catch (IllegalAccessException e) {
						throw new AssertionError(e);
					} catch (IllegalArgumentException e) {
						throw new AssertionError(e);
					} catch (InvocationTargetException e) {
						Throwable cause = e.getCause();
						if (cause instanceof IllegalArgumentException) {
							throw new IllegalOptionArgException(
									option, optArg, cause);
						}
						throw new AssertionError(e);
					}
				}

				@Override
				public boolean isValueForMethodParameterTypes(
						final Class<?>[] types) {
					if (types.length != 1) { return false; }
					Class<?> type = types[0];
					return type.equals(List.class);
				}
				
			},
			
			NONE {

				@Override
				public Class<?> getTargetClass(final Method method) {
					return null;
				}

				@Override
				public String getTargetMethodParameterTypesString() {
					return "()";
				}

				@Override
				public void invoke(
						final Object obj, 
						final Method method, 
						final OptionOccurrence optionOccurrence) {
					try {
						method.invoke(obj);
					} catch (IllegalAccessException e) {
						throw new AssertionError(e);
					} catch (IllegalArgumentException e) {
						throw new AssertionError(e);
					} catch (InvocationTargetException e) {
						throw new AssertionError(e);
					}
				}

				@Override
				public boolean isValueForMethodParameterTypes(
						final Class<?>[] types) {
					return types.length == 0;
				}
				
			},
			
			OBJECT {

				@Override
				public Class<?> getTargetClass(final Method method) {
					return method.getParameterTypes()[0];
				}

				@Override
				public String getTargetMethodParameterTypesString() {
					return String.format("(%s)", Object.class.getName());
				}

				@Override
				public void invoke(
						final Object obj, 
						final Method method, 
						final OptionOccurrence optionOccurrence) {
					Option option = optionOccurrence.getOption();
					OptionArg optionArg = optionOccurrence.getOptionArg();
					Object objectValue = null;
					String optArg = null;
					if (optionArg != null) {
						objectValue = optionArg.getObjectValue();
						optArg = optionArg.toString();
					}
					try {
						method.invoke(obj, objectValue);
					} catch (IllegalAccessException e) {
						throw new AssertionError(e);
					} catch (IllegalArgumentException e) {
						throw new AssertionError(e);
					} catch (InvocationTargetException e) {
						Throwable cause = e.getCause();
						if (cause instanceof IllegalArgumentException) {
							throw new IllegalOptionArgException(
									option, optArg, cause);
						}
						throw new AssertionError(e);
					}
				}

				@Override
				public boolean isValueForMethodParameterTypes(
						final Class<?>[] types) {
					return types.length == 1;
				}
				
			};
			
			public static TargetMethodParameterTypesType valueForMethodParameterTypes(
					final Class<?>[] types) {
				for (TargetMethodParameterTypesType targetMethodParameterTypesType 
						: TargetMethodParameterTypesType.values()) {
					if (targetMethodParameterTypesType.isValueForMethodParameterTypes(
							types)) {
						return targetMethodParameterTypesType;
					}
				}
				return null;
			}
			
			public abstract Class<?> getTargetClass(final Method method);
			
			public abstract String getTargetMethodParameterTypesString();
			
			public abstract void invoke(
					final Object obj, 
					final Method method, 
					final OptionOccurrence optionOccurrence);
			
			public abstract boolean isValueForMethodParameterTypes(
					final Class<?>[] types);
			
		}
		
		public static OptionGroupMethod newInstance(final Method method) {
			return new OptionGroupMethod(method);
		}
		
		private static OptionArgSpec newOptionArgSpec(
				final Annotations.OptionArgSpec optionArgSpec,
				final Method method,
				final TargetMethodParameterTypesType t) {
			OptionArgSpecBuilder optionArgSpecBuilder = 
					new OptionArgSpecBuilder();
			String name = optionArgSpec.name();
			if (!name.isEmpty()) {
				optionArgSpecBuilder.setName(optionArgSpec.name());
			}
			OptionalBoolean required = optionArgSpec.required();
			if (required.optionalBooleanValue().isPresent()) {
				optionArgSpecBuilder.setRequired(
						required.optionalBooleanValue().get().booleanValue());
			}
			String separator = optionArgSpec.separator();
			if (!separator.equals(OptionArgSpec.DEFAULT_SEPARATOR)) {
				optionArgSpecBuilder.setSeparator(optionArgSpec.separator());
			}
			Class<?> stringConverterClass =	optionArgSpec.stringConverter();
			if (!stringConverterClass.equals(StringConverter.class)) {
				StringConverter stringConverter = newStringConverter(
						stringConverterClass);
				optionArgSpecBuilder.setStringConverter(stringConverter);
			}
			Class<?> targetClass = t.getTargetClass(method);
			if (targetClass != null) {
				optionArgSpecBuilder.setType(targetClass);
			}
			return optionArgSpecBuilder.build();
		}
		
		private static OptionBuilder newOptionBuilder(
				final Annotations.Option option, 
				final boolean firstInGroup,
				final Method method,
				final TargetMethodParameterTypesType t) {
			OptionType type = option.type();
			OptionTypeProfile typeProfile = OptionTypeProfile.valueOfOptionType(
					type);
			String name = option.name();
			OptionBuilder optionBuilder = typeProfile.newOptionBuilder(name);
			OptionalBoolean displayable = option.displayable();
			if (displayable.optionalBooleanValue().isPresent()) {
				optionBuilder.setDisplayable(
						displayable.optionalBooleanValue().get().booleanValue());
			}
			String doc = option.doc();
			if (!doc.isEmpty()) {
				optionBuilder.setDoc(option.doc());
			}
			Annotations.OptionArgSpec optionArgSpec = option.optionArgSpec();
			OptionalBoolean optionArgAllowed = optionArgSpec.allowed();
			if (!optionArgAllowed.optionalBooleanValue().isPresent()) {
				if (firstInGroup) {
					Class<?> targetClass = t.getTargetClass(method);
					if (targetClass != null) {
						optionBuilder.getOptionArgSpec(newOptionArgSpec(
								optionArgSpec, method, t));
					}
				}
			} else if (optionArgAllowed.optionalBooleanValue().get().booleanValue()) {
				optionBuilder.getOptionArgSpec(newOptionArgSpec(
						optionArgSpec, method, t));
			} else {
				optionBuilder.getOptionArgSpec(null);
			}
			Class<?> optionUsageProviderClass = option.optionUsageProvider();
			if (!optionUsageProviderClass.equals(OptionUsageProvider.class)) {
				OptionUsageProvider optionUsageProvider = 
						newOptionUsageProvider(optionUsageProviderClass);
				optionBuilder.setOptionUsageProvider(optionUsageProvider);
			}
			String usage = option.usage();
			if (!usage.isEmpty()) {
				optionBuilder.setUsage(usage);
			}
			return optionBuilder;
		}
		
		private static OptionGroup newOptionGroup(
				final Method method, 
				final TargetMethodParameterTypesType t) {
			OptionGroupBuilder optionGroupBuilder = new OptionGroupBuilder();
			if (method.isAnnotationPresent(Annotations.HelpText.class)) {
				Annotations.HelpText helpTextAnnotation = method.getAnnotation(
						Annotations.HelpText.class);
				optionGroupBuilder.setHelpText(helpTextAnnotation.value());
			}
			Annotations.Option[] optionAnnotations = method.getAnnotationsByType(
					Annotations.Option.class);
			if (optionAnnotations.length == 0 && method.isAnnotationPresent(
					Annotations.Options.class)) {
				Annotations.Options optsAnnotation = method.getAnnotation(
						Annotations.Options.class);
				optionAnnotations = optsAnnotation.value();
			}
			if (optionAnnotations.length > 0) {
				List<OptionBuilder> optionBuilders = new ArrayList<OptionBuilder>();
				for (Annotations.Option optionAnnotation : optionAnnotations) {
					boolean firstInGroup = optionBuilders.size() == 0;
					OptionBuilder optionBuilder = newOptionBuilder(
							optionAnnotation, 
							firstInGroup, 
							method, 
							t);
					optionBuilders.add(optionBuilder);
				}
				optionGroupBuilder.setOptionBuilders(optionBuilders);
			}
			if (method.isAnnotationPresent(
					Annotations.OptionGroupHelpTextProvider.class)) {
				Annotations.OptionGroupHelpTextProvider optionGroupHelpTextProviderAnnotation =
						method.getAnnotation(Annotations.OptionGroupHelpTextProvider.class);
				OptionGroupHelpTextProvider optionGroupHelpTextProvider = 
						newOptionGroupHelpTextProvider(
								optionGroupHelpTextProviderAnnotation.value());
				optionGroupBuilder.setOptionGroupHelpTextProvider(
						optionGroupHelpTextProvider);
			}
			return optionGroupBuilder.build();
		}
		
		private static OptionGroupHelpTextProvider newOptionGroupHelpTextProvider(
				final Class<?> optionGroupHelpTextProviderClass) {
			OptionGroupHelpTextProvider optionGroupHelpTextProvider = null;
			Constructor<?> ctor = null;
			try {
				ctor = optionGroupHelpTextProviderClass.getDeclaredConstructor();
			} catch (NoSuchMethodException e) {
				throw new AssertionError(e);
			} catch (SecurityException e) {
				throw new AssertionError(e);
			}
			ctor.setAccessible(true);
			try {
				optionGroupHelpTextProvider = 
						(OptionGroupHelpTextProvider) ctor.newInstance();
			} catch (InstantiationException e) {
				throw new AssertionError(e);
			} catch (IllegalAccessException e) {
				throw new AssertionError(e);
			} catch (IllegalArgumentException e) {
				throw new AssertionError(e);
			} catch (InvocationTargetException e) {
				throw new AssertionError(e);
			}
			return optionGroupHelpTextProvider;
		}
		
		private static OptionUsageProvider newOptionUsageProvider(
				final Class<?> optionUsageProviderClass) {
			OptionUsageProvider optionUsageProvider = null;
			Constructor<?> ctor = null;
			try {
				ctor = optionUsageProviderClass.getDeclaredConstructor();
			} catch (NoSuchMethodException e) {
				throw new AssertionError(e);
			} catch (SecurityException e) {
				throw new AssertionError(e);
			}
			ctor.setAccessible(true);
			try {
				optionUsageProvider = (OptionUsageProvider) ctor.newInstance();
			} catch (InstantiationException e) {
				throw new AssertionError(e);
			} catch (IllegalAccessException e) {
				throw new AssertionError(e);
			} catch (IllegalArgumentException e) {
				throw new AssertionError(e);
			} catch (InvocationTargetException e) {
				throw new AssertionError(e);
			}
			return optionUsageProvider;
		}
		
		private static StringConverter newStringConverter(
				final Class<?> stringConverterClass) {
			StringConverter stringConverter = null;
			Constructor<?> ctor = null;
			try {
				ctor = stringConverterClass.getDeclaredConstructor();
			} catch (NoSuchMethodException e) {
				throw new AssertionError(e);
			} catch (SecurityException e) {
				throw new AssertionError(e);
			}
			ctor.setAccessible(true);
			try {
				stringConverter = (StringConverter) ctor.newInstance();
			} catch (InstantiationException e) {
				throw new AssertionError(e);
			} catch (IllegalAccessException e) {
				throw new AssertionError(e);
			} catch (IllegalArgumentException e) {
				throw new AssertionError(e);
			} catch (InvocationTargetException e) {
				throw new AssertionError(e);
			}
			return stringConverter;
		}
		
		private final Method method;
		
		private final OptionGroup optionGroup;
		
		private final int ordinal;
		
		private final TargetMethodParameterTypesType targetMethodParameterTypesType;

		private OptionGroupMethod(final Method mthd) {
			int modifiers = mthd.getModifiers();
			if (Modifier.isStatic(modifiers)) {
				throw new IllegalArgumentException(String.format(
						"method '%s' cannot be static", 
						mthd));
			}
			TargetMethodParameterTypesType t = 
					TargetMethodParameterTypesType.valueForMethodParameterTypes(
							mthd.getParameterTypes());
			if (t == null) {
				StringBuilder sb = new StringBuilder();
				List<TargetMethodParameterTypesType> list = Arrays.asList(
						TargetMethodParameterTypesType.values());
				for (Iterator<TargetMethodParameterTypesType> iterator = list.iterator();
						iterator.hasNext();) {
					sb.append(iterator.next().getTargetMethodParameterTypesString());
					if (iterator.hasNext()) {
						sb.append(", ");
					}
				}
				throw new IllegalArgumentException(String.format(
						"method '%s' must have one the following parameter types: %s", 
						mthd,
						sb.toString()));
			}
			OptionGroup optGroup = newOptionGroup(mthd, t);
			int ord = 0;
			if (mthd.isAnnotationPresent(Annotations.Ordinal.class)) {
				Annotations.Ordinal ordAnnotation = mthd.getAnnotation(
						Annotations.Ordinal.class);
				ord = ordAnnotation.value();
			}
			this.method = mthd;
			this.optionGroup = optGroup;
			this.ordinal = ord;
			this.targetMethodParameterTypesType = t; 
		}
		
		public OptionGroup getOptionGroup() {
			return this.optionGroup;
		}
		
		public int getOrdinal() {
			return this.ordinal;
		}
		
		public void invoke(
				final Object obj, 
				final OptionOccurrence optionOccurrence) {
			this.targetMethodParameterTypesType.invoke(
					obj, this.method, optionOccurrence);
		}
		
		public Method toMethod() {
			return this.method;
		}
		
	}
	
	public static final class OptionGroups {
		
		public static OptionGroups newInstance(
				final List<OptionGroup> optGroups) {
			return new OptionGroups(optGroups);
		}
		
		public static OptionGroups newInstance(final OptionGroup... optGroups) {
			return newInstance(Arrays.asList(optGroups));
		}
		
		private final List<OptionGroup> optionGroups;
		
		private OptionGroups(final List<OptionGroup> optGroups) {
			for (OptionGroup optGroup : optGroups) {
				Objects.requireNonNull(
						optGroup, "OptionGroup(s) must not be null");
			}
			this.optionGroups = new ArrayList<OptionGroup>(optGroups);
		}
		
		public OptionGroup get(final int index) {
			return this.optionGroups.get(index);
		}
		
		public void printHelpText() {
			this.printHelpText(System.out);
		}
		
		public void printHelpText(final PrintStream s) {
			this.printHelpText(new PrintWriter(s));
		}
		
		public final void printHelpText(final PrintWriter w) {
			for (OptionGroup optionGroup : this.optionGroups) {
				String helpText = optionGroup.getHelpText();
				if (helpText != null) {
					w.println(helpText);
					w.flush();
				}
			}
		}
		
		public List<OptionGroup> toList() {
			return Collections.unmodifiableList(this.optionGroups);
		}

		@Override
		public final String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(this.getClass().getSimpleName())
				.append(" [optionGroups=")
				.append(this.optionGroups)
				.append("]");
			return sb.toString();
		}
		
	}
	
	static abstract class OptionHandler extends AbstractArgHandler {
		
		private final Class<?> optionClass;
		
		protected OptionHandler(
				final ArgHandler handler, 
				final Class<? extends Option> optClass) {
			super(handler);
			this.optionClass = Objects.requireNonNull(
					optClass, "Option class must not be null");
		}
		
		public final Class<?> getOptionClass() {
			return this.optionClass;
		}
		
		@Override
		public final void handle(
				final String arg, final ArgHandlerContext context) {
			ArgHandlerContextProperties properties = 
					new ArgHandlerContextProperties(context);
			if (!properties.isOptionHandlingEnabled()) {
				this.getArgHandler().handle(arg, context);
				return;
			}
			boolean hasOption = false;
			Map<String, Option> optionMap = properties.getOptionMap();
			for (Option option : optionMap.values()) {
				if (this.optionClass.isInstance(option)) {
					hasOption = true;
					break;
				}
			}
			if (!hasOption) {
				this.getArgHandler().handle(arg, context);
				return;
			}
			if (!this.isOption(arg, context)) {
				this.getArgHandler().handle(arg, context);
				return;
			}
			this.handleOption(arg, context);
		}
		
		protected abstract void handleOption(
				final String arg, final ArgHandlerContext context);

		protected abstract boolean isOption(
				final String arg, final ArgHandlerContext context);
		
	}

	public static final class OptionOccurrence {

		private final Option option;
		private final OptionArg optionArg;
		
		OptionOccurrence(final Option opt, final OptionArg optArg) {
			this.option = opt;
			this.optionArg = optArg;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof OptionOccurrence)) {
				return false;
			}
			OptionOccurrence other = (OptionOccurrence) obj;
			if (this.option == null) {
				if (other.option != null) {
					return false;
				}
			} else if (!this.option.equals(other.option)) {
				return false;
			}
			if (this.optionArg == null) {
				if (other.optionArg != null) {
					return false;
				}
			} else if (!this.optionArg.equals(other.optionArg)) {
				return false;
			}
			return true;
		}

		public Option getOption() {
			return this.option;
		}

		public OptionArg getOptionArg() {
			return this.optionArg;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((this.option == null) ? 
					0 : this.option.hashCode());
			result = prime * result + ((this.optionArg == null) ? 
					0 : this.optionArg.hashCode());
			return result;
		}
		
		public boolean hasOptionArg() {
			return this.optionArg != null;
		}
		
		public boolean hasOptionOf(final String opt) {
			return this.option.isOf(opt);
		}
		
		public boolean hasOptionOfAnyOf(final List<String> opts) {
			return this.option.isOfAnyOf(opts);
		}
		
		public boolean hasOptionOfAnyOf(final String opt1, final String opt2) {
			return this.option.isOfAnyOf(opt1, opt2);
		}
		
		public boolean hasOptionOfAnyOf(final String opt1, final String opt2,
				final String opt3) {
			return this.option.isOfAnyOf(opt1, opt2, opt3);
		}
		
		public boolean hasOptionOfAnyOf(final String opt1, final String opt2,
				final String opt3, final String... additionalOpts) {
			return this.option.isOfAnyOf(opt1, opt2, opt3, additionalOpts);
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(this.getClass().getSimpleName())
				.append(" [option=")
				.append(this.option)
				.append(", optionArg=")
				.append(this.optionArg)
				.append("]");
			return sb.toString();
		}
		
	}
	
	public static enum OptionType {
		
		GNU_LONG,
		
		LONG,
		
		POSIX;
		
	}
	
	static enum OptionTypeProfile {
		
		GNU_LONG(OptionType.GNU_LONG) {
			
			@Override
			public Class<? extends Option> getOptionClass() {
				return GnuLongOption.class;
			}

			@Override
			public OptionUsageProvider newDefaultOptionUsageProvider() {
				return new DefaultGnuLongOptionUsageProvider();
			}

			@Override
			public OptionBuilder newOptionBuilder(final String name) {
				return new GnuLongOptionBuilder(name);
			}

			@Override
			public OptionHandler newOptionHandler(final ArgHandler argHandler) {
				return new GnuLongOptionHandler(argHandler);
			}
			
		},
		
		LONG(OptionType.LONG) {
			
			@Override
			public Class<? extends Option> getOptionClass() {
				return LongOption.class;
			}

			@Override
			public OptionUsageProvider newDefaultOptionUsageProvider() {
				return new DefaultLongOptionUsageProvider();
			}

			@Override
			public OptionBuilder newOptionBuilder(final String name) {
				return new LongOptionBuilder(name);
			}

			@Override
			public OptionHandler newOptionHandler(final ArgHandler argHandler) {
				return new LongOptionHandler(argHandler);
			}
			
		},
		
		POSIX(OptionType.POSIX) {
			
			@Override
			public Class<? extends Option> getOptionClass() {
				return PosixOption.class;
			}

			@Override
			public OptionUsageProvider newDefaultOptionUsageProvider() {
				return new DefaultPosixOptionUsageProvider();
			}

			@Override
			public OptionBuilder newOptionBuilder(final String name) {
				if (name.length() != 1) {
					throw new IllegalArgumentException(String.format(
							"expected option name to be only one alphanumeric "
							+ "character. actual option name is '%s'", 
							name));
				}
				return new PosixOptionBuilder(name.charAt(0));
			}

			@Override
			public OptionHandler newOptionHandler(final ArgHandler argHandler) {
				return new PosixOptionHandler(argHandler);
			}
			
		};
		
		public static OptionTypeProfile valueOfOptionType(final OptionType optType) {
			for (OptionTypeProfile optionTypeProfile : OptionTypeProfile.values()) {
				if (optionTypeProfile.optionTypeValue().equals(optType)) {
					return optionTypeProfile;
				}
			}
			throw new IllegalArgumentException(String.format(
					"unknown %s: %s", 
					OptionType.class.getSimpleName(),
					optType));
		}
		
		private final OptionType optionType;
		
		private OptionTypeProfile(final OptionType optType) {
			this.optionType = optType;
		}
		
		public abstract Class<? extends Option> getOptionClass();
		
		public abstract OptionUsageProvider newDefaultOptionUsageProvider();
		
		public abstract OptionBuilder newOptionBuilder(final String name);
		
		public abstract OptionHandler newOptionHandler(
				final ArgHandler argHandler);
		
		public OptionType optionTypeValue() {
			return this.optionType;
		}
		
	}
	
	public static final class OptionUsageParams {

		private final Option option;
		
		OptionUsageParams(final Option opt) {
			this.option = opt;
		}
		
		public String getOption() {
			return this.option.toString();
		}

		public OptionArgSpec getOptionArgSpec() {
			return this.option.getOptionArgSpec();
		}
				
	}
	
	public static abstract class OptionUsageProvider {

		private static final Map<Class<? extends Option>, OptionUsageProvider> DEFAULT_INSTANCES = 
				new HashMap<Class<? extends Option>, OptionUsageProvider>();

		static {
			for (OptionTypeProfile optionTypeProfile : OptionTypeProfile.values()) {
				DEFAULT_INSTANCES.put(
						optionTypeProfile.getOptionClass(), 
						optionTypeProfile.newDefaultOptionUsageProvider());
			}
		}

		public static OptionUsageProvider getDefault(
				final Class<? extends Option> optClass) {
			return DEFAULT_INSTANCES.get(optClass);
		}

		public static Map<Class<? extends Option>, OptionUsageProvider> getDefaults() {
			return Collections.unmodifiableMap(DEFAULT_INSTANCES);
		}

		public static OptionUsageProvider putDefault(
				final Class<? extends Option> optClass,
				final OptionUsageProvider optUsageProvider) {
			return DEFAULT_INSTANCES.put(optClass, optUsageProvider);
		}

		public static OptionUsageProvider removeDefault(
				final Class<? extends Option> optClass) {
			return DEFAULT_INSTANCES.remove(optClass);
		}

		public OptionUsageProvider() { }
		
		public abstract String getOptionUsage(OptionUsageParams params);
		
	}

	public static final class ParseResultHolder {
		
		private final Object parseResult;
		
		ParseResultHolder(final Object result) {
			this.parseResult = result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof ParseResultHolder)) {
				return false;
			}
			ParseResultHolder other = (ParseResultHolder) obj;
			if (this.parseResult == null) {
				if (other.parseResult != null) {
					return false;
				}
			} else if (!this.parseResult.equals(other.parseResult)) {
				return false;
			}
			return true;
		}

		public EndOfOptionsDelimiter getEndOfOptionsDelimiter() {
			if (this.parseResult instanceof EndOfOptionsDelimiter) {
				return (EndOfOptionsDelimiter) this.parseResult;
			}
			return null;
		}
		
		public String getNonparsedArg() {
			if (this.parseResult instanceof String) {
				return (String) this.parseResult;
			}
			return null;
		}
		
		public Option getOption() {
			if (this.hasOptionOccurrence()) {
				return this.getOptionOccurrence().getOption();
			}
			return null;
		}
		
		public OptionArg getOptionArg() {
			if (this.hasOptionOccurrence()) {
				return this.getOptionOccurrence().getOptionArg();
			}
			return null;
		}

		public OptionOccurrence getOptionOccurrence() {
			if (this.parseResult instanceof OptionOccurrence) {
				return (OptionOccurrence) this.parseResult;
			}
			return null;
		}

		public Object getParseResult() {
			return this.parseResult;
		}
		
		public boolean hasEndOfOptionsDelimiter() {
			return this.getEndOfOptionsDelimiter() != null;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((this.parseResult == null) ? 
					0 : this.parseResult.hashCode());
			return result;
		}
		
		public boolean hasNonparsedArg() {
			return this.getNonparsedArg() != null;
		}
		
		public boolean hasOption() {
			return this.getOption() != null;
		}
		
		public boolean hasOptionArg() {
			return this.getOptionArg() != null;
		}
		
		public boolean hasOptionOccurrence() {
			return this.getOptionOccurrence() != null;
		}
		
		public boolean hasOptionOf(final String opt) {
			if (this.hasOptionOccurrence()) {
				return this.getOptionOccurrence().hasOptionOf(opt);
			}
			return false;
		}
		
		public boolean hasOptionOfAnyOf(final List<String> opts) {
			if (this.hasOptionOccurrence()) {
				return this.getOptionOccurrence().hasOptionOfAnyOf(opts);
			}
			return false;
		}

		public boolean hasOptionOfAnyOf(final String opt1, final String opt2) {
			if (this.hasOptionOccurrence()) {
				return this.getOptionOccurrence().hasOptionOfAnyOf(opt1, opt2);
			}
			return false;
		}
		
		public boolean hasOptionOfAnyOf(final String opt1, final String opt2,
				final String opt3) {
			if (this.hasOptionOccurrence()) {
				return this.getOptionOccurrence().hasOptionOfAnyOf(
						opt1, opt2, opt3);
			}
			return false;
		}
		
		public boolean hasOptionOfAnyOf(final String opt1, final String opt2,
				final String opt3, final String... additionalOpts) {
			if (this.hasOptionOccurrence()) {
				return this.getOptionOccurrence().hasOptionOfAnyOf(
						opt1, opt2, opt3, additionalOpts);
			}
			return false;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(this.getClass().getSimpleName())
				.append(" [parseResult=")
				.append(this.parseResult)
				.append("]");
			return sb.toString();
		}
		
	}
	
	public static final class PosixOption extends Option {
		
		PosixOption(final PosixOptionBuilder posixOptionBuilder) {
			super(posixOptionBuilder);
		}
		
	}

	static final class PosixOptionBuilder extends OptionBuilder {

		private static String toPosixOptionName(final char ch) {
			if (!Character.isLetterOrDigit(ch)) {
				throw new IllegalArgumentException(
						"option name must be a letter or a digit");
			}
			return Character.toString(ch);
		}
		
		public PosixOptionBuilder(final char optName) {
			super(toPosixOptionName(optName),
					"-".concat(toPosixOptionName(optName)));
		}

		@Override
		public PosixOption build() {
			return new PosixOption(this);
		}
		
		@Override
		public PosixOptionBuilder getOptionArgSpec(
				final OptionArgSpec optArgSpec) {
			super.getOptionArgSpec(optArgSpec);
			return this;
		}
		
		@Override
		public PosixOptionBuilder setDisplayable(final boolean b) {
			super.setDisplayable(b);
			return this;
		}
		
		@Override
		public PosixOptionBuilder setDoc(final String d) {
			super.setDoc(d);
			return this;
		}
		
		@Override
		public PosixOptionBuilder setOptionUsageProvider(
				final OptionUsageProvider optUsageProvider) {
			super.setOptionUsageProvider(optUsageProvider);
			return this;
		}
		
		@Override
		public PosixOptionBuilder setUsage(final String u) {
			super.setUsage(u);
			return this;
		}

	}

	static final class PosixOptionHandler extends OptionHandler {

		public PosixOptionHandler(final ArgHandler handler) {
			super(handler, PosixOption.class);
		}

		@Override
		protected void handleOption(
				final String arg, final ArgHandlerContext context) {
			int argCharIndex = context.getArgCharIndex();
			if (argCharIndex == -1) { /* not incremented yet */
				/* 
				 * initiate incrementing. ArgsParser will do the 
				 * incrementing 
				 */
				/* index 0 - '-' */
				argCharIndex = context.incrementArgCharIndex();
				/* index 1 - first alphanumeric character */
				argCharIndex = context.incrementArgCharIndex();
			}
			char ch = arg.charAt(argCharIndex);
			String name = Character.toString(ch);
			String option = "-".concat(name);
			ArgHandlerContextProperties properties = 
					new ArgHandlerContextProperties(context);
			Map<String, Option> optionMap = properties.getOptionMap();
			Option opt = optionMap.get(option);
			if (opt == null) {
				throw new UnknownOptionException(option);
			}
			String optionArg = null;
			OptionArgSpec optionArgSpec = opt.getOptionArgSpec();
			if (optionArgSpec != null) {
				if (argCharIndex < arg.length() - 1) {
					argCharIndex = context.incrementArgCharIndex();
					optionArg = arg.substring(argCharIndex);
					while (argCharIndex < arg.length() - 1) {
						argCharIndex = context.incrementArgCharIndex();
					}
				} else {
					if (optionArgSpec.isRequired()) {
						String[] args = context.getArgs();
						int argIndex = context.getArgIndex();
						if (argIndex < args.length - 1) {
							argCharIndex = context.resetArgCharIndex();
							argIndex = context.incrementArgIndex();
							optionArg = args[argIndex];
						}
					}
				}
			}
			properties.setParseResultHolder(new ParseResultHolder(
					new OptionOccurrence(opt, opt.newOptionArg(optionArg))));
		}

		@Override
		protected boolean isOption(
				final String arg, final ArgHandlerContext context) {
			return arg.length() > 1 
					&& arg.startsWith("-") 
					&& !arg.startsWith("--");
		}		
	}
	
	static final class PropertiesHelper {

		public static void copy(
				final Properties source, final Properties destination) {
			Set<String> sourceStringPropertyNames = 
					source.stringPropertyNames();
			for (String sourceStringPropertyName : sourceStringPropertyNames) {
				String property = source.getProperty(sourceStringPropertyName);
				destination.setProperty(sourceStringPropertyName, property);
			}
		}

		private PropertiesHelper() { }

	}
	
	/**
	 * Converts the provided {@code String} to an {@code Object}.
	 */
	public static abstract class StringConverter {
	
		/**
		 * Constructs a {@code StringConverter}.
		 */
		public StringConverter() { }
		
		/**
		 * Converts the provided {@code String} to an {@code Object}.
		 * 
		 * @param string the provided {@code String}
		 * 
		 * @return the converted {@code Object}
		 * 
		 * @throws IllegalArgumentException if the provided {@code String} is 
		 * illegal or inappropriate
		 */
		public abstract Object convert(String string);
		
	}
	
	static final class StringInterpolator {
		
		public static String interpolate(
				final String string, final Properties properties) {
			StringBuilder sb = new StringBuilder(string);
			List<Integer> propertyVariableStartIndices = 
					new ArrayList<Integer>();
			for (int i = 0; i < sb.length(); i++) {
				char ch = sb.charAt(i);
				if (ch == '$' && i + 1 < sb.length()) {
					char nextCh = sb.charAt(i + 1);
					switch (nextCh) {
					case '$':
						sb.replace(i, i + 2, "$");
						break;
					case '{':
						propertyVariableStartIndices.add(Integer.valueOf(i));
						i++;
						continue;
					case '}':
						sb.replace(i, i + 2, "}");
						break;
					default:
						break;
					}
				} else if (ch == '}' && propertyVariableStartIndices.size() > 0) {
					int lastIndex = propertyVariableStartIndices.size() - 1;
					int propertyVariableStartIndex = 
							propertyVariableStartIndices.get(lastIndex).intValue();
					String propertyName = sb.substring(
							propertyVariableStartIndex + 2, i);
					String property = properties.getProperty(propertyName);
					if (property != null) {
						sb.replace(propertyVariableStartIndex, i + 1, property);
						i = propertyVariableStartIndex + property.length() - 1;
					}
					propertyVariableStartIndices.remove(lastIndex);
				}
			}
			return sb.toString();
		}
		
		private StringInterpolator() { }
		
	}
	
	/**
	 * Thrown when an unknown command line option is encountered.
	 */
	public static final class UnknownOptionException extends RuntimeException {

		/** The default serial version UID. */
		private static final long serialVersionUID = 1L;
		
		/** The unknown command line option. */
		private final String option;
		
		/**
		 * Constructs an {@code UnknownOptionException} with the provided 
		 * unknown command line option.
		 * 
		 * @param opt the provided unknown command line option
		 */
		UnknownOptionException(final String opt) {
			super(String.format("unknown option `%s'", opt));
			this.option = opt;
		}
		
		/**
		 * Returns the unknown command line option.
		 * 
		 * @return the unknown command line option
		 */
		public String getOption() {
			return this.option;
		}

	}
	
	private ArgMatey() { }
	
}
