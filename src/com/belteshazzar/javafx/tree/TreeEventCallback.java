package com.belteshazzar.javafx.tree;

import netscape.javascript.JSObject;

/**
 * Internal use only.
 *
 * @param <T>
 */
public class TreeEventCallback<T> {
	/**
	 * 
	 */
	private final Tree<T> tree;

	/**
	 * @param tree
	 */
	TreeEventCallback(Tree<T> tree) {
		this.tree = tree;
	}

	public void selected(JSObject selected) {
		tree.selected.setValue(this.tree.nodeLookup.get(selected.toString()));
	}
	
	public void opened(String id) {
		tree.nodeLookup.get(id).open.set(true);
	}
	
	public void closed(String id) {
		tree.nodeLookup.get(id).open.set(false);
	}
	
	public void changed(String id) {
		System.err.println("unhandled jstree id: " + id + " changed event");
	}

	public void shown(String id) {
		System.err.println("unhandled jstree id: " + id + " shown event");
	}

	public void hidden(String id) {
		System.err.println("unhandled jstree id: " + id + " hidden event");
	}
	
	public void dblclick(String id) {
		if (this.tree.editableProperty().get()) {
			Tree<T>.Node node = this.tree.nodeLookup.get(id);
			if (node.editableProperty().get()) node.startEditing();
		}
	}
}