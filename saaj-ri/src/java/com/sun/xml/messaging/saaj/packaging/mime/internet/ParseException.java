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
 * @(#)ParseException.java    1.3 02/03/27
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.xml.messaging.saaj.packaging.mime.internet;

import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;

/**
 * The exception thrown due to an error in parsing RFC822 
 * or MIME headers
 *
 * @author John Mani
 */

public class ParseException extends MessagingException {

    /**
     * Constructs a ParseException with no detail message.
     */
    public ParseException() {
	super();
    }

    /**
     * Constructs a ParseException with the specified detail message.
     * @param s		the detail message
     */
    public ParseException(String s) {
	super(s);
    }
}
