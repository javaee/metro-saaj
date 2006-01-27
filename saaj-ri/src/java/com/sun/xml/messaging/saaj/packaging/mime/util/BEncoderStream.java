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
 * @(#)BEncoderStream.java    1.3 02/03/27
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.xml.messaging.saaj.packaging.mime.util;

import java.io.OutputStream;

/**
 * This class implements a 'B' Encoder as defined by RFC2047 for
 * encoding MIME headers. It subclasses the BASE64EncoderStream
 * class.
 * 
 * @author John Mani
 */

public class BEncoderStream extends BASE64EncoderStream {

    /**
     * Create a 'B' encoder that encodes the specified input stream.
     * @param out        the output stream
     */
    public BEncoderStream(OutputStream out) {
	super(out, Integer.MAX_VALUE); // MAX_VALUE is 2^31, should
				       // suffice (!) to indicate that
				       // CRLFs should not be inserted
    }

    /**
     * Returns the length of the encoded version of this byte array.
     */
    public static int encodedLength(byte[] b) {
        return ((b.length + 2)/3) * 4;
    }
}
