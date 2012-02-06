package com.porvak.bracket.repository.impl;

import com.porvak.bracket.domain.Tournament;
import com.porvak.bracket.repository.TournamentRepository;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Repository
public class TournamentRepositoryImpl implements TournamentRepository {

    ObjectMapper mapper = new ObjectMapper();
    Tournament tournament;

    @PostConstruct
    public void init() throws IOException {
        tournament = mapper.readValue(new ClassPathResource("/data/tournament.json").getInputStream(), Tournament.class);
    }
    
    public Tournament findTournamentById(String id){
        return tournament;
    }
}
