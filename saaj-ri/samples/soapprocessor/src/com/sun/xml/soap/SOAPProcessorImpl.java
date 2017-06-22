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

import java.util.*;

import javax.xml.namespace.QName;
import javax.xml.soap.*;

public class SOAPProcessorImpl extends SOAPProcessor {

    private String ultimateReceiverURL =
        "http://schemas.xmlsoap.org/soap/actor/ultimateReceiver";

    //Limit access
    protected SOAPProcessorImpl() {
    }

    /**
	 * Implementation for SOAP 1.1
	 */
    public SOAPMessage acceptMessage(SOAPMessage message) throws Exception {

        SOAPHeader header = message.getSOAPHeader();
        
        checkMustUnderstand(header);

        ProcessingContext context = new ProcessingContext();
        context.setProperty(SOAPProcessorConstants.MESSAGE_PROPERTY, message);
        
        ProcessingStates currentState = null;
        Stack recipientsCalled = new Stack();
        
        Iterator eachHeaderElement = header.examineAllHeaderElements();
        while (eachHeaderElement.hasNext()) {
            SOAPHeaderElement element =
                (SOAPHeaderElement) eachHeaderElement.next();
            String role = getRoleAttributeValue(element);
            for (Iterator eachRecipient = recipients.iterator();
                eachRecipient.hasNext();
                ) {
                SOAPRecipient recipient = (SOAPRecipient) eachRecipient.next();
                if (recipient.supportsRole(role)) {
                    Name elementName = element.getElementName();
                    QName elementQName =
                        new QName(
                            elementName.getURI(),
                            elementName.getLocalName(),
                            elementName.getPrefix());
                    if (recipient.supportsHeader(elementQName)) {

                        recipientsCalled.push(recipient);
                        
                        recipient.acceptHeaderElement(element, context);
                        
                        currentState =
                            (ProcessingStates) context.getProperty(
                                SOAPProcessorConstants.STATE_PROPERTY);
                        if (currentState == ProcessingStates.STOP
                            || currentState == ProcessingStates.FAULT
                            || currentState == ProcessingStates.HEADER_DONE) {
                            
                            break;
                        }
                    }
                }
            }
            if (currentState == ProcessingStates.STOP) {
                break;
            }
            if (currentState == ProcessingStates.FAULT) {
                handleFault(context, recipientsCalled);
                break;
            }
        }
        message.saveChanges();
        return message;
    }

    private void checkMustUnderstand(SOAPHeader header) throws SOAPException {
        Iterator eachHeaderElement = header.examineAllHeaderElements();
        for (; eachHeaderElement.hasNext();) {
            SOAPHeaderElement headerElement =
                (SOAPHeaderElement) eachHeaderElement.next();
            Name headerName = headerElement.getElementName();
            QName headerQName =
                new QName(
                    headerName.getURI(),
                    headerName.getLocalName(),
                    headerName.getPrefix());
            String role = getRoleAttributeValue(headerElement);
            boolean canBeProcessed = false;
            boolean isTargeted = false;
            for (Iterator eachRecipient = recipients.iterator();
                eachRecipient.hasNext();
                ) {
                SOAPRecipient recipient = (SOAPRecipient) eachRecipient.next();
                if (recipient.supportsRole(role)) {
                    isTargeted = true;
                    if (recipient.supportsHeader(headerQName)) {
                        canBeProcessed = true;
                    }
                }
            }
            if (!canBeProcessed
                && isTargeted
                && headerElement.getMustUnderstand()) {

                throw new SOAPException(
                    "MustUnderstand failure. Header = "
                        + headerQName
                        + " Role = "
                        + role);
            }
        }
    }

    public SOAPMessage prepareMessage(SOAPMessage message) throws Exception {

        SOAPHeader header = message.getSOAPHeader();
        ProcessingContext context = new ProcessingContext();
        context.setProperty(SOAPProcessorConstants.MESSAGE_PROPERTY, message);
        ProcessingStates currentState = null;
        Stack annotatorsCalled = new Stack();
        for (Iterator it = annotators.iterator(); it.hasNext();) {
            SOAPAnnotator annotator = (SOAPAnnotator) it.next();
            annotatorsCalled.push(annotator);
            annotator.annotateHeader(header, context);
            currentState =
                (ProcessingStates) context.getProperty(
                    SOAPProcessorConstants.STATE_PROPERTY);
            if (currentState == ProcessingStates.STOP
                || currentState == ProcessingStates.FAULT) {
                break;
            }
        }
        if (currentState == ProcessingStates.FAULT) {
            handleFault(context, annotatorsCalled);
        }
        message.saveChanges();
        return message;
    }

    private void handleFault(
        ProcessingContext context,
        Stack recipientsCalled) {
        while (true) {
            ProcessingFaultHandler faultHandler =
                (ProcessingFaultHandler) recipientsCalled.pop();
            if (faultHandler == null) {
                break;
            }
            faultHandler.handleIncomingFault(context);
        }
    }

    /**
	 * Implementation for SOAP 1.1
	 */
    protected String getRoleAttributeValue(SOAPHeaderElement element) {

        String ret = element.getActor();
        if (ret == null)
            ret = ultimateReceiverURL;
        return ret;
    }

}
