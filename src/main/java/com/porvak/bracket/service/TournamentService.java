package com.porvak.bracket.service;

import com.porvak.bracket.domain.Tournament;
import com.porvak.bracket.domain.user.UserTournament;

public interface TournamentService {
    Tournament getTournament(String tournamentId);

    UserTournament getUserTournament(String poolId, String userId);

    void createUserTournament(String poolId, String userId);
}
