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
 * $Id: AttachMultipartTest.java,v 1.1.1.1 2006-01-27 13:11:00 kumarjayanti Exp $
 */

package mime;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.*;
import java.net.URL;
import java.util.Iterator;

import javax.activation.DataHandler;
import javax.xml.soap.*;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;

import javax.activation.FileDataSource;

import com.sun.xml.messaging.saaj.packaging.mime.internet.*;

/*
 * Attaches an image object and verifies whether it gets the image object back
 */

public class AttachMultipartTest extends TestCase {

    public AttachMultipartTest(String name) {
        super(name);
    }
     
    public void testAddMultipartAndVerify() throws Exception {

    /*
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();        
        SOAPPart sp = msg.getSOAPPart();
        
        SOAPEnvelope envelope = sp.getEnvelope();
        
        SOAPHeader hdr = envelope.getHeader();
        SOAPBody bdy = envelope.getBody();

		// Add to body 
        SOAPBodyElement gltp = bdy.addBodyElement(
			envelope.createName("GetLastTradePrice", "ztrade",
				"http://wombat.ztrade.com"));
        
        gltp.addChildElement(envelope.createName("symbol", "ztrade",
			"http://wombat.ztrade.com")).addTextNode("SUNW");

        URL url = new File("src/test/mime/data/message1.txt").toURL();
         
        // create a multipart object 
        MimeMultipart multipart = new MimeMultipart("mixed");
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setDataHandler(new DataHandler(url));
        String bpct = bodyPart.getContentType();
        bodyPart.setHeader("Content-Type", bpct);

        multipart.addBodyPart(bodyPart);

        String contentType = multipart.getContentType().toString();
        AttachmentPart ap1 = msg.createAttachmentPart(multipart, contentType);
        ap1.setContentId("cid:someid1234");
        msg.addAttachmentPart(ap1); 

        
        // Attach Image        
		Image img = Toolkit.getDefaultToolkit().getImage(
			"src/test/mime/data/java_logo.jpg");
        AttachmentPart ap = msg.createAttachmentPart(img, "image/jpeg");
        msg.addAttachmentPart(ap);
        msg.saveChanges();
		
                // Save the soap message to file
		FileOutputStream sentFile = new FileOutputStream(
			"src/test/mime/data/java_logo_sent.jpg");
		msg.writeTo(sentFile);
		sentFile.close();

		// See if we get the image object back
		FileInputStream fin= new FileInputStream(
			"src/test/mime/data/java_logo_sent.jpg");
		SOAPMessage newMsg = mf.createMessage(msg.getMimeHeaders(), fin);
		Iterator i = newMsg.getAttachments();
		while(i.hasNext()) {
			AttachmentPart att = (AttachmentPart)i.next();
			Object ct = att.getContent();
		        if (!(ct instanceof MimeMultipart)) {
                           fail("Didnt get the Multipart type, instead got " +
                              ct.getClass());
                        }
                        break;  	
		}
		fin.close();
    */
    }
    
}
