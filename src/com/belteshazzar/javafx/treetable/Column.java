package com.belteshazzar.javafx.treetable;

import com.belteshazzar.javafx.table.ValueFactory;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Column<T> {

	private Property<String> title;
	private Property<ValueFactory<T,String>> cellValueFactory;
	
	public Column(String title) {
		this.title = new SimpleStringProperty(title);
		this.cellValueFactory = new SimpleObjectProperty<ValueFactory<T,String>>(new ValueFactory<T,String>() {

			@Override
			public Property<String> createProperty(T t) {
				return new SimpleStringProperty(t.toString());
			}
			
		});

	}
	
	public Property<String> titleProperty() {
		return title;
	}
	
	public Property<ValueFactory<T,String>> cellValueFactoryProperty() {
		return cellValueFactory;
	}

}
