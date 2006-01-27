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
 * $Id: UddiPing.java,v 1.1.1.1 2006-01-27 13:11:15 kumarjayanti Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2006-01-27 13:11:15 $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import java.io.FileInputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import javax.xml.soap.*;

public class UddiPing {

    public static void main(String[] args) {
        try {

            if (args.length != 2)  {
                System.err.println("Usage: UddiPing properties-file business-name");
                System.exit(1);
            }


            Properties myprops = new Properties();
            myprops.load(new FileInputStream(args[0]));


            Properties props = System.getProperties();

            Enumeration it = myprops.propertyNames();
            while (it.hasMoreElements()) {
                String s = (String) it.nextElement();
                props.put(s, myprops.getProperty(s));
            }

            // Create the connection and the message factory.
            SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
            SOAPConnection connection = scf.createConnection();
            MessageFactory msgFactory = MessageFactory.newInstance();

            // Create a message
            SOAPMessage msg = msgFactory.createMessage();

            // Create an envelope in the message
            SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();

            // Get hold of the the body
            SOAPBody body = envelope.getBody();
  
            body.addChildElement(envelope.createName("find_service", "",
                                                     "urn:uddi-org:api_v2"))
                .addAttribute(envelope.createName("generic"), "2.0")
                .addAttribute(envelope.createName("maxRows"), "100")
                .addChildElement(envelope.createName("name"))
                .addTextNode(args[1]);

            URL endpoint
                = new URL(System.getProperties().getProperty("URL"));

            msg.saveChanges();

            System.out.println("\n----------- Request Message ----------\n");
            msg.writeTo(System.out);
            
            SOAPMessage reply = connection.call(msg, endpoint);

            System.out.println("Received reply from: "+endpoint);

            System.out.println("\n----------- Reply Message ----------\n");
            reply.writeTo(System.out);

            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}






