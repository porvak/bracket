package com.porvak.bracket.socialize.account;

import com.porvak.bracket.domain.User;
import com.porvak.bracket.socialize.account.exception.SignInNotFoundException;

public interface AccountRepository {

	/**
	 * Create a new Account
     */
	Account createAccount(User user);

	/**
	 * Find an Account by its internal identifier.
	 */
	Account findById(String accountId);

	/**
	 * Find an Account by an eligible sign-in name. The sign-in name may be an account username, or an account email address, if that email address is unique.
	 */
	Account findBySignin(String signin) throws SignInNotFoundException;

}