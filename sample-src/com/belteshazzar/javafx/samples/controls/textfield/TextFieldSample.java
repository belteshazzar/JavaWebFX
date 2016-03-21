package com.belteshazzar.javafx.samples.controls.textfield;

import org.w3c.dom.html.HTMLElement;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.WebScene;

import javafx.application.Application;
import javafx.beans.property.Property;
import javafx.stage.Stage;

public class TextFieldSample extends Application {

	@HTML private Property<String> name;
	@HTML private HTMLElement label;
	
	public void initialize() {
		name.addListener((ob,ov,nv) -> label.setTextContent(nv));
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new WebScene(getClass().getResource("textfield_sample.html"), 300, 300));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}