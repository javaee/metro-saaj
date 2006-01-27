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
 * $Id: StringDataContentHandler.java,v 1.1.1.1 2006-01-27 13:10:55 kumarjayanti Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2006-01-27 13:10:55 $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sun.xml.messaging.saaj.soap;

import java.awt.datatransfer.DataFlavor;
import java.io.*;

import javax.activation.*;

/**
 * JAF data content handler for text/plain --> String
 *
 * @author Anil Vijendran
 */
public class StringDataContentHandler implements DataContentHandler {
    /**
     * return the DataFlavors for this <code>DataContentHandler</code>
     * @return The DataFlavors.
     */
    public DataFlavor[] getTransferDataFlavors() { // throws Exception;
        DataFlavor flavors[] = new DataFlavor[2];

        try {
            flavors[0] =
                new ActivationDataFlavor(
                    Class.forName("java.lang.String"),
                    "text/plain",
                    "text string");
        } catch (Exception e) {
        }

        flavors[1] = new DataFlavor("text/plain", "Plain Text");
        return flavors;
    }

    /**
     * return the Transfer Data of type DataFlavor from InputStream
     * @param df The DataFlavor.
     * @param ins The InputStream corresponding to the data.
     * @return The constructed Object.
     */
    public Object getTransferData(DataFlavor df, DataSource ds) {

        // this is sort of hacky, but will work for the
        // sake of testing...
        if (df.getMimeType().startsWith("text/plain")) {
            if (df
                .getRepresentationClass()
                .getName()
                .equals("java.lang.String")) {
                // spit out String
                StringBuffer buf = new StringBuffer();
                char data[] = new char[1024];
                // InputStream is = null;
                InputStreamReader isr = null;
                int bytes_read = 0;
                int total_bytes = 0;

                try {
                    isr = new InputStreamReader(ds.getInputStream());

                    while (true) {
                        bytes_read = isr.read(data);
                        if (bytes_read > 0)
                            buf.append(data, 0, bytes_read);
                        else
                            break;
                        total_bytes += bytes_read;
                    }
                } catch (Exception e) {
                }

                return buf.toString();

            } else if (
                df.getRepresentationClass().getName().equals(
                    "java.io.InputStream")) {
                // spit out InputStream
                try {
                    return ds.getInputStream();
                } catch (Exception e) {
                }
            }

        }
        return null;
    }

    /**
     *
     */
    public Object getContent(DataSource ds) { // throws Exception;
        StringBuffer buf = new StringBuffer();
        char data[] = new char[1024];
        // InputStream is = null;
        InputStreamReader isr = null;
        int bytes_read = 0;
        int total_bytes = 0;

        try {
            isr = new InputStreamReader(ds.getInputStream());

            while (true) {
                bytes_read = isr.read(data);
                if (bytes_read > 0)
                    buf.append(data, 0, bytes_read);
                else
                    break;
                total_bytes += bytes_read;
            }
        } catch (Exception e) {
        }

        return buf.toString();
    }
    /**
     * construct an object from a byte stream
     * (similar semantically to previous method, we are deciding
     *  which one to support)
     */
    public void writeTo(Object obj, String mimeType, OutputStream os)
        throws IOException {
        if (!mimeType.startsWith("text/plain"))
            throw new IOException(
                "Invalid type \"" + mimeType + "\" on StringDCH");

        Writer out = new OutputStreamWriter(os);
        out.write((String) obj);
        out.flush();
    }

}
