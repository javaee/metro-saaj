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

package com.sun.xml.messaging.saaj.util;


import org.xml.sax.SAXException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;


/**
 * Pool of SAXParser objects
 */
public class ParserPool {
    private final BlockingQueue<SAXParser> queue;
    private SAXParserFactory factory;

    public ParserPool(int capacity) {
        queue = new ArrayBlockingQueue<SAXParser>(capacity);
        factory = SAXParserFactory.newInstance("com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl", SAAJUtil.getSystemClassLoader());
        try {
            factory.setFeature("jdk.xml.resetSymbolTable", true);
        } catch(SAXException | ParserConfigurationException e) {
        }
        factory.setNamespaceAware(true);
        for (int i = 0; i < capacity; i++) {
           try {
                queue.put(factory.newSAXParser());
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(ex);
            } catch (ParserConfigurationException ex) {
                throw new RuntimeException(ex);
            } catch (SAXException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public SAXParser get() throws ParserConfigurationException,
		SAXException {

        try {
            return (SAXParser) queue.take();
        } catch (InterruptedException ex) {
            throw new SAXException(ex);
        }

    }

    public boolean put(SAXParser parser) {
        return queue.offer(parser);
    }
    
    public void returnParser(SAXParser saxParser) {
        saxParser.reset();
        put(saxParser);
    }

}
