package com.nl.authenticator;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.nl.launcher.Launcher;
import com.nl.launcher.initFiles;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Base64;


public class MinecraftAuthenticator {
	@SuppressWarnings("all")
	public static String Authenticate(String username, String password) throws IOException, InterruptedException, ParseException {
		//Globals
		File auth_response = new File(System.getProperty("user.dir") + "/.nuovo/auth_response.json");
		File auth_token = new File(System.getProperty("user.dir") + "/.nuovo/auth_token.json");
		File user_info = new File(System.getProperty("user.dir") + "/.nuovo/user-info.json");
		
		//Create the files
		try {
			initFiles.create_file(auth_response);
			initFiles.create_file(auth_token);
			initFiles.create_file(user_info);
		} catch(Exception e) {
			Launcher.create_folders();
		} finally {
			initFiles.create_file(auth_response);
			initFiles.create_file(auth_token);
			initFiles.create_file(user_info);
		}
		
		//Format the JSON request
		
		JSONObject agent_object = new JSONObject();
		JSONObject request_object = new JSONObject();
		String mc_user = "";
		
		agent_object.put("name", "Minecraft");
		agent_object.put("version", "1");
		
		request_object.put("agent", agent_object);
		request_object.put("username", username);
		request_object.put("password", password);
		
		String json_request = request_object.toJSONString();
		
		//Write it to disk
		String response = AutenticationHandler.runner(json_request);
		PrintWriter pw = new PrintWriter(auth_response);
		pw.write(response);
		pw.flush();
		pw.close();
		
		try { 
			//Decode the response from the auth server
			Object response_object = new JSONParser().parse(new FileReader(auth_response));
			JSONObject response_json = (JSONObject) response_object;
			//Get the username of the player
			JSONObject mc_username = (JSONObject) response_json.get("selectedProfile");
			mc_user = mc_username.get("name").toString();
			//Get and decode the accessToken from the response
			String accessObject = response_json.get("accessToken").toString();
			accessObject = accessObject.replace(".", "\n").replace("_", "\n");
			String accessObjectString = accessObject.split("\n")[1];
			
			//Get the clientToken
			String clientToken = response_json.get("clientToken").toString();
			
			//accessObjectString is a Base64 encoded string, so now we need to decode it
			byte[] decodedBytes = Base64.getDecoder().decode(accessObjectString.getBytes("UTF-8"));
			String decodedString = new String(decodedBytes);
			
			//Parse the JSON
			PrintWriter accessTokenObjectWriter = new PrintWriter(auth_token);
			accessTokenObjectWriter.write(decodedString);
			accessTokenObjectWriter.flush();
			accessTokenObjectWriter.close();
			
			//Actually save the accessToken
			Object auth_object = new JSONParser().parse(new FileReader(auth_token));
			JSONObject auth_jo = (JSONObject) auth_object;
			String accessToken = auth_jo.get("yggt").toString();
			
			//Prepare to write, then write the user cache (last logged in user only)
			JSONObject dataObject = new JSONObject();
			dataObject.put("username", mc_user);
			dataObject.put("clientToken", clientToken);
			dataObject.put("accessToken", accessToken);
			
			PrintWriter pw1 = new PrintWriter(user_info);
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
