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

/**
*
* @author SAAJ RI Development Team
*/

package soap;

import java.io.ByteArrayInputStream;
import java.util.Iterator;

import javax.xml.soap.*;
import javax.xml.transform.stream.*;
import javax.xml.namespace.QName;

import junit.framework.TestCase;

public class FaultTest extends TestCase {

    public FaultTest(String name) {
        super(name);
    }

    public void testGetDetail() throws Exception {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        SOAPBody body = envelope.getBody();
        SOAPFault fault = body.addFault();
        Detail detail = fault.addDetail();
        String detailEntryLocalName = "name";
        Name detailEntryName = envelope.createName(detailEntryLocalName, "prefix", "uri");
        detail.addDetailEntry(detailEntryName);
        
        SOAPFault extractedFault = body.getFault();
        assertTrue(extractedFault != null);
        Detail extractedDetail = extractedFault.getDetail();
        assertTrue(extractedDetail != null);
        Iterator eachDetailEntry = extractedDetail.getDetailEntries();
        assertTrue(eachDetailEntry.hasNext());
        DetailEntry extractedEntry = (DetailEntry) eachDetailEntry.next();
        assertEquals(detailEntryLocalName, extractedEntry.getLocalName());
        assertFalse(eachDetailEntry.hasNext());

        extractedFault.setFaultActor(SOAPConstants.URI_SOAP_ACTOR_NEXT);
        Detail d = extractedFault.getDetail();
        d.detachNode();
        d = extractedFault.getDetail();
        assertTrue(d == null);
    }

    public void testFaultWithAmp() throws Exception {
        String testDoc = 
          "<?xml version='1.0' encoding='UTF-8'?>"
        + "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
        +     "<soap:Body>"
        +         "<soap:Fault>"
        +             "<soap:faultcode>"
        +                 "code&gt;code"
        +             "</soap:faultcode>"
        +             "<soap:faultstring>"
        +                 "string&gt;string"
        +             "</soap:faultstring>"
        +             "<soap:faultactor>"
        +                 "actor&gt;actor"
        +             "</soap:faultactor>"
        +             "<soap:detail />"
        +         "</soap:Fault>"
        +     "</soap:Body>"
        + "</soap:Envelope>";

        byte[] testDocBytes = testDoc.getBytes("UTF-8");
        ByteArrayInputStream bais = new ByteArrayInputStream(testDocBytes);
        StreamSource strSource = new StreamSource(bais);

        MessageFactory msgFactory = MessageFactory.newInstance();
        SOAPMessage message = msgFactory.createMessage();
        SOAPPart soapPart = message.getSOAPPart();

        soapPart.setContent(strSource);
        message.saveChanges();
        SOAPEnvelope envelope = soapPart.getEnvelope();
        SOAPBody body = envelope.getBody();
        SOAPFault fault = body.getFault();
        assertTrue(fault.getFaultCode().equals("code>code"));
        assertTrue(fault.getFaultString().equals("string>string"));
        assertTrue(fault.getFaultActor().equals("actor>actor"));
    }

    /**
     * Test case for CR ID 6212709
     */
    public void testSetFaultCodeWithNameArg() throws Exception {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPBody body = message.getSOAPBody();
        SOAPFault fault = body.addFault();
        Name faultCodeName = SOAPFactory.newInstance().createName("Client", "env", null);
        try {
            fault.setFaultCode(faultCodeName);
        } catch (SOAPException se) {
            return;
        }
        fail("Invalid fault code allowed to be set");
    }
    
    public void testSetFaultCodeWithPrefixEmpty() throws Exception {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPBody body = message.getSOAPBody();
        body.addFault(new QName("some-uri", "code"), "Some string");
    }

    public void testSetFaultStringAndLocale() throws Exception {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPBody body = message.getSOAPBody();
        body.addFault(new QName("some-uri", "code"), "Some string");
        SOAPFault fault = body.getFault();
        fault.setFaultString("EN faultString" , java.util.Locale.ENGLISH);
        //System.out.println(fault.getFaultString() + " : " + fault.getFaultStringLocale());
        fault.setFaultString("No Locale faultString");
        //System.out.println(fault.getFaultString() + " : " + fault.getFaultStringLocale());
        assertTrue(fault.getFaultStringLocale() == null);
    }
}
