package com.porvak.bracket.socialize.account;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Account implements Serializable {
	
	private final String id;
	
	private final String displayName;
	
	private final String twitterName;

    private final String profileUrl;

    public Account(String id, String displayName, String twitterName, String profileUrl) {
        this.id = id;
        this.displayName = displayName;
        this.twitterName = twitterName;
        this.profileUrl = profileUrl;
    }


    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getTwitterName() {
        return twitterName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

	public String getProfileId() {
		return id.toString();
	}
	

	public String toString() {
		return id.toString();
	}
}