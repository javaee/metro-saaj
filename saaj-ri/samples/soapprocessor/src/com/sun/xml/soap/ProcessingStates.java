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
 * $Id: ProcessingStates.java,v 1.1.1.1 2006-01-27 13:11:13 kumarjayanti Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.xml.soap;

/**
*
* @author SAAJ Development Team
*/
public class ProcessingStates {
    public static final ProcessingStates CONTINUE = new ProcessingStates();
    public static final ProcessingStates STOP = new ProcessingStates();
    public static final ProcessingStates HEADER_DONE = new ProcessingStates();
    public static final ProcessingStates FAULT = new ProcessingStates();
    
    private ProcessingStates() {
    }
}
