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

package com.sun.xml.soap;

import java.util.Vector;
import javax.xml.soap.*;

public abstract class SOAPProcessor {

    protected Vector recipients = new Vector();
    protected Vector annotators = new Vector();

    /**
	 * adds a <code>SOAPRecipient</code> to this <code>SOAPProcessor</code>
	 * 
	 * @param recipient
	 *            The <code>SOAPRecipient</code> to be added
	 */
    public void addRecipient(SOAPRecipient recipient) {
        recipients.addElement(recipient);
    }

    /**
	 * adds a <code>SOAPAnnotator</code> to this <code>SOAPProcessor</code>
	 * 
	 * @param annotator
	 *            The <code>SOAPAnnotator</code> to be added
	 */
    public void addAnnotator(SOAPAnnotator annotator) {
        annotators.addElement(annotator);
    }

    /**
	 * Processes an incoming <code>SOAPMessage</code> according to the SOAP
	 * Processing model given in the appropriate SOAP specification. The
	 * message is passed through one or more <code>SOAPRecipient</code> s
	 * 
	 * @param message
	 *            the <code>SOAPMessage</code> to be processed
	 * 
	 * @return the <code>SOAPMessage</code> after processing //todo
	 * @exception SOAPException
	 *                if there is an error accepting the message
	 */

    public abstract SOAPMessage acceptMessage(SOAPMessage message)
        throws Exception;

    /**
	 * Processes an outgoing <code>SOAPMessage</code> through one or more
	 * <code>SOAPAnnotator</code>s.
	 * 
	 * @param message
	 *            the <code>SOAPMessage</code> to be processed
	 * 
	 * @return the <code>SOAPMessage</code> after processing //todo
	 * @exception SOAPException
	 *                if there is an error while preparing the message
	 */
    public abstract SOAPMessage prepareMessage(SOAPMessage message)
        throws Exception;

    protected abstract String getRoleAttributeValue(SOAPHeaderElement element);
}
