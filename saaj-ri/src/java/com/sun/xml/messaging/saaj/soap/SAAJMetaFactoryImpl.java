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
package com.sun.xml.messaging.saaj.soap;

import javax.xml.soap.SAAJMetaFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPException;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.xml.messaging.saaj.util.LogDomainConstants;

public class SAAJMetaFactoryImpl extends SAAJMetaFactory {

    protected static Logger log =
        Logger.getLogger(LogDomainConstants.SOAP_DOMAIN,
                         "com.sun.xml.messaging.saaj.soap.LocalStrings");

    protected  MessageFactory newMessageFactory(String protocol)
        throws SOAPException {
        if (SOAPConstants.SOAP_1_1_PROTOCOL.equals(protocol)) {
              return new com.sun.xml.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl();
        } else if (SOAPConstants.SOAP_1_2_PROTOCOL.equals(protocol)) {
              return new com.sun.xml.messaging.saaj.soap.ver1_2.SOAPMessageFactory1_2Impl();
        } else if (SOAPConstants.DYNAMIC_SOAP_PROTOCOL.equals(protocol)) {
              return new com.sun.xml.messaging.saaj.soap.dynamic.SOAPMessageFactoryDynamicImpl(); 
        } else {
            log.log(
                Level.SEVERE,
                "SAAJ0569.soap.unknown.protocol",
                new Object[] {protocol, "MessageFactory"});
            throw new SOAPException("Unknown Protocol: " + protocol + 
                                        "  specified for creating MessageFactory");
        }
    }

    protected  SOAPFactory newSOAPFactory(String protocol)
        throws SOAPException {
        if (SOAPConstants.SOAP_1_1_PROTOCOL.equals(protocol)) {
            return new com.sun.xml.messaging.saaj.soap.ver1_1.SOAPFactory1_1Impl();
        } else if (SOAPConstants.SOAP_1_2_PROTOCOL.equals(protocol)) {
            return new com.sun.xml.messaging.saaj.soap.ver1_2.SOAPFactory1_2Impl();
        } else if (SOAPConstants.DYNAMIC_SOAP_PROTOCOL.equals(protocol)) {
            return new com.sun.xml.messaging.saaj.soap.dynamic.SOAPFactoryDynamicImpl(); 
        } else {
            log.log(
                Level.SEVERE,
                "SAAJ0569.soap.unknown.protocol",
                new Object[] {protocol, "SOAPFactory"});
            throw new SOAPException("Unknown Protocol: " + protocol + 
                                        "  specified for creating SOAPFactory");
        }
    }

}
