/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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
