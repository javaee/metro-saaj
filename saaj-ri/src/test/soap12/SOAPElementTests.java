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

package soap12;

import javax.xml.soap.*;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.*;

import java.io.*;

import junit.framework.TestCase;


public class SOAPElementTests extends TestCase {

    public SOAPElementTests(String name) throws Exception {
        super(name);
    }

    public void testSetEncodingStyle1() throws Exception {
        MessageFactory mFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage msg = mFactory.createMessage();
        SOAPBody body = msg.getSOAPBody();
        SOAPBodyElement bodyElement =
            body.addBodyElement(new QName("some-uri", "content", "p"));
        bodyElement.setEncodingStyle("http://example.com/encoding");
        assertEquals(
            bodyElement.getAttributeValue(
                new QName(
                    SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE,
                    "encodingStyle")),
            "http://example.com/encoding");
    }

    /**
     * Testcase for CR ID 6213337
     */
    public void testSetEncodingStyle2() throws Exception {
        MessageFactory mFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPFactory sFactory = SOAPFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage msg = mFactory.createMessage();
        SOAPBody body = msg.getSOAPBody();
        SOAPElement bodyElement = sFactory.createElement("content", "p", "some-uri");
        bodyElement.setEncodingStyle("http://example.com/encoding");
        SOAPBodyElement addedElement = (SOAPBodyElement) body.addChildElement(bodyElement);
        assertEquals(
            addedElement.getAttributeValue(
                new QName(
                    SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE,
                    "encodingStyle")),
            "http://example.com/encoding");
    }

    /**
     * Testcase for CR ID 6213350
     */
    public void testGetEncodingStyle() throws Exception {
        String xml =
            "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\"><SOAP-ENV:Header/><SOAP-ENV:Body><p:content SOAP-ENV:encodingStyle=\"http://example.com/encoding\" xmlns:p=\"some-uri\"/></SOAP-ENV:Body></SOAP-ENV:Envelope>";
        MessageFactory mFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage msg = mFactory.createMessage();
        SOAPPart soapPart = msg.getSOAPPart();
        soapPart.setContent(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
        SOAPBodyElement element =
            (SOAPBodyElement) msg.getSOAPBody().getChildElements().next();
        assertNotNull(element.getEncodingStyle());
    }
}
