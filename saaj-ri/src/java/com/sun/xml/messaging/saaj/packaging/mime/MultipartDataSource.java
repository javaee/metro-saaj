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
 * @(#)MultipartDataSource.java       1.6 02/03/27
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.xml.messaging.saaj.packaging.mime;

import com.sun.xml.messaging.saaj.packaging.mime.internet.MimeBodyPart;

import javax.activation.DataSource;

/**
 * MultipartDataSource is a <code>DataSource</code> that contains body
 * parts.  This allows "mail aware" <code>DataContentHandlers</code> to
 * be implemented more efficiently by being aware of such
 * <code>DataSources</code> and using the appropriate methods to access
 * <code>BodyParts</code>. <p>
 *
 * Note that the data of a MultipartDataSource is also available as
 * an input stream. <p>
 *
 * This interface will typically be implemented by providers that
 * preparse multipart bodies, for example an IMAP provider.
 *
 * @version	1.6, 02/03/27
 * @author	John Mani
 * @see		javax.activation.DataSource
 */

public interface MultipartDataSource extends DataSource {

    /**
     * Return the number of enclosed MimeBodyPart objects.
     *
     * @return          number of parts
     */
    public int getCount();

    /**
     * Get the specified MimeBodyPart.  Parts are numbered starting at 0.
     *
     * @param index     the index of the desired MimeBodyPart
     * @return          the MimeBodyPart
     * @exception       IndexOutOfBoundsException if the given index
     *			is out of range.
     * @exception       MessagingException
     */
    public MimeBodyPart getBodyPart(int index) throws MessagingException;

}
