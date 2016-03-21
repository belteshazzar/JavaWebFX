package com.belteshazzar.javafx.samples.controls.richtexttablecell;

import org.w3c.dom.html.HTMLTableCellElement;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.WebScene;
import com.belteshazzar.javafx.table.Cell;
import com.belteshazzar.javafx.table.CellFactory;
import com.belteshazzar.javafx.table.Column;
import com.belteshazzar.javafx.table.EditableHTMLCell;
import com.belteshazzar.javafx.table.HTMLCell;
import com.belteshazzar.javafx.table.PropertyValueFactory;
import com.belteshazzar.javafx.table.Table;

import javafx.fxml.LoadException;

public class RichTextEditingExampleController {

	@HTML
	WebScene scene;

	@HTML
	private Table<Note> table;
	
	public void initialize() {

		Column<Note,String> titleCol = new Column<Note,String>("Title");
		titleCol.setCellValueFactory(new PropertyValueFactory<Note,String>("title"));
		
		Column<Note,String> textCol  = new Column<Note,String>("Text");
		textCol.setCellValueFactory(new PropertyValueFactory<Note,String>("text"));
		textCol.setCellFactory(new CellFactory<Note,String>() {

			@Override
			public Cell<Note, String> create(HTMLTableCellElement cell) {
				return new EditableHTMLCell<Note>(cell,scene);
			}
			
		});

		
		table.getColumns().addAll(titleCol,textCol);
		
		table.getItems().addAll(
		    new Note("first note", "This is my <b>first</b> note with <u>rich content</u>"),
		    new Note("second note", "This is a <i>second</i> note with some other <b>rich</b> content.")
		);
	}
	
}
