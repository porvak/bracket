/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.porvak.bracket.social.develop.oauth;

import com.porvak.bracket.social.develop.InvalidApiKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;


/**
 * The UI Controller that shows the application authorization form to the member.
 * @author Keith Donald
 */
@Controller
public class ConfirmAccessController {

	private final OAuthSessionManager sessionManager;
	
	//private final AppRepository appRepository;

//	@Inject
//	public ConfirmAccessController(OAuthSessionManager sessionManager, AppRepository appRepository) {
//		this.sessionManager = sessionManager;
//		this.appRepository = appRepository;
//	}
    @Inject
    public ConfirmAccessController(OAuthSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

	@RequestMapping(value="/oauth/confirm_access", method= RequestMethod.GET)
	protected String confirmAccessForm(@RequestParam("oauth_token") String requestToken, Model model) throws InvalidRequestTokenException, InvalidApiKeyException {
		String apiKey = sessionManager.getSession(requestToken).getApiKey();

		// TODO
		//App app = appRepository.findAppByApiKey(apiKey);
		model.addAttribute("oauth_token", requestToken);

        //model.addAttribute("clientApp", app);
		return "oauth/confirmAccess";
	}

}