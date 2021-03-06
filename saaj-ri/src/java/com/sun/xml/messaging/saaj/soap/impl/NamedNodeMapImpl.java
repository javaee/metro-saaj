/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
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

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.Objects;

/**
 * {@link NamedNodeMap} wrapper, finding SOAP elements automatically when possible.
 *
 * @author Roman Grigoriadi
 */
public class NamedNodeMapImpl implements NamedNodeMap {

    private final NamedNodeMap namedNodeMap;

    private final SOAPDocumentImpl soapDocument;

    /**
     * Create wrapper.
     *
     * @param namedNodeMap node map to wrap
     * @param soapDocument soap document to find soap elements
     */
    public NamedNodeMapImpl(NamedNodeMap namedNodeMap, SOAPDocumentImpl soapDocument) {
        Objects.requireNonNull(namedNodeMap);
        Objects.requireNonNull(soapDocument);
        this.namedNodeMap = namedNodeMap;
        this.soapDocument = soapDocument;
    }

    @Override
    public Node getNamedItem(String name) {
        return soapDocument.findIfPresent(namedNodeMap.getNamedItem(name));
    }

    @Override
    public Node setNamedItem(Node arg) throws DOMException {
        return namedNodeMap.setNamedItem(arg);
    }

    @Override
    public Node removeNamedItem(String name) throws DOMException {
        return namedNodeMap.removeNamedItem(name);
    }

    @Override
    public Node item(int index) {
        return namedNodeMap.item(index);
    }

    @Override
    public int getLength() {
        return namedNodeMap.getLength();
    }

    @Override
    public Node getNamedItemNS(String namespaceURI, String localName) throws DOMException {
        return namedNodeMap.getNamedItemNS(namespaceURI, localName);
    }

    @Override
    public Node setNamedItemNS(Node arg) throws DOMException {
        return namedNodeMap.setNamedItemNS(arg);
    }

    @Override
    public Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException {
        return namedNodeMap.removeNamedItemNS(namespaceURI, localName);
    }
}
