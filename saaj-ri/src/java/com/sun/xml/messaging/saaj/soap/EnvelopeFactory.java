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
 * $Id: EnvelopeFactory.java,v 1.1.1.1 2006-01-27 13:10:55 kumarjayanti Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2006-01-27 13:10:55 $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sun.xml.messaging.saaj.soap;

import java.util.logging.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.soap.SOAPException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.sun.xml.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.messaging.saaj.util.*;

import com.sun.xml.messaging.saaj.util.transform.EfficientStreamingTransformer;

/**
 * EnvelopeFactory creates SOAP Envelope objects using different
 * underlying implementations.
 */
public class EnvelopeFactory {
    
    protected static Logger
        log = Logger.getLogger(LogDomainConstants.SOAP_DOMAIN,
        "com.sun.xml.messaging.saaj.soap.LocalStrings");
    
    private static ParserPool parserPool = new ParserPool(5);
        
    public static Envelope createEnvelope(Source src, SOAPPartImpl soapPart)
        throws SOAPException 
    {
        // Insert SAX filter to disallow Document Type Declarations since
        // they are not legal in SOAP
        SAXParser saxParser = null;
        if (src instanceof StreamSource) {
            if (src instanceof JAXMStreamSource) {
                try {
                    ((JAXMStreamSource) src).reset();
                } catch (java.io.IOException ioe) {
                    log.severe("SAAJ0515.source.reset.exception");
                    throw new SOAPExceptionImpl(ioe);
                }
            }
            try {
                saxParser = parserPool.get();
            } catch (Exception e) {
                log.severe("SAAJ0601.util.newSAXParser.exception");
                throw new SOAPExceptionImpl(
                    "Couldn't get a SAX parser while constructing a envelope",
                    e);
            }
            InputSource is = SAXSource.sourceToInputSource(src);
            XMLReader rejectFilter;
            try {
                rejectFilter = new RejectDoctypeSaxFilter(saxParser);
            } catch (Exception ex) {
                log.severe("SAAJ0510.soap.cannot.create.envelope");
                throw new SOAPExceptionImpl(
                    "Unable to create envelope from given source: ",
                    ex);
            }
            src = new SAXSource(rejectFilter, is);
        }
        
        try {
            Transformer transformer =
                EfficientStreamingTransformer.newTransformer();
            DOMResult result = new DOMResult(soapPart);
            transformer.transform(src, result);
            
            Envelope env = (Envelope) soapPart.getEnvelope();
            if (saxParser != null) {
                parserPool.put(saxParser);
            }
            return env;
        } catch (Exception ex) {
            if (ex instanceof SOAPVersionMismatchException) {
                throw (SOAPVersionMismatchException) ex;
            }
            log.severe("SAAJ0511.soap.cannot.create.envelope");
            throw new SOAPExceptionImpl(
                "Unable to create envelope from given source: ",
                ex);
        }
    }
}
