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
 * $Id: BodyElementImpl.java,v 1.1.1.1 2006-01-27 13:10:56 kumarjayanti Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2006-01-27 13:10:56 $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sun.xml.messaging.saaj.soap.impl;

import javax.xml.namespace.QName;
import javax.xml.soap.*;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;

/**
 * All elements of the SOAP-ENV:BODY.
 *
 * @author Anil Vijendran (akv@eng.sun.com)
 */
public abstract class BodyElementImpl
    extends ElementImpl
    implements SOAPBodyElement {
        
    public BodyElementImpl(SOAPDocumentImpl ownerDoc, Name qname) {
        super(ownerDoc, qname);
    }

    public BodyElementImpl(SOAPDocumentImpl ownerDoc, QName qname) {
        super(ownerDoc, qname);
    }

    public void setParentElement(SOAPElement element) throws SOAPException {
        if (! (element instanceof SOAPBody)) {
            log.severe("SAAJ0101.impl.parent.of.body.elem.mustbe.body");
            throw new SOAPException("Parent of a SOAPBodyElement has to be a SOAPBody");
        }
        super.setParentElement(element);
    }


}
