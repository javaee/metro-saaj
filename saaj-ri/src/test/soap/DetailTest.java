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

package soap;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import javax.xml.soap.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.sun.xml.messaging.saaj.util.SAAJUtil;
import junit.framework.TestCase;

public class DetailTest extends TestCase {
    
    /** Creates a new instance of Test1 */
    public DetailTest(String name) {
	super(name);
    }
    
    public void testDetailImpl() {
        try {
            doit1();
        } catch (Throwable t) {
            System.out.println("Exception: " + t);
            t.printStackTrace();
	    fail();
        }
    }

    public void doit1() throws Exception {
		String testDoc = 		  "<?xml version='1.0' encoding='UTF-8'?>\n" 		+ "<D:Envelope xmlns:D='http://schemas.xmlsoap.org/soap/envelope/'>\n"     		+ "	<D:Body>\n" 	        + "		<D:Fault>\n"             	+ "			<D:faultcode>Client.invalidSignature</D:faultcode>\n"             	+ "			<D:faultstring>invalid signature</D:faultstring>\n"             	+ "			<D:detail>\n"                 + "				27: Invalid Signature\n"             	+ "			</D:detail>\n"         	+ "		</D:Fault>\n"     		+ "	</D:Body>\n" 		+ "</D:Envelope>\n"; 
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
		Detail detail = fault.getDetail();
		assertTrue(detail.getPrefix().length()>0);
    }

    public String nodeToString(org.w3c.dom.Node node) throws Exception {
        	// Use a Transformer for output
        	TransformerFactory tFactory = TransformerFactory.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", SAAJUtil.getSystemClassLoader());
        	Transformer transformer = tFactory.newTransformer();
        	StringWriter stringWriter = new StringWriter();

        	DOMSource source = new DOMSource(node);
        	StreamResult result = new StreamResult(stringWriter);
        	transformer.transform(source, result);
        	return stringWriter.toString();
    }

    public void testDetailEntryCR6581434() {
        try {
            MessageFactory mf = MessageFactory.newInstance();
            SOAPMessage m = mf.createMessage();
            SOAPEnvelope se = m.getSOAPPart().getEnvelope();
            Name codeName  = se.createName("ErrorCode", "as", null);
            SOAPFault fault = m.getSOAPBody().addFault();
            Detail detail = fault.addDetail();
            detail.addNamespaceDeclaration("as", "http://abc.com/test");
            DetailEntry codeDetail = detail.addDetailEntry(codeName);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }


    }
}
