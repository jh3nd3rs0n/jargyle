package jargyle.server.socks5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
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

	public static byte[] getXsd() throws JAXBException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JAXBContext jaxbContext = JAXBContext.newInstance(UsersXml.class);
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
		/* 
		 * https://stackoverflow.com/questions/50237516/proper-fix-for-java-10-complaining-about-illegal-reflection-access-by-jaxb-impl#50251510
		 */
		System.setProperty(
				"com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize", "true");
		UsersCLI usersCLI = new UsersCLI(null, null, args, false);
		int status = usersCLI.handleRemaining();
		if (status != 0) { System.exit(status);	}
	}
	
	public static Users newInstance(final List<User> usrs) {
		return new Users(usrs);
	}
	
	public static Users newInstance(final String s) {
		List<User> users = new ArrayList<User>();
		String[] sElements = s.split("\\s");
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
			final InputStream in) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(UsersXml.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setEventHandler(new DefaultValidationEventHandler());
		UsersXml usersXml = (UsersXml) unmarshaller.unmarshal(in);
		return newInstance(usersXml);
	}
	
	private final List<User> users;
	
	private Users(final List<User> usrs) {
		this.users = new ArrayList<User>(usrs);
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
	
	public List<User> toList() {
		return Collections.unmodifiableList(this.users);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Iterator<User> iterator = this.users.iterator(); 
				iterator.hasNext();) {
			User user = iterator.next();
			builder.append(user.toString());
			if (iterator.hasNext()) {
				builder.append(" ");
			}
		}
		return builder.toString();
	}
	
	private UsersXml toUsersXml() {
		UsersXml usersXml = new UsersXml();
		usersXml.users.addAll(this.users);
		return usersXml;
	}
	
	public byte[] toXml() throws JAXBException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JAXBContext jaxbContext = JAXBContext.newInstance(UsersXml.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(this.toUsersXml(), out);
		return out.toByteArray();
	}
}
