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

package namespace;

import javax.xml.soap.*;
import javax.xml.transform.stream.StreamSource;

import java.io.*;

import junit.framework.TestCase;


public class DefaultNamespaceTest extends TestCase {

    /*
     * Testcase for Bug Id 5034339 (Synopsis - Non-namespace-qualified
     * element is assigned a namespace declaration)
     */
    public void testBugId5034339() throws Exception {

        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        SOAPBody body = msg.getSOAPBody();
        Name name = SOAPFactory.newInstance().createName("Content", "", "some-uri");
        body.addBodyElement(name).addChildElement("Value").addTextNode("SUNW");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        msg.writeTo(baos);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        StreamSource source = new StreamSource(bais);

        SOAPMessage reconstructedMsg = MessageFactory.newInstance().createMessage();
        SOAPPart soapPart = reconstructedMsg.getSOAPPart();
        soapPart.setContent(source);
        SOAPEnvelope env = soapPart.getEnvelope();
        SOAPElement content = (SOAPElement) env.getBody().getFirstChild();
        //System.out.println("content.getNamespaceURI() = **" + content.getNamespaceURI() + "**");
        SOAPElement value = (SOAPElement) content.getFirstChild();
        //System.out.println("value.getNamespaceURI() = **" + value.getNamespaceURI() + "**");
        assertNotNull("The namespace of Value element shouldn't have been null", value.getNamespaceURI());
    }

    /*
     * Testcase for Bug Id 6206247 (Synopsis - 3 argument addChildElement
     * doesn't create a ns element with "" uri)
     */
    public void test3ArgAddChildElement() throws Exception {
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        SOAPBody body = msg.getSOAPBody();
        Name name = SOAPFactory.newInstance().createName("Content", "", "some-uri");
        SOAPElement element =
            body.addBodyElement(name).addChildElement("lname", null, null);
        assertNull(element.getNamespaceURI());
    }

    /*
     * Testcase for Bug Id 6236737
     * Synopsis: SOAPElement.addChildElement("localname"); is adding
     * wrong namespace for the element.
     */
    public void testBug6236737() throws Exception {
        String xml =
            "<SOAP-ENV:Envelope xmlns=\"abc\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Body/></SOAP-ENV:Envelope>";
        MessageFactory mFactory = MessageFactory.newInstance();
        SOAPMessage msg = mFactory.createMessage();
        msg.getSOAPPart().setContent(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
        SOAPBody sb = msg.getSOAPPart().getEnvelope().getBody();
        SOAPElement ele =
            sb.addChildElement("lname1","","uri").addChildElement("lname3");
        assertEquals(
            "Element 'ele' lies in namespace 'uri' and not 'abc'",
            ele.getNamespaceURI(),
            "uri");
    }
}
