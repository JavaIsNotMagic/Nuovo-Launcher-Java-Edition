package com.nl.gui;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.json.simple.parser.ParseException;

import com.nl.launcher.Launcher;
import com.nl.launcher.NuovoGenericError;

@SuppressWarnings("all")
public class VersionSelector extends Application {
  private List possible_versions;

public static void main(String[] args) {
    launch(args);
  }

@Override
  public void start(Stage stage) throws MalformedURLException, IOException, ParseException {
    Scene scene = new Scene(new Group(), 450, 250);

	possible_versions = Launcher.setup();
  
    ComboBox<String> myComboBox = new ComboBox<String>();
    myComboBox.getItems().addAll(possible_versions);
    myComboBox.setEditable(false);
    
    myComboBox.valueProperty().addListener(new ChangeListener<String>() {
        public void changed(ObservableValue ov, String t, String t1) {
            //System.out.println(ov);
            //System.out.println(t);
            String[] version = {t1, "vanilla"};
            try {
				stage.hide();
            	Launcher.main(version);
			} catch (ArrayIndexOutOfBoundsException | IOException | ParseException | NuovoGenericError | InterruptedException e) {
				e.printStackTrace();
			}
            
        }    
    });
      
    
    Group root = (Group) scene.getRoot();
    root.getChildren().add(myComboBox);
    stage.setScene(scene);
    stage.show();

  }
}