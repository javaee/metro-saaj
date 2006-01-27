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
 * $Id: CustomTypeTest.java,v 1.1.1.1 2006-01-27 13:11:00 kumarjayanti Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2006-01-27 13:11:00 $
 */

package mime.custom;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

import javax.activation.*;
import javax.xml.soap.*;

import junit.framework.TestCase;


/*
 * Attaches a custom object to a SOAP message in two different ways. The data
 * content handlers are got using MailcapCommandMap and DataContentHandlerFactory
 *
 * @author Jitendra Kotamraju (jitendra.kotamraju@sun.com)
 */

public class CustomTypeTest extends TestCase {

    public CustomTypeTest(String name) {
        super(name);
    }

	/*
     * Add CustomType object to SOAP message and verify that it comes back
	 * after writing to a file
     */
    public void addCustomObjAndVerify(String mimeType) throws Exception {

        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();        
        SOAPPart sp = msg.getSOAPPart();
        
        SOAPEnvelope envelope = sp.getEnvelope();
        
        SOAPHeader hdr = envelope.getHeader();
        SOAPBody bdy = envelope.getBody();

		// Add something to body 
        SOAPBodyElement gltp = bdy.addBodyElement(
			envelope.createName("GetLastTradePrice", "ztrade",
				"http://wombat.ztrade.com"));
        
        gltp.addChildElement(envelope.createName("symbol", "ztrade",
			"http://wombat.ztrade.com")).addTextNode("SUNW");
        
        // Attach Custom Type        
		CustomType type = new CustomType();
        AttachmentPart ap = msg.createAttachmentPart(type, mimeType);
        msg.addAttachmentPart(ap);
        msg.saveChanges();

		// Save the soap message to file
		FileOutputStream sentFile = new FileOutputStream(
			"src/test/mime/data/custom_type_sent.ctp");
		msg.writeTo(sentFile);
		sentFile.close();

		// See if we get the CustomType object back
		FileInputStream fin= new FileInputStream(
			"src/test/mime/data/custom_type_sent.ctp");
		SOAPMessage newMsg = mf.createMessage(msg.getMimeHeaders(), fin);
		Iterator i = newMsg.getAttachments();
		while(i.hasNext()) {
			AttachmentPart att = (AttachmentPart)i.next();
			CustomType obj = (CustomType)att.getContent();	// Works or throws
			break;
		}
		fin.close();
    }


	/*
	 * Set DataConentHandlerFactory which provides a DataContentHandler for
	 * "custom/factory" MIME type
	 */
	private void setFactory() {
		DataHandler.setDataContentHandlerFactory(
			new CustomDataContentHandlerFactory());
	}


    public void testCustomTypeUsingFactory() {
        try {
			setFactory();
            addCustomObjAndVerify("custom/factory");
        } catch (Exception e) {
            e.printStackTrace();
            fail("No exception should have been thrown");
        }
    }

	/*
	 * Set MailCap entry which provides a DataContentHandler for
	 * "custom/mailcap" MIME type
	 */
	private void setMailcap() {
		// Register data content handler for "custom/mailcap"
		CommandMap map = CommandMap.getDefaultCommandMap();
		if (map instanceof MailcapCommandMap) {
			MailcapCommandMap mailMap = (MailcapCommandMap)map;
			String hndlrStr = "; ;x-java-content-handler=";
			mailMap.addMailcap("custom/mailcap"+hndlrStr
				+"mime.custom.MailcapDataContentHandler");
		}
	}

    
    public void testCustomTypeUsingMailcap() {
        try {
			setMailcap();
            addCustomObjAndVerify("custom/mailcap");
        } catch (Exception e) {
            e.printStackTrace();
            fail("No exception should have been thrown");
        }
    }
}
