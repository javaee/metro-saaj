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
package namespace;

import javax.xml.soap.*;

import junit.framework.TestCase;


public class GetNamespaceURITest extends TestCase {

    public GetNamespaceURITest(String name) {
        super(name);
    }

    public void testGetNamespaceURI() throws Exception {
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        SOAPBody body = msg.getSOAPBody();
        Name name = SOAPFactory.newInstance().createName("Content", "p", "some-uri");
        SOAPElement element = body.addBodyElement(name).addChildElement("Value").addTextNode("SUNW");
        assertEquals(element.getNamespaceURI("p"), "some-uri");
    }
}
