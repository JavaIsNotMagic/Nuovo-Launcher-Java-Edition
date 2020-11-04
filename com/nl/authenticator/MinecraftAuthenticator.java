package com.nl.authenticator;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Base64;


public class MinecraftAuthenticator {
	@SuppressWarnings("all")
	public static String Authenticate(String username, String password) throws IOException, InterruptedException, ParseException {
		JSONObject agent_object = new JSONObject();
		JSONObject request_object = new JSONObject();
		String mc_user = "";
		
		agent_object.put("name", "Minecraft");
		agent_object.put("version", "1");
		
		request_object.put("agent", agent_object);
		request_object.put("username", username);
		request_object.put("password", password);
		
		String json_request = request_object.toJSONString();
		//DEBUG Print JSON Request
		//System.out.println(json_request);
		
		String response = AutenticationHandler.runner(json_request);
		PrintWriter pw = new PrintWriter(System.getProperty("user.dir") + "/.nuovo/auth_response.json");
		pw.write(response);
		pw.flush();
		pw.close();
		
		try { 
			Object response_object = new JSONParser().parse(new FileReader(System.getProperty("user.dir") + "/.nuovo/auth_response.json"));
			JSONObject response_json = (JSONObject) response_object;
			JSONObject mc_username = (JSONObject) response_json.get("selectedProfile");
			mc_user = mc_username.get("name").toString();
			String accessObject = response_json.get("accessToken").toString();
			String clientToken = response_json.get("clientToken").toString();
			accessObject = accessObject.replace(".", "\n").replace("_", "\n");
			String accessObjectString = accessObject.split("\n")[1];
			
			//accessObjectString is a Base64 encoded string, so now we need to decode it
			byte[] decodedBytes = Base64.getDecoder().decode(accessObjectString.getBytes("UTF-8"));
			String decodedString = new String(decodedBytes);
			
			//Parse the JSON
			PrintWriter accessTokenObjectWriter = new PrintWriter(System.getProperty("user.dir") + "/.nuovo/auth_token.json");
			accessTokenObjectWriter.write(decodedString);
			accessTokenObjectWriter.flush();
			accessTokenObjectWriter.close();
			
			Object auth_object = new JSONParser().parse(new FileReader(System.getProperty("user.dir") + "/.nuovo/auth_token.json"));
			JSONObject auth_jo = (JSONObject) auth_object;
			String accessToken = auth_jo.get("yggt").toString();
			
			JSONObject dataObject = new JSONObject();
			dataObject.put("username", mc_user);
			dataObject.put("clientToken", clientToken);
			dataObject.put("accessToken", accessToken);
			
			PrintWriter pw1 = new PrintWriter(System.getProperty("user.dir") + "/.nuovo/user-info.json");
			pw1.write(dataObject.toJSONString());
			pw1.flush();
			pw1.close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			return mc_user;
		}
		
	}

}
