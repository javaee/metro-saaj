/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
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

package stax;

import com.sun.xml.messaging.saaj.soap.impl.ElementImpl;
import com.sun.xml.messaging.saaj.util.SAAJUtil;
import com.sun.xml.messaging.saaj.util.stax.SaajStaxWriter;
import junit.framework.TestCase;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests {@link SaajStaxWriter} with respect to namespace assignments.
 *
 * <p>
 * This test class was created to check a fix of JDK-8159058.
 * </p>
 */
public class SaajStaxWriterNamespaceTest extends TestCase {

    private static final String ENV_URI = "http://schemas.xmlsoap.org/soap/envelope";
    private static final String WRAP_URI = "http://test.org/wrapper";
    private static final String PREFIX =
            "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
            "<SOAP-ENV:Header/>" +
            "<SOAP-ENV:Body>" +
            "<wrapper xmlns=\"http://test.org/wrapper\">";
    private static final String SUFFIX = "</wrapper></SOAP-ENV:Body></SOAP-ENV:Envelope>";

    private static final String EXAMPLE_URI_1 = "http://example.org/schema1";
    private static final String EXAMPLE_URI_2 = "http://example.org/schema2";
    private SOAPMessage message;
    private SaajStaxWriter writer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        message = MessageFactory.newInstance().createMessage();
        writer = new SaajStaxWriter(message, ENV_URI);
    }

    @Override
    protected  void tearDown() throws Exception {
        super.tearDown();
        this.message = null;
        this.writer = null;
    }

    private void openLayout() throws XMLStreamException {
        writer.writeStartDocument();
        writer.writeStartElement(ENV_URI, "Envelope");
        writer.writeStartElement(ENV_URI, "Body");
        writer.writeEmptyElement(WRAP_URI, "wrapper");
    }

    private void closeLayout() throws XMLStreamException {
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndDocument();
    }

    private void closeWriter() throws XMLStreamException {
        writer.flush();
        writer.close();
    }

    public void testDifferentDefaultNamespacesManual() throws Exception {
        openLayout();

        writer.writeStartElement("container");
        writer.writeAttribute("valid", "true"); // intentionally out of order but shall be handled correctly
        writer.writeNamespace("", EXAMPLE_URI_1);
        //writer.setPrefix("", EXAMPLE_URI_1);

        writer.writeStartElement("content");
        writer.writeNamespace("", EXAMPLE_URI_2);

        writer.writeEndElement();

        writer.writeEndElement();

        closeLayout();
        closeWriter();

        SOAPElement container = (SOAPElement)getWrapper().getFirstChild();
        assertEquals(EXAMPLE_URI_1, container.getNamespaceURI());
        SOAPElement content = (SOAPElement) container.getFirstChild();
        assertEquals(EXAMPLE_URI_2, content.getNamespaceURI());

        assertEquals("<container xmlns=\"http://example.org/schema1\" valid=\"true\">"
                        + "<content xmlns=\"http://example.org/schema2\"/>"
                        + "</container>",
                getSoapMessageContentsAsString());
    }

    public void testDifferentDefaultNamespacesAuto() throws Exception {
        openLayout();

        writer.writeStartElement(EXAMPLE_URI_1, "container");
        writer.writeStartElement(EXAMPLE_URI_2, "content");

        writer.writeEndElement();

        writer.writeEndElement();

        closeLayout();
        closeWriter();

        SOAPElement container = (SOAPElement)getWrapper().getFirstChild();
        assertEquals(EXAMPLE_URI_1, container.getNamespaceURI());
        SOAPElement content = (SOAPElement) container.getFirstChild();
        assertEquals(EXAMPLE_URI_2, content.getNamespaceURI());

        assertEquals("<container xmlns=\"http://example.org/schema1\">"
                        + "<content xmlns=\"http://example.org/schema2\"/>"
                        + "</container>",
                getSoapMessageContentsAsString());
    }

    public void testGlobalNamespaceInDefaultNamespacesManual() throws Exception {
        openLayout();

        writer.writeStartElement("container");
        writer.writeNamespace("", EXAMPLE_URI_1);
        writer.setPrefix("", EXAMPLE_URI_1);

        writer.writeStartElement("content");
        writer.writeNamespace("", "");
        writer.setPrefix("", "");

        writer.writeEndElement();

        writer.writeEndElement();

        closeLayout();
        closeWriter();

        SOAPElement container = (SOAPElement)getWrapper().getFirstChild();
        assertEquals(EXAMPLE_URI_1, container.getNamespaceURI());
        SOAPElement content = (SOAPElement) container.getFirstChild();
        assertNull(content.getNamespaceURI());

        assertEquals("<container xmlns=\"http://example.org/schema1\">"
                        + "<content xmlns=\"\"/>"
                        + "</container>",
                getSoapMessageContentsAsString());
    }

    public void testGlobalNamespaceInDefaultNamespacesAuto() throws Exception {
        openLayout();

        writer.writeStartElement(EXAMPLE_URI_1,"container");
        writer.writeStartElement("","content");

        writer.writeEndElement();

        writer.writeEndElement();

        closeLayout();
        closeWriter();

        SOAPElement container = (SOAPElement)getWrapper().getFirstChild();
        assertEquals(EXAMPLE_URI_1, container.getNamespaceURI());
        SOAPElement content = (SOAPElement) container.getFirstChild();
        assertNull(content.getNamespaceURI());

        assertEquals("<container xmlns=\"http://example.org/schema1\">"
                        + "<content xmlns=\"\"/>"
                        + "</container>",
                getSoapMessageContentsAsString());
    }

    public void testGlobalNamespaceInDefaultNamespacesMixed() throws Exception {
        openLayout();

        writer.writeStartElement(EXAMPLE_URI_1,"container");

        writer.writeStartElement("content");
        writer.writeNamespace("", "");
        writer.setPrefix("", "");

        writer.writeEndElement();

        writer.writeEndElement();

        closeLayout();
        closeWriter();

        SOAPElement container = (SOAPElement)getWrapper().getFirstChild();
        assertEquals(EXAMPLE_URI_1, container.getNamespaceURI());
        SOAPElement content = (SOAPElement) container.getFirstChild();
        assertNull(content.getNamespaceURI());

        assertEquals("<container xmlns=\"http://example.org/schema1\">"
                        + "<content xmlns=\"\"/>"
                        + "</container>",
                getSoapMessageContentsAsString());
    }


    public void testInheritNamespaceInDefaultNamespacesManual() throws Exception {
        openLayout();

        writer.writeStartElement("container");
        writer.writeNamespace("", EXAMPLE_URI_1);
        writer.setPrefix("", EXAMPLE_URI_1);

        writer.writeStartElement("content");

        writer.writeEndElement();

        writer.writeEndElement();

        closeLayout();
        closeWriter();

        ElementImpl element = (ElementImpl) getWrapper();
        SOAPElement container = (SOAPElement)element.getFirstChild();
        assertEquals(EXAMPLE_URI_1, container.getNamespaceURI());
        SOAPElement content = (SOAPElement) container.getFirstChild();
        assertEquals(EXAMPLE_URI_1, content.getNamespaceURI());

        assertEquals("<container xmlns=\"http://example.org/schema1\">"
                        + "<content/>"
                        + "</container>",
                getSoapMessageContentsAsString());
    }

    public void testInheritNamespaceInDefaultNamespacesAuto() throws Exception {
        openLayout();

        writer.writeStartElement(EXAMPLE_URI_1,"container");
        writer.writeStartElement("content");

        writer.writeEndElement();

        writer.writeEndElement();

        closeLayout();
        closeWriter();

        SOAPElement container = (SOAPElement)getWrapper().getFirstChild();
        assertEquals(EXAMPLE_URI_1, container.getNamespaceURI());
        SOAPElement content = (SOAPElement) container.getFirstChild();
        assertEquals(EXAMPLE_URI_1, content.getNamespaceURI());

        assertEquals("<container xmlns=\"http://example.org/schema1\">"
                        + "<content/>"
                        + "</container>",
                getSoapMessageContentsAsString());
    }

    private String getSoapMessageContentsAsString() throws IOException, SOAPException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        message.writeTo(os);
        String txtMessage = os.toString("UTF-8");
        if (txtMessage.startsWith(PREFIX)) {
            txtMessage = txtMessage.substring(PREFIX.length());
        } else {
            fail("SOAP message does not start with " + PREFIX);
        }
        if (txtMessage.endsWith(SUFFIX)) {
            txtMessage = txtMessage.substring(0, txtMessage.length() - SUFFIX.length());
        } else {
            fail("SOAP message does not end with " + SUFFIX);
        }
        return txtMessage;
    }

    private SOAPElement getWrapper() throws SOAPException {
        return (SOAPElement) message.getSOAPBody().getFirstChild();
    }
}
