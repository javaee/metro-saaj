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
 * $Id: SOAPStructureTest.java,v 1.1.1.1 2006-01-27 13:11:01 kumarjayanti Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/**
*
* @author SAAJ RI Development Team
*/

package soap;

import java.util.Iterator;

import javax.xml.soap.*;

import junit.framework.TestCase;

public class SOAPStructureTest extends TestCase {
    public SOAPStructureTest(String name) {
        super(name);
    }

    public void testAddHeaderElement() throws Exception {
        SOAPMessage message = MessageFactory.newInstance().createMessage();
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        SOAPHeader header = envelope.getHeader();
        
        Name name1 = envelope.createName("firstElement", null, "foo");
        Name name2 = envelope.createName("secondElement", null, "foo");
        
        SOAPHeaderElement element1 = header.addHeaderElement(name1);
        element1.setActor("theActor");

        SOAPHeaderElement element2 = header.addHeaderElement(name2);
        element2.setActor("theActor");
        
        Iterator eachElement = header.extractHeaderElements("theActor");
        
        assertTrue("First element is there", eachElement.hasNext());
        SOAPHeaderElement outElement1 = (SOAPHeaderElement) eachElement.next();
        assertTrue("Second element is there", eachElement.hasNext());
        SOAPHeaderElement outElement2 = (SOAPHeaderElement) eachElement.next();
        assertFalse("No more elements", eachElement.hasNext());

        assertEquals("First element is correct", element1, outElement1);
        assertEquals("Second element is correct", element2, outElement2);
    }

    public void testAddHeader() throws Exception {
        SOAPMessage message = MessageFactory.newInstance().createMessage();
        message.getSOAPHeader().detachNode();
        
        SOAPHeader header = message.getSOAPHeader();
        assertTrue(header == null);
        
        message.getSOAPPart().getEnvelope().addHeader();
        header = message.getSOAPHeader();
        assertTrue(header != null);
        
        String headerPrefix = header.getPrefix();
        String headerUri = header.getNamespaceURI(headerPrefix);
        assertTrue(SOAPConstants.URI_NS_SOAP_ENVELOPE.equals(headerUri));
    }
}
