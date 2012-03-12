package com.porvak.bracket.socialize.connect;

import com.porvak.bracket.socialize.account.Account;
import com.porvak.bracket.socialize.account.AccountRepository;
import com.porvak.bracket.socialize.account.AccountUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccountSignInAdapter implements SignInAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountSignInAdapter.class);
    private final AccountRepository accountRepository;

	private final RequestCache requestCache;

	@Inject
	public AccountSignInAdapter(AccountRepository accountRepository, RequestCache requestCache) {
		this.accountRepository = accountRepository;
		this.requestCache = requestCache;
	}

	public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
        LOGGER.debug("signIn: " + userId);
        //Long longUserId = Long.valueOf(userId);
		Account account = accountRepository.findById(userId);
		AccountUtils.signin(account);
		String extractedUrl = extractOriginalUrl(request);
        LOGGER.debug("Extracted URL: " + extractedUrl);
        return extractedUrl;
	}

	// internal helpers
	
	private String extractOriginalUrl(NativeWebRequest request) {
		HttpServletRequest nativeReq = request.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse nativeRes = request.getNativeResponse(HttpServletResponse.class);
		SavedRequest saved = requestCache.getRequest(nativeReq, nativeRes);
		if (saved == null) {
			return null;
		}
		requestCache.removeRequest(nativeReq, nativeRes);
		removeAutheticationAttributes(nativeReq.getSession(false));
		return saved.getRedirectUrl();
	}

	private void removeAutheticationAttributes(HttpSession session) {
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
	
}
