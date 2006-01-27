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
 * $Id: Header1_2Impl.java,v 1.1.1.1 2006-01-27 13:10:57 kumarjayanti Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/**
*
* @author SAAJ RI Development Team
*/
package com.sun.xml.messaging.saaj.soap.ver1_2;

import java.util.List;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.xml.namespace.QName;
import javax.xml.soap.*;

import com.sun.xml.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.messaging.saaj.soap.SOAPDocument;
import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.impl.HeaderElementImpl;
import com.sun.xml.messaging.saaj.soap.impl.HeaderImpl;
import com.sun.xml.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.messaging.saaj.util.LogDomainConstants;


public class Header1_2Impl extends HeaderImpl {
    
    protected static Logger log =
        Logger.getLogger(
            LogDomainConstants.SOAP_VER1_2_DOMAIN,
            "com.sun.xml.messaging.saaj.soap.ver1_2.LocalStrings");
        
    public Header1_2Impl(SOAPDocumentImpl ownerDocument, String prefix) {
        super(ownerDocument, NameImpl.createHeader1_2Name(prefix));
    }

    protected NameImpl getNotUnderstoodName() {
        return NameImpl.createNotUnderstood1_2Name(null);
    }

    protected NameImpl getUpgradeName() {
        return NameImpl.createUpgrade1_2Name(null);
    }

    protected NameImpl getSupportedEnvelopeName() {
        return NameImpl.createSupportedEnvelope1_2Name(null);
    }

    public SOAPHeaderElement addNotUnderstoodHeaderElement(final QName sourceName)
        throws SOAPException {

        if (sourceName == null) {
            log.severe("SAAJ0410.ver1_2.no.null.to.addNotUnderstoodHeader");
            throw new SOAPException("Cannot pass NULL to addNotUnderstoodHeaderElement");
        }
        if ("".equals(sourceName.getNamespaceURI())) {
            log.severe("SAAJ0417.ver1_2.qname.not.ns.qualified");
            throw new SOAPException("The qname passed to addNotUnderstoodHeaderElement must be namespace-qualified");
        }
        String prefix = sourceName.getPrefix();
        if ("".equals(prefix)) {
            prefix = "ns1";
        }
        Name notunderstoodName = getNotUnderstoodName();
        SOAPHeaderElement notunderstoodHeaderElement =
            (SOAPHeaderElement) addChildElement(notunderstoodName);
        notunderstoodHeaderElement.addAttribute(
            NameImpl.createFromUnqualifiedName("qname"),
            getQualifiedName(
                new QName(
                    sourceName.getNamespaceURI(),
                    sourceName.getLocalPart(),
                    prefix)));
        notunderstoodHeaderElement.addNamespaceDeclaration(
            prefix,
            sourceName.getNamespaceURI());
        return notunderstoodHeaderElement;
    }

    public SOAPElement addTextNode(String text) throws SOAPException {
        log.log(
            Level.SEVERE,
            "SAAJ0416.ver1_2.adding.text.not.legal",
            getElementQName());
        throw new SOAPExceptionImpl("Adding text to SOAP 1.2 Header is not legal");
    }

    protected SOAPHeaderElement createHeaderElement(Name name)
        throws SOAPException {
        String uri = name.getURI();
        if (uri == null || uri.equals("")) {          
            log.severe("SAAJ0413.ver1_2.header.elems.must.be.ns.qualified");
            throw new SOAPExceptionImpl("SOAP 1.2 header elements must be namespace qualified");
        }
        return new HeaderElement1_2Impl(
            ((SOAPDocument) getOwnerDocument()).getDocument(),
            name);
    }

    protected SOAPHeaderElement createHeaderElement(QName name)
        throws SOAPException {
        String uri = name.getNamespaceURI();
        if (uri == null || uri.equals("")) {          
            log.severe("SAAJ0413.ver1_2.header.elems.must.be.ns.qualified");
            throw new SOAPExceptionImpl("SOAP 1.2 header elements must be namespace qualified");
        }
        return new HeaderElement1_2Impl(
            ((SOAPDocument) getOwnerDocument()).getDocument(),
            name);
    }

    public void setEncodingStyle(String encodingStyle) throws SOAPException {
        log.severe("SAAJ0409.ver1_2.no.encodingstyle.in.header");
        throw new SOAPExceptionImpl("encodingStyle attribute cannot appear on Header");
    }

    public SOAPElement addAttribute(Name name, String value)
        throws SOAPException {
        if (name.getLocalName().equals("encodingStyle")
            && name.getURI().equals(NameImpl.SOAP12_NAMESPACE)) {

            setEncodingStyle(value);
        }
        return super.addAttribute(name, value);
    }

    public SOAPElement addAttribute(QName name, String value)
        throws SOAPException {
        if (name.getLocalPart().equals("encodingStyle")
            && name.getNamespaceURI().equals(NameImpl.SOAP12_NAMESPACE)) {

            setEncodingStyle(value);
        }
        return super.addAttribute(name, value);
    }
}
