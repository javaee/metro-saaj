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
