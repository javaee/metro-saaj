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
 * $Id: ParserPool.java,v 1.1.1.1 2006-01-27 13:10:58 kumarjayanti Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2006-01-27 13:10:58 $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.xml.messaging.saaj.util;

import java.util.EmptyStackException;
import java.util.Stack;

import javax.xml.parsers.*;

import org.xml.sax.SAXException;

/**
 * Pool of SAXParser objects
 */
public class ParserPool {
    private Stack parsers;
    private SAXParserFactory factory;
    private int capacity;

    public ParserPool(int capacity) {
		this.capacity = capacity;
        factory = new com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl(); //SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        parsers = new Stack();
    }

    public synchronized SAXParser get() throws ParserConfigurationException,
		SAXException {

        try {
            return (SAXParser) parsers.pop();
        } catch (EmptyStackException e) {
            return factory.newSAXParser();
        }
    }

    public synchronized void put(SAXParser parser) {
        if (parsers.size() < capacity) {
            parsers.push(parser);
        }
    }

}
