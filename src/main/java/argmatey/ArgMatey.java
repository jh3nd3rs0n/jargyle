/*
Copyright © 2016-2020 Jonathan K. Henderson

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

/**
 * Provides interfaces and classes for parsing command line arguments and 
 * for displaying the usage and help text of the command line options.
 */
public final class ArgMatey {

	public static final class Annotations {
		
		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.METHOD})
		public static @interface NonparsedArg { }

		@Retention(RetentionPolicy.RUNTIME)
		public static @interface Option {
			
			String doc() default "";
			
			boolean hidden() default false;
			
			String name();
			
			Annotations.OptionArgSpec optionArgSpec() 
				default @OptionArgSpec(allowed = false);
			
			Class<? extends ArgMatey.OptionUsageProvider> optionUsageProvider()
				default ArgMatey.DefaultOptionUsageProvider.class;
					
			boolean special() default false;
			
			Class<? extends ArgMatey.Option> type();
			
		}

		@Retention(RetentionPolicy.RUNTIME)
		public static @interface OptionArgSpec {
			
			boolean allowed() default true;
			
			String name() default ArgMatey.OptionArgSpec.DEFAULT_NAME;
			
			boolean required() default true;
			
			String separator() default ArgMatey.OptionArgSpec.DEFAULT_SEPARATOR;
			
			Class<? extends ArgMatey.StringConverter> stringConverter() 
				default ArgMatey.DefaultStringConverter.class;
			
		}

		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.METHOD})
		public static @interface OptionGroup {
			
			Annotations.Option option();
			
			Class<? extends ArgMatey.OptionGroupHelpTextProvider> optionGroupHelpTextProvider()
				default ArgMatey.DefaultOptionGroupHelpTextProvider.class;
			
			int ordinal() default 0;
			
			Annotations.Option[] otherOptions() default { };
			
		}

		private Annotations() { }
		
	}
	
	public static final class ArgsHandler {
		
		public static ArgsHandler newInstance(
				final String[] args, 
				final Object parseResultHandler, 
				final boolean posixlyCorrect) {
			return new ArgsHandler(args, parseResultHandler, posixlyCorrect);
		}
		
		private final ArgsParser argsParser;
		private final Object parseResultHandler;
		private final ParseResultHandlerClass parseResultHandlerClass;
		
		private ArgsHandler(
				final String[] args, 
				final Object handler, 
				final boolean posixlyCorrect) {
			ParseResultHandlerClass handlerClass = 
					ParseResultHandlerClass.newInstance(handler.getClass());
			ArgsParser parser = ArgsParser.newInstance(
					args, handlerClass.getOptionGroups(), posixlyCorrect);
			this.argsParser = parser;
			this.parseResultHandler = handler;
			this.parseResultHandlerClass = handlerClass;
		}
		
		public int getArgCharIndex() {
			return this.argsParser.getArgCharIndex();
		}
		
		public int getArgIndex() {
			return this.argsParser.getArgIndex();
		}
		
		public String[] getArgs() {
			return this.argsParser.getArgs();
		}
		
		public ArgsParser getArgsParser() {
			return this.argsParser;
		}
		
		public OptionGroups getOptionGroups() {
			return this.argsParser.getOptionGroups();
		}
		
		public Object getParseResultHandler() {
			return this.parseResultHandler;
		}
		
		public ParseResultHolder getParseResultHolder() {
			return this.argsParser.getParseResultHolder();
		}
		
		public void handleNext() {
			ParseResultHolder parseResultHolder = this.argsParser.parseNext();
			if (parseResultHolder.hasNonparsedArg()) {
				String nonparsedArg = parseResultHolder.getNonparsedArg();
				NonparsedArgMethod nonparsedArgMethod =
						this.parseResultHandlerClass.getNonparsedArgMethod();
				if (nonparsedArgMethod != null) {
					nonparsedArgMethod.invoke(
							this.parseResultHandler, nonparsedArg);
				}
			}
			if (parseResultHolder.hasOptionOccurrence()) {
				OptionOccurrence optionOccurrence = 
						parseResultHolder.getOptionOccurrence();
				Option option = optionOccurrence.getOption();
				Map<String, OptionGroupMethod> optionMethodMap = 
						this.parseResultHandlerClass.getOptionGroupMethodMap();
				OptionGroupMethod optionGroupMethod = optionMethodMap.get(
						option.toString());
				if (optionGroupMethod != null) {
					optionGroupMethod.invoke(
							this.parseResultHandler, optionOccurrence);
				}
			}
		}
		
		public boolean hasNext() {
			return this.argsParser.hasNext();
		}
		
		public String next() {
			return this.argsParser.next();
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(this.getClass().getSimpleName())
				.append(" [getArgCharIndex()=")
				.append(this.getArgCharIndex())
				.append(", getArgIndex()=")
				.append(this.getArgIndex())
				.append(", getArgs()=")
				.append(Arrays.toString(this.getArgs()))
				.append(", getOptionGroups()=")
				.append(this.getOptionGroups())
				.append("]");
			return sb.toString();
		}
		
	}
	
	public static final class ArgsParser {

		private static abstract class AbstractArgHandler implements ArgHandler {

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
			
		private static interface ArgHandler {
			
			void handle(String arg, ArgHandlerContext context);
			
		}
		
		private static final class ArgHandlerContext {
			
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
			
			@SuppressWarnings("unused")
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
			
			@SuppressWarnings("unused")
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
		
		private static final class ArgHandlerContextProperties {
			
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
		
		private static enum DefaultArgHandler implements ArgHandler {

			INSTANCE;
			
			@Override
			public void handle(
					final String arg, final ArgHandlerContext context) {
				ArgHandlerContextProperties properties =
						new ArgHandlerContextProperties(context);
				properties.setParseResultHolder(new ParseResultHolder(arg));
			}
			
			@Override
			public String toString() {
				return DefaultArgHandler.class.getSimpleName();
			}
			
		}

		private static final class EndOfOptionsArgHandler 
			extends AbstractArgHandler {
			
			public EndOfOptionsArgHandler(final ArgHandler handler) {
				super(handler);
			}
			
			@Override
			public void handle(
					final String arg, final ArgHandlerContext context) {
				ArgHandlerContextProperties properties = 
						new ArgHandlerContextProperties(context); 
				if (properties.isOptionHandlingEnabled()) {
					properties.setOptionHandlingEnabled(false);
				}
				this.getArgHandler().handle(arg, context);
			}
			
		}
		
		private static final class EndOfOptionsDelimiterHandler 
			extends AbstractArgHandler {
			
			public EndOfOptionsDelimiterHandler(final ArgHandler argHandler) {
				super(argHandler);
			}
			
			@Override
			public void handle(
					final String arg, final ArgHandlerContext context) {
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
		
		private static final class GnuLongOptionHandler extends OptionHandler {
			
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
		
		private static final class LongOptionHandler extends OptionHandler {
			
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
		
		private static abstract class OptionHandler extends AbstractArgHandler {
			
			private final Class<?> optionClass;
			
			protected OptionHandler(final ArgHandler handler, 
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
		
		private static final class PosixOptionHandler extends OptionHandler {

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
		
		public static ArgsParser newInstance(
				final String[] args, 
				final OptionGroups optionGroups, 
				final boolean posixlyCorrect) {
			ArgHandler endOfOptionsArgHandler = null;
			if (posixlyCorrect) {
				endOfOptionsArgHandler = new EndOfOptionsArgHandler(null);
			}
			ArgHandler argHandler = new GnuLongOptionHandler(
					new LongOptionHandler(new EndOfOptionsDelimiterHandler(
							endOfOptionsArgHandler)));
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
			boolean earlierUsageNotNullOrEmpty = false;
			String doc = null;
			for (Option option : params.getOptions()) {
				if (!option.isHidden()) {
					String usage = option.getUsage();
					if (usage != null && !usage.isEmpty()) {
						if (sb == null) {
							sb = new StringBuilder();
							sb.append("  ");
						}
						if (earlierUsageNotNullOrEmpty) {
							sb.append(", ");
						}
						sb.append(usage);
						if (!earlierUsageNotNullOrEmpty) {
							earlierUsageNotNullOrEmpty = true;
						}
						if (doc == null || doc.isEmpty()) {
							doc = option.getDoc();
						}
					}
				}
			}
			if (sb != null) {
				if (doc != null && !doc.isEmpty()) {
					sb.append(System.getProperty("line.separator"));
					sb.append("      ");
					sb.append(doc);
				}
				optionGroupHelpText = sb.toString();
			}
			return optionGroupHelpText;
		}
		
	}
	
	public static final class DefaultOptionUsageProvider 
		extends OptionUsageProvider {

		private DefaultOptionUsageProvider() { 
			throw new AssertionError(String.format(
					"%s is not to be constructed", 
					this.getClass().getName()));
		}
		
		@Override
		public String getOptionUsage(final OptionUsageParams params) {
			throw new AssertionError(String.format(
					"method '%s' is not to be invoked", 
					this.getClass().getEnclosingMethod()));
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
	 * that has one method parameter of type {@code String} and a method 
	 * return type of the provided type. If the provided type does not have 
	 * that type of method, this {@code StringConverter} uses the provided 
	 * type's public instantiatable constructor that has one constructor 
	 * parameter of type {@code String}. If the provided type has neither, a 
	 * {@code IllegalArgumentException} is thrown.
	 */
	public static final class DefaultStringConverter extends StringConverter {
		
		/**
		 * Returns the provided type's {@code Method} that is public and static 
		 * and has one method parameter of type {@code String} and a method 
		 * return type of the provided type. If the provided type has no such 
		 * method, {@code null} is returned.
		 *  
		 * @param type the provided type
		 * 
		 * @return the provided type's {@code Method} that is public and static 
		 * and has one method parameter of type {@code String} and a method 
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
					return method;
				}
			}
			return null;
		}
		
		/**
		 * Returns the provided type's {@code Constructor} that is public and 
		 * instantiatable and has a constructor parameter of type 
		 * {@code String}. If the provided type has no such constructor, 
		 * {@code null} is returned.
		 * 
		 * @param type the provided type
		 * 
		 * @return the provided type's {@code Constructor} that is public and 
		 * instantiatable and has a constructor parameter of type 
		 * {@code String} or {@code null} if no such constructor is found
		 */
		private static <T> Constructor<T> getStringParameterConstructor(
				final Class<T> type) {
			for (Constructor<?> constructor : type.getConstructors()) {
				int modifiers = constructor.getModifiers();
				Class<?>[] parameterTypes = constructor.getParameterTypes();
				boolean isInstantiatable = !Modifier.isAbstract(modifiers)
						&& !Modifier.isInterface(modifiers);
				boolean isParameterTypeString = parameterTypes.length == 1 
						&& parameterTypes[0].equals(String.class);
				if (isInstantiatable && isParameterTypeString) {
					@SuppressWarnings("unchecked")
					Constructor<T> ctor = (Constructor<T>) constructor;
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
		 * The {@code Method} that is public and static and has one method 
		 * parameter of type {@code String} and a method return type of the 
		 * converted type. 
		 */
		private final Method staticStringConversionMethod;
		
		/**
		 * The {@code Constructor} that is public and instantiatable and has a 
		 * constructor parameter of type {@code String}.
		 */
		private final Constructor<?> stringParameterConstructor;
		
		/**
		 * Constructs a {@code DefaultStringConverter} with the provided type.
		 * 
		 * @param type the provided type
		 * 
		 * @throws IllegalArgumentException if the provided type does not have 
		 * either a public static method that has one method parameter of type 
		 * {@code String} and a method return type of the provided type nor a 
		 * public instantiatable constructor that has one constructor parameter 
		 * of type {@code String}
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
						throw new AssertionError(
								InvocationTargetExceptionHelper.toString(e), 
								e);
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
						throw new AssertionError(
								InvocationTargetExceptionHelper.toString(e), 
								e);
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

	/**
	 * Enum singleton that represents the delimiter signaling the end of the 
	 * command line options.
	 */
	public static enum EndOfOptionsDelimiter {

		/** The single instance. */
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

	public static final class GnuLongOption extends Option {

		public static final class Builder extends Option.Builder {

			public Builder(final String optName) {
				super(optName, "--".concat(optName));
			}

			@Override
			public GnuLongOption build() {
				return new GnuLongOption(this);
			}
			
			@Override
			public Builder doc(final String d) {
				super.doc(d);
				return this;
			}
			
			@Override
			public Builder hidden(final boolean b) {
				super.hidden(b);
				return this;
			}
			
			@Override
			public Builder optionArgSpec(final OptionArgSpec optArgSpec) {
				super.optionArgSpec(optArgSpec);
				return this;
			}
			
			@Override
			public Builder optionUsageProvider(
					final OptionUsageProvider optUsageProvider) {
				super.optionUsageProvider(optUsageProvider);
				return this;
			}
			
			@Override
			public Builder special(final boolean b) {
				super.special(b);
				return this;
			}
			
		}
		
		private GnuLongOption(final Builder builder) {
			super(builder);
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
	
	static final class InvocationTargetExceptionHelper {
		
		public static String toString(final InvocationTargetException e) {
			StringBuilder sb = new StringBuilder();
			sb.append(e.getClass().getName());
			String message = e.getMessage();
			if (message != null) {
				sb.append(": ");
				sb.append(message);
				return sb.toString();
			}
			Throwable cause = e.getCause();
			if (cause != null) {
				sb.append(": ");
				sb.append(cause);
				return sb.toString();
			}
			return sb.toString();
		}
		
		private InvocationTargetExceptionHelper() { }
		
	}
	
	public static final class LongOption extends Option {

		public static final class Builder extends Option.Builder {

			public Builder(final String optName) {
				super(optName, "-".concat(optName));
			}

			@Override
			public LongOption build() {
				return new LongOption(this);
			}
			
			@Override
			public Builder doc(final String d) {
				super.doc(d);
				return this;
			}
			
			@Override
			public Builder hidden(final boolean b) {
				super.hidden(b);
				return this;
			}

			@Override
			public Builder optionArgSpec(final OptionArgSpec optArgSpec) {
				super.optionArgSpec(optArgSpec);
				return this;
			}
			
			@Override
			public Builder optionUsageProvider(
					final OptionUsageProvider optUsageProvider) {
				super.optionUsageProvider(optUsageProvider);
				return this;
			}
			
			@Override
			public Builder special(final boolean b) {
				super.special(b);
				return this;
			}
			
		}
		
		private LongOption(final Builder builder) {
			super(builder);
		}
		
	}

	static final class NonparsedArgMethod {
		
		public static NonparsedArgMethod newInstance(final Method method) {
			validateMethod(method);
			return new NonparsedArgMethod(method);
		}
		
		private static void validateMethod(final Method method) {
			Annotations.NonparsedArg nonparsedArg = method.getAnnotation(
					Annotations.NonparsedArg.class);
			if (nonparsedArg == null) {
				throw new IllegalArgumentException(String.format(
						"method '%s' must have the annotation %s", 
						method,
						Annotations.NonparsedArg.class.getName()));
			}
			int modifiers = method.getModifiers();
			if (Modifier.isStatic(modifiers)) {
				throw new IllegalArgumentException(String.format(
						"method '%s' cannot be static", 
						method));
			}
			Class<?>[] parameterTypes = method.getParameterTypes();
			if (parameterTypes.length != 1 
					|| !parameterTypes[0].equals(String.class)) {
				throw new IllegalArgumentException(String.format(
						"method '%s' must have only one paramter of type %s", 
						method,
						String.class.getName()));
			}
		}
		
		private final Method method;
		
		private NonparsedArgMethod(final Method mthd) {
			this.method = mthd;
		}
		
		public void invoke(final Object obj, final String nonparsedArg) {
			try {
				this.method.invoke(obj, nonparsedArg);
			} catch (IllegalAccessException e) {
				throw new AssertionError(e);
			} catch (IllegalArgumentException e) {
				throw new AssertionError(e);
			} catch (InvocationTargetException e) {
				Throwable cause = e.getCause();
				if (cause instanceof IllegalArgumentException) {
					throw new IllegalArgException(nonparsedArg, cause);
				}
				throw new AssertionError(
						InvocationTargetExceptionHelper.toString(e), 
						e);
			}
		}
		
		public Method toMethod() {
			return this.method;
		}
	}
	
	public static abstract class Option {

		public static abstract class Builder {
			
			private String doc;
			private boolean hidden;
			private boolean hiddenSet;
			private final String name;
			private OptionArgSpec optionArgSpec;
			private boolean optionArgSpecSet;
			private OptionUsageProvider optionUsageProvider;
			private boolean optionUsageProviderSet;
			private boolean special;
			private boolean specialSet;
			private String string;
			
			Builder(final String optName, final String opt) {
				Objects.requireNonNull(optName, "option name must not be null");
				if (optName.isEmpty()) {
					throw new IllegalArgumentException(
							"option name must not be empty");
				}
				Objects.requireNonNull(opt, "option must not be null");
				if (opt.isEmpty()) {
					throw new IllegalArgumentException(
							"option must not be empty");
				}
				this.hidden = false;
				this.hiddenSet = false;
				this.name = optName;
				this.optionArgSpecSet = false;
				this.optionUsageProviderSet = false;
				this.special = false;
				this.specialSet = false;
				this.string = opt;
			}
			
			public abstract Option build();
			
			public Builder doc(final String d) {
				this.doc = d;
				return this;
			}
			
			public final boolean hidden() {
				return this.hidden;
			}
			
			public Builder hidden(final boolean b) {
				this.hidden = b;
				this.hiddenSet = true;
				return this;
			}
			
			public final boolean hiddenSet() {
				return this.hiddenSet;
			}
			
			public final OptionArgSpec optionArgSpec() {
				return this.optionArgSpec;
			}
			
			public Builder optionArgSpec(final OptionArgSpec optArgSpec) {
				this.optionArgSpec = optArgSpec;
				this.optionArgSpecSet = true;
				return this;
			}
			
			public final boolean optionArgSpecSet() {
				return this.optionArgSpecSet;
			}
			
			public Builder optionUsageProvider(
					final OptionUsageProvider optUsageProvider) {
				this.optionUsageProvider = optUsageProvider;
				this.optionUsageProviderSet = true;
				return this;
			}
			
			public final boolean special() {
				return this.special;
			}
			
			public Builder special(final boolean b) {
				this.special = b;
				this.specialSet = true;
				return this;
			}
			
			public final boolean specialSet() {
				return this.specialSet;
			}
			
		}
		
		private final String doc;
		private final boolean hidden;
		private final String name;
		private final OptionArgSpec optionArgSpec;
		private final OptionUsageProvider optionUsageProvider;
		private final boolean special;
		private final String string;

		Option(final Builder builder) {
			String d = builder.doc;
			boolean hide = builder.hidden;
			String n = builder.name;
			OptionArgSpec optArgSpec = builder.optionArgSpec;
			OptionUsageProvider optUsageProvider = builder.optionUsageProvider;
			boolean spcl = builder.special;
			String str = builder.string;
			if (!builder.optionUsageProviderSet) {
				optUsageProvider = OptionUsageProvider.getDefault(
						this.getClass());
			}
			this.doc = d;
			this.hidden = hide;
			this.name = n;
			this.optionArgSpec = optArgSpec;
			this.optionUsageProvider = optUsageProvider;
			this.special = spcl;
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
		
		public final boolean isHidden() {
			return this.hidden;
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
		
		public final boolean isSpecial() {
			return this.special;
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
	 * An {@code Object} that represents a command line option argument.
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
			if (obj == null) {
				return false;
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
			if (this.objectValueSet != other.objectValueSet) {
				return false;
			}
			if (this.optionArgs == null) {
				if (other.optionArgs != null) {
					return false;
				}
			} else if (!this.optionArgs.equals(other.optionArgs)) {
				return false;
			}
			if (this.string == null) {
				if (other.string != null) {
					return false;
				}
			} else if (!this.string.equals(other.string)) {
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
					objectValues.add(optionArg.getObjectValue());
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
					0 : this.objectValue.hashCode());
			result = prime * result + (this.objectValueSet ? 1231 : 1237);
			result = prime * result + ((this.optionArgs == null) ? 
					0 : this.optionArgs.hashCode());
			result = prime * result + ((this.string == null) ? 
					0 : this.string.hashCode());
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

		public static final class Builder {
					
			private String name;
			private boolean required;
			private String separator;
			private StringConverter stringConverter;
			private Class<?> type;
			
			public Builder() {
				this.name = null;
				this.required = true;
				this.separator = null;
				this.stringConverter = null;
				this.type = null;
			}
			
			public OptionArgSpec build() {
				return new OptionArgSpec(this); 
			}
			
			public Builder name(final String n) {
				this.name = n;
				return this;
			}
			
			public Builder required(final boolean b) {
				this.required = b;
				return this;
			}
			
			public Builder separator(final String s) {
				this.separator = s;
				return this;
			}
			
			public Builder stringConverter(final StringConverter sc) {
				this.stringConverter = sc;
				return this;
			}
			
			public Builder type(final Class<?> t) {
				this.type = t;
				return this;
			}
			
		}
		
		public static final String DEFAULT_NAME = "option_argument";
		public static final String DEFAULT_SEPARATOR = "[^\\w\\W]";
		public static final Class<?> DEFAULT_TYPE = String.class;
			
		private final String name;
		private final boolean required;
		private final String separator;
		private final StringConverter stringConverter;
		private final Class<?> type;
		
		private OptionArgSpec(final Builder builder) {
			String n = builder.name;
			boolean r = builder.required;
			String s = builder.separator;
			StringConverter sc = builder.stringConverter;
			Class<?> t = builder.type;
			if (n == null) { n = DEFAULT_NAME; }
			if (s == null) { s = DEFAULT_SEPARATOR;	}
			if (t == null) { t = DEFAULT_TYPE; }
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
	
	public static final class OptionGroup {
		
		public static final class Builder {
			
			private final Option.Builder optionBuilder;
			private OptionGroupHelpTextProvider optionGroupHelpTextProvider;
			private boolean optionGroupHelpTextProviderSet;
			private final List<Option.Builder> otherOptionBuilders;
			
			public Builder(final Option.Builder optBuilder,
					final List<Option.Builder> otherOptBuilders) {
				Objects.requireNonNull(optBuilder);
				Objects.requireNonNull(otherOptBuilders);
				this.optionBuilder = optBuilder;
				this.otherOptionBuilders = new ArrayList<Option.Builder>(
						otherOptBuilders);
				this.optionGroupHelpTextProvider = null;
				this.optionGroupHelpTextProviderSet = false;
			}
			
			public Builder(final Option.Builder optBuilder,
					final Option.Builder... otherOptBuilders) {
				this(optBuilder, Arrays.asList(otherOptBuilders));
			}
			
			public OptionGroup build() {
				return new OptionGroup(this); 
			}
			
			public Builder optionGroupHelpTextProvider(
					final OptionGroupHelpTextProvider optGroupHelpTextProvider) {
				this.optionGroupHelpTextProvider = optGroupHelpTextProvider;
				this.optionGroupHelpTextProviderSet = true;
				return this;
			}
			
		}
		
		private final List<Option> options;
		private final OptionGroupHelpTextProvider optionGroupHelpTextProvider;
		
		private OptionGroup(final Builder builder) {
			Option.Builder optBuilder = builder.optionBuilder;
			OptionGroupHelpTextProvider optGroupHelpTextProvider =
					builder.optionGroupHelpTextProvider;
			List<Option.Builder> otherOptBuilders = 
					new ArrayList<Option.Builder>(builder.otherOptionBuilders);
			List<Option> opts = new ArrayList<Option>();
			Option opt = optBuilder.build();
			opts.add(opt);
			for (Option.Builder otherOptBuilder : otherOptBuilders) {
				if (optBuilder.hiddenSet() && !otherOptBuilder.hiddenSet()) {
					otherOptBuilder.hidden(optBuilder.hidden());
				}
				if (optBuilder.optionArgSpecSet() 
						&& !otherOptBuilder.optionArgSpecSet()) {
					otherOptBuilder.optionArgSpec(optBuilder.optionArgSpec());
				}
				if (optBuilder.specialSet() && !otherOptBuilder.specialSet()) {
					otherOptBuilder.special(optBuilder.special());
				}
				Option otherOpt = otherOptBuilder.build();
				opts.add(otherOpt);
			}
			if (!builder.optionGroupHelpTextProviderSet) {
				optGroupHelpTextProvider = 
						OptionGroupHelpTextProvider.getDefault();
			}
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
	
	public static final class OptionGroupHelpTextParams {
		
		private final OptionGroup optionGroup;
		
		OptionGroupHelpTextParams(final OptionGroup optGroup) {
			this.optionGroup = optGroup;
		}
		
		public List<Option> getOptions() {
			return this.optionGroup.toList();
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
						throw new AssertionError(
								InvocationTargetExceptionHelper.toString(e), 
								e);
					}
				}

				@Override
				public boolean isTargetMethodParameterTypes(
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
						throw new AssertionError(
								InvocationTargetExceptionHelper.toString(e), 
								e);
					}
				}

				@Override
				public boolean isTargetMethodParameterTypes(
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
						throw new AssertionError(
								InvocationTargetExceptionHelper.toString(e), 
								e);
					}
				}

				@Override
				public boolean isTargetMethodParameterTypes(
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
						throw new AssertionError(
								InvocationTargetExceptionHelper.toString(e), 
								e);
					}
				}

				@Override
				public boolean isTargetMethodParameterTypes(
						final Class<?>[] types) {
					return types.length == 1;
				}
				
			};
			
			public static TargetMethodParameterTypesType get(
					final Class<?>[] types) {
				for (TargetMethodParameterTypesType targetMethodParameterTypesType 
						: TargetMethodParameterTypesType.values()) {
					if (targetMethodParameterTypesType.isTargetMethodParameterTypes(types)) {
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
					OptionOccurrence optionOccurrence);
			
			public abstract boolean isTargetMethodParameterTypes(
					final Class<?>[] types);
			
		}
		
		public static OptionGroupMethod newInstance(final Method method) {
			validateMethod(method);
			return new OptionGroupMethod(method);
		}
		
		private static void validateMethod(final Method method) {
			Annotations.OptionGroup optionGroup = method.getAnnotation(
					Annotations.OptionGroup.class);
			if (optionGroup == null) {
				throw new IllegalArgumentException(String.format(
						"method '%s' must have the annotation %s", 
						method,
						Annotations.OptionGroup.class.getName()));
			}
			int modifiers = method.getModifiers();
			if (Modifier.isStatic(modifiers)) {
				throw new IllegalArgumentException(String.format(
						"method '%s' cannot be static", 
						method));
			}
			TargetMethodParameterTypesType targetMethodParameterTypesType =
					TargetMethodParameterTypesType.get(
							method.getParameterTypes());
			if (targetMethodParameterTypesType == null) {
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
						method,
						sb.toString()));
			}
		}
		
		private final Method method;
		private OptionGroup optionGroup;
		private final Annotations.OptionGroup optionGroupAnnotation;
		private final TargetMethodParameterTypesType targetMethodParameterTypesType;
		
		private OptionGroupMethod(final Method mthd) {
			Annotations.OptionGroup optGroupAnnotation = mthd.getAnnotation(
					Annotations.OptionGroup.class);
			TargetMethodParameterTypesType t = TargetMethodParameterTypesType.get(
					mthd.getParameterTypes());
			this.method = mthd;
			this.optionGroup = null;
			this.optionGroupAnnotation = optGroupAnnotation;
			this.targetMethodParameterTypesType = t;
		}
		
		public OptionGroup getOptionGroup() {
			if (this.optionGroup == null) {
				this.optionGroup = this.newOptionGroup();
			}
			return this.optionGroup;
		}
		
		public Annotations.OptionGroup getOptionGroupAnnotation() {
			return this.optionGroupAnnotation;
		}

		public void invoke(
				final Object obj, 
				final OptionOccurrence optionOccurrence) {
			this.targetMethodParameterTypesType.invoke(
					obj, this.method, optionOccurrence);
		}
		
		private OptionArgSpec.Builder newOptionArgSpecBuilder(
				final Annotations.OptionArgSpec optionArgSpec) {
			OptionArgSpec.Builder builder = new OptionArgSpec.Builder();
			builder.name(optionArgSpec.name());
			builder.required(optionArgSpec.required());
			builder.separator(optionArgSpec.separator());
			Class<?> stringConverterClass =	optionArgSpec.stringConverter();
			if (!stringConverterClass.equals(DefaultStringConverter.class)) {
				StringConverter stringConverter = this.newStringConverter(
						stringConverterClass);
				builder.stringConverter(stringConverter);
			}
			builder.type(this.targetMethodParameterTypesType.getTargetClass(
					this.method));
			return builder;
		}
		
		private Option.Builder newOptionBuilder(
				final Annotations.Option option) {
			Option.Builder builder = null;
			Class<?> type = option.type();
			String name = option.name();
			if (type.equals(GnuLongOption.class)) {
				builder = new GnuLongOption.Builder(name);
			} else if (type.equals(LongOption.class)) {
				builder = new LongOption.Builder(name);
			} else if (type.equals(PosixOption.class)) {
				if (name.length() != 1) {
					throw new AssertionError(String.format(
							"expected name for %s to be only one character. "
							+ "actual name is '%s'", 
							Annotations.Option.class.getName(),
							name));
				}
				builder = new PosixOption.Builder(name.charAt(0));
			} else {
				throw new AssertionError(String.format(
						"unhandled %s: %s", 
						Option.class.getName(), 
						type.getName()));
			}
			builder.doc(option.doc());
			builder.hidden(option.hidden());
			Annotations.OptionArgSpec optionArgSpec = option.optionArgSpec();
			if (optionArgSpec.allowed()) {
				builder.optionArgSpec(this.newOptionArgSpecBuilder(
						optionArgSpec).build());
			}
			Class<?> optionUsageProviderClass = option.optionUsageProvider();
			if (!optionUsageProviderClass.equals(
					DefaultOptionUsageProvider.class)) {
				OptionUsageProvider optionUsageProvider = 
						this.newOptionUsageProvider(optionUsageProviderClass);
				builder.optionUsageProvider(optionUsageProvider);
			}
			builder.special(option.special());
			return builder;
		}
		
		private OptionGroup newOptionGroup() {
			Annotations.Option option = this.optionGroupAnnotation.option();
			Annotations.Option[] otherOptions = 
					this.optionGroupAnnotation.otherOptions();
			Option.Builder optionBuilder = this.newOptionBuilder(option);
			List<Option.Builder> otherOptionBuilders = 
					new ArrayList<Option.Builder>();
			for (Annotations.Option otherOption : otherOptions) {
				otherOptionBuilders.add(this.newOptionBuilder(otherOption));
			}
			OptionGroup.Builder builder = new OptionGroup.Builder(
					optionBuilder, otherOptionBuilders);
			Class<?> optionGroupHelpTextProviderClass = 
					this.optionGroupAnnotation.optionGroupHelpTextProvider();
			OptionGroupHelpTextProvider optionGroupHelpTextProvider = 
					this.newOptionGroupHelpTextProvider(
							optionGroupHelpTextProviderClass);
			builder.optionGroupHelpTextProvider(optionGroupHelpTextProvider);
			return builder.build();
		}
		
		private OptionGroupHelpTextProvider newOptionGroupHelpTextProvider(
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
				throw new AssertionError(
						InvocationTargetExceptionHelper.toString(e), 
						e);
			}
			return optionGroupHelpTextProvider;
		}
		
		private OptionUsageProvider newOptionUsageProvider(
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
			try {
				optionUsageProvider = (OptionUsageProvider) ctor.newInstance();
			} catch (InstantiationException e) {
				throw new AssertionError(e);
			} catch (IllegalAccessException e) {
				throw new AssertionError(e);
			} catch (IllegalArgumentException e) {
				throw new AssertionError(e);
			} catch (InvocationTargetException e) {
				throw new AssertionError(
						InvocationTargetExceptionHelper.toString(e), 
						e);
			}
			return optionUsageProvider;
		}
		
		private StringConverter newStringConverter(
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
			try {
				stringConverter = (StringConverter) ctor.newInstance();
			} catch (InstantiationException e) {
				throw new AssertionError(e);
			} catch (IllegalAccessException e) {
				throw new AssertionError(e);
			} catch (IllegalArgumentException e) {
				throw new AssertionError(e);
			} catch (InvocationTargetException e) {
				throw new AssertionError(
						InvocationTargetExceptionHelper.toString(e), 
						e);
			}
			return stringConverter;
		}
		
		public Method toMethod() {
			return this.method;
		}
		
	}
	
	static final class OptionGroupMethodComparator 
		implements Comparator<OptionGroupMethod> {

		public OptionGroupMethodComparator() { }
		
		@Override
		public int compare(
				final OptionGroupMethod arg0, final OptionGroupMethod arg1) {
			Annotations.OptionGroup optionGroup0 = 
					arg0.getOptionGroupAnnotation();
			Annotations.OptionGroup optionGroup1 = 
					arg1.getOptionGroupAnnotation();
			int diff = optionGroup0.ordinal() - optionGroup1.ordinal();
			if (diff != 0) { return diff; }
			Annotations.Option option0 = optionGroup0.option();
			Annotations.Option option1 = optionGroup1.option();
			return option0.name().compareTo(option1.name());
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
			boolean earlierHelpTextNotNullOrEmpty = false;
			for (OptionGroup optionGroup : this.optionGroups) {
				String helpText = optionGroup.getHelpText();
				if (helpText != null && !helpText.isEmpty()) {
					if (earlierHelpTextNotNullOrEmpty) {
						w.println();
					}
					w.print(helpText);
					w.flush();
					if (!earlierHelpTextNotNullOrEmpty) {
						earlierHelpTextNotNullOrEmpty = true;
					}
				}
			}
		}

		public void printUsage() {
			this.printUsage(System.out);
		}
		
		public void printUsage(final PrintStream s) {
			this.printUsage(new PrintWriter(s));
		}
		
		public void printUsage(final PrintWriter w) {
			boolean earlierUsageNotNullOrEmpty = false;
			for (OptionGroup optionGroup : this.optionGroups) {
				String usage = null;
				for (Option option : optionGroup.toList()) {
					if (!option.isHidden() && !option.isSpecial()) {
						String use = option.getUsage();
						if (use != null && !use.isEmpty()) {
							usage = use;
							break;
						}
					}
				}
				if (usage != null && !usage.isEmpty()) {
					if (earlierUsageNotNullOrEmpty) {
						w.print(" ");
					}
					w.print(String.format("[%s]", usage));
					w.flush();
					if (!earlierUsageNotNullOrEmpty) {
						earlierUsageNotNullOrEmpty = true;
					}
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
			DEFAULT_INSTANCES.put(
					GnuLongOption.class, 
					new DefaultGnuLongOptionUsageProvider());
			DEFAULT_INSTANCES.put(
					LongOption.class, 
					new DefaultLongOptionUsageProvider());
			DEFAULT_INSTANCES.put(
					PosixOption.class, 
					new DefaultPosixOptionUsageProvider());
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

	static final class ParseResultHandlerClass {
		
		public static ParseResultHandlerClass newInstance(final Class<?> cls) {
			return new ParseResultHandlerClass(cls);
		}
		
		private final Class<?> cls;
		private final NonparsedArgMethod nonparsedArgMethod;
		private final Map<String, OptionGroupMethod> optionGroupMethodMap;
		private final List<OptionGroupMethod> optionGroupMethods;
		private final OptionGroups optionGroups;
		
		private ParseResultHandlerClass(final Class<?> c) {
			NonparsedArgMethod argSinkMethod = null;
			Map<String, OptionGroupMethod> optGroupMethodMap = 
					new HashMap<String, OptionGroupMethod>();
			List<OptionGroupMethod> optGroupMethods = 
					new ArrayList<OptionGroupMethod>();
			Method[] methods = c.getMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(Annotations.NonparsedArg.class)) {
					NonparsedArgMethod mthd =
							NonparsedArgMethod.newInstance(method);
					argSinkMethod = mthd;
				}
				if (method.isAnnotationPresent(Annotations.OptionGroup.class)) {
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
			this.nonparsedArgMethod = argSinkMethod;
			this.optionGroupMethodMap = new HashMap<String, OptionGroupMethod>(
					optGroupMethodMap);
			this.optionGroupMethods = new ArrayList<OptionGroupMethod>(optGroupMethods);
			this.optionGroups = OptionGroups.newInstance(optGroups);
		}
		
		public NonparsedArgMethod getNonparsedArgMethod() {
			return this.nonparsedArgMethod;
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

		public static final class Builder extends Option.Builder {

			private static String toPosixOptionName(final char ch) {
				if (!Character.isLetterOrDigit(ch)) {
					throw new IllegalArgumentException(
							"option name must be a letter or a digit");
				}
				return Character.toString(ch);
			}
			
			public Builder(final char optName) {
				super(toPosixOptionName(optName), 
						"-".concat(toPosixOptionName(optName)));
			}

			@Override
			public PosixOption build() {
				return new PosixOption(this);
			}
			
			@Override
			public Builder doc(final String d) {
				super.doc(d);
				return this;
			}
			
			@Override
			public Builder hidden(final boolean b) {
				super.hidden(b);
				return this;
			}
			
			@Override
			public Builder optionArgSpec(final OptionArgSpec optArgSpec) {
				super.optionArgSpec(optArgSpec);
				return this;
			}
			
			@Override
			public Builder optionUsageProvider(
					final OptionUsageProvider optUsageProvider) {
				super.optionUsageProvider(optUsageProvider);
				return this;
			}
			
			@Override
			public Builder special(final boolean b) {
				super.special(b);
				return this;
			}
			
		}
		
		private PosixOption(final Builder builder) {
			super(builder);
		}
		
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
