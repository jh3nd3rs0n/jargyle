package jargyle.net.socks.server.v5.userpassauth;

final class StringSourceUsersService extends UsersService {

	private final Users users;
	
	public StringSourceUsersService(final String string) {
		this.users = Users.newInstance(string);
	}
	
	@Override
	public Users getUsers() {
		return Users.newInstance(this.users.toList());
	}

}
