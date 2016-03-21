package com.belteshazzar.javafx;

import netscape.javascript.JSObject;

public interface DeferredScriptCallback {
	void scriptExecuted(JSObject js);
}