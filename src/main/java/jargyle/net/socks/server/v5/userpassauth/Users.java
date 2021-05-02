package jargyle.net.socks.server.v5.userpassauth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

	public static void generateXsd(final OutputStream out) throws IOException {
		JAXBContext jaxbContext = null;
		try {
			jaxbContext = JAXBContext.newInstance(UsersXml.class);
		} catch (JAXBException e) {
			throw new AssertionError(e);
		}
		StreamResult result = new StreamResult(out);
		result.setSystemId("");
		jaxbContext.generateSchema(new CustomSchemaOutputResolver(result));
	}
	
	public static void main(final String[] args) {
		UsersCLI usersCLI = new UsersCLI(null, null, args, false);
		Optional<Integer> status = usersCLI.handleArgs();
		if (status.isPresent() && status.get().intValue() != 0) { 
			System.exit(status.get().intValue());
		}
	}
	
	public static Users newInstance(final List<User> usrs) {
		Map<String, User> u = new LinkedHashMap<String, User>();
		for (User usr : usrs) {
			u.put(usr.getName(), usr);
		}
		return new Users(u);
	}
	
	public static Users newInstance(final String s) {
		Map<String, User> users = new LinkedHashMap<String, User>();
		if (s.isEmpty()) {
			return new Users(users);
		}
		String[] sElements = s.split(" ");
		for (String sElement : sElements) {
			User user = User.newInstance(sElement);
			users.put(user.getName(), user);
		}
		return new Users(users);
	}
	
	public static Users newInstance(final User... usrs) {
		return newInstance(Arrays.asList(usrs));
	}
	
	public static Users newInstance(final Users usrs) {
		return new Users(usrs);
	}
	
	private static Users newInstance(final UsersXml usersXml) {
		return newInstance(usersXml.users);
	}

	public static Users newInstanceFromXml(
			final InputStream in) throws IOException {
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
	
	private final Map<String, User> users;
	
	private Users(final Map<String, User> usrs) {
		this.users = new LinkedHashMap<String, User>(usrs);
	}
	
	private Users(final Users other) {
		this.users = new LinkedHashMap<String, User>(other.users);
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
	
	public User get(final String name) {
		return this.users.get(name);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.users == null) ? 
				0 : this.users.hashCode());
		return result;
	}

	public User put(final User usr) {
		return this.users.put(usr.getName(), usr);
	}
	
	public void putAll(final Users usrs) {
		this.users.putAll(usrs.toMap());
	}
	
	public User remove(final String name) {
		return this.users.remove(name);
	}

	public Map<String, User> toMap() {
		return Collections.unmodifiableMap(this.users);
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
		usersXml.users.addAll(this.users.values());
		return usersXml;
	}
	
	public void toXml(final OutputStream out) throws IOException {
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
			throw new IOException(e);
		}
	}	
}
