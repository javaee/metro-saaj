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

package saaj.receiver;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;

import com.sun.xml.messaging.soap.server.SAAJServlet;

/**
 * Sample servlet that receives messages.
 *
 * @author Krishna Meduri (krishna.meduri@sun.com)
 */

public class ReceivingServlet extends SAAJServlet
{

    static Logger
        logger = Logger.getLogger("Samples/Simple");

    // This is the application code for handling the message.. Once the
    // message is received the application can retrieve the soap part, the
    // attachment part if there are any, or any other information from the
    // message.

    public SOAPMessage onMessage(SOAPMessage message) {
        System.out.println("On message called in receiving servlet");
        try {
            System.out.println("Here's the message: ");
            message.writeTo(System.out);

            SOAPMessage msg = msgFactory.createMessage();

            SOAPEnvelope env = msg.getSOAPPart().getEnvelope();

            env.getBody()
                .addChildElement(env.createName("Response"))
                .addTextNode("This is a response");

            return msg;
        } catch(Exception e) {
            logger.log(
                Level.SEVERE,
                "Error in processing or replying to a message", 
                e);
            return null;
        }
    }
}

