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

/**
 * 
 */

package mime;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import javax.activation.*;
import javax.xml.soap.*;

import junit.framework.TestCase;

/*
 * These code snippets taken from Krishna's test workspace. 
 * The whole aim of the test is to create a message, and write it
 * out to a .msg file and write the mime headers to a .mh file
 *
 * Later use these to recreate the message and verify that the 
 * original and the recreated messages are indeed the same.
 *
 * @author Manveen Kaur (manveen.kaur@sun.com)
 */

public class MimeRecreateTest extends TestCase {

    private static final int MESSAGE_HEADERS = 0;
    private static final int MESSAGE_BYTES = 1;

    public SOAPMessage createMessage(byte[][] result) throws Exception {
        
        MessageBuilder mBuilder = new MessageBuilder();
        
        // Create a message factory.
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();        
        SOAPPart sp = msg.getSOAPPart();
        
        SOAPEnvelope envelope = sp.getEnvelope();
        SOAPBody bdy = envelope.getBody();
        
        SOAPBodyElement gltp
        = bdy.addBodyElement(envelope.createName("GetLastTradePrice",
        "ztrade",
        "http://wombat.ztrade.com"));
        
        gltp.addChildElement(envelope.createName("symbol",
        "ztrade",
        "http://wombat.ztrade.com"))
        .addTextNode("SUNW");

        URL url = getClass().getResource("../attach1.xml");
        
        //-----This code is written as a work around for one problem.
        
        class XmlDataSource extends URLDataSource {
            public XmlDataSource(URL u) { super(u); }
            @Override
            public String getContentType() { return "text/xml"; }
        }
        DataSource xmlDataSource = new XmlDataSource(url);
                
        AttachmentPart ap = msg.createAttachmentPart(
        new DataHandler(xmlDataSource));

        /*        
        AttachmentPart ap =
        msg.createAttachmentPart(new DataHandler(url));
        
        ap.setContentType("text/xml");
        */
         //-----
        
        msg.addAttachmentPart(ap);

        /*
        AttachmentPart ap = msg.createAttachmentPart();
        ap.setContent("<foo>hello</foo>","text/plain");
        msg.addAttachmentPart(ap);  
        */
        
        msg.saveChanges();

        result[MESSAGE_HEADERS] = mBuilder.saveMimeHeaders(msg);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        msg.writeTo(baos);
        result[MESSAGE_BYTES] = baos.toByteArray();
        baos.close();

        //System.out.println("\n\n------Original message----------");
        //msg.writeTo(System.out);
        
        return msg;
    }
    
    public void testMessageRecreation() {
        try {
            byte[][] result = new byte[2][];
            SOAPMessage originalMsg = createMessage(result);
            MessageBuilder mBuilder = new MessageBuilder();
            SOAPMessage newMsg = mBuilder.constructMessage(result[0], result[1]);
            
            //System.out.println("\n\n------Recreated message---------");
            //newMsg.writeTo(System.out);
            
            assertTrue( 
                    "Messages must match",
                    mBuilder.verifyMessage(originalMsg, newMsg));
            
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("No exception should have been thrown");
        }
        
    }

    public static void main(String argv[]) {
    
        junit.textui.TestRunner.run(MimeRecreateTest.class);

    }

}
