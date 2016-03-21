package com.belteshazzar.javafx.table;

import javafx.beans.property.Property;

@FunctionalInterface
public interface ValueFactory<S,T> {

	Property<T> createProperty(S s);

}
