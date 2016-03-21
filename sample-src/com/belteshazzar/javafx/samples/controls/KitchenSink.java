package com.belteshazzar.javafx.samples.controls;

import com.belteshazzar.javafx.WebScene;

import javafx.application.Application;
import javafx.stage.Stage;

public class KitchenSink extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new WebScene(getClass().getResource("kitchen_sink.html"), 800, 600));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}