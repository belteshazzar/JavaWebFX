package com.belteshazzar.javafx.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.html.HTMLElement;

import com.belteshazzar.javafx.WebScene;
import com.belteshazzar.javafx.table.PropertyValueFactory;
import com.belteshazzar.javafx.util.JSUtils;
import com.sun.webkit.dom.HTMLElementImpl;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import netscape.javascript.JSObject;

public class Tree<T> {

	private HTMLElementImpl container;
	private JSObject jstree;
	private Node root;
	private int nextId = 0;
	Map<String,Node> nodeLookup;
	ObjectProperty<Node> selected;
	private PropertyValueFactory<T, String> propertyValueFactory;
	private BooleanProperty editable;

	public Tree(WebScene webScene, HTMLElement container) {
		this.container = (HTMLElementImpl)container;
		this.nodeLookup = new HashMap<String,Node>();
		this.root = new Node();
		while (container.hasChildNodes()) container.removeChild(container.getFirstChild());
		this.selected = new SimpleObjectProperty<Node>();
		this.editable = new SimpleBooleanProperty(false);
		webScene.addDeferredScript("$('#" + container.getId() + "').jstree({ 'core' : { 'check_callback' : true, 'multiple': true}, 'plugins' : [ 'wholerow' ] })",(js) -> {
			this.jstree = (JSObject)js.eval("$('#" + container.getId() + "').jstree(true)");
			JSObject core = (JSObject)((JSObject)this.jstree.getMember("settings")).getMember("core");
			JSObject fn = JSUtils.createFunction(jstree,"function(op,node,parent,pos) { return _this.java.check_callback(op,node,parent,pos); }");
			core.setMember("check_callback", fn);
			this.jstree.setMember("java", new TreeEditCallback<T>(this));
			js.setMember("java", new TreeEventCallback<T>(this));
			js.eval("(function() { var _this = this; this.on('select_node.jstree',function(ev,data) { _this.java.selected(data.selected); }) }).call(this)");
			js.eval("(function() { var _this = this; this.on('open_node.jstree',function(ev,data) { _this.java.opened(data.node.id); }) }).call(this)");
			js.eval("(function() { var _this = this; this.on('close_node.jstree',function(ev,data) { _this.java.closed(data.node.id); }) }).call(this)");
			js.eval("(function() { var _this = this; this.on('changed.jstree',function(ev,data) { _this.java.changed(data.node.id); }) }).call(this)");
			js.eval("(function() { var _this = this; this.on('show_node.jstree',function(ev,data) { _this.java.shown(data.node.id); }) }).call(this)");
			js.eval("(function() { var _this = this; this.on('hide_node.jstree',function(ev,data) { _this.java.hidden(data.node.id); }) }).call(this)");
			js.eval("(function() { var _this = this; this.off('dblclick').on('dblclick','.jstree-anchor',function() { var instance = $.jstree.reference(this); var node = instance.get_node(this); _this.java.dblclick(node.id);  }); }).call(this)");
		});
	}
	
	public Node getRoot() {
		return root;
	}

	public ReadOnlyObjectProperty<Node> selectedProperty() {
		return this.selected;
	}

	public void setNodeValueFactory(PropertyValueFactory<T, String> propertyValueFactory) {
		this.propertyValueFactory = propertyValueFactory;
	}
	
	public BooleanProperty editableProperty() {
		return editable;
	}

	public class Node {
		String id;
		Property<String> text;
		T item;
		Node parent;
		List<Node> children;
		
		BooleanProperty open;
		HTMLElement el;
		private BooleanProperty editable;
		
		Node() {
			this.id = "#";
			this.parent = null;
			this.children = new ArrayList<Node>();
			this.editable = new ReadOnlyBooleanWrapper(false);
		}
		
		Node(Node parent, String text, T item, String img) {
			this.parent = parent;
			if (text!=null) {
				this.text = new SimpleStringProperty(text);
				this.item = null;
			} else if (item!=null) {
				this.text = propertyValueFactory.createProperty(item);
				this.item = item;
			} else {
				throw new IllegalArgumentException("text or item must be non-null");
			}
			this.editable = new SimpleBooleanProperty(true);
			this.text.addListener((ob,ov,nv) -> {
				setText(nv);
			});
			this.children = new ArrayList<Node>();
			open = new SimpleBooleanProperty();
			open.addListener((ob,ov,nv) -> {
				if (nv) open();
				else close();
			});
			JSObject json = JSUtils.createJSON(jstree,"{'id': '"+container.getId()+(nextId++)+"', 'text' : '"+this.text.getValue()+"'}");
			this.id = (String)jstree.call("create_node",(parent==null?"#":parent.id),json,"last");
			nodeLookup.put(this.id, this);
			if (img!=null) setIcon(img);
			if (parent!=null) {
				parent.children.add(this);
			}
		}
		
		public Node appendChild(String text) {
			return new Node(this,text,null,null);
		}

		public Node appendChild(T item) {
			return new Node(this,null,item,null);
		}

		public Node appendChild(String text, String img) {
			return new Node(this,text,null,img);
		}

		public Node appendChild(T item, String img) {
			return new Node(this,null,item,img);
		}

		public BooleanProperty openProperty() {
			return open;
		}
		
		private void open() {
			jstree.call("open_node",this.id);
		}

		private void close() {
			jstree.call("close_node", this.id);
		}

		public void setIcon(String string) {
			jstree.call("set_icon", id, string);
		}

		public void setIconVisible(boolean isVisible) {
			if (isVisible) jstree.call("show_icon", id);
			else jstree.call("hide_icon", id);
		}

		public String getId() {
			return id;
		}
		
		public String getText() {
			return text.getValue();
		}
		
		public void setText(String text) {
			jstree.call("rename_node", id, text);
		}
		
		public T getItem() {
			return item;
		}
		
		public void startEditing() {
			JSObject cb = JSUtils.createFunction(jstree,"function(node,success,cancelled) { if (!cancelled) _this.java.changed(node.id,node.text); }");
			jstree.call("edit", id, text.getValue(), cb);
		}

		public List<Node> children() {
			return Collections.unmodifiableList(children);
		}

		public BooleanProperty editableProperty() {
			return this.editable;
		}
	}
	
}
