package com.belteshazzar.javafx.canvas;

import netscape.javascript.JSObject;

public class Uint8ClampedArray {

	private JSObject js;

	public Uint8ClampedArray(Object obj) {
		js = (JSObject)obj;
	}

	public int get(int idx) {
		return (Integer)js.getSlot(idx);
	}

	public void set(int idx, int val) {
		js.setSlot(idx, val);
	}

	public void set(int idx, double val) {
		set(idx,(int)val);
	}
}
