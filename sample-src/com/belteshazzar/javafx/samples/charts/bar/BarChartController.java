package com.belteshazzar.javafx.samples.charts.bar;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.chart.BarChart;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BarChartController {

	@HTML
	private BarChart barchart;
	
	@SuppressWarnings("unchecked")
	public void initialize() {
		barchart.labels().addAll("Mon", "Tue", "Wed", "Thu", "Fri");
		ObservableList<Number> s1 = FXCollections.observableArrayList();
		s1.addAll(5, 2, 4, 0, 2);
		ObservableList<Number> s2 = FXCollections.observableArrayList();
		s2.addAll(4, 1, 5, 3, 1);
		barchart.series().addAll(s1,s2);
	}
	
}
