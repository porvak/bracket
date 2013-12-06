package com.porvak.bracket.socialize.signup;

import com.porvak.bracket.socialize.account.Account;
import com.porvak.bracket.socialize.account.AccountRepository;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import javax.inject.Inject;

@Controller
public class SignupController {

	private final SignupHelper signupHelper;

    @Inject
    public SignupController(AccountRepository accountRepository) {
        this.signupHelper = new SignupHelper(accountRepository);
    }

	@RequestMapping(value="/signup", method= RequestMethod.GET)
	public String signupForm(final WebRequest request) {
		Connection<?> connection = ProviderSignInUtils.getConnection(request);

// REDIRECT TO HOME PAGE IF sign-in was good.
		if (connection != null) {
			request.setAttribute("message", "Your twitter account is not connected yet. Please signup", WebRequest.SCOPE_REQUEST);
            UserProfile userProfile = connection.fetchUserProfile();
            SignupForm form = SignupForm.fromProviderUser(userProfile);
            form.setProfileUrl(connection.getImageUrl());
            boolean result = signupHelper.signup(form, null, new SignupHelper.SignupCallback() {
                public void postSignup(Account account) {
                    ProviderSignInUtils.handlePostSignUp(account.getId().toString(), request);
                }
            });
            return result ? "redirect:/" : null;
		}

        return null;
	}

	@RequestMapping(value="/signup", method= RequestMethod.POST)
	public String signup(SignupForm form, BindingResult formBinding, final WebRequest request) {
		if (formBinding.hasErrors()) {
			return null;
		}
		boolean result = signupHelper.signup(form, formBinding, new SignupHelper.SignupCallback() {
			public void postSignup(Account account) {
				ProviderSignInUtils.handlePostSignUp(account.getId().toString(), request);
			}
		});
		return result ? "redirect:/" : null;
	}
}