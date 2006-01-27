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
 * $Id: ByteInputStream.java,v 1.1.1.1 2006-01-27 13:10:58 kumarjayanti Exp $ $Revision:
 * 1.3 $ $Date: 2006-01-27 13:10:58 $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sun.xml.messaging.saaj.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;

// This class just gives access to the underlying buffer without copying.

public class ByteInputStream extends ByteArrayInputStream {
    private static final byte[] EMPTY_ARRAY = new byte[0];
    
    public ByteInputStream() {
        this(EMPTY_ARRAY, 0);
    }
    
    public ByteInputStream(byte buf[], int length) {
        super(buf, 0, length);
    }

    public ByteInputStream(byte buf[], int offset, int length) {
        super(buf, offset, length);
    }

    public byte[] getBytes() {
        return buf;
    }

    public int getCount() {
        return count;
    }

    public void close() throws IOException {
        reset();
    }

    public void setBuf(byte[] buf) {
        this.buf = buf;
        this.pos = 0;
        this.count = buf.length;
    }
}
