package com.belteshazzar.javafx.canvas;

import netscape.javascript.JSObject;

public class ImageData {

	JSObject js;
	
	public ImageData(Object obj) {
		js = (JSObject)obj;
	}

	public Uint8ClampedArray getData() {
		return new Uint8ClampedArray(js.getMember("data"));
	}

}
