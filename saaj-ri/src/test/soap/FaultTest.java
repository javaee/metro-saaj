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
 * $Id: FaultTest.java,v 1.1.1.1 2006-01-27 13:11:01 kumarjayanti Exp $
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
        +             "<faultcode>"
        +                 "code&gt;code"
        +             "</faultcode>"
        +             "<faultstring>"
        +                 "string&gt;string"
        +             "</faultstring>"
        +             "<faultactor>"
        +                 "actor&gt;actor"
        +             "</faultactor>"
        +             "<detail />"
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
