package com.belteshazzar.javafx.samples.controls.tree;

import java.util.Arrays;
import java.util.List;

import org.w3c.dom.html.HTMLElement;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.WebScene;
import com.belteshazzar.javafx.table.PropertyValueFactory;
import com.belteshazzar.javafx.tree.Tree;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;

public class TreeSample extends Application {

	@HTML
	private Tree<Employee> jstree_demo_div;
	@HTML
	private Property<Boolean> open;
	@HTML
	private HTMLElement selectedEmployee;

	public void initialize() {
		Employee emma = new Employee("Emma Jones", "Sales Department");
	    List<Employee> employees = Arrays.<Employee>asList(
	            new Employee("Ethan Williams", "Sales Department"),
	            emma,
	            new Employee("Michael Brown", "Sales Department"),
	            new Employee("Anna Black", "Sales Department"),
	            new Employee("Rodger York", "Sales Department"),
	            new Employee("Susan Collins", "Sales Department"),
	            new Employee("Mike Graham", "IT Support"),
	            new Employee("Judy Mayer", "IT Support"),
	            new Employee("Gregory Smith", "IT Support"),
	            new Employee("Jacob Smith", "Accounts Department"),
	            new Employee("Isabella Johnson", "Accounts Department"));

	    jstree_demo_div.setNodeValueFactory(new PropertyValueFactory<Employee,String>("name"));
	    Tree<Employee>.Node resources = jstree_demo_div.getRoot().appendChild("MyCompany Human Resources","root.png");
	    resources.editableProperty().set(false);
	    for (Employee e : employees) {
	    	boolean found = false;
	    	for (Tree<Employee>.Node dept : resources.children()) {
	    		if (dept.getText().equals(e.getDepartment())) {
	    			Tree<Employee>.Node emp = dept.appendChild(e);
	    			emp.setIconVisible(false);
	    			found = true;
	    			break;
	    		}
	    	}
	    	if (!found) {
	    		Tree<Employee>.Node dept = resources.appendChild(e.getDepartment(),"department.png");
	    		dept.editableProperty().set(false);
	    		Tree<Employee>.Node emp = dept.appendChild(e);
	    		emp.setIconVisible(false);
	    	}
	    }
	    
		Bindings.bindBidirectional(open, resources.openProperty());
		ChangeListener<String> selectedEmployeeListener = new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				selectedEmployee.setTextContent(newValue==null?"":newValue);
			}
			
		};
		jstree_demo_div.selectedProperty().addListener((ob,ov,nv) -> {
			if (ov!=null && ov.getItem()!=null) {
				ov.getItem().name.removeListener(selectedEmployeeListener);
			}
			if (nv.getItem()!=null) {
				nv.getItem().name.addListener(selectedEmployeeListener);
				selectedEmployee.setTextContent(nv.getItem().getName());
			} else {
				selectedEmployee.setTextContent("");
			}
		});
		jstree_demo_div.editableProperty().set(true);
		// emma doesn't like her name being changed
		emma.name.addListener((ob,ov,nv) -> {
			emma.name.setValue("Emma Jones");
		});
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new WebScene(getClass().getResource("tree_sample.html"), 300, 400));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

    public static class Employee {

        private final SimpleStringProperty name;
        private final SimpleStringProperty department;

        private Employee(String name, String department) {
            this.name = new SimpleStringProperty(name);
            this.department = new SimpleStringProperty(department);
        }

        public String getName() {
            return name.get();
        }

        public void setName(String fName) {
            name.set(fName);
        }

        public String getDepartment() {
            return department.get();
        }

        public void setDepartment(String fName) {
            department.set(fName);
        }
    }

}