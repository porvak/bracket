package com.porvak.bracket.utils.builder;

import com.google.common.collect.Maps;
import com.porvak.bracket.domain.Region;
import com.porvak.bracket.domain.Tournament;
import com.porvak.bracket.domain.TournamentType;

import java.util.Map;

public class TournamentBuilder {
    private String id;
    private String name;
    private TournamentType type;
    private Map<Integer, Region> regions;
    
    public TournamentBuilder(){
        defaults();
    }

    private void defaults() {
        id = "1";
        name = "2012 Men's Basketball Championship";
        type = TournamentType.NCAA;
        regions = Maps.newHashMap();
    }
    
    public TournamentBuilder withId(String id){
        this.id = id;
        return this;
    }
    
    public TournamentBuilder withName(String name){
        this.name = name;
        return this;
    }

    public TournamentBuilder withType(TournamentType type){
        this.type = type;
        return this;
    }

    public TournamentBuilder addRegion(Region region){
        regions.put(region.getRegionId(), region);
        return this;
    }
    
    public Tournament build(){
        Tournament tournament = new Tournament();
        tournament.setId(id);
        tournament.setName(name);
        tournament.setType(type);
        tournament.setRegionMap(regions);
        return tournament;
    }
}
