/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.porvak.bracket.social.develop.oauth;

import com.porvak.bracket.social.account.Account;
import com.porvak.bracket.social.account.AccountRepository;
import com.porvak.bracket.social.account.AccountUtils;
import com.porvak.bracket.social.develop.AppConnection;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.provider.token.OAuthAccessProviderToken;
import org.springframework.security.oauth.provider.token.OAuthProviderToken;


/**
 * Adapts a {@link } returned by the {@link } to a Spring Security {@link OAuthProviderToken}.
 * @author Keith Donald
 * @see OAuthSessionManagerProviderTokenServices
 */
@SuppressWarnings("serial") 
class AppConnectionProviderToken implements OAuthAccessProviderToken {

	private AppConnection connection;

	private AccountRepository accountRepository;
	
	private Authentication userAuthentication;
	
	public AppConnectionProviderToken(AppConnection connection, AccountRepository accountRepository) {
		this.connection = connection;
		this.accountRepository = accountRepository;
	}

	public String getConsumerKey() {
		return connection.getApiKey();
	}
	
	public String getValue() {
		return connection.getAccessToken();
	}
	
	public String getSecret() {
		return connection.getSecret();
	}
	
	public String getCallbackUrl() {
		return null;
	}

	public String getVerifier() {
		return null;
	}

	public boolean isAccessToken() {
		return true;
	}
	
	public Authentication getUserAuthentication() {
		if (userAuthentication == null) {
			Account account = accountRepository.findById(connection.getAccountId());
			return AccountUtils.authenticationTokenFor(account);
		}
		return userAuthentication;
	}
		
}