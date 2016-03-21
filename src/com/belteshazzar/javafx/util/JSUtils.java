package com.belteshazzar.javafx.util;

import netscape.javascript.JSObject;

public class JSUtils {

	public final static String getKeys = "{var keys = [];for (var key in this) {keys.push(key);} keys;}";

	public static void printProperties(Object o) {
		if (o==null) return;
		if (o instanceof JSObject) {
			JSObject js = (JSObject)o;
	        final JSObject keys = (JSObject) js.eval(getKeys);
	        for (int i = 0;; i++) {
	            final String key = (String) keys.getSlot(i);
	            if ((key != null) && !(key.equals("undefined"))) {
	                System.err.println(key + " ::: " + js.getMember(key));
	            } else {
	                break;
	            }
	        }
		} else {
			System.err.println(o);
		}
	}
	
	public static JSObject createJSON(JSObject js, String json) {
		return (JSObject)js.eval("(function() { return "+json+";})()");
	}

	public static JSObject createFunction(JSObject _this, String src) {
		return (JSObject)_this.eval("(function() { var _this = this; return "+src+"; }).call(this);");
		
	}


}
