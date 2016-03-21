package com.belteshazzar.javafx.samples.controls.colorpicker;

import org.w3c.dom.html.HTMLElement;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.WebScene;

import javafx.application.Application;
import javafx.beans.property.Property;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ColorPickerSample extends Application {

	@HTML private Property<Color> picker;
	@HTML private HTMLElement label;
	
	public void initialize() {
		picker.addListener((ob,ov,nv) -> label.setTextContent(nv.toString()));
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new WebScene(getClass().getResource("colorpicker_sample.html"), 200, 100));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}