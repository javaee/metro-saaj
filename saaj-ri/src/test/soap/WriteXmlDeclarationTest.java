/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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
