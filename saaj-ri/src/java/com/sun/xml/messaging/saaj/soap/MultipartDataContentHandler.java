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
    @Override
    public DataFlavor[] getTransferDataFlavors() { // throws Exception;
	return new DataFlavor[] { myDF };
    }

    /**
     * Return the Transfer Data of type DataFlavor from InputStream.
     *
     * @param df The DataFlavor
     * @param ds The DataSource
     * @return String object
     */
    @Override
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
     *
     * @param ds The DataSource
     * @return content
     */
    @Override
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
    @Override
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

