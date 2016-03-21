package com.belteshazzar.javafx.samples.controls.label;

import org.w3c.dom.html.HTMLElement;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.WebScene;

import javafx.application.Application;
import javafx.stage.Stage;

public class LabelSample extends Application {

	@HTML private HTMLElement myLabel;
	
	public void initialize() {
		myLabel.setAttribute("style", "font-face: tahoma; color: orange; font-size: 22pt");
		myLabel.setTextContent("This is my label, populated from the java controller");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new WebScene(getClass().getResource("label_sample.html"), 300, 200));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}