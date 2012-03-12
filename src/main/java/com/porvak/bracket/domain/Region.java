package com.porvak.bracket.domain;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

public class Region extends AbstractBracket{
    @JsonProperty("regionId")
    @Field("regionId")
    private int regionId;

    private String name;

//    @JsonProperty
//    @Transient
//    private List<Round> rounds;

    @JsonIgnore
    @Field("rounds")
    private Map<Integer, Round> roundMap;

    public Region(){
        roundMap = Maps.newHashMap();
    }
    
    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public List<Round> getRounds(){
        List<Round> roundList = Lists.newArrayList(roundMap.values());
        Collections.sort(roundList);
        return roundList;
    }

    public Round getRoundById(int id){
        return roundMap.get(id);
    }

    public void addRound(Round round) {
        round = checkNotNull(round, "Round cannot be null");
        checkArgument(round.getRoundId() >= 1, "Round ID must be greater than or equal to 1: Received [%s]", round.getRoundId());
        roundMap.put(round.getRoundId(), round);
    }
}
