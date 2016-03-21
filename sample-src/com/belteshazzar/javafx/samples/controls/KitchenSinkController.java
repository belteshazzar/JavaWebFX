package com.belteshazzar.javafx.samples.controls;

import java.io.File;
import java.time.LocalDate;

import org.w3c.dom.events.Event;

import com.belteshazzar.javafx.HTML;
import com.belteshazzar.javafx.input.RichText;

import javafx.beans.property.Property;
import javafx.scene.paint.Color;

public class KitchenSinkController {

	@HTML
	private Property<Boolean> buttonButton;
	
	@HTML
	private void handleButton(Event ev) {
		System.err.println("set to male");
		inputRadio.setValue("male");
	}
	
	@HTML
	private Property<Boolean> buttonReset;

	@HTML
	private void handleReset(Event ev) {
		System.err.println("set to female");
		inputRadio.setValue("female");
	}

	@HTML
	private Property<Boolean> buttonSubmit;
	@HTML
	private Property<Boolean> inputButton;
	@HTML
	private Property<Boolean> inputCheckbox;
	@HTML
	private Property<Color> inputColor;
	@HTML
	private Property<LocalDate> inputDate;
	@HTML
	private Property<LocalDate> inputDatetime;
	@HTML
	private Property<LocalDate> inputDatetimeLocal;
	@HTML
	private Property<String> inputEmail;
	@HTML
	private Property<File> inputFile;
	@HTML
	private Property<String> inputHidden;
	@HTML
	private Property<String> inputImage;
	@HTML
	private Property<String> inputMonth;
	@HTML
	private Property<Number> inputNumber; // doesn't work with integer/double
	@HTML
	private Property<String> inputPassword;
	@HTML(name="gender")
	private Property<String> inputRadio;
	@HTML
	private Property<Number> inputRange;
	@HTML
	private Property<String> inputReset;
	@HTML
	private Property<String> inputSearch;
	@HTML
	private Property<String> inputSubmit;
	@HTML
	private Property<String> inputTel;
	@HTML
	private Property<String> inputText;
	@HTML
	private Property<String> inputTime;
	@HTML
	private Property<String> inputUrl;
	@HTML
	private Property<String> inputWeek;
	@HTML
	private Property<Number> progress;
	@HTML
	private Property<String> select;
	@HTML
	private Property<String> textareaPlain;
	@HTML
	private RichText textareaRich;

	public void initialize() {
		buttonButton.addListener((ob,old,val) -> System.err.println("buttonButton: " + val));
		buttonReset.addListener((ob,old,val) -> System.err.println("buttonReset: " + val));
		buttonSubmit.addListener((ob,old,val) -> System.err.println("buttonSubmit: " + val));

		inputButton.addListener((ob,old,val) -> System.err.println("inputButton: " + val));
		inputCheckbox.addListener((ob,old,val) -> {
			System.err.println("inputCheckbox: " + val);
			textareaRich.content().setValue((val?"bike":"no ' <b>bike</b>"));
		});
		inputColor.addListener((ob,old,val) -> System.err.println("inputColor: " + val));
		inputDate.addListener((ob,old,val) -> System.err.println("inputDate: " + val));
		inputDatetime.addListener((ob,old,val) -> System.err.println("inputDatetime: " + val));
		inputDatetimeLocal.addListener((ob,old,val) -> System.err.println("inputDatetimeLocal: " + val));
		inputEmail.addListener((ob,old,val) -> System.err.println("inputEmail: " + val));
		
		inputFile.addListener((ob,old,val) -> System.err.println("inputFile: " + val));

		inputHidden.addListener((ob,old,val) -> System.err.println("inputHidden: " + val));
		inputImage.addListener((ob,old,val) -> System.err.println("inputImage: " + val));
		inputMonth.addListener((ob,old,val) -> System.err.println("inputMonth: " + val));
		inputNumber.addListener((ob,old,val) -> System.err.println("inputNumber: " + val));
		inputPassword.addListener((ob,old,val) -> System.err.println("inputPassword: " + val));
		inputRadio.addListener((ob,old,val) -> System.err.println("inputRadio: " + val));
		inputRange.addListener((ob,old,val) -> System.err.println("inputRange: " + val));
		inputReset.addListener((ob,old,val) -> System.err.println("inputReset: " + val));
		inputSearch.addListener((ob,old,val) -> System.err.println("inputSearch: " + val));
		inputSubmit.addListener((ob,old,val) -> System.err.println("inputSubmit: " + val));
		inputTel.addListener((ob,old,val) -> System.err.println("inputTel: " + val));
		inputText.addListener((ob,old,val) -> System.err.println("inputText: " + val));
		inputTime.addListener((ob,old,val) -> System.err.println("inputTime: " + val));
		inputUrl.addListener((ob,old,val) -> System.err.println("inputUrl: " + val));
		inputWeek.addListener((ob,old,val) -> System.err.println("inputWeek: " + val));
		
		select.addListener((ob,old,val) -> System.err.println("select: " + val));
		
		textareaPlain.addListener((ob,old,val) -> System.err.println("textareaPlain: " + val));
		textareaRich.content().addListener((ob,old,val) -> System.err.println("textareaRich: " + val));
		
	}
}
