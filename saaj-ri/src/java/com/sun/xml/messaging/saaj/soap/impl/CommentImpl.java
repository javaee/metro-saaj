/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://jwsdp.dev.java.net/CDDLv1.0.html
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://jwsdp.dev.java.net/CDDLv1.0.html  If applicable,
 * add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your
 * own identifying information: Portions Copyright [yyyy]
 * [name of copyright owner]
 */
/*
 * $Id: CommentImpl.java,v 1.1.1.1 2006-01-27 13:10:56 kumarjayanti Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2006-01-27 13:10:56 $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sun.xml.messaging.saaj.soap.impl;

import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Text;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.util.LogDomainConstants;

public class CommentImpl
    extends com.sun.org.apache.xerces.internal.dom.CommentImpl
    implements javax.xml.soap.Text, org.w3c.dom.Comment { 

    protected static Logger log =
        Logger.getLogger(LogDomainConstants.SOAP_IMPL_DOMAIN,
                         "com.sun.xml.messaging.saaj.soap.impl.LocalStrings");
    protected static ResourceBundle rb =
        log.getResourceBundle();
    
    public CommentImpl(SOAPDocumentImpl ownerDoc, String text) {
        super(ownerDoc, text);
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

}
