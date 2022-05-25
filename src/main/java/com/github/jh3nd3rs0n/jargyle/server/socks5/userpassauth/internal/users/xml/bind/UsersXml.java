package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.users.xml.bind;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.User;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.Users;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.helpers.DefaultValidationEventHandler;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "users")
@XmlType(name = "users", propOrder = { "usersXml" }) 
public class UsersXml {

	public static UsersXml newInstanceFromXml(
			final InputStream in) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(UsersXml.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setEventHandler(new DefaultValidationEventHandler());
		return (UsersXml) unmarshaller.unmarshal(in);
	}
	
	@XmlElement(name = "user")
	protected List<UserXml> usersXml = new ArrayList<UserXml>();
	
	public UsersXml() {
		this.usersXml = new ArrayList<UserXml>(); 
	}
	
	public UsersXml(final Users users) {
		this.usersXml = new ArrayList<UserXml>();
		for (User value : users.toMap().values()) {
			this.usersXml.add(new UserXml(value));
		}
	}
	
	public Users toUsers() {
		List<User> users = new ArrayList<User>();
		for (UserXml userXml : this.usersXml) {
			users.add(userXml.toUser());
		}
		return Users.newInstance(users);
	}
	
	public void toXml(final OutputStream out) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(UsersXml.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(this, out);			
	}
	
}