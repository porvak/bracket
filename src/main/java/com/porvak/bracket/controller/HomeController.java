package com.porvak.bracket.controller;

import com.porvak.bracket.domain.Tournament;
import com.porvak.bracket.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
public class HomeController {

    @Autowired
    TournamentRepository tournamentRepository;

    @RequestMapping(value = "/")
    public String home(){
        return "index";
    }
    
    @ResponseBody
    @RequestMapping(value = "/tournament/{id}", method = GET)
    public Tournament getTournamentById(@PathVariable("id") String id){
        return tournamentRepository.findTournamentById(id);
    }
}
