package com.belteshazzar.javafx.chart;

import org.w3c.dom.html.HTMLDivElement;

import com.belteshazzar.javafx.DeferredScriptCallback;
import com.belteshazzar.javafx.WebScene;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import netscape.javascript.JSObject;

public abstract class Chart implements DeferredScriptCallback {

	protected HTMLDivElement div;
	protected WebScene scene;
	protected JSObject chartist;
	protected JSObject chartist_data;
	protected JSObject chartist_data_labels;
	protected JSObject chartist_data_series;
	
	private final ObservableList<String> labels;

	public Chart(WebScene scene, HTMLDivElement div, String type, String options) {
		this.scene = scene;
		this.div = div;
		chartist = null;
		labels = FXCollections.<String>observableArrayList();
		labels.addListener(new ListChangeListener<String>() {

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends String> c) {
				labelsUpdated(c);
			}
			
		});
		final String script = "new Chartist." + type + "('#" + div.getId() + "', {labels: [], series: []"+(options==null?"":",options: "+options)+" })";
		scene.addDeferredScript(script,this);
	}
	
	protected void labelsUpdated(javafx.collections.ListChangeListener.Change<? extends String> c) {
		chartist_data_labels.call("length",0);
		for (String label : labels) {
			chartist_data_labels.call("push", label);
		}
		chartist.call("update");
	}

	public ObservableList<String> labels() {
		return labels;
	}
	
	@Override
	public void scriptExecuted(JSObject js) {
		chartist = js;
		chartist_data = (JSObject) chartist.getMember("data");
		chartist_data_labels = (JSObject) chartist_data.getMember("labels");
		chartist_data_series = (JSObject) chartist_data.getMember("series");
	}

}
