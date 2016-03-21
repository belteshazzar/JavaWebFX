package com.belteshazzar.javafx.samples.charts.pie;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.chart.PieChart;

public class PieChartController {

	@HTML
	private PieChart piechart;

	public void initialize() {
		piechart.labels().addAll("Mon", "Tue", "Wed", "Thu", "Fri");
		piechart.series().addAll(5, 2, 4, 3, 2);
	}
	
}
