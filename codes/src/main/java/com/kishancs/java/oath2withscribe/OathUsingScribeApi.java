package com.kishancs.java.oath2withscribe;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * @author kishancs
 *
 */
public class AddwordsOath {
	private static final String NETWORK_NAME = "Google";
	private static final String PROTECTED_RESOURCE_URL = "https://www.googleapis.com/plus/v1/people/me";

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		new AddwordsOath().googleOAuth2();
		System.out.println("kishan");
	}

	public void googleOAuth2() throws IOException, InterruptedException, ExecutionException {
		try {
			// Replace these with your own api key and secret

			String apiKey = "297379726866-fpihmr83kj6pp4ovdca37dndg7c0cm53.apps.googleusercontent.com";// App ID
			String apiSecret = "ILVC6MyafW3rGSmB5Ofq4vER";
			// OAuth20Service service = new
			// ServiceBuilder(apiKey).apiSecret(apiSecret).callback("urn:ietf:wg:oauth:2.0:oob").build(GoogleApi20.instance());

			OAuth20Service service = new ServiceBuilder(apiKey).apiKey(apiKey).apiSecret(apiSecret).scope("profile").callback("urn:ietf:wg:oauth:2.0:oob").build(GoogleApi20.instance());
			Scanner in = new Scanner(System.in);

			System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
			System.out.println();

			// Obtain the Authorization URL
			System.out.println("Fetching the Authorization URL...");
			String authorizationUrl = service.getAuthorizationUrl();
			System.out.println("Got the Authorization URL!");
			System.out.println("Now go and authorize Scribe here:");
			System.out.println(authorizationUrl);
			System.out.println("And paste the authorization code here");
			System.out.print(">>");
			String verifierCode = in.nextLine();
			System.out.println();

			// Trade the Request Token and Verfier for the Access Token
			System.out.println("Trading the Request Token for an Access Token...");
			OAuth2AccessToken accessToken = service.getAccessToken(verifierCode);
			System.out.println("Got the Access Token!");
			System.out.println("-----------------------------------------");
			System.out.println("Access Token :" + accessToken.getAccessToken());
			System.out.println("Refresh Token:" + accessToken.getRefreshToken());
			System.out.println("ExpiresIn :" + accessToken.getExpiresIn());
			System.out.println("-----------------------------------------");
			System.out.println("Now we're going to access a protected resource...");
			OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
			service.signRequest(accessToken, request);
			Response response = service.execute(request);
			System.out.println("Got it! ..." + response.getMessage());
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}