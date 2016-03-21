package com.belteshazzar.javafx.samples;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLParagraphElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.WebScene;
import com.belteshazzar.javafx.canvas.CanvasRenderingContext2D;
import com.belteshazzar.javafx.canvas.HTMLCanvasElement;


public class IndexController {

	@HTML
	WebScene scene;
	
	@HTML
	HTMLParagraphElement p;
	
	@HTML
	HTMLButtonElement my_button;
	
	@HTML(id = "my_text")
	HTMLInputElement my_textElement;
	
	@HTML
	HTMLTableElement table;
	
	@HTML
	HTMLCanvasElement cnvs;

	@HTML
	public void handleMyButtonClick(Event evt) {
		System.out.println("my_button click: " + evt);
		scene.load(getClass().getResource("main.html"));
	}

	@HTML
	public void handleTextInput(Event evt) {
		System.out.println("text input: " + evt);
		p.setTextContent(my_textElement.getValue());
	}
	
	@HTML
	public void onMouseMove(MouseEvent evt) {
		System.out.println("mouse move: " + evt.getClientX()+","+evt.getClientY());	
	}

	public void initialize() {
		System.out.println("init controller");
		((EventTarget)my_textElement).addEventListener("change", new EventListener() {

			@Override
			public void handleEvent(Event evt) {
				System.out.println(evt);
			}
			
		}, true);
		for (int i=0 ; i<10 ; i++) {
			HTMLTableRowElement row = (HTMLTableRowElement)table.insertRow(table.getRows().getLength());
			row.insertCell(0).setTextContent(i+"");
			row.insertCell(0).setTextContent("my content");
		}
		CanvasRenderingContext2D ctx = (CanvasRenderingContext2D)cnvs.getContext("2d");
		ctx.fillRect(10, 10, 100, 100);

	}
}
