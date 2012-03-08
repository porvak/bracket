package com.porvak.bracket.repository.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.porvak.bracket.config.ComponentConfig;
import com.porvak.bracket.config.DataConfig;
import com.porvak.bracket.domain.BracketConstants;
import com.porvak.bracket.domain.Game;
import com.porvak.bracket.domain.GamePointer;
import com.porvak.bracket.domain.GameStatus;
import com.porvak.bracket.domain.GameTeam;
import com.porvak.bracket.domain.Region;
import com.porvak.bracket.domain.Round;
import com.porvak.bracket.domain.Status;
import com.porvak.bracket.domain.Team;
import com.porvak.bracket.domain.Tournament;
import com.porvak.bracket.domain.User;
import com.porvak.bracket.domain.UserPicks;
import com.porvak.bracket.domain.user.UserTournament;
import com.porvak.bracket.repository.PoolRepository;
import com.porvak.bracket.repository.TeamRepository;
import com.porvak.bracket.repository.TournamentRepository;
import com.porvak.bracket.repository.UserPickRepository;
import com.porvak.bracket.repository.UserPicksRepository;
import com.porvak.bracket.repository.UserTournamentRepository;
import com.porvak.bracket.socialize.account.Account;
import com.porvak.bracket.socialize.account.MongoAccountRepository;
import com.porvak.bracket.utils.builder.GameBuilder;
import com.porvak.bracket.utils.builder.GameTeamBuilder;
import com.porvak.bracket.utils.builder.RegionBuilder;
import com.porvak.bracket.utils.builder.RoundBuilder;
import com.porvak.bracket.utils.builder.TournamentBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.google.common.base.Preconditions.*;
import static com.porvak.bracket.domain.BracketConstants.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DataConfig.class, ComponentConfig.class})
public class TournamentRepositoryImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TournamentRepositoryImplTest.class);
    private static final String BLANK_TOURNAMENT_ID = BracketConstants.TOURNAMENT_ID;

    @Inject
    PoolRepository poolRepository;

    @Inject
    UserPicksRepository userPicksRepository;

    @Inject
    TournamentRepository tournamentRepository;

    @Inject
    TeamRepository teamRepository;

    @Inject
    UserPickRepository userPickRepository;

    @Inject
    MongoAccountRepository userRepository;

    @Inject
    UserTournamentRepository userTournamentRepository;

    ObjectMapper mapper = new ObjectMapper();
    ListMultimap<Integer, GameTeam> gameTeamData;

    @Before
    public void setUp() throws Exception {
        populateGameTeams();
    }

    @Test @Ignore
    public void testUserPicks() throws IOException {
        String poolId = "4f3c8297a0eea26b78d77538";
        String userId = "2";
        String teamId = "9999411";
//TODO: FIX THIS
//        UserPick userPick = new UserPick(2, 10, teamId);

//        userPickRepository.updateUserPick(userId, poolId, userPick);
        for(UserPicks userPicksDataSet: userPicksRepository.findAll()){
            LOGGER.debug("{}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userPicksDataSet));
        }
    }
    
    @Test @Ignore
    public void getUserPicks() throws IOException {
        for(UserPicks userPicksDataSet: userPicksRepository.findAll()){
            String userPickJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userPicksDataSet);
            Files.write(userPickJson.getBytes(),
                    new File(String.format("/opt/code/github/bracket/src/test/resources/data/userPick_%s.json", userPicksDataSet.getUserId())));
            LOGGER.debug("{}", userPickJson);
        }
    }
    
    @Test @Ignore
    public void generateUserTournament() throws IOException {
        Tournament tournament = tournamentRepository.findOne(BLANK_TOURNAMENT_ID);
        
        for(int i = 1; i < 2; i++){
            Account account = userRepository.createAccount(new User(RandomStringUtils.randomAlphabetic(8), "me@nospam.com", "http://nowhere.com"));
            UserTournament userTournament = new UserTournament(tournament);
            userTournament.setUserId(account.getId());
            userTournament.setPoolId(POOL_ID);
            userTournamentRepository.save(userTournament);

            for(Region region: userTournament.getRegions()){
                Round round1 = region.getRoundById(1);
                Round round2 = region.getRoundById(2);
                Round round3 = region.getRoundById(3);
                Round round4 = region.getRoundById(4);

                Map<Integer, Team> gameWinner = Maps.newHashMap();
                Map<Integer, Team> regionWinner = Maps.newHashMap();

                Game game = round1.findByGameId(1);
                Team winner = getGameWinner(game, region.getRegionId());
//                game.setUserGameWinner(winner);
//                userTournament.addUserPick(winner, region.getRegionId());
            }
            File userFile = new File(String.format("/opt/code/github/bracket/src/test/resources/data/userTouranment_%s.json", userTournament.getTournamentId()));
            mapper.writerWithDefaultPrettyPrinter().writeValue(userFile, userTournament);
        }
        
        
    }

    private Team getGameWinner(Game game, int regionId) {
        Pair<Integer, Integer> score = generateScore();
        int topScore = score.getLeft();
        int bottomScore =  score.getRight();

        boolean topTeamWins = topScore > bottomScore;

        if(topTeamWins){
            return new Team(game.getTeamByPosition(0), regionId);
        }
        else {
            return new Team(game.getTeamByPosition(1), regionId);
        }
    }

    @Test @Ignore
    public void testFindTournamentById() throws Exception {
// Completed Tournament
//        Tournament tournament = tournamentRepository.findOne("4f349bc6d170640b9c895a47");
// Default Tournament only 1st round populated
//        Tournament tournament = tournamentRepository.findOne(BLANK_TOURNAMENT_ID);
//        Generate new tournament via code
        Tournament tournament = new BlankTournamentFactory(gameTeamData).getBlankTournamentById(BLANK_TOURNAMENT_ID);
        tournament = tournamentRepository.save(tournament);
        tournament.setPickStatus(Status.OPEN);
        String tournamentJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tournament);
        LOGGER.debug(tournamentJson);
        tournament = mapper.readValue(tournamentJson, Tournament.class);
        Files.write(tournamentJson.getBytes(), new File(
                String.format("/opt/code/github/bracket/src/test/resources/data/tournament_%s.json", tournament.getId())));
//        assertThat(tournament.getRegions(), hasSize(5));
//        assertThat(tournament.getRegions().get(0).getRounds(), hasSize(4));
//        assertThat(tournament.getRegions().get(0).getRoundById(1).getGames(), hasSize(8));
    }

    @Test @Ignore
    public void populateTeamCollection() throws IOException {
        List<Team> teamList = getTeams();
        for (Team team : teamList) {
            teamRepository.save(new Team(null, team.getName()));
        }

        teamList = teamRepository.findAll();
        mapper.writeValue(new File("/opt/code/github/bracket/src/main/resources/data/teams.json"), teamList);
    }

    @Test @Ignore
    public void populateTeamCollectionWithSeeds() throws IOException {
        Tournament tournament = tournamentRepository.findOne(BLANK_TOURNAMENT_ID);
        for(Region region: tournament.getRegions()){
            int regionId = region.getRegionId();
            if(regionId != 5){
                Round round1 = region.getRoundById(1);
                for(Game game: round1.getGames()){
                    GameTeam gameTeam0 = game.getTeamByPosition(0);
                    GameTeam gameTeam1 = game.getTeamByPosition(1);
                    Team team = teamRepository.findOne(gameTeam0.getId());
                    team.setSeed(gameTeam0.getSeed());
                    team.setRegionId(regionId);
                    teamRepository.save(team);

                    team = teamRepository.findOne(gameTeam1.getId());
                    team.setSeed(gameTeam1.getSeed());
                    team.setRegionId(regionId);
                    teamRepository.save(team);
                }
            }
        }

        List<Team> teamList = teamRepository.findAll();
        mapper.writeValue(new File("/opt/code/github/bracket/src/test/resources/data/teamList.json"), teamList);
    }

    private Tournament getTournamentById(String id) {
        Region region1 = getRegionById(1);
        Region region2 = getRegionById(2);
        Region region3 = getRegionById(3);
        Region region4 = getRegionById(4);
        
        return new TournamentBuilder()
                .withId(id)
                .addRegion(region1)
                .addRegion(region2)
                .addRegion(region3)
                .addRegion(region4).build();
    }

    private Region getRegionById(int regionId) {
        Round round1 = getRoundById(1, regionId, null);
        Round round2 = getRoundById(2, regionId, round1);
        Round round3 = getRoundById(3, regionId, round2);
        Round round4 = getRoundById(4, regionId, round3);
        
        return new RegionBuilder().withId(regionId).withName(String.format("Region %s", regionId))
                .addRound(round1)
                .addRound(round2)
                .addRound(round3)
                .addRound(round4)
                .build();
    }

    private Round getRoundById(int roundId, int regionId, Round previousRound) {
        switch (roundId) {
            case 1:
                return new RoundBuilder()
                        .withRoundId(roundId)
                        .withRoundName(String.format("Round %s", roundId))
                        .addGame(getGameById(1, regionId, new GamePointer(regionId, 9, 0), previousRound))
                        .addGame(getGameById(2, regionId, new GamePointer(regionId, 9, 1), previousRound))
                        .addGame(getGameById(3, regionId, new GamePointer(regionId, 10, 0), previousRound))
                        .addGame(getGameById(4, regionId, new GamePointer(regionId, 10, 1), previousRound))
                        .addGame(getGameById(5, regionId, new GamePointer(regionId, 11, 0), previousRound))
                        .addGame(getGameById(6, regionId, new GamePointer(regionId, 11, 1), previousRound))
                        .addGame(getGameById(7, regionId, new GamePointer(regionId, 12, 0), previousRound))
                        .addGame(getGameById(8, regionId, new GamePointer(regionId, 12, 1), previousRound)).build();
            case 2:
                return new RoundBuilder()
                        .withRoundId(roundId)
                        .withRoundName(String.format("Round %s", roundId))
                        .addGame(getGameById(9, regionId, new GamePointer(regionId, 13, 0), previousRound))
                        .addGame(getGameById(10, regionId, new GamePointer(regionId, 13, 1), previousRound))
                        .addGame(getGameById(11, regionId, new GamePointer(regionId, 14, 0), previousRound))
                        .addGame(getGameById(12, regionId, new GamePointer(regionId, 14, 1), previousRound)).build();
            case 3:
                return new RoundBuilder()
                        .withRoundId(roundId)
                        .withRoundName(String.format("Round %s", roundId))
                        .addGame(getGameById(13, regionId, new GamePointer(regionId, 15, 0), previousRound))
                        .addGame(getGameById(14, regionId, new GamePointer(regionId, 15, 1), previousRound)).build();
            case 4:
                int gameId = 0;
                int positionId = 0;
                switch (regionId) {
                    case 1:
                        gameId = 1;
                        positionId = 0;
                        break;
                    case 2:
                        gameId = 1;
                        positionId = 1;
                        break;
                    case 3:
                        gameId = 2;
                        positionId = 0;
                        break;
                    case 4:
                        gameId = 2;
                        positionId = 1;

                }
                return new RoundBuilder()
                        .withRoundId(roundId)
                        .withRoundName(String.format("Round %s", roundId))
                        .addGame(getGameById(15, regionId, new GamePointer(5, gameId, positionId), previousRound)).build();
            case 5:
                checkArgument(regionId == 5 && roundId < 2, "region %s can not have more than 2 rounds. round [%s]", regionId, roundId);
                return null;
        }
        return null;
    }

    /**
     * @param gameId       base 1. (1-15 are valid)
     * @param region       base 1. (1-4 are valid)
     * @param nextGame
     * @return populate game objects
     */
    private Game getGameById(int gameId, int region, GamePointer nextGame, Round previousRound) {
        int topSeed = 0;
        int bottomSeed = 0;
        GamePointer previousGamePointerTop = null;
        GamePointer previousGamePointerBottom = null;
        Game previousGameTop = null;
        Game previousGameBottom = null;
        GameTeam winningTeamTop = null;
        GameTeam winningTeamBottom = null;
        GameTeam topTeam = null; //gameTeamData.get(topSeed).get(region - 1);
        GameTeam bottomTeam = null; //gameTeamData.get(bottomSeed).get(region - 1);

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

            case 9:
                checkNotNull(previousRound, "Previous round can not be null.");
                previousGameTop = previousRound.findGameById(1);
                previousGameBottom = previousRound.findGameById(2);
                winningTeamTop = previousGameTop.getWinningTeam();
                winningTeamBottom = previousGameBottom.getWinningTeam();

                if(winningTeamTop != null){
                    previousGamePointerTop = new GamePointer(region, 1, winningTeamTop.getPosition());
                    topSeed = winningTeamTop.getSeed();
                    topTeam = gameTeamData.get(topSeed).get(region - 1);
                }

                if(winningTeamBottom != null){
                    previousGamePointerBottom = new GamePointer(region, 2, winningTeamBottom.getPosition());
                    bottomSeed = winningTeamBottom.getSeed();
                    bottomTeam = gameTeamData.get(bottomSeed).get(region - 1);
                }
                break;

            case 10:
                checkNotNull(previousRound, "Previous round can not be null.");
                previousGameTop = previousRound.findGameById(3);
                previousGameBottom = previousRound.findGameById(4);
                   winningTeamTop = previousGameTop.getWinningTeam();
                winningTeamBottom = previousGameBottom.getWinningTeam();

                if(winningTeamTop != null){
                    previousGamePointerTop = new GamePointer(region, 3, winningTeamTop.getPosition());
                    topSeed = winningTeamTop.getSeed();
                    topTeam = gameTeamData.get(topSeed).get(region - 1);
                }

                if(winningTeamBottom != null){
                    previousGamePointerBottom = new GamePointer(region, 4, winningTeamBottom.getPosition());
                    bottomSeed = winningTeamBottom.getSeed();
                    bottomTeam = gameTeamData.get(bottomSeed).get(region - 1);
                }
                break;

            case 11:
                checkNotNull(previousRound, "Previous round can not be null.");
                previousGameTop = previousRound.findGameById(5);
                previousGameBottom = previousRound.findGameById(6);
                winningTeamTop = previousGameTop.getWinningTeam();
                winningTeamBottom = previousGameBottom.getWinningTeam();

                if(winningTeamTop != null){
                    previousGamePointerTop = new GamePointer(region, 5, winningTeamTop.getPosition());
                    topSeed = winningTeamTop.getSeed();
                    topTeam = gameTeamData.get(topSeed).get(region - 1);
                }

                if(winningTeamBottom != null){
                    previousGamePointerBottom = new GamePointer(region, 6, winningTeamBottom.getPosition());
                    bottomSeed = winningTeamBottom.getSeed();
                    bottomTeam = gameTeamData.get(bottomSeed).get(region - 1);
                }
                break;

            case 12:
                checkNotNull(previousRound, "Previous round can not be null.");
                previousGameTop = previousRound.findGameById(7);
                previousGameBottom = previousRound.findGameById(8);
                winningTeamTop = previousGameTop.getWinningTeam();
                winningTeamBottom = previousGameBottom.getWinningTeam();

                if(winningTeamTop != null){
                    previousGamePointerTop = new GamePointer(region, 7, winningTeamTop.getPosition());
                    topSeed = winningTeamTop.getSeed();
                    topTeam = gameTeamData.get(topSeed).get(region - 1);
                }

                if(winningTeamBottom != null){
                    previousGamePointerBottom = new GamePointer(region, 8, winningTeamBottom.getPosition());
                    bottomSeed = winningTeamBottom.getSeed();
                    bottomTeam = gameTeamData.get(bottomSeed).get(region - 1);
                }
                break;

            case 13:
                checkNotNull(previousRound, "Previous round can not be null.");
                previousGameTop = previousRound.findGameById(9);
                previousGameBottom = previousRound.findGameById(10);
                winningTeamTop = previousGameTop.getWinningTeam();
                winningTeamBottom = previousGameBottom.getWinningTeam();

                if(winningTeamTop != null){
                    previousGamePointerTop = new GamePointer(region, 9, winningTeamTop.getPosition());
                    topSeed = winningTeamTop.getSeed();
                    topTeam = gameTeamData.get(topSeed).get(region - 1);
                }

                if(winningTeamBottom != null){
                    previousGamePointerBottom = new GamePointer(region, 10, winningTeamBottom.getPosition());
                    bottomSeed = winningTeamBottom.getSeed();
                    bottomTeam = gameTeamData.get(bottomSeed).get(region - 1);
                }
                break;

            case 14:
                checkNotNull(previousRound, "Previous round can not be null.");
                previousGameTop = previousRound.findGameById(11);
                previousGameBottom = previousRound.findGameById(12);
                winningTeamTop = previousGameTop.getWinningTeam();
                winningTeamBottom = previousGameBottom.getWinningTeam();

                if(winningTeamTop != null){
                    previousGamePointerTop = new GamePointer(region, 11, winningTeamTop.getPosition());
                    topSeed = winningTeamTop.getSeed();
                    topTeam = gameTeamData.get(topSeed).get(region - 1);
                }

                if(winningTeamBottom != null){
                    previousGamePointerBottom = new GamePointer(region, 12, winningTeamBottom.getPosition());
                    bottomSeed = winningTeamBottom.getSeed();
                    bottomTeam = gameTeamData.get(bottomSeed).get(region - 1);
                }
                break;

            case 15:
                checkNotNull(previousRound, "Previous round can not be null.");
                previousGameTop = previousRound.findGameById(13);
                previousGameBottom = previousRound.findGameById(14);
                winningTeamTop = previousGameTop.getWinningTeam();
                winningTeamBottom = previousGameBottom.getWinningTeam();

                if(winningTeamTop != null){
                    previousGamePointerTop = new GamePointer(region, 13, winningTeamTop.getPosition());
                    topSeed = winningTeamTop.getSeed();
                    topTeam = gameTeamData.get(topSeed).get(region - 1);
                }

                if(winningTeamBottom != null){
                    previousGamePointerBottom = new GamePointer(region, 14, winningTeamBottom.getPosition());
                    bottomSeed = winningTeamBottom.getSeed();
                    bottomTeam = gameTeamData.get(bottomSeed).get(region - 1);
                }
                break;

        }

        Pair<Integer, Integer> score = generateScore();
        int topScore = score.getLeft();
        int bottomScore =  score.getRight();

        boolean topTeamWins = topScore > bottomScore;
        boolean bottomTeamWins = topScore < bottomScore;

        return new GameBuilder().withGameId(gameId).withStatus(GameStatus.COMPLETE)
                .withNextGame(nextGame)
                .addTeam(topTeam.getId(), topTeam.getName(), topSeed, 0, topScore, topTeamWins, previousGamePointerTop)
                .addTeam(bottomTeam.getId(), bottomTeam.getName(), bottomSeed, 1, bottomScore, bottomTeamWins, previousGamePointerBottom).build();
    }

    /**
     * Returns a randomly generated score
     * <ul>
     *     <li>left = top Score</li>
     *     <li>right = bottom Score</li>
     * </ul>
     * @return 
     */
    private Pair<Integer, Integer> generateScore(){
        Random random = new Random();
        int topScore = random.nextInt(120);
        int bottomScore = random.nextInt(120);

        if (topScore == bottomScore) {
            bottomScore = random.nextInt(120);
        }
        
        return Pair.of(topScore, bottomScore);
    }

    public List<Team> getTeams() {
//        return teamRepository.findAll();
        try {
            return mapper.readValue(new ClassPathResource("/data/teams.json").getInputStream(), new TypeReference<List<Team>>() {});
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to load /data/teams.json from classpath.", e);
        }
    }

    private void populateGameTeams() {
        List<Team> teams = getTeams();
        Iterator<Team> teamIterator = teams.iterator();
        gameTeamData = ArrayListMultimap.create();

        for (int seedCounter = 1; seedCounter <= 16; seedCounter++) {
            Team team = teamIterator.next();
            gameTeamData.put(seedCounter, new GameTeamBuilder().withName(team.getName()).withId(team.getId()).withSeed(seedCounter).build());
            team = teamIterator.next();
            gameTeamData.put(seedCounter, new GameTeamBuilder().withName(team.getName()).withId(team.getId()).withSeed(seedCounter).build());
            team = teamIterator.next();
            gameTeamData.put(seedCounter, new GameTeamBuilder().withName(team.getName()).withId(team.getId()).withSeed(seedCounter).build());
            team = teamIterator.next();
            gameTeamData.put(seedCounter, new GameTeamBuilder().withName(team.getName()).withId(team.getId()).withSeed(seedCounter).build());
        }
    }
}
