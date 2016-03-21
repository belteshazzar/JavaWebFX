package com.belteshazzar.javafx.table;

import org.w3c.dom.html.HTMLTableCellElement;

public interface CellFactory<S,T> {

	Cell<S,T> create(HTMLTableCellElement cell);

}
