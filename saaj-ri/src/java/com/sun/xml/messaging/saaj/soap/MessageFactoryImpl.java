/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
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

package com.sun.xml.messaging.saaj.soap;

import java.io.*;
import java.util.logging.Logger;

import javax.xml.soap.*;
import javax.xml.stream.XMLStreamReader;

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

    protected static final Logger log =
        Logger.getLogger(LogDomainConstants.SOAP_DOMAIN,
                         "com.sun.xml.messaging.saaj.soap.LocalStrings");

    protected  OutputStream listener;

    protected boolean lazyAttachments = false;
    
    public  OutputStream listen(OutputStream newListener) {
        OutputStream oldListener = listener;
        listener = newListener;
        return oldListener;
    }
    
    @Override
    public SOAPMessage createMessage() throws SOAPException {
        throw new UnsupportedOperationException();
    }
    
    public SOAPMessage createMessage(String protocol) throws SOAPException {
    	if (SOAPConstants.SOAP_1_1_PROTOCOL.equals(protocol))
    		return new com.sun.xml.messaging.saaj.soap.ver1_1.Message1_1Impl();
    	else
    		return new com.sun.xml.messaging.saaj.soap.ver1_2.Message1_2Impl();
    }

    public SOAPMessage createMessage(boolean isFastInfoset, 
        boolean acceptFastInfoset) throws SOAPException 
    {
        throw new UnsupportedOperationException();
    }
    
    public SOAPMessage createMessage(MimeHeaders headers, XMLStreamReader reader) throws SOAPException, IOException {
        String contentTypeString = MessageImpl.getContentType(headers);

        if (listener != null) {
            throw new SOAPException("Listener OutputStream is not supported with XMLStreamReader");
        }

        try {
            ContentType contentType = new ContentType(contentTypeString);
            int stat = MessageImpl.identifyContentType(contentType);

            if (MessageImpl.isSoap1_1Content(stat)) {
                return new Message1_1Impl(headers,contentType,stat,reader);
            } else if (MessageImpl.isSoap1_2Content(stat)) {
                return new Message1_2Impl(headers,contentType,stat,reader);
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
    @Override
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
