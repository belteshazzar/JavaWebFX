package com.belteshazzar.javafx;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLDivElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLLinkElement;
import org.w3c.dom.html.HTMLMenuElement;
import org.w3c.dom.html.HTMLScriptElement;
import org.w3c.dom.html.HTMLSelectElement;
import org.w3c.dom.html.HTMLTextAreaElement;

import com.belteshazzar.javafx.canvas.HTMLCanvasElement;
import com.belteshazzar.javafx.canvas.HTMLCanvasElementImpl;
import com.belteshazzar.javafx.canvas.HTMLDocumentWrapper;
import com.belteshazzar.javafx.chart.BarChart;
import com.belteshazzar.javafx.chart.Chart;
import com.belteshazzar.javafx.chart.DonutChart;
import com.belteshazzar.javafx.chart.LineChart;
import com.belteshazzar.javafx.chart.PieChart;
import com.belteshazzar.javafx.input.QuillRichText;
import com.belteshazzar.javafx.input.RichText;
import com.belteshazzar.javafx.table.Table;
import com.belteshazzar.javafx.tree.Tree;
import com.belteshazzar.javafx.treetable.TreeTable;
import com.belteshazzar.javafx.util.StyleSheetWatcher;
import com.belteshazzar.javafx.util.WebUtils;
import com.sun.webkit.dom.HTMLDocumentImpl;
import com.sun.webkit.dom.HTMLElementImpl;
import com.sun.webkit.dom.HTMLTableElementImpl;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.fxml.LoadException;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebHistory.Entry;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import netscape.javascript.JSObject;

public class WebScene extends Scene implements EventHandler<WindowEvent> {
	
	private final VBox root;
	private final StackPane content;
	private final WebView webView;
	private final WebEngine webEngine;
	private final WebHistory webHistory;
	private HTMLDocument doc;
	private final MenuBar menuBar;
	private final Map<String,MenuItem> menuItems;
	private HTMLDocumentWrapper wrappedDoc;
	private Object controller;
	private ObjectProperty<WebSceneState> state;
	private JSObject window;
	private StyleSheetWatcher watcher;
	private final String[] srcs = { // TODO: these should be loaded on demand
			"jquery/jquery-2.2.0.min.js",
			"quill/quill.js",
			"chartist/chartist.min.js",
			"vintageJS/vintage.js",
			"vintageJS/vintage.presets.js",
			"jstree/jstree.min.js",
			"treetable/jquery.treetable.js",
			"bootstrap/js/bootstrap.js",
			"jquery-ui/jquery-ui.js"
	};
	private final String[] styles = {
			"chartist/chartist.min.css",
			"jstree/themes/default/style.css",
			"treetable/css/jquery.treetable.css",
			"treetable/css/jquery.treetable.theme.default.css",
			"bootstrap/css/bootstrap.css",
			"bootstrap/css/bootstrap-theme.css",
			"jquery-ui/jquery-ui.structure.css",
			"jquery-ui/jquery-ui.theme.css",
			"jquery-ui/jquery-ui.css"
	};
	private final List<DeferredScript> deferredScripts;

	public void addDeferredScript(String script) {
		deferredScripts.add(new DeferredScript(script));
	}

	public void addDeferredScript(String script, DeferredScriptCallback callback) {
		deferredScripts.add(new DeferredScript(script,callback));
	}
	
	public WebScene(URL url) throws LoadException {
		this(url,-1,-1,Color.WHITE,false,SceneAntialiasing.DISABLED);
	}
	
	public WebScene(URL url,double width, double height) throws LoadException {
		this(url,width,height,Color.WHITE,false,SceneAntialiasing.DISABLED);
	}
	
	public WebScene(URL url,double width, double height, Paint fill) throws LoadException {
		this(url,width,height,Color.WHITE,false,SceneAntialiasing.DISABLED);
	}

	public WebScene(URL url,double width, double height, boolean depthBuffer) throws LoadException {
		this(url,width,height,Color.WHITE,depthBuffer,SceneAntialiasing.DISABLED);
	}

	public WebScene(URL url,double width, double height, boolean depthBuffer, SceneAntialiasing antiAliasing) throws LoadException {
		this(url,width,height,Color.WHITE,depthBuffer,antiAliasing);
	}

	public WebScene(URL url,double width, double height, Paint fill, boolean depthBuffer, SceneAntialiasing antiAliasing) throws LoadException {
		super(new Group(),width,height,depthBuffer,antiAliasing);
		if (url==null) throw new LoadException("url must be non-null, maybe the path to your html file is wrong?");
		this.setFill(fill);
		deferredScripts = new ArrayList<DeferredScript>();
		state = new SimpleObjectProperty<WebSceneState>(WebSceneState.LOADING);
		watcher = new StyleSheetWatcher();
		root = new VBox();
		menuBar = new MenuBar();
		root.getChildren().add(menuBar);
		menuItems = new HashMap<String,MenuItem>();
		content = new StackPane();
		content.setAlignment(Pos.TOP_LEFT);
		root.getChildren().add(content);
		webView = new WebView();
		content.getChildren().add(webView);
		this.setRoot(root);
		webEngine = webView.getEngine();
		webEngine.setOnAlert(event -> showAlert(event.getData()));
		webEngine.setConfirmHandler(message -> showConfirm(message));
		webHistory = webEngine.getHistory();

		webHistory.getEntries().addListener(new ListChangeListener<WebHistory.Entry>() {

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Entry> c) {
				System.out.println("history change: " + c);
			}

		});
		webEngine.getLoadWorker().stateProperty().addListener(new WebEngineChangeListener());
		load(url);
		
		windowProperty().addListener(new ChangeListener<Window>() {

			@Override
			public void changed(ObservableValue<? extends Window> observable, Window oldValue, Window newValue) {
				if (oldValue!=null) ((Stage)oldValue).setOnCloseRequest(null);			
				((Stage)newValue).setOnCloseRequest(WebScene.this);
			}
			
		});
	}

	@Override
	public void handle(WindowEvent event) {
		try {
			watcher.stopWatching();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void load(URL url) {
		menuBar.getMenus().clear();
		webEngine.load(url.toExternalForm());
	}

	public Object getController() {
		return controller;
	}
	
	public WebEngine getEngine() {
		return webEngine;
	}
	
	public JSObject getJSWindow() {
		return window;
	}
	
	private class WebEngineChangeListener implements ChangeListener<State> {
		@Override
		public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
			if (newValue == State.SUCCEEDED) {
				try {
					initDocument();
					state.set(WebSceneState.SUCCEEDED);
				} catch (LoadException e) {
					state.set(WebSceneState.FAILED);
					e.printStackTrace();
				}
			}
		}
	}

	private void showAlert(String message) {
		JOptionPane.showMessageDialog(null,  message,"Alert", JOptionPane.WARNING_MESSAGE);
    }

    private boolean showConfirm(String message) {
    	return 0 != JOptionPane.showConfirmDialog(null,message, "Confirm",JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

    }

	private void initDocument() throws LoadException {
		doc = (HTMLDocumentImpl)webEngine.executeScript("window.document");
		if (doc!=null) {
			initController();
		}		
	}
	
	private void setControllerFields() throws LoadException {
		for (Field field : controller.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(HTML.class)) {
				HTML html = field.getAnnotation(HTML.class);
				if (HTMLCanvasElement.class==field.getType()) {
					setCanvasField(field, html);
				} else if (Element.class.isAssignableFrom(field.getType())) {
					setElementField(field, html);
				} else if (Property.class.isAssignableFrom(field.getType())) {
					setPropertyField(field, html);
				} else if (WebScene.class.isAssignableFrom(field.getType())) {
					setField(field,this);
				} else if (Table.class.isAssignableFrom(field.getType())) {
					setTableField(field,html);
				} else if (TreeTable.class.isAssignableFrom(field.getType())) {
					setTreeTableField(field,html);
				} else if (Tree.class.isAssignableFrom(field.getType())) {
					setTreeField(field,html);
				} else if (Chart.class.isAssignableFrom(field.getType())) {
					setChartField(field, html);
				} else if (QuillRichText.class.isAssignableFrom(field.getType())) {
					setQuillRichField(field,html);
				} else if (RichText.class.isAssignableFrom(field.getType())) {
					setRichField(field, html);
				} else if (MenuItem.class.isAssignableFrom(field.getType())) {
					setMenuItemField(field,html);
				} else {
					throw new LoadException("@HTML can only be applied to org.w3c.dom.Element or " + this.getClass().getSimpleName() + " fields found: " + field.getType().getSimpleName() + " " + field.getName());
				}
			}
		}
	}
	
	private void setMenuItemField(Field field, HTML html) throws LoadException {
		MenuItem menuItem = menuItems.get(html.id().equals("")?field.getName():html.id());
		if (menuItem==null) throw new LoadException("unable to find Menu/MenuItem for controller field: " + field);
		setField(field,menuItem);
	}

	private void setRichField(Field field, HTML html) throws LoadException {
		Element el = doc.getElementById(html.id().equals("")?field.getName():html.id());
		if (el==null) throw new LoadException("unable to find HTML element for controller field: " + field);
		if (!(el instanceof HTMLTextAreaElement))  throw new LoadException("rich text can only be applied to textarea, field: " + field);
		setField(field,new RichText(this, (HTMLTextAreaElement)el));
	}

	private void setQuillRichField(Field field, HTML html) throws LoadException {
		Element el = doc.getElementById(html.id().equals("")?field.getName():html.id());
		if (el==null) throw new LoadException("unable to find HTML element for controller field: " + field);
		if (!(el instanceof HTMLTextAreaElement))  throw new LoadException("quill rich text can only be applied to textarea, field: " + field);
		setField(field,new QuillRichText(this, (HTMLTextAreaElement)el));
	}

	@SuppressWarnings("rawtypes")
	private void setTableField(Field field, HTML html) throws LoadException {
		Element el = doc.getElementById(html.id().equals("")?field.getName():html.id());
		if (el==null) throw new LoadException("unable to find HTML element for controller field: " + html);
		setField(field,new Table((HTMLTableElementImpl)el));
	}

	@SuppressWarnings("rawtypes")
	private void setTreeTableField(Field field, HTML html) throws LoadException {
		Element el = doc.getElementById(html.id().equals("")?field.getName():html.id());
		if (el==null) throw new LoadException("unable to find HTML element for controller field: " + html);
		setField(field,new TreeTable(this,(HTMLTableElementImpl)el));
	}

	@SuppressWarnings("rawtypes")
	private void setTreeField(Field field, HTML html) throws LoadException {
		Element el = doc.getElementById(html.id().equals("")?field.getName():html.id());
		if (el==null) throw new LoadException("unable to find HTML element for controller field: " + html);
		setField(field,new Tree(this,(HTMLElement)el));
	}

	private void setChartField(Field field, HTML html) throws LoadException {
		Element el = doc.getElementById(html.id().equals("")?field.getName():html.id());
		if (el==null) throw new LoadException("unable to find HTML element for controller field: " + html);
		if (BarChart.class.isAssignableFrom(field.getType())) setField(field,new BarChart(this, (HTMLDivElement) el));
		else if (LineChart.class.isAssignableFrom(field.getType())) setField(field,new LineChart(this, (HTMLDivElement) el));
		else if (PieChart.class.isAssignableFrom(field.getType())) setField(field,new PieChart(this, (HTMLDivElement) el));
		else if (DonutChart.class.isAssignableFrom(field.getType())) setField(field,new DonutChart(this, (HTMLDivElement) el));
		else throw new LoadException("chart can not be applied to field type " + field.getType());
	}

	private void setPropertyField(Field field, HTML html) throws LoadException {
		if (html.id().equals("")) {
			if (html.name().equals("")) {
				Element el = doc.getElementById(field.getName());
				if (el==null) throw new LoadException("unable to find HTML element for controller field: " + field.getName());
				setPropertyField(field,el);
			} else {
				NodeList els = doc.getElementsByName(html.name());
				if (els.getLength()==0) throw new LoadException("unable to find HTML element for controller field: " + field.getName() + " by name: " + html.name());
				setPropertyField(field,els);
			}
		} else {
			Element el = doc.getElementById(html.id());
			if (el==null) throw new LoadException("unable to find HTML element for controller field: " + field.getName() + " by id: " + html.id());
			setPropertyField(field,el);
		}
	}

	private void setPropertyField(Field field, final NodeList els) throws LoadException {
		final Property<String> property = new SimpleStringProperty();
		EventListener el = new EventListener() {
			
			@Override
			public void handleEvent(Event evt) {
				property.setValue(((HTMLInputElement)evt.getTarget()).getValue());
			}
			
		};
		for (int i=0 ; i<els.getLength() ; i++) {
			Node n = els.item(i);
			if (n.getAttributes().getNamedItem("checked")!=null) {
				property.setValue(n.getAttributes().getNamedItem("value").getNodeValue());
			}
			((EventTarget)n).addEventListener("input", el, true);
			((EventTarget)n).addEventListener("change", el, true);
		}
		property.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				for (int i=0 ; i<els.getLength() ; i++) {
					Node n = els.item(i);
					HTMLInputElement input = (HTMLInputElement)n;
					if (input.getValue().equals(newValue)) {
						input.setChecked(true);
						break;
					}
				}
			}
		});
		setField(field,property);
	}

	private void setPropertyField(Field field, Element el) throws LoadException {
		if (el instanceof HTMLInputElement) {
			setHTMLInputElementField(field, (HTMLInputElement)el);
		} else if (el instanceof HTMLButtonElement) {
			setHTMLButtonElementField(field, (HTMLButtonElement)el);
		} else if (el instanceof HTMLTextAreaElement) {
			setHTMLTextAreaElementField(field, (HTMLTextAreaElement)el);
		} else if (el instanceof HTMLSelectElement) {
			setHTMLSelectElementField(field, (HTMLSelectElement)el);
		} else if (((HTMLElementImpl)el).getNodeName().equals("PROGRESS")) {
			setHTMLProgressElementField(field,(HTMLElement)el);
		} else {
			throw new LoadException("unable to bind Property field: " + field + ", unsupported element type: " + el.getClass());
		}
	}

	private void setHTMLInputElementField(Field field, final HTMLInputElement input) throws LoadException {
		final String propertyGenericType = field.getGenericType().toString().split("[<>]")[1];
		if ((input.getType().equalsIgnoreCase("number")
				|| input.getType().equalsIgnoreCase("range"))
			&& propertyGenericType.equals("java.lang.Number")) {
			setHTMLInputElementNumberField(field, input);
		} else if (input.getType().equalsIgnoreCase("checkbox")
				&& propertyGenericType.equals("java.lang.Boolean")) {
			setHTMLInputElementBooleanField(field, input);
		} else if (input.getType().equalsIgnoreCase("file")
				&& propertyGenericType.equals("java.io.File")) {
			setHTMLInputElementFileField(field, input);
		} else if (input.getType().equalsIgnoreCase("color")
				&& propertyGenericType.equals("javafx.scene.paint.Color")) {
			setHTMLInputElementColorField(field, input);
		} else if (input.getType().equalsIgnoreCase("date")
				&& propertyGenericType.equals("java.time.LocalDate")) {
			setHTMLInputElementLocalDateField(field, input);
		} else {
			setHTMLInputElementStringField(field, input);
		}
	}
	
	private void setHTMLProgressElementField(Field field, HTMLElement progress) throws LoadException {
		final Property<Number> property = new SimpleDoubleProperty();
		try {
			String stringValue = progress.getAttribute("value");
			if (stringValue==null) {
				property.setValue(0.0);
			} else {
				double value = Double.parseDouble(stringValue);
				property.setValue(value);
			}
		} catch (NumberFormatException nfex) {
			property.setValue(0.0);
		}
		property.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				progress.setAttribute("value",newValue.toString());
			}
		});
		setField(field,property);
	}

	private void setHTMLInputElementStringField(Field field, final HTMLInputElement input) throws LoadException {
		final Property<String> property = new SimpleStringProperty();
		property.setValue(input.getValue());
		EventListener el = new EventListener() {

			@Override
			public void handleEvent(Event evt) {
				if (!input.getValue().equals(property.getValue())) {
					property.setValue(input.getValue());
				}
			}
			
		};
		((EventTarget)input).addEventListener("input", el, true);
		((EventTarget)input).addEventListener("change", el, true);
		property.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.equals(input.getValue())) {
					input.setValue(newValue);
				}
			}
		});
		setField(field,property);
	}

	private void setHTMLInputElementLocalDateField(Field field, final HTMLInputElement input) throws LoadException {
		final Property<LocalDate> property = new SimpleObjectProperty<LocalDate>();
		((EventTarget)input).addEventListener("click", new EventListener() {

			@Override
			public void handleEvent(Event evt) {
				evt.preventDefault();
				WebUtils.Pos xp = WebUtils.getPosition(input);
				Platform.runLater(() -> {
					final DatePicker dp = new DatePicker();
					root.getChildren().add(dp);
					dp.setTranslateX(xp.x);
					dp.setTranslateY(xp.y);
					dp.setVisible(false);
					root.layout();
					dp.showingProperty().addListener(new ChangeListener<Boolean>() {

						@Override
						public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
								Boolean newValue) {
							if (!newValue) {
								root.getChildren().remove(dp);
								root.layout();
							}
						}
						
					});
					dp.valueProperty().addListener(new ChangeListener<LocalDate>() {

						@Override
						public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
								LocalDate newValue) {
							if (!newValue.equals(property.getValue())) {
								property.setValue(newValue);
								webEngine.executeScript("document.getElementById('"+input.getId()+"').value = '" + WebUtils.web(newValue) + "';");
							}
						}
						
					});
					// make it visible
					// TODO: there has to be a better way
					Runnable show = new Runnable() {

						@Override
						public void run() {
							if (null==dp.getSkin()) {
								Platform.runLater(this);
							} else {
								dp.arm();
								dp.show();
							}
						}
						
					};
					Platform.runLater(show);
				});
			}
			
		}, false);
		property.addListener(new ChangeListener<LocalDate>() {

			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
				String web = WebUtils.web(newValue);
				if (!web.equals(input.getValue())) {
					webEngine.executeScript("document.getElementById('"+input.getId()+"').value = '" + web + "';");
				}
			}
		});
		setField(field,property);
	}

	private void setHTMLInputElementColorField(Field field, final HTMLInputElement input) throws LoadException {
		final Property<Color> property = new SimpleObjectProperty<Color>();
		((EventTarget)input).addEventListener("click", new EventListener() {

			@Override
			public void handleEvent(Event evt) {
				evt.preventDefault();
				WebUtils.Pos xp = WebUtils.getPosition(input);
				Platform.runLater(() -> {
					final ColorPicker cp = new ColorPicker();
					root.getChildren().add(cp);
					cp.setTranslateX(xp.x);
					cp.setTranslateY(xp.y);
					cp.setVisible(false);
					root.layout();
					cp.showingProperty().addListener(new ChangeListener<Boolean>() {

						@Override
						public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
								Boolean newValue) {
							if (!newValue) {
								root.getChildren().remove(cp);
								root.layout();
							}
						}
						
					});
					cp.valueProperty().addListener(new ChangeListener<Color>() {

						@Override
						public void changed(ObservableValue<? extends Color> observable, Color oldValue,
								Color newValue) {
							if (!newValue.equals(property.getValue())) {
								property.setValue(newValue);
								webEngine.executeScript("document.getElementById('"+input.getId()+"').value = '" + WebUtils.web(newValue) + "';");
							}
						}
						
					});
					// make it visible
					// TODO: there has to be a better way
					Runnable show = new Runnable() {

						@Override
						public void run() {
							if (null==cp.getSkin()) {
								Platform.runLater(this);
							} else {
								cp.arm();
								cp.show();
							}
						}
						
					};
					Platform.runLater(show);
				});
			}
			
		}, false);
		property.addListener(new ChangeListener<Color>() {

			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				String web = WebUtils.web(newValue);
				if (!web.equals(input.getValue())) {
					webEngine.executeScript("document.getElementById('"+input.getId()+"').value = '" + web + "';");
				}
			}
		});
		setField(field,property);
	}

	private void setHTMLInputElementFileField(Field field, final HTMLInputElement input) throws LoadException {
		//convert to a button
		webEngine.executeScript("document.getElementById('" + input.getId() + "').type = 'button'");
		webEngine.executeScript("document.getElementById('" + input.getId() + "').value = 'Choose File'");
		final Property<File> property = new SimpleObjectProperty<File>();
		((EventTarget)input).addEventListener("click", new EventListener() {

			@Override
			public void handleEvent(Event evt) {
				evt.preventDefault();
				FileChooser fc = new FileChooser();
				File f = fc.showOpenDialog(getWindow());
				property.setValue(f);
			}
			
		}, false);
		setField(field,property);
	}

	private void setHTMLInputElementBooleanField(Field field, final HTMLInputElement input) throws LoadException {
		final Property<Boolean> property = new SimpleBooleanProperty();
		property.setValue(input.getChecked());
		EventListener el = new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				if (input.getChecked() != property.getValue().booleanValue()) {
					property.setValue(input.getChecked());
				}
			}
		};
		((EventTarget)input).addEventListener("input", el, true);
		((EventTarget)input).addEventListener("change", el, true);
		property.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue.booleanValue() != input.getChecked()) {
					input.setChecked(newValue.booleanValue());
				}
			}
		});
		setField(field,property);
	}

	private void setHTMLInputElementNumberField(Field field, final HTMLInputElement input) throws LoadException {
		final Property<Number> property = new SimpleDoubleProperty();
		property.setValue(new Double(input.getValue()));
		EventListener el = new EventListener() {

			@Override
			public void handleEvent(Event evt) {
				Number val= (input.getValue().length()==0?new Double(0.0):new Double(input.getValue()));
				if (!val.equals(property.getValue())) {
					property.setValue(val);
				}
			}
			
		};
		((EventTarget)input).addEventListener("input", el, true);
		((EventTarget)input).addEventListener("change", el, true);
		property.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (!newValue.equals(input.getValue())) {
					input.setValue(newValue.toString());
				}
			}
		});
		setField(field,property);
	}

	private void setHTMLButtonElementField(Field field, final HTMLButtonElement input)
			throws LoadException {
		final Property<String> property = new SimpleStringProperty();
		property.setValue(input.getValue());
		EventListener el = new EventListener() {
			
			@Override
			public void handleEvent(Event evt) {
				if (!input.getValue().equals(property.getValue())) {
					property.setValue(input.getValue());
				}
			}
			
		};
		((EventTarget)input).addEventListener("input", el, true);
		((EventTarget)input).addEventListener("change", el, true);
		property.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.equals(input.getValue())) {
					input.setValue(newValue);
				}
			}
		});
		setField(field,property);
	}

	private void setHTMLSelectElementField(Field field, final HTMLSelectElement input)
			throws LoadException {
		setField(field,createStringPropertyAndBind(input));
	}

	private void setHTMLTextAreaElementField(Field field, final HTMLTextAreaElement input)
			throws LoadException {
		setField(field,createStringPropertyAndBind(input));
	}
	
	private void setField(Field field, Object obj) throws LoadException {
		field.setAccessible(true);
		try {
			field.set(controller, obj);
		} catch (IllegalArgumentException e) {
			throw new LoadException("unable to set controller field: " + field.getName(),e);
		} catch (IllegalAccessException e) {
			throw new LoadException("unable to access controller field: " + field.getName(),e);
		}
	}

	private Property<String> createStringPropertyAndBind(final HTMLTextAreaElement input) {
		final Property<String> property = new SimpleStringProperty();
		property.setValue(input.getValue());
		EventListener el = new EventListener() {

			@Override
			public void handleEvent(Event evt) {
				if (!input.getValue().equals(property.getValue())) {
					property.setValue(input.getValue());
				}
			}

		};
		((EventTarget)input).addEventListener("input", el, true);
		((EventTarget)input).addEventListener("change", el, true);
		property.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.equals(input.getValue())) {
					input.setValue(newValue);
				}
			}
		});
		return property;
	}

	private Property<String> createStringPropertyAndBind(final HTMLSelectElement input) {
		final Property<String> property = new SimpleStringProperty();
		property.setValue(input.getValue());
		EventListener el = new EventListener() {

			@Override
			public void handleEvent(Event evt) {
				if (!input.getValue().equals(property.getValue())) {
					property.setValue(input.getValue());
				}
			}

		};
		((EventTarget)input).addEventListener("input", el, true);
		((EventTarget)input).addEventListener("change", el, true);
		property.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.equals(input.getValue())) {
					input.setValue(newValue);
				}
			}
		});
		return property;
	}

	private void setElementField(Field field, HTML html) throws LoadException {
		Element el = doc.getElementById(html.id().equals("")?field.getName():html.id());
		if (el==null) throw new LoadException("unable to find HTML element for controller field: " + field.getName());
		setField(field,field.getType().cast(el));
	}

	private void setCanvasField(Field field, HTML html) throws LoadException {
		Element el = doc.getElementById(html.id().equals("")?field.getName():html.id());
		if (el==null) throw new LoadException("unable to find HTML element for controller field: " + html);
		setField(field,new HTMLCanvasElementImpl((HTMLElementImpl)el));
	}
	
	private Map<String,Method> getEventMethods() {
		Map<String,Method> eventMethods = new HashMap<String,Method>();
		for (Method method : controller.getClass().getDeclaredMethods()) {
			if (method.isAnnotationPresent(HTML.class)) {
				Parameter[] ps = method.getParameters();
				if (ps.length==1 && Event.class.isAssignableFrom(ps[0].getType())) {
					eventMethods.put(method.getName(), method);
					method.setAccessible(true);
				}
			}
		}
		return eventMethods;
	}
	
	private void findStyleSheets() throws MalformedURLException, URISyntaxException, IOException {
		NodeList all = doc.getElementsByTagName("link");
		for (int i=0 ; i<all.getLength() ; i++) {
			Node n = all.item(i);
			HTMLLinkElement link = (HTMLLinkElement)n;
			watcher.watch(link);
		}
	}

	private void findAndAttachEventListeners() throws LoadException {
		Map<String,Method> eventMethods = getEventMethods();
		NodeList all = doc.getElementsByTagName("*");
		for (int i=0 ; i<all.getLength() ; i++) {
			Node n = all.item(i);
			NamedNodeMap attrs = n.getAttributes();
			for (int j=0 ; j<attrs.getLength() ; j++) {
				Node attr = attrs.item(j);
				if (attr.getNodeName().length()<3 || !attr.getNodeName().substring(0, 2).equalsIgnoreCase("on")) continue;
				String value = attr.getNodeValue().trim();
				if (value.length()>1 && value.charAt(0) == '#') {
					// unset from the node to stop events in the web page trying to call js
					attr.setNodeValue("return false;");
					String methodName = value.substring(1);
					String eventName = attr.getNodeName().substring(2).toLowerCase();
					final Method method = eventMethods.get(methodName);
					if (method==null) {
						throw new LoadException("failed to find event handler method in controller: " + methodName);
					}
					if (n.getNodeName().equals("MENU") || n.getNodeName().equals("MENUITEM")) {
						if (!eventName.equals("action")) throw new LoadException("only onAction events are supported on menu and menuitem's");
						final MenuItem menuItem = menuItems.get(((HTMLElement)n).getId());
						if (menuItem==null) {
							throw new LoadException("failed to find Menu/MenuItem for event handler, make sure you specify id: " + methodName);
						}
						menuItem.addEventHandler(javafx.event.ActionEvent.ACTION, new EventHandler<javafx.event.ActionEvent>() {
					
							@Override
							public void handle(javafx.event.ActionEvent event) {
								try {
									HTMLDocumentImpl di = (HTMLDocumentImpl)doc;
									Event ev = di.createEvent("Event");
									ev.initEvent("action", true, true);
									method.invoke(controller,ev);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					} else {
						((EventTarget)n).addEventListener(eventName, new EventListener() {
	
							@Override
							public void handleEvent(Event evt) {
								try {
									method.invoke(controller, evt);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							
						}, false);
					}
				}
			}
		}
	}
	
	private void createMenu(HTMLMenuElement el) throws LoadException {
		menuBar.getMenus().add(createSubMenu(el));
	}
	
	private Menu createSubMenu(HTMLMenuElement el) throws LoadException {
		Menu menu = new Menu(el.getAttribute("label"));
		Map<String,ToggleGroup> groups = new HashMap<String,ToggleGroup>();
		if (el.getId()!=null) {
			menuItems.put(el.getId(), menu);
		}
		NodeList children = el.getChildNodes();
		for (int i=0 ; i<children.getLength() ; i++) {
			Node child = children.item(i);
			if (child instanceof HTMLMenuElement) {
				menu.getItems().add(createSubMenu((HTMLMenuElement)child));
			} else if (child instanceof HTMLElement && child.getNodeName().equals("MENUITEM")) {
				menu.getItems().add(createMenuItem((HTMLElement)child,groups));
			}
		}
		return menu;
	}

	private MenuItem createMenuItem(HTMLElement child,Map<String,ToggleGroup> groups) throws LoadException {
		MenuItem item;
		if (child.hasAttribute("type")) {
			if (child.getAttribute("type").equals("checkbox")) {
				item = new CheckMenuItem(child.getAttribute("label"));
			} else if (child.getAttribute("type").equals("radio")) {
				if (!child.hasAttribute("group")) throw new LoadException("radio type menuitem must have group attribute");
				ToggleGroup group = groups.get(child.getAttribute("group"));
				if (group==null) {
					group = new ToggleGroup();
					groups.put(child.getAttribute("group"), group);
				}
				RadioMenuItem radioItem = new RadioMenuItem(child.getAttribute("label"));
				radioItem.setToggleGroup(group);
				item = radioItem;
			} else {
				return new SeparatorMenuItem();
			}
		} else {
			item = new MenuItem(child.getAttribute("label"));
		}
		if (child.getId()!=null) {
			menuItems.put(child.getId(), item);
		}
		if (child.hasAttribute("icon")) {
			String iconFile = child.getAttribute("icon");
			String url = controller.getClass().getResource(iconFile).toExternalForm();
			try {
				ImageView icon = new ImageView(new Image(url));
				item.setGraphic(icon);
			} catch (IllegalArgumentException ex) {
				throw new IllegalArgumentException("Unable to find menu item icon: " + url, ex);
			}
		}
		return item;
	}

	private void initController() throws LoadException {
		// set the window title based on html title
		((Stage)this.getWindow()).setTitle(doc.getTitle());
		
		// disable context menu
		doc.getBody().setAttribute("oncontextmenu", "return false");
		
		// add links
		CountDownLatch loadLatch = new CountDownLatch(srcs.length+styles.length);
		Node head = doc.getFirstChild().getFirstChild();
		final String baseDir = this.getClass().getResource("/").toExternalForm();
		// add js link to document
		for (final String src : srcs) {
			HTMLScriptElement script = (HTMLScriptElement)doc.createElement("script");
			((EventTarget)script).addEventListener("load", new EventListener() {

				@Override
				public void handleEvent(Event evt) {
					loadLatch.countDown();
				}
				
			}, false);
			script.setSrc(baseDir + src);
			head.appendChild(script);
		}
		// add css link to document
		for (final String style : styles) {
			HTMLLinkElement link = (HTMLLinkElement)doc.createElement("link");
			link.setRel("stylesheet");
			link.setHref(baseDir + style);
			((EventTarget)link).addEventListener("load", new EventListener() {

				@Override
				public void handleEvent(Event evt) {
					loadLatch.countDown();
				}
				
			}, false);
			head.appendChild(link);
		}
		
	    // setup the controller
		String controllerClassName = doc.getDocumentElement().getAttribute("fx:controller");
		System.err.println("loading controller: " + controllerClassName);
		if (controllerClassName==null) return;
		Class<?> controllerClass;
		try {
			controllerClass = this.getClass().getClassLoader().loadClass(controllerClassName);
		} catch (ClassNotFoundException e) {
			throw new LoadException("failed to load controller class: " + controllerClassName,e);
		}
		try {
			controller = controllerClass.newInstance();
		} catch (InstantiationException e) {
			throw new LoadException("controller must have zero argument constructor",e);
		} catch (IllegalAccessException e) {
			throw new LoadException("controller is not accessible",e);
		}
		
		// add menu's
		NodeList bodyChildren = doc.getBody().getChildNodes();
		for (int i=0 ; i<bodyChildren.getLength() ; i++) {
			if (bodyChildren.item(i) instanceof HTMLMenuElement) {
				createMenu((HTMLMenuElement)bodyChildren.item(i));
			}
		}

		// set controller fields
		setControllerFields();
		findAndAttachEventListeners();
		try {
			findStyleSheets();
		} catch (Exception e) {
			throw new LoadException("error watching style sheet for changes",e);
		}

		new Thread() {

			@Override
			public void run() {
				
				try {
					if (!loadLatch.await(2,TimeUnit.SECONDS)) System.err.println("ERROR: failed to load scripts");
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// init the javascript
						window = (JSObject)webEngine.executeScript("window");
					    JavaBridge bridge = new JavaBridge(controller);
					    window.setMember("java", bridge);
					    webEngine.executeScript("console.log = function(message) { java.log(message); };");

					    // run any deferred scripts
						for (DeferredScript deferred : deferredScripts) {
							Object o = window.eval(deferred.script);
							if (deferred.callback!=null) deferred.callback.scriptExecuted(o==null?null:(JSObject)o);
						}
						
						// call ready if it exists
					    if (!"undefined".equals(window.getMember("ready"))) {
					    	webEngine.executeScript("ready();");
					    }
						
						// init the controller (if the 'initialize' method exists)
						try {
							Method initialize = controllerClass.getDeclaredMethod("initialize");
							try {
								initialize.setAccessible(true);
								initialize.invoke(controller);
							} catch (Exception e) {
								e.printStackTrace();
							}
						} catch (NoSuchMethodException e) {
						} catch (SecurityException e) {
							e.printStackTrace();
						}
				
					}
					
				});
			}
			
		}.start();
	}

	public HTMLDocument getDocument() {
		if (wrappedDoc==null) {
			wrappedDoc = new HTMLDocumentWrapper(doc);
		}
		return wrappedDoc;
	}

}