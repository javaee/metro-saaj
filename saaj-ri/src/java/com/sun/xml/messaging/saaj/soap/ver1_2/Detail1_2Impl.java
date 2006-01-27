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
 * $Id: Detail1_2Impl.java,v 1.1.1.1 2006-01-27 13:10:57 kumarjayanti Exp $
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
import javax.xml.soap.*;

import com.sun.xml.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.messaging.saaj.soap.SOAPDocument;
import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.impl.DetailImpl;
import com.sun.xml.messaging.saaj.soap.name.NameImpl;

public class Detail1_2Impl extends DetailImpl {

    protected static Logger log =
        Logger.getLogger(Detail1_2Impl.class.getName(),
                         "com.sun.xml.messaging.saaj.soap.ver1_2.LocalStrings");

    public Detail1_2Impl(SOAPDocumentImpl ownerDocument, String prefix) {
        super(ownerDocument, NameImpl.createSOAP12Name("Detail", prefix));
    }

    public Detail1_2Impl(SOAPDocumentImpl ownerDocument) {
        super(ownerDocument, NameImpl.createSOAP12Name("Detail"));
    }

    protected DetailEntry createDetailEntry(Name name) {
        return new DetailEntry1_2Impl(
            ((SOAPDocument) getOwnerDocument()).getDocument(),
            name);
    }

    protected DetailEntry createDetailEntry(QName name) {
        return new DetailEntry1_2Impl(
            ((SOAPDocument) getOwnerDocument()).getDocument(),
            name);
    }

    /*
     * Override setEncodingStyle of ElementImpl to restrict adding encodingStyle
     * attribute to SOAP Detail (SOAP 1.2 spec, part 1, section 5.1.1)
     */
    public void setEncodingStyle(String encodingStyle) throws SOAPException {        
        log.severe("SAAJ0403.ver1_2.no.encodingStyle.in.detail");
        throw new SOAPExceptionImpl("EncodingStyle attribute cannot appear in Detail");
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
