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
 * $Id: SOAPPart1_1Impl.java,v 1.1.1.1 2006-01-27 13:10:57 kumarjayanti Exp $
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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.transform.Source;

import com.sun.xml.messaging.saaj.soap.*;
import com.sun.xml.messaging.saaj.soap.impl.EnvelopeImpl;
import com.sun.xml.messaging.saaj.util.LogDomainConstants;

public class SOAPPart1_1Impl extends SOAPPartImpl implements SOAPConstants {

    protected static Logger log =
        Logger.getLogger(LogDomainConstants.SOAP_VER1_1_DOMAIN,
                         "com.sun.xml.messaging.saaj.soap.ver1_1.LocalStrings");

    public SOAPPart1_1Impl() {
        super();
     }
    
    public SOAPPart1_1Impl(MessageImpl message) {
        super(message);
    }

    protected String getContentType() {
        return isFastInfoset() ? "application/fastinfoset" : "text/xml";
    }

    protected Envelope createEnvelopeFromSource() throws SOAPException {
        // Record the presence of xml declaration before the envelope gets
        // created.
        String xmlDecl = lookForXmlDecl();
        Source tmp = source;
        source = null;
        EnvelopeImpl envelope =
            (EnvelopeImpl) EnvelopeFactory.createEnvelope(tmp, this);

        if (!envelope.getNamespaceURI().equals(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE)) {
            log.severe("SAAJ0304.ver1_1.msg.invalid.SOAP1.1");
            throw new SOAPException("InputStream does not represent a valid SOAP 1.1 Message");
        }

        if (!omitXmlDecl) {
            envelope.setOmitXmlDecl("no");
            envelope.setXmlDecl(xmlDecl);
        }
        return envelope;
    }

    protected Envelope createEmptyEnvelope(String prefix)
        throws SOAPException {
        return new Envelope1_1Impl(getDocument(), prefix, true, true);
    }

    protected SOAPPartImpl duplicateType() {
        return new SOAPPart1_1Impl();
    }

}
