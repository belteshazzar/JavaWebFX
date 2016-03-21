package com.belteshazzar.javafx.samples.controls.radiobutton;

import org.w3c.dom.html.HTMLImageElement;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.WebScene;

import javafx.application.Application;
import javafx.beans.property.Property;
import javafx.stage.Stage;

public class RadioButtonSample extends Application {

	@HTML
	private HTMLImageElement img;
	
	@HTML(name="selectedImage")
	private Property<String> selectedImage;
	
	public void initialize() {
		selectedImage.addListener((ob,ov,nv) -> img.setSrc(nv));
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new WebScene(getClass().getResource("radiobutton_sample.html"), 250, 150));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}