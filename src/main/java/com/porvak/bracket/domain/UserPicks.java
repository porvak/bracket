package com.porvak.bracket.domain;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

import java.util.List;
import java.util.Map;

@CompoundIndexes({
    @CompoundIndex(name = "userPicks_pool_id_idx", unique = true, def = "{'userId': 1, 'poolId': 1}")
})
public class UserPicks extends AbstractBracket{

    private String id;
    private String tournamentId;
    private String poolId;
    private String userId;

    @JsonProperty
    private List<UserPick> picks;

    @Transient
    private Map<SearchKey, UserPick> indexedPicks;

    public UserPicks() {
        picks = Lists.newArrayList();
    }
    
    public void addUserPick(UserPick pick){
        picks.add(pick);
        if(!arePicksIndexed()){
            createIndexedPicks();
        }
        indexedPicks.put(new SearchKey(pick.getRegionId(), pick.getGameId()), pick);
    }
    
    private boolean arePicksIndexed(){
        return picks == null || (indexedPicks != null && indexedPicks.size() != 0);
    } 
    
    private void createIndexedPicks(){
        indexedPicks = Maps.newHashMap();
        if(picks != null && picks.size() > 0){
            for (UserPick pick : picks) {
                indexedPicks.put(new SearchKey(pick.getRegionId(), pick.getGameId()), pick);
            }
        }
    }
    
    public UserPick getByRegionIdAndGameId(int regionId, int gameId){
        if(!arePicksIndexed()){
            createIndexedPicks();
        }
        return indexedPicks.get(new SearchKey(regionId, gameId));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean hasUserPicks() {
        return picks == null || picks.size() != 0;
    }

    private class SearchKey {
        private int regionId;
        private int gameId;

        public SearchKey(int regionId, int gameId) {
            this.regionId = regionId;
            this.gameId = gameId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o instanceof SearchKey){
                final SearchKey searchKey = (SearchKey) o;
                return Objects.equal(gameId, searchKey.gameId)
                        && Objects.equal(regionId, searchKey.regionId);
            }
            
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(regionId, gameId);
        }
    }
}
