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

/*
 * Created on Feb 28, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.sun.xml.messaging.saaj.util;

import java.io.*;

/**
 * @author pgoodwin
 */
public class TeeInputStream extends InputStream {
    protected InputStream source;
    protected OutputStream copySink;
    
    public TeeInputStream(InputStream source, OutputStream sink) {
        super();
        this.copySink = sink;
        this.source = source;
    }

    public int read() throws IOException {
        int result = source.read();
        copySink.write(result);
        return result;
    }

    public int available() throws IOException {
        return source.available();
    }

    public void close() throws IOException {
        source.close();
    }

    public synchronized void mark(int readlimit) {
        source.mark(readlimit);
    }

    public boolean markSupported() {
        return source.markSupported();
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int result = source.read(b, off, len);
        copySink.write(b, off, len);
        return result;
    }

    public int read(byte[] b) throws IOException {
        int result = source.read(b);
        copySink.write(b);
        return result;
    }

    public synchronized void reset() throws IOException {
        source.reset();
    }

    public long skip(long n) throws IOException {
        return source.skip(n);
    }

}
