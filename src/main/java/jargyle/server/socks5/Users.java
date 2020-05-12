package jargyle.server.socks5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

public final class Users {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlRootElement(name = "users")
	@XmlType(name = "users", propOrder = { "users" })
	public static class UsersXml {
		@XmlElement(name = "user")
		protected List<User> users = new ArrayList<User>();
	}

	public static void main(final String[] args) {
		UsersCli usersCli = new UsersCli();
		usersCli.process(args);
	}
	
	public static Users newInstance(final List<User> usrs) {
		return new Users(usrs);
	}
	
	public static Users newInstance(final String s) {
		List<User> users = new ArrayList<User>();
		String[] sElements = s.split(",");
		for (String sElement : sElements) {
			users.add(User.newInstance(sElement));
		}
		return new Users(users);
	}
	
	public static Users newInstance(final User... usrs) {
		return newInstance(Arrays.asList(usrs));
	}
	
	public static Users newInstance(final UsersXml usersXml) {
		return newInstance(usersXml.users);
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
				builder.append(",");
			}
		}
		return builder.toString();
	}
	
	public UsersXml toUsersXml() {
		UsersXml usersXml = new UsersXml();
		usersXml.users.addAll(this.users);
		return usersXml;
	}
}
