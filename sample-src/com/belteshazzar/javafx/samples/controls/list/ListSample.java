package com.belteshazzar.javafx.samples.controls.list;

import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableCellElement;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.WebScene;
import com.belteshazzar.javafx.table.Cell;
import com.belteshazzar.javafx.table.Column;
import com.belteshazzar.javafx.table.Table;

import javafx.application.Application;
import javafx.stage.Stage;

public class ListSample extends Application {

	@HTML private Table<String> list;
	@HTML private HTMLElement value;
	
	@SuppressWarnings("unused")
	private void initialize() {
		list.showHeadingsProperty().setValue(false);
		Column<String,String> column = new Column<String,String>("Color");
		column.setCellFactory((cell) -> {
			return new ColorRectCell(cell);
		});
		list.getColumns().add(column);
	    list.getItems().addAll("chocolate", "salmon", "gold", "coral", "darkorchid",
	            "darkgoldenrod", "lightsalmon", "black", "rosybrown", "blue",
	            "blueviolet", "brown");
	    list.selectedItemProperty().addListener((ob,ov,nv) -> {
	    	value.setTextContent(nv);
	    	value.setAttribute("style", "color: " + nv);
	    });
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new WebScene(getClass().getResource("list_sample.html"), 300, 200));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	private class ColorRectCell extends Cell<String,String> {

		private HTMLElement rect;
		private HTMLElement label;
		
		private String style(String color) {
			return String.format("float: left; width: 100px; height: 20px; background-color: %s;",color);
		}

		public ColorRectCell(HTMLTableCellElement cell) {
			super(cell);
			rect = (HTMLElement)cell.getOwnerDocument().createElement("div");
			rect.setAttribute("style", style("green"));
			cell.appendChild(rect);
			label = (HTMLElement)cell.getOwnerDocument().createElement("div");
			label.setAttribute("style", "font: 18px tahoma");
			cell.appendChild(label);
		}

		@Override
		public void updateCell(HTMLTableCellElement cell, String value) {
			rect.setAttribute("style", style(value));
			label.setTextContent(value);
		}
		
	}
}