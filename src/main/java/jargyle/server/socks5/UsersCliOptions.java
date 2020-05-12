package jargyle.server.socks5;

import argmatey.ArgMatey.GnuLongOption;
import argmatey.ArgMatey.Option;
import argmatey.ArgMatey.Options;
import argmatey.ArgMatey.PosixOption;

public final class UsersCliOptions extends Options {
	
	public static final Option HELP_OPTION = new GnuLongOption.Builder("help")
			.doc("Print this help and exit")
			.ordinal(0)
			.otherBuilders(new PosixOption.Builder('h'))
			.special(true)
			.build();
	
	public static final Option XSD_OPTION = new GnuLongOption.Builder("xsd")
			.doc("Print the XSD and exit")
			.ordinal(1)
			.otherBuilders(new PosixOption.Builder('x'))
			.special(true)
			.build();

	public UsersCliOptions() { }
	
}
