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
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Provides interfaces and classes for parsing command line arguments and 
 * for displaying the usage and help text of the command line options.
 */
public final class ArgMatey {

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
					if (argument == null) {
						throw new NullPointerException(
								"argument(s) must not be null");
					}
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
			
			private static final class PropertyNames {
				
				public static final String OPTION_HANDLING_ENABLED = 
						"OPTION_HANDLING_ENABLED";
				
				public static final String OPTIONS = "OPTIONS";
				
				public static final String PARSE_RESULT_HOLDER = 
						"PARSE_RESULT_HOLDER";
				
				private PropertyNames() { }

			}
			
			private final ArgHandlerContext argHandlerContext;
			
			public ArgHandlerContextProperties(
					final ArgHandlerContext handlerContext) {
				this.argHandlerContext = handlerContext;
			}
			
			public Map<String, Option> getOptions() {
				Map<String, Option> options = null;
				@SuppressWarnings("unchecked")
				Map<String, Option> value = 
						(Map<String, Option>) this.argHandlerContext.getProperty(
								PropertyNames.OPTIONS);
				if (value != null) {
					options = Collections.unmodifiableMap(
							new HashMap<String, Option>(value));
				}
				return options;
			}
			
			public ParseResultHolder getParseResultHolder() {
				ParseResultHolder parseResult = null;
				ParseResultHolder value = 
						(ParseResultHolder) this.argHandlerContext.getProperty(
								PropertyNames.PARSE_RESULT_HOLDER);
				if (value != null) {
					parseResult = value;
				}
				return parseResult;
			}
			
			public boolean isOptionHandlingEnabled() {
				boolean optionHandlingEnabled = false;
				Boolean value = (Boolean) this.argHandlerContext.getProperty(
						PropertyNames.OPTION_HANDLING_ENABLED);
				if (value != null) {
					optionHandlingEnabled = value.booleanValue();
				}
				return optionHandlingEnabled;
			}
			
			public void setOptionHandlingEnabled(
					final boolean optHandlingEnabled) {
				this.argHandlerContext.putProperty(
						PropertyNames.OPTION_HANDLING_ENABLED, 
						Boolean.valueOf(optHandlingEnabled));
			}
			
			public void setOptions(final Map<String, Option> opts) {
				this.argHandlerContext.putProperty(PropertyNames.OPTIONS, opts);
			}
			
			public void setParseResultHolder(
					final ParseResultHolder parseResultHolder) {
				this.argHandlerContext.putProperty(
						PropertyNames.PARSE_RESULT_HOLDER, parseResultHolder);
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
				Map<String, Option> options = properties.getOptions();
				Option opt = options.get(option);
				if (opt == null) {
					throw new UnknownOptionException(option);
				}
				OptionArgSpec optionArgSpec = opt.getOptionArgSpec();
				if (optionArg == null && optionArgSpec != null 
						&& !optionArgSpec.isOptional()) {
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
				Map<String, Option> options = properties.getOptions();
				Option opt = options.get(option);
				if (opt == null) {
					OptionHandler posixOptionHandler = 
							(PosixOptionHandler) this.getArgHandler();
					boolean hasPosixOption = false;
					for (Option o : options.values()) {
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
				if (optionArgSpec != null && !optionArgSpec.isOptional()) {
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
				if (optClass == null) {
					throw new NullPointerException(
							"Option class must not be null");
				}
				this.optionClass = optClass;
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
				Map<String, Option> options = properties.getOptions();
				for (Option option : options.values()) {
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
				Map<String, Option> options = properties.getOptions();
				Option opt = options.get(option);
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
						if (!optionArgSpec.isOptional()) {
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
				final Options options, 
				final boolean posixlyCorrect) {
			ArgHandler endOfOptionsArgHandler = null;
			if (posixlyCorrect) {
				endOfOptionsArgHandler = new EndOfOptionsArgHandler(null);
			}
			ArgHandler argHandler = new GnuLongOptionHandler(
					new LongOptionHandler(new EndOfOptionsDelimiterHandler(
							endOfOptionsArgHandler)));
			return new ArgsParser(args, options, argHandler);
		}
		
		private final ArgHandler argHandler;
		private ArgHandlerContext argHandlerContext;
		private final Options options;
			
		private ArgsParser(
				final String[] args, 
				final Options opts, 
				final ArgHandler handler) {
			for (String arg : args) {
				if (arg == null) {
					throw new NullPointerException(
							"argument(s) must not be null");
				}
			}
			if (opts == null) {
				throw new NullPointerException("Options must not be null");
			}
			ArgHandlerContext handlerContext = new ArgHandlerContext(args);
			List<Option> optsList = opts.toList();
			if (optsList.size() > 0) {
				Map<String, Option> optsMap = new HashMap<String, Option>();
				for (Option opt : optsList) {
					for (Option o : opt.getAllOptions()) {
						optsMap.put(o.toString(), o);
					}
				}
				ArgHandlerContextProperties properties = 
						new ArgHandlerContextProperties(handlerContext);
				properties.setOptionHandlingEnabled(true);
				properties.setOptions(optsMap);
			}
			this.argHandler = handler;
			this.argHandlerContext = handlerContext;
			this.options = opts;
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
		
		public Options getOptions() {
			return this.options;
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
			return next;
		}
		
		public ParseResultHolder parseNext() {
			ArgHandlerContext recentArgHandlerContext = 
					new ArgHandlerContext(this.argHandlerContext);
			this.next();
			try {
				this.argHandler.handle(this.getArgs()[this.getArgIndex()], 
						this.argHandlerContext);
			} catch (Throwable t) {
				/* 
				 * failure atomicity (return back to most recent working state) 
				 */
				this.argHandlerContext = recentArgHandlerContext;
				if (t instanceof Error) {
					Error e = (Error) t;
					throw e;
				}
				if (t instanceof RuntimeException) {
					RuntimeException rte = (RuntimeException) t;
					throw rte;
				}
			}
			ArgHandlerContextProperties properties = 
					new ArgHandlerContextProperties(this.argHandlerContext);
			ParseResultHolder parseResultHolder = 
					properties.getParseResultHolder();
			properties.setParseResultHolder(null);
			return parseResultHolder;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(this.getClass().getSimpleName())
				.append(" [options=")
				.append(this.options)
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
	
	public static enum DefaultGnuLongOptionUsageProvider 
		implements OptionUsageProvider {

		INSTANCE;
		
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
				if (optionArgSpec.isOptional()) {
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
				} else {
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
				}
			}
			return usage;
		}

		@Override
		public String toString() {
			return DefaultGnuLongOptionUsageProvider.class.getSimpleName();
		}
		
	}

	public static enum DefaultLongOptionUsageProvider 
		implements OptionUsageProvider {

		INSTANCE;
		
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
				if (optionArgSpec.isOptional()) {
					usage = option;
				} else {
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
				}
			}		
			return usage;
		}
		
		@Override
		public String toString() {
			return DefaultLongOptionUsageProvider.class.getSimpleName();
		}

	}

	public static enum DefaultOptionComparator implements Comparator<Option> {

		INSTANCE;

		@Override
		public int compare(final Option option1, final Option option2) {
			int diff = option1.getOrdinal() - option2.getOrdinal();
			if (diff == 0) {
				return option1.getName().compareTo(option2.getName());
			}
			return diff;
		}
		
		public String toString() {
			return DefaultOptionComparator.class.getSimpleName();
		}
		
	}

	public static enum DefaultOptionHelpTextProvider 
		implements OptionHelpTextProvider {

		INSTANCE; 
		
		@Override
		public String getOptionHelpText(final OptionHelpTextParams params) {
			String helpText = null;
			StringBuilder sb = null;
			boolean earlierUsageNotNull = false;
			String doc = null;
			for (OptionHelpTextParams p : params.getAllOptionHelpTextParams()) {
				if (!p.isHidden()) {
					String usage = p.getUsage();
					if (usage != null) {
						if (sb == null) {
							sb = new StringBuilder();
							sb.append("  ");
						}
						if (earlierUsageNotNull) {
							sb.append(", ");
						}
						sb.append(usage);
						if (!earlierUsageNotNull) {
							earlierUsageNotNull = true;
						}
						if (doc == null) {
							doc = p.getDoc();
						}
					}
				}
			}
			if (sb != null) {
				if (doc != null) {
					sb.append(System.getProperty("line.separator"));
					sb.append("      ");
					sb.append(doc);
				}
				helpText = sb.toString();
			}
			return helpText;
		}

		@Override
		public String toString() {
			return DefaultOptionHelpTextProvider.class.getSimpleName();
		}
	}

	public static enum DefaultPosixOptionUsageProvider 
		implements OptionUsageProvider {

		INSTANCE;
		
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
				if (optionArgSpec.isOptional()) {
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
				} else {
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
	public static final class DefaultStringConverter 
		implements StringConverter {
		
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
						if (cause instanceof Error) {
							Error err = (Error) cause;
							throw err;
						}
						if (cause instanceof RuntimeException) {
							RuntimeException rte = (RuntimeException) cause;
							throw rte;
						}
						throw new RuntimeException(cause);
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
						if (cause instanceof Error) {
							Error err = (Error) cause;
							throw err;
						}
						if (cause instanceof RuntimeException) {
							RuntimeException rte = (RuntimeException) cause;
							throw rte;
						}
						throw new RuntimeException(cause);
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
			public Builder optionHelpTextProvider(
					final OptionHelpTextProvider optHelpTextProvider) {
				super.optionHelpTextProvider(optHelpTextProvider);
				return this;
			}
			
			@Override
			public Builder optionUsageProvider(
					final OptionUsageProvider optUsageProvider) {
				super.optionUsageProvider(optUsageProvider);
				return this;
			}
			
			@Override
			public Builder ordinal(final int i) {
				super.ordinal(i);
				return this;
			}
			
			@Override
			public Builder otherBuilders(
					final ArgMatey.Option.Builder otherBldr) {
				super.otherBuilders(otherBldr);
				return this;
			}
			
			@Override
			public Builder otherBuilders(
					final ArgMatey.Option.Builder otherBldr1,
					final ArgMatey.Option.Builder otherBldr2) {
				super.otherBuilders(otherBldr1, otherBldr2);
				return this;
			}
			
			@Override
			public Builder otherBuilders(
					final ArgMatey.Option.Builder otherBldr1,
					final ArgMatey.Option.Builder otherBldr2,
					final ArgMatey.Option.Builder... additionalOtherBldrs) {
				super.otherBuilders(
						otherBldr1, otherBldr2, additionalOtherBldrs);
				return this;
			}
			
			@Override
			public Builder otherBuilders(
					final List<ArgMatey.Option.Builder> otherBldrs) {
				super.otherBuilders(otherBldrs);
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
			public Builder optionHelpTextProvider(
					final OptionHelpTextProvider optHelpTextProvider) {
				super.optionHelpTextProvider(optHelpTextProvider);
				return this;
			}
			
			@Override
			public Builder optionUsageProvider(
					final OptionUsageProvider optUsageProvider) {
				super.optionUsageProvider(optUsageProvider);
				return this;
			}
			
			@Override
			public Builder ordinal(final int i) {
				super.ordinal(i);
				return this;
			}
			
			@Override
			public Builder otherBuilders(
					final ArgMatey.Option.Builder otherBldr) {
				super.otherBuilders(otherBldr);
				return this;
			}
			
			@Override
			public Builder otherBuilders(
					final ArgMatey.Option.Builder otherBldr1,
					final ArgMatey.Option.Builder otherBldr2) {
				super.otherBuilders(otherBldr1, otherBldr2);
				return this;
			}
			
			@Override
			public Builder otherBuilders(
					final ArgMatey.Option.Builder otherBldr1,
					final ArgMatey.Option.Builder otherBldr2,
					final ArgMatey.Option.Builder... additionalOtherBldrs) {
				super.otherBuilders(
						otherBldr1, otherBldr2, additionalOtherBldrs);
				return this;
			}
			
			@Override
			public Builder otherBuilders(
					final List<ArgMatey.Option.Builder> otherBldrs) {
				super.otherBuilders(otherBldrs);
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

	public static abstract class Option {

		public static abstract class Builder {
					
			private String doc;
			private boolean hidden;
			private boolean hiddenSet;
			private final String name;
			private OptionArgSpec optionArgSpec;
			private boolean optionArgSpecSet;
			private OptionHelpTextProvider optionHelpTextProvider;
			private boolean optionHelpTextProviderSet;
			private OptionUsageProvider optionUsageProvider;
			private boolean optionUsageProviderSet;
			private int ordinal;
			private final List<Builder> otherBuilders;
			private boolean special;
			private boolean specialSet;
			private String string;
			
			Builder(final String optName, final String opt) {
				if (optName == null) {
					throw new NullPointerException(
							"option name must not be null");
				}
				if (optName.isEmpty()) {
					throw new IllegalArgumentException(
							"option name must not be empty");
				}
				if (opt == null) {
					throw new NullPointerException("option must not be null");
				}
				if (opt.isEmpty()) {
					throw new IllegalArgumentException(
							"option must not be empty");
				}
				this.hidden = false;
				this.hiddenSet = false;
				this.name = optName;
				this.optionArgSpecSet = false;
				this.optionHelpTextProviderSet = false;
				this.optionUsageProviderSet = false;
				this.ordinal = 0;
				this.otherBuilders = new ArrayList<Builder>();
				this.special = false;
				this.specialSet = false;
				this.string = opt;
			}
			
			public abstract Option build();
			
			public Builder doc(final String d) {
				this.doc = d;
				return this;
			}
			
			public Builder hidden(final boolean b) {
				this.hidden = b;
				this.hiddenSet = true;
				return this;
			}
			
			public Builder optionArgSpec(final OptionArgSpec optArgSpec) {
				this.optionArgSpec = optArgSpec;
				this.optionArgSpecSet = true;
				return this;
			}
			
			public Builder optionHelpTextProvider(
					final OptionHelpTextProvider optHelpTextProvider) {
				this.optionHelpTextProvider = optHelpTextProvider;
				this.optionHelpTextProviderSet = true;
				return this;
			}
			
			public Builder optionUsageProvider(
					final OptionUsageProvider optUsageProvider) {
				this.optionUsageProvider = optUsageProvider;
				this.optionUsageProviderSet = true;
				return this;
			}
			
			public Builder ordinal(final int i) {
				this.ordinal = i;
				return this;
			}
			
			public Builder otherBuilders(final Builder otherBldr) {
				List<Builder> otherBldrs = new ArrayList<Builder>();
				otherBldrs.add(otherBldr);
				return this.otherBuilders(otherBldrs);
			}
			
			public Builder otherBuilders(
					final Builder otherBldr1, final Builder otherBldr2) {
				List<Builder> otherBldrs = new ArrayList<Builder>();
				otherBldrs.add(otherBldr1);
				otherBldrs.add(otherBldr2);
				return this.otherBuilders(otherBldrs);
			}
			
			public Builder otherBuilders(
					final Builder otherBldr1, 
					final Builder otherBldr2,
					final Builder... additionalOtherBldrs) {
				List<Builder> otherBldrs = new ArrayList<Builder>();
				otherBldrs.add(otherBldr1);
				otherBldrs.add(otherBldr2);
				otherBldrs.addAll(Arrays.asList(additionalOtherBldrs));
				return this.otherBuilders(otherBldrs);
			}
			
			public Builder otherBuilders(final List<Builder> otherBldrs) {
				for (Builder otherBldr : otherBldrs) {
					if (otherBldr == null) {
						throw new NullPointerException(
								"Builder(s) must not be null");
					}
				}
				this.otherBuilders.clear();
				this.otherBuilders.addAll(otherBldrs);
				return this;
			}
			
			public Builder special(final boolean b) {
				this.special = b;
				this.specialSet = true;
				return this;
			}
			
		}
		
		private final String doc;
		private final boolean hidden;
		private final String name;
		private final OptionArgSpec optionArgSpec;
		private final OptionHelpTextProvider optionHelpTextProvider;
		private final OptionUsageProvider optionUsageProvider;
		private final int ordinal;
		private final List<Option> otherOptions;
		private final boolean special;
		private final String string;

		Option(final Builder builder) {
			String d = builder.doc;
			boolean hide = builder.hidden;
			String n = builder.name;
			OptionArgSpec optArgSpec = builder.optionArgSpec;
			OptionHelpTextProvider optHelpTextProvider = 
					builder.optionHelpTextProvider;
			OptionUsageProvider optUsageProvider = builder.optionUsageProvider;
			int ord = builder.ordinal;
			List<Builder> otherBldrs = new ArrayList<Builder>(
					builder.otherBuilders);
			boolean spcl = builder.special;
			String str = builder.string;
			if (!builder.optionHelpTextProviderSet) {
				optHelpTextProvider = OptionHelpTextProviders.getDefault();
			}
			if (!builder.optionUsageProviderSet) {
				optUsageProvider = OptionUsageProviders.getDefault(
						this.getClass());
			}
			List<Option> otherOpts = new ArrayList<Option>();
			for (Builder otherBldr : otherBldrs) {
				if (!otherBldr.hiddenSet) {
					otherBldr.hidden(hide);
				}
				if (!otherBldr.optionArgSpecSet) {
					otherBldr.optionArgSpec(optArgSpec);
				}
				if (!otherBldr.specialSet) {
					otherBldr.special(spcl);
				}
				Option otherOpt = otherBldr.build();
				otherOpts.add(otherOpt);
			}		
			this.doc = d;
			this.hidden = hide;
			this.name = n;
			this.optionArgSpec = optArgSpec;
			this.optionHelpTextProvider = optHelpTextProvider;
			this.optionUsageProvider = optUsageProvider;
			this.ordinal = ord;
			this.otherOptions = otherOpts;
			this.special = spcl;
			this.string = str;
		}
		
		public final List<Option> getAllOptions() {
			List<Option> allOptions = new ArrayList<Option>();
			allOptions.add(this);
			allOptions.addAll(this.getAllOtherOptions());
			return Collections.unmodifiableList(allOptions);
		}
		
		public final List<Option> getAllOtherOptions() {
			List<Option> allOtherOptions = new ArrayList<Option>();
			for (Option otherOption : this.otherOptions) {
				allOtherOptions.add(otherOption);
				allOtherOptions.addAll(otherOption.getAllOtherOptions());
			}
			return Collections.unmodifiableList(allOtherOptions);
		}
		
		public final String getDoc() {
			return this.doc;
		}
		
		public final String getHelpText() {
			String helpText = null;
			if (this.optionHelpTextProvider != null) {
				helpText = this.optionHelpTextProvider.getOptionHelpText(
						new OptionHelpTextParams(this));
			}
			return helpText;
		}

		public final String getName() {
			return this.name;
		}
		
		public final OptionArgSpec getOptionArgSpec() {
			return this.optionArgSpec;
		}

		public final OptionHelpTextProvider getOptionHelpTextProvider() {
			return this.optionHelpTextProvider;
		}
		
		public final OptionUsageProvider getOptionUsageProvider() {
			return this.optionUsageProvider;
		}

		public final int getOrdinal() {
			return this.ordinal;
		}

		public final List<Option> getOtherOptions() {
			return Collections.unmodifiableList(this.otherOptions);
		}
		
		public final String getUsage() {
			String usage = null;
			if (this.optionUsageProvider != null) {
				usage = this.optionUsageProvider.getOptionUsage(
						new OptionUsageParams(this));
			}
			return usage;
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
			if (optionArg != null && this.optionArgSpec == null) {
				throw new OptionArgNotAllowedException(this);
			}
			OptionArg optArg = null;
			if (optionArg != null && this.optionArgSpec != null) {
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
			private boolean optional;
			private String separator;
			private StringConverter stringConverter;
			private Class<?> type;
			
			public Builder() {
				this.name = null;
				this.optional = false;
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
			
			public Builder optional(final boolean b) {
				this.optional = b;
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
		private final boolean optional;
		private final String separator;
		private final StringConverter stringConverter;
		private final Class<?> type;
		
		private OptionArgSpec(final Builder builder) {
			String n = builder.name;
			boolean o = builder.optional;
			String s = builder.separator;
			StringConverter sc = builder.stringConverter;
			Class<?> t = builder.type;
			if (n == null) { n = DEFAULT_NAME; }
			if (s == null) { s = DEFAULT_SEPARATOR;	}
			if (t == null) { t = DEFAULT_TYPE; }
			if (sc == null) { sc = new DefaultStringConverter(t); }
			this.name = n;
			this.optional = o;
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

		public boolean isOptional() {
			return this.optional;
		}

		public OptionArg newOptionArg(final String optionArg) {
			if (!this.optional && optionArg == null) {
				throw new NullPointerException(
						"option argument must not be null");
			}
			if (this.optional && optionArg == null) {
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
				.append(", optional=")
				.append(this.optional)
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

	public static final class OptionHelpTextParams {

		private final Option option;
		private final List<OptionHelpTextParams> otherOptionHelpTextParams;
		
		OptionHelpTextParams(final Option opt) {
			List<OptionHelpTextParams> otherParams = 
					new ArrayList<OptionHelpTextParams>();
			for (Option o : opt.getOtherOptions()) {
				otherParams.add(new OptionHelpTextParams(o));
			}
			this.option = opt;
			this.otherOptionHelpTextParams = otherParams;
		}
		
		public List<OptionHelpTextParams> getAllOptionHelpTextParams() {
			List<OptionHelpTextParams> allOptionHelpTextParams = 
					new ArrayList<OptionHelpTextParams>();
			allOptionHelpTextParams.add(this);
			allOptionHelpTextParams.addAll(
					this.getAllOtherOptionHelpTextParams());
			return Collections.unmodifiableList(allOptionHelpTextParams);
		}
		
		public List<OptionHelpTextParams> getAllOtherOptionHelpTextParams() {
			List<OptionHelpTextParams> allOtherOptionHelpTextParams =
					new ArrayList<OptionHelpTextParams>();
			for (OptionHelpTextParams otherParams 
					: this.otherOptionHelpTextParams) {
				allOtherOptionHelpTextParams.add(otherParams);
				allOtherOptionHelpTextParams.addAll(
						otherParams.getAllOtherOptionHelpTextParams());
			}
			return Collections.unmodifiableList(allOtherOptionHelpTextParams);
		}

		public String getDoc() {
			return this.option.getDoc();
		}

		public String getOption() {
			return this.option.toString();
		}

		public OptionArgSpec getOptionArgSpec() {
			return this.option.getOptionArgSpec();
		}

		public List<OptionHelpTextParams> getOtherOptionHelpTextParams() {
			return Collections.unmodifiableList(
					this.otherOptionHelpTextParams);
		}

		public String getUsage() {
			return this.option.getUsage();
		}

		public boolean isHidden() {
			return this.option.isHidden();
		}
		
	}
	
	public static interface OptionHelpTextProvider {

		String getOptionHelpText(OptionHelpTextParams params);
		
	}
	
	public static final class OptionHelpTextProviders {
		
		private static OptionHelpTextProvider defaultInstance = 
				DefaultOptionHelpTextProvider.INSTANCE;
		
		public static OptionHelpTextProvider getDefault() {
			return defaultInstance;
		}

		public static void setDefault(
				final OptionHelpTextProvider optHelpTextProvider) {
			defaultInstance = optHelpTextProvider;
		}

		private OptionHelpTextProviders() {	}

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

	public static class Options {

		public static Options newInstance(final List<Option> opts) {
			return new Options(opts);
		}
		
		public static Options newInstance(final Option... opts) {
			return new Options(Arrays.asList(opts));
		}
		
		private final List<Option> options;
		
		protected Options() {
			this(DefaultOptionComparator.INSTANCE);
		}
		
		protected Options(final Comparator<Option> comparator) {
			Class<?> cls = this.getClass();
			Field[] fields = cls.getFields();
			List<Option> opts = new ArrayList<Option>();
			for (Field field : fields) {
				int modifiers = field.getModifiers();
				Class<?> type = field.getType();
				boolean isStatic = Modifier.isStatic(modifiers);
				boolean isFinal = Modifier.isFinal(modifiers);
				boolean isTypeOption = Option.class.isAssignableFrom(type);
				if (isStatic && isFinal && isTypeOption) {
					Option opt = null;
					try {
						opt = (Option) field.get(null);
					} catch (IllegalArgumentException e) {
						throw new AssertionError(e);
					} catch (IllegalAccessException e) {
						throw new AssertionError(e);
					}
					if (opt == null) {
						throw new NullPointerException(String.format(
								"Field '%s %s' in class '%s' must not be null",
								Modifier.toString(modifiers),
								field.getName(),
								cls.getName()));
					}
					opts.add(opt);
				}
			}
			Comparator<Option> cmprtr = comparator;
			if (cmprtr == null) {
				cmprtr = DefaultOptionComparator.INSTANCE;
			}
			Collections.sort(opts, cmprtr);
			this.options = new ArrayList<Option>(opts);
		}
		
		private Options(final List<Option> opts) {
			for (Option opt : opts) {
				if (opt == null) {
					throw new NullPointerException(
							"Option(s) must not be null");
				}
			}
			this.options = new ArrayList<Option>(opts);
		}
		
		public final void printHelpText() {
			this.printHelpText(System.out);
		}
		
		public final void printHelpText(final PrintStream s) {
			this.printHelpText(new PrintWriter(s));
		}
		
		public final void printHelpText(final PrintWriter w) {
			boolean earlierHelpTextNotNull = false;
			for (Option option : this.options) {
				String helpText = option.getHelpText();
				if (helpText != null) {
					if (earlierHelpTextNotNull) {
						w.println();
					}
					w.print(helpText);
					w.flush();
					if (!earlierHelpTextNotNull) {
						earlierHelpTextNotNull = true;
					}
				}
			}
		}

		public final void printUsage() {
			this.printUsage(System.out);
		}
		
		public final void printUsage(final PrintStream s) {
			this.printUsage(new PrintWriter(s));
		}
		
		public final void printUsage(final PrintWriter w) {
			boolean earlierUsageNotNull = false;
			for (Option option : this.options) {
				String usage = null;
				for (Option opt : option.getAllOptions()) {
					if (!opt.isHidden() && !opt.isSpecial() 
							&& (usage = opt.getUsage()) != null) {
						break;
					}
				}
				if (usage != null) {
					if (earlierUsageNotNull) {
						w.print(" ");
					}
					w.print(String.format("[%s]", usage));
					w.flush();
					if (!earlierUsageNotNull) {
						earlierUsageNotNull = true;
					}
				}
			}
		}
		
		public final List<Option> toList() {
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
	
	public static interface OptionUsageProvider {

		String getOptionUsage(OptionUsageParams params);
		
	}
	
	public static final class OptionUsageProviders {

		private static final Map<Class<? extends Option>, OptionUsageProvider> DEFAULT_INSTANCES = 
				new HashMap<Class<? extends Option>, OptionUsageProvider>();

		static {
			DEFAULT_INSTANCES.put(
					GnuLongOption.class, 
					DefaultGnuLongOptionUsageProvider.INSTANCE);
			DEFAULT_INSTANCES.put(
					LongOption.class, 
					DefaultLongOptionUsageProvider.INSTANCE);
			DEFAULT_INSTANCES.put(
					PosixOption.class, 
					DefaultPosixOptionUsageProvider.INSTANCE);
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

		private OptionUsageProviders() { }

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
			public Builder optionHelpTextProvider(
					final OptionHelpTextProvider optHelpTextProvider) {
				super.optionHelpTextProvider(optHelpTextProvider);
				return this;
			}
			
			@Override
			public Builder optionUsageProvider(
					final OptionUsageProvider optUsageProvider) {
				super.optionUsageProvider(optUsageProvider);
				return this;
			}
			
			@Override
			public Builder ordinal(final int i) {
				super.ordinal(i);
				return this;
			}
			
			@Override
			public Builder otherBuilders(
					final ArgMatey.Option.Builder otherBldr) {
				super.otherBuilders(otherBldr);
				return this;
			}
			
			@Override
			public Builder otherBuilders(
					final ArgMatey.Option.Builder otherBldr1,
					final ArgMatey.Option.Builder otherBldr2) {
				super.otherBuilders(otherBldr1, otherBldr2);
				return this;
			}
			
			@Override
			public Builder otherBuilders(
					final ArgMatey.Option.Builder otherBldr1,
					final ArgMatey.Option.Builder otherBldr2,
					final ArgMatey.Option.Builder... additionalOtherBldrs) {
				super.otherBuilders(
						otherBldr1, otherBldr2, additionalOtherBldrs);
				return this;
			}
			
			@Override
			public Builder otherBuilders(
					final List<ArgMatey.Option.Builder> otherBldrs) {
				super.otherBuilders(otherBldrs);
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
	public static interface StringConverter {

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
		Object convert(String string);
		
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
