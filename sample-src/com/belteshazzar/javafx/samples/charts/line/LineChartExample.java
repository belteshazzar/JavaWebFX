package com.belteshazzar.javafx.samples.charts.line;

import com.belteshazzar.javafx.WebScene;

import javafx.application.Application;
import javafx.stage.Stage;

public class LineChartExample extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new WebScene(getClass().getResource("linechart.html"), 400, 500));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}