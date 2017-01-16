/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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

import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import com.sun.xml.messaging.saaj.util.SAAJUtil;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.util.LogDomainConstants;
import org.w3c.dom.UserDataHandler;

public class SOAPCommentImpl
    implements javax.xml.soap.Text, org.w3c.dom.Comment {

    protected static final Logger log =
        Logger.getLogger(LogDomainConstants.SOAP_IMPL_DOMAIN,
                         "com.sun.xml.messaging.saaj.soap.impl.LocalStrings");
    protected static ResourceBundle rb =
        log.getResourceBundle();

    @Override
    public String getData() throws DOMException {
        return comment.getData();
    }

    @Override
    public void setData(String data) throws DOMException {
        comment.setData(data);
    }

    @Override
    public int getLength() {
        return comment.getLength();
    }

    @Override
    public String substringData(int offset, int count) throws DOMException {
        return comment.substringData(offset, count);
    }

    @Override
    public void appendData(String arg) throws DOMException {
        comment.appendData(arg);
    }

    @Override
    public void insertData(int offset, String arg) throws DOMException {
        comment.insertData(offset, arg);
    }

    @Override
    public void deleteData(int offset, int count) throws DOMException {
        comment.deleteData(offset, count);
    }

    @Override
    public void replaceData(int offset, int count, String arg) throws DOMException {
        comment.replaceData(offset, count, arg);
    }

    @Override
    public String getNodeName() {
        return comment.getNodeName();
    }

    @Override
    public String getNodeValue() throws DOMException {
        return comment.getNodeValue();
    }

    @Override
    public void setNodeValue(String nodeValue) throws DOMException {
        comment.setNodeValue(nodeValue);
    }

    @Override
    public short getNodeType() {
        return comment.getNodeType();
    }

    @Override
    public Node getParentNode() {
        return comment.getParentNode();
    }

    @Override
    public NodeList getChildNodes() {
        return comment.getChildNodes();
    }

    @Override
    public Node getFirstChild() {
        return comment.getFirstChild();
    }

    @Override
    public Node getLastChild() {
        return comment.getLastChild();
    }

    @Override
    public Node getPreviousSibling() {
        return comment.getPreviousSibling();
    }

    @Override
    public Node getNextSibling() {
        return comment.getNextSibling();
    }

    @Override
    public NamedNodeMap getAttributes() {
        return comment.getAttributes();
    }

    @Override
    public Document getOwnerDocument() {
        return comment.getOwnerDocument();
    }

    @Override
    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        return comment.insertBefore(newChild, refChild);
    }

    @Override
    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        return comment.replaceChild(newChild, oldChild);
    }

    @Override
    public Node removeChild(Node oldChild) throws DOMException {
        return comment.removeChild(oldChild);
    }

    @Override
    public Node appendChild(Node newChild) throws DOMException {
        return comment.appendChild(newChild);
    }

    @Override
    public boolean hasChildNodes() {
        return comment.hasChildNodes();
    }

    @Override
    public Node cloneNode(boolean deep) {
        return comment.cloneNode(deep);
    }

    @Override
    public void normalize() {
        comment.normalize();
    }

    @Override
    public boolean isSupported(String feature, String version) {
        return comment.isSupported(feature, version);
    }

    @Override
    public String getNamespaceURI() {
        return comment.getNamespaceURI();
    }

    @Override
    public String getPrefix() {
        return comment.getPrefix();
    }

    @Override
    public void setPrefix(String prefix) throws DOMException {
        comment.setPrefix(prefix);
    }

    @Override
    public String getLocalName() {
        return comment.getLocalName();
    }

    @Override
    public boolean hasAttributes() {
        return comment.hasAttributes();
    }

    @Override
    public String getBaseURI() {
        return comment.getBaseURI();
    }

    @Override
    public short compareDocumentPosition(Node other) throws DOMException {
        return comment.compareDocumentPosition(other);
    }

    @Override
    public String getTextContent() throws DOMException {
        return comment.getTextContent();
    }

    @Override
    public void setTextContent(String textContent) throws DOMException {
        comment.setTextContent(textContent);
    }

    @Override
    public boolean isSameNode(Node other) {
        return comment.isSameNode(other);
    }

    @Override
    public String lookupPrefix(String namespaceURI) {
        return comment.lookupPrefix(namespaceURI);
    }

    @Override
    public boolean isDefaultNamespace(String namespaceURI) {
        return comment.isDefaultNamespace(namespaceURI);
    }

    @Override
    public String lookupNamespaceURI(String prefix) {
        return comment.lookupNamespaceURI(prefix);
    }

    @Override
    public boolean isEqualNode(Node arg) {
        return comment.isEqualNode(arg);
    }

    @Override
    public Object getFeature(String feature, String version) {
        return comment.getFeature(feature, version);
    }

    @Override
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        return comment.setUserData(key, data, handler);
    }

    @Override
    public Object getUserData(String key) {
        return comment.getUserData(key);
    }

    private Comment comment;
    
    public SOAPCommentImpl(SOAPDocumentImpl ownerDoc, String text) {
        comment = ownerDoc.getDomDocument().createComment(text);
        ownerDoc.register(this);
    }

    public String getValue() {
        String nodeValue = getNodeValue();
        return (nodeValue.equals("") ? null : nodeValue);
    }
    
    public void setValue(String text) {
        setNodeValue(text);
    }
    

    public void setParentElement(SOAPElement element) throws SOAPException {
        if (element == null) {
            log.severe("SAAJ0112.impl.no.null.to.parent.elem");
            throw new SOAPException("Cannot pass NULL to setParentElement");
        }
        ((ElementImpl) element).addNode(this);
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
        return true;
    }

    public Text splitText(int offset) throws DOMException {
        log.severe("SAAJ0113.impl.cannot.split.text.from.comment");
        throw new UnsupportedOperationException("Cannot split text from a Comment Node.");
    }

    public Text replaceWholeText(String content) throws DOMException {
        log.severe("SAAJ0114.impl.cannot.replace.wholetext.from.comment");
        throw new UnsupportedOperationException("Cannot replace Whole Text from a Comment Node.");
    }

    public String getWholeText() {
        //TODO: maybe we have to implement this in future.
        throw new UnsupportedOperationException("Not Supported");
    }

    public boolean isElementContentWhitespace() {
        //TODO: maybe we have to implement this in future.
        throw new UnsupportedOperationException("Not Supported");
    }

    public Comment getDomElement() {
        return comment;
    }
}
