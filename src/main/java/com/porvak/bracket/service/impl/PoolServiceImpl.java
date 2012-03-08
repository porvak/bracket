package com.porvak.bracket.service.impl;

import com.porvak.bracket.domain.UserPick;
import com.porvak.bracket.repository.PoolRepository;
import com.porvak.bracket.repository.UserPickRepository;
import com.porvak.bracket.service.PoolService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.inject.Inject;

@Service
public class PoolServiceImpl implements PoolService {

    @Inject
    private UserPickRepository userPickRepository;

    @Inject
    private PoolRepository poolRepository;

    @Override
    public void addUserPick(String userId, String poolId, UserPick userPick){
        Assert.noNullElements(new Object[]{userId, poolId, userPick}, String.format("No null elements allowed for " +
                "userId:[%s], poolId:[%s], userPick:[%s]", userId, poolId, userPick));
//TODO: FIX THIS
        userPickRepository.updateUserPick(userId, poolId, userPick);
    }

    @Override
    public void addTieBreaker(String userId, String poolId, int tieBreaker) {
        userPickRepository.addTieBreaker(userId, poolId, tieBreaker);
    }
}
