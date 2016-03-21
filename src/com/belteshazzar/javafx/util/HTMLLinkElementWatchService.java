package com.belteshazzar.javafx.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLLinkElement;

import javafx.application.Platform;

class HTMLLinkElementWatchService extends Thread {
	private final WatchService watchService;
	private final Path folder;
	private boolean watching;
	private final Map<String,HTMLLinkElement> files;
	
	public HTMLLinkElementWatchService(Path folder) throws IOException {
		this.watchService = FileSystems.getDefault().newWatchService();
		folder.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
		this.folder = folder;
		this.watching = true;
		this.files = new HashMap<String,HTMLLinkElement>();
	}
	
	public void startWatching() {
		this.start();
	}

	@Override
	public void run() {
		while (watching) {
			try {
				WatchKey wk = this.watchService.take();
				for (WatchEvent<?> event : wk.pollEvents()) {
					Path changed = (Path) event.context();
					final HTMLLinkElement link = files.get(changed.toString());
					if (link!=null) {
						Platform.runLater(new Runnable() {
					        @Override public void run() {
					            Node parent = link.getParentNode();
					            parent.removeChild(link);
					            link.setHref(link.getHref().split("\\?")[0] + "?d=" + System.currentTimeMillis());
					            parent.appendChild(link);
					        }
					    });
					}
				}
				wk.reset();
			} catch (InterruptedException e) {
				if (watching) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private String checkAndGetFilename(HTMLLinkElement link) throws URISyntaxException, MalformedURLException {
		URL url = new URL(link.getHref());
		Path path = Paths.get(url.toURI());
		Path folderPath = path.getParent();
		if (!folderPath.equals(folder)) throw new IllegalArgumentException(folderPath + " not in this watchers path: " + folder);
		return path.getFileName().toString();
	}
	
	public void watch(HTMLLinkElement link) throws URISyntaxException, MalformedURLException {
		files.put(checkAndGetFilename(link),link);
	}
	
	public void unwatch(HTMLLinkElement link) throws URISyntaxException, MalformedURLException {
		files.remove(checkAndGetFilename(link));
	}

	public void stopWatching() {
		watching = false;
		this.interrupt();
	}
}