package com.nl.authenticator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;




public class AutenticationHandler {
	public static String runner(String requestBody) throws IOException, InterruptedException {
		URL url = new URL("https://authserver.mojang.com/authenticate");
		URLConnection con = url.openConnection();
		HttpURLConnection http = (HttpURLConnection) con;
		http.setRequestMethod("POST");
		http.setDoOutput(true);
		byte[] request = requestBody.getBytes();
		int length = request.length;
		
		http.setFixedLengthStreamingMode(length);
		http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		http.connect();
		try(OutputStream os = http.getOutputStream()) {
			os.write(request);
		}
		InputStream out = http.getInputStream();
		byte[] b = {};
		int off = 0;
		out.read(b, off, length);
		return b.toString();
		
		
	}

}
