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

package com.sun.xml.messaging.saaj.util;

import java.io.*;

import javax.xml.transform.TransformerException;

/* 
 * Class that parses the very first construct in the document i.e.
 *  <?xml ... ?>
 *
 * @author Panos Kougiouris (panos@acm.org)
 * @version  
 */

public class XMLDeclarationParser {
    private String m_encoding;
    private PushbackReader m_pushbackReader;
    private boolean m_hasHeader; // preserve the case where no XML Header exists
    private String xmlDecl = null;
    static String gt16 = null;
    static String utf16Decl = null;
    static int maxDeclPrefixLength;
    static {
         try {
             gt16 = new String(">".getBytes("utf-16"));
             utf16Decl = new String("<?xml".getBytes("utf-16"));
             maxDeclPrefixLength = utf16Decl.length();
         } catch (Exception e) {}
    }

    //---------------------------------------------------------------------

    public XMLDeclarationParser(PushbackReader pr)
    {
        m_pushbackReader = pr;
        m_encoding = "utf-8";
        m_hasHeader = false;
    }

    //---------------------------------------------------------------------
    public String getEncoding()
    {
        return m_encoding;
    }

    public String getXmlDeclaration() {
        return xmlDecl;
    }

    //---------------------------------------------------------------------

     public void parse()  throws TransformerException, IOException
     {
        int c = 0;
        int index = 0;
        boolean utf16 = false;
        boolean utf8 = false;
        int xmlIndex = -1;
        StringBuilder xmlDeclStr = new StringBuilder();
        while ((c = m_pushbackReader.read()) != -1) {
            xmlDeclStr.append((char)c);
            index++;
            if (index == maxDeclPrefixLength) {
               xmlIndex = xmlDeclStr.indexOf(utf16Decl);
               if (xmlIndex > -1) {
                   utf16 = true;
               } else {
                   xmlIndex = xmlDeclStr.indexOf("<?xml");
                   if (xmlIndex > -1) {
                       utf8 = true;
                   }
               }
               // no XML decl
               if (!utf16 && !utf8) {
                   int len = index;
                   m_pushbackReader.unread(xmlDeclStr.toString().toCharArray(), 0, len);
                   return;
               }
           }
            if (c == '>') {
                break;
            }
        }
        int len = index;

        String decl = xmlDeclStr.toString();
        if (len < maxDeclPrefixLength) {
            xmlIndex = xmlDeclStr.indexOf("<?xml");
            if (xmlIndex == -1) {
                m_pushbackReader.unread(decl.toCharArray(), 0, len);
                return;
            } else {
                utf8 = true;
            }
        }

        m_hasHeader = true;
        
        if (utf16) {
            xmlDecl = new String(decl.getBytes(), "utf-16");
            xmlDecl = xmlDecl.substring(xmlDecl.indexOf("<"));
        } else {
            xmlDecl = decl;
        }
        // do we want to check that there are no other characters preceeding <?xml
        if (xmlIndex != 0) {
            throw new IOException("Unexpected characters before XML declaration");
        }

        int versionIndex =  xmlDecl.indexOf("version");
        if (versionIndex == -1) {
            throw new IOException("Mandatory 'version' attribute Missing in XML declaration");
        }

        // now set
        int encodingIndex = xmlDecl.indexOf("encoding");
        if (encodingIndex == -1) {
            return;
        }

        if (versionIndex > encodingIndex) {
            throw new IOException("The 'version' attribute should preceed the 'encoding' attribute in an XML Declaration");
        }

        int stdAloneIndex = xmlDecl.indexOf("standalone");
        if ((stdAloneIndex > -1) && ((stdAloneIndex < versionIndex) || (stdAloneIndex < encodingIndex))) {
            throw new IOException("The 'standalone' attribute should be the last attribute in an XML Declaration");
        }

        int eqIndex = xmlDecl.indexOf("=", encodingIndex);
        if (eqIndex == -1) {
            throw new IOException("Missing '=' character after 'encoding' in XML declaration");
        }

        m_encoding = parseEncoding(xmlDecl, eqIndex);
        if(m_encoding.startsWith("\"")){
            m_encoding = m_encoding.substring(m_encoding.indexOf("\"")+1, m_encoding.lastIndexOf("\""));
        } else if(m_encoding.startsWith("\'")){
            m_encoding = m_encoding.substring(m_encoding.indexOf("\'")+1, m_encoding.lastIndexOf("\'"));
        }
     }

     //--------------------------------------------------------------------

    public void writeTo(Writer wr) throws IOException {
        if (!m_hasHeader) return;
        wr.write(xmlDecl.toString());
    }

    private String parseEncoding(String xmlDeclFinal, int eqIndex) throws IOException {
        java.util.StringTokenizer strTok = new java.util.StringTokenizer(
            xmlDeclFinal.substring(eqIndex + 1));
        if (strTok.hasMoreTokens()) {
            String encodingTok = strTok.nextToken();
            int indexofQ = encodingTok.indexOf("?");
            if (indexofQ > -1) {
                return encodingTok.substring(0,indexofQ);
            } else {
                return encodingTok;
            }
        } else {
            throw new IOException("Error parsing 'encoding' attribute in XML declaration");
        }
    }

}
    
