package com.nl.gui;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.json.simple.JSONObject;


public class AccountUtils {
    public static String encodePassword(String password) throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);

        String hashed_password = md.digest(password.getBytes(StandardCharsets.UTF_8)).toString() + ":" + salt.toString();
        return hashed_password;

    }
    @SuppressWarnings("unchecked")
	public static void writeUserFile(String user, String passwd) throws FileNotFoundException {
    	JSONObject jo = new JSONObject();
    	jo.put("username", user);
    	jo.put("password", passwd);
    	
    	PrintWriter pw = new PrintWriter(System.getProperty("user.dir") + "/.nuovo/usercache.json");
    	pw.write(jo.toJSONString());
    	pw.flush();
    	pw.close();
    	
    }
    public static String encodePasswordWithSalt(String salt, String password) throws NoSuchAlgorithmException {
    	 MessageDigest md = MessageDigest.getInstance("SHA-512");
         md.update(salt.getBytes());

         String hashed_password = md.digest(password.getBytes(StandardCharsets.UTF_8)).toString();
         return hashed_password;
    }
}
        
