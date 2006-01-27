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
 * $Id: FactoryDataContentHandler.java,v 1.1.1.1 2006-01-27 13:11:00 kumarjayanti Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2006-01-27 13:11:00 $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package mime.custom;

import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.io.OutputStream;

import javax.activation.*;

/**
 * DataContentHandler for Content-Type : custom/factory
 *
 * @author Jitendra Kotamraju (jitendra.kotamraju@sun.com)
 */
public class FactoryDataContentHandler implements DataContentHandler {
   
    private static ActivationDataFlavor myDF =
        new ActivationDataFlavor(
            mime.custom.CustomType.class,
            "custom/factory",
            "Custom Type");

    protected ActivationDataFlavor getDF() {
        return myDF;
    }

    /**
     * Return the DataFlavors for this <code>DataContentHandler</code>.
     *
     * @return The DataFlavors
     */
    public DataFlavor[] getTransferDataFlavors() { // throws Exception;
        return new DataFlavor[] { getDF() };
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
        return new CustomType();
    }

    /**
     * Write the object to the output stream, using the specified MIME type.
     */
    public void writeTo(Object obj, String type, OutputStream os)
        throws IOException {
        if (!(obj instanceof CustomType)) {             
            throw new IOException(
                    getDF().getMimeType()
                    + " DataContentHandler requires CustomType object, "
                    + "instead got "
                    + obj.getClass().toString());
        }
		os.write((int)'F');
		os.flush();
    }
}
