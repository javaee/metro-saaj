/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
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

    //overriding this method since the only two uses of this method 
    // are in ElementImpl and DetailImpl
    //whereas the original base impl does the correct job for calls to it inside ElementImpl
    // But it would not work for DetailImpl.
    protected SOAPElement circumventBug5034339(SOAPElement element) {

        Name elementName = element.getElementName();
        if (!isNamespaceQualified(elementName)) {
            String prefix = elementName.getPrefix();
            String defaultNamespace = getNamespaceURI(prefix);
            if (defaultNamespace != null) {
                Name newElementName =
                    NameImpl.create(
                        elementName.getLocalName(),
                        elementName.getPrefix(),
                        defaultNamespace);
                SOAPElement newElement = createDetailEntry(newElementName);
                replaceChild(newElement, element);
                return newElement;
            }
        }
        return element;
    }
   
}
