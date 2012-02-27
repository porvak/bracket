package com.porvak.bracket.social.develop;

import java.util.List;


public interface AppRepository {

	/**
	 * Get a short summary of all the applications the member is a developer for.
	 * Used to present the list of registered applications to their developer.
	 * @param accountId the member account id
	 */
	List<AppSummary> findAppSummaries(Long accountId);



	/**
	 * Delete an application the member is a developer for.
	 * Used by the developer when they no longer wish to connect their application to this system.
	 * @param accountId the member account id
	 * @param slug a short, meaningful key that identifies the application
	 */
	void deleteApp(Long accountId, String slug);

	
	// for supporting AppConnections
	
	/**
	 * Get a detailed description of the Application assigned the apiKey.
	 * Used to present details of the application to a member during connection authorization.
	 * @param apiKey the assigned api key, submitted by the client application itself on behalf of the member
	 * @throws com.porvak.bracket.social.develop.InvalidApiKeyException the key provided by the client is not valid
	 */
	//App findAppByApiKey(String apiKey) throws InvalidApiKeyException;

	/**
	 * Connect the member to the Application assigned the api key.
	 * Called to grant the application access to the member's account.
	 * Called only after member authorization.
	 * @param accountId the member account id
	 * @param apiKey the api key
	 * @return a model for the new connection, to be used to send the connection details such as the assigned accessToken back to the client
	 * @throws com.porvak.bracket.social.develop.InvalidApiKeyException the app api key provided by the client is not valid
	 */
	AppConnection connectApp(Long accountId, String apiKey) throws InvalidApiKeyException;

	/**
	 * Find the App-to-Member Connection with the client-provided access token.
	 * Called by the client application when making a request for a protected resource.
	 * @param accessToken the access token serving as a key for the connection that was assigned on {@link #connectApp(Long, String)}.
	 * @return a model for the connection, to be used to validate the client request for a protected resource
	 * @throws NoSuchAccountConnectionException no such connection exists for the provided access token; this could happen if the connection was severed due to explicit disconnect or expiration.
	 */
	AppConnection findAppConnection(String accessToken) throws NoSuchAccountConnectionException;
	
	/**
	 * Disconnect the connection between an Application and a Member that assigned the provided access token.
	 * Called by the member or an administrator to revoke access by the application to the member's data.
	 * @param accountId the member account id 
	 * @param accessToken the access token serving as the key for the connection
	 */
	void disconnectApp(Long accountId, String accessToken);
	
}