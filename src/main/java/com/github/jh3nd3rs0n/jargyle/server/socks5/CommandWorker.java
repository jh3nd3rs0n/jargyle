package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.internal.logging.LoggerHelper;
import com.github.jh3nd3rs0n.jargyle.server.RuleActionDenyException;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public abstract class CommandWorker {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			CommandWorker.class);

	private final CommandWorkerContext commandWorkerContext;
	private final Settings settings;
	
	public CommandWorker(final CommandWorkerContext context) {
		Settings sttngs = context.getSettings();
		this.commandWorkerContext = context;
		this.settings = sttngs;
	}

	protected boolean canAllow(
			final String clientAddress,
			final MethodSubnegotiationResults methSubnegotiationResults,
			final Socks5Request socks5Req,
			final Socks5Reply socks5Rep) {
		String user = methSubnegotiationResults.getUser();
		String possibleUser = (user != null) ? 
				String.format(" (%s)", user) : "";		
		Socks5ReplyRules socks5ReplyRules = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_SOCKS5_REPLY_RULES);
		Socks5ReplyRule socks5ReplyRule = null;
		try {
			socks5ReplyRule = socks5ReplyRules.anyAppliesTo(
					clientAddress, 
					methSubnegotiationResults, 
					socks5Req,
					socks5Rep);
		} catch (IllegalArgumentException e) {
			LOGGER.error(
					LoggerHelper.objectMessage(this, String.format(
							"Error regarding SOCKS5 reply to %s%s. "
							+ "SOCKS5 request: %s. SOCKS5 reply: %s",
							clientAddress,
							possibleUser,
							socks5Req,
							socks5Rep)),
					e);
			Socks5Reply rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Sending %s",
					rep.toString())));			
			try {
				this.commandWorkerContext.writeThenFlush(
						rep.toByteArray());
			} catch (IOException ex) {
				LOGGER.error(
						LoggerHelper.objectMessage(
								this, "Error in writing SOCKS5 reply"), 
						e);
			}
			return false;			
		}
		try {
			socks5ReplyRule.applyTo(
					clientAddress, 
					methSubnegotiationResults, 
					socks5Req,
					socks5Rep);
		} catch (RuleActionDenyException e) {
			Socks5Reply rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Sending %s",
					rep.toString())));			
			try {
				this.commandWorkerContext.writeThenFlush(rep.toByteArray());
			} catch (IOException ex) {
				LOGGER.error( 
						LoggerHelper.objectMessage(
								this, "Error in writing SOCKS5 reply"), 
						e);
			}
			return false;
		}
		return true;
	}
	
	public abstract void run() throws IOException;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [commandWorkerContext=")
			.append(this.commandWorkerContext)
			.append("]");
		return builder.toString();
	}
	
}
