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

package bugfixes;

//import com.sun.xml.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl;
//import com.sun.xml.messaging.saaj.soap.ver1_2.SOAPMessageFactory1_2Impl;
import junit.framework.TestCase;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * TestCase for http://java.net/jira/browse/SAAJ-67
 * Loosing Mime Headers when Content-Type header not present
 *
 * @author Miroslav Kos (miroslav.kos at oracle.com)
 */
public class SAAJ67Test extends TestCase {
    private static final String EXTERNAL_1_1_NAME = "com.sun.xml.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl";
    private static final String EXTERNAL_1_2_NAME = "com.sun.xml.messaging.saaj.soap.ver1_2.SOAPMessageFactory1_2Impl";
    private static final String INTERNAL_1_1_NAME = "com.sun.xml.internal.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl";
    private static final String INTERNAL_1_2_NAME = "com.sun.xml.internal.messaging.saaj.soap.ver1_2.SOAPMessageFactory1_2Impl";

    void runWithFactory(MessageFactory factory, String messagePath, String contentType) throws SOAPException, IOException {
        MimeHeaders headers = new MimeHeaders();
        headers.addHeader("Content-Type", contentType);
        headers.addHeader("Some-Header", "Some-Value");
        InputStream is = new FileInputStream(messagePath);
        SOAPMessage msg = factory.createMessage(headers, is);
        assertNotNull(msg.getMimeHeaders().getHeader("Some-Header"));

        headers = new MimeHeaders();
        headers.addHeader("Some-Header", "Some-Value");
        is = new FileInputStream(messagePath);
        msg = factory.createMessage(headers, is);
        assertNotNull(msg.getMimeHeaders().getHeader("Some-Header"));
    }

    public void testFactory1_1() throws IOException, SOAPException {
        MessageFactory mf = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
        String isJDK = System.getProperty("jdk.build", "false");
        if ("true".equalsIgnoreCase(isJDK)) {
            assertEquals(INTERNAL_1_1_NAME, mf.getClass().getName());
        } else {
            assertEquals(EXTERNAL_1_1_NAME, mf.getClass().getName());
        }
        runWithFactory(mf, "src/test/bugfixes/data/empty-message.xml", "text/xml");
    }

    public void testFactory1_2() throws IOException, SOAPException {
        MessageFactory mf = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        String isJDK = System.getProperty("jdk.build", "false");
        if ("true".equalsIgnoreCase(isJDK)) {
            assertEquals(INTERNAL_1_2_NAME, mf.getClass().getName());
        } else {
            assertEquals(EXTERNAL_1_2_NAME, mf.getClass().getName());
        }
        runWithFactory(mf, "src/test/bugfixes/data/empty-message12.xml", "application/soap+xml");
    }
}
