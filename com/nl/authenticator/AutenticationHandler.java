package com.nl.authenticator;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AutenticationHandler {
	public static String runner(String requestBody) throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://authserver.mojang.com/authenticate")).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
		
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		return response.body().toString();
	}

}
