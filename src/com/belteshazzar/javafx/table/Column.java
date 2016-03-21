package com.belteshazzar.javafx.table;

import org.w3c.dom.html.HTMLTableCellElement;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Column<S,T> {
	
	private String title;
	private ValueFactory<S,T> cellValueFactory;
	private CellFactory<S, T> cellFactory;
	private final BooleanProperty editable;

	public Column(String text) {
		this.title = text;
		this.cellValueFactory = new ValueFactory<S,T>() {

			@SuppressWarnings("unchecked")
			@Override
			public Property<T> createProperty(S s) {
				return new SimpleObjectProperty<T>((T) s);
			}
			
		};
		this.cellFactory = new CellFactory<S,T>() {

			@Override
			public Cell<S,T> create(HTMLTableCellElement cell) {
				return new TextCell<S,T>(cell);
			}
			
		};
		this.editable = new SimpleBooleanProperty(false);
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public void setCellValueFactory(ValueFactory<S, T> cellValueFactory) {
		this.cellValueFactory = cellValueFactory;
	}

	public CellFactory<S,T> getCellFactory() {
		return cellFactory;
	}
	
	public void setCellFactory(CellFactory<S,T> cellFactory) {
		this.cellFactory = cellFactory;
	}
	

	public ValueFactory<S,T> getCellValueFactory() {
		return cellValueFactory;
	}
	
	public BooleanProperty editableProperty() {
		return editable;
	}
}
