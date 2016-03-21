package com.belteshazzar.javafx.input;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLTextAreaElement;

import com.belteshazzar.javafx.DeferredScriptCallback;
import com.belteshazzar.javafx.WebScene;
import com.sun.webkit.dom.HTMLElementImpl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import netscape.javascript.JSObject;

public class QuillRichText {

	protected HTMLTextAreaElement textarea;
	protected WebScene scene;
	protected JSObject editor;
	private SimpleStringProperty content;
	private Property<Number> selectionStart;
	private Property<Number> selectionEnd;

	public QuillRichText(WebScene webScene, HTMLTextAreaElement textarea) {
		this.scene = webScene;
		this.textarea = textarea;
		
		HTMLElementImpl impl = (HTMLElementImpl)textarea;
		int width = impl.getClientWidth();
		int height = impl.getClientHeight();
		impl.setHidden(true);

		// replace textarea with div
		HTMLElementImpl div = (HTMLElementImpl)textarea.getOwnerDocument().createElement("div");
		div.setInnerHTML(textarea.getTextContent());
		div.setId(textarea.getId()+"quill");
		div.setAttribute("style", "width: " + width + "px; height: " + height + "px; border: 1px solid red;");
		textarea.getParentNode().appendChild(div);
		
		// convert to quill
		String script = "(function() {"
				+ "  function triggerEvent(elem,type) {"
			    + "    var event = document.createEvent('Event');"
			    + "    event.initEvent(type,true,true);"
			    + "    elem.dispatchEvent(event);"
			    + "  }"
				+ "  var textarea = document.getElementById('" + textarea.getId() + "');"
				+ "  var quill = new Quill('#" + textarea.getId() + "quill');"
				+ "  quill.on('text-change', function(delta,source) {"
				+ "    textarea.value = quill.getHTML();"
				+ "    triggerEvent(textarea,'change');"
				+ "  });"
				+ "  quill.on('selection-change', function(range) {"
				+ "    textarea.setAttribute('start',(range?range.start:-1));"
				+ "    textarea.setAttribute('end',(range?range.end:-1));"
				+ "    triggerEvent(textarea,'selection');"
				+ "  });"
				+ "  return quill;"
				+ "})();";
		scene.addDeferredScript(script,new DeferredScriptCallback() {
			
			@Override
			public void scriptExecuted(JSObject js) {
				editor = js;
				content.addListener(new ChangeListener<String>() {

					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						String nv = newValue.replaceAll("'", "\\\\'");
						String ov = (String)editor.call("getHTML");
						if (!nv.equals(ov)) {
							editor.call("setHTML", nv);
							editor.call("focus");
						}
					}
				});
				content.setValue(textarea.getValue());
			}
		});

		content = new SimpleStringProperty();
		selectionStart = new SimpleIntegerProperty();
		selectionEnd = new SimpleIntegerProperty();
		((EventTarget)textarea).addEventListener("change", new EventListener() {
			
			@Override
			public void handleEvent(Event evt) {
				if ((textarea.getValue()==null && content.getValue()!=null)
					|| (!textarea.getValue().equals(content.getValue()))) {
					content.setValue(textarea.getValue());
				}
			}
			
		}, true);
		((EventTarget)textarea).addEventListener("selection", new EventListener() {
			
			@Override
			public void handleEvent(Event evt) {
				selectionStart.setValue(Integer.parseInt(textarea.getAttribute("start")));
				selectionEnd.setValue(Integer.parseInt(textarea.getAttribute("end")));
			}
			
		}, true);
	}
	
	public Property<String> content() {
		return this.content;
	}
	
	public Property<Number> selectionStart() {
		return this.selectionStart;
	}

	public Property<Number> selectionEnd() {
		return this.selectionEnd;
	}
	
	public void formatText(Format format) {
		editor.call("formatText", selectionStart.getValue().intValue(), selectionEnd.getValue().intValue(), format.toJS(editor));
	}

	public void formatText(int start, int end, Format format) {
		editor.call("formatText", start, end, format.toJS(editor));
	}
	
	public void formatLine(Format format) {
		editor.call("formatLine", selectionStart.getValue().intValue(), selectionEnd.getValue().intValue(), format.toJS(editor));		
	}

	public void formatLine(int start, int end, Format format) {
		editor.call("formatLine", start, end, format.toJS(editor));		
	}
	
	public static class Format {
		public Boolean bold;
		public Boolean italic;
		public Boolean strike;
		public Boolean underline;
		public String font;
		public Integer size;
		public String color;
		public String background;
		public String image;
		public String link;
		public String bullet;
		public String list;
		public String align;
		
		public JSObject toJS(JSObject context) {
			JSObject js = (JSObject)context.eval("new Object()");
			if (bold!=null) js.setMember("bold", bold.booleanValue());
			if (italic!=null) js.setMember("italic", italic.booleanValue());
			if (strike!=null) js.setMember("strike", strike.booleanValue());
			if (underline!=null) js.setMember("underline", underline.booleanValue());
			if (font!=null) js.setMember("font", font);
			if (size!=null) js.setMember("size", size.intValue());
			if (color!=null) js.setMember("color", color);
			if (background!=null) js.setMember("background", background);
			if (image!=null) js.setMember("image", image);
			if (link!=null) js.setMember("link", link);
			if (bullet!=null) js.setMember("bullet", bullet);
			if (list!=null) js.setMember("list", list);
			if (align!=null) js.setMember("align", align);
			return js;
		}
		
		public Format bold() {
			bold = true;
			return this; 
		}

		public Format unbold() {
			bold = false;
			return this; 
		}

		public Format italic() {
			italic = true;
			return this;
		}

		public Format unitalic() {
			italic = false;
			return this;
		}

		public Format strike() {
			strike = true;
			return this;
		}

		public Format unstrike() {
			strike = true;
			return this;
		}

		public Format underline() {
			underline = true;
			return this;
		}

		public Format ununderline() {
			underline = false;
			return this;
		}
		
		public Format size(int size) {
			this.size = size;
			return this;
		}
	}
}
