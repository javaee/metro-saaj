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
 * $Id: SOAPMessageFactory1_2Impl.java,v 1.1.1.1 2006-01-27 13:10:57 kumarjayanti Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/**
*
* @author JAX-RPC RI Development Team
*/
package com.sun.xml.messaging.saaj.soap.ver1_2;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.soap.*;

import com.sun.xml.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.messaging.saaj.soap.MessageFactoryImpl;
import com.sun.xml.messaging.saaj.soap.MessageImpl;

public class SOAPMessageFactory1_2Impl extends MessageFactoryImpl {

    public SOAPMessage createMessage() throws SOAPException {
        return new Message1_2Impl();
    }

    public SOAPMessage createMessage(boolean isFastInfoset, 
        boolean acceptFastInfoset) throws SOAPException 
    {
        return new Message1_2Impl(isFastInfoset, acceptFastInfoset);
    }
    
    public SOAPMessage createMessage(MimeHeaders headers, InputStream in)
        throws IOException, SOAPExceptionImpl {
        if ((headers == null) || (getContentType(headers) == null)) {
            headers = new MimeHeaders();
            headers.setHeader("Content-Type", SOAPConstants.SOAP_1_2_CONTENT_TYPE);
        }
        MessageImpl msg = new Message1_2Impl(headers, in);
        msg.setLazyAttachments(lazyAttachments);
        return msg;
    }
}
