package com.belteshazzar.javafx.samples.controls.menu;

import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLElement;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.WebScene;

import javafx.application.Application;
import javafx.scene.control.CheckMenuItem;
import javafx.stage.Stage;

public class MenuSample extends Application {

	@HTML
	private void exit(Event ev) {
		System.exit(0);
	}
	
	@HTML private HTMLElement title;
	@HTML private HTMLElement picture;
	@HTML private HTMLElement description;
	
	@HTML private CheckMenuItem toggleTitle;
	@HTML private CheckMenuItem togglePicture;
	@HTML private CheckMenuItem toggleDescription;
	
	@HTML
	private void toggleTitle(Event ev) {
		title.setAttribute("style", "visibility: "+(toggleTitle.isSelected()?"visible":"hidden")+";");
	}

	@HTML
	private void togglePicture(Event ev) {
		picture.setAttribute("style", "visibility: "+(togglePicture.isSelected()?"visible":"hidden")+";");
	}

	@HTML
	private void toggleDescription(Event ev) {
		description.setAttribute("style", "visibility: "+(toggleDescription.isSelected()?"visible":"hidden")+";");
	}

	public void initialize() {
		toggleTitle.setSelected(true);
		togglePicture.setSelected(true);
		toggleDescription.setSelected(true);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new WebScene(getClass().getResource("menu_sample.html"), 320, 500));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}