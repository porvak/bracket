package com.porvak.bracket.domain;

public class Team extends AbstractBracket{
    private String id;
    private String name;
    
    public Team(){
    }
    
    public Team(String id, String name){
        this.id = id;
        this.name = name;
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
}
