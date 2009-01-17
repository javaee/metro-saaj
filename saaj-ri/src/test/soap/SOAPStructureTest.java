/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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
