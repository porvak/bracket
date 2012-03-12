package com.porvak.bracket.domain;

public class User extends AbstractBracket {
    private String id;
    private String displayName;
    private String twitterName;
    private String profileUrl;

    public User(){
    }

    public User(String id, String displayName, String twitterName, String profileUrl) {
        this.id = id;
        this.displayName = displayName;
        this.twitterName = twitterName;
        this.profileUrl = profileUrl;
    }


    public User(String displayName, String twitterName, String profileUrl) {
        this.displayName = displayName;
        this.twitterName = twitterName;
        this.profileUrl = profileUrl;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTwitterName() {
        return twitterName;
    }

    public void setTwitterName(String twitterName) {
        this.twitterName = twitterName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

}
