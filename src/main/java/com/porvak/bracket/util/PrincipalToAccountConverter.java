package com.porvak.bracket.util;

import com.google.common.base.Predicate;
import com.porvak.bracket.socialize.account.Account;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;

import static com.google.common.base.Predicates.*;

public class PrincipalToAccountConverter implements Converter<Principal, Account> {
    private static final Predicate<Object> isTokenInstance = and(instanceOf(UsernamePasswordAuthenticationToken.class), notNull());
    private static final Predicate<Object> isAccountInstance = and(instanceOf(Account.class), notNull());

    @Override
    public Account convert(Principal source) {
        if(isTokenInstance.apply(source)){
            Object token = ((UsernamePasswordAuthenticationToken) source).getPrincipal();
            if(isAccountInstance.apply(token)){
                return (Account) token;
            }
        }
        return null;
    }
}
