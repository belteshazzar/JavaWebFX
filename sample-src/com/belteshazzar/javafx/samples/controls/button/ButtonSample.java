package com.belteshazzar.javafx.samples.controls.button;

import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLElement;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.WebScene;

import javafx.application.Application;
import javafx.stage.Stage;

public class ButtonSample extends Application {

	@HTML
	private void accept(Event ev) {
		label.setTextContent("Accepted");
	}

	@HTML
	private void decline(Event ev) {
		label.setTextContent("Declined");
	}
	
	@HTML
	private HTMLElement label;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new WebScene(getClass().getResource("button_sample.html"), 300, 190));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}