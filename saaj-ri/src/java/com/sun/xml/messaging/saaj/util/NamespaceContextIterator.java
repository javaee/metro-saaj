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
 * $Id: NamespaceContextIterator.java,v 1.1.1.1 2006-01-27 13:10:58 kumarjayanti Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/**
*
* @author SAAJ RI Development Team
*/
package com.sun.xml.messaging.saaj.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.*;

public class NamespaceContextIterator implements Iterator {
    Node context;
    NamedNodeMap attributes = null;
    int attributesLength;
    int attributeIndex;
    Attr next = null;
    Attr last = null;
    boolean traverseStack = true;

    public NamespaceContextIterator(Node context) {
        this.context = context;
        findContextAttributes();
    }

    public NamespaceContextIterator(Node context, boolean traverseStack) {
        this(context);
        this.traverseStack = traverseStack;
    }

    protected void findContextAttributes() {
        while (context != null) {
            int type = context.getNodeType();
            if (type == Node.ELEMENT_NODE) {
                attributes = context.getAttributes();
                attributesLength = attributes.getLength();
                attributeIndex = 0;
                return;
            } else {
                context = null;
            }
        }
    }

    protected void findNext() {
        while (next == null && context != null) {
            for (; attributeIndex < attributesLength; ++attributeIndex) {
                Node currentAttribute = attributes.item(attributeIndex);
                String attributeName = currentAttribute.getNodeName();
                if (attributeName.startsWith("xmlns")
                    && (attributeName.length() == 5
                        || attributeName.charAt(5) == ':')) {
                    next = (Attr) currentAttribute;
                    ++attributeIndex;
                    return;
                }
            }
            if (traverseStack) {
                context = context.getParentNode();
                findContextAttributes();
            } else {
                context = null;
            }
        }
    }

    public boolean hasNext() {
        findNext();
        return next != null;
    }

    public Object next() {
        return getNext();
    }
    
    public Attr nextNamespaceAttr() {
        return getNext();
    }

    protected Attr getNext() {
        findNext();
        if (next == null) {
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
        ((Element) context).removeAttributeNode(last);
    }

}
