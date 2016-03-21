package com.belteshazzar.javafx.chart;

import org.w3c.dom.html.HTMLDivElement;

import com.belteshazzar.javafx.WebScene;

public class DonutChart extends SingleSeriesChart {

	public DonutChart(WebScene scene, HTMLDivElement div) {
		super(scene,div,"Pie","{ donut: true }");
	}

}
