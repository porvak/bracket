package com.porvak.bracket.controller;

import com.porvak.bracket.domain.Pool;
import com.porvak.bracket.domain.UserPick;
import com.porvak.bracket.domain.UserPicks;
import com.porvak.bracket.repository.PoolRepository;
import com.porvak.bracket.repository.UserPicksRepository;
import com.porvak.bracket.service.PoolService;
import com.porvak.bracket.socialize.account.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;
import java.security.Principal;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
public class PoolController extends AbstractBracketController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PoolController.class);

    @Inject
    private UserPicksRepository userPicksRepository;

    @Inject
    private PoolService poolService;

    @Inject
    private PoolRepository poolRepository;

    /**
     * Create or update a users pick for a specific game in a given pool.
     *
     * A user may only have one set of picks per pool, so if a users pick already exists then
     * the application will overwrite the existing pick for the given game.
     *
     * @param poolId
     * @param userPickMap Map that contains 'regionId', 'gameId', and 'teamId'
     * @param currentUser
     */
    @ResponseStatus(CREATED)
    @RequestMapping(value = "/api/pool/{poolId}/user/pick", method = POST)
    public void saveUserPick(@PathVariable("poolId") String poolId, @RequestBody Map<String, Object> userPickMap, Principal currentUser){
        //TODO: validate that the given user has rights to save this pick
        Account account = getUserAccount(currentUser);
        UserPick userPick = new UserPick(userPickMap);
        poolService.addUserPick(account.getId(), poolId, userPick);

        LOGGER.debug("Added user[{}] pick: poolId: [{}]with:\n{}", new Object[]{account.getId(), poolId, userPick});
    }

    /**
     * Return a users pick for a specific pool
     *
     * @param poolId
     * @param currentUser
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/pool/{poolId}/user/picks", method = GET)
    public UserPicks getUserPicks(@PathVariable("poolId")String poolId, Principal currentUser){
        Account account = getUserAccount(currentUser);
        return userPicksRepository.findByUserIdAndPoolId(account.getId(), poolId);
    }

    /**
     * Retrieve pool information for given pool
     *
     * @param poolId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/pool/{poolId}", method = GET)
    public Pool getPool(@PathVariable("poolId") String poolId){
        return poolRepository.findOne(poolId);
    }
}
