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
package soap;

import junit.framework.TestCase;

import java.io.*;

import java.util.Iterator;

import javax.xml.soap.*;
import javax.xml.transform.stream.*;

public class GetChildElementsTest extends TestCase {

    public GetChildElementsTest(String name) {
        super(name);
    }

    public void testGetChildElements() throws Exception {

        String xml =
            "<SOAP-ENV:Envelope xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'><SOAP-ENV:Header><wsse:Security xmlns='http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd' xmlns:wsse='http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd'/></SOAP-ENV:Header><SOAP-ENV:Body/></SOAP-ENV:Envelope>";

        byte[] testDocBytes = xml.getBytes("UTF-8");
        ByteArrayInputStream bais = new ByteArrayInputStream(testDocBytes);
        StreamSource strSource = new StreamSource(bais);
        MessageFactory msgFactory = MessageFactory.newInstance();
        SOAPMessage message = msgFactory.createMessage();
        SOAPPart soapPart = message.getSOAPPart();
        soapPart.setContent(strSource);
        message.saveChanges();

        Iterator headerElements =
            message.getSOAPHeader().getChildElements(
                SOAPFactory.newInstance().createName(
                    "Security",
                    "wsse",
                    "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"));

        assertTrue(headerElements.hasNext());
    }
}
