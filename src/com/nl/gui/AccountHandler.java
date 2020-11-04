/*    */ package com.nl.gui;
		 import java.security.NoSuchAlgorithmException;
/*    */ import java.sql.Connection;
/*    */ import java.sql.DriverManager;
		 import java.sql.PreparedStatement;
/*    */ import java.sql.ResultSet;
		 import java.sql.Statement;
/*    */ 
/*    */ public class AccountHandler {
/*    */   private static void dumpInformation(String username, String name, String password, String email) throws NoSuchAlgorithmException {
			System.out.println("Account Information");	
			System.out.println("-------------------");
			System.out.println("Username: " + username);
			System.out.println("Name    : " + name    );
			System.out.println("Password: " + AccountUtils.encodePassword(password));
			System.out.println("Email for user: " + username + ": " + email);
			System.out.println("-------------------");
			}
		 	
			public static boolean accountCreate(String username, String password, String email) {
/*    */    	try {
/*  9 */       		String acct_passwd = AccountUtils.encodePassword(password.toString());
	                //Class.forName("com.mysql.jdbc.Driver");
/* 10 */       		Connection con = DriverManager.getConnection("jdbc:mysql://mcpypc.com:3306/NuovoLauncher?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "nl", "nuovoLauncher1234");
/* 11 */       		//String statement_string = new String("insert into users(username, name, password, email) values('" + "\""+ username.toString() +"\"" + "', '" + "noname" + "', '" + "\""+ password.toString() +"\"" + "' , '" + "\""+ email +"\"" + ")");
/* 12 */       		String statement_string = "insert into users(username, password, email) values('" + username.toString() + "', '" + acct_passwd + "' , '" + email.toString() + "')";
					PreparedStatement mystatement = con.prepareStatement(statement_string);
/* 13 */       		dumpInformation(username, "noname", password, email);
/* 14 */       		mystatement.executeUpdate();
					//Write user cache
					AccountUtils.writeUserFile(username, acct_passwd);
/* 15 */       		return true;
/* 16 */     	} catch (Exception e) {
/* 17 */       		e.printStackTrace();
/* 18 */      		return false;
/*    */    	}
/*    */   }
/*    */   
/*    */   public static boolean authUser(String username, String password) {
/*    */     try {
/* 24 */       Class.forName("com.mysql.jdbc.Driver");
/* 25 */       Connection con = DriverManager.getConnection("jdbc:mysql://mcpypc.com:3306/NuovoLauncher?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "nl", "nuovoLauncher1234");
/* 26 */       Statement mystatement = con.createStatement();
/* 27 */       ResultSet codespeedy = mystatement.executeQuery("select * from users");
/* 28 */       if (codespeedy.next() && username.equals(codespeedy.getString("username"))) {
					if (password.equals(codespeedy.getString("password"))) {
/* 31 */           return true;
/*    */         }
/* 33 */         System.out.println("LOGIN FAILED");
/* 34 */         return false;
/*    */       } 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 40 */       return false;
/* 41 */     } catch (Exception e) {
/* 42 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/connor/Desktop/Development/NuovoLauncher - Java Edition/com/gui-1.0.jar!/com/mcpy/gui/AccountHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
