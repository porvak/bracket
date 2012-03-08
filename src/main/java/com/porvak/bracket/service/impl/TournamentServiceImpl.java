package com.porvak.bracket.service.impl;

import com.porvak.bracket.domain.Tournament;
import com.porvak.bracket.domain.user.UserTournament;
import com.porvak.bracket.repository.TournamentRepository;
import com.porvak.bracket.repository.UserTournamentRepository;
import com.porvak.bracket.service.TournamentService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.*;

@Service
public class TournamentServiceImpl implements TournamentService {

    @Inject
    private TournamentRepository tournamentRepository;

    @Inject
    private UserTournamentRepository userTournamentRepository;

    
    @Override
    public Tournament getTournament(String tournamentId){
        checkNotNull(tournamentId, "Tournament ID can not be null.");
        return tournamentRepository.findOne(tournamentId);
    }
    
    @Override
    public UserTournament getUserTournament(String poolId, String userId){
        checkNotNull(poolId, "Pool Id cannot be null");
        checkNotNull(userId, "User Id cannot be null");
        return userTournamentRepository.findByUserIdAndPoolId(userId, poolId);
    }
}
