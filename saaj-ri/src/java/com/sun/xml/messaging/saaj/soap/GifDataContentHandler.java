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
 * $Id: GifDataContentHandler.java,v 1.1.1.1 2006-01-27 13:10:55 kumarjayanti Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2006-01-27 13:10:55 $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sun.xml.messaging.saaj.soap;

import java.awt.datatransfer.DataFlavor;
import java.io.*;
import com.sun.image.codec.jpeg.*;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.MediaTracker;

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
    public DataFlavor[] getTransferDataFlavors() { // throws Exception;
        return new DataFlavor[] { getDF()};
    }

    /**
     * Return the Transfer Data of type DataFlavor from InputStream.
     *
     * @param df The DataFlavor
     * @param ins The InputStream corresponding to the data
     * @return String object
     */
    public Object getTransferData(DataFlavor df, DataSource ds)
        throws IOException {
        // use myDF.equals to be sure to get ActivationDataFlavor.equals,
        // which properly ignores Content-Type parameters in comparison
        if (getDF().equals(df))
            return getContent(ds);
        else
            return null;
    }

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
     */
    public void writeTo(Object obj, String type, OutputStream os)
			throws IOException {
	if (!(obj instanceof Image))
	    throw new IOException("\"" + getDF().getMimeType() +
		"\" DataContentHandler requires Image object, " +
		"was given object of type " + obj.getClass().toString());

	throw new IOException(getDF().getMimeType() + " encoding not supported");
    }
    

}
