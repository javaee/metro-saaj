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

package soap;

import javax.xml.soap.*;
import javax.xml.transform.stream.StreamSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import junit.framework.TestCase;

/**
 * These unit tests are based on one of the TCK tests
 */
public class XmlDeclarationUtf16Test extends TestCase {

    private static final String GoodSoapMessage =
        "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:tns=\"http://helloservice.org/wsdl\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><tns:hello><String_1 xsi:type=\"xsd:string\">&lt;Bozo&gt;</String_1></tns:hello></soap:Body></soap:Envelope>";

    public XmlDeclarationUtf16Test(String name) {
        super(name);
    }
    
    public void test1VerifyXmlDeclarationUtf16() throws Exception {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPPart sp = message.getSOAPPart();
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        SOAPHeader header = envelope.getHeader();
        ByteArrayInputStream bais =
            new ByteArrayInputStream(GoodSoapMessage.getBytes());
        StreamSource ssrc=new StreamSource(bais);
        sp.setContent(ssrc);
        message.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");
        message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "utf-16");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        String decoded = baos.toString("utf-16");
        //String decoded = baos.toString();
        assertTrue(
            decoded.indexOf("<?xml") != -1 && decoded.indexOf("utf-16") != -1);
    }

    public void test2VerifyXmlDeclarationUtf16() throws Exception {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPPart sp = message.getSOAPPart();
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        SOAPHeader header = envelope.getHeader();
        StringReader reader = new StringReader(GoodSoapMessage);
        StreamSource ssrc=new StreamSource(reader);
        sp.setContent(ssrc);
        message.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");
        message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "utf-16");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        String decoded = baos.toString("utf-16");
        //String decoded = baos.toString();
        assertTrue(
            decoded.indexOf("<?xml") != -1 && decoded.indexOf("utf-16") != -1);
    }

    public void testVerifyNoXmlDeclarationOutput() throws Exception {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPPart sp = message.getSOAPPart();
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        SOAPHeader header = envelope.getHeader();
        ByteArrayInputStream bais =
            new ByteArrayInputStream(GoodSoapMessage.getBytes());
        StreamSource ssrc = new StreamSource(bais);
        sp.setContent(ssrc);
        message.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "false");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        String soapmessage = new String(baos.toByteArray());
        assertFalse(soapmessage.indexOf("<?xml") != -1);
    }

    //CR:4952752
    public void testVerifyXmlDeclUtf16() throws Exception {
         MessageFactory factory = MessageFactory.newInstance();
         String xml = "<?xml version=\"1.0\" encoding=\"UTF-16\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><SOAP-ENV:Body><p:content SOAP-ENV:encodingStyle=\"http://example.com/encoding\" xmlns:p=\"some-uri\">Jeu universel de caract�res cod�s � plusieurs octets</p:content></SOAP-ENV:Body></SOAP-ENV:Envelope>";
           SOAPMessage msg = factory.createMessage();
           msg.getMimeHeaders().setHeader("Content-Type","text/xml; charset=utf-16");
           msg.getSOAPPart().setContent(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-16"))));
           msg.saveChanges();
           String[] contentType = msg.getMimeHeaders().getHeader("Content-Type");
           if((contentType != null)&&(contentType.length > 0) && 
               (!contentType[0].equals("text/xml; charset=utf-16"))){
               fail();
           } 
    }
}
