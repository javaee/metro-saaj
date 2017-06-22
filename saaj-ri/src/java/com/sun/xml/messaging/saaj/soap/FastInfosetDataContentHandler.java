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
    public static final String STR_SRC = "org.jvnet.fastinfoset.FastInfosetSource";

    public FastInfosetDataContentHandler() {
    }

    /**
     * Return the DataFlavors for this <code>DataContentHandler</code>
     * @return The DataFlavors.
     */
    @Override
    public DataFlavor[] getTransferDataFlavors() { // throws Exception;
        DataFlavor flavors[] = new DataFlavor[1];
        flavors[0] = new ActivationDataFlavor(
                FastInfosetReflection.getFastInfosetSource_class(), 
                "application/fastinfoset", "Fast Infoset");
        return flavors;
    }

    /**
     * Return the Transfer Data of type DataFlavor from InputStream
     * @param flavor The DataFlavor.
     * @param dataSource DataSource.
     * @return The constructed Object.
     * @exception IOException in case of an I/O error
     */
    @Override
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

    @Override
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
     * Construct an object from a byte stream
     * (similar semantically to previous method, we are deciding
     *  which one to support)
     */
    @Override
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
