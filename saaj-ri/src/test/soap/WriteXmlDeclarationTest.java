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

package soap;

import javax.xml.soap.*;
import javax.xml.transform.stream.StreamSource;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.StringReader;

import junit.framework.TestCase;

/**
 * Tests for Bug Id: 4952738
 */
public class WriteXmlDeclarationTest extends TestCase {

    private static final String xmlDecl =
        "<?xml version=\"1.0\" encoding=\"utf-8\"";

    public WriteXmlDeclarationTest(String name) {
        super(name);
    }

    public void testXmlDeclWithInputStream1() throws Exception {
        String testString = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body/></soap:Envelope>";
        byte[] utf8bytes = testString.getBytes("utf-8");
        ByteArrayInputStream bais = new ByteArrayInputStream(utf8bytes);
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        SOAPPart soapPart = msg.getSOAPPart();
        soapPart.setContent(new StreamSource(bais));
        soapPart.getEnvelope();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        msg.writeTo(baos);
        String msgString = baos.toString("utf-8");
        assertTrue(msgString.startsWith(xmlDecl));
    }

    public void testXmlDeclWithInputStream2() throws Exception {
        String testString = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body/></soap:Envelope>";
        byte[] utf8bytes = testString.getBytes("utf-8");
        ByteArrayInputStream bais = new ByteArrayInputStream(utf8bytes);
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        SOAPPart soapPart = msg.getSOAPPart();
        soapPart.setContent(new StreamSource(bais));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        msg.writeTo(baos);
        String msgString = baos.toString("utf-8");
        assertTrue(msgString.startsWith(xmlDecl));
    }

    public void testXmlDeclWithReader1() throws Exception {
        String testString = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body/></soap:Envelope>";
        StringReader reader = new StringReader(testString);
        StreamSource content = new StreamSource(reader);
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        SOAPPart soapPart = msg.getSOAPPart();
        soapPart.setContent(content);
        soapPart.getEnvelope();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        msg.writeTo(baos);
        String msgString = baos.toString("utf-8");
        assertTrue(msgString.startsWith(xmlDecl));
    }

    public void testXmlDeclWithReader2() throws Exception {
        String testString = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body/></soap:Envelope>";
        StringReader reader = new StringReader(testString);
        StreamSource content = new StreamSource(reader);
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        SOAPPart soapPart = msg.getSOAPPart();
        soapPart.setContent(content);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        msg.writeTo(baos);
        String msgString = baos.toString("utf-8");
        assertTrue(msgString.startsWith(xmlDecl));
    }
}
