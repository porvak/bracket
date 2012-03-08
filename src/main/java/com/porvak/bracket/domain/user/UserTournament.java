package com.porvak.bracket.domain.user;

import com.porvak.bracket.domain.AbstractBracket;
import com.porvak.bracket.domain.GameTeam;
import com.porvak.bracket.domain.Region;
import com.porvak.bracket.domain.Status;
import com.porvak.bracket.domain.Tournament;
import com.porvak.bracket.domain.TournamentType;
import com.porvak.bracket.domain.UserPick;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

import java.util.List;

@CompoundIndexes({
   @CompoundIndex(name = "user_tourn_pool_id_idx", unique = true, def = "{'userId': 1, 'poolId': 1, 'tournamentId': 1}"),
   @CompoundIndex(name = "user_pool_id_idx", unique = true, def = "{'userId': 1, 'poolId': 1}")
})
public class UserTournament extends AbstractBracket {
    private String id;
    private String name;
    private TournamentType type;
    private List<Region> regions;
    private Status pickStatus;
    private String tournamentId;
    private String poolId;
    private String userId;
    private Integer tieBreaker;

    @PersistenceConstructor
    public UserTournament(){
    }

    public UserTournament(Tournament tournament){
        this.tournamentId = tournament.getId();
        this.name = tournament.getName();
        this.type = tournament.getType();
        this.regions = tournament.getRegions();
        this.pickStatus = tournament.getPickStatus();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TournamentType getType() {
        return type;
    }

    public void setType(TournamentType type) {
        this.type = type;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public Status getPickStatus() {
        return pickStatus;
    }

    public void setPickStatus(Status pickStatus) {
        this.pickStatus = pickStatus;
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(String tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public GameTeam findTeamByUserPick(UserPick userPick) {
        return null;
    }

    public Integer getTieBreaker() {
        return tieBreaker;
    }

    public void setTieBreaker(Integer tieBreaker) {
        this.tieBreaker = tieBreaker;
    }
}
