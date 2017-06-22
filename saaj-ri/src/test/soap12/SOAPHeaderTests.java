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
