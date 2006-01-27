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
 * $Id: HeaderElement1_2Impl.java,v 1.1.1.1 2006-01-27 13:10:57 kumarjayanti Exp $
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

import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.impl.HeaderElementImpl;
import com.sun.xml.messaging.saaj.soap.name.NameImpl;

public class HeaderElement1_2Impl extends HeaderElementImpl {

    private static Logger log =
        Logger.getLogger(HeaderElement1_2Impl.class.getName(),
                         "com.sun.xml.messaging.saaj.soap.ver1_2.LocalStrings");
       
    public HeaderElement1_2Impl(SOAPDocumentImpl ownerDoc, Name qname) {
        super(ownerDoc, qname);
    }
    public HeaderElement1_2Impl(SOAPDocumentImpl ownerDoc, QName qname) {
        super(ownerDoc, qname);
    }

    public SOAPElement setElementQName(QName newName) throws SOAPException {
        HeaderElementImpl copy =
            new HeaderElement1_2Impl((SOAPDocumentImpl)getOwnerDocument(), newName);
        return replaceElementWithSOAPElement(this,copy);
    }

    protected NameImpl getRoleAttributeName() {
        return NameImpl.create("role", null, NameImpl.SOAP12_NAMESPACE);
    }

    // Actor equivalent to Role in SOAP 1.2
    protected NameImpl getActorAttributeName() {              
        return getRoleAttributeName();
    }

    protected NameImpl getMustunderstandAttributeName() {
        return NameImpl.create("mustUnderstand", null, NameImpl.SOAP12_NAMESPACE);
    }

    // mustUnderstand attribute has literal value "true" or "false" 
    protected String getMustunderstandLiteralValue(boolean mustUnderstand) {
        return (mustUnderstand == true ? "true" : "false");
    }

    protected boolean getMustunderstandAttributeValue(String mu) {
        if (mu.equals("true") || mu.equals("1"))
            return true;
        return false;
    }

   protected NameImpl getRelayAttributeName() {
        return NameImpl.create("relay", null, NameImpl.SOAP12_NAMESPACE);
    }

    //relay attribute has literal value "true" or "false"
    protected String getRelayLiteralValue(boolean relay) {
        return (relay == true ? "true" : "false");
    }

    protected boolean getRelayAttributeValue(String relay) {
        if (relay.equals("true") || relay.equals("1"))
            return true;
        return false;
    }

    protected String getActorOrRole() {
        return getRole();
    }
}
