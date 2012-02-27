package com.porvak.bracket.social.develop.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth.common.OAuthException;
import org.springframework.security.oauth.common.signature.SharedConsumerSecret;
import org.springframework.security.oauth.common.signature.SignatureSecret;
import org.springframework.security.oauth.provider.ConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


/**
 * Adapts App records returned by an {@link com.porvak.bracket.social.develop.AppRepository} to Spring Security OAuth {@link org.springframework.security.oauth.provider.ConsumerDetails}.
 * Allows an AppRepository to serve as the source for OAuth Consumers known to the Spring Security OAuth provider.
 * @author Keith Donald
 */
@Service
public class AppConsumerDetailsService implements ConsumerDetailsService {

	public ConsumerDetails loadConsumerByConsumerKey(String key) throws OAuthException {
//		try {
//			return consumerDetailsFor();
		/*} catch (InvalidApiKeyException e) {
			throw new OAuthException("Invalid OAuth consumer key " + key, e);
		}
		*/
        return new AppConsumerDetails();
	}

	private ConsumerDetails consumerDetailsFor() {
		return new AppConsumerDetails();
	}

	@SuppressWarnings("serial")
	private static class AppConsumerDetails implements ConsumerDetails {

		public String getConsumerName() {
			// TODO
            return "Bracket";

			//return app.getSummary().getName();
		}

		public String getConsumerKey() {
            return "TODO";
			//return app.getApiKey();
		}

		@Override
		public SignatureSecret getSignatureSecret() {
            return new SharedConsumerSecret("TODO");
			//return new SharedConsumerSecret(app.getSecret());
		}

		public List<GrantedAuthority> getAuthorities() {
			return Collections.emptyList();
		}

	}

}