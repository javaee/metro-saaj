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

package mime;

import javax.xml.soap.*;
import javax.xml.transform.stream.StreamSource;

import java.io.StringReader;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import javax.activation.DataHandler;

import junit.framework.TestCase;

/**
 * Tests for Bug ID: 4991879
 */
public class XmlAttachmentTest extends TestCase {

    public XmlAttachmentTest(String name) {
        super(name);
    }

    public void testXmlAttachmentWithXmlDecl() throws Exception {
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>    <START><A>Hello</A><B>World</B></START>";
        StringReader reader = new StringReader(xml);
        StreamSource content = new StreamSource(reader);
        AttachmentPart attachment =
            msg.createAttachmentPart(content, "text/xml");

        DataHandler handler = attachment.getDataHandler();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        handler.writeTo(baos);
        String reconstructedXml = baos.toString("utf-8");
        assertEquals(
            "Attachment content is preserved", xml, reconstructedXml);
    }

    public void testXmlAttachmentWithoutXmlDecl() throws Exception {
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        String xml = "<START><A>Hello</A><B>World</B></START>";
        StringReader reader = new StringReader(xml);
        StreamSource content = new StreamSource(reader);
        AttachmentPart attachment =
            msg.createAttachmentPart(content, "text/xml");

        DataHandler handler = attachment.getDataHandler();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        handler.writeTo(baos);
        String reconstructedXml = baos.toString();
        assertEquals(
            "Attachment content is preserved", xml, reconstructedXml);
    }

    public void testXmlAttachmentReWriteWithReader() throws Exception {
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>    <START><A>Hello</A><B>World</B></START>";
        StringReader reader = new StringReader(xml);
        StreamSource content = new StreamSource(reader);
        AttachmentPart attachment =
            msg.createAttachmentPart(content, "text/xml");

        DataHandler handler = attachment.getDataHandler();
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        handler.writeTo(baos1);
        handler.writeTo(baos2);
        String reconstructedXml1 = baos1.toString("utf-8");
        String reconstructedXml2 = baos2.toString("utf-8");
        assertEquals(
            "writeTo for the attachment should succeed the second time too!",
            reconstructedXml1,
            reconstructedXml2);
    }

    public void testXmlAttachmentReWriteWithIS() throws Exception {
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>    <START><A>Hello</A><B>World</B></START>";
        ByteArrayInputStream bais =
            new ByteArrayInputStream(xml.getBytes("utf-8"));
        StreamSource content = new StreamSource(bais);
        AttachmentPart attachment =
            msg.createAttachmentPart(content, "text/xml");

        DataHandler handler = attachment.getDataHandler();
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        handler.writeTo(baos1);
        handler.writeTo(baos2);
        String reconstructedXml1 = baos1.toString("utf-8");
        String reconstructedXml2 = baos2.toString("utf-8");
        assertEquals(
            "writeTo for the attachment should succeed the second time too!",
            reconstructedXml1,
            reconstructedXml2);
    }
}
