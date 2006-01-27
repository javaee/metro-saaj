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
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.xml.soap;

import java.util.Iterator;
import java.util.Vector;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPHeaderElement;

public abstract class SOAPRecipient implements ProcessingFaultHandler {

    protected Vector roles = new Vector();
    protected Vector supportedHeaders = new Vector();

    public void addRole(String role) {
        roles.addElement(role);
    }

    public Iterator getRoles() {
        return roles.iterator();
    }

    public boolean supportsRole(String role) {
        for (Iterator it = roles.iterator(); it.hasNext();) {
            String thisRole = (String) it.next();
            if (role.equals(thisRole))
                return true;
        }
        return false;
    }

    public void addHeader(QName header) {
        supportedHeaders.addElement(header);
    }

    public QName[] getHeaders() {
        QName[] returnValue = new QName[supportedHeaders.size()];
        return (QName[]) supportedHeaders.toArray(returnValue);
    }

    public boolean supportsHeader(QName header) {
        for (Iterator it = supportedHeaders.iterator(); it.hasNext();) {
            QName thisHeader = (QName) it.next();
            if (header.equals(thisHeader))
                return true;
        }
        return false;
    }

    public abstract void acceptHeaderElement(SOAPHeaderElement element, ProcessingContext context)
        throws Exception;
    
}
