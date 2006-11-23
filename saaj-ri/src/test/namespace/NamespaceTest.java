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
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package namespace;

import java.util.Iterator;

import javax.xml.soap.*;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;

import org.w3c.dom.Document;

import com.sun.xml.messaging.saaj.soap.name.NameImpl;

import java.io.*;

/*
 * Tests to check for namespace rules being followed in SOAP message creation.
 */

public class NamespaceTest extends TestCase {

    private SOAPEnvelope envelope;
    private SOAPFactory sFactory;
    private MessageFactory msgFactory;

    public NamespaceTest(String name) {
        super(name);
    }

    protected void setUp() {

        try {
            msgFactory = MessageFactory.newInstance();
            sFactory = SOAPFactory.newInstance();
            SOAPMessage msg = msgFactory.createMessage();
            envelope = msg.getSOAPPart().getEnvelope();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private SOAPMessage createMessage(Name name, String attrib)
        throws SOAPException {
        /*
         * Creating a message.
         */
        SOAPMessage msg = msgFactory.createMessage();

        envelope = msg.getSOAPPart().getEnvelope();

        SOAPHeader hdr = envelope.getHeader();
        SOAPBody bdy = envelope.getBody();

        SOAPElement elem = sFactory.createElement(name);

        if (attrib != null)
            elem.addAttribute(name, attrib);

        SOAPHeaderElement she =
            hdr.addHeaderElement(
                envelope.createName(
                    "HeaderElement1",
                    "he1",
                    "http://foo.xyz.com"));
        she.addChildElement(elem);

        return msg;
    }

    /*
     * xmlns prefix is reserved and cannot be declared
     */
    public void testXmlnsAsPrefix() {

        String exception = null;
        try {

            Name name1 =
                envelope.createName(
                    "foo",
                    "xmlns",
                    "http://www.w3.org/2000/xmlns/");

            SOAPMessage msg = createMessage(name1, "myNameSpaceURI");

            //msg.writeTo(System.out);

        } catch (Exception e) {
            exception = e.getMessage();
        }

        // exception should be thrown if someone tried to declare a namespace prefix with the
        // reserved 'xmlns' word.
        assertTrue("An exception should have been thrown", (exception != null));
    }

    /*
     * Qualified name cannot be xmlns.
     */
    public void testQNameAsPrefix() {

        String exception = null;
        try {

            Name name2 =
                envelope.createName(
                    "xmlns",
                    null,
                    "http://www.w3.org/2000/xmlns/");

            SOAPMessage msg = createMessage(name2, "");

            //msg.writeTo(System.out);

        } catch (Exception e) {
            exception = e.getMessage();
        }

        assertTrue("An exception should have been thrown", (exception != null));
    }

    /*
     * URI cannot be null.
     */
    public void testNullUriInName() {

        String exception = null;
        try {

            Name name3 = envelope.createName("test", "prefix", null);

            SOAPMessage msg = createMessage(name3, null);

            //msg.writeTo(System.out);

        } catch (Exception e) {
            exception = e.getMessage();
        }

        assertTrue("An exception should have been thrown", (exception != null));
    }

    /*
     * Test to verify SOAPFactory.createElement(localname);
     */
    public void testCreateElementString() {

        try {
            SOAPMessage msg = msgFactory.createMessage();
            envelope = msg.getSOAPPart().getEnvelope();

            SOAPHeader hdr = envelope.getHeader();
            SOAPBody bdy = envelope.getBody();

            SOAPElement elem = sFactory.createElement("localname");
            bdy.addChildElement(elem);

        } catch (Exception e) {
            // e.printStackTrace();
            fail("No exception should be thrown" + e.getMessage());
        }
    }

    public void testGetNamespacePrefixes() throws Exception {
        SOAPMessage message = msgFactory.createMessage();
        envelope = message.getSOAPPart().getEnvelope();
        SOAPBody body = envelope.getBody();
        body.addNamespaceDeclaration("prefix", "http://aUri");

        Iterator eachPrefix = body.getNamespacePrefixes();

        String prefix;

        assertTrue(eachPrefix.hasNext());
        prefix = (String) eachPrefix.next();
        assertTrue(
            "wrong first prefix: \"" + prefix + "\"",
            "prefix".equalsIgnoreCase(prefix));
        //        assertTrue(eachPrefix.hasNext());
        //        prefix = (String) eachPrefix.next();
        //        assertTrue("wrong second prefix: \""+ prefix+"\"", "SOAP-ENV".equalsIgnoreCase(prefix));

        if (eachPrefix.hasNext()) {
            String errorString = "";
            int count = 0;
            while (eachPrefix.hasNext() && count < 10) {
                prefix = (String) eachPrefix.next();
                errorString =
                    errorString + "Unexpected prefix: " + prefix + "\n";
            }
            if (count == 10) {
                errorString = errorString + "more...";
            }

            fail(errorString);
        }

        eachPrefix = body.getNamespacePrefixes();
        eachPrefix.next();
        eachPrefix.remove();

        //        eachPrefix = body.getNamespacePrefixes();
        //        assertTrue(eachPrefix.hasNext());
        //        prefix = (String) eachPrefix.next();
        //        assertTrue("wrong prefix found after removal: \""+ prefix+"\"", "SOAP-ENV".equalsIgnoreCase(prefix));
        assertTrue(!eachPrefix.hasNext());
    }

    public void testBodyPrefix() throws Exception {
        // Create Envelope element
        SOAPElement env =
            SOAPFactory.newInstance().createElement(
                "Envelope",
                "env",
                "http://schemas.xmlsoap.org/soap/envelope/");
        env.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
        env.addNamespaceDeclaration(
            "xsi",
            "http://www.w3.org/2001/XMLSchema-instance");
        env.addNamespaceDeclaration(
            "enc",
            "http://schemas.xmlsoap.org/soap/encoding/");

        // Insert Envelope element
        SOAPMessage soapMsg = msgFactory.createMessage();
        Document dom = soapMsg.getSOAPPart();
        SOAPElement elem = (SOAPElement) dom.importNode(env, true);
        dom.insertBefore(elem, null);

        //Insert Body element
        elem.addChildElement(
            "Body",
            "env",
            "http://schemas.xmlsoap.org/soap/envelope/");
        soapMsg.saveChanges();

        // Is namespace of Body "env" ?
        SOAPBody body = soapMsg.getSOAPBody();
        assertTrue(body.getPrefix().equals("env"));
    }

    public void testAttrPrefix() throws Exception {
        String msgStr =
            "<env:Envelope "
                + "xmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\" "
                + "xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\" "
                + "xmlns:ns0=\"http://echoservice.org/types4\" "
                + "xmlns:ns1=\"http://java.sun.com/jax-rpc-ri/internal\" "
                + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
                + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                + "<SOAP-ENV:Body "
                + "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "<ans1:echoMapEntryResponse "
                + "env:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" "
                + "xmlns:ans1=\"http://echoservice.org/wsdl\">"
                + "<result xsi:nil=\"1\" xsi:type=\"ns1:mapEntry\"/>"
                + "</ans1:echoMapEntryResponse>"
                + "</SOAP-ENV:Body>"
                + "</env:Envelope>";

        SOAPMessage soapMsg = msgFactory.createMessage();
        soapMsg.getSOAPPart().setContent(
            new StreamSource(new java.io.StringReader(msgStr)));
        soapMsg.saveChanges();
        SOAPBody body = soapMsg.getSOAPBody();
        Iterator i = body.getChildElements();
        SOAPElement elem = null;
        if (i.hasNext()) {
            elem = (SOAPElement) i.next(); // elem=ans1:echoMapEntryResponse
        }
        if (elem != null) {
            i = elem.getChildElements();
            elem = null;
            if (i.hasNext()) {
                elem = (SOAPElement) i.next(); // elem=result
                String got = elem.getNamespaceURI("ns1");
                String expected = "http://java.sun.com/jax-rpc-ri/internal";
                assertTrue(got.equals(expected));
            }
        }
    }

    public void testNamespaceDeclarationAsAttribute() throws Exception {
        SOAPElement element = SOAPFactory.newInstance().createElement(
                "Envelope",
                "env",
                "http://schemas.xmlsoap.org/soap/envelope/");
        element.addAttribute(NameImpl.createFromTagName("xmlns:fooName"), "http://foo");
        
        Iterator eachDeclaration = element.getNamespacePrefixes();
        assertTrue(eachDeclaration.hasNext());
        assertEquals("fooName", (String) eachDeclaration.next());
        if (eachDeclaration.hasNext()) {
            String extraPrefix = (String) eachDeclaration.next();
            fail("An extra namespace declaration was added for: " + extraPrefix);
        }
    }
    
    public void testLookupNamespaceURIDOML3() throws Exception {
        String msg = "<?xml version='1.0' ?><S:Envelope xmlns:S='http://schemas.xmlsoap.org/soap/envelope/'><S:Body><ns2:Fault xmlns:ns2='http://schemas.xmlsoap.org/soap/envelope/' xmlns:ns3='http://www.w3.org/2003/05/soap-envelope'><faultcode>ns3:VersionMismatch</faultcode><faultstring>Couldn't create SOAP message. Expecting Envelope in namespace http://schemas.xmlsoap.org/soap/envelope/, but got http://wrongname.org </faultstring></ns2:Fault></S:Body></S:Envelope>";
        
        
        MessageFactory messageFactory = MessageFactory.newInstance();
        MimeHeaders headers = new MimeHeaders();
        headers.addHeader("Content-Type", "text/xml");
        
        SOAPMessage soapMsg = messageFactory.createMessage(headers,new ByteArrayInputStream(msg.getBytes()));
        soapMsg.writeTo(System.out);
        SOAPBody body = soapMsg.getSOAPPart().getEnvelope().getBody();
        
        SOAPFault fault = (SOAPFault)body.getFault();
        String uri = fault.lookupNamespaceURI("ns3");
        assertTrue(uri.equals("http://www.w3.org/2003/05/soap-envelope"));
         
    }

    public static void main(String argv[]) {

        junit.textui.TestRunner.run(NamespaceTest.class);

    }

}
