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
 * $Id: Header1_1Impl.java,v 1.1.1.1 2006-01-27 13:10:57 kumarjayanti Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/**
*
* @author SAAJ RI Development Team
*/
package com.sun.xml.messaging.saaj.soap.ver1_1;

import java.util.List;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.soap.*;

import com.sun.xml.messaging.saaj.soap.SOAPDocument;
import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.impl.HeaderImpl;
import com.sun.xml.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.messaging.saaj.util.LogDomainConstants;

public class Header1_1Impl extends HeaderImpl {
    
    protected static Logger log =
        Logger.getLogger(LogDomainConstants.SOAP_VER1_1_DOMAIN,
                         "com.sun.xml.messaging.saaj.soap.ver1_1.LocalStrings");
        
    public Header1_1Impl(SOAPDocumentImpl ownerDocument, String prefix) {
            super(ownerDocument, NameImpl.createHeader1_1Name(prefix));
    }

    protected NameImpl getNotUnderstoodName() {
        log.log(
            Level.SEVERE,
            "SAAJ0301.ver1_1.hdr.op.unsupported.in.SOAP1.1",
            new String[] { "getNotUnderstoodName" });
        throw new UnsupportedOperationException("Not supported by SOAP 1.1");
    }

    protected NameImpl getUpgradeName() {
        return NameImpl.create(
            "Upgrade",
            getPrefix(),
            SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE);
    }

    protected NameImpl getSupportedEnvelopeName() {
        return NameImpl.create(
            "SupportedEnvelope",
            getPrefix(),
            SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE);
    }

    public SOAPHeaderElement addNotUnderstoodHeaderElement(QName name)
        throws SOAPException {            
        log.log(
            Level.SEVERE,
            "SAAJ0301.ver1_1.hdr.op.unsupported.in.SOAP1.1",
            new String[] { "addNotUnderstoodHeaderElement" });
        throw new UnsupportedOperationException("Not supported by SOAP 1.1");
    }

    protected SOAPHeaderElement createHeaderElement(Name name) {
        return new HeaderElement1_1Impl(
            ((SOAPDocument) getOwnerDocument()).getDocument(),
            name);
    }
    protected SOAPHeaderElement createHeaderElement(QName name) {
        return new HeaderElement1_1Impl(
            ((SOAPDocument) getOwnerDocument()).getDocument(),
            name);
    }
}
