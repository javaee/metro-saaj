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

package com.sun.xml.messaging.saaj.soap;

import java.awt.datatransfer.DataFlavor;
import java.io.*;
import java.awt.*;

import javax.activation.*;

/**
 * DataContentHandler for image/gif.
 *
 * @author Ana Lindstrom-Tamer
 */
public class GifDataContentHandler extends Component implements DataContentHandler {
    private static ActivationDataFlavor myDF =
        new ActivationDataFlavor(
            java.awt.Image.class,
            "image/gif",
            "GIF Image");

    protected ActivationDataFlavor getDF() {
        return myDF;
    }

    /**
     * Return the DataFlavors for this <code>DataContentHandler</code>.
     *
     * @return The DataFlavors
     */
    @Override
    public DataFlavor[] getTransferDataFlavors() { // throws Exception;
        return new DataFlavor[] { getDF()};
    }

    /**
     * Return the Transfer Data of type DataFlavor from InputStream.
     *
     * @param df The DataFlavor
     * @param ds The DataSource
     * @return String object
     * @exception IOException in case of an I/O error
     */
    @Override
    public Object getTransferData(DataFlavor df, DataSource ds)
        throws IOException {
        // use myDF.equals to be sure to get ActivationDataFlavor.equals,
        // which properly ignores Content-Type parameters in comparison
        if (getDF().equals(df))
            return getContent(ds);
        else
            return null;
    }

    @Override
    public Object getContent(DataSource ds) throws IOException {
	InputStream is = ds.getInputStream();
	int pos = 0;
	int count;
	byte buf[] = new byte[1024];

	while ((count = is.read(buf, pos, buf.length - pos)) != -1) {
	    pos += count;
	    if (pos >= buf.length) {
		int size = buf.length;
		if (size < 256*1024)
		    size += size;
		else
		    size += 256*1024;
		byte tbuf[] = new byte[size];
		System.arraycopy(buf, 0, tbuf, 0, pos);
		buf = tbuf;
	    }
	}
	Toolkit tk = Toolkit.getDefaultToolkit();
	return tk.createImage(buf, 0, pos);
    }

    /**
     * Write the object to the output stream, using the specified MIME type.
     * @param obj object to write
     * @param type requested MIME type of the resulting byte stream
     * @param os OutputStream
     */
    @Override
    public void writeTo(Object obj, String type, OutputStream os)
			throws IOException {
	if (obj != null && !(obj instanceof Image))
	    throw new IOException("\"" + getDF().getMimeType() +
		"\" DataContentHandler requires Image object, " +
		"was given object of type " + obj.getClass().toString());

	throw new IOException(getDF().getMimeType() + " encoding not supported");
    }
    

}
