/*    */ package com.nl.gui;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javafx.application.Application;
/*    */ import javafx.fxml.FXMLLoader;
/*    */ import javafx.scene.Parent;
/*    */ import javafx.scene.Scene;
/*    */ import javafx.stage.Stage;
/*    */ 
/*    */ public class CreateAccount
/*    */   extends Application
/*    */ {
/*    */   public void start(Stage primaryStage) throws Exception {
/*    */     Parent root;
/* 15 */     FXMLLoader loader = new FXMLLoader(getClass().getResource("styles/CreateAccount.fxml"));
/*    */ 
/*    */     
/*    */     try {
/* 19 */       root = (Parent)loader.load();
/* 20 */     } catch (IOException ioe) {
/*    */       return;
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 26 */     CreateAccountController CreateAccountController = (CreateAccountController)loader.getController();
/* 27 */     CreateAccountController.setMain(this);
/*    */     
/* 29 */     primaryStage.setTitle("Nuovo Launcher");
/* 30 */     primaryStage.setScene(new Scene(root, 800.0D, 500.0D));
/* 31 */     primaryStage.show();
/*    */   }
/*    */ 
/*    */   
/*    */   public static void main(String[] args) {
/* 36 */     launch(args);
/*    */   }
/*    */ }


/* Location:              /home/connor/Desktop/Development/NuovoLauncher - Java Edition/com/gui-1.0.jar!/com/mcpy/gui/CreateAccount.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
