package com.porvak.bracket.repository.impl;

import com.google.common.collect.ListMultimap;
import com.porvak.bracket.domain.Game;
import com.porvak.bracket.domain.GamePointer;
import com.porvak.bracket.domain.GameStatus;
import com.porvak.bracket.domain.GameTeam;
import com.porvak.bracket.domain.Region;
import com.porvak.bracket.domain.Round;
import com.porvak.bracket.domain.Tournament;
import com.porvak.bracket.utils.builder.GameBuilder;
import com.porvak.bracket.utils.builder.RegionBuilder;
import com.porvak.bracket.utils.builder.RoundBuilder;
import com.porvak.bracket.utils.builder.TournamentBuilder;

import static com.porvak.bracket.repository.impl.TournamentMetaData.*;

public class BlankTournamentFactory {

    ListMultimap<Integer, GameTeam> gameTeamData;

    public BlankTournamentFactory(ListMultimap<Integer, GameTeam> gameTeamData) {
        this.gameTeamData = gameTeamData;
    }

    public Tournament getBlankTournamentById(String id) {
        Region region1 = getDefaultRegionById(1);
        Region region2 = getDefaultRegionById(2);
        Region region3 = getDefaultRegionById(3);
        Region region4 = getDefaultRegionById(4);
        Region region5 = getDefaultRegionById(5);

        return new TournamentBuilder()
                .withId(id)
                .addRegion(region1)
                .addRegion(region2)
                .addRegion(region3)
                .addRegion(region4)
                .addRegion(region5).build();
    }


    private Game getUnplayedGame(int gameId, GamePointer nextGame, GamePointer previousGame1, GamePointer previousGame2){
        GameTeam team1 = new GameTeam();
        team1.setPosition(0);
        team1.setPreviousGame(previousGame1);

        GameTeam team2 = new GameTeam();
        team2.setPosition(1);
        team2.setPreviousGame(previousGame2);

        return new GameBuilder().withGameId(gameId).withStatus(GameStatus.FUTURE)
                .withNextGame(nextGame)
                .addTeam(team1)
                .addTeam(team2).build();
    }

    private Round getDefaultRoundById(int roundId, int regionId) {
        //-- Final 4 is region 5
        if(regionId == 5){
            switch (roundId){
                case 1:
                    return new RoundBuilder()
                            .withRoundId(roundId)
                            .withRoundName(String.format("Round %s", roundId))
                            .addGame(getUnplayedGame(1, new GamePointer(regionId, 3, 0), new GamePointer(1, 15, 0), new GamePointer(3, 15, 0)))
                            .addGame(getUnplayedGame(2, new GamePointer(regionId, 3, 1), new GamePointer(2, 15, 0), new GamePointer(4, 15, 0)))
                            .build();
                case 2:
                    return new RoundBuilder()
                            .withRoundId(roundId)
                            .withRoundName(String.format("Round %s", roundId))
                            .addGame(getUnplayedGame(3, new GamePointer(regionId, 4, 1), new GamePointer(regionId, 1, 0), new GamePointer(regionId, 2, 0)))
                            .build();
                case 3:
                    GameTeam gameTeam = new GameTeam();
                    gameTeam.setPosition(1);
                    gameTeam.setPreviousGame(new GamePointer(regionId, 3, 0));
                    return new RoundBuilder()
                            .withRoundId(roundId)
                            .withRoundName(String.format("Round %s", roundId))
                            .addGame(new GameBuilder().withGameId(4).withStatus(GameStatus.FUTURE).addTeam(new GameTeam())
                                    .addTeam(gameTeam).build())
                            .build();
            }
        }

        //-- Regions 1 - 4
        switch (roundId) {
            case 1:
                return new RoundBuilder()
                        .withRoundId(roundId)
                        .withRoundName(String.format("Round %s", roundId))
                        .addGame(getUnplayedGame(1, new GamePointer(regionId, 9, 0), null, null))
                        .addGame(getUnplayedGame(2, new GamePointer(regionId, 9, 1), null, null))
                        .addGame(getUnplayedGame(3, new GamePointer(regionId, 10, 0), null, null))
                        .addGame(getUnplayedGame(4, new GamePointer(regionId, 10, 1), null, null))
                        .addGame(getUnplayedGame(5, new GamePointer(regionId, 11, 0), null, null))
                        .addGame(getUnplayedGame(6, new GamePointer(regionId, 11, 1), null, null))
                        .addGame(getUnplayedGame(7, new GamePointer(regionId, 12, 0), null, null))
                        .addGame(getUnplayedGame(8, new GamePointer(regionId, 12, 1), null, null)).build();
            case 2:
                return new RoundBuilder()
                        .withRoundId(roundId)
                        .withRoundName(String.format("Round %s", roundId))
                        .addGame(getUnplayedGame(9, new GamePointer(regionId, 13, 0), new GamePointer(regionId, 1, 0), new GamePointer(regionId, 2, 0)))
                        .addGame(getUnplayedGame(10, new GamePointer(regionId, 13, 1), new GamePointer(regionId, 3, 0), new GamePointer(regionId, 4, 0)))
                        .addGame(getUnplayedGame(11, new GamePointer(regionId, 14, 0), new GamePointer(regionId, 5, 0), new GamePointer(regionId, 6, 0)))
                        .addGame(getUnplayedGame(12, new GamePointer(regionId, 14, 1), new GamePointer(regionId, 7, 0), new GamePointer(regionId, 8, 0))).build();
            case 3:
                return new RoundBuilder()
                        .withRoundId(roundId)
                        .withRoundName(String.format("Round %s", roundId))
                        .addGame(getUnplayedGame(13, new GamePointer(regionId, 15, 0), new GamePointer(regionId, 9, 0), new GamePointer(regionId, 10, 0)))
                        .addGame(getUnplayedGame(14, new GamePointer(regionId, 15, 1), new GamePointer(regionId, 11, 0), new GamePointer(regionId, 12, 0))).build();
            case 4:
                int gameId = 0;
                int positionId = 0;
                switch (regionId) {
                    case 1:
                        gameId = 1;
                        positionId = 0;
                        break;
                    case 2:
                        gameId = 2;
                        positionId = 0;
                        break;
                    case 3:
                        gameId = 1;
                        positionId = 1;
                        break;
                    case 4:
                        gameId = 2;
                        positionId = 1;

                }
                return new RoundBuilder()
                        .withRoundId(roundId)
                        .withRoundName(String.format("Round %s", roundId))
                        .addGame(getUnplayedGame(15, new GamePointer(5, gameId, positionId), new GamePointer(regionId, 13, 0), new GamePointer(regionId, 14, 0))).build();
        }
        return null;
    }

    private Region getDefaultRegionById(int regionId) {
        if (regionId == 5) {
            Round round1 = getDefaultRoundById(1, regionId);
            Round round2 = getDefaultRoundById(2, regionId);
            Round round3 = getDefaultRoundById(3, regionId);

            return new RegionBuilder().withId(regionId).withName(getRegionName("5"))
                    .addRound(round1)
                    .addRound(round2)
                    .addRound(round3)
                    .build();
        }

        Round round1 = generateFirstRound(1, regionId);
        Round round2 = getDefaultRoundById(2, regionId);
        Round round3 = getDefaultRoundById(3, regionId);
        Round round4 = getDefaultRoundById(4, regionId);
        
        String regionName = "";

        return new RegionBuilder().withId(regionId).withName(getRegionName(Integer.toString(regionId)))
                .addRound(round1)
                .addRound(round2)
                .addRound(round3)
                .addRound(round4)
                .build();
    }

    private Round generateFirstRound(int roundId, int regionId) {
        return new RoundBuilder()
                .withRoundId(roundId)
                .withRoundName(String.format("Round %s", roundId))
                .addGame(getGameById(1, regionId, new GamePointer(regionId, 9, 0)))
                .addGame(getGameById(2, regionId, new GamePointer(regionId, 9, 1)))
                .addGame(getGameById(3, regionId, new GamePointer(regionId, 10, 0)))
                .addGame(getGameById(4, regionId, new GamePointer(regionId, 10, 1)))
                .addGame(getGameById(5, regionId, new GamePointer(regionId, 11, 0)))
                .addGame(getGameById(6, regionId, new GamePointer(regionId, 11, 1)))
                .addGame(getGameById(7, regionId, new GamePointer(regionId, 12, 0)))
                .addGame(getGameById(8, regionId, new GamePointer(regionId, 12, 1))).build();

    }

    /**
     * @param gameId       base 1. (1-15 are valid)
     * @param region       base 1. (1-4 are valid)
     * @param nextGame
     * @return populate game objects
     */
    private Game getGameById(int gameId, int region, GamePointer nextGame) {
        int topSeed = 0;
        int bottomSeed = 0;
        GameTeam topTeam = null;
        GameTeam bottomTeam = null;

        switch (gameId) {
            case 1:
                topSeed = 1;
                bottomSeed = 16;
                topTeam = gameTeamData.get(topSeed).get(region - 1);
                bottomTeam = gameTeamData.get(bottomSeed).get(region - 1);
                break;
            case 2:
                topSeed = 8;
                bottomSeed = 9;
                topTeam = gameTeamData.get(topSeed).get(region - 1);
                bottomTeam = gameTeamData.get(bottomSeed).get(region - 1);
                break;
            case 3:
                topSeed = 5;
                bottomSeed = 12;
                topTeam = gameTeamData.get(topSeed).get(region - 1);
                bottomTeam = gameTeamData.get(bottomSeed).get(region - 1);
                break;

            case 4:
                topSeed = 4;
                bottomSeed = 13;
                topTeam = gameTeamData.get(topSeed).get(region - 1);
                bottomTeam = gameTeamData.get(bottomSeed).get(region - 1);
                break;

            case 5:
                topSeed = 6;
                bottomSeed = 11;
                topTeam = gameTeamData.get(topSeed).get(region - 1);
                bottomTeam = gameTeamData.get(bottomSeed).get(region - 1);
                break;

            case 6:
                topSeed = 3;
                bottomSeed = 14;
                topTeam = gameTeamData.get(topSeed).get(region - 1);
                bottomTeam = gameTeamData.get(bottomSeed).get(region - 1);
                break;

            case 7:
                topSeed = 7;
                bottomSeed = 10;
                topTeam = gameTeamData.get(topSeed).get(region - 1);
                bottomTeam = gameTeamData.get(bottomSeed).get(region - 1);
                break;

            case 8:
                topSeed = 2;
                bottomSeed = 15;
                topTeam = gameTeamData.get(topSeed).get(region - 1);
                bottomTeam = gameTeamData.get(bottomSeed).get(region - 1);
                break;
        }

        return new GameBuilder().withGameId(gameId).withStatus(GameStatus.FUTURE)
                .withNextGame(nextGame)
                .addTeam(topTeam.getId(), topTeam.getName(), topSeed, 0, 0, false, null)
                .addTeam(bottomTeam.getId(), bottomTeam.getName(), bottomSeed, 1, 0, false, null).build();
    }


}
