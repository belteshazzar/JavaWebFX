package com.belteshazzar.javafx.FXMLExample;

import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLElement;

import com.belteshazzar.javafx.HTML;

public class FXMLExampleController {

	@HTML
	private HTMLElement actiontarget;
	
	@HTML
	protected void handleSubmitButtonAction(Event ev) {
		actiontarget.setTextContent("Sign in button pressed");
	}
}
