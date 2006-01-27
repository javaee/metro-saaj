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
 * $Id: Envelope.java,v 1.1.1.1 2006-01-27 13:10:55 kumarjayanti Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2006-01-27 13:10:55 $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sun.xml.messaging.saaj.soap;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.soap.SOAPEnvelope;
import javax.xml.transform.Source;

/**
 * Different implementations for SOAP Envelope must all implement this
 * interface.
 *
 * @author Anil Vijendran (akv@eng.sun.com)
 */
public interface Envelope extends SOAPEnvelope {
    /**
     * Get the content as a JAXP Source.
     */
    Source getContent();

    /**
     * Output the content.
     */
    void output(OutputStream out) throws IOException;
    
    /**
     * Output the content.
     */
    void output(OutputStream out, boolean isFastInfoset) throws IOException;
}
