package com.belteshazzar.javafx.samples.controls.richtexttablecell;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;

public class Note {

    private final SimpleStringProperty title;
    private final SimpleStringProperty text;
 
    public Note(String title, String text) {
        this.title = new SimpleStringProperty(title);
        this.text = new SimpleStringProperty(text);
    }
 
    public Property<String> title() {
    	return this.title;
    }
    
    public String getTitle() {
        return title.get();
    }
    public void setTitle(String title) {
        this.title.set(title);
    }
    
    public Property<String> text() {
    	return this.text;
    }
        
    public String getText() {
        return text.get();
    }
    public void setText(String text) {
        this.text.set(text);
    }
        
}
