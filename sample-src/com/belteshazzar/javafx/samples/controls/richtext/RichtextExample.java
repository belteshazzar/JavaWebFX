package com.belteshazzar.javafx.samples.controls.richtext;

import com.belteshazzar.javafx.WebScene;

import javafx.application.Application;
import javafx.stage.Stage;

public class RichtextExample extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new WebScene(getClass().getResource("richtext_example.html"), 800, 600));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}