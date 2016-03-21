package com.belteshazzar.javafx.table;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

public class NullValueFactory<S,T> implements ValueFactory<S, T> {

	@Override
	public Property<T> createProperty(S s) {
		return new SimpleObjectProperty<T>();
	}

}
