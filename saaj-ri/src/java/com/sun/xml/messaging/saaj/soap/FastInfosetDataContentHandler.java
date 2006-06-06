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
 * $Id: FastInfosetDataContentHandler.java,v 1.2 2006-06-06 18:46:01 sandoz Exp $
 * $Revision: 1.2 $
 * $Date: 2006-06-06 18:46:01 $
 */

/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sun.xml.messaging.saaj.soap;

import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

import javax.activation.*;
import javax.xml.transform.Source;

import com.sun.xml.messaging.saaj.util.FastInfosetReflection;

/**
 * JAF data handler for Fast Infoset content
 *
 * @author Santiago Pericas-Geertsen
 */
public class FastInfosetDataContentHandler implements DataContentHandler {
    public final String STR_SRC = "org.jvnet.fastinfoset.FastInfosetSource";

    public FastInfosetDataContentHandler() {
    }

    /**
     * return the DataFlavors for this <code>DataContentHandler</code>
     * @return The DataFlavors.
     */
    public DataFlavor[] getTransferDataFlavors() { // throws Exception;
        DataFlavor flavors[] = new DataFlavor[1];
        flavors[0] = new ActivationDataFlavor(
                FastInfosetReflection.getFastInfosetSource_class(), 
                "application/fastinfoset", "Fast Infoset");
        return flavors;
    }

    /**
     * return the Transfer Data of type DataFlavor from InputStream
     * @param df The DataFlavor.
     * @param ins The InputStream corresponding to the data.
     * @return The constructed Object.
     */
    public Object getTransferData(DataFlavor flavor, DataSource dataSource)
        throws IOException 
    {
        if (flavor.getMimeType().startsWith("application/fastinfoset")) {
            try {
                if (flavor.getRepresentationClass().getName().equals(STR_SRC)) {
                    return FastInfosetReflection.FastInfosetSource_new(
                        dataSource.getInputStream());
                }
            }
            catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        }
        return null;
    }

    public Object getContent(DataSource dataSource) throws IOException {
        try {
            return FastInfosetReflection.FastInfosetSource_new(
                dataSource.getInputStream());
        }
        catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * construct an object from a byte stream
     * (similar semantically to previous method, we are deciding
     *  which one to support)
     */
    public void writeTo(Object obj, String mimeType, OutputStream os)
        throws IOException 
    {
        if (!mimeType.equals("application/fastinfoset")) {
            throw new IOException("Invalid content type \"" + mimeType 
                + "\" for FastInfosetDCH");
        }
        
        try {            
            InputStream is = FastInfosetReflection.FastInfosetSource_getInputStream(
                (Source) obj);
            
	    int n; byte[] buffer = new byte[4096];
            while ((n = is.read(buffer)) != -1) {
                os.write(buffer, 0, n);
            }
        } 
        catch (Exception ex) {
            throw new IOException(
                "Error copying FI source to output stream " + ex.getMessage());
        }
    }
}
