package com.belteshazzar.javafx.tree;

import netscape.javascript.JSObject;

public class TreeEditCallback<T> {

	private Tree<T> tree;

	public TreeEditCallback(Tree<T> tree) {
		this.tree = tree;
	}

	public void changed(String id, String updatedText) {
		tree.nodeLookup.get(id).text.setValue(updatedText);
	}
	
	public boolean check_callback(String op, JSObject node, JSObject parent, int pos) {
		if (op.equals("create_node")) return true;
		if (!tree.editableProperty().get()) return false;
		return tree.nodeLookup.get(node.getMember("id")).editableProperty().get();
	}
}
