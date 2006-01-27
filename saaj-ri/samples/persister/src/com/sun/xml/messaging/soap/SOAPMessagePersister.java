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

package com.sun.xml.messaging.soap;

import java.util.*;

import java.io.*;

import javax.xml.soap.*;
import javax.xml.transform.stream.StreamSource;

/** 
 * This class provides a method <code>save</code> for saving a
 * <code>SOAPMessage</code> to a file and a method <code>load</code> for
 * loading a saved <code>SOAPMessage</code> from a file (in which the
 * <code>SOAPMessage</code> was saved using the <code>save</code> method of
 * this class).
 * The format of the saved <code>SOAPMessage</code> is human readable.
 */
public class SOAPMessagePersister {

    /**
     * Saves a given <code>SOAPMessage</code> to a given file location in a
     * human readable format.
     */
    public void save(SOAPMessage msg, String location)
        throws IOException, SOAPException {

        FileWriter writer = new FileWriter(location);

        // Write all the message Mime headers
        msg.saveChanges();
        Iterator iterator = msg.getMimeHeaders().getAllHeaders();
        while(iterator.hasNext()) {
            MimeHeader mimeHeader = (MimeHeader) iterator.next();
            writer.write(mimeHeader.getName() + ": " +
                         mimeHeader.getValue() + "\n");
        }
        writer.write("\n");

        // If there are no attachments
        if(msg.countAttachments() == 0) {

            // Write a boundary to mark the beginning of the soap-part
            writer.write("=--soap-part--=\n");

            // Write the Mime headers of the soap-part
            Iterator it = msg.getSOAPPart().getAllMimeHeaders();
            while(it.hasNext()) {
                MimeHeader mimeHeader = (MimeHeader) it.next();
                writer.write(mimeHeader.getName() + ": " +
                         mimeHeader.getValue() + "\n");
            }    
            writer.write("\n");
        }

        // Do a writeTo() of the message
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        msg.writeTo(baos);
        writer.write(baos.toString());

        // If there were no attachments write the boundary to mark the end of
        // the soap-part
        if(msg.countAttachments() == 0)
            writer.write("\n=--soap-part--=\n");

        writer.flush();
        writer.close();
    }

    /**
     * Loads a <code>SOAPMessage</code> from a given file location.
     * The message in the given file location should have been stored using
     * the <code>save</code> method of this class.
     */
    public SOAPMessage load(String location)
        throws IOException, SOAPException {
      
        FileReader reader = new FileReader(location);

        // Read the Mime headers of the message
        MimeHeaders hdrs = new MimeHeaders();
        String headerLine = readLine(reader);
        while(!headerLine.equals("")) {

            int colonIndex = headerLine.indexOf(':');
            String headerName = headerLine.substring(0, colonIndex);
            String headerValue = headerLine.substring(colonIndex + 1);
            hdrs.addHeader(headerName, headerValue);
            headerLine = readLine(reader);
        }

        // If there are no attachments
        if(readLine(reader).equals("=--soap-part--=")) {

            // Read the Mime headers of soap-part
            MimeHeaders soapPartHdrs = new MimeHeaders();
            headerLine = readLine(reader);
            while(!headerLine.equals("")) {
                int colonIndex = headerLine.indexOf(':');
                String headerName = headerLine.substring(0, colonIndex);
                String headerValue = headerLine.substring(colonIndex + 1);
                soapPartHdrs.addHeader(headerName, headerValue);
                headerLine = readLine(reader);
            }

            // Read the content of the soap-part in a StringBuffer
            StringBuffer soapPartBuffer = new StringBuffer();
            String soapPartLine = readLine(reader);
            while(!soapPartLine.equals("=--soap-part--=")) {
                soapPartBuffer.append(soapPartLine);
                soapPartBuffer.append('\n');
                soapPartLine = readLine(reader);
            }
            soapPartBuffer.deleteCharAt(soapPartBuffer.length() - 1);

            // Create a new SOAPMessage
            SOAPMessage msg = MessageFactory.newInstance().createMessage(
                hdrs, new StringBufferInputStream(soapPartBuffer.toString()));

            // Set the Mime headers of its soap-part
            SOAPPart soapPart = msg.getSOAPPart();
            soapPart.removeAllMimeHeaders();
            for(Iterator it = soapPartHdrs.getAllHeaders(); it.hasNext(); ) {
                MimeHeader hdr = (MimeHeader) it.next();
                soapPart.addMimeHeader(hdr.getName(), hdr.getValue());
            }

            msg.saveChanges();

            reader.close();

            // Return message
            return msg;
        }

        reader.close();

        // When there are no attachments, pass the FileInputStream of the
        // saved message to CreateMessage method. This works because the parser
        // ignores everything before the first part boundary
        return MessageFactory.newInstance().createMessage(
            hdrs, new FileInputStream(location));
    }

    private String readLine(FileReader reader) throws IOException {

        StringBuffer buffer = new StringBuffer();
        char ch = (char) reader.read();
        while(ch != '\n') {
            buffer.append(ch);
            ch = (char) reader.read();
        }
        return buffer.toString();      
    }
}
