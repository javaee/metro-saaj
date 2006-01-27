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
 * $Id: LogDomainConstants.java,v 1.1.1.1 2006-01-27 13:10:58 kumarjayanti Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2006-01-27 13:10:58 $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.xml.messaging.saaj.util;

/**
 * @author  Manveen Kaur (manveen.kaur@eng.sun.com)
 */

/**
 * This interface defines a number of constants pertaining to Logging domains.
 */

public interface LogDomainConstants {

    // TBD -- this should be configurable from a properties
    // file or something .. Leaving it as it is for now.     
    public static String MODULE_TOPLEVEL_DOMAIN = 
                    "javax.xml.messaging.saaj";

    // First Level Domain 
    public static String CLIENT_DOMAIN = 
                MODULE_TOPLEVEL_DOMAIN + ".client";
    
    public static String SOAP_DOMAIN = 
                MODULE_TOPLEVEL_DOMAIN + ".soap";

    public static String UTIL_DOMAIN = 
                MODULE_TOPLEVEL_DOMAIN + ".util";

    // Second Level Domain
    public static String HTTP_CONN_DOMAIN = 
                  CLIENT_DOMAIN + ".p2p";
    
    public static String NAMING_DOMAIN =  
                SOAP_DOMAIN + ".name";

    public static String SOAP_IMPL_DOMAIN = 
                  SOAP_DOMAIN + ".impl";

    public static String SOAP_VER1_1_DOMAIN = 
                  SOAP_DOMAIN + ".ver1_1";
    
    public static String SOAP_VER1_2_DOMAIN = 
                  SOAP_DOMAIN + ".ver1_2";

}
