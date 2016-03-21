package com.belteshazzar.javafx.samples.controls.progress;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.WebScene;

import javafx.application.Application;
import javafx.beans.property.Property;
import javafx.stage.Stage;

public class ProgressSample extends Application {

	@HTML private Property<Number> progress;
	@HTML private Property<Number> range;
	
	public void initialize() {
		range.addListener((ob,ov,nv) -> progress.setValue(nv));
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new WebScene(getClass().getResource("progress_sample.html"), 300, 190));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}