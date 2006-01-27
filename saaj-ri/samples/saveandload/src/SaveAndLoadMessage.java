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

import java.net.URL;

import javax.activation.DataHandler;

import java.io.*;

import javax.xml.soap.*;

import com.sun.xml.messaging.soap.SOAPMessagePersister;

/**
 * This class demonstrates the use of SOAPMessagePersister sample
 */
public class SaveAndLoadMessage {

    public static void main(String[] args) throws Exception {
 
        SOAPMessagePersister persister = new SOAPMessagePersister();

        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();        
        SOAPPart soapPart = msg.getSOAPPart();
        soapPart.setContentId("soapPart");
        SOAPEnvelope envelope = soapPart.getEnvelope();
        SOAPHeader hdr = envelope.getHeader();
        SOAPBody bdy = envelope.getBody();

        // Populate the message body
        SOAPBodyElement gltp =
            bdy.addBodyElement(envelope.createName(
                "GetLastTradePrice", "ztrade", "http://wombat.ztrade.com"));
        gltp.addChildElement(envelope.createName(
            "symbol", "ztrade", "http://wombat.ztrade.com"))
                .addTextNode("SUNW");

        // Add an atachment
        URL url = new URL("file", null, "message.xml");
        AttachmentPart ap = msg.createAttachmentPart(new DataHandler(url));
        ap.setContentType("text/xml");
        ap.setContentId("attachmentPart");
        msg.addAttachmentPart(ap);

        msg.saveChanges();
        
        // Save the message to a file
        persister.save(msg, "savedMsg.txt");
        System.out.println("Message saved to savedMsg.txt");
        
        // Load the message from the file
        SOAPMessage newMsg = persister.load("savedMsg.txt");
    }
}
