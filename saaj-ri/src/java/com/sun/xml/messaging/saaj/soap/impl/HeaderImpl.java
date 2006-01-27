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
 * $Id: HeaderImpl.java,v 1.1.1.1 2006-01-27 13:10:56 kumarjayanti Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2006-01-27 13:10:56 $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sun.xml.messaging.saaj.soap.impl;

import java.util.*;
import java.util.logging.Level;

import javax.xml.namespace.QName;
import javax.xml.soap.*;

import org.w3c.dom.Element;

import com.sun.xml.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.messaging.saaj.soap.SOAPDocument;
import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.name.NameImpl;

public abstract class HeaderImpl extends ElementImpl implements SOAPHeader {
    protected static final boolean MUST_UNDERSTAND_ONLY = false;

    protected HeaderImpl(SOAPDocumentImpl ownerDoc, NameImpl name) {
        super(ownerDoc, name);
    }

    protected abstract SOAPHeaderElement createHeaderElement(Name name)
        throws SOAPException;
    protected abstract SOAPHeaderElement createHeaderElement(QName name)
        throws SOAPException;
    protected abstract NameImpl getNotUnderstoodName();
    protected abstract NameImpl getUpgradeName();
    protected abstract NameImpl getSupportedEnvelopeName();

    public SOAPHeaderElement addHeaderElement(Name name) throws SOAPException {
        SOAPElement newHeaderElement =
            ElementFactory.createNamedElement(
                ((SOAPDocument) getOwnerDocument()).getDocument(),
                name.getLocalName(),
                name.getPrefix(),
                name.getURI());
        if (newHeaderElement == null
            || !(newHeaderElement instanceof SOAPHeaderElement)) {
            newHeaderElement = createHeaderElement(name);
        }

        // header elements must be namespace qualified
        // check that URI is  not empty, ensuring that the element is NS qualified.
        String uri = newHeaderElement.getElementQName().getNamespaceURI();
        if ((uri == null) || ("").equals(uri)) {
            log.severe("SAAJ0131.impl.header.elems.ns.qualified");
            throw new SOAPExceptionImpl("HeaderElements must be namespace qualified");
        }
        addNode(newHeaderElement);
        return (SOAPHeaderElement) newHeaderElement;
    }

    public SOAPHeaderElement addHeaderElement(QName name) throws SOAPException {
        SOAPElement newHeaderElement =
            ElementFactory.createNamedElement(
                ((SOAPDocument) getOwnerDocument()).getDocument(),
                name.getLocalPart(),
                name.getPrefix(),
                name.getNamespaceURI());
        if (newHeaderElement == null
            || !(newHeaderElement instanceof SOAPHeaderElement)) {
            newHeaderElement = createHeaderElement(name);
        }

        // header elements must be namespace qualified
        // check that URI is  not empty, ensuring that the element is NS qualified.
        String uri = newHeaderElement.getElementQName().getNamespaceURI();
        if ((uri == null) || ("").equals(uri)) {
            log.severe("SAAJ0131.impl.header.elems.ns.qualified");
            throw new SOAPExceptionImpl("HeaderElements must be namespace qualified");
        }
        addNode(newHeaderElement);
        return (SOAPHeaderElement) newHeaderElement;
    }

    protected SOAPElement addElement(Name name) throws SOAPException {
        return addHeaderElement(name);
    }

    protected SOAPElement addElement(QName name) throws SOAPException {
        return addHeaderElement(name);
    }

    public Iterator examineHeaderElements(String actor) {
        return getHeaderElementsForActor(actor, false, false);
    }

    public Iterator extractHeaderElements(String actor) {
        return getHeaderElementsForActor(actor, true, false);
    }

    protected Iterator getHeaderElementsForActor(
        String actor,
        boolean detach,
        boolean mustUnderstand) {
        if (actor == null || actor.equals("")) {
            log.severe("SAAJ0132.impl.invalid.value.for.actor.or.role");
            throw new IllegalArgumentException("Invalid value for actor or role");
        }
        return getHeaderElements(actor, detach, mustUnderstand);
    }

    protected Iterator getHeaderElements(
        String actor,
        boolean detach,
        boolean mustUnderstand) {
        List elementList = new ArrayList();

        Iterator eachChild = getChildElements();

        Object currentChild = iterate(eachChild);
        while (currentChild != null) {
            if (!(currentChild instanceof SOAPHeaderElement)) {
                currentChild = iterate(eachChild);
            } else {
                HeaderElementImpl currentElement =
                    (HeaderElementImpl) currentChild;
                currentChild = iterate(eachChild);

                boolean isMustUnderstandMatching =
                    (!mustUnderstand || currentElement.getMustUnderstand());
                boolean doAdd = false;
                if (actor == null && isMustUnderstandMatching) {
                    doAdd = true;
                } else {
                    String currentActor = currentElement.getActorOrRole();
                    if (currentActor == null) {
                        currentActor = "";
                    }

                    if (currentActor.equalsIgnoreCase(actor)
                        && isMustUnderstandMatching) {
                        doAdd = true;
                    }
                }

                if (doAdd) {
                    elementList.add(currentElement);
                    if (detach) {
                        currentElement.detachNode();
                    }
                }
            }
        }

        return elementList.listIterator();
    }

    private Object iterate(Iterator each) {
        return each.hasNext() ? each.next() : null;
    }

    public void setParentElement(SOAPElement element) throws SOAPException {
        if (!(element instanceof SOAPEnvelope)) {
            log.severe("SAAJ0133.impl.header.parent.mustbe.envelope");
            throw new SOAPException("Parent of SOAPHeader has to be a SOAPEnvelope");
        }
        super.setParentElement(element);
    }

    // overriding ElementImpl's method to ensure that HeaderElements are
    // namespace qualified. Holds for both SOAP versions.
    // TODO - This check needs to be made for other addChildElement() methods
    // as well.
    public SOAPElement addChildElement(String localName) throws SOAPException {

        SOAPElement element = super.addChildElement(localName);
        // check that URI is  not empty, ensuring that the element is NS qualified.
        String uri = element.getElementName().getURI();
        if ((uri == null) || ("").equals(uri)) {
            log.severe("SAAJ0134.impl.header.elems.ns.qualified");
            throw new SOAPExceptionImpl("HeaderElements must be namespace qualified");
        }
        return element;
    }

    public Iterator examineAllHeaderElements() {
        return getHeaderElements(null, false, MUST_UNDERSTAND_ONLY);
    }

    public Iterator examineMustUnderstandHeaderElements(String actor) {
        return getHeaderElements(actor, false, true);

    }

    public Iterator extractAllHeaderElements() {
        return getHeaderElements(null, true, false);
    }

    public SOAPHeaderElement addUpgradeHeaderElement(Iterator supportedSoapUris)
        throws SOAPException {
        if (supportedSoapUris == null) {
            log.severe("SAAJ0411.ver1_2.no.null.supportedURIs");
            throw new SOAPException("Argument cannot be null; iterator of supportedURIs cannot be null");
        }
        if (!supportedSoapUris.hasNext()) {
            log.severe("SAAJ0412.ver1_2.no.empty.list.of.supportedURIs");
            throw new SOAPException("List of supported URIs cannot be empty");
        }
        Name upgradeName = getUpgradeName();
        SOAPHeaderElement upgradeHeaderElement =
            (SOAPHeaderElement) addChildElement(upgradeName);
        Name supportedEnvelopeName = getSupportedEnvelopeName();
        int i = 0;
        while (supportedSoapUris.hasNext()) {
            SOAPElement subElement =
                upgradeHeaderElement.addChildElement(supportedEnvelopeName);
            String ns = "ns" + Integer.toString(i);
            subElement.addAttribute(
                NameImpl.createFromUnqualifiedName("qname"),
                ns + ":Envelope");
            subElement.addNamespaceDeclaration(
                ns,
                (String) supportedSoapUris.next());
            i ++;
        }
        return upgradeHeaderElement;
    }

    public SOAPHeaderElement addUpgradeHeaderElement(String supportedSoapUri)
        throws SOAPException {
        return addUpgradeHeaderElement(new String[] {supportedSoapUri});
    }

    public SOAPHeaderElement addUpgradeHeaderElement(String[] supportedSoapUris)
        throws SOAPException {

        if (supportedSoapUris == null) {
            log.severe("SAAJ0411.ver1_2.no.null.supportedURIs");
            throw new SOAPException("Argument cannot be null; array of supportedURIs cannot be null");
        }
        if (supportedSoapUris.length == 0) {
            log.severe("SAAJ0412.ver1_2.no.empty.list.of.supportedURIs");
            throw new SOAPException("List of supported URIs cannot be empty");
        }
        Name upgradeName = getUpgradeName();
        SOAPHeaderElement upgradeHeaderElement =
            (SOAPHeaderElement) addChildElement(upgradeName);
        Name supportedEnvelopeName = getSupportedEnvelopeName();
        for (int i = 0; i < supportedSoapUris.length; i ++) {
            SOAPElement subElement =
                upgradeHeaderElement.addChildElement(supportedEnvelopeName);
            String ns = "ns" + Integer.toString(i);
            subElement.addAttribute(
                NameImpl.createFromUnqualifiedName("qname"),
                ns + ":Envelope");
            subElement.addNamespaceDeclaration(ns, supportedSoapUris[i]);
        }
        return upgradeHeaderElement;
    }

    protected SOAPElement convertToSoapElement(Element element) {
        if (element instanceof SOAPHeaderElement) {
            return (SOAPElement) element;
        } else {
            SOAPHeaderElement headerElement;
            try {
                headerElement =
                    createHeaderElement(NameImpl.copyElementName(element));
            } catch (SOAPException e) {
                throw new ClassCastException("Could not convert Element to SOAPHeaderElement: " + e.getMessage());
            }
            return replaceElementWithSOAPElement(
                element,
                (ElementImpl) headerElement);
        }
    }

    public SOAPElement setElementQName(QName newName) throws SOAPException {
       log.log(Level.SEVERE,
                "SAAJ0146.impl.invalid.name.change.requested",
                new Object[] {elementQName.getLocalPart(),
                              newName.getLocalPart()});
        throw new SOAPException("Cannot change name for "
                                + elementQName.getLocalPart() + " to "
                                + newName.getLocalPart());
    }

}
