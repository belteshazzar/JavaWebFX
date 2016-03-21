package com.belteshazzar.javafx.canvas;

import org.w3c.dom.html.HTMLElement;

import com.sun.webkit.dom.HTMLImageElementImpl;

import netscape.javascript.JSObject;

public class CanvasRenderingContext2D implements RenderingContext {

	private JSObject js;
	
	public CanvasRenderingContext2D(JSObject js) {
		this.js = js;
	}
	
	public void fillRect(double x, double y, double w, double h) {
		js.call("fillRect", x,y,w,h);
	}

	public void drawImage(HTMLImageElementImpl cappuccino, double x, double y) {
		js.call("drawImage", cappuccino, x, y);
	}

	public ImageData getImageData(double x, double y, double width, double height) {
		return new ImageData(js.call("getImageData", x, y, width, height));
	}

	public void putImageData(ImageData imageData, double x, double y) {
		js.call("putImageData", imageData.js, x, y);
	}

	public HTMLCanvasElement getCanvas() {
		return new HTMLCanvasElementImpl((HTMLElement)js.getMember("canvas"));
	}
}
