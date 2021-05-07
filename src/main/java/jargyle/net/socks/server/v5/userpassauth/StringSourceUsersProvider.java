package jargyle.net.socks.server.v5.userpassauth;

final class StringSourceUsersProvider extends UsersProvider {

	private final Users users;
	
	public StringSourceUsersProvider(final String string) {
		super(string);
		this.users = Users.newInstance(string);
	}
	
	@Override
	public Users getUsers() {
		return Users.newInstance(this.users);
	}

}
