package com.belteshazzar.javafx.samples.controls.slider;

import org.w3c.dom.html.HTMLElement;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.WebScene;
import com.belteshazzar.javafx.canvas.CanvasRenderingContext2D;
import com.belteshazzar.javafx.canvas.HTMLCanvasElement;
import com.belteshazzar.javafx.canvas.ImageData;
import com.belteshazzar.javafx.canvas.Uint8ClampedArray;
import com.sun.webkit.dom.HTMLImageElementImpl;

import javafx.beans.property.Property;
import netscape.javascript.JSObject;

public class SliderSampleController {

	@HTML
	private WebScene webScene;
	@HTML
	private HTMLImageElementImpl cappuccino;
	@HTML
	private Property<Number> opacity;
	@HTML
	private HTMLElement opacityLabel;
	@HTML
	private Property<Number> sepia;
	@HTML
	private HTMLElement sepiaLabel;
	@HTML
	private Property<Number> scale;
	@HTML
	private HTMLElement scaleLabel;
	@HTML
	private Property<Boolean> useJava;
	@HTML
	private HTMLElement useJavaLabel;

	private String style() {
		String style = "opacity: " + opacity.getValue() + ";" + "-webkit-transform: scale(" + scale.getValue() + ","
				+ scale.getValue() + ");";
		return style;
	}

	public void initialize() {

		opacity.addListener((ob, old, val) -> {
			opacityLabel.setTextContent(val.toString());
			cappuccino.setAttribute("style", style());
		});
		sepia.addListener((ob, old, val) -> {
			sepiaLabel.setTextContent(val.toString());
			if (useJava.getValue()) {
				doSepia(val.doubleValue());
			} else {
				((JSObject)webScene.getJSWindow().getMember("sepia")).call("apply",val.doubleValue());
			}
		});
		scale.addListener((ob, old, val) -> {
			scaleLabel.setTextContent(val.toString());
			cappuccino.setAttribute("style", style());
		});
		useJava.addListener((ob,old,val) -> useJavaLabel.setTextContent(val?"Java":"JavaScript"));
	}

	private void doSepia(double p) {
		HTMLCanvasElement canvas = (HTMLCanvasElement)webScene.getDocument().createElement("canvas");
		canvas.setWidth(cappuccino.getWidth());
		canvas.setHeight(cappuccino.getHeight());
		CanvasRenderingContext2D ctx = (CanvasRenderingContext2D) canvas.getContext("2d");
		ctx.drawImage(cappuccino, 0, 0);
		ImageData imageData = ctx.getImageData(0, 0, canvas.getWidth(), canvas.getHeight());
		Uint8ClampedArray data = imageData.getData();
		@SuppressWarnings("unused")
		int idx, r, g, b, a;
		for (int i = (canvas.getWidth() * canvas.getHeight()) - 1; i >= 0; --i) {
			idx = i << 2; // idx = i * 4;
			r = data.get(idx);
			g = data.get(idx + 1);
			b = data.get(idx + 2);
			a = data.get(idx + 3);
			data.set(idx, r * (1 - p) + (r * 0.393 + g * 0.769 + b * 0.189) * p);
			data.set(idx + 1, g * (1 - p) + (r * 0.349 + g * 0.686 + b * 0.168) * p);
			data.set(idx + 2, b * (1 - p) + (r * 0.272 + g * 0.534 + b * 0.131) * p);
		}
		ctx.putImageData(imageData, 0, 0);
		cappuccino.setSrc(ctx.getCanvas().toDataURL());
	}
}
