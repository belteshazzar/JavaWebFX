package com.belteshazzar.javafx.treetable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLImageElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

import com.belteshazzar.javafx.WebScene;
import com.sun.webkit.dom.HTMLDocumentImpl;
import com.sun.webkit.dom.HTMLElementImpl;
import com.sun.webkit.dom.HTMLTableElementImpl;
import com.sun.webkit.dom.HTMLTableRowElementImpl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import netscape.javascript.JSObject;

public class TreeTable<T> {

	private HTMLTableElementImpl container;
	private HashMap<String, TreeTable<T>.Node> rowLookup;
	private SimpleObjectProperty<TreeTable<T>.Node> selected;
	private SimpleBooleanProperty editable;
	private JSObject jquery;
	private Node root;
	private ObservableList<Column<T>> columns;
	private int nextId = 0;
	private HTMLTableSectionElement header;

	public TreeTable(WebScene webScene, HTMLTableElement container) {
		this.container = (HTMLTableElementImpl)container;
		this.rowLookup = new HashMap<String,Node>();
		while (container.hasChildNodes()) container.removeChild(container.getFirstChild());
		this.header = (HTMLTableSectionElement)container.createTHead();
		this.selected = new SimpleObjectProperty<Node>();
		this.editable = new SimpleBooleanProperty(false);
		this.root = new Node();
		this.columns = FXCollections.observableArrayList();
		this.columns.addListener(new ListChangeListener<Column<T>>() {

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Column<T>> c) {
				updateHeader();
				updateNodes();
			}
			
		});
		webScene.addDeferredScript("$('#" + container.getId() + "').treetable({expandable: true})",(js) -> {
			this.jquery = (JSObject)js.eval("$('#" + container.getId() + "')");
		});
	}
	
	public void collapseAll() {
		jquery.call("treetable","collapseAll");
	}

	public void expandAll() {
		jquery.call("treetable","expandAll");
	}

	private void updateHeader() {
		while (this.header.hasChildNodes()) this.header.removeChild(this.header.getFirstChild());
		HTMLTableRowElement tr = (HTMLTableRowElement)container.getOwnerDocument().createElement("tr");
		for (Column<T> col : this.columns) {
			HTMLElement th = (HTMLElement)container.getOwnerDocument().createElement("th");
			th.setTextContent(col.titleProperty().getValue());
			tr.appendChild(th);
		}
		this.header.appendChild(tr);
	}
	
	private void updateNodes() {
		System.err.println("TreeTable.updateNodes NOT-IMPLEMENTED");
		
	}

	public Node getRoot() {
		return root;
	}
	
	private String createId() {
		return container.getId() + Integer.toString(++nextId);
	}
	
	public ObservableList<Column<T>> getColumns() {
		return columns;
	}

	
	public class Node {
		private final String id;
		private Node parent;
		private final List<Node> children;
		private final T item;
		private final JSObject js;
		private final List<CellInfo> cells;
		private final HTMLTableRowElementImpl row;
		
		Node() {
			this.id = createId();
			this.parent = null;
			this.children = new ArrayList<Node>();
			this.item = null;
			this.js = null;
			this.cells = null;
			row = null;
		}
		
		private Node(Node parent, T item, String imgFilename) {
			this.id = createId();
			this.parent = parent;
			this.children = new ArrayList<Node>();
			this.item = item;
			this.cells = new ArrayList<CellInfo>();
			// create the properties to get the initial values
			for (int colIndex=0 ; colIndex<columns.size() ; colIndex++) {
				cells.add(new CellInfo(colIndex,item));
			}
			// create the row
			String rowSrc = "<tr data-tt-id='" + id + "' data-tt-parent-id='" + parent.id + "'>"+rowContent(imgFilename)+"</tr>";
			jquery.call("treetable","loadBranch",parent.js,rowSrc);
			js = (JSObject)jquery.call("treetable","node",id);
			row = (HTMLTableRowElementImpl)((HTMLDocumentImpl)container.getOwnerDocument()).querySelector("tr[data-tt-id="+id+"]");
			// get the row cells and update the cell info
			for (int colIndex=0 ; colIndex<columns.size() ; colIndex++) {
				CellInfo ci = cells.get(colIndex);
				HTMLElement td = (HTMLElement)row.querySelector("#" + ci.tdId);
				// last child as first column will have indenting span added to it
				ci.el = (HTMLElementImpl)td.getLastChild();
				if (colIndex==0) {
					 ci.img = (HTMLImageElement)ci.el.getPreviousSibling();
				}
			}
			parent.children.add(this);
		}
		
		public void collapse() {
			jquery.call("treetable","collapseNode",this.id);
		}
	
		public void expand() {
			jquery.call("treetable","expandNode",this.id);
		}
		
		public void moveTo(Node newParent) {
			jquery.call("treetable","move",this.id,newParent.id);
			this.parent.children.remove(this);
			newParent.children.add(this);
			this.parent = newParent;
		}
		
		public void remove() {
			jquery.call("treetable", "removeNode", this.id);
			this.parent.children.remove(this);
		}
		
		public void reveal() {
			jquery.call("treetable", "reveal", this.id);
		}

		private String rowContent(String imgFilename) {
			StringBuilder sb = new StringBuilder();
			for (int i=0 ; i<columns.size() ; i++) {
				CellInfo ci = cells.get(i);
				sb.append("<td id='" + ci.tdId + "'>");
				if (i==0) {
					if (imgFilename==null) sb.append("<img src='' style='display: none;'>");
					else sb.append("<img src='" + imgFilename + "'>");
				}
				sb.append("<span>");
				sb.append(ci.prop.getValue());
				sb.append("</span></td>");
			}
			return sb.toString();
		}

		public T getItem() {
			return item;
		}

		public List<Node> children() {
			return Collections.unmodifiableList(children);
		}

		public Node appendChild(T item) {
			return new Node(this,item,null);
		}
		
		public Node appendChild(T item, String imgFile) {
			return new Node(this,item,imgFile);
		}

		public void setIcon(String string) {
			CellInfo ci = cells.get(0);
			ci.img.setSrc(string);
			ci.img.setAttribute("style", "");
		}

		private class CellInfo implements ChangeListener<String> {
			final int colIndex;
			final Column<T> col;
			final Property<String> prop;
			final String tdId;
			HTMLElementImpl el;
			HTMLImageElement img;
			
			public CellInfo(int colIndex, T item) {
				this.colIndex = colIndex;
				this.col = columns.get(colIndex);
				this.prop = col.cellValueFactoryProperty().getValue().createProperty(item);
				this.tdId = Node.this.id + "_" + colIndex;
				this.prop.addListener(this);

			}

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				el.setInnerHTML(newValue);
			}
		}

	}
	


}
