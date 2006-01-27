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
