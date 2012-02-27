package com.porvak.bracket.domain;

public class User extends AbstractBracket {
    private String id;
    private String displayName;
    private String email;
    private String profileUrl;

    public User(){
    }

    public User(String id, String displayName, String email, String profileUrl) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.profileUrl = profileUrl;
    }


    public User(String displayName, String email, String profileUrl) {
        this.displayName = displayName;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

}
