package com.porvak.bracket.controller;

import com.porvak.bracket.socialize.account.Account;
import org.springframework.core.convert.ConversionService;

import javax.inject.Inject;
import javax.inject.Named;
import java.security.Principal;

public abstract class AbstractBracketController {

    @Inject @Named("bracketConversionService")
    ConversionService conversionService;

    protected Account getUserAccount(Principal currentUser) {
        return conversionService.convert(currentUser, Account.class);
    }
}
