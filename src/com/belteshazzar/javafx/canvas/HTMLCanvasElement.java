package com.belteshazzar.javafx.canvas;

import java.util.function.Function;

import org.w3c.dom.html.HTMLElement;

public interface HTMLCanvasElement extends HTMLElement {

	CanvasCaptureMediaStream captureStream(double framerate);
	
	RenderingContext getContext(String type);
	
	String toDataURL(String type, double encoderOptions);
	
	void toBlob(Function<Blob,Void> callback, String mimeType, double qualityArgument);
	
	OffscreenCanvas transferControlToOffscreen();

	void setWidth(String width);
	
	void setHeight(String height);

	int getWidth();

	int getHeight();

	String toDataURL();
}
