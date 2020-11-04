/*    */ package com.nl.gui;
/*    */ 
/*    */ import javafx.application.Platform;
/*    */ import javafx.event.ActionEvent;
/*    */ import javafx.fxml.FXML;
/*    */ import javafx.scene.control.Button;
/*    */ import javafx.scene.control.Label;
/*    */ import javafx.scene.control.PasswordField;
/*    */ import javafx.scene.control.TextField;
/*    */ import javafx.scene.layout.AnchorPane;
		 import javafx.stage.Stage;

/*    */ public class CreateAccountController
/*    */ {
/*    */   public CreateAccount acct;
/*    */   @FXML
/*    */   public TextField username;
/*    */   @FXML
/*    */   public PasswordField password;
/*    */   @FXML
/*    */   public TextField email;
/*    */   @FXML
/*    */   public Button submit;
/*    */   @FXML
/*    */   public AnchorPane submit_pane;
/*    */   @FXML
/*    */   public Label label_username;
/*    */   @FXML
/*    */   public Label label_password;
/*    */   @FXML
/*    */   public Label label_email;
/*    */   
/*    */   @FXML
/*    */   public void submitActionEvent(ActionEvent event) {
/* 41 */     String pass = this.password.getText().toString();
/* 42 */     String user = this.username.getText().toString();
/* 43 */     String email_text = this.email.getText().toString();
/* 44 */     Boolean a = Boolean.valueOf(AccountHandler.accountCreate(user, pass, email_text));
/* 45 */     if (a.booleanValue()) {
/* 46 */       System.out.println("ACCOUNT CREATION SUCCSESS");
			   Stage stage = (Stage) label_username.getScene().getWindow();
			   stage.close();
/*    */     } else {
/* 48 */       System.out.println("ACCOUNT CREATION FAILED");
/* 49 */       Platform.exit();
/*    */     } 
/*    */   }
/*    */   public void setMain(CreateAccount acct) {
/* 53 */     this.acct = acct;
/*    */   }
/*    */ }


/* Location:              /home/connor/Desktop/Development/NuovoLauncher - Java Edition/com/gui-1.0.jar!/com/mcpy/gui/CreateAccountController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
