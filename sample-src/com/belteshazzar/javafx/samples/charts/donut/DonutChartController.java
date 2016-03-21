package com.belteshazzar.javafx.samples.charts.donut;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.chart.DonutChart;

public class DonutChartController {

	@HTML
	private DonutChart donutchart;

	public void initialize() {
		donutchart.labels().addAll("Mon", "Tue", "Wed", "Thu", "Fri");
		donutchart.series().addAll(5, 2, 4, 3, 2);
	}
	
}
