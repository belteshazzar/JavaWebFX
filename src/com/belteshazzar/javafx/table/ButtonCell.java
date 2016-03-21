package com.belteshazzar.javafx.table;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLTableCellElement;

public class ButtonCell<S,T> extends Cell<S,T> {

	private String buttonText;
	private Clicked<S> clicked;
	
	public ButtonCell(HTMLTableCellElement cell, String buttonText, Clicked<S> clicked) {
		super(cell);
		this.buttonText = buttonText;
		this.clicked = clicked;
	}
	
	@Override
	public void updateCell(HTMLTableCellElement cell, T value) {
		while (cell.hasChildNodes()) cell.removeChild(cell.getLastChild());
		HTMLButtonElement button = (HTMLButtonElement)cell.getOwnerDocument().createElement("button");
		button.setTextContent(buttonText);
		((EventTarget)button).addEventListener("click", new EventListener() {

			@Override
			public void handleEvent(Event evt) {
				clicked.clicked(itemProperty().getValue());
			}
			
		}, false);
		cell.appendChild(button);
	}
	
	@FunctionalInterface
	public interface Clicked<T> {
		void clicked(T t);
	}
	
}
