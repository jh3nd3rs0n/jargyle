package jargyle.server;

import argmatey.ArgMatey.GnuLongOption;
import argmatey.ArgMatey.Option;
import argmatey.ArgMatey.OptionArgSpec;
import argmatey.ArgMatey.OptionUsageParams;
import argmatey.ArgMatey.OptionUsageProvider;
import argmatey.ArgMatey.Options;
import argmatey.ArgMatey.PosixOption;
import jargyle.client.socks5.UsernamePassword;
import jargyle.server.socks5.UsernamePasswordAuthenticator;

public final class SocksServerCliOptions extends Options {
	
	public static final Option ALLOWED_CLIENT_ADDRESSES_OPTION =
			new GnuLongOption.Builder("allowed-client-addresses")
			.doc("The comma separated list of allowed client addresses as "
					+ "expressions")
			.optionArgSpec(new OptionArgSpec.Builder()
					.name("EXPRESSIONS")
					.type(Expressions.class)
					.build())
			.optionUsageProvider(new OptionUsageProvider() {

				@Override
				public String getOptionUsage(final OptionUsageParams params) {
					return String.format(
							"%1$s=[%2$s:%3$s1[,%2$s:%3$s2[...]]]", 
							params.getOption(),
							"lit|regex",
							"EXPRESSION");
				}
				
			})			
			.ordinal(0)
			.build();
	
	public static final Option BLOCKED_CLIENT_ADDRESSES_OPTION =
			new GnuLongOption.Builder("blocked-client-addresses")
			.doc("The comma separated list of blocked client addresses as "
					+ "expressions")
			.optionArgSpec(new OptionArgSpec.Builder()
					.name("EXPRESSIONS")
					.type(Expressions.class)
					.build())
			.optionUsageProvider(new OptionUsageProvider() {

				@Override
				public String getOptionUsage(final OptionUsageParams params) {
					return String.format(
							"%1$s=[%2$s:%3$s1[,%2$s:%3$s2[...]]]", 
							params.getOption(),
							"lit|regex",
							"EXPRESSION");
				}
				
			})
			.ordinal(1)
			.build();
	
	public static final Option CONFIG_FILE_OPTION = new GnuLongOption.Builder(
			"config-file")
			.doc("The configuration file")
			.optionArgSpec(new OptionArgSpec.Builder()
					.name("FILE")
					.build())
			.ordinal(2)
			.otherBuilders(new PosixOption.Builder('f'))
			.build();
	
	public static final Option CONFIG_FILE_XSD_OPTION = 
			new GnuLongOption.Builder("config-file-xsd")
			.doc("Print the configuration file XSD and exit")
			.ordinal(3)
			.otherBuilders(new PosixOption.Builder('x'))
			.special(true)
			.build();
	
	public static final Option ENTER_EXTERNAL_CLIENT_SOCKS5_USER_PASS_OPTION =
			new GnuLongOption.Builder("enter-external-client-socks5-user-pass")
			.doc("Enter through an interactive prompt the username password "
					+ "for the external SOCKS5 server for external connections")
			.ordinal(4)
			.build();
	
	public static final Option EXTERNAL_CLIENT_SOCKS5_USER_PASS_OPTION =
			new GnuLongOption.Builder("external-client-socks5-user-pass")
			.doc("The username password for the external SOCKS5 server for "
					+ "external connections")
			.optionArgSpec(new OptionArgSpec.Builder()
					.name("USERNAME_PASSWORD")
					.type(UsernamePassword.class)
					.build())
			.optionUsageProvider(new OptionUsageProvider() {

				@Override
				public String getOptionUsage(final OptionUsageParams params) {
					return String.format(
							"%s=USERNAME:PASSWORD",	params.getOption());
				}
				
			})
			.ordinal(5)
			.build();
	
	public static final Option HELP_OPTION = new GnuLongOption.Builder("help")
			.doc("Print this help and exit")
			.ordinal(6)
			.otherBuilders(new PosixOption.Builder('h'))
			.special(true)
			.build();
	
	public static final Option MONITORED_CONFIG_FILE = 
			new GnuLongOption.Builder("monitored-config-file")
			.doc("The monitored configuration file")
			.optionArgSpec(new OptionArgSpec.Builder()
					.name("FILE")
					.build())
			.ordinal(7)
			.otherBuilders(new PosixOption.Builder('m'))
			.build();
	
	public static final Option NEW_CONFIG_FILE_OPTION = 
			new GnuLongOption.Builder("new-config-file")
			.doc("Create a new configuration file based on the preceding "
					+ "options and exit")
			.optionArgSpec(new OptionArgSpec.Builder()
					.name("FILE")
					.build())
			.ordinal(8)
			.otherBuilders(new PosixOption.Builder('n'))
			.special(true)
			.build();
	
	public static final Option SETTINGS_HELP_OPTION = new GnuLongOption.Builder(
			"settings-help")
			.doc("Print the list of available settings for the SOCKS server "
					+ "and exit")
			.ordinal(9)
			.otherBuilders(new PosixOption.Builder('H'))
			.special(true)
			.build();
	
	public static final Option SETTINGS_OPTION = new GnuLongOption.Builder(
			"settings")
			.doc("The comma-separated list of settings for the SOCKS server")
			.optionArgSpec(new OptionArgSpec.Builder()
					.name("SETTINGS")
					.type(Settings.class)
					.build())
			.optionUsageProvider(new OptionUsageProvider() {

				@Override
				public String getOptionUsage(final OptionUsageParams params) {
					return String.format(
							"%1$s=[%2$s1=%3$s1[,%2$s2=%3$s2[...]]]", 
							params.getOption(),
							"NAME",
							"VALUE");
				}
				
			})
			.ordinal(10)
			.otherBuilders(new PosixOption.Builder('s')
					.optionUsageProvider(new OptionUsageProvider() {
						
						@Override
						public String getOptionUsage(
								final OptionUsageParams params) {
							return String.format(
									"%1$s [%2$s1=%3$s1[,%2$s2=%3$s2[...]]]",
									params.getOption(),
									"NAME",
									"VALUE");
						}
						
					}))
			.build();
	
	public static final Option SOCKS5_USER_PASS_AUTHENTICATOR_OPTION = 
			new GnuLongOption.Builder("socks5-user-pass-authenticator")
			.doc("The SOCKS5 username password authenticator for the SOCKS server")
			.optionArgSpec(new OptionArgSpec.Builder()
					.name("SOCKS5_USER_PASS_AUTHENTICATOR")
					.type(UsernamePasswordAuthenticator.class)
					.build())
			.optionUsageProvider(new OptionUsageProvider() {

				@Override
				public String getOptionUsage(final OptionUsageParams params) {
					return String.format(
							"%s=CLASSNAME[:PARAMETER_STRING]", 
							params.getOption());
				}
				
			})
			.ordinal(11)
			.build();
	
	public static final Option SOCKS5_USERS_OPTION = new GnuLongOption.Builder(
			"socks5-users")
			.doc(String.format("Mode for managing SOCKS5 users "
					+ "(add %s for more information)",
					jargyle.server.socks5.UsersCliOptions.HELP_OPTION.getUsage()))
			.ordinal(12)
			.special(true)
			.build();
	
	public SocksServerCliOptions() { }
	
}
