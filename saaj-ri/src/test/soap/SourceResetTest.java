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

package soap;

import java.io.*;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.soap.*;


import junit.framework.TestCase;

public class SourceResetTest extends TestCase {

    private String msgText[];

    public SourceResetTest(String name) {
        super(name);
    }

    public void testReset() throws Exception {
        String message = messageThroughBodyElement +
            "<ns0:helloWorld><String_1 xsi:type=\"xsd:string\">" +
            "foo2</String_1></ns0:helloWorld>" +
            endBodyElementMessage;

        // JAXRPC servlet creates SOAP message
        MimeHeaders headers = new MimeHeaders();
        headers.addHeader("Content-Type", "text/xml");
        //ByteInputStream in = new ByteInputStream(message.getBytes(),
	//		message.getBytes().length);
        ByteArrayInputStream in = new ByteArrayInputStream(message.getBytes());
        SOAPMessage msg = MessageFactory.newInstance().createMessage(headers,
			in);

        // JAXRPC streaming parser consumes the of stream partially/fully
        // Then invokes the service method
        jaxrpcParser(msg);

        // Uncomment the following to work without exceptions. This resets the
        // stream. Since SAAJ keeps a copy in JAXMStreamSource, SAAJ can get
        // bytes any time. JAXRPC doesn't call close() consistently.
        //in.close();

        // Service method calls the following to access message. This doesn't
        // work because the stream is incorrect state.
        SOAPPart part = msg.getSOAPPart();
		SOAPEnvelope envelope = part.getEnvelope();
    }

    /*
     * Parses the SOAP message and deserializes to data types
     */
    protected static void jaxrpcParser(SOAPMessage msg)
		throws Exception {

        Source source = msg.getSOAPPart().getContent();
        InputStream istream = ((StreamSource) source).getInputStream();

        if (istream != null) {
        	byte[] buf = new byte[1024];
        	int num = 0;
            while ((num = istream.read(buf)) != -1) {
            }
        }
    }

    private static final String UTF8_DECL = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    private static final String messageThroughBodyElement =
        "<env:Envelope " +
        "xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
        "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
        "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
        "xmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\" " +
        "xmlns:ns0=\"http://hello.org/wsdl\" " +
        "env:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
        "<env:Body>";
    private String endBodyElementMessage = "</env:Body></env:Envelope>";

}
