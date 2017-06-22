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

package saaj.sender;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.soap.*;


/**
 * Sample servlet that is used for sending the message.
 *
 * @author Krishna Meduri (krishna.meduri@sun.com)
 */

public class SendingServlet extends HttpServlet {

    static Logger
    logger = Logger.getLogger("Samples/Simple");

    String to = null;
    String data = null;
    ServletContext servletContext;

    // Connection to send messages.
    private SOAPConnection con;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init( servletConfig );
        servletContext = servletConfig.getServletContext();

        try {
        SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
            con = scf.createConnection();
        } catch(Exception e) {
            logger.log(
                Level.SEVERE,
                "Unable to open a SOAPConnection", e);
        }

        InputStream in
        = servletContext.getResourceAsStream("/WEB-INF/address.properties");

        if (in != null) {
            Properties props = new Properties();

            try {
                props.load(in);

                to = props.getProperty("to");
                data = props.getProperty("data");
            } catch (IOException ex) {
                // Ignore
            }
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException {

        String retval ="<html> <H4>";

        try {
            // Create a message factory.
            MessageFactory mf = MessageFactory.newInstance();

            // Create a message from the message factory.
            SOAPMessage msg = mf.createMessage();

            // Message creation takes care of creating the SOAPPart - a
            // required part of the message as per the SOAP 1.1
            // specification.
            SOAPPart sp = msg.getSOAPPart();

            // Retrieve the envelope from the soap part to start building
            // the soap message.
            SOAPEnvelope envelope = sp.getEnvelope();

            // Create a soap header from the envelope.
            SOAPHeader hdr = envelope.getHeader();

            // Create a soap body from the envelope.
            SOAPBody bdy = envelope.getBody();

            // Add a soap body element to the soap body
            SOAPBodyElement gltp
            = bdy.addBodyElement(envelope.createName("GetLastTradePrice",
            "ztrade",
            "http://wombat.ztrade.com"));

            gltp.addChildElement(envelope.createName("symbol",
            "ztrade",
            "http://wombat.ztrade.com")).addTextNode("SUNW");

            StringBuffer urlSB=new StringBuffer();
            urlSB.append(req.getScheme()).append("://").append(req.getServerName());
            urlSB.append( ":" ).append( req.getServerPort() ).append( req.getContextPath() );
            String reqBase=urlSB.toString();

            if(data==null) {
                data=reqBase + "/index.html";
            }

            // Want to set an attachment from the following url.
            //Get context
            URL url = new URL(data);

            AttachmentPart ap = msg.createAttachmentPart(new DataHandler(url));

            ap.setContentType("text/html");

            // Add the attachment part to the message.
            msg.addAttachmentPart(ap);

            // Create an endpoint for the recipient of the message.
            if(to==null) {
                to=reqBase + "/receiver";
            }

            URL urlEndpoint = new URL(to);

            System.err.println("Sending message to URL: "+urlEndpoint);
            System.err.println("Sent message is logged in \"sent.msg\"");

            retval += " Sent message (check \"sent.msg\") and ";

            FileOutputStream sentFile = new FileOutputStream("sent.msg");
            msg.writeTo(sentFile);
            sentFile.close();

            // Send the message to the provider using the connection.
            SOAPMessage reply = con.call(msg, urlEndpoint);

            if (reply != null) {
                FileOutputStream replyFile = new FileOutputStream("reply.msg");
                reply.writeTo(replyFile);
                replyFile.close();
                System.err.println("Reply logged in \"reply.msg\"");
                retval += " received reply (check \"reply.msg\").</H4> </html>";

            } else {
                System.err.println("No reply");
                retval += " no reply was received. </H4> </html>";
            }

        } catch(Throwable e) {
            e.printStackTrace();
            logger.severe("Error in constructing or sending message "
            +e.getMessage());
            retval += " There was an error " +
            "in constructing or sending message. </H4> </html>";
        }

        try {
            OutputStream os = resp.getOutputStream();
            os.write(retval.getBytes());
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.severe( "Error in outputting servlet response "
            + e.getMessage());
        }
    }

}
