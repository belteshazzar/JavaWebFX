package com.belteshazzar.javafx.samples.controls.hyperlink;

import com.belteshazzar.javafx.WebScene;

import javafx.application.Application;
import javafx.stage.Stage;

public class HyperlinkSample extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new WebScene(getClass().getResource("index.html"), 300, 300));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}