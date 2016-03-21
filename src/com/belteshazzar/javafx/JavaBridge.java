package com.belteshazzar.javafx;

public class JavaBridge {
	
	public Object controller;

	public JavaBridge(Object controller) {
		this.controller = controller;
	}
	
    public void log(Object text) {
        System.out.println(text);
    }
}

