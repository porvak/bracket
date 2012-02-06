package com.porvak.bracket.controller;

import com.porvak.bracket.social.account.Account;
import com.porvak.bracket.social.account.AccountRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import java.security.Principal;

@Controller
public class HomeController {

    private final AccountRepository accountRepository;

    private final ConnectionRepository connectionRepository;

    @Inject
    public HomeController(AccountRepository accountRepository, ConnectionRepository connectionRepository) {
        this.accountRepository = accountRepository;
        this.connectionRepository = connectionRepository;
    }

    @RequestMapping("/")
    public String home(Principal currentUser, Model model) {
        if (currentUser != null) {
            model.addAttribute("twitter_status", connectionRepository.findConnections("twitter").size() > 0 ? "Yes" : "No");
            //model.addAttribute(accountRepository.findById(((Account) currentUser).getId()).getFullName());
            model.addAttribute(((UsernamePasswordAuthenticationToken)currentUser).getPrincipal());
        }
        return "index";
    }

}
