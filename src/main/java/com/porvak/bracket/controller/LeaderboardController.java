package com.porvak.bracket.controller;

import com.porvak.bracket.domain.Leaderboard;
import com.porvak.bracket.repository.LeaderboardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
public class LeaderboardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeaderboardController.class);
    
    @Inject
    private LeaderboardRepository leaderboardRepository;
    
    @ResponseBody
    @RequestMapping(value = "/leaderboard/pool/{poolId}", method = GET)
    public List<Leaderboard> getLeaderboard(@PathVariable("poolId") String poolId){
        LOGGER.debug("Hello get Leaderboard [{}]", poolId);
        List<Leaderboard> leaderboards = leaderboardRepository.findByPoolId(poolId, new Sort(DESC, "score"));
        return leaderboards;
    }
    
}
