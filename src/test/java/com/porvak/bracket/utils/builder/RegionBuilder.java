package com.porvak.bracket.utils.builder;

import com.google.common.collect.Lists;
import com.porvak.bracket.domain.Region;
import com.porvak.bracket.domain.Round;

import java.util.List;

public class RegionBuilder {

    private int id;
    private String name;
    private List<Round> rounds;
    
    public RegionBuilder(){
        defaults();
    }

    private void defaults() {
        id = 1;
        name = "Region 1";
        rounds = Lists.newLinkedList();
    }
    
    public RegionBuilder withId(int id){
        this.id = id;
        return this;
    }
    
    public RegionBuilder withName(String name){
        this.name = name;
        return this;
    }
    
    public RegionBuilder addRound(Round round){
        rounds.add(round);
        return this;
    }
    
    public Region build(){
        Region region = new Region();
        region.setId(id);
        region.setName(name);
        for (Round round : rounds) {
            region.addRound(round);
        }
        return region;
    }
}
