package com.porvak.bracket.config;

import com.porvak.bracket.social.develop.oauth.ConcurrentMapOAuthSessionManager;
import com.porvak.bracket.social.develop.oauth.OAuthSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@ImportResource("classpath:com/porvak/bracket/config/security.xml")
public class SecurityConfig {

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