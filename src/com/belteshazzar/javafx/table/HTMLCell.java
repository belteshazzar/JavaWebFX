package com.belteshazzar.javafx.table;

import org.w3c.dom.html.HTMLTableCellElement;

import com.sun.webkit.dom.HTMLTableCellElementImpl;

public class HTMLCell<ItemType> extends Cell<ItemType,String> {

	public HTMLCell(HTMLTableCellElement cell) {
		super(cell);
	}
	
	@Override
	public void updateCell(HTMLTableCellElement cell, String value) {
		((HTMLTableCellElementImpl)cell).setInnerHTML(value==null?"":value.toString());
	}

}
