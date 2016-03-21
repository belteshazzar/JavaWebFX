package com.belteshazzar.javafx.chart;

import org.w3c.dom.html.HTMLDivElement;

import com.belteshazzar.javafx.WebScene;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import netscape.javascript.JSObject;

public abstract class MultiSeriesChart extends Chart {

	private final ObservableList<ObservableList<Number>> series;

	public MultiSeriesChart(WebScene scene, HTMLDivElement div, String type, String options) {
		super(scene, div, type, options);
		series = FXCollections.observableArrayList();
		series.addListener(new ListChangeListener<ObservableList<Number>>() {

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends ObservableList<Number>> c) {
				chartist_data_series.call("length", 0);
				for (ObservableList<Number> s : series) {
					JSObject array = (JSObject)scene.getEngine().executeScript("[]");
					for (Number n : s) {
						array.call("push", n);
					}
					chartist_data_series.call("push",array);
				}
				chartist.call("update");
			}
			
		});
	}
	
	public ObservableList<ObservableList<Number>> series() {
		return series;
	}

}
