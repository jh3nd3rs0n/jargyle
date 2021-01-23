package jargyle.server.socks5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import jargyle.common.xml.bind.XmlBindHelper;

public final class Users {
	
	private static final class CustomSchemaOutputResolver 
		extends SchemaOutputResolver {

		private final Result result;
		
		public CustomSchemaOutputResolver(final Result res) {
			this.result = res;
		}
		
		@Override
		public Result createOutput(
				final String namespaceUri, 
				final String suggestedFileName) throws IOException {
			return this.result;
		}
		
	}

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlRootElement(name = "users")
	@XmlType(name = "users", propOrder = { "users" })
	static class UsersXml {
		@XmlElement(name = "user")
		protected List<User> users = new ArrayList<User>();
	}

	public static byte[] getXsd() {
		if (!XmlBindHelper.isOptimizedCodeGenerationDisabled()) {
			XmlBindHelper.setOptimizedCodeGenerationDisabled(true);
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JAXBContext jaxbContext = null;
		try {
			jaxbContext = JAXBContext.newInstance(UsersXml.class);
		} catch (JAXBException e) {
			throw new AssertionError(e);
		}
		StreamResult result = new StreamResult(out);
		result.setSystemId("");
		try {
			jaxbContext.generateSchema(new CustomSchemaOutputResolver(result));
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		return out.toByteArray();
	}
	
	public static void main(final String[] args) {
		UsersCLI usersCLI = new UsersCLI(null, null, args, false);
		int status = usersCLI.handleArgs();
		if (status != 0) { System.exit(status);	}
	}
	
	public static Users newInstance(final List<User> usrs) {
		return new Users(usrs);
	}
	
	public static Users newInstance(final String s) {
		List<User> users = new ArrayList<User>();
		String[] sElements = s.split(" ");
		for (String sElement : sElements) {
			users.add(User.newInstance(sElement));
		}
		return new Users(users);
	}
	
	public static Users newInstance(final User... usrs) {
		return newInstance(Arrays.asList(usrs));
	}
	
	private static Users newInstance(final UsersXml usersXml) {
		return newInstance(usersXml.users);
	}
	
	public static Users newInstanceFrom(
			final InputStream in) throws IOException {
		if (!XmlBindHelper.isOptimizedCodeGenerationDisabled()) {
			XmlBindHelper.setOptimizedCodeGenerationDisabled(true);
		}
		JAXBContext jaxbContext = null;
		try {
			jaxbContext = JAXBContext.newInstance(UsersXml.class);
		} catch (JAXBException e) {
			throw new IOException(e);
		}
		Unmarshaller unmarshaller = null;
		try {
			unmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			throw new IOException(e);
		}
		try {
			unmarshaller.setEventHandler(new DefaultValidationEventHandler());
		} catch (JAXBException e) {
			throw new IOException(e);
		}
		UsersXml usersXml = null;
		try {
			usersXml = (UsersXml) unmarshaller.unmarshal(in);
		} catch (JAXBException e) {
			throw new IOException(e);
		}
		return newInstance(usersXml);
	}
	
	private final List<User> users;
	
	private Users(final List<User> usrs) {
		this.users = new ArrayList<User>(usrs);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Users other = (Users) obj;
		if (this.users == null) {
			if (other.users != null) {
				return false;
			}
		} else if (!this.users.equals(other.users)) {
			return false;
		}
		return true;
	}
	
	public List<User> get(final String name) {
		List<User> usrs = new ArrayList<User>();
		for (User user : this.users) {
			if (user.getName().equals(name)) {
				usrs.add(user);
			}
		}
		return Collections.unmodifiableList(usrs);
	}
	
	public User getLast(final String name) {
		List<User> usrs = this.get(name);
		User usr = null;
		if (usrs.size() > 0) {
			usr = usrs.get(usrs.size() - 1);
		}
		return usr;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.users == null) ? 0 : this.users.hashCode());
		return result;
	}

	public List<User> toList() {
		return Collections.unmodifiableList(this.users);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [users=")
			.append(this.users)
			.append("]");
		return builder.toString();
	}

	private UsersXml toUsersXml() {
		UsersXml usersXml = new UsersXml();
		usersXml.users.addAll(this.users);
		return usersXml;
	}
	
	public byte[] toXml() {
		if (!XmlBindHelper.isOptimizedCodeGenerationDisabled()) {
			XmlBindHelper.setOptimizedCodeGenerationDisabled(true);
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JAXBContext jaxbContext = null;
		try {
			jaxbContext = JAXBContext.newInstance(UsersXml.class);
		} catch (JAXBException e) {
			throw new AssertionError(e);
		}
		Marshaller marshaller = null;
		try {
			marshaller = jaxbContext.createMarshaller();
		} catch (JAXBException e) {
			throw new AssertionError(e);
		}
		try {
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		} catch (PropertyException e) {
			throw new AssertionError(e);
		}
		try {
			marshaller.marshal(this.toUsersXml(), out);
		} catch (JAXBException e) {
			throw new AssertionError(e);
		}
		return out.toByteArray();
	}
}
