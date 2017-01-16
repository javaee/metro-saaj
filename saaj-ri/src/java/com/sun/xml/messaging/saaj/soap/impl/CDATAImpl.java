/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.xml.messaging.saaj.soap.impl;

import java.util.logging.Logger;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.util.LogDomainConstants;
import com.sun.xml.messaging.saaj.util.SAAJUtil;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

public class CDATAImpl implements CDATASection, javax.xml.soap.Text {

    protected static final Logger log =
        Logger.getLogger(LogDomainConstants.SOAP_IMPL_DOMAIN,
                         "com.sun.xml.messaging.saaj.soap.impl.LocalStrings");
    
    static final String cdataUC = "<![CDATA[";
    static final String cdataLC = "<![cdata[";

    @Override
    public Text splitText(int offset) throws DOMException {
        return cdataSection.splitText(offset);
    }

    @Override
    public boolean isElementContentWhitespace() {
        return cdataSection.isElementContentWhitespace();
    }

    @Override
    public String getWholeText() {
        return cdataSection.getWholeText();
    }

    @Override
    public Text replaceWholeText(String content) throws DOMException {
        return cdataSection.replaceWholeText(content);
    }

    @Override
    public String getData() throws DOMException {
        return cdataSection.getData();
    }

    @Override
    public void setData(String data) throws DOMException {
        cdataSection.setData(data);
    }

    @Override
    public int getLength() {
        return cdataSection.getLength();
    }

    @Override
    public String substringData(int offset, int count) throws DOMException {
        return cdataSection.substringData(offset, count);
    }

    @Override
    public void appendData(String arg) throws DOMException {
        cdataSection.appendData(arg);
    }

    @Override
    public void insertData(int offset, String arg) throws DOMException {
        cdataSection.insertData(offset, arg);
    }

    @Override
    public void deleteData(int offset, int count) throws DOMException {
        cdataSection.deleteData(offset, count);
    }

    @Override
    public void replaceData(int offset, int count, String arg) throws DOMException {
        cdataSection.replaceData(offset, count, arg);
    }

    @Override
    public String getNodeName() {
        return cdataSection.getNodeName();
    }

    @Override
    public String getNodeValue() throws DOMException {
        return cdataSection.getNodeValue();
    }

    @Override
    public void setNodeValue(String nodeValue) throws DOMException {
        cdataSection.setNodeValue(nodeValue);
    }

    @Override
    public short getNodeType() {
        return cdataSection.getNodeType();
    }

    @Override
    public Node getParentNode() {
        return cdataSection.getParentNode();
    }

    @Override
    public NodeList getChildNodes() {
        return cdataSection.getChildNodes();
    }

    @Override
    public Node getFirstChild() {
        return cdataSection.getFirstChild();
    }

    @Override
    public Node getLastChild() {
        return cdataSection.getLastChild();
    }

    @Override
    public Node getPreviousSibling() {
        return cdataSection.getPreviousSibling();
    }

    @Override
    public Node getNextSibling() {
        return cdataSection.getNextSibling();
    }

    @Override
    public NamedNodeMap getAttributes() {
        return cdataSection.getAttributes();
    }

    @Override
    public Document getOwnerDocument() {
        return cdataSection.getOwnerDocument();
    }

    @Override
    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        return cdataSection.insertBefore(newChild, refChild);
    }

    @Override
    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        return cdataSection.replaceChild(newChild, oldChild);
    }

    @Override
    public Node removeChild(Node oldChild) throws DOMException {
        return cdataSection.removeChild(oldChild);
    }

    @Override
    public Node appendChild(Node newChild) throws DOMException {
        return cdataSection.appendChild(newChild);
    }

    @Override
    public boolean hasChildNodes() {
        return cdataSection.hasChildNodes();
    }

    @Override
    public Node cloneNode(boolean deep) {
        return cdataSection.cloneNode(deep);
    }

    @Override
    public void normalize() {
        cdataSection.normalize();
    }

    @Override
    public boolean isSupported(String feature, String version) {
        return cdataSection.isSupported(feature, version);
    }

    @Override
    public String getNamespaceURI() {
        return cdataSection.getNamespaceURI();
    }

    @Override
    public String getPrefix() {
        return cdataSection.getPrefix();
    }

    @Override
    public void setPrefix(String prefix) throws DOMException {
        cdataSection.setPrefix(prefix);
    }

    @Override
    public String getLocalName() {
        return cdataSection.getLocalName();
    }

    @Override
    public boolean hasAttributes() {
        return cdataSection.hasAttributes();
    }

    @Override
    public String getBaseURI() {
        return cdataSection.getBaseURI();
    }

    @Override
    public short compareDocumentPosition(Node other) throws DOMException {
        return cdataSection.compareDocumentPosition(other);
    }

    @Override
    public String getTextContent() throws DOMException {
        return cdataSection.getTextContent();
    }

    @Override
    public void setTextContent(String textContent) throws DOMException {
        cdataSection.setTextContent(textContent);
    }

    @Override
    public boolean isSameNode(Node other) {
        return cdataSection.isSameNode(other);
    }

    @Override
    public String lookupPrefix(String namespaceURI) {
        return cdataSection.lookupPrefix(namespaceURI);
    }

    @Override
    public boolean isDefaultNamespace(String namespaceURI) {
        return cdataSection.isDefaultNamespace(namespaceURI);
    }

    @Override
    public String lookupNamespaceURI(String prefix) {
        return cdataSection.lookupNamespaceURI(prefix);
    }

    @Override
    public boolean isEqualNode(Node arg) {
        return cdataSection.isEqualNode(arg);
    }

    @Override
    public Object getFeature(String feature, String version) {
        return cdataSection.getFeature(feature, version);
    }

    @Override
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        return cdataSection.setUserData(key, data, handler);
    }

    @Override
    public Object getUserData(String key) {
        return cdataSection.getUserData(key);
    }

    private CDATASection cdataSection;

    public CDATAImpl(SOAPDocumentImpl ownerDoc, String text) {
        cdataSection = ownerDoc.getDomDocument().createCDATASection(text);
        ownerDoc.register(this);
    }

    public String getValue() {
        String nodeValue = getNodeValue();
        return (nodeValue.equals("") ? null : nodeValue);
    }
    
    public void setValue(String text) {
        setNodeValue(text);
    }

    public void setParentElement(SOAPElement parent) throws SOAPException {
        if (parent == null) {
            log.severe("SAAJ0145.impl.no.null.to.parent.elem");
            throw new SOAPException("Cannot pass NULL to setParentElement");
        }
        ((ElementImpl) parent).addNode(this);
    }

    public SOAPElement getParentElement() {
        return (SOAPElement) getParentNode();
    }


    public void detachNode() {
        org.w3c.dom.Node parent = getParentNode();
        if (parent != null) {
            parent.removeChild(this);
        }
    }

    public void recycleNode() {
        detachNode();
        // TBD
        //  - add this to the factory so subsequent
        //    creations can reuse this object.
    }

    public boolean isComment() {
        return false;
    }

    public CDATASection getDomElement() {
        return cdataSection;
    }
}
