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

package soap;

import java.io.ByteArrayInputStream;

import javax.xml.soap.*;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;

public class SOAPPartTest extends TestCase {

	public SOAPPartTest(String name) {
	        super(name);
    	}

    	public void testGetChildNodes() throws Exception {
        	String testDoc =
	 	"<env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/'\n"
                + " xmlns:ns1='http://example.com/wsdl'>\n"
                + "  <env:Body>\n"
                + "    <ns1:sayHello>\n"
                + "      <String_1>Duke!</String_1>\n"
                + "    </ns1:sayHello>\n"
                + "  </env:Body>\n"
                + "</env:Envelope>\n";

        	byte[] testDocBytes = testDoc.getBytes("UTF-8");
        	ByteArrayInputStream bais = new ByteArrayInputStream(testDocBytes);
	        StreamSource strSource = new StreamSource(bais);
        	MessageFactory mf = MessageFactory.newInstance();
	        SOAPMessage sm = mf.createMessage();
        	SOAPPart sp = sm.getSOAPPart();
	        sp.setContent(strSource);
		assertTrue(sp.getChildNodes().getLength()>0);
    	}
}

