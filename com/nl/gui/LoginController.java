/*    */ package com.nl.gui;
import java.io.File;
import java.io.FileReader;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.application.Application;
/*    */ 
/*    */ import javafx.application.Platform;
/*    */ import javafx.event.ActionEvent;
/*    */ import javafx.fxml.FXML;
/*    */ import javafx.scene.control.Button;
/*    */ import javafx.scene.control.PasswordField;
/*    */ import javafx.scene.control.TextField;
import javafx.stage.Stage;
/*    */ 
/*    */@SuppressWarnings("all") 
		public class LoginController
/*    */ {
/*    */   private Login login;
/*    */   
/*    */   public void setMain(Login login) {
/* 15 */     this.setLogin(login);
/*    */   }
/*    */ 
/*    */   
/*    */   @FXML
/*    */   public TextField username;
/*    */   
/*    */   @FXML
/*    */   public PasswordField password;
/*    */   
/*    */   @FXML
/*    */   public Button submit;
/*    */ 
/*    */   
/*    */   @FXML
/*    */   public void submitActionEvent(ActionEvent sae) throws Exception {
/* 31 */     System.out.println("Login button pressed.");
/* 32 */     String user = this.username.getText();
/* 33 */     String pass = this.password.getText();
			 Object obj = new JSONParser().parse(new FileReader(System.getProperty("user.dir") + "/.nuovo/usercache.json"));
			 JSONObject jo = (JSONObject) obj;
			 String password_full = jo.get("password").toString();
			 String password = password_full.split(":")[0];
			 String salt = password_full.split(":")[1];
/* 34 */	 if(AccountUtils.encodePasswordWithSalt(salt, pass) == password) {      
			 	if (pass != null || pass != "") {
			 		Boolean a = Boolean.valueOf(AccountHandler.authUser(user, AccountUtils.encodePasswordWithSalt(salt, pass)));
			 		if (a.booleanValue()) {
			 			System.out.println("LOGIN SUCCESS");
			 			// Start the Version Selector
			 			Application version_selector = new VersionSelector();
			 			version_selector.init();
			 			version_selector.start(new Stage());
			 			//Close the login window
			 			Stage stage = (Stage) username.getScene().getWindow();
			 			stage.close(); 
			 		} else {       
			 			System.out.println("LOGIN FAILED");
			 			Stage stage = (Stage) username.getScene().getWindow();
			 			stage.close();
			 		} 
			 	} else {      
			 		System.out.println("Username or password is empty.");
			 		Platform.exit();
			 	}
   			} else {
   				Platform.exit();
   			}
		}

			public Login getLogin() {
				return login;
			}
			public void setLogin(Login login) {
				this.login = login;
			} 			
	}



/* Location:              /home/connor/Desktop/Development/NuovoLauncher - Java Edition/com/gui-1.0.jar!/com/mcpy/gui/LoginController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
