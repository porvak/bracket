package com.porvak.bracket.socialize.signup;

import com.porvak.bracket.domain.User;
import com.porvak.bracket.socialize.account.Account;
import com.porvak.bracket.socialize.account.AccountRepository;
import com.porvak.bracket.socialize.account.AccountUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;


public class SignupHelper {

	private final AccountRepository accountRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(SignupHelper.class);

	public SignupHelper(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public boolean signup(SignupForm form, BindingResult formBinding) {
		return signup(form, formBinding, null);
	}

	public boolean signup(SignupForm form, BindingResult formBinding, SignupCallback callback) {
		try {
			Account account = createAccount(form.createPerson(), callback);
			//gateway.signedUp(account);
			AccountUtils.signin(account);
			if (callback != null) {
				callback.postSignup(account);
			}
			return true;
		} catch (Exception e) {
			if(formBinding == null){
                LOGGER.debug("There was an error signing up", e);
                throw new RuntimeException("There was an error. Please try again.");
            }
            formBinding.rejectValue("email", "account.duplicateEmail", "already on file");
			return false;
		}		
	}

	// internal helpers

	private Account createAccount(User person, SignupCallback callback)  {
		return accountRepository.createAccount(person);
	}

	public interface SignupCallback {
		
		void postSignup(Account account);
		
	}

}