package com.belteshazzar.javafx.samples.controls.treetable;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.w3c.dom.events.Event;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.WebScene;
import com.belteshazzar.javafx.treetable.Column;
import com.belteshazzar.javafx.treetable.TreeTable;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TreeTableSample extends Application {

	@HTML
	private WebScene scene;
	
	@HTML
	private TreeTable<Employee> treetable;
	
	@HTML
	private void expandAll(Event ev) {
		treetable.expandAll();
	}
	
	@HTML
	private void collapseAll(Event ev) {
		treetable.collapseAll();
	}

	public void initialize() {
		
		Column<Employee> nameCol = new Column<Employee>("Name");
		nameCol.cellValueFactoryProperty().setValue((e) -> {
				return e.nameProperty();
		});
		Column<Employee> emailCol = new Column<Employee>("Email");
		emailCol.cellValueFactoryProperty().setValue((e) -> {
			return e.emailProperty();
		});
		treetable.getColumns().addAll(nameCol, emailCol);
		
		List<Employee> employees = Arrays.<Employee> asList(
				new Employee("Ethan Williams", "ethan@example.com", "Sales Department"),
				new Employee("Emma Jones", "emma@example.com", "Sales Department"),
				new Employee("Michael Brown", "michael@example.com", "Sales Department"),
				new Employee("Anna Black", "anna@example.com", "Sales Department"),
				new Employee("Rodger York", "rodger@example.com", "Sales Department"),
				new Employee("Susan Collins", "susan@example.com", "Sales Department"),
				new Employee("Mike Graham", "mike@example.com", "IT Support"),
				new Employee("Judy Mayer", "judy@example.com", "IT Support"),
				new Employee("Gregory Smith", "greg@example.com", "IT Support"),
				new Employee("Jacob Smith", "jacob@example.com", "Accounts Department"),
				new Employee("Isabella Johnson", "isabella@example.com", "Accounts Department"));
		for (Employee e : employees) {
			boolean found = false;
			for (TreeTable<Employee>.Node dept : treetable.getRoot().children()) {
				if (dept.getItem().getName().equals(e.getDepartment())) {
					TreeTable<Employee>.Node emp = dept.appendChild(e);
					found = true;
					break;
				}
			}
			if (!found) {
				TreeTable<Employee>.Node dept = treetable.getRoot().appendChild(new Employee(e.getDepartment(), "", ""),"folder.png");
				TreeTable<Employee>.Node emp = dept.appendChild(e);
			}
		}
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				employees.get(0).setName(employees.get(0).getName().equals("Jacen")?"Jaina":"Jacen");
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
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new WebScene(getClass().getResource("treetable_sample.html"), 500, 400));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public class Employee {

		private SimpleStringProperty name;
		private SimpleStringProperty email;
		private SimpleStringProperty department;

		public SimpleStringProperty nameProperty() {
			if (name == null) {
				name = new SimpleStringProperty(this, "name");
			}
			return name;
		}

		public SimpleStringProperty emailProperty() {
			if (email == null) {
				email = new SimpleStringProperty(this, "email");
			}
			return email;
		}

		public SimpleStringProperty departmentProperty() {
			if (department == null) {
				department = new SimpleStringProperty(this, "department");
			}
			return department;
		}

		private Employee(String name, String email, String department) {
			this.name = new SimpleStringProperty(name);
			this.email = new SimpleStringProperty(email);
			this.department = new SimpleStringProperty(department);
		}

		public String getName() {
			return name.get();
		}

		public void setName(String fName) {
			name.set(fName);
		}

		public String getEmail() {
			return email.get();
		}

		public void setEmail(String fName) {
			email.set(fName);
		}

		public String getDepartment() {
			return department.get();
		}

		public void setDepartment(String department) {
			this.department.set(department);
		}
	}

}