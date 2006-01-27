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
