package com.belteshazzar.javafx.canvas;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

public class HTMLDocumentWrapper implements HTMLDocument {

	private HTMLDocument doc;

	public HTMLDocumentWrapper(HTMLDocument doc) {
		this.doc = doc;
	}

	@Override
	public DocumentType getDoctype() {
		return doc.getDoctype();
	}

	@Override
	public DOMImplementation getImplementation() {
		return doc.getImplementation();
	}

	@Override
	public Element getDocumentElement() {
		return doc.getDocumentElement();
	}

	@Override
	public Element createElement(String tagName) throws DOMException {
		Element el = doc.createElement(tagName);
		if (tagName.equals("canvas")) {
			el = new HTMLCanvasElementImpl((HTMLElement)el);
		}
		return el;
	}

	@Override
	public DocumentFragment createDocumentFragment() {
		return doc.createDocumentFragment();
	}

	@Override
	public Text createTextNode(String data) {
		return doc.createTextNode(data);
	}

	@Override
	public Comment createComment(String data) {
		return doc.createComment(data);
	}

	@Override
	public CDATASection createCDATASection(String data) throws DOMException {
		return doc.createCDATASection(data);
	}

	@Override
	public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
		return doc.createProcessingInstruction(target, data);
	}

	@Override
	public Attr createAttribute(String name) throws DOMException {
		return doc.createAttribute(name);
	}

	@Override
	public EntityReference createEntityReference(String name) throws DOMException {
		return doc.createEntityReference(name);
	}

	@Override
	public NodeList getElementsByTagName(String tagname) {
		return doc.getElementsByTagName(tagname);
	}

	@Override
	public Node importNode(Node importedNode, boolean deep) throws DOMException {
		return doc.importNode(importedNode, deep);
	}

	@Override
	public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
		return doc.createElementNS(namespaceURI, qualifiedName);
	}

	@Override
	public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
		return doc.createAttributeNS(namespaceURI, qualifiedName);
	}

	@Override
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
		return doc.getElementsByTagNameNS(namespaceURI, localName);
	}

	@Override
	public Element getElementById(String elementId) {
		return doc.getElementById(elementId);
	}

	@Override
	public String getInputEncoding() {
		return doc.getInputEncoding();
	}

	@Override
	public String getXmlEncoding() {
		return doc.getXmlEncoding();
	}

	@Override
	public boolean getXmlStandalone() {
		return doc.getXmlStandalone();
	}

	@Override
	public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
		doc.setXmlStandalone(xmlStandalone);
	}

	@Override
	public String getXmlVersion() {
		return doc.getXmlVersion();
	}

	@Override
	public void setXmlVersion(String xmlVersion) throws DOMException {
		doc.setXmlVersion(xmlVersion);
	}

	@Override
	public boolean getStrictErrorChecking() {
		return doc.getStrictErrorChecking();
	}

	@Override
	public void setStrictErrorChecking(boolean strictErrorChecking) {
		doc.setStrictErrorChecking(strictErrorChecking);
	}

	@Override
	public String getDocumentURI() {
		return doc.getDocumentURI();
	}

	@Override
	public void setDocumentURI(String documentURI) {
		doc.setDocumentURI(documentURI);
	}

	@Override
	public Node adoptNode(Node source) throws DOMException {
		return doc.adoptNode(source);
	}

	@Override
	public DOMConfiguration getDomConfig() {
		return doc.getDomConfig();
	}

	@Override
	public void normalizeDocument() {
		doc.normalizeDocument();
	}

	@Override
	public Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
		return doc.renameNode(n, namespaceURI, qualifiedName);
	}

	@Override
	public String getNodeName() {
		return doc.getNodeName();
	}

	@Override
	public String getNodeValue() throws DOMException {
		return doc.getNodeValue();
	}

	@Override
	public void setNodeValue(String nodeValue) throws DOMException {
		doc.setNodeValue(nodeValue);
	}

	@Override
	public short getNodeType() {
		return doc.getNodeType();
	}

	@Override
	public Node getParentNode() {
		return doc.getParentNode();
	}

	@Override
	public NodeList getChildNodes() {
		return doc.getChildNodes();
	}

	@Override
	public Node getFirstChild() {
		return doc.getFirstChild();
	}

	@Override
	public Node getLastChild() {
		return doc.getLastChild();
	}

	@Override
	public Node getPreviousSibling() {
		return doc.getPreviousSibling();
	}

	@Override
	public Node getNextSibling() {
		return doc.getNextSibling();
	}

	@Override
	public NamedNodeMap getAttributes() {
		return doc.getAttributes();
	}

	@Override
	public Document getOwnerDocument() {
		return doc.getOwnerDocument();
	}

	@Override
	public Node insertBefore(Node newChild, Node refChild) throws DOMException {
		return doc.insertBefore(newChild, refChild);
	}

	@Override
	public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
		return doc.replaceChild(newChild, oldChild);
	}

	@Override
	public Node removeChild(Node oldChild) throws DOMException {
		return doc.removeChild(oldChild);
	}

	@Override
	public Node appendChild(Node newChild) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildNodes() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Node cloneNode(boolean deep) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void normalize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSupported(String feature, String version) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNamespaceURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrefix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPrefix(String prefix) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getLocalName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasAttributes() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getBaseURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short compareDocumentPosition(Node other) throws DOMException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getTextContent() throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTextContent(String textContent) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSameNode(Node other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String lookupPrefix(String namespaceURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDefaultNamespace(String namespaceURI) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String lookupNamespaceURI(String prefix) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEqualNode(Node arg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getFeature(String feature, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object setUserData(String key, Object data, UserDataHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getUserData(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTitle(String title) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getReferrer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDomain() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HTMLElement getBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBody(HTMLElement body) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HTMLCollection getImages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HTMLCollection getApplets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HTMLCollection getLinks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HTMLCollection getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HTMLCollection getAnchors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCookie() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCookie(String cookie) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void open() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeln(String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NodeList getElementsByName(String elementName) {
		// TODO Auto-generated method stub
		return null;
	}

}
