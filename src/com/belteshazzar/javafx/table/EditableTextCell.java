package com.belteshazzar.javafx.table;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLTableCellElement;

import com.sun.webkit.dom.KeyboardEventImpl;

public class EditableTextCell<ItemType> extends Cell<ItemType,String>{

	private HTMLInputElement textInput = null;
	private EventListener keyupListener;
	private EventListener focusoutListener;
	
	public EditableTextCell(HTMLTableCellElement cell) {
		super(cell);
	}
	
	@Override
	public void updateCell(HTMLTableCellElement cell, String value) {
		if (this.editingProperty().get()) {
			textInput.setValue(value==null?"":value.toString());
		} else {
			cell.setTextContent((value==null?"":value.toString()));
		}
	}
	
	@Override
	public void startEditing(HTMLTableCellElement cell, String value) {
		if (textInput==null) {
			textInput = (HTMLInputElement) cell.getOwnerDocument().createElement("input");
			textInput.setAttribute("type", "text");
			keyupListener = new EventListener() {

				@Override
				public void handleEvent(Event evt) {
					if (evt instanceof KeyboardEventImpl) {
						KeyboardEventImpl ke = (KeyboardEventImpl)evt;
						if (ke.getKeyCode()==13) {
							commitEdit(textInput.getValue());
						}
						else if (ke.getKeyCode()==27) {
							cancelEdit();
						}
					}
					
				}
				
			};
			focusoutListener = new EventListener() {

				@Override
				public void handleEvent(Event evt) {
					commitEdit(textInput.getValue());
				}
				
			};
			((EventTarget)textInput).addEventListener("focusout", focusoutListener, false);
		}
		((EventTarget)cell.getOwnerDocument()).addEventListener("keyup",keyupListener,false);
		textInput.setValue(valueProperty().getValue().toString());
		textInput.focus();
		while (cell.hasChildNodes()) cell.removeChild(cell.getLastChild());
		cell.appendChild(textInput);
	}
	
	@Override
	public void endEditing() {
		((EventTarget)cell.getOwnerDocument()).removeEventListener("keyup",keyupListener,false);
		((EventTarget)textInput).removeEventListener("focusout", focusoutListener, false);
		cell.removeChild(textInput);
	}

}
