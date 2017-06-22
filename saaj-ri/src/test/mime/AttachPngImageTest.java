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
