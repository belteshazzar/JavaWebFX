package com.belteshazzar.javafx.chart;

import org.w3c.dom.html.HTMLDivElement;

import com.belteshazzar.javafx.WebScene;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public abstract class SingleSeriesChart extends Chart {

	private final ObservableList<Number> series;

	public SingleSeriesChart(WebScene scene, HTMLDivElement div, String type) {
		this(scene,div,type,null);
	}
	public SingleSeriesChart(WebScene scene, HTMLDivElement div, String type, String options) {
		super(scene, div, type, options);
		series = FXCollections.observableArrayList();
		series.addListener(new ListChangeListener<Number>() {

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Number> c) {
				chartist_data_series.call("length", 0);
				for (Number n : series) {
					chartist_data_series.call("push", n);
				}
				chartist.call("update");
			}
			
		});
	}

	public ObservableList<Number> series() {
		return series;
	}

}
