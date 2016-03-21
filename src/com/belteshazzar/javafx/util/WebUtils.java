package com.belteshazzar.javafx.util;

import java.time.LocalDate;

import org.w3c.dom.html.HTMLElement;

import com.sun.webkit.dom.HTMLElementImpl;

import javafx.scene.paint.Color;

public class WebUtils {

	public static class Pos {
		public final double x;
		public final double y;
		
		@Override
		public String toString() {
			return "Pos [x=" + x + ", y=" + y + "]";
		}

		public Pos(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}
	
	public static Pos getPosition(HTMLElement el) {
		HTMLElementImpl element = (HTMLElementImpl)el;
	    double xPosition = 0;
	    double yPosition = 0;
	  
	    while(element!=null) {
	        xPosition += (element.getOffsetLeft() - element.getScrollLeft() + element.getClientLeft());
	        yPosition += (element.getOffsetTop() - element.getScrollTop() + element.getClientTop());
	        element = (HTMLElementImpl)element.getOffsetParent();
	    }
	    return new Pos(xPosition,yPosition);
	}
	
	public static String web(Color c) {
		return "#" + Integer.toHexString(c.hashCode()).substring(0, 6).toUpperCase();
	}
	
	public static String web(LocalDate ld) {
		return ld.getYear()+"-"+(ld.getMonthValue()<10?"0":"") + ld.getMonthValue()+"-"+(ld.getDayOfMonth()<10?"0":"")+ld.getDayOfMonth();
	}

}
