package com.belteshazzar.javafx.samples.controls.checkbox;

import org.w3c.dom.html.HTMLElement;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.WebScene;

import javafx.application.Application;
import javafx.beans.property.Property;
import javafx.stage.Stage;

public class CheckboxSample extends Application {

	@HTML private HTMLElement projectImg;
	@HTML private HTMLElement securityImg;
	@HTML private HTMLElement chartImg;
	@HTML private Property<Boolean> project;
	@HTML private Property<Boolean> security;
	@HTML private Property<Boolean> chart;
	
	public void initialize() {
		security.addListener((ob,ov,nv) -> securityImg.setAttribute("style", "visibility:" + (nv?"visible":"hidden")));
		project.addListener((ob,ov,nv) -> projectImg.setAttribute("style", "visibility:" + (nv?"visible":"hidden")));
		chart.addListener((ob,ov,nv) -> chartImg.setAttribute("style", "visibility:" + (nv?"visible":"hidden")));
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new WebScene(getClass().getResource("checkbox_sample.html"), 300, 190));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}