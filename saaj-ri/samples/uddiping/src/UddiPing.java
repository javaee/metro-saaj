/*
 * $Id: UddiPing.java,v 1.3 2009-01-16 05:08:59 ofung Exp $
 * $Revision: 1.3 $
 * $Date: 2009-01-16 05:08:59 $
 */

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






