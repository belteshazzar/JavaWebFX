package com.belteshazzar.javafx.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.html.HTMLLinkElement;

public class StyleSheetWatcher {

	private Map<Path,HTMLLinkElementWatchService> watchServices;
	
	public StyleSheetWatcher() {
		this.watchServices = new HashMap<Path,HTMLLinkElementWatchService>();
	}
	
	private HTMLLinkElementWatchService serviceForLink(HTMLLinkElement link) throws IOException, URISyntaxException {
		URL url = new URL(link.getHref());
		Path folderPath = Paths.get(url.toURI()).getParent();
		HTMLLinkElementWatchService sws = this.watchServices.get(folderPath);
		if (sws==null) {
			sws = new HTMLLinkElementWatchService(folderPath);
			this.watchServices.put(folderPath, sws);
			sws.startWatching();
		}
		return sws;
	}
	
	public void watch(HTMLLinkElement link) throws URISyntaxException, IOException {
		serviceForLink(link).watch(link);
	}
	
	public void unwatch(HTMLLinkElement link) throws IOException, URISyntaxException {
		serviceForLink(link).unwatch(link);
	}

	public void stopWatching() throws Exception {
		for (HTMLLinkElementWatchService ws : this.watchServices.values()) ws.stopWatching();
		
	}

}
