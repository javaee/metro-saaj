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
package com.sun.xml.messaging.saaj.soap.ver1_1;

import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.Name;

import com.sun.xml.messaging.saaj.soap.SOAPDocument;
import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.impl.*;
import com.sun.xml.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.messaging.saaj.util.LogDomainConstants;
import com.sun.xml.messaging.saaj.SOAPExceptionImpl;
import org.w3c.dom.Element;


public class Fault1_1Impl extends FaultImpl {

    protected static final Logger log =
        Logger.getLogger(
            LogDomainConstants.SOAP_VER1_1_DOMAIN,
            "com.sun.xml.messaging.saaj.soap.ver1_1.LocalStrings");

    public Fault1_1Impl(SOAPDocumentImpl ownerDocument, String prefix) {
       super(ownerDocument, NameImpl.createFault1_1Name(prefix));
    }

    public Fault1_1Impl(SOAPDocumentImpl ownerDoc, Element domElement) {
        super(ownerDoc, domElement);
    }

    public Fault1_1Impl(SOAPDocumentImpl ownerDoc) {
        super(ownerDoc, NameImpl.createFault1_1Name(null));
    }

    @Override
    protected NameImpl getDetailName() {
        return NameImpl.createDetail1_1Name();
    }

    @Override
    protected NameImpl getFaultCodeName() {
        return NameImpl.createFromUnqualifiedName("faultcode");
    }

    @Override
    protected NameImpl getFaultStringName() {
        return NameImpl.createFromUnqualifiedName("faultstring");
    }

    @Override
    protected NameImpl getFaultActorName() {
        return NameImpl.createFromUnqualifiedName("faultactor");
    }

    @Override
    protected DetailImpl createDetail() {
        return new Detail1_1Impl(
                       ((SOAPDocument) getOwnerDocument()).getDocument());
    }

    @Override
    protected FaultElementImpl createSOAPFaultElement(String localName) {
        return new FaultElement1_1Impl(
                       ((SOAPDocument) getOwnerDocument()).getDocument(),
                       localName);
    }

    @Override
    protected void checkIfStandardFaultCode(String faultCode, String uri)
        throws SOAPException {
        // SOAP 1.1 doesn't seem to mandate using faultcode from a particular
        // set of values.
        // Also need to be backward compatible.
    }

    @Override
    protected void finallySetFaultCode(String faultcode) throws SOAPException {
        this.faultCodeElement.addTextNode(faultcode);
    }

    @Override
    public String getFaultCode() {
        if (this.faultCodeElement == null)
            findFaultCodeElement();
        return this.faultCodeElement.getValue();
    }

    @Override
    public Name getFaultCodeAsName() {

        String faultcodeString = getFaultCode();
        if (faultcodeString == null) {
            return null;
        }
        int prefixIndex = faultcodeString.indexOf(':');
        if (prefixIndex == -1) {
            // Not a valid SOAP message, but we return the unqualified name
            // anyway since some apps do not strictly conform to SOAP
            // specs.  A message that does not contain a <faultcode>
            // element itself is also not valid in which case we return
            // null instead of throwing an exception so this is consistent.
            return NameImpl.createFromUnqualifiedName(faultcodeString);
        }

        // Get the prefix and map it to a namespace name (AKA namespace URI)
        String prefix = faultcodeString.substring(0, prefixIndex);
        if (this.faultCodeElement == null)
            findFaultCodeElement();
        String nsName = this.faultCodeElement.getNamespaceURI(prefix);
        return NameImpl.createFromQualifiedName(faultcodeString, nsName);
    }

    @Override
    public QName getFaultCodeAsQName() {
        String faultcodeString = getFaultCode();
        if (faultcodeString == null) {
            return null;
        }
        if (this.faultCodeElement == null)
            findFaultCodeElement();
        return convertCodeToQName(faultcodeString, this.faultCodeElement);
    }

    @Override
    public void setFaultString(String faultString) throws SOAPException {

        if (this.faultStringElement == null)
            findFaultStringElement();

        if (this.faultStringElement == null)
            this.faultStringElement = addSOAPFaultElement("faultstring");
        else {
            this.faultStringElement.removeContents();
            //this.faultStringElement.removeAttributeNS("http://www.w3.org/XML/1998/namespace", "lang");
            this.faultStringElement.removeAttribute("xml:lang");
        }

        this.faultStringElement.addTextNode(faultString);
    }

    @Override
    public String getFaultString() {
        if (this.faultStringElement == null)
            findFaultStringElement();
        return this.faultStringElement.getValue();

    }

    @Override
    public Locale getFaultStringLocale() {
        if (this.faultStringElement == null)
            findFaultStringElement();
        if (this.faultStringElement != null) {
            String xmlLangAttr =
                this.faultStringElement.getAttributeValue(
                    NameImpl.createFromUnqualifiedName("xml:lang"));
            if (xmlLangAttr != null)
                return xmlLangToLocale(xmlLangAttr);
        }
        return null;
    }

    @Override
    public void setFaultString(String faultString, Locale locale)
        throws SOAPException {
        setFaultString(faultString);
        this.faultStringElement.addAttribute(
            NameImpl.createFromTagName("xml:lang"),
            localeToXmlLang(locale));
    }

    @Override
    protected boolean isStandardFaultElement(String localName) {
        if (localName.equalsIgnoreCase("detail") ||
            localName.equalsIgnoreCase("faultcode") ||
            localName.equalsIgnoreCase("faultstring") ||
            localName.equalsIgnoreCase("faultactor")) {
            return true;
        }
        return false;
    }

    @Override
    public void appendFaultSubcode(QName subcode) {
        log.log(
            Level.SEVERE,
            "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1",
            "appendFaultSubcode");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override
    public void removeAllFaultSubcodes() {
        log.log(
            Level.SEVERE,
            "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1",
            "removeAllFaultSubcodes");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override
    public Iterator<QName> getFaultSubcodes() {
        log.log(
            Level.SEVERE,
            "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1",
            "getFaultSubcodes");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override
    public String getFaultReasonText(Locale locale) {
        log.log(
            Level.SEVERE,
            "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1",
            "getFaultReasonText");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override
    public Iterator<String> getFaultReasonTexts() {
        log.log(
            Level.SEVERE,
            "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1",
            "getFaultReasonTexts");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override
    public Iterator<Locale> getFaultReasonLocales() {
        log.log(
            Level.SEVERE,
            "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1",
            "getFaultReasonLocales");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override
    public void addFaultReasonText(String text, java.util.Locale locale)
        throws SOAPException {
        log.log(
            Level.SEVERE,
            "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1",
            "addFaultReasonText");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override
    public String getFaultRole() {
        log.log(
            Level.SEVERE,
            "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1",
            "getFaultRole");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override
    public void setFaultRole(String uri) {
        log.log(
            Level.SEVERE,
            "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1",
            "setFaultRole");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override
    public String getFaultNode() {
        log.log(
            Level.SEVERE,
            "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1",
            "getFaultNode");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override
    public void setFaultNode(String uri) {
        log.log(
            Level.SEVERE,
            "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1",
            "setFaultNode");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override
    protected QName getDefaultFaultCode() {
        return new QName(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE, "Server");
    }

    @Override
    public SOAPElement addChildElement(SOAPElement element)
        throws SOAPException {
        String localName = element.getLocalName();
        if ("Detail".equalsIgnoreCase(localName)) {
            if (hasDetail()) {
                log.severe("SAAJ0305.ver1_2.detail.exists.error");
                throw new SOAPExceptionImpl("Cannot add Detail, Detail already exists");
            }
        }
        return super.addChildElement(element);
    }

    @Override
    protected FaultElementImpl createSOAPFaultElement(QName qname) {
         return new FaultElement1_1Impl(
                       ((SOAPDocument) getOwnerDocument()).getDocument(),
                       qname);
    }

    @Override
    protected FaultElementImpl createSOAPFaultElement(Name qname) {
         return new FaultElement1_1Impl(
                       ((SOAPDocument) getOwnerDocument()).getDocument(),
                       (NameImpl)qname);
    }

    @Override
    public void setFaultCode(String faultCode, String prefix, String uri)
        throws SOAPException {
        if (prefix == null || "".equals(prefix)) {
            if (uri != null && !"".equals(uri)) {
                prefix = getNamespacePrefix(uri);
                if (prefix == null || "".equals(prefix)) {
                    prefix = "ns0";
                }
            }   
        }

        if (this.faultCodeElement == null)
            findFaultCodeElement();

        if (this.faultCodeElement == null)
            this.faultCodeElement = addFaultCodeElement();
        else
            this.faultCodeElement.removeContents();
 
        if (uri == null || "".equals(uri)) {
            if (prefix != null && !"".equals("prefix")) {
                uri = this.faultCodeElement.getNamespaceURI(prefix);
            }
        }

        if (uri == null || "".equals(uri)) {
            if (prefix != null && !"".equals(prefix)) {
                //cannot allow an empty URI for a non-Empty prefix
                log.log(Level.SEVERE, "SAAJ0307.impl.no.ns.URI", new Object[]{prefix + ":" + faultCode});
                throw new SOAPExceptionImpl("Empty/Null NamespaceURI specified for faultCode \"" + prefix + ":" + faultCode + "\"");
            } else {
                uri = "";
            }
        }

        checkIfStandardFaultCode(faultCode, uri);
        ((FaultElementImpl) this.faultCodeElement).ensureNamespaceIsDeclared(prefix, uri);
        
        if (prefix == null || "".equals(prefix)) {
            finallySetFaultCode(faultCode);
        } else {
            finallySetFaultCode(prefix + ":" + faultCode);
        }
    }
    
    private boolean standardFaultCode(String faultCode) {
        if (faultCode.equals("VersionMismatch") || faultCode.equals("MustUnderstand")
            || faultCode.equals("Client") || faultCode.equals("Server")) {
            return true;    
        }
        if (faultCode.startsWith("VersionMismatch.") || faultCode.startsWith("MustUnderstand.")
            || faultCode.startsWith("Client.") || faultCode.startsWith("Server.")) {
            return true;    
        }
        return false;
    }

    @Override
     public void setFaultActor(String faultActor) throws SOAPException {
        if (this.faultActorElement == null)
            findFaultActorElement();
        if (this.faultActorElement != null)
            this.faultActorElement.detachNode();
        if (faultActor == null)
            return;
        this.faultActorElement =
            createSOAPFaultElement(getFaultActorName());
        this.faultActorElement.addTextNode(faultActor);
        if (hasDetail()) {
            insertBefore(this.faultActorElement, this.detail);
            return;
        }
        addNode(this.faultActorElement);
        
    }
}
