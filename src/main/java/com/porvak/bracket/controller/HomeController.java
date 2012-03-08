package com.porvak.bracket.controller;

import com.porvak.bracket.service.TournamentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.security.Principal;

import static com.porvak.bracket.domain.BracketConstants.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
public class HomeController extends AbstractBracketController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @Inject
    private TournamentService tournamentService;

    @Inject
    private ConnectionRepository connectionRepository;

    @RequestMapping("/")
    public String home(Principal currentUser, Model model) {
        if (currentUser != null) {
            model.addAttribute("twitter_status", connectionRepository.findConnections("twitter").size() > 0 ? "Yes" : "No");
            model.addAttribute(getUserAccount(currentUser));
        }

        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/api/pub/tournament/{id}", method = GET)
    public Object getTournamentById(@PathVariable("id") String id, Principal currentUser){
        if(currentUser == null){
            return tournamentService.getTournament(id);
        }

        return tournamentService.getUserTournament(POOL_ID, getUserAccount(currentUser).getId());
    }
    
}
