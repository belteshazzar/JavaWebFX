package com.belteshazzar.javafx.samples.controls.table;

import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLTableCellElement;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.WebScene;
import com.belteshazzar.javafx.table.ButtonCell;
import com.belteshazzar.javafx.table.Cell;
import com.belteshazzar.javafx.table.CellFactory;
import com.belteshazzar.javafx.table.Column;
import com.belteshazzar.javafx.table.EditableTextCell;
import com.belteshazzar.javafx.table.PropertyValueFactory;
import com.belteshazzar.javafx.table.Table;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

public class TableExampleController {

	@HTML
	WebScene scene;

	@HTML
	private Table<Person> table;
	
	@HTML
	private HTMLInputElement firstName;

	@HTML
	private HTMLInputElement lastName;

	@HTML
	private HTMLInputElement email;

	@HTML(name="sort")
	private Property<String> sortRadio;

	@HTML
	private void add(Event ev) {
		Person p = new Person(firstName.getValue(),lastName.getValue(),email.getValue());
		firstName.setValue("");
		lastName.setValue("");
		email.setValue("");
		table.getItems().add(p);
	};

	private Timer timer;

	@SuppressWarnings("unchecked")
	public void initialize() {
		
		Column<Person,String> firstNameCol = new Column<Person,String>("First Name");
		firstNameCol.setCellValueFactory(new PropertyValueFactory<Person,String>("firstName"));
		firstNameCol.setCellFactory(new CellFactory<Person,String>() {

			@Override
			public Cell<Person, String> create(HTMLTableCellElement cell) {
				return new EditableTextCell<Person>(cell);
			}
			
		});
		Column<Person,String> lastNameCol  = new Column<Person,String>("Last Name");
		lastNameCol.setCellValueFactory(new PropertyValueFactory<Person,String>("lastName"));
		Column<Person,String> emailCol  = new Column<Person,String>("Email");
		emailCol.setCellValueFactory(new PropertyValueFactory<Person,String>("email"));
		Column<Person,String> removeCol  = new Column<Person,String>("Remove");
		removeCol.setCellFactory(new CellFactory<Person,String>() {

			@Override
			public Cell<Person,String> create(HTMLTableCellElement cell) {
				return new ButtonCell<Person,String>(cell,"Remove", (p) -> {
					table.getItems().remove(p);
				});
			}
			
		});
		
		table.sortProperty().setValue(new Comparator<Person>() {

			@Override
			public int compare(Person p1, Person p2) {
				return p1.getLastName().compareTo(p2.getLastName());
			}
		});
		
		table.getColumns().addAll(firstNameCol, lastNameCol, emailCol, removeCol);
				
		Person changing = new Person("Jacen", "Solo", "jacob.smith@example.com");
		table.getItems().addAll(
			    changing,
			    new Person("Isabella", "Johnson", "isabella.johnson@example.com"),
			    new Person("Ethan", "Williams", "ethan.williams@example.com"),
			    new Person("Emma", "Jones", "emma.jones@example.com"),
			    new Person("Michael", "Brown", "michael.brown@example.com")
			);
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				changing.setFirstName(changing.getFirstName().equals("Jacen")?"Jaina":"Jacen");
			}
					
		},1000,1000);
		scene.getWindow().addEventHandler(WindowEvent.ANY, new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				if (event.getEventType()==WindowEvent.WINDOW_CLOSE_REQUEST) {
					timer.cancel();
				}
			}
		});
		sortRadio.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.equals("ascending")) {
					table.sortProperty().setValue(new Comparator<Person>() {
	
						@Override
						public int compare(Person p1, Person p2) {
							return p1.getLastName().compareTo(p2.getLastName());
						}
					});
				} else {
					table.sortProperty().setValue(new Comparator<Person>() {
						
						@Override
						public int compare(Person p1, Person p2) {
							return p2.getLastName().compareTo(p1.getLastName());
						}
					});
					
				}
			}
		});
	}
}
