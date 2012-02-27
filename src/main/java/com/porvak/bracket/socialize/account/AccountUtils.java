package com.porvak.bracket.socialize.account;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

public final class AccountUtils {

	/**
	 * Programmatically sign-in the member holding the provided Account.
	 * Sets the Account in the SecurityContext, which will associate this Account with the user Session.
	 */
	public static void signin(com.porvak.bracket.socialize.account.Account account) {
		SecurityContextHolder.getContext().setAuthentication(authenticationTokenFor(account));
	}

	/**
	 * Construct a Spring Security Authentication token from an Account object.
	 * Useful for treating the Account as a Principal in Spring Security.
	 */
	public static Authentication authenticationTokenFor(com.porvak.bracket.socialize.account.Account account) {
		return new UsernamePasswordAuthenticationToken(account, null, (Collection<GrantedAuthority>)null);
	}

	/**
	 * Get the currently authenticated Account principal.
	 */
	public static com.porvak.bracket.socialize.account.Account getCurrentAccount() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		return principal instanceof com.porvak.bracket.socialize.account.Account ? (Account) principal : null;
	}
	
	private AccountUtils() {
	}
}
