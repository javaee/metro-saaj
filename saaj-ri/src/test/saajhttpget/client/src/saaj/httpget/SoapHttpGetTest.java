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
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package saaj.httpget;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.xml.soap.*;

import java.net.*;
import java.io.*;
import java.util.*;

import javax.servlet.http.*;
import javax.servlet.*;

public class SoapHttpGetTest {
    
    public static void main(String args[]) throws Exception {

        try {

            // Create a SOAP connection
            SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
            SOAPConnection con = scf.createConnection();

            String to = System.getProperty("to");
            System.out.println("Sending resuest to : " + to);
	
	    SOAPMessage reply = con.get(new URL(to));
            if (!(reply.getSOAPPart().getEnvelope() instanceof 
                 com.sun.xml.messaging.saaj.soap.ver1_1.Envelope1_1Impl)) {
                 throw new Exception("expected a 1.1 message");
            }

            try {
	        reply = con.get(new URL(to));
                throw new Exception("Should throw an error");
            } catch (SOAPException e) {

            }

	    reply = con.get(new URL(to));
            if (!(reply.getSOAPPart().getEnvelope() instanceof 
                 com.sun.xml.messaging.saaj.soap.ver1_2.Envelope1_2Impl)) {
                 throw new Exception("expected a 1.2 message");
            }
            SOAPEnvelope env = reply.getSOAPPart().getEnvelope();

	} catch(Exception e) {
            e.printStackTrace();
	}
    }
}
