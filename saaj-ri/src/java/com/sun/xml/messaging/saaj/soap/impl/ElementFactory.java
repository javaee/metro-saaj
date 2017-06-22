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

package com.sun.xml.messaging.saaj.soap.impl;

import javax.xml.namespace.QName;
import javax.xml.soap.*;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.messaging.saaj.soap.ver1_1.Body1_1Impl;
import com.sun.xml.messaging.saaj.soap.ver1_1.Detail1_1Impl;
import com.sun.xml.messaging.saaj.soap.ver1_1.Envelope1_1Impl;
import com.sun.xml.messaging.saaj.soap.ver1_1.Fault1_1Impl;
import com.sun.xml.messaging.saaj.soap.ver1_1.FaultElement1_1Impl;
import com.sun.xml.messaging.saaj.soap.ver1_1.Header1_1Impl;
import com.sun.xml.messaging.saaj.soap.ver1_1.SOAPPart1_1Impl;
import com.sun.xml.messaging.saaj.soap.ver1_2.Body1_2Impl;
import com.sun.xml.messaging.saaj.soap.ver1_2.Detail1_2Impl;
import com.sun.xml.messaging.saaj.soap.ver1_2.Envelope1_2Impl;
import com.sun.xml.messaging.saaj.soap.ver1_2.Fault1_2Impl;
import com.sun.xml.messaging.saaj.soap.ver1_2.Header1_2Impl;
import com.sun.xml.messaging.saaj.soap.ver1_2.SOAPPart1_2Impl;
import org.w3c.dom.Element;

import java.util.Objects;


public class ElementFactory {
    public static SOAPElement createElement(
        SOAPDocumentImpl ownerDocument,
        Name name) {
        return createElement(
            ownerDocument,
            name.getLocalName(),
            name.getPrefix(),
            name.getURI());
    }
    public static SOAPElement createElement(
        SOAPDocumentImpl ownerDocument,
        QName name) {
        return createElement(
            ownerDocument,
            name.getLocalPart(),
            name.getPrefix(),
            name.getNamespaceURI());
    }

    /**
     * Create element wrapper for existing DOM element.
     *
     * @param ownerDocument SOAP document wrapper not null
     * @param element DOM element not null
     * @return SOAP wrapper for DOM element
     */
    public static SOAPElement createElement(SOAPDocumentImpl ownerDocument, Element element) {
        Objects.requireNonNull(ownerDocument);
        Objects.requireNonNull(element);

        String localName = element.getLocalName();
        String namespaceUri = element.getNamespaceURI();
        String prefix = element.getPrefix();

        if ("Envelope".equalsIgnoreCase(localName)) {
            if (NameImpl.SOAP11_NAMESPACE.equals(namespaceUri)) {
                return new Envelope1_1Impl(ownerDocument, element);
            } else if (NameImpl.SOAP12_NAMESPACE.equals(namespaceUri)) {
                return new Envelope1_2Impl(ownerDocument, element);
            }
        }
        if ("Body".equalsIgnoreCase(localName)) {
            if (NameImpl.SOAP11_NAMESPACE.equals(namespaceUri)) {
                return new Body1_1Impl(ownerDocument, element);
            } else if (NameImpl.SOAP12_NAMESPACE.equals(namespaceUri)) {
                return new Body1_2Impl(ownerDocument, element);
            }
        }
        if ("Header".equalsIgnoreCase(localName)) {
            if (NameImpl.SOAP11_NAMESPACE.equals(namespaceUri)) {
                return new Header1_1Impl(ownerDocument, element);
            } else if (NameImpl.SOAP12_NAMESPACE.equals(namespaceUri)) {
                return new Header1_2Impl(ownerDocument, element);
            }
        }
        if ("Fault".equalsIgnoreCase(localName)) {
            if (NameImpl.SOAP11_NAMESPACE.equals(namespaceUri)) {
                return new Fault1_1Impl(ownerDocument, element);
            } else if (NameImpl.SOAP12_NAMESPACE.equals(namespaceUri)) {
                return new Fault1_2Impl(ownerDocument, element);
            }

        }
        if ("Detail".equalsIgnoreCase(localName)) {
            if (NameImpl.SOAP11_NAMESPACE.equals(namespaceUri)) {
                return new Detail1_1Impl(ownerDocument, element);
            } else if (NameImpl.SOAP12_NAMESPACE.equals(namespaceUri)) {
                return new Detail1_2Impl(ownerDocument, element);
            }
        }
        if ("faultcode".equalsIgnoreCase(localName)
                || "faultstring".equalsIgnoreCase(localName)
                || "faultactor".equalsIgnoreCase(localName)) {
            // SOAP 1.2 does not have fault(code/string/actor)
            // So there is no else case required
            if (NameImpl.SOAP11_NAMESPACE.equals(namespaceUri)) {
                return new FaultElement1_1Impl(ownerDocument,
                        localName,
                        prefix);
            }
        }

        return new ElementImpl(ownerDocument, element);
    }

    public static SOAPElement createElement(
        SOAPDocumentImpl ownerDocument,
        String localName,
        String prefix,
        String namespaceUri) {


        if (ownerDocument == null) {
            if (NameImpl.SOAP11_NAMESPACE.equals(namespaceUri)) {
                ownerDocument = new SOAPPart1_1Impl().getDocument();
            } else if (NameImpl.SOAP12_NAMESPACE.equals(namespaceUri)) {
                ownerDocument = new SOAPPart1_2Impl().getDocument();
            } else {
                ownerDocument = new SOAPDocumentImpl(null);
            }
        }

        SOAPElement newElement =
            createNamedElement(ownerDocument, localName, prefix, namespaceUri);

        return newElement != null
            ? newElement
            : new ElementImpl(
                ownerDocument,
                namespaceUri,
                NameImpl.createQName(prefix, localName));
    }

    public static SOAPElement createNamedElement(
        SOAPDocumentImpl ownerDocument,
        String localName,
        String prefix,
        String namespaceUri) {

        if (prefix == null) {
            prefix = NameImpl.SOAP_ENVELOPE_PREFIX;
        }

        if ("Envelope".equalsIgnoreCase(localName)) {
            if (NameImpl.SOAP11_NAMESPACE.equals(namespaceUri)) {
                return new Envelope1_1Impl(ownerDocument, prefix);
            } else if (NameImpl.SOAP12_NAMESPACE.equals(namespaceUri)) {
                return new Envelope1_2Impl(ownerDocument, prefix);
            }
        }
        if ("Body".equalsIgnoreCase(localName)) {
            if (NameImpl.SOAP11_NAMESPACE.equals(namespaceUri)) {
                return new Body1_1Impl(ownerDocument, prefix);
            } else if (NameImpl.SOAP12_NAMESPACE.equals(namespaceUri)) {
                return new Body1_2Impl(ownerDocument, prefix);
            }
        }
        if ("Header".equalsIgnoreCase(localName)) {
            if (NameImpl.SOAP11_NAMESPACE.equals(namespaceUri)) {
                return new Header1_1Impl(ownerDocument, prefix);
            } else if (NameImpl.SOAP12_NAMESPACE.equals(namespaceUri)) {
                return new Header1_2Impl(ownerDocument, prefix);
            }
        }
        if ("Fault".equalsIgnoreCase(localName)) {
            SOAPFault fault = null;
            if (NameImpl.SOAP11_NAMESPACE.equals(namespaceUri)) {
                fault = new Fault1_1Impl(ownerDocument, prefix);
            } else if (NameImpl.SOAP12_NAMESPACE.equals(namespaceUri)) {
                fault = new Fault1_2Impl(ownerDocument, prefix);
            }

            if (fault != null) {
//                try {
//                    fault.addNamespaceDeclaration(
//                        NameImpl.SOAP_ENVELOPE_PREFIX,
//                        SOAPConstants.URI_NS_SOAP_ENVELOPE);
//                    fault.setFaultCode(
//                        NameImpl.create(
//                            "Server",
//                            NameImpl.SOAP_ENVELOPE_PREFIX,
//                            SOAPConstants.URI_NS_SOAP_ENVELOPE));
//                    fault.setFaultString(
//                        "Fault string, and possibly fault code, not set");
//                } catch (SOAPException e) {
//                }
                return fault;
            }

        }
        if ("Detail".equalsIgnoreCase(localName)) {
            if (NameImpl.SOAP11_NAMESPACE.equals(namespaceUri)) {
                return new Detail1_1Impl(ownerDocument, prefix);
            } else if (NameImpl.SOAP12_NAMESPACE.equals(namespaceUri)) {
                return new Detail1_2Impl(ownerDocument, prefix);
            }
        }
        if ("faultcode".equalsIgnoreCase(localName)
                || "faultstring".equalsIgnoreCase(localName)
                || "faultactor".equalsIgnoreCase(localName)) {
            // SOAP 1.2 does not have fault(code/string/actor)
            // So there is no else case required
            if (NameImpl.SOAP11_NAMESPACE.equals(namespaceUri)) {
                return new FaultElement1_1Impl(ownerDocument,
                                               localName,
                                               prefix);
            }
        }

        return null;
    }
    
    protected static void invalidCreate(String msg) {
        throw new TreeException(msg);
    }
}
