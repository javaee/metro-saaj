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
 * $Id: AttachPngImageTest.java,v 1.1.1.1 2006-01-27 13:11:00 kumarjayanti Exp $
 */

package mime;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

import javax.xml.soap.*;

import junit.framework.TestCase;

/*
 * Attaches an image object and verifies whether it gets the image object back
 */

public class AttachPngImageTest extends TestCase {

    public AttachPngImageTest(String name) {
        super(name);
    }
     
    public void testAddPngImageAndVerify() throws Exception {
        
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
        
        // Attach Image        
		Image img = Toolkit.getDefaultToolkit().getImage(
			"src/test/mime/data/image.png");
        AttachmentPart ap = msg.createAttachmentPart(img, "image/png");
        msg.addAttachmentPart(ap);

        // Attach Image        
		Image img1 = Toolkit.getDefaultToolkit().getImage(
			"src/test/mime/data/java_logo.jpg");
        AttachmentPart ap1 = msg.createAttachmentPart(img1, "image/jpeg");
        msg.addAttachmentPart(ap1);

        msg.saveChanges();

		// Save the soap message to file
		FileOutputStream sentFile = new FileOutputStream(
			"src/test/mime/data/image_sent.png");
		msg.writeTo(sentFile);
		sentFile.close();

		// See if we get the image object back
		FileInputStream fin= new FileInputStream(
			"src/test/mime/data/image_sent.png");
		SOAPMessage newMsg = mf.createMessage(msg.getMimeHeaders(), fin);
		Iterator i = newMsg.getAttachments();
		while(i.hasNext()) {
			AttachmentPart att = (AttachmentPart)i.next();
			Object obj = att.getContent();
			if (!(obj instanceof Image)) {
            	fail("Didn't get the image type, instead got:"+obj.getClass());
			}
		}
		fin.close();
    }

}
