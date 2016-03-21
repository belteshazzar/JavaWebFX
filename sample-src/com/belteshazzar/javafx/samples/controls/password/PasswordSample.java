package com.belteshazzar.javafx.samples.controls.password;

import org.w3c.dom.html.HTMLElement;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.WebScene;

import javafx.application.Application;
import javafx.beans.property.Property;
import javafx.stage.Stage;

public class PasswordSample extends Application {

	@HTML private Property<String> password;
	@HTML private HTMLElement label;
	
	public void initialize() {
		password.addListener((ob,ov,nv) -> label.setTextContent(nv));
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new WebScene(getClass().getResource("password_sample.html"), 400, 100));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}