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
package soap12;

import javax.xml.soap.*;
import javax.xml.namespace.QName;

import java.util.Iterator;

import junit.framework.TestCase;


public class SOAPHeaderTests extends TestCase {

    public SOAPHeaderTests(String name) throws Exception {
        super(name);
    }

    public void testAddNotUnderstoodHeaderElement() throws Exception {

        MessageFactory mFactory =
            MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage msg = mFactory.createMessage();
        SOAPHeader header = msg.getSOAPHeader();
        QName nameOfHeaderNotUnderstood = new QName("some-uri", "name");
        header.addNotUnderstoodHeaderElement(nameOfHeaderNotUnderstood);
        Iterator eachHeaderElement = header.examineAllHeaderElements();
        assertTrue("There's a header element", eachHeaderElement.hasNext());
        SOAPHeaderElement notUnderstoodElement =
            (SOAPHeaderElement) eachHeaderElement.next();
        assertEquals(
            notUnderstoodElement.getElementQName(),
            new QName(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "NotUnderstood"));
        String qname =
            notUnderstoodElement.getAttributeValue(new QName("qname"));
        String prefix = getPrefixFromExpandedName(qname);
        String localName = getLocalnameFromExpandedName(qname);
        assertEquals(
            nameOfHeaderNotUnderstood,
            notUnderstoodElement.createQName(localName, prefix));
    }

    public void testAddUpgradeHeaderElement() throws Exception {
        MessageFactory msgFactory = MessageFactory.newInstance();
        SOAPMessage msg = msgFactory.createMessage();
        SOAPHeader header = msg.getSOAPHeader();
        header.addUpgradeHeaderElement(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
        Iterator eachHeaderElement = header.examineAllHeaderElements();
        assertTrue("There's a header element", eachHeaderElement.hasNext());
        SOAPHeaderElement upgradeElement =
            (SOAPHeaderElement) eachHeaderElement.next();
        assertEquals(
            upgradeElement.getElementQName(),
            new QName(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE, "Upgrade"));
        Iterator eachSupportedEnvElement =
            upgradeElement.getChildElements(
                new QName(
                    SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE,
                    "SupportedEnvelope"));
        assertTrue(
            "There's a SupportedEnvelope element",
            eachSupportedEnvElement.hasNext());
        SOAPElement supportedEnvElement =
            (SOAPElement) eachSupportedEnvElement.next();
        String qname =
            supportedEnvElement.getAttributeValue(new QName("qname"));
        String prefix = getPrefixFromExpandedName(qname);
        String localName = getLocalnameFromExpandedName(qname);
        assertEquals(localName, "Envelope");
        assertEquals(
            SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE,
            supportedEnvElement.getNamespaceURI(prefix));
    }

    private static String getPrefixFromExpandedName(String expandedName) {
        int index = expandedName.indexOf(':');
        if (index < 0)
            return "";
        else
            return expandedName.substring(0, index);
    }

    private static String getLocalnameFromExpandedName(String expandedName) {
        int index = expandedName.indexOf(':');
        if (index < 0)
            return expandedName;
        else
            return expandedName.substring(index + 1);
    }
}
