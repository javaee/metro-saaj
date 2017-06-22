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

package mime;

import javax.xml.soap.*;

import java.io.ByteArrayInputStream;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;

public class CharacterSetEncodingTest extends TestCase {

    public CharacterSetEncodingTest(String name) {
        super(name);
    }

    public void testCharacterSetEncoding() throws Exception {
        String testString = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body/></soap:Envelope>";
        byte[] utf16bytes = testString.getBytes("utf-16");
        ByteArrayInputStream bais = new ByteArrayInputStream(utf16bytes);
        MimeHeaders headers = new MimeHeaders();
        headers.setHeader("Content-Type", "text/xml; charset=utf-16");
        SOAPMessage msg =
            MessageFactory.newInstance().createMessage(headers, bais);
        msg.saveChanges();

        headers = msg.getMimeHeaders();
        String contentTypeString  = headers.getHeader("Content-Type")[0];
        assertEquals(contentTypeString, "text/xml; charset=utf-16");
   }
    
    public void testCharacterSetUtf16() throws Exception {
    
        MessageFactory factory = MessageFactory.newInstance();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-16\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><SOAP-ENV:Body><p:content SOAP-ENV:encodingStyle=\"http://example.com/encoding\" xmlns:p=\"some-uri\">Jeu universel de caract�res cod�s � plusieurs octets</p:content></SOAP-ENV:Body></SOAP-ENV:Envelope>";
        SOAPMessage msg = factory.createMessage();
        msg.getMimeHeaders().setHeader("Content-Type","text/xml; charset=utf-16");
        msg.getSOAPPart().setContent(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-16"))));   
        msg.saveChanges(); 

        MimeHeaders headers = msg.getMimeHeaders();
                
        String contentTypeString  = headers.getHeader("Content-Type")[0];
        assertEquals(contentTypeString, "text/xml; charset=utf-16");
    }
    
    /* 
     * MimeHeaders.getHeader seems to fail in SOAP 1.2
     * bug id: 6267874 
     */
    
       public void testCharacterSetSOAP12() throws Exception {
        String testString = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\"><soap:Body/></soap:Envelope>";
        byte[] utf16bytes = testString.getBytes("utf-16");
        ByteArrayInputStream bais = new ByteArrayInputStream(utf16bytes);
        MimeHeaders headers = new MimeHeaders();
        //headers.setHeader("Content-Type", "");
        SOAPMessage msg =
            MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage(headers, bais);
        msg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING,"utf-16");
        msg.saveChanges();
      
        headers = msg.getMimeHeaders();
        headers.setHeader("Content-Type", "");
        String contentTypeString  = headers.getHeader("Content-Type")[0];
        assertEquals(contentTypeString, "");
   }
}
