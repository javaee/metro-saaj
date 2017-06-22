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

    public DetailImpl(SOAPDocumentImpl ownerDoc, Element domElement) {
        super(ownerDoc, domElement);
    }

    protected abstract DetailEntry createDetailEntry(Name name);
    protected abstract DetailEntry createDetailEntry(QName name);

    @Override
    public DetailEntry addDetailEntry(Name name) throws SOAPException {
        DetailEntry entry = createDetailEntry(name);
        addNode(entry);
        return entry;
    }

    @Override
    public DetailEntry addDetailEntry(QName qname) throws SOAPException {
        DetailEntry entry = createDetailEntry(qname);
        addNode(entry);
        return entry;
    }

    @Override
    protected SOAPElement addElement(Name name) throws SOAPException {
        return addDetailEntry(name);
    }

    @Override
    protected SOAPElement addElement(QName name) throws SOAPException {
        return addDetailEntry(name);
    }

    @Override
    protected SOAPElement convertToSoapElement(Element element) {
        final javax.xml.soap.Node soapNode = getSoapDocument().find(element);
        if (soapNode instanceof DetailEntry) {
            return (SOAPElement) soapNode;
        } else {
            DetailEntry detailEntry =
                createDetailEntry(NameImpl.copyElementName(element));
            return replaceElementWithSOAPElement(
                element,
                (ElementImpl) detailEntry);
        }
    }

    @Override
    public Iterator<DetailEntry> getDetailEntries() {
        return new Iterator<DetailEntry>() {
            Iterator<org.w3c.dom.Node> eachNode = getChildElementNodes();
            SOAPElement next = null;
            SOAPElement last = null;

            @Override
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

            @Override
            public DetailEntry next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                last = next;
                next = null;
                return (DetailEntry) last;
            }

            @Override
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

    @Override
   protected  boolean isStandardFaultElement() {
       return true;
   }

}
