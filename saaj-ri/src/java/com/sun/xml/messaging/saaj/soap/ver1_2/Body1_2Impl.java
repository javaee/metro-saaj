/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
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

/**
*
* @author SAAJ RI Development Team
*/
package com.sun.xml.messaging.saaj.soap.ver1_2;

import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.soap.*;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sun.xml.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.messaging.saaj.soap.SOAPDocument;
import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.impl.BodyImpl;
import com.sun.xml.messaging.saaj.soap.name.NameImpl;

public class Body1_2Impl extends BodyImpl {
    
    protected static final Logger log =
        Logger.getLogger(Body1_2Impl.class.getName(),
                         "com.sun.xml.messaging.saaj.soap.ver1_2.LocalStrings");
    
    public Body1_2Impl(SOAPDocumentImpl ownerDocument, String prefix) {
            super(ownerDocument, NameImpl.createBody1_2Name(prefix));
    }

    public Body1_2Impl(SOAPDocumentImpl ownerDoc, Element domElement) {
        super(ownerDoc, domElement);
    }

    @Override
    protected NameImpl getFaultName(String name) {
        return NameImpl.createFault1_2Name(name, null);
    }

    @Override
    protected SOAPBodyElement createBodyElement(Name name) {
        return new BodyElement1_2Impl(
            ((SOAPDocument) getOwnerDocument()).getDocument(),
            name);
    }
    @Override
    protected SOAPBodyElement createBodyElement(QName name) {
        return new BodyElement1_2Impl(
            ((SOAPDocument) getOwnerDocument()).getDocument(),
            name);
    }

    @Override
    protected QName getDefaultFaultCode() {
        return SOAPConstants.SOAP_RECEIVER_FAULT;
    }

    @Override
    public SOAPFault addFault() throws SOAPException {
        if (hasAnyChildElement()) {
            log.severe("SAAJ0402.ver1_2.only.fault.allowed.in.body");
            throw new SOAPExceptionImpl(
                "No other element except Fault allowed in SOAPBody");
        }
        return super.addFault();
    }

    /*
     * Override setEncodingStyle of ElementImpl to restrict adding encodingStyle
     * attribute to SOAP Body (SOAP 1.2 spec, part 1, section 5.1.1)
     */
    @Override
    public void setEncodingStyle(String encodingStyle) throws SOAPException {
        log.severe("SAAJ0401.ver1_2.no.encodingstyle.in.body");
        throw new SOAPExceptionImpl("encodingStyle attribute cannot appear on Body");
    }

    /*
     * Override addAttribute of ElementImpl to restrict adding encodingStyle
     * attribute to SOAP Body (SOAP 1.2 spec, part 1, section 5.1.1)
     */
    @Override
    public SOAPElement addAttribute(Name name, String value)
        throws SOAPException {
        if (name.getLocalName().equals("encodingStyle")
            && name.getURI().equals(NameImpl.SOAP12_NAMESPACE)) {

            setEncodingStyle(value);
        }
        return super.addAttribute(name, value);
    }

    @Override
    public SOAPElement addAttribute(QName name, String value)
        throws SOAPException {
        if (name.getLocalPart().equals("encodingStyle")
            && name.getNamespaceURI().equals(NameImpl.SOAP12_NAMESPACE)) {

            setEncodingStyle(value);
        }
        return super.addAttribute(name, value);
    }

    @Override
    protected boolean isFault(SOAPElement child) {
        return (child.getElementName().getURI().equals(
                    SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE) &&
                child.getElementName().getLocalName().equals(
                    "Fault"));
    }

    @Override
    protected SOAPFault createFaultElement() {
        return new Fault1_2Impl(
            ((SOAPDocument) getOwnerDocument()).getDocument(), getPrefix());
    }

    /*
     * section 5.4 of SOAP1.2 candidate recommendation says that a
     * SOAP message MUST contain a single Fault element as the only
     * child element of the SOAP Body.
     */
    @Override
    public SOAPBodyElement addBodyElement(Name name) throws SOAPException {
        if (hasFault()) {
            log.severe("SAAJ0402.ver1_2.only.fault.allowed.in.body");
            throw new SOAPExceptionImpl(
                "No other element except Fault allowed in SOAPBody");
        }
        return super.addBodyElement(name);
    }

    @Override
    public SOAPBodyElement addBodyElement(QName name) throws SOAPException {
        if (hasFault()) {
            log.severe("SAAJ0402.ver1_2.only.fault.allowed.in.body");
            throw new SOAPExceptionImpl(
                "No other element except Fault allowed in SOAPBody");
        }
        return super.addBodyElement(name);
    }

    @Override
    protected SOAPElement addElement(Name name) throws SOAPException {
        if (hasFault()) {            
            log.severe("SAAJ0402.ver1_2.only.fault.allowed.in.body");
            throw new SOAPExceptionImpl(
                "No other element except Fault allowed in SOAPBody");
        }
        return super.addElement(name);
    }

    @Override
    protected SOAPElement addElement(QName name) throws SOAPException {
        if (hasFault()) {            
            log.severe("SAAJ0402.ver1_2.only.fault.allowed.in.body");
            throw new SOAPExceptionImpl(
                "No other element except Fault allowed in SOAPBody");
        }
        return super.addElement(name);
    }

    @Override
    public SOAPElement addChildElement(Name name) throws SOAPException {
        if (hasFault()) {            
            log.severe("SAAJ0402.ver1_2.only.fault.allowed.in.body");
            throw new SOAPExceptionImpl(
                "No other element except Fault allowed in SOAPBody");
        }
        return super.addChildElement(name);
    }

    @Override
    public SOAPElement addChildElement(QName name) throws SOAPException {
        if (hasFault()) {            
            log.severe("SAAJ0402.ver1_2.only.fault.allowed.in.body");
            throw new SOAPExceptionImpl(
                "No other element except Fault allowed in SOAPBody");
        }
        return super.addChildElement(name);
    }

    private boolean hasAnyChildElement() {
        Node currentNode = getFirstChild();
        while (currentNode != null) {
            if (currentNode.getNodeType() == Node.ELEMENT_NODE)
                return true;
            currentNode = currentNode.getNextSibling();
        }
        return false;
    }
}
