package com.belteshazzar.javafx.samples.controls.filechooser;

import java.io.File;

import org.w3c.dom.html.HTMLElement;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.WebScene;

import javafx.application.Application;
import javafx.beans.property.Property;
import javafx.stage.Stage;

public class FileChooserSample extends Application {

	@HTML private Property<File> open;
	@HTML private HTMLElement filename;
	
	public void initialize() {
		open.addListener((ob,ov,nv) -> filename.setTextContent(nv.toString()));
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new WebScene(getClass().getResource("filechooser_sample.html"), 800, 600));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}