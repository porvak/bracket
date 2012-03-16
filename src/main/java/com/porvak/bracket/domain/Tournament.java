package com.porvak.bracket.domain;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class Tournament extends AbstractBracket{
    private String id;
    private String name;
    private TournamentType type;

    @JsonIgnore
    @Field("regions")
    private Map<Integer, Region> regionMap;

    private Status pickStatus;
    
    @PersistenceConstructor
    public Tournament(){

    }

    public Status getPickStatus() {
        return pickStatus;
    }

    public void setPickStatus(Status pickStatus) {
        this.pickStatus = pickStatus;
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

    public Map<Integer, Region> getRegionMap() {
        return regionMap;
    }

    public void setRegionMap(Map<Integer, Region> regionMap) {
        this.regionMap = regionMap;
    }

    public List<Region> getRegions() {
        return Lists.newArrayList(regionMap.values());
    }
    
    public void setRegions(List<Region> regions){
        this.regionMap = Maps.uniqueIndex(regions, new Function<Region, Integer>() {
            @Override
            public Integer apply(@Nullable Region input) {
                if(input == null){
                    return null;
                }
                return input.getRegionId();
            }
        });
    }

}
