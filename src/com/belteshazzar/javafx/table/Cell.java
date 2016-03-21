package com.belteshazzar.javafx.table;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLTableCellElement;

import com.belteshazzar.javafx.WebScene;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public abstract class Cell<ItemType,ItemPropertyType> {

	final ObjectProperty<Column<ItemType,ItemPropertyType>> column;
	final ObjectProperty<ItemType> item;
	Property<ItemPropertyType> valueSource;
	final Property<ItemPropertyType> value;
	protected final HTMLTableCellElement cell;
	final BooleanProperty editing;
	private final ChangeListener<ItemPropertyType> sourceValueListener; 
	BooleanProperty editable;
	private final ChangeListener<Boolean> editableListener;
	private WebScene webScene;
	
	public Cell(HTMLTableCellElement cell) {
		this.cell = cell;
		((EventTarget)this.cell).addEventListener("dblclick", new EventListener() {

			@Override
			public void handleEvent(Event evt) {
				editing.set(true);
			}
		}, false);
		this.value = new SimpleObjectProperty<ItemPropertyType>();
		this.value.addListener(new ChangeListener<ItemPropertyType>() {

			@Override
			public void changed(ObservableValue<? extends ItemPropertyType> observable, ItemPropertyType oldValue, ItemPropertyType newValue) {
				updateCell(cell,newValue);
			}
			
		});
		this.editableListener = new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				editable.setValue(newValue);
				
			}
		};
		this.column = new SimpleObjectProperty<Column<ItemType,ItemPropertyType>>();
		this.column.addListener(new ChangeListener<Column<ItemType,ItemPropertyType>>() {


			@Override
			public void changed(ObservableValue<? extends Column<ItemType, ItemPropertyType>> observable, Column<ItemType, ItemPropertyType> oldValue,
					Column<ItemType, ItemPropertyType> newValue) {
				if (oldValue!=null) oldValue.editableProperty().removeListener(editableListener);
				if (newValue!=null) newValue.editableProperty().addListener(editableListener);
				if (item.getValue()==null) {
					updateCell(cell,null);
				} else {
					if (valueSource!=null) {
						valueSource.removeListener(sourceValueListener);
					}
					valueSource = newValue.getCellValueFactory().createProperty(item.getValue());
					valueSource.addListener(sourceValueListener);
					value.setValue(valueSource.getValue());
					updateCell(cell,value.getValue());
				}
			}
			
		});
		this.sourceValueListener = new ChangeListener<ItemPropertyType>() {

			@Override
			public void changed(ObservableValue<? extends ItemPropertyType> observable, ItemPropertyType oldValue,
					ItemPropertyType newValue) {
				value.setValue(newValue);
			}
		};
		this.item = new SimpleObjectProperty<ItemType>();
		this.item.addListener(new ChangeListener<ItemType>() {

			@Override
			public void changed(ObservableValue<? extends ItemType> observable, ItemType oldValue, ItemType newValue) {
				if (column.getValue()==null) {
					updateCell(cell,null);
				} else {
					if (valueSource!=null) {
						valueSource.removeListener(sourceValueListener);
					}
					valueSource = column.getValue().getCellValueFactory().createProperty(newValue);
					valueSource.addListener(sourceValueListener);
					value.setValue(valueSource.getValue());
					updateCell(cell,value.getValue());
				}
				
			}
		});
		this.editing = new SimpleBooleanProperty(false);
		this.editing.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) startEditing(cell, value.getValue());
			}
		});
		this.editable = new SimpleBooleanProperty(false);
	}

	public WebScene getWebScene() {
		return webScene;
	}

	public void cancelEdit() {
		editing.set(false);
		endEditing();
		updateCell(cell,value.getValue());
	}
	
	public void commitEdit(ItemPropertyType newValue) {
		editing.set(false);
		endEditing();
		value.setValue(newValue);
		updateCell(cell,newValue);
	}

	public ReadOnlyProperty<ItemPropertyType> valueProperty() {
		return value;
	}
	
	public ReadOnlyProperty<ItemType> itemProperty() {
		return item;
	}
	
	public ReadOnlyBooleanProperty editingProperty() {
		return editing;
	}
	
	public ReadOnlyBooleanProperty editableProperty() {
		return editable;
	}
	
	public abstract void updateCell(HTMLTableCellElement cell, ItemPropertyType value);

	public void startEditing(HTMLTableCellElement cell, ItemPropertyType value) {}

	public void endEditing() {}

}
