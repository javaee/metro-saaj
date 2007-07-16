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

/**
 * $Id: CustomTypeTest.java,v 1.2 2007-07-16 16:41:28 ofung Exp $
 * $Revision: 1.2 $
 * $Date: 2007-07-16 16:41:28 $
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
