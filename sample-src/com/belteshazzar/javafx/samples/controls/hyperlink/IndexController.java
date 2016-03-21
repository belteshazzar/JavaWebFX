package com.belteshazzar.javafx.samples.controls.hyperlink;

import org.w3c.dom.events.Event;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.WebScene;

public class IndexController {

	@HTML
	private WebScene scene;
	
	@HTML
	private void gotoMain(Event evt) {
		scene.load(getClass().getResource("main.html"));
	}

}
