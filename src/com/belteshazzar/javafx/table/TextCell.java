package com.belteshazzar.javafx.table;

import org.w3c.dom.html.HTMLTableCellElement;

public class TextCell<ItemType,ItemPropertyType> extends Cell<ItemType,ItemPropertyType>{

	public TextCell(HTMLTableCellElement cell) {
		super(cell);
	}
	
	@Override
	public void updateCell(HTMLTableCellElement cell, ItemPropertyType value) {
		cell.setTextContent((value==null?"":value.toString()));
	}
	
	@Override
	public void startEditing(HTMLTableCellElement cell, ItemPropertyType value) {
	}

	@Override
	public void endEditing() {
	}

}
