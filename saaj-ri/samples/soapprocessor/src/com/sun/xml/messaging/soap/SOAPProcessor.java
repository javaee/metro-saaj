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

import java.util.Vector;
import javax.xml.soap.*;

public abstract class SOAPProcessor {

    protected Vector recipients = new Vector();
    protected Vector annotators = new Vector();

    /**
     * adds a <code>SOAPRecipient</code> to this <code>SOAPProcessor</code>
     *
     * @param recipient The <code>SOAPRecipient</code> to be added
     */
    public void addRecipient(SOAPRecipient recipient) {
        recipients.addElement(recipient);
    }

    /**
     * adds a <code>SOAPAnnotator</code> to this <code>SOAPProcessor</code>
     *
     * @param annotator The <code>SOAPAnnotator</code> to be added
     */
    public void addAnnotator(SOAPAnnotator annotator) {
        annotators.addElement(annotator);
    }

    /**
     * Processes an incoming <code>SOAPMessage</code> according to 
     * the SOAP Processing model given in the appropriate SOAP specification.
     * The message is passed through one or more <code>SOAPRecipient</code>s
     *
     * @param message the <code>SOAPMessage</code> to be processed
     *
     * @return the <code>SOAPMessage</code> after processing
     * 
     * @exception SOAPException if there is an error accepting the message
     */

    public abstract SOAPMessage acceptMessage(SOAPMessage message) 
        throws SOAPException;


    /**
     * Processes an outgoing <code>SOAPMessage</code> through one or more 
     * <code>SOAPAnnotator</code>s.
     *
     * @param message the <code>SOAPMessage</code> to be processed
     *
     * @return the <code>SOAPMessage</code> after processing
     * 
     * @exception SOAPException if there is an error while preparing 
     * the message
     */
    public abstract SOAPMessage prepareMessage(SOAPMessage message)
        throws SOAPException;

    protected abstract String getRoleAttributeValue(
        SOAPHeaderElement element);
}
