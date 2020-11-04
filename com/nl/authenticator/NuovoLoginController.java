package com.nl.authenticator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import com.nl.launcher.Launcher;
import com.nl.launcher.NuovoGenericError;

@SuppressWarnings("all")
public class NuovoLoginController {
	private NuovoLoginController nlc;
	public void setMain(NuovoLoginController login) {
		this.setMain(login);
	}
	
	@FXML
	public TextField minecraft_username;
	
	@FXML
	public PasswordField minecraft_password;
	
	@FXML
	public Button submit;
	
	public void submitActionEvent(ActionEvent sae) throws Exception {
		String username = this.minecraft_username.getText();
		String password = this.minecraft_password.getText();
		
		//I don't even know....
		try {
			String s = MinecraftAuthenticator.Authenticate(username, password);
			if(s != null) {
				Application vs = new OfficialVersionSelector();
				vs.init();
				vs.start(new Stage());
			} else {
				Platform.exit();
			}
	} catch(Exception w) {
		;
	} finally {
			String s = MinecraftAuthenticator.Authenticate(username, password);
			if(s != null) {
				Stage stage = (Stage) minecraft_password.getScene().getWindow();
				stage.hide();
				Application vs = new OfficialVersionSelector();
				vs.init();
				vs.start(new Stage());
			} else {
				Platform.exit();
			}
		}
	}
}
