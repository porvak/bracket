package com.porvak.bracket.utils.builder;

import com.google.common.collect.Lists;
import com.porvak.bracket.domain.Region;
import com.porvak.bracket.domain.Tournament;
import com.porvak.bracket.domain.TournamentType;

import java.util.List;

public class TournamentBuilder {
    private String id;
    private String name;
    private TournamentType type;
    private List<Region> regions;
    
    public TournamentBuilder(){
        defaults();
    }

    private void defaults() {
        id = "1";
        name = "ncaa tournament";
        type = TournamentType.NCAA;
        regions = Lists.newLinkedList();
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
        regions.add(region);
        return this;
    }
    
    public Tournament build(){
        Tournament tournament = new Tournament();
        tournament.setId(id);
        tournament.setName(name);
        tournament.setType(type);
        tournament.setRegions(regions);
        return tournament;
    }
}
