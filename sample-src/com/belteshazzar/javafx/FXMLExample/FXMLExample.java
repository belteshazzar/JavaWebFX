package com.belteshazzar.javafx.FXMLExample;

import com.belteshazzar.javafx.WebScene;

import javafx.application.Application;
import javafx.stage.Stage;

public class FXMLExample extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new WebScene(getClass().getResource("fxml_example.html"), 300, 275));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}