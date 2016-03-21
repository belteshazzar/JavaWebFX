package com.belteshazzar.javafx.samples.controls.richtext;

import org.w3c.dom.events.Event;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.input.QuillRichText;
import com.belteshazzar.javafx.input.RichText;

public class RichtextExampleController {

	@HTML
	private RichText richtext;

	@HTML
	public void woot(Event ev) {
		richtext.content().setValue(richtext.content().getValue() + " woot");
	}

	@HTML
	public void bold(Event ev) {
		richtext.bold();
	}

	@HTML
	public void italic(Event ev) {
		richtext.italic();
	}

	@HTML
	public void strike(Event ev) {
		richtext.strikeThrough();
	}

	@HTML
	public void underline(Event ev) {
		richtext.underline();
	}

	@HTML
	public void size24(Event ev) {
		richtext.fontSize(24);
	}
	
	@HTML
	public void insertTable(Event ev) {
		richtext.insertHTML("<table><tr><td style='border: 1px solid green'></td><td style='border: 1px solid green'></td></tr><tr><td style='border: 1px solid green'></td><td style='border: 1px solid green'></td></tr></table>");
	}
	
	@HTML
	private QuillRichText quill;

	public void initialize() {
		
		richtext.content().addListener((ob,old,val) -> System.err.println("richtext: " + val));
		richtext.content().setValue("this is    my    <table id='fred'><tr style='color:red'><td>     test          </td><td>table</td><td>cell</td></tr><tr style='color:red'><td>r2 test</td><td>r2 table</td><td>r2 cell</td></tr></table>change");
		
		richtext.selectionStart().addListener((ob,old,val) -> System.err.println("sel start: " + val));
		richtext.selectionEnd().addListener((ob,old,val) -> System.err.println("sel end: " + val));
		
		richtext.fontWeight().addListener((obj,old,val) -> System.err.println("fontWeight: " + val));
		
	}
}
