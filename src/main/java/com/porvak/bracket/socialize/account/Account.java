package com.porvak.bracket.socialize.account;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Account implements Serializable {
	
	private final String id;
	
	private final String displayName;
	
	private final String email;

    private final String profileUrl;

    public Account(String id, String displayName, String email, String profileUrl) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.profileUrl = profileUrl;
    }


    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
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