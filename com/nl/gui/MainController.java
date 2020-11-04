/*    */ package com.nl.gui;
/*    */ 
/*    */ import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.nl.authenticator.NuovoLogin;
import com.nl.launcher.NuovoGenericError;

import javafx.application.Application;
/*    */ import javafx.event.ActionEvent;
/*    */ import javafx.fxml.FXML;
/*    */ import javafx.scene.control.Button;
/*    */ import javafx.scene.control.TitledPane;
/*    */ import javafx.stage.Stage;
/*    */ 
/*    */ 
/*    */ public class MainController
/*    */ {
/*    */   @SuppressWarnings("unused")
		   private Main main;
		   public void setMain(Main main) {
/* 18 */     this.main = main;
/*    */   }

/*    */   @FXML
/*    */   private static Button login;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @FXML
/*    */   private static Button create_account;
/*    */   
/*    */   @FXML
/*    */   private static Button play;

		   @FXML
		   private static Button play_online;
/*    */   
/*    */   @FXML
/*    */   private static TitledPane rootTitledPane;
/*    */ 
/*    */   
/*    */   @FXML
/*    */   public void loginButtonEvent(ActionEvent lbe) throws Exception {
		     Application player_login = new Login();
/* 36 */     player_login.init();
/* 37 */     player_login.start(new Stage());
/*    */   }
/*    */   
/*    */   @FXML
/*    */   public void create_accountButtonEvent(ActionEvent cabe) throws Exception {
/* 42 */     Application create_login = new CreateAccount();
/* 43 */     create_login.init();
/* 44 */     create_login.start(new Stage());
/*    */   }
/*    */   
/*    */   @FXML
/*    */   public void playButtonActionEvent(ActionEvent pbae) throws IOException, ParseException, NuovoGenericError, InterruptedException {
/* 49 */     Play.play_mc();
/*    */   }
		   @FXML
/*    */   public void onlineButtonEvent(ActionEvent pbae) throws Exception {
/* 49 */     Application online = new NuovoLogin();
			 online.init();
			 online.start(new Stage());
/*    */   }
		
/*    */ }
