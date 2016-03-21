package com.belteshazzar.javafx.table;

import org.w3c.dom.DOMException;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTextAreaElement;

import com.belteshazzar.javafx.WebScene;
import com.belteshazzar.javafx.input.RichText;
import com.sun.webkit.dom.HTMLElementImpl;
import com.sun.webkit.dom.HTMLTableCellElementImpl;
import com.sun.webkit.dom.KeyboardEventImpl;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class EditableHTMLCell<ItemType> extends Cell<ItemType,String>{

	private RichText editor = null;
	private EventListener keyupListener;
	private WebScene webScene;
	private EventListener focusoutListener;
	
	public EditableHTMLCell(HTMLTableCellElement cell, WebScene webScene) {
		super(cell);
		this.webScene = webScene;
	}
	
	@Override
	public void updateCell(HTMLTableCellElement cell, String value) {
		if (this.editingProperty().get()) {
			editor.content().setValue(value);
		} else {
			((HTMLTableCellElementImpl)cell).setInnerHTML((value==null?"":value.toString()));
		}
	}
	
	@Override
	public void startEditing(HTMLTableCellElement cell, String value) {
		editor = new RichText(webScene,cell);
		keyupListener = new EventListener() {

			@Override
			public void handleEvent(Event evt) {
				if (evt instanceof KeyboardEventImpl) {
					KeyboardEventImpl ke = (KeyboardEventImpl)evt;
					if (ke.getKeyCode()==27) {
						cancelEdit();
					}
				}
				
			}
			
		};
		focusoutListener = new EventListener() {

			@Override
			public void handleEvent(Event evt) {
				commitEdit(editor.content().getValue());
			}
			
		};
		((EventTarget)editor.getEditor()).addEventListener("focusout", focusoutListener, false);
		((EventTarget)cell.getOwnerDocument()).addEventListener("keyup",keyupListener,false);
		editor.getEditor().focus();
	}
	
	@Override
	public void endEditing() {
		((EventTarget)cell.getOwnerDocument()).removeEventListener("keyup",keyupListener,false);
		((EventTarget)editor.getEditor()).removeEventListener("focusout", focusoutListener, false);
		editor.destroy();
	}

}
