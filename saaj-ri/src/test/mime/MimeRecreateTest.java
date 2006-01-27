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

/**
 * $Id: MimeRecreateTest.java,v 1.1.1.1 2006-01-27 13:11:00 kumarjayanti Exp $
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
