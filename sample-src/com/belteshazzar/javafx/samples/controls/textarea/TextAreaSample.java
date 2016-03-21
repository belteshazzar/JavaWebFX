package com.belteshazzar.javafx.samples.controls.textarea;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.WebScene;
import com.sun.webkit.dom.HTMLElementImpl;

import javafx.application.Application;
import javafx.beans.property.Property;
import javafx.stage.Stage;

public class TextAreaSample extends Application {

	@HTML private Property<String> textarea;
	@HTML private HTMLElementImpl display;
	
	public void initialize() {
		textarea.addListener((ob,ov,nv) -> display.setInnerHTML(nv));
		textarea.setValue("sample <b>text</b> here");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new WebScene(getClass().getResource("textarea_sample.html"), 300, 300));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}