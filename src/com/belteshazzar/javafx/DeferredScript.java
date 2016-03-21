package com.belteshazzar.javafx;

class DeferredScript {
	public final String script;
	public final DeferredScriptCallback callback;
	public final Object[] params;
	
	public DeferredScript(String script) {
		this(script, null);
	}
	
	public DeferredScript(String script, DeferredScriptCallback callback, Object ... params) {
		this.script = script;
		this.callback = callback;
		this.params = params;
	}
}