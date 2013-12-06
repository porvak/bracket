package com.porvak.bracket.socialize.account.exception;

@SuppressWarnings("serial")
public final class SignInNotFoundException extends AccountException {

	private final String username;
	
	public SignInNotFoundException(String username) {
		super("username not found");
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}

}
