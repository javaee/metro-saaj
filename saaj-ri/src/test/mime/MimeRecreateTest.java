/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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

/**
 * 
 */

package mime;

import java.io.FileOutputStream;
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

    public MimeRecreateTest(String name) {
        super(name);
    }
     
    public SOAPMessage createMessage() throws Exception {
        
        MessageBuilder mBuilder = new MessageBuilder();
        
        // Create a message factory.
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();        
        SOAPPart sp = msg.getSOAPPart();
        
        SOAPEnvelope envelope = sp.getEnvelope();
        
        SOAPHeader hdr = envelope.getHeader();
        SOAPBody bdy = envelope.getBody();
        
        SOAPBodyElement gltp
        = bdy.addBodyElement(envelope.createName("GetLastTradePrice",
        "ztrade",
        "http://wombat.ztrade.com"));
        
        gltp.addChildElement(envelope.createName("symbol",
        "ztrade",
        "http://wombat.ztrade.com"))
        .addTextNode("SUNW");
        
        URL url = new URL("file", null, 
                  "src/test/mime/data/attach1.xml");
        
        //-----This code is written as a work around for one problem.
        
        class XmlDataSource extends URLDataSource {
            public XmlDataSource(URL u) { super(u); }
            public String getContentType() { return "text/xml"; }
        };
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
        
        FileOutputStream sentFile =
        new FileOutputStream("src/test/mime/data/golden.msg");
        
        msg.saveChanges();

        mBuilder.saveMimeHeaders(msg, "src/test/mime/data/golden.mh");
        
        msg.writeTo(sentFile);
        sentFile.close();
        
        //System.out.println("\n\n------Original message----------");
        //msg.writeTo(System.out);
        
        return msg;
    }
    
    public void testMessageRecreation() {
        try {
            
            SOAPMessage originalMsg = createMessage();

            MessageBuilder mBuilder = new MessageBuilder();
                       
            SOAPMessage newMsg = mBuilder.constructMessage(
            "src/test/mime/data/golden.mh" ,
            "src/test/mime/data/golden.msg");
            
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
