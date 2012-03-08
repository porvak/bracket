package com.porvak.bracket.domain;

public class Team extends AbstractBracket{
    private String id;
    private String name;
    private int seed;
    private int regionId;
    
    public Team(){
    }
    
    public Team(String id, String name){
        this.id = id;
        this.name = name;
    }

    public Team(GameTeam gameTeam, int regionId) {
        this.id = gameTeam.getId();
        this.name = gameTeam.getName();
        this.seed = gameTeam.getSeed();
        this.regionId = regionId;
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

    public int getSeed() {
        return seed;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public void setSeed(int seed) {
        this.seed = seed;

    }
}
