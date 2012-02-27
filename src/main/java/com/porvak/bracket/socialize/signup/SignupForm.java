package com.porvak.bracket.socialize.signup;

import com.porvak.bracket.domain.User;
import org.springframework.social.connect.UserProfile;

public class SignupForm {

	private String displayName;

	private String email;

	private String confirmEmail;

    private String profileUrl;

	public User createPerson() {
        return new User(displayName, email, profileUrl);
	}

	public static SignupForm fromProviderUser(UserProfile providerUser) {
		SignupForm form = new SignupForm();
        //form.setDisplayName("TODO DISPLAYNAME");
		form.setEmail(providerUser.getEmail());
		form.setConfirmEmail(providerUser.getEmail());
		return form;
	}

    /**
     * The person's email, must be unique.
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * A confirmation of the person's email. Must match.
     */
    public String getConfirmEmail() {
        return confirmEmail;
    }

    public void setConfirmEmail(String confirmEmail) {
        this.confirmEmail = confirmEmail;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

}