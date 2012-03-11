package com.porvak.bracket.service.impl;

import com.porvak.bracket.domain.UserPick;
import com.porvak.bracket.repository.PoolRepository;
import com.porvak.bracket.repository.UserPickRepository;
import com.porvak.bracket.service.PoolService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.util.Map;

@Service
public class PoolServiceImpl implements PoolService {

    @Inject
    private UserPickRepository userPickRepository;

    @Inject
    private PoolRepository poolRepository;

    @Override
    public void addUserPick(String userId, String poolId, Map<String, Object> userPickMap){
        Assert.noNullElements(new Object[]{userId, poolId, userPickMap}, String.format("No null elements allowed for " +
                "userId:[%s], poolId:[%s], userPickMap:[%s]", userId, poolId, userPickMap));

        userPickRepository.updateUserPick(userId, poolId, new UserPick(userPickMap));
        userPickRepository.updatePreviousGame(userId, poolId, userPickMap);
        userPickRepository.addGameWinner(userId, poolId, userPickMap);
    }

    @Override
    public void addTieBreaker(String userId, String poolId, int tieBreaker) {
        userPickRepository.addTieBreaker(userId, poolId, tieBreaker);
    }

    @Override
    public void removeUserPick(String userId, String poolId, int regionId, int gameId, int position) {
          userPickRepository.removeUserPick(userId, poolId, regionId, gameId, position);
    }
}
