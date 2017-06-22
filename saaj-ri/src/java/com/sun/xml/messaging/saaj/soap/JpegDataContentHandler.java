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

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.activation.*;

//import com.sun.image.codec.jpeg.*;
import javax.imageio.ImageIO;

/**
 * JAF data handler for Jpeg content
 *
 * @author Ana Lindstrom-Tamer
 */

public class JpegDataContentHandler
    extends Component
    implements DataContentHandler {
    public static final String STR_SRC = "java.awt.Image";

    /**
     * Return the DataFlavors for this <code>DataContentHandler</code>
     * @return The DataFlavors.
     */
    @Override
    public DataFlavor[] getTransferDataFlavors() { // throws Exception;
        DataFlavor flavors[] = new DataFlavor[1];

        try {
            flavors[0] =
                new ActivationDataFlavor(
                    Class.forName(STR_SRC),
                    "image/jpeg",
                    "JPEG");
        } catch (Exception e) {
            System.out.println(e);
        }

        return flavors;
    }

    /**
     * Return the Transfer Data of type DataFlavor from InputStream
     * @param df The DataFlavor
     * @param ds The DataSource
     * @return The constructed Object.
     */
    @Override
    public Object getTransferData(DataFlavor df, DataSource ds) {

        // this is sort of hacky, but will work for the
        // sake of testing...
        if (df.getMimeType().startsWith("image/jpeg")) {
            if (df.getRepresentationClass().getName().equals(STR_SRC)) {
                InputStream inputStream = null;
                BufferedImage jpegLoadImage = null;

                try {
                    inputStream = ds.getInputStream();
                    jpegLoadImage = ImageIO.read(inputStream); 

                } catch (Exception e) {
                    System.out.println(e);
                }

                return jpegLoadImage;
            }
        }
        return null;
    }

    /**
     *
     */
    @Override
    public Object getContent(DataSource ds) { // throws Exception;
        InputStream inputStream = null;
        BufferedImage jpegLoadImage = null;

        try {
            inputStream = ds.getInputStream();
            jpegLoadImage = ImageIO.read(inputStream);

        } catch (Exception e) {
        }

        return jpegLoadImage;
    }

    /**
     * Construct an object from a byte stream
     * (similar semantically to previous method, we are deciding
     *  which one to support)
     * @param obj object to write
     * @param mimeType requested MIME type of the resulting byte stream
     * @param os OutputStream
     */
    @Override
    public void writeTo(Object obj, String mimeType, OutputStream os)
        throws IOException {
        if (!mimeType.equals("image/jpeg"))
            throw new IOException(
                "Invalid content type \""
                    + mimeType
                    + "\" for ImageContentHandler");

        if (obj == null) {
            throw new IOException("Null object for ImageContentHandler");
        }

        try {
            BufferedImage bufImage = null;
            if (obj instanceof BufferedImage) {
                bufImage = (BufferedImage) obj;

            } else {
                Image img = (Image) obj;
                MediaTracker tracker = new MediaTracker(this);
                tracker.addImage(img, 0);
                tracker.waitForAll();
                if (tracker.isErrorAny()) {
			throw new IOException("Error while loading image");
		}
                bufImage =
                    new BufferedImage(
                        img.getWidth(null),
                        img.getHeight(null),
                        BufferedImage.TYPE_INT_RGB);

                Graphics g = bufImage.createGraphics();
                g.drawImage(img, 0, 0, null);
            }
            ImageIO.write(bufImage, "jpeg", os);

        } catch (Exception ex) {
            throw new IOException(
                "Unable to run the JPEG Encoder on a stream "
                    + ex.getMessage());
        }
    }
}
