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

/**
*
* @author SAAJ RI Development Team
*/
package com.sun.xml.messaging.saaj.soap.ver1_1;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPElement;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.impl.HeaderElementImpl;
import com.sun.xml.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.messaging.saaj.util.LogDomainConstants;

public class HeaderElement1_1Impl extends HeaderElementImpl {
    
    protected static final Logger log =
        Logger.getLogger(LogDomainConstants.SOAP_VER1_1_DOMAIN,
                         "com.sun.xml.messaging.saaj.soap.ver1_1.LocalStrings");
    
    public HeaderElement1_1Impl(SOAPDocumentImpl ownerDoc, Name qname) {
        super(ownerDoc, qname);
    }
    public HeaderElement1_1Impl(SOAPDocumentImpl ownerDoc, QName qname) {
        super(ownerDoc, qname);
    }

    public SOAPElement setElementQName(QName newName) throws SOAPException {
        HeaderElementImpl copy =
            new HeaderElement1_1Impl((SOAPDocumentImpl) getOwnerDocument(), newName);
        return replaceElementWithSOAPElement(this,copy);
    }

    protected NameImpl getActorAttributeName() {
        return NameImpl.create("actor", null, NameImpl.SOAP11_NAMESPACE);
    }

    // role not supported by SOAP 1.1
    protected NameImpl getRoleAttributeName() {
        log.log(
            Level.SEVERE,
            "SAAJ0302.ver1_1.hdr.attr.unsupported.in.SOAP1.1",
            new String[] { "Role" });
        throw new UnsupportedOperationException("Role not supported by SOAP 1.1");
    }

    protected NameImpl getMustunderstandAttributeName() {
        return NameImpl.create("mustUnderstand", null, NameImpl.SOAP11_NAMESPACE);
    }

    // mustUnderstand attribute has literal value "1" or "0"
    protected String getMustunderstandLiteralValue(boolean mustUnderstand) {
        return (mustUnderstand == true ? "1" : "0");
    }

    protected boolean getMustunderstandAttributeValue(String mu) {
        if ("1".equals(mu) || "true".equalsIgnoreCase(mu))
            return true;
        return false;
    }

    // relay not supported by SOAP 1.1
    protected NameImpl getRelayAttributeName() {        
        log.log(
            Level.SEVERE,
            "SAAJ0302.ver1_1.hdr.attr.unsupported.in.SOAP1.1",
            new String[] { "Relay" });
        throw new UnsupportedOperationException("Relay not supported by SOAP 1.1");
    }

    protected String getRelayLiteralValue(boolean relayAttr) {
        log.log(
            Level.SEVERE,
            "SAAJ0302.ver1_1.hdr.attr.unsupported.in.SOAP1.1",
            new String[] { "Relay" });
        throw new UnsupportedOperationException("Relay not supported by SOAP 1.1");
    }

    protected boolean getRelayAttributeValue(String mu) {
        log.log(
            Level.SEVERE,
            "SAAJ0302.ver1_1.hdr.attr.unsupported.in.SOAP1.1",
            new String[] { "Relay" });
        throw new UnsupportedOperationException("Relay not supported by SOAP 1.1");
    }

    protected String getActorOrRole() {
        return getActor();
    }

}
