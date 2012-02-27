package com.porvak.bracket.config;

import com.porvak.bracket.socialize.account.Account;
import com.porvak.bracket.socialize.account.AccountRepository;
import com.porvak.bracket.socialize.account.AccountUtils;
import com.porvak.bracket.socialize.connect.AccountSignInAdapter;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import javax.sql.DataSource;

@Configuration
//@ComponentScan(basePackages="org.springframework.social")
public class SocialConfig {

    @Inject
    private Environment environment;

    @Inject
    private DataSource dataSource;

    @Inject
    private TextEncryptor textEncryptor;

    @Bean
    @Scope(value="singleton", proxyMode=ScopedProxyMode.INTERFACES)
    public ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new TwitterConnectionFactory(environment.getProperty("twitter.consumerKey"), environment.getProperty("twitter.consumerSecret")));
        return registry;
    }


    @Bean
    public UsersConnectionRepository usersConnectionRepository() {
//		return new MongoUsersConnectionRepository(mongoTemplate);
        return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator(), textEncryptor);
    }

    @Bean
    @Scope(value="request", proxyMode= ScopedProxyMode.INTERFACES)
    public ConnectionRepository connectionRepository() {
        Account account = AccountUtils.getCurrentAccount();
        if (account == null) {
            throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
        }
        return usersConnectionRepository().createConnectionRepository(account.getId().toString());
    }

    @Bean
    @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
    public Twitter twitter() {
        Connection<Twitter> twitter = connectionRepository().findPrimaryConnection(Twitter.class);
        return twitter != null ? twitter.getApi() : new TwitterTemplate();
    }

    @Bean
    public ProviderSignInController providerSignInController(AccountRepository accountRepository, RequestCache requestCache) {
        return new ProviderSignInController(connectionFactoryLocator(), usersConnectionRepository(), new AccountSignInAdapter(accountRepository, requestCache));
    }
}
