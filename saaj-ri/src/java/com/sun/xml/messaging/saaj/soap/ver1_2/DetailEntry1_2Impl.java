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
 * $Id: DetailEntry1_2Impl.java,v 1.1.1.1 2006-01-27 13:10:57 kumarjayanti Exp $
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

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPElement;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.impl.DetailEntryImpl;

public class DetailEntry1_2Impl extends DetailEntryImpl {

    public DetailEntry1_2Impl(SOAPDocumentImpl ownerDoc, Name qname) {
        super(ownerDoc, qname);
    }

    public DetailEntry1_2Impl(SOAPDocumentImpl ownerDoc, QName qname) {
        super(ownerDoc, qname);
    }

    public SOAPElement setElementQName(QName newName) throws SOAPException {
        DetailEntryImpl copy =
            new DetailEntry1_2Impl((SOAPDocumentImpl) getOwnerDocument(), newName);
        return replaceElementWithSOAPElement(this,copy);
    }

}
