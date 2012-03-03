package com.porvak.bracket.config;

import com.porvak.bracket.social.develop.oauth.ConcurrentMapOAuthSessionManager;
import com.porvak.bracket.social.develop.oauth.OAuthSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class TestSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.noOpText();
    }

    @Bean
    public OAuthSessionManager oauthSessionManager() {
        return new ConcurrentMapOAuthSessionManager();
    }
}
