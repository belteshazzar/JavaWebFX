package com.belteshazzar.javafx.table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;

import com.sun.webkit.dom.HTMLTableElementImpl;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

public class Table<T> {

	private final HTMLTableElementImpl tableElement;
	private final ObservableList<Column<T,?>> columns;
	private final SortedList<Column<T,?>> sortedColumns;
	private final FilteredList<Column<T, ?>> filteredSortedColumns;
	private final ObservableList<T> items;
	private final SortedList<T> sortedItems;
	private final FilteredList<T> filteredSortedItems;
	private final ObjectProperty<T> selectedItem;
	private final ObjectProperty<HTMLTableRowElement> selectedRow;
	private final HTMLElement tableBodyElement;
	private final List<List<Cell<T,?>>> cells; // rows->cols
	private HTMLElement tableHeadElement;
	private final BooleanProperty showHeadings;
	
	public Table(HTMLTableElementImpl el) {
		this.tableElement = el;
		this.items = FXCollections.observableArrayList();
		this.sortedItems = new SortedList<T>(items);
		this.filteredSortedItems = new FilteredList<T>(sortedItems);
		this.filteredSortedItems.addListener(new ListChangeListener<T>() {

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends T> c) {
				updateRows();
				updateCells();
			}
			
		});
		this.selectedItem = new SimpleObjectProperty<T>();
		this.selectedRow = new SimpleObjectProperty<HTMLTableRowElement>();
		this.columns = FXCollections.observableArrayList();
		this.sortedColumns = new SortedList<Column<T,?>>(columns);
		this.filteredSortedColumns = new FilteredList<Column<T,?>>(sortedColumns);
		this.filteredSortedColumns.addListener(new ListChangeListener<Column<T,?>>() {

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Column<T, ?>> c) {
				updateColumns();
				updateCells();
			}
			
		});
		this.showHeadings = new SimpleBooleanProperty(true);
		this.showHeadings.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				updateHeadings();
			}
		});
		this.cells = new ArrayList<List<Cell<T,?>>>();
		HTMLCollection bods = tableElement.getTBodies();
		for (int i=0 ; i<bods.getLength() ; i++) {
			tableElement.removeChild(bods.item(i));
		}
		tableBodyElement = tableElement.createTBody();
		tableHeadElement = tableElement.createTHead();
		while (tableHeadElement.hasChildNodes()) tableHeadElement.removeChild(tableHeadElement.getFirstChild());
	}
	
	public ObjectProperty<Predicate<? super T>> filterProperty() {
		return this.filteredSortedItems.predicateProperty();
	}
	
	public ObjectProperty<Comparator<? super T>> sortProperty() {
		return this.sortedItems.comparatorProperty();
	}
	
	public ObjectProperty<Predicate<? super Column<T,?>>> columnFilterProperty() {
		return this.filteredSortedColumns.predicateProperty();
	}
	
	public ObjectProperty<Comparator<? super Column<T,?>>> columnSortProperty() {
		return this.sortedColumns.comparatorProperty();
	}

	public HTMLTableElement getHTMLElement() {
		return tableElement;
	}
		
	private HTMLTableRowElement createRow(int rowNum) {
		HTMLTableRowElement row = (HTMLTableRowElement)tableElement.getOwnerDocument().createElement("tr");
		((EventTarget)row).addEventListener("click", new EventListener() {

			@Override
			public void handleEvent(Event evt) {
				HTMLTableRowElement previous = selectedRow.getValue();
				if (previous!=null) {
					previous.setAttribute("class", "");
				}
				selectedRow.setValue(row);
				row.setAttribute("class", "selected");
				selectedItem.setValue(filteredSortedItems.get(rowNum));
			}
			
		}, false);
		return row;
	}

	
	
	private HTMLTableCellElement createCell() {
		return (HTMLTableCellElement)tableElement.getOwnerDocument().createElement("td");
	}

	private HTMLElement createHeaderCell() {
		return (HTMLElement)tableElement.getOwnerDocument().createElement("th");
	}

	private void updateRows() {
		int numRows = filteredSortedItems.size();
		int numCols = filteredSortedColumns.size();
		if (numRows!=tableBodyElement.getChildNodes().getLength()) {
			int diff = numRows - tableBodyElement.getChildNodes().getLength();
			while (diff<0) {
				tableBodyElement.removeChild(tableBodyElement.getLastChild());
				cells.remove(cells.size()-1);
				diff++;
			}
			while (diff>0) {
				HTMLTableRowElement row = createRow(tableBodyElement.getChildNodes().getLength());
				List<Cell<T,?>> rowCells = new ArrayList<Cell<T,?>>();
				for (int i=0 ; i<numCols ; i++) {
					HTMLTableCellElement cell = createCell();
					row.appendChild(cell);
					rowCells.add(filteredSortedColumns.get(i).getCellFactory().create(cell));
				}
				tableBodyElement.appendChild(row);
				cells.add(rowCells);
				diff--;
			}
		}
	}
	
	private void updateHeadings() {
		int numCols = filteredSortedColumns.size();
		while (tableHeadElement.hasChildNodes()) tableHeadElement.removeChild(tableHeadElement.getFirstChild());
		if (showHeadings.get()) {
			HTMLTableRowElement headerRow = (HTMLTableRowElement)tableElement.getOwnerDocument().createElement("tr");
			tableHeadElement.appendChild(headerRow);
			for (int c=0 ; c<numCols ; c++) {
				HTMLElement th = createHeaderCell();
				tableHeadElement.appendChild(th);
				th.setTextContent(filteredSortedColumns.get(c).getTitle());
			}
		}
	}
	
	private void updateColumns() {	
		int numRows = filteredSortedItems.size();
		int numCols = filteredSortedColumns.size();
		updateHeadings();
		for (int r=0 ; r<numRows ; r++) {
			HTMLTableRowElement row = (HTMLTableRowElement)tableBodyElement.getChildNodes().item(numRows);
			List<Cell<T,?>> rowCells = cells.get(r);
			for (int c=0 ; c<numCols ; c++) {
				if (rowCells.size()<=c) {
					row.appendChild(createCell());
					rowCells.add(null);
				}
				Column<T, ?> col =filteredSortedColumns.get(c);
				rowCells.set(c,col.getCellFactory().create((HTMLTableCellElement) row.getCells().item(c)));
			}		
			
			while (rowCells.size()>numCols) {
				row.removeChild(row.getLastChild());
				rowCells.remove(rowCells.size()-1);
			}
		}
	}
		


	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateCells() {
		for (int r=0 ; r<filteredSortedItems.size() ; r++) {
			for (int c=0 ; c<filteredSortedColumns.size() ; c++) {
				Cell<T,?> cell = cells.get(r).get(c);
				Column val = filteredSortedColumns.get(c);
				cell.column.setValue(val);
				cell.item.setValue(filteredSortedItems.get(r));
			}
		}
//		for (int r=0 ; r<numRows ; r++) {
//			T item = items.get(r);
//			HTMLTableRowElement row = (HTMLTableRowElement) tableBodyElement.getChildNodes().item(r);
//			for (int c=0 ; c<numCols ; c++) {
//				Column<T,?> col = columns.get(c);
//				HTMLTableCellElement cell = (HTMLTableCellElement) row.getChildNodes().item(c);
//				
//				CellFactory renderer = col.getCellFactory();
//				ObservableValue<?> value = col.getCellValueFactory().createObservable(item);
//				System.err.println("row: " + r + ", col: " + c +" (" + row.getChildNodes().getLength() + ") = " + value.getValue());
//				renderer.render(cell,value.getValue());
//			}
//		}
	}

	public ObservableList<T> getItems() {
		return items;
	}

	public ObservableList<Column<T,?>> getColumns() {
		return columns;
	}

	public BooleanProperty showHeadingsProperty() {
		return showHeadings;
	}
	
	public ObjectProperty<T> selectedItemProperty() {
		return selectedItem;
	}
}
