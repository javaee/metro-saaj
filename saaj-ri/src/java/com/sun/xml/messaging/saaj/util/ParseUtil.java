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
 * $Id: ParseUtil.java,v 1.1.1.1 2006-01-27 13:10:58 kumarjayanti Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2006-01-27 13:10:58 $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sun.xml.messaging.saaj.util;


// Cut&paste from sun.net.www.ParseUtil: decode, unescape

public class ParseUtil {
    /**
     * Un-escape and return the character at position i in string s.
     */
    private static char unescape(String s, int i) {
        return (char) Integer.parseInt(s.substring(i+1,i+3),16);
    }

    /**
     * Returns a new String constructed from the specified String by replacing
     * the URL escape sequences and UTF8 encoding with the characters they 
     * represent.
     */
    public static String decode(String s) {
        StringBuffer sb = new StringBuffer();

        int i=0;
        while (i<s.length()) {
            char c = s.charAt(i);
            char c2, c3;

            if (c != '%') {
                i++;
            } else {
                try {
                    c = unescape(s, i);
                    i += 3;

                    if ((c & 0x80) != 0) {
                        switch (c >> 4) {
                            case 0xC: case 0xD:
                                c2 = unescape(s, i);
                                i += 3;
                                c = (char)(((c & 0x1f) << 6) | (c2 & 0x3f));
                                break;

                            case 0xE:
                                c2 = unescape(s, i);
                                i += 3;
                                c3 = unescape(s, i);
                                i += 3;
                                c = (char)(((c & 0x0f) << 12) |
                                           ((c2 & 0x3f) << 6) |
                                            (c3 & 0x3f));
                                break;

                            default:
                                throw new IllegalArgumentException();
                        }
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException();
                }
            }

            sb.append(c);
        }

        return sb.toString();
    }


}
