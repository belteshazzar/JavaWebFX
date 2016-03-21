package com.belteshazzar.javafx.input;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTextAreaElement;

import com.belteshazzar.javafx.WebScene;
import com.sun.webkit.dom.HTMLDocumentImpl;
import com.sun.webkit.dom.HTMLElementImpl;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import netscape.javascript.JSObject;

public class RichText {

	private final WebScene scene;
	private final SimpleStringProperty content;
	private final ReadOnlyIntegerWrapper selectionStart;
	private final ReadOnlyIntegerWrapper selectionEnd;
	private final HTMLElementImpl editor;
	public HTMLElementImpl getEditor() {
		return editor;
	}

	private final HTMLDocumentImpl document;
	private final ReadOnlyStringWrapper textAlign;
	private final ReadOnlyStringWrapper fontFamily;
	private final ReadOnlyStringWrapper fontStyle;
	private final ReadOnlyStringWrapper fontWeight;
	private final ReadOnlyStringWrapper fontSize;
	private final ReadOnlyStringWrapper color;
	private final ReadOnlyStringWrapper backgroundColor;
	private final ReadOnlyStringWrapper textDecoration;
	private EventListener changeListener;
	private EventListener inputListener;
	private EventListener selectionListener;
	private HTMLElementImpl source;

	private boolean isContainedBy(Node node, Node container) {
	    while (node!=null) {
	        if (node == container) {
	            return true;
	        }
	        node = node.getParentNode();
	    }
	    return false;
	}

	private Node getNextNode(Node node)
	{
	    if (node.getFirstChild()!=null) return node.getFirstChild();
	    while (node!=null) {
	        if (node.getNextSibling()!=null) return node.getNextSibling();
	        node = node.getParentNode();
	    }
	    return null;
	}

	@SuppressWarnings("unused")
	private List<Node> getNodesInRange(JSObject range)
	{
	    Node start = (Node)range.getMember("startContainer");
	    Node end = (Node)range.getMember("endContainer");
	    if (start==end) {
	    	return Arrays.asList(start);
	    }
	    Node commonAncestor = (Node)range.getMember("commonAncestorContainer");
	    return getNodesBetween(start,end,commonAncestor);
	}
	
	private List<Node> getNodesBetween(Node start, Node end, Node commonAncestor) {
	    List<Node> nodes = new LinkedList<Node>();
	    Node node;

	    // walk parent nodes from start to common ancestor
	    for (node = start.getParentNode(); node!=null ; node = node.getParentNode()) {
	        nodes.add(0,node);
	        if (node == commonAncestor) break;
	    }

	    // walk children and siblings from start until end is found
	    for (node = start; node!=null ; node = getNextNode(node))
	    {
	        nodes.add(node);
	        if (node == end) break;
	    }

	    return nodes;
	}

	public RichText(WebScene webScene, HTMLElement source) {
		this.scene = webScene;
		this.source = (HTMLElementImpl)source;
		this.document = (HTMLDocumentImpl)source.getOwnerDocument();
		selectionListener = new EventListener() {
			
			@Override
			public void handleEvent(Event evt) {
				JSObject selection = (JSObject)scene.getJSWindow().call("getSelection");
				int rangeCount = (int)selection.getMember("rangeCount");
				if (rangeCount==0) {
					selectionStart.setValue(-1);
					selectionEnd.setValue(-1);					
				} else {
					JSObject range = (JSObject)selection.call("getRangeAt", 0); // ignoring multiple
					Node commonAncestor = (Node)range.getMember("commonAncestorContainer");
					if (isContainedBy(commonAncestor,editor)) {
						Node beginsIn = (Node)range.getMember("startContainer");
						int beginOffset = (int)range.getMember("startOffset"); 
						Node endsIn = (Node)range.getMember("endContainer");
						int endOffset = (int)range.getMember("endOffset");
						if (beginsIn==editor) {
							beginsIn = editor.getChildNodes().item(beginOffset);
							beginOffset = 0;
						}
						if (endsIn==editor) {
							endsIn = editor.getChildNodes().item(endOffset);
							endOffset = 0;
						}
						if (beginsIn==endsIn) {
							Element beginsInElement;
							if (beginsIn==null) {
								beginsInElement = editor;
							} else {
								if (beginsIn instanceof Element) beginsInElement = (Element)beginsIn;
								else beginsInElement = (Element)beginsIn.getParentNode();								
							}
							JSObject style = (JSObject)scene.getJSWindow().call("getComputedStyle", beginsInElement, null);
							if (style!=null) {
								textAlign.setValue(style.getMember("textAlign").toString());
								fontFamily.setValue(style.getMember("fontFamily").toString());
								fontStyle.setValue(style.getMember("fontStyle").toString());
								fontWeight.setValue(style.getMember("fontWeight").toString());
								fontSize.setValue(style.getMember("fontSize").toString());
								color.setValue(style.getMember("color").toString());
								backgroundColor.setValue(style.getMember("backgroundColor").toString());
								textDecoration.setValue(style.getMember("textDecoration").toString());
							}
						}
						List<Node> nodes = getNodesBetween(editor.getFirstChild(),endsIn,editor);
						int offset = 0;
						int from = -1;
						int to = -1;
						for (Node n : nodes) {
							if (n==beginsIn) {
								from = offset + beginOffset;
							}
							if (n==endsIn) {
								to = offset + endOffset;
							}
							if (n.getNodeValue()!=null) {
								offset += n.getNodeValue().length();
							}
						}
						selectionStart.setValue(from);
						selectionEnd.setValue(to);
					} else {
						selectionStart.setValue(-1);
						selectionEnd.setValue(-1);
					}
				}
			}
		};
		((EventTarget)document).addEventListener("selectionchange", selectionListener, false);
		
		int width = this.source.getClientWidth();
		int height = this.source.getClientHeight();
		this.source.setHidden(true);
		
		// replace textarea with div
		editor = (HTMLElementImpl)source.getOwnerDocument().createElement("div");
		if (source instanceof HTMLTextAreaElement) {
			editor.setInnerHTML(source.getTextContent());
		} else {
			editor.setInnerHTML(((HTMLElementImpl)source).getInnerHTML());
		}
		cleanup(editor);
		editor.setId(source.getId()+"quill");
		editor.setAttribute("style", "width: " + width + "px; height: " + height + "px; border: 1px solid red;");
		source.getParentNode().appendChild(editor);

		// make it editable
		editor.setContentEditable("true");		

		content = new SimpleStringProperty();
		selectionStart = new ReadOnlyIntegerWrapper();
		selectionEnd = new ReadOnlyIntegerWrapper();
		textAlign = new ReadOnlyStringWrapper();
		fontFamily = new ReadOnlyStringWrapper();
		fontStyle = new ReadOnlyStringWrapper();
		fontWeight = new ReadOnlyStringWrapper();
		fontSize = new ReadOnlyStringWrapper();
		color = new ReadOnlyStringWrapper();
		backgroundColor = new ReadOnlyStringWrapper();
		textDecoration = new ReadOnlyStringWrapper();
		changeListener = new EventListener() {
			
			@Override
			public void handleEvent(Event evt) {
				if ((editor.getInnerHTML()==null && content.getValue()!=null)
					|| (!editor.getInnerHTML().equals(content.getValue()))) {
					content.setValue(editor.getInnerHTML());
				}
			}
			
		};
		((EventTarget)editor).addEventListener("change", changeListener, false);
		inputListener = new EventListener() {
			
			@Override
			public void handleEvent(Event evt) {
				if ((editor.getInnerHTML()==null && content.getValue()!=null)
						|| (!editor.getInnerHTML().equals(content.getValue()))) {
					content.setValue(editor.getInnerHTML());
				}
			}
			
		};
		((EventTarget)editor).addEventListener("input", inputListener, false);
		content.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.equals(editor.getInnerHTML())) {
					editor.setInnerHTML(newValue);
					cleanup(editor);
					String cleanedUp = editor.getInnerHTML();
					if (!cleanedUp.equals(newValue)) {
						content.setValue(editor.getInnerHTML()); // chould trigger editor#change instead of this
					}
				}
			}
		});
		content.setValue(editor.getInnerHTML());
	}
	
	private void cleanup(Node n) {
		String nv = n.getNodeValue();
		if (nv!=null) {
			n.setNodeValue(nv.trim().replaceAll("\\s+", " "));
		}
		NodeList children  = n.getChildNodes();
		for (int i=0 ; i<children.getLength() ; i++) {
			cleanup(children.item(i));
		}
	}

	public void destroy() {
		((EventTarget)document).removeEventListener("selectionchange", selectionListener, false);
		((EventTarget)editor).removeEventListener("change", changeListener, false);
		((EventTarget)editor).removeEventListener("input", inputListener, false);
		source.getParentNode().removeChild(editor);
		source.setHidden(false);
	}

	public Property<String> content() {
		return this.content;
	}
	
	public ReadOnlyProperty<Number> selectionStart() {
		return this.selectionStart;
	}

	public ReadOnlyProperty<Number> selectionEnd() {
		return this.selectionEnd;
	}
	
	public ReadOnlyProperty<String> textAlign() {
		return this.textAlign.getReadOnlyProperty();
	}

	public ReadOnlyProperty<String> fontFamily() {
		return this.fontFamily.getReadOnlyProperty();
	}

	public ReadOnlyProperty<String> fontStyle() {
		return this.fontStyle.getReadOnlyProperty();
	}
	
	public ReadOnlyProperty<String> fontWeight() {
		return this.fontWeight.getReadOnlyProperty();
	}
	
	public ReadOnlyProperty<String> fontSize() {
		return this.fontSize.getReadOnlyProperty();
	}
	
	public ReadOnlyProperty<String> color() {
		return this.color.getReadOnlyProperty();
	}
	
	public ReadOnlyProperty<String> backgroundColor() {
		return this.backgroundColor.getReadOnlyProperty();
	}
	
	public ReadOnlyProperty<String> textDecoration() {
		return this.textDecoration.getReadOnlyProperty();
	}

	
	// document.execCommand interfaces
	
	public void backColor(String color) {
		document.execCommand("backColor", false, color);
	}
	
	public void bold() {
		document.execCommand("bold", false, null);
	}
	
	public void copy() {
		document.execCommand("copy", false, null);
	}
	
	public void createLink(String url) {
		document.execCommand("createLink", false, url);
	}
	
	public void cut() {
		document.execCommand("cut", false, null);
	}

	public void decreaseFontSize() {
		document.execCommand("decreateFontSize", false, null);
	}

	public void delete() {
		document.execCommand("delete", false, null);
	}

	public void enableInlineTableEditing() {
		document.execCommand("enableInlineTableEditing", false, null);
	}

	public void enableObjectResizing() {
		document.execCommand("enableObjectResizing", false, null);
	}

	public void fontName(String fontName) {
		document.execCommand("fontName", false, fontName);
	}

	public void fontSize(int fontSize) {
		document.execCommand("fontSize", false, String.valueOf(fontSize));
	}

	public void foreColor(String foreColor) {
		document.execCommand("foreColor", false, foreColor);
	}

	public void formatBlock() {
		document.execCommand("formatBlock", false, null);
	}

	public void forwardDelete() {
		document.execCommand("forwardDelete", false, null);
	}

	public void heading(String heading) {
		document.execCommand("heading", false, heading);
	}

	public void hiliteColor(String hiliteColor) {
		document.execCommand("hiliteColor", false, hiliteColor);
	}

	public void increaseFontSize() {
		document.execCommand("increaseFontSize", false, null);
	}

	public void indent() {
		document.execCommand("indent", false, null);
	}

	public void insertBrOnReturn() {
		document.execCommand("insertBrOnReturn", false, null);
	}

	public void insertHorizontalRule() {
		document.execCommand("insertHorizontalRule", false, null);
	}

	public void insertHTML(String html) {
		document.execCommand("insertHTML", false, html);
	}

	public void insertImage(String url) {
		document.execCommand("insertImage", false, url);
	}

	public void insertOrderedList() {
		document.execCommand("insertOrderedList", false, null);
	}

	public void insertUnorderedList() {
		document.execCommand("insertUnorderedList", false, null);
	}

	public void insertParagraph() {
		document.execCommand("insertParagraph", false, null);
	}

	public void insertText(String text) {
		document.execCommand("insertText", false, text);
	}

	public void italic() {
		document.execCommand("italic", false, null);
	}

	public void justifyCenter() {
		document.execCommand("justifyCenter", false, null);
	}

	public void justifyFull() {
		document.execCommand("justifyFull", false, null);
	}
	
	public void justifyLeft() {
		document.execCommand("justifyLeft", false, null);
	}
	
	public void justifyRight() {
		document.execCommand("justifyRight", false, null);
	}
	
	public void outdent() {
		document.execCommand("outdent", false, null);
	}
	
	public void paste() {
		document.execCommand("paste", false, null);
	}
	
	public void redo() {
		document.execCommand("redo", false, null);
	}
	
	public void removeFormat() {
		document.execCommand("removeFormat", false, null);
	}
	
	public void selectAll() {
		document.execCommand("selectAll", false, null);
	}
	
	public void strikeThrough() {
		document.execCommand("strikeThrough", false, null);
	}
	
	public void subscript() {
		document.execCommand("subscript", false, null);
	}
	
	public void superscript() {
		document.execCommand("superscript", false, null);
	}
	
	public void underline() {
		document.execCommand("underline", false, null);
	}
	
	public void undo() {
		document.execCommand("undo", false, null);
	}
	
	public void unlink() {
		document.execCommand("unlink", false, null);
	}

	public void styleWithCSS(boolean useCSS) {
		document.execCommand("styleWithCSS", false, Boolean.toString(useCSS));		
	}


}
