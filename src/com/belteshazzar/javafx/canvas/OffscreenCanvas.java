package com.belteshazzar.javafx.canvas;

public interface OffscreenCanvas {

	double getHeight();
	void setHeight(double height);
	
	double getWidth();
	void setWidth(double width);
	
	RenderingContext getContext(String type);

	Promise<Blob> toBlob(String type, double encoderOptions);
	
	ImageBitmap transferToImageBitmap();
}
