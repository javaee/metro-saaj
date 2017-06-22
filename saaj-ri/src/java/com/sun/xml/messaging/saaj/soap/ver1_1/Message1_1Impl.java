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
* @author SAAJ RI Development Team
*/
package com.sun.xml.messaging.saaj.soap.ver1_1;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.soap.*;
import javax.xml.stream.XMLStreamReader;

import com.sun.xml.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.messaging.saaj.packaging.mime.internet.ContentType;
import com.sun.xml.messaging.saaj.soap.MessageImpl;
import com.sun.xml.messaging.saaj.util.LogDomainConstants;

public class Message1_1Impl extends MessageImpl implements SOAPConstants {
    
    protected static final Logger log =
        Logger.getLogger(LogDomainConstants.SOAP_VER1_1_DOMAIN,
                         "com.sun.xml.messaging.saaj.soap.ver1_1.LocalStrings");
   
    public Message1_1Impl() {
        super();
    }
    
    public Message1_1Impl(boolean isFastInfoset, boolean acceptFastInfoset) {
        super(isFastInfoset, acceptFastInfoset);
    }

    public Message1_1Impl(SOAPMessage msg) {
        super(msg);
    }

    // unused. can we delete this? - Kohsuke
    public Message1_1Impl(MimeHeaders headers, InputStream in)
        throws IOException, SOAPExceptionImpl {
        super(headers, in);
    }

    public Message1_1Impl(MimeHeaders headers, ContentType ct, int stat, InputStream in)
        throws SOAPExceptionImpl {
        super(headers,ct,stat,in);
    }

    public Message1_1Impl(MimeHeaders headers, ContentType ct, int stat, XMLStreamReader reader)
            throws SOAPExceptionImpl {
            super(headers,ct,stat,reader);
    }
    
    @Override
    public SOAPPart getSOAPPart() {
        if (soapPartImpl == null) {
            soapPartImpl = new SOAPPart1_1Impl(this);
        }
        return soapPartImpl;
    }

    @Override
    protected boolean isCorrectSoapVersion(int contentTypeId) {
        return (contentTypeId & SOAP1_1_FLAG) != 0;
    }

    @Override
    public String getAction() {
        log.log(
            Level.SEVERE,
            "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1",
            new String[] { "Action" });
        throw new UnsupportedOperationException("Operation not supported by SOAP 1.1");
    }

    @Override
    public void setAction(String type) {
        log.log(
            Level.SEVERE,
            "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1",
            new String[] { "Action" });
        throw new UnsupportedOperationException("Operation not supported by SOAP 1.1");
    }

    @Override
    public String getCharset() {
        log.log(
            Level.SEVERE,
            "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1",
            new String[] { "Charset" });
        throw new UnsupportedOperationException("Operation not supported by SOAP 1.1");
    }

    @Override
    public void setCharset(String charset) {
        log.log(
            Level.SEVERE,
            "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1",
            new String[] { "Charset" });
        throw new UnsupportedOperationException("Operation not supported by SOAP 1.1");
    }

    @Override
    protected String getExpectedContentType() {
        return isFastInfoset ? "application/fastinfoset" : "text/xml";
    }

    @Override
   protected String getExpectedAcceptHeader() {
       String accept = "text/xml, text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2";
       return acceptFastInfoset ? ("application/fastinfoset, " + accept) : accept;
   }

}
