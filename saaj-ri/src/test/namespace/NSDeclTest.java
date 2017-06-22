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

package namespace;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.sun.xml.messaging.saaj.util.SAAJUtil;
import junit.framework.TestCase;

import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/*
 * Tests to check for namespace rules being followed in SOAP message creation.
 */

public class NSDeclTest extends TestCase {

    public static final String NamespaceSpecNS =
        "http://www.w3.org/2000/xmlns/";


    public NSDeclTest(String name) {
        super(name);
    }

   public void testAttributes() throws Exception {

        String testDoc =
            "<env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/'\n"
                + " xmlns:xsd='http://www.w3.org/2001/XMLSchema'\n"
                + " xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n"
                + " xmlns:enc='http://schemas.xmlsoap.org/soap/encoding/'\n"
                + " xmlns:ns0='http://example.com/wsdl'>\n"
                + "  <env:Body>\n"
                + "    <ns0:sayHello>\n"
                + "      <String_1>to Duke!</String_1>\n"
                + "    </ns0:sayHello>\n"
                + "  </env:Body>\n"
                + "</env:Envelope>\n";

        byte[] testDocBytes = testDoc.getBytes("UTF-8");
        ByteArrayInputStream bais = new ByteArrayInputStream(testDocBytes);
        StreamSource strSource = new StreamSource(bais);

        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage sm = mf.createMessage();
        SOAPPart sp = sm.getSOAPPart();
        sp.setContent(strSource);
        Document doc = sp;
        
        // Workaround for SAAJ bug 4871599
        //sp.getEnvelope();

        // Uncomment the following to enable viewing DOM Node.
        // dumpDomNode(doc);
        // This test fails
        doTest(doc);

        // The following code which does not use SAAJ works correctly...
        byte[] testDocBytes2 = testDoc.getBytes("UTF-8");
        ByteArrayInputStream bais2 = new ByteArrayInputStream(testDocBytes2);
        InputSource is = new InputSource(bais2);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc2 = dbf.newDocumentBuilder().parse(is);
        // Uncomment to enable dumping to see the DOM Node
        // dumpDomNode(doc2);
        doTest(doc2);
    }

    private static boolean doTest(Document doc) {
        Element documentElement = doc.getDocumentElement();

        // Set dumpAttrs to true to print out root element attributes
        boolean dumpAttrs = false;
        if (dumpAttrs) {
            NamedNodeMap nnm = documentElement.getAttributes();
            for (int i = 0; i < nnm.getLength(); i++) {
                Node node = nnm.item(i);
                System.err.println(
                    "type="
                        + node.getNodeType()
                        + ", name="
                        + node.getNodeName()
                        + ", namespaceUri="
                        + node.getNamespaceURI()
                        + ", localName="
                        + node.getLocalName());
            }
        }

        // DOM Level 2 Core says that this should return the "xmlns:env"
        // namespace declaration but it does not.  See first "Note" under
        // http://www.w3.org/TR/DOM-Level-2-Core/core.html#Namespaces-Considerations
        Attr xmlnsAttr =
            documentElement.getAttributeNodeNS(NamespaceSpecNS, "env");

        if (xmlnsAttr == null) {
            fail("Error: a DOM Attr should have been returned");
            return false;
        } else {
            //System.err.println("Success: a DOM Attr was returned");
        }
        return true;
    }

    static void dumpDomNode(Node node) throws TransformerException {
        System.err.println("==== DebugUtil.dumpDomNode(...) Start ====");
        DOMSource domSource = new DOMSource(node);
        TransformerFactory tf = TransformerFactory.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", SAAJUtil.getSystemClassLoader());
        Transformer xform = null;
        xform = tf.newTransformer();
        xform.transform(domSource, new StreamResult(System.err));
        System.err.println();
        System.err.println("==== DebugUtil.dumpDomNode(...) End ====");
    }
    
    // test for bug id 4871599
    public void testGetDocElement() throws Exception {

        String testDoc =
            "<env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/'\n"
                + " xmlns:xsd='http://www.w3.org/2001/XMLSchema'\n"
                + " xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n"
                + " xmlns:enc='http://schemas.xmlsoap.org/soap/encoding/'\n"
                + " xmlns:ns0='http://example.com/wsdl'>\n"
                + "  <env:Body>\n"
                + "    <ns0:sayHello>\n"
                + "      <String_1>to Duke!</String_1>\n"
                + "    </ns0:sayHello>\n"
                + "  </env:Body>\n"
                + "</env:Envelope>\n";

        byte[] testDocBytes = testDoc.getBytes("UTF-8");
        ByteArrayInputStream bais = new ByteArrayInputStream(testDocBytes);
        StreamSource strSource = new StreamSource(bais);

        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage sm = mf.createMessage();
        SOAPPart sp = sm.getSOAPPart();
        sp.setContent(strSource);
        
        // Uncomment following line for a workaround
        //sp.getEnvelope();
        Element docElement = sp.getDocumentElement();
        if (docElement == null)
            fail("The following value should not be null: " + docElement);
    }
 
    public static void main(String argv[]) {

        junit.textui.TestRunner.run(NSDeclTest.class);

    }

}
