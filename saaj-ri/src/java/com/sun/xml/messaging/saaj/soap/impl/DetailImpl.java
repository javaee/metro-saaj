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
 * $Id: DetailImpl.java,v 1.1.1.1 2006-01-27 13:10:56 kumarjayanti Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2006-01-27 13:10:56 $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sun.xml.messaging.saaj.soap.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.namespace.QName;
import javax.xml.soap.*;

import org.w3c.dom.Element;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.name.NameImpl;

public abstract class DetailImpl extends FaultElementImpl implements Detail {
    public DetailImpl(SOAPDocumentImpl ownerDoc, NameImpl detailName) {
        super(ownerDoc, detailName);
    }

    protected abstract DetailEntry createDetailEntry(Name name);
    protected abstract DetailEntry createDetailEntry(QName name);

    public DetailEntry addDetailEntry(Name name) throws SOAPException {
        DetailEntry entry = createDetailEntry(name);
        addNode(entry);
        return (DetailEntry) circumventBug5034339(entry);
    }

    public DetailEntry addDetailEntry(QName qname) throws SOAPException {
        DetailEntry entry = createDetailEntry(qname);
        addNode(entry);
        return (DetailEntry) circumventBug5034339(entry);
    }

    protected SOAPElement addElement(Name name) throws SOAPException {
        return addDetailEntry(name);
    }

    protected SOAPElement addElement(QName name) throws SOAPException {
        return addDetailEntry(name);
    }

    protected SOAPElement convertToSoapElement(Element element) {
        if (element instanceof DetailEntry) {
            return (SOAPElement) element;
        } else {
            DetailEntry detailEntry =
                createDetailEntry(NameImpl.copyElementName(element));
            return replaceElementWithSOAPElement(
                element,
                (ElementImpl) detailEntry);
        }
    }

    public Iterator getDetailEntries() {
        return new Iterator() {
            Iterator eachNode = getChildElementNodes();
            SOAPElement next = null;
            SOAPElement last = null;

            public boolean hasNext() {
                if (next == null) {
                    while (eachNode.hasNext()) {
                        next = (SOAPElement) eachNode.next();
                        if (next instanceof DetailEntry) {
                            break;
                        }
                        next = null;
                    }
                }
                return next != null;
            }

            public Object next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                last = next;
                next = null;
                return last;
            }

            public void remove() {
                if (last == null) {
                    throw new IllegalStateException();
                }
                SOAPElement target = last;
                removeChild(target);
                last = null;
            }
        };
    }

   protected  boolean isStandardFaultElement() {
       return true;
   }

}
