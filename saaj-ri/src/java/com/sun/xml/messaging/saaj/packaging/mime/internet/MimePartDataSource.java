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

/*
 * @(#)MimePartDataSource.java        1.9 02/03/27
 */


package com.sun.xml.messaging.saaj.packaging.mime.internet;

import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;

import javax.activation.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownServiceException;

/**
 * A utility class that implements a DataSource out of
 * a MimeBodyPart. This class is primarily meant for service providers.
 *
 * @author John Mani
 */

public final class MimePartDataSource implements DataSource {
    private final MimeBodyPart part;

    /**
     * Constructor, that constructs a DataSource from a MimeBodyPart.
     *
     * @param part body part
     */
    public MimePartDataSource(MimeBodyPart part) {
        this.part = part;
    }

    /**
     * Returns an input stream from this  MimeBodyPart. <p>
     *
     * This method applies the appropriate transfer-decoding, based
     * on the Content-Transfer-Encoding attribute of this MimeBodyPart.
     * Thus the returned input stream is a decoded stream of bytes.<p>
     *
     * This implementation obtains the raw content from the MimeBodyPart
     * using the <code>getContentStream()</code> method and decodes
     * it using the <code>MimeUtility.decode()</code> method.
     *
     * @return decoded input stream
     */
    @Override
    public InputStream getInputStream() throws IOException {

        try {
            InputStream is = part.getContentStream();

            String encoding = part.getEncoding();
            if (encoding != null)
                return MimeUtility.decode(is, encoding);
            else
                return is;
        } catch (MessagingException mex) {
            throw new IOException(mex.getMessage());
        }
    }

    /**
     * DataSource method to return an output stream. <p>
     *
     * This implementation throws the UnknownServiceException.
     */
    @Override
    public OutputStream getOutputStream() throws IOException {
        throw new UnknownServiceException();
    }

    /**
     * Returns the content-type of this DataSource. <p>
     *
     * This implementation just invokes the <code>getContentType</code>
     * method on the MimeBodyPart.
     */
    @Override
    public String getContentType() {
        return part.getContentType();
    }

    /**
     * DataSource method to return a name.  <p>
     *
     * This implementation just returns an empty string.
     */
    @Override
    public String getName() {
        try {
            return part.getFileName();
        } catch (MessagingException mex) {
            return "";
        }
    }
}
