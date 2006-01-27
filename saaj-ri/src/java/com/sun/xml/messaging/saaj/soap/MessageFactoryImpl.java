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
 * $Id: MessageFactoryImpl.java,v 1.1.1.1 2006-01-27 13:10:56 kumarjayanti Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2006-01-27 13:10:56 $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sun.xml.messaging.saaj.soap;

import java.io.*;
import java.util.logging.Logger;

import javax.xml.soap.*;

import com.sun.xml.messaging.saaj.packaging.mime.internet.ContentType;
import com.sun.xml.messaging.saaj.packaging.mime.internet.ParseException;
import com.sun.xml.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.messaging.saaj.soap.ver1_1.Message1_1Impl;
import com.sun.xml.messaging.saaj.soap.ver1_2.Message1_2Impl;
import com.sun.xml.messaging.saaj.util.LogDomainConstants;
import com.sun.xml.messaging.saaj.util.TeeInputStream;

/**
 * A factory for creating SOAP messages.
 *
 * Converted to a placeholder for common functionality between SOAP
 * implementations.
 *
 * @author Phil Goodwin (phil.goodwin@sun.com)
 */
public class MessageFactoryImpl extends MessageFactory {

    protected static Logger log =
        Logger.getLogger(LogDomainConstants.SOAP_DOMAIN,
                         "com.sun.xml.messaging.saaj.soap.LocalStrings");

    protected static OutputStream listener;

    protected boolean lazyAttachments = false;
    
    public static OutputStream listen(OutputStream newListener) {
        OutputStream oldListener = listener;
        listener = newListener;
        return oldListener;
    }
    
    public SOAPMessage createMessage() throws SOAPException {
        throw new UnsupportedOperationException();
    }

    public SOAPMessage createMessage(boolean isFastInfoset, 
        boolean acceptFastInfoset) throws SOAPException 
    {
        throw new UnsupportedOperationException();
    }
    
    public SOAPMessage createMessage(MimeHeaders headers, InputStream in)
        throws SOAPException, IOException {
        String contentTypeString = MessageImpl.getContentType(headers);

        if (listener != null) {
            in = new TeeInputStream(in, listener);
        }

        try {
            ContentType contentType = new ContentType(contentTypeString);
            int stat = MessageImpl.identifyContentType(contentType);

            if (MessageImpl.isSoap1_1Content(stat)) {
                return new Message1_1Impl(headers,contentType,stat,in);
            } else if (MessageImpl.isSoap1_2Content(stat)) {
                return new Message1_2Impl(headers,contentType,stat,in);
            } else {
                log.severe("SAAJ0530.soap.unknown.Content-Type");
                throw new SOAPExceptionImpl("Unrecognized Content-Type");
            }
        } catch (ParseException e) {            
            log.severe("SAAJ0531.soap.cannot.parse.Content-Type");
            throw new SOAPExceptionImpl(
                "Unable to parse content type: " + e.getMessage());
        }
    }

    protected static final String getContentType(MimeHeaders headers) {
        String[] values = headers.getHeader("Content-Type");
        if (values == null)
            return null;
        else
            return values[0];
    }

    public void setLazyAttachmentOptimization(boolean flag) {
        lazyAttachments = flag;
    }

}
