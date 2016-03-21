package com.belteshazzar.javafx.samples;

import org.w3c.dom.events.Event;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.WebScene;

public class MainController {

	@HTML
	WebScene scene;
	
	@HTML
	public void gotoIndex(Event evt) {
		System.out.println("goto index: " + evt);
		scene.load(getClass().getResource("index.html"));
	}

}
