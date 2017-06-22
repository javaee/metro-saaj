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

package com.sun.xml.messaging.saaj.util.stax;

import org.jvnet.staxex.Base64Data;
import org.jvnet.staxex.BinaryText;
import org.jvnet.staxex.XMLStreamReaderEx;
import org.jvnet.staxex.util.DOMStreamReader;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.xml.soap.SOAPElement;
import javax.xml.stream.XMLStreamException;
import java.util.Iterator;

/**
 * SaajStaxReaderEx 
 * 
 * @author shih-chang.chen@oracle.com
 */
public class SaajStaxReaderEx extends DOMStreamReader implements XMLStreamReaderEx {
    //TODO extends com.sun.xml.ws.streaming.DOMStreamReader
    private BinaryText binaryText = null;
    private Base64Data base64AttData = null;
    
    public SaajStaxReaderEx(SOAPElement se) {
        super(se);
    }
    
    @Override
    public int next() throws XMLStreamException {
        binaryText = null;
        base64AttData = null;
        while(true) {
            int r = _next();
            switch (r) {
            case CHARACTERS:
                if (_current instanceof BinaryText) {
                    binaryText = (BinaryText) _current;
                    base64AttData = new Base64Data();
                    base64AttData.set(binaryText.getDataHandler());
//System.out.println("--------------- debug SaajStaxReaderEx binaryText " + binaryText);
                } else {
                    // if we are currently at text node, make sure that this is a meaningful text node.
                    Node prev = _current.getPreviousSibling();
                    if(prev!=null && prev.getNodeType()==Node.TEXT_NODE)
                        continue;   // nope. this is just a continuation of previous text that should be invisible
    
                    Text t = (Text)_current;
                    wholeText = t.getWholeText();
                    if(wholeText.length()==0)
                        continue;   // nope. this is empty text.
                }
                return CHARACTERS;
            case START_ELEMENT:
                splitAttributes();
                return START_ELEMENT;
            default:
                return r;
            }
        }
    }

    @Override
    public String getElementTextTrim() throws XMLStreamException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CharSequence getPCDATA() throws XMLStreamException {
        return (binaryText != null) ? base64AttData : getText();
    }   

    @Override
    public org.jvnet.staxex.NamespaceContextEx getNamespaceContext() {        
        return new org.jvnet.staxex.NamespaceContextEx() {

            @Override
            public String getNamespaceURI(String prefix) {
                return _current.lookupNamespaceURI(prefix);
            }

            @Override
            public String getPrefix(String uri) {
                return _current.lookupPrefix(uri);
            }

            @Override
            public Iterator getPrefixes(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Iterator<Binding> iterator() {
                // TODO Auto-generated method stub
                return null;
            }
            
        };
    }


    @Override
    public int getTextLength() {
        return (binaryText != null) ? base64AttData.length() : super.getTextLength();
    }

    @Override
    public int getTextStart() {
        return (binaryText != null) ? 0: super.getTextStart();
    } 

    @Override
    public char[] getTextCharacters() {
        if (binaryText != null) {
            char[] chars = new char[base64AttData.length()];
            base64AttData.writeTo(chars, 0);
            return chars;
        }
        return super.getTextCharacters();
    }   
}
