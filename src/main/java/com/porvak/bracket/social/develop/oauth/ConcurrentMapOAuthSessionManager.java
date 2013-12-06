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

import javax.inject.Inject;
import java.util.concurrent.ConcurrentMap;

/**
 * OAuthSessionManager implementation that stores OAuthSessions in an in-memory ConcurrentMap.
 * Depends on an AppRepository to store app connections.
 * The ConcurrentMap, built by Google Guava's MapMaker, uses soft values and expires entries after 2 minutes of idle activity.
 * @author Keith Donald
 * @see com.google.common.collect.MapMaker
 */
public class ConcurrentMapOAuthSessionManager extends AbstractOAuthSessionManager {

	private final ConcurrentMap<String, StandardOAuthSession> sessions;

//	@Inject
//	public ConcurrentMapOAuthSessionManager(AppRepository appRepository) {
//		super(appRepository);
//		sessions = null;//new MapMaker().softValues().expireAfterWrite(2, TimeUnit.MINUTES).makeMap();
//	}

    @Inject
    public ConcurrentMapOAuthSessionManager() {
        super(null);
        sessions = null;//new MapMaker().softValues().expireAfterWrite(2, TimeUnit.MINUTES).makeMap();
    }

	@Override
	protected void put(StandardOAuthSession session) {
		sessions.put(session.getRequestToken(), session);
	}

	@Override
	protected StandardOAuthSession get(String requestToken) {
		return sessions.get(requestToken);
	}

	@Override
	protected void remove(String requestToken) {
		sessions.remove(requestToken);
	}

}