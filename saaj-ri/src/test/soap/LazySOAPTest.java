/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2017 Oracle and/or its affiliates. All rights reserved.
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

package soap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.messaging.saaj.soap.MessageImpl;

import junit.framework.TestCase;

public class LazySOAPTest extends TestCase {
    public void testStaxCorrectness() throws Exception {
        //make a small message
        String soapMsg = makeSoapMessageString(3);
        MimeHeaders headers = new MimeHeaders();
        headers.addHeader("Content-Type", "text/xml");
        InputStream in = new ByteArrayInputStream(soapMsg.getBytes("UTF-8")); 
//      System.out.println("soap msg: " + soapMsg);
        SOAPMessage msg = MessageFactory.newInstance().createMessage(headers, in);
        msg.setProperty(MessageImpl.LAZY_SOAP_BODY_PARSING, "true");
        SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
        traverseStaxSoapMessageForCorrectness(env);
        
        //write and re-read, make sure it's still correct
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        msg.writeTo(bos);
        msg = MessageFactory.newInstance().createMessage(headers, new ByteArrayInputStream(bos.toByteArray()));
        env = msg.getSOAPPart().getEnvelope();
        traverseStaxSoapMessageForCorrectness(env);
        
    }
    
    public void testLazyEnvelopeWrite() throws Exception {
      //make a small message
        String soapMsg = makeSoapMessageString(3);
        MimeHeaders headers = new MimeHeaders();
        headers.addHeader("Content-Type", "text/xml");
        InputStream in = new ByteArrayInputStream(soapMsg.getBytes("UTF-8")); 
//      System.out.println("soap msg: " + soapMsg);
        SOAPMessage msg = MessageFactory.newInstance().createMessage(headers, in);
        msg.setProperty(MessageImpl.LAZY_SOAP_BODY_PARSING, "true");
        //force envelope to be parsed
        SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
        
        //now write lazy
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter w = XMLOutputFactory.newInstance().createXMLStreamWriter(bos);
//        ((LazyEnvelope) env).writeTo(w);
        Method method = env.getClass().getMethod("writeTo", new Class[]{XMLStreamWriter.class});
        method.invoke(env, new Object[]{w});
        w.flush();
        //TODO desagar Body is EMPTY!! Why? Fix it
        String writtenSoap = bos.toString();
        System.out.println(writtenSoap);
        assertTrue(writtenSoap.indexOf("Hello") > 0);
        

    }
    
    public void testLazySoapFault() throws Exception {
        String soapMsg = makeSoapFaultMessageString();
        MimeHeaders headers = new MimeHeaders();
        headers.addHeader("Content-Type", "text/xml");
        InputStream in = new ByteArrayInputStream(soapMsg.getBytes("UTF-8")); 
//      System.out.println("soap msg: " + soapMsg);
        SOAPMessage msg = MessageFactory.newInstance().createMessage(headers, in);
        msg.setProperty(MessageImpl.LAZY_SOAP_BODY_PARSING, "true");
        //force envelope to be parsed
        SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
        SOAPFault fault = env.getBody().getFault();
        assertNotNull(fault);
    }
    /**
     * Use DOM APIs to traverse to make sure message is materialized correctly
     * @param elem
     */
    private void traverseStaxSoapMessageForCorrectness(SOAPElement elem) {
        assertEquals("Envelope", elem.getLocalName());
        Node child;
        child = getElementChild(elem);
        assertTrue(child instanceof javax.xml.soap.SOAPHeader);
        assertEquals("Header", child.getLocalName());
        
        child = getElementSibling(child);
        assertTrue(child instanceof javax.xml.soap.SOAPBody);
        assertEquals("Body", child.getLocalName());
        
        child = getElementChild(child);
        assertEquals("Hello", child.getLocalName());
        
        NodeList children = child.getChildNodes();
        int elemChildren = 0;
        for (int i=0;i<children.getLength();i++) {
            Node nextChild = children.item(i);
            if (nextChild.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            elemChildren++;
            assertEquals("String_1", nextChild.getLocalName());
            
        }
        assertEquals(3, elemChildren);
    }
    
    private Node getElementSibling(Node elem) {
        Node sib;
        do {
            sib = elem.getNextSibling();
        } while (sib != null && sib.getNodeType() != Node.ELEMENT_NODE);
        return sib;
    }

    private Node getElementChild(Node elem) {
        Node child;
        for (child = elem.getFirstChild();child != null && child.getNodeType() != Node.ELEMENT_NODE;child = child.getNextSibling());
        return child;
    }
    
    private String makeSoapMessageString(int numPayloadElems) {
        String soapPrefix = "<env:Envelope"
                        + " xmlns:env='http://schemas.xmlsoap.org/soap/envelope/'"
                        + " xmlns:ns1='http://example.com/wsdl'>"
                        +    "<env:Header/>"
                        +    "<env:Body>"
                        +      "<ns1:Hello>";

        String soapSuffix =
        "</ns1:Hello>"
                +    "</env:Body>"
                + "</env:Envelope>";
        
        StringBuilder soap = new StringBuilder(soapPrefix);
        for (int i=0;i<numPayloadElems;i++) {
            soap.append("<String_1>Duke " + i + "</String_1>");
        }
        soap.append(soapSuffix);
        return soap.toString();
    }
    
    private String makeSoapFaultMessageString() {
        String soapPrefix = "<env:Envelope"
                        + " xmlns:env='http://schemas.xmlsoap.org/soap/envelope/'"
                        + " xmlns:ns1='http://example.com/wsdl'>"
                        +    "<env:Header/>"
                        +    "<env:Body>"
                        +      "<env:Fault>";

        String soapSuffix =
        "</env:Fault>"
                +    "</env:Body>"
                + "</env:Envelope>";
        
        StringBuilder soap = new StringBuilder(soapPrefix);
        soap.append("<env:faultcode>myfaultcode</env:faultcode>");
        soap.append("<env:faultstring>myfaultcode</env:faultstring>");
        soap.append("<env:faultactor>myfaultactor</env:faultactor>");
        soap.append("<env:detail><mydetailelem/></env:detail>");

        soap.append(soapSuffix);
        return soap.toString();
    }
}
