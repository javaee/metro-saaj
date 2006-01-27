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
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package soap;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import java.util.Iterator;

import javax.xml.soap.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;

import junit.framework.TestCase;

import org.w3c.dom.*;

public class ExtractContentAsDocumentTest extends TestCase {

	private SOAPMessage sm1;
	private SOAPEnvelope envelopeOne;

	public ExtractContentAsDocumentTest(String name) {
	        super(name);
    	}

	public void setUp() throws Exception {
		createMessageOne();
	}

	private void createMessageOne() throws Exception {
        	String testDoc =
	 	"<env:Envelope"
                + " xmlns:env='http://schemas.xmlsoap.org/soap/envelope/'"
                + " xmlns:ns1='http://example.com/wsdl'>"
                +    "<env:Header/>"
                +    "<env:Body>"
                +      "<ns1:Hi/>"
                +      "<ns1:Hello>"
                +        "<String_1>Duke!</String_1>"
                +        "<String_2>Hi!</String_2>"
                +      "</ns1:Hello>"
                +    "</env:Body>"
                + "</env:Envelope>";

        	byte[] testDocBytes = testDoc.getBytes("UTF-8");
        	ByteArrayInputStream bais = 
                	new ByteArrayInputStream(testDocBytes);
	        StreamSource strSource = new StreamSource(bais);
        	MessageFactory mf = MessageFactory.newInstance();
	        sm1 = mf.createMessage();
        	SOAPPart sp = sm1.getSOAPPart();
	        sp.setContent(strSource);
		envelopeOne = sp.getEnvelope();
	}
        
    	public void testExtractContentAsDocument() throws Exception {
		SOAPBody body = envelopeOne.getBody();
		String exception = null;
		Document document = null;

                try {
			document = body.extractContentAsDocument();
		} catch(Exception e) {
			exception = e.getMessage();
		}
		assertNotNull("Body has 2 child elements so extract fails.",
			      exception);

		// Remove one out of the two body elements
		Iterator eachChild = body.getChildElements();
		SOAPBodyElement firstElement = (SOAPBodyElement)
					       eachChild.next();
		firstElement.detachNode();
		sm1.saveChanges();

		exception = null;
                try {
                        document = body.extractContentAsDocument();
                } catch(Exception e) {
			fail("Body has exactly one child element.");
                }

		Element element = document.getDocumentElement();
		assertEquals("element has a particular tag name.",
			     element.getTagName(), "ns1:Hello");
		assertEquals("first child element has a particular tag name",
			     ((Element) element.getFirstChild()).getTagName(),
			     "String_1");
                //System.out.println(nodeToString(document));
                //System.out.println(nodeToString(element));
		
                try {
			document = body.extractContentAsDocument();
		} catch(Exception e) {
			exception = e.getMessage();
		}
		assertNotNull("Body is empty so extract fails.", exception);
    	}
    
	public String nodeToString(org.w3c.dom.Node node) throws Exception {
        	// Use a Transformer for output
        	TransformerFactory tFactory =
                    new com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl();
        	Transformer transformer = tFactory.newTransformer();
        	StringWriter stringWriter = new StringWriter();

        	DOMSource source = new DOMSource(node);
        	StreamResult result = new StreamResult(stringWriter);

        	transformer.transform(source, result);
        	return stringWriter.toString();
	}
}
