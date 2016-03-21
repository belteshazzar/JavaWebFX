package com.belteshazzar.javafx.table;

import java.lang.reflect.Field;

import javafx.beans.property.Property;

public class PropertyValueFactory<S,T> implements ValueFactory<S,T> {

	private String fieldName;
	private Field field;
	
	public PropertyValueFactory(String fieldName) {
		this.fieldName = fieldName;
		this.field = null;
	}

//	@Override
//	public void bind(HTMLTableElement html, HTMLTableRowElement row, HTMLTableCellElement cell, Object o) {
//		try {
//			if (field==null) initField(o);
//			Property<?> property = (Property<?>)field.get(o);
//			final HTMLTableCellElementImpl impl = (HTMLTableCellElementImpl)cell;
//			property.addListener(new ChangeListener<Object>() {
//	
//				@Override
//				public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
//					impl.setInnerHTML(newValue==null?"":newValue.toString());
//				}
//			});
//	
//			Object value = property.getValue();
//			impl.setInnerHTML(value==null?"":value.toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	private void initField(Object o) throws NoSuchFieldException {
		field = o.getClass().getDeclaredField(fieldName);
		if (!Property.class.isAssignableFrom(field.getType())) throw new NoSuchFieldException("field: " + fieldName + " of " + o.getClass() + " is not a Property");
		field.setAccessible(true);
	}

	@Override
	public Property<T> createProperty(S s) {
		if (s==null) return null;
		try {
			if (field==null) initField(s);
			@SuppressWarnings("unchecked")
			Property<T> property = (Property<T>)field.get(s);
			return property;
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
	}

}
