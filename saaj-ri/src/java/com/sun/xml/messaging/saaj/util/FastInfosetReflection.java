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
 * $Id: FastInfosetReflection.java,v 1.3 2006-06-16 11:39:58 sandoz Exp $
 * $Revision: 1.3 $
 * $Date: 2006-06-16 11:39:58 $
 */

/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.xml.messaging.saaj.util;

import java.lang.reflect.*;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import java.io.InputStream;
import java.io.OutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author Santiago.PericasGeertsen@sun.com
 * @author Paul.Sandoz@sun.com
 */
public class FastInfosetReflection {
    
    /**
     * FI DOMDocumentParser constructor using reflection.
     */
    static Constructor fiDOMDocumentParser_new;
    
    /**
     * FI <code>DOMDocumentParser.parse()</code> method via reflection.
     */
    static Method fiDOMDocumentParser_parse;
    
    /**
     * FI DOMDocumentSerializer constructor using reflection.
     */
    static Constructor fiDOMDocumentSerializer_new;
    
    /**
     * FI <code>FastInfosetSource.serialize(Document)</code> method via reflection.
     */
    static Method fiDOMDocumentSerializer_serialize;
    
    /**
     * FI <code>FastInfosetSource.setOutputStream(OutputStream)</code> method via reflection.
     */
    static Method fiDOMDocumentSerializer_setOutputStream;
    
    /**
     * FI FastInfosetSource constructor using reflection.
     */
    static Class fiFastInfosetSource_class;
    
    /**
     * FI FastInfosetSource constructor using reflection.
     */
    static Constructor fiFastInfosetSource_new;
    
    /**
     * FI <code>FastInfosetSource.getInputStream()</code> method via reflection.
     */
    static Method fiFastInfosetSource_getInputStream;
       
    /**
     * FI <code>FastInfosetSource.setInputSTream()</code> method via reflection.
     */
    static Method fiFastInfosetSource_setInputStream;
    
    /**
     * FI FastInfosetResult constructor using reflection.
     */
    static Constructor fiFastInfosetResult_new;
    
    /**
     * FI <code>FastInfosetResult.getOutputSTream()</code> method via reflection.
     */
    static Method fiFastInfosetResult_getOutputStream;
    
    static {
        try {
            Class clazz = null;
            clazz =
                Class.forName("com.sun.xml.fastinfoset.dom.DOMDocumentParser");
            fiDOMDocumentParser_new = clazz.getConstructor(null);
            fiDOMDocumentParser_parse = clazz.getMethod("parse", 
                new Class[] { org.w3c.dom.Document.class, java.io.InputStream.class });
            
            clazz = Class.forName("com.sun.xml.fastinfoset.dom.DOMDocumentSerializer");
            fiDOMDocumentSerializer_new = clazz.getConstructor(null);
            fiDOMDocumentSerializer_serialize = clazz.getMethod("serialize", 
                new Class[] { org.w3c.dom.Node.class });
            fiDOMDocumentSerializer_setOutputStream = clazz.getMethod("setOutputStream",
                new Class[] { java.io.OutputStream.class });
            
            fiFastInfosetSource_class = clazz = Class.forName("org.jvnet.fastinfoset.FastInfosetSource");
            fiFastInfosetSource_new = clazz.getConstructor(
                new Class[] { java.io.InputStream.class });
            fiFastInfosetSource_getInputStream = clazz.getMethod("getInputStream", null);          
            fiFastInfosetSource_setInputStream = clazz.getMethod("setInputStream", 
                new Class[] { java.io.InputStream.class });          
            
            clazz = Class.forName("org.jvnet.fastinfoset.FastInfosetResult");
            fiFastInfosetResult_new = clazz.getConstructor(
                new Class[] { java.io.OutputStream.class });
            fiFastInfosetResult_getOutputStream = clazz.getMethod("getOutputStream", null);           
        } 
        catch (Exception e) {
            // falls through
        }
    }

    // -- DOMDocumentParser ----------------------------------------------

    public static Object DOMDocumentParser_new() throws Exception {
        if (fiDOMDocumentParser_new == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        return fiDOMDocumentParser_new.newInstance(null);
    }
    
    public static void DOMDocumentParser_parse(Object parser, 
        Document d, InputStream s) throws Exception 
    {
        if (fiDOMDocumentParser_parse == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        fiDOMDocumentParser_parse.invoke(parser, new Object[] { d, s });
    }
    
    // -- DOMDocumentSerializer-------------------------------------------
    
    public static Object DOMDocumentSerializer_new() throws Exception {
        if (fiDOMDocumentSerializer_new == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        return fiDOMDocumentSerializer_new.newInstance(null);
    }
    
    public static void DOMDocumentSerializer_serialize(Object serializer, Node node)
        throws Exception
    {
        if (fiDOMDocumentSerializer_serialize == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        fiDOMDocumentSerializer_serialize.invoke(serializer, new Object[] { node });
    }
    
    public static void DOMDocumentSerializer_setOutputStream(Object serializer,
        OutputStream os) throws Exception
    {
        if (fiDOMDocumentSerializer_setOutputStream == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        fiDOMDocumentSerializer_setOutputStream.invoke(serializer, new Object[] { os });
    }
    
    // -- FastInfosetSource ----------------------------------------------
    
    public static boolean isFastInfosetSource(Source source) {
        return source.getClass().getName().equals(
            "org.jvnet.fastinfoset.FastInfosetSource");
    }

    public static Class getFastInfosetSource_class() {
        if (fiFastInfosetSource_class == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        
        return fiFastInfosetSource_class;
    }
    public static Source FastInfosetSource_new(InputStream is) 
        throws Exception 
    {
        if (fiFastInfosetSource_new == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        return (Source) fiFastInfosetSource_new.newInstance(new Object[] { is });        
    }
    
    public static InputStream FastInfosetSource_getInputStream(Source source) 
        throws Exception 
    {
        if (fiFastInfosetSource_getInputStream == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        return (InputStream) fiFastInfosetSource_getInputStream.invoke(source, null);
    }
    
    public static void FastInfosetSource_setInputStream(Source source,
        InputStream is) throws Exception
    {
        if (fiFastInfosetSource_setInputStream == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        fiFastInfosetSource_setInputStream.invoke(source, new Object[] { is });
    }
    
    // -- FastInfosetResult ----------------------------------------------
    
    public static boolean isFastInfosetResult(Result result) {
        return result.getClass().getName().equals(
            "org.jvnet.fastinfoset.FastInfosetResult");        
    }
    
    public static Result FastInfosetResult_new(OutputStream os) 
        throws Exception 
    {
        if (fiFastInfosetResult_new == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        return (Result) fiFastInfosetResult_new.newInstance(new Object[] { os });                
    }
    
    public static OutputStream FastInfosetResult_getOutputStream(Result result) 
        throws Exception 
    {
        if (fiFastInfosetResult_getOutputStream == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        return (OutputStream) fiFastInfosetResult_getOutputStream.invoke(result, null);
    }
}
