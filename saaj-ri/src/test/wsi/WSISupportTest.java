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

/**
 * 
 */

package wsi;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import javax.xml.soap.*;
import javax.xml.transform.dom.DOMSource;

import junit.framework.TestCase;

/*
 * Unit test cases to test out the ws-i support.
 *
 * @author Manveen Kaur (manveen.kaur@sun.com)
 */

public class WSISupportTest extends TestCase {

    public WSISupportTest(String name) {
        super(name);
    }

    private SOAPMessage createMessage() throws SOAPException {
        MessageFactory msgFactory = MessageFactory.newInstance();

        SOAPMessage msg = msgFactory.createMessage();

        return msg;
    }

    /*
     * Test to verify set char encoding.
     */
    public void testCharEncoding() {

        try {

            SOAPMessage message = createMessage();
            SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
            SOAPHeader hdr = envelope.getHeader();

            // create a header element
            SOAPHeaderElement transaction =
                hdr.addHeaderElement(
                    envelope.createName("Transaction", "t", "some-uri"));

            transaction.addTextNode("5");

            message.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");
            // set char encoding to utf-16
            message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "utf-16");
            //message.writeTo(System.out);

            //System.out.println("\n\nContent type = " + message.getContentType());
        } catch (Exception e) {
            // e.printStackTrace();
            fail("No exception should be thrown: " + e);
        }
    }

    public void xtestContentType() {

        try {
            MessageFactory factory = MessageFactory.newInstance();
            SOAPMessage message = factory.createMessage();
            SOAPPart part = message.getSOAPPart();
            SOAPEnvelope envelope = part.getEnvelope();
            SOAPHeader hdr = envelope.getHeader();
            SOAPBody body = envelope.getBody();
            SOAPFault fault = body.addFault();

            part.setMimeHeader("Content-Type", "text/html");
            fault.setFaultCode("100");
            fault.setFaultString("some reason for fault");

            fault.addChildElement("test");
            //message.writeTo(System.out);

            // System.out.println("\n\nContent type = " + message.getContentType());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testFaultDetail() throws Exception {

        String RETAIL_ORDER_NAMESPACE = "http://www.Something.com";

        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPPart part = message.getSOAPPart();
        SOAPEnvelope envelope = part.getEnvelope();
        SOAPHeader hdr = envelope.getHeader();
        SOAPBody body = envelope.getBody();

        SOAPFault fault = body.addFault();
        //Detail detail = fault.addDetail();

        SOAPFactory soapFactory = SOAPFactory.newInstance();
        Detail detail = soapFactory.createDetail();
        Name name =
            soapFactory.createName(
                "InvalidProductCode",
                "ns1",
                RETAIL_ORDER_NAMESPACE);
        DetailEntry detailEntry = detail.addDetailEntry(name);
        SOAPElement soapElement =
            detailEntry.addChildElement(
                "Reason",
                "ns1",
                RETAIL_ORDER_NAMESPACE);
        soapElement.addTextNode("InvalidProductCode");
        soapElement =
            detailEntry.addChildElement(
                "ProductNumber",
                "ns1",
                RETAIL_ORDER_NAMESPACE);
        soapElement.addTextNode("600510");

        DOMSource source = new DOMSource(detail);

        fault.addChildElement(detail);

        String expected =
            "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/"
                + "soap/envelope/\"><SOAP-ENV:Header/><SOAP-ENV:Body><SOAP-ENV:"
                + "Fault><faultcode>SOAP-ENV:Server</faultcode><faultstring>"
                + "Fault string, and possibly fault code, not set</faultstring>"
                + "<detail><ns1:InvalidProductCode xmlns:ns1=\"http://www."
                + "Something.com\"><ns1:Reason>InvalidProductCode</ns1:Reason><ns1:"
                + "ProductNumber>600510</ns1:ProductNumber></ns1:InvalidProductCode>"
                + "</detail></SOAP-ENV:Fault></SOAP-ENV:Body></SOAP-ENV:Envelope>";

        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();

        message.writeTo(bytesOut);
        //message.writeTo(System.out);

        assertEquals(expected, bytesOut.toString());

    }

    public void testFaultLocale() throws Exception {

        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPPart part = message.getSOAPPart();
        SOAPEnvelope envelope = part.getEnvelope();
        SOAPHeader hdr = envelope.getHeader();
        SOAPBody body = envelope.getBody();

        // add fault here
        SOAPFault fault = body.addFault();
        // Setting fault string to locale en_US
        fault.setFaultString("something", Locale.US);

        assertTrue(
            "Locale should be english",
            Locale.US.equals(fault.getFaultStringLocale()));

    }

    public static void main(String argv[]) {

        junit.textui.TestRunner.run(WSISupportTest.class);

    }

}
