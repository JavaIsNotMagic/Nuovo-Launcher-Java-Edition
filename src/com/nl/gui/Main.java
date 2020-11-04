package com.nl.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.nl.gui.MainController;
	@SuppressWarnings("unused")
	public class Main extends Application {
		public void start(Stage primaryStage) throws Exception {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("styles/Main.fxml"));
				Parent root = loader.load();
				primaryStage.setTitle("Nuovo Launcher");
				primaryStage.setScene(new Scene(root, 800.0D, 500.0D));
				primaryStage.show();
		}
		public static void main(String[] args) {
			launch(args);
		}
}

