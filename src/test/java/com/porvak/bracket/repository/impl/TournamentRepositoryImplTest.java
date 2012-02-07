package com.porvak.bracket.repository.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.porvak.bracket.domain.Game;
import com.porvak.bracket.domain.GameStatus;
import com.porvak.bracket.domain.GameTeam;
import com.porvak.bracket.domain.Region;
import com.porvak.bracket.domain.Round;
import com.porvak.bracket.domain.Team;
import com.porvak.bracket.domain.Tournament;
import com.porvak.bracket.utils.builder.GameBuilder;
import com.porvak.bracket.utils.builder.GameTeamBuilder;
import com.porvak.bracket.utils.builder.RegionBuilder;
import com.porvak.bracket.utils.builder.RoundBuilder;
import com.porvak.bracket.utils.builder.TournamentBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class TournamentRepositoryImplTest {

    TournamentRepositoryImpl tournamentRepository;
    ObjectMapper mapper = new ObjectMapper();
    ListMultimap<Integer, GameTeam> gameTeamData;

    @Before
    public void setUp() throws Exception {
        tournamentRepository = new TournamentRepositoryImpl();
        tournamentRepository.init();
        populateGameTeams();
    }

    @Test
    public void testFindTournamentById() throws Exception {
        Tournament tournament = tournamentRepository.findTournamentById("779d06cc-ebaa-4b10-8c04-79e26c4aa84e");
        String tournamentJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tournament);

        tournament = mapper.readValue(tournamentJson, Tournament.class);
        assertThat(tournament.getRegions(), hasSize(4));
        assertThat(tournament.getRegions().get(0).getRounds(), hasSize(4));
        assertThat(tournament.getRegions().get(0).getRoundById(1).getGames(), hasSize(8));
    }

    private Tournament getTournamentById(String id) {
        return new TournamentBuilder()
                .withId(id)
                .addRegion(getRegionById(1))
                .addRegion(getRegionById(2))
                .addRegion(getRegionById(3))
                .addRegion(getRegionById(4)).build();
    }

    private Region getRegionById(int regionId){
        return new RegionBuilder().withId(regionId).withName(String.format("Region %s", regionId))
                .addRound(getRoundById(1, regionId))
                .addRound(getRoundById(2, regionId))
                .addRound(getRoundById(3, regionId))
                .addRound(getRoundById(4, regionId))
                .build();
    }
    
    private Round getRoundById(int roundId, int regionId){
        switch (roundId){
            case 1:
                return new RoundBuilder()
                        .withRoundId(roundId)
                        .withRoundName(String.format("Round %s", roundId))
                        .addGame(getGameById(1, regionId))
                        .addGame(getGameById(2, regionId))
                        .addGame(getGameById(3, regionId))
                        .addGame(getGameById(4, regionId))
                        .addGame(getGameById(5, regionId))
                        .addGame(getGameById(6, regionId))
                        .addGame(getGameById(7, regionId))
                        .addGame(getGameById(8, regionId)).build();
            case 2:
                return new RoundBuilder()
                        .withRoundId(roundId)
                        .withRoundName(String.format("Round %s", roundId))
                        .addGame(getGameById(9, regionId))
                        .addGame(getGameById(10, regionId))
                        .addGame(getGameById(11, regionId))
                        .addGame(getGameById(12, regionId)).build();
            case 3:
                return new RoundBuilder()
                        .withRoundId(roundId)
                        .withRoundName(String.format("Round %s", roundId))
                        .addGame(getGameById(13, regionId))
                        .addGame(getGameById(14, regionId)).build();
            case 4:
                return new RoundBuilder()
                        .withRoundId(roundId)
                        .withRoundName(String.format("Round %s", roundId))
                        .addGame(getGameById(15, regionId)).build();
        }
        return null;
    }

    /**
     * 
     * @param id base 1. (1-15 are valid)
     * @param region base 1. (1-4 are valid)
     * @return populate game objects
     */
    private Game getGameById(int id, int region){
        int topSeed = 0;
        int bottomSeed = 0;
        switch (id){
            case 1:
                topSeed = 1;
                bottomSeed = 16;
                break;
            case 2:
                topSeed = 8;
                bottomSeed = 9;
                break;
            case 3:
                topSeed = 5;
                bottomSeed = 12;
                break;

            case 4:
                topSeed = 4;
                bottomSeed = 13;
                break;

            case 5:
                topSeed = 6;
                bottomSeed = 11;
                break;

            case 6:
                topSeed = 3;
                bottomSeed = 14;
                break;

            case 7:
                topSeed = 7;
                bottomSeed = 10;
                break;

            case 8:
                topSeed = 2;
                bottomSeed = 15;
                break;

            case 9:
                topSeed = 1;
                bottomSeed = 8;
                break;

            case 10:
                topSeed = 5;
                bottomSeed = 4;
                break;

            case 11:
                topSeed = 4;
                bottomSeed = 6;
                break;

            case 12:
                topSeed = 7;
                bottomSeed = 2;
                break;

            case 13:
                topSeed = 1;
                bottomSeed = 4;
                break;

            case 14:
                topSeed = 3;
                bottomSeed = 2;
                break;

            case 15:
                topSeed = 1;
                bottomSeed = 2;
                break;

        }
        GameTeam topTeam = gameTeamData.get(topSeed).get(region - 1);
        GameTeam bottomTeam = gameTeamData.get(bottomSeed).get(region - 1);
        return new GameBuilder().withGameId(id).withStatus(GameStatus.FUTURE)
                .addTeam(topTeam.getId(), topTeam.getName(), topSeed, 0, 0, false)
                .addTeam(bottomTeam.getId(), bottomTeam.getName(), bottomSeed, 1, 0, false).build();
    }
    
    private void populateGameTeams() throws IOException {
        List<Team> teams = mapper.readValue(new ClassPathResource("/data/teams.json").getInputStream(), new TypeReference<List<Team>>() {});
        Iterator<Team> teamIterator = teams.iterator();
        gameTeamData = ArrayListMultimap.create();

        for (int seedCounter = 1; seedCounter <= 16; seedCounter++){
            Team team = teamIterator.next();
            gameTeamData.put(seedCounter, new GameTeamBuilder().withName(team.getName()).withId(team.getId()).withSeed(seedCounter).build());
            team = teamIterator.next();
            gameTeamData.put(seedCounter, new GameTeamBuilder().withName(team.getName()).withId(team.getId()).withSeed(seedCounter).build());
            team = teamIterator.next();
            gameTeamData.put(seedCounter,new GameTeamBuilder().withName(team.getName()).withId(team.getId()).withSeed(seedCounter).build());
            team = teamIterator.next();
            gameTeamData.put(seedCounter,new GameTeamBuilder().withName(team.getName()).withId(team.getId()).withSeed(seedCounter).build());
        }
    } 
}
