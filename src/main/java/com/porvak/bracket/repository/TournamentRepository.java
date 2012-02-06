package com.porvak.bracket.repository;

import com.porvak.bracket.domain.Tournament;

public interface TournamentRepository {
    Tournament findTournamentById(String id);
}
