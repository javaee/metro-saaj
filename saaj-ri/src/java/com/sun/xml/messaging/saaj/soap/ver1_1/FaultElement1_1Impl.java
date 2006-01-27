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
 * $Id: FaultElement1_1Impl.java,v 1.1.1.1 2006-01-27 13:10:57 kumarjayanti Exp $
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

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.impl.FaultElementImpl;
import com.sun.xml.messaging.saaj.soap.name.NameImpl;

public class FaultElement1_1Impl extends FaultElementImpl {

    public FaultElement1_1Impl(SOAPDocumentImpl ownerDoc, NameImpl qname) {
        super(ownerDoc, qname);
    }

    public FaultElement1_1Impl(SOAPDocumentImpl ownerDoc, QName qname) {
        super(ownerDoc, qname);
    }

    public FaultElement1_1Impl(SOAPDocumentImpl ownerDoc,
                               String localName) {
        super(ownerDoc, NameImpl.createFaultElement1_1Name(localName));
    }
    
    public FaultElement1_1Impl(SOAPDocumentImpl ownerDoc,
                               String localName,
                               String prefix) {
        super(ownerDoc,
              NameImpl.createFaultElement1_1Name(localName, prefix));
    }

    protected boolean isStandardFaultElement() {
        String localName = elementQName.getLocalPart(); 
        if (localName.equalsIgnoreCase("faultcode") ||
            localName.equalsIgnoreCase("faultstring") ||
            localName.equalsIgnoreCase("faultactor")) {
            return true;
        }
        return false;
    }

    public SOAPElement setElementQName(QName newName) throws SOAPException {
        if (!isStandardFaultElement()) {
            FaultElement1_1Impl copy =
                new FaultElement1_1Impl((SOAPDocumentImpl) getOwnerDocument(), newName);
            return replaceElementWithSOAPElement(this,copy);
        } else {
            return super.setElementQName(newName);
        }
    }
}
