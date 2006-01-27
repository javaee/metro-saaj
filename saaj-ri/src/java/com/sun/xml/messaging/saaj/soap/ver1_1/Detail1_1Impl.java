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
 * $Id: Detail1_1Impl.java,v 1.1.1.1 2006-01-27 13:10:57 kumarjayanti Exp $
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
import javax.xml.soap.DetailEntry;
import javax.xml.soap.Name;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.impl.DetailImpl;
import com.sun.xml.messaging.saaj.soap.name.NameImpl;

public class Detail1_1Impl extends DetailImpl {

    public Detail1_1Impl(SOAPDocumentImpl ownerDoc, String prefix) {
        super(ownerDoc, NameImpl.createDetail1_1Name(prefix));
    }
    public Detail1_1Impl(SOAPDocumentImpl ownerDoc) {
        super(ownerDoc, NameImpl.createDetail1_1Name());
    }
    protected DetailEntry createDetailEntry(Name name) {
        return new DetailEntry1_1Impl(
            (SOAPDocumentImpl) getOwnerDocument(),
            name);
    }
    protected DetailEntry createDetailEntry(QName name) {
        return new DetailEntry1_1Impl(
            (SOAPDocumentImpl) getOwnerDocument(),
            name);
    }

}
