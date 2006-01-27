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
 * $Id: MultipartDataContentHandler.java,v 1.1.1.1 2006-01-27 13:10:55 kumarjayanti Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2006-01-27 13:10:55 $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sun.xml.messaging.saaj.soap;

import java.io.*;
import java.awt.datatransfer.DataFlavor;
import javax.activation.*;
import com.sun.xml.messaging.saaj.packaging.mime.internet.MimeMultipart;
import com.sun.xml.messaging.saaj.packaging.mime.internet.ContentType;
import com.sun.xml.messaging.saaj.util.ByteOutputStream;

public class MultipartDataContentHandler implements DataContentHandler {
    private ActivationDataFlavor myDF = new ActivationDataFlavor(
	    com.sun.xml.messaging.saaj.packaging.mime.internet.MimeMultipart.class,
	    "multipart/mixed", 
	    "Multipart");

    /**
     * Return the DataFlavors for this <code>DataContentHandler</code>.
     *
     * @return The DataFlavors
     */
    public DataFlavor[] getTransferDataFlavors() { // throws Exception;
	return new DataFlavor[] { myDF };
    }

    /**
     * Return the Transfer Data of type DataFlavor from InputStream.
     *
     * @param df The DataFlavor
     * @param ins The InputStream corresponding to the data
     * @return String object
     */
    public Object getTransferData(DataFlavor df, DataSource ds) {
	// use myDF.equals to be sure to get ActivationDataFlavor.equals,
	// which properly ignores Content-Type parameters in comparison
	if (myDF.equals(df))
	    return getContent(ds);
	else
	    return null;
    }
    
    /**
     * Return the content.
     */
    public Object getContent(DataSource ds) {
	try {
	    return new MimeMultipart(
                ds, new ContentType(ds.getContentType())); 
	} catch (Exception e) {
	    return null;
	}
    }
    
    /**
     * Write the object to the output stream, using the specific MIME type.
     */
    public void writeTo(Object obj, String mimeType, OutputStream os) 
			throws IOException {
	if (obj instanceof MimeMultipart) {
	    try {
                //TODO: temporarily allow only ByteOutputStream
                // Need to add writeTo(OutputStream) on MimeMultipart
                ByteOutputStream baos = null; 
                if (os instanceof ByteOutputStream) {
                    baos = (ByteOutputStream)os;
                } else {
                    throw new IOException("Input Stream expected to be a com.sun.xml.messaging.saaj.util.ByteOutputStream, but found " + 
                        os.getClass().getName());
                }
		((MimeMultipart)obj).writeTo(baos);
	    } catch (Exception e) {
		throw new IOException(e.toString());
	    }
	}
    }
}

