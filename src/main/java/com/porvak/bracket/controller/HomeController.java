package com.porvak.bracket.controller;

import com.porvak.bracket.domain.Tournament;
import com.porvak.bracket.domain.UserPick;
import com.porvak.bracket.domain.UserPicks;
import com.porvak.bracket.repository.TournamentRepository;
import com.porvak.bracket.repository.UserPicksRepository;
import com.porvak.bracket.social.account.AccountRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.security.Principal;

@Controller
public class HomeController {

    @Inject
    private AccountRepository accountRepository;

    @Inject
    private ConnectionRepository connectionRepository;

    @Inject
    private TournamentRepository tournamentRepository;
    
    @Inject
    private UserPicksRepository userPicksRepository;

    public HomeController() {
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
    
    @ResponseBody
    @RequestMapping(value = "/tournament/{id}", method = RequestMethod.GET)
    public Tournament getTournamentById(@PathVariable("id") String id){
        return tournamentRepository.findTournamentById(id);
    }
    
    @RequestMapping("/mongo")
    public String testMongo(){
        UserPicks userPicks = new UserPicks();
        userPicks.setPoolId("1");
        userPicks.setTournamentId("1");
        userPicks.addUserPick(new UserPick(1, 1, "teamId"));
        userPicksRepository.save(userPicks);
        return "index";
    }
}
