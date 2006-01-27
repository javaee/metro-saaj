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
 * $Id: HeaderElementImpl.java,v 1.1.1.1 2006-01-27 13:10:57 kumarjayanti Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2006-01-27 13:10:57 $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sun.xml.messaging.saaj.soap.impl;

import javax.xml.namespace.QName;
import javax.xml.soap.*;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.name.NameImpl;

public abstract class HeaderElementImpl
    extends ElementImpl
    implements SOAPHeaderElement {

    protected static Name RELAY_ATTRIBUTE_LOCAL_NAME =
        NameImpl.createFromTagName("relay");
    protected static Name MUST_UNDERSTAND_ATTRIBUTE_LOCAL_NAME =
        NameImpl.createFromTagName("mustUnderstand");

    public HeaderElementImpl(SOAPDocumentImpl ownerDoc, Name qname) {
        super(ownerDoc, qname);
    }
    public HeaderElementImpl(SOAPDocumentImpl ownerDoc, QName qname) {
        super(ownerDoc, qname);
    }

    protected abstract NameImpl getActorAttributeName();
    protected abstract NameImpl getRoleAttributeName();
    protected abstract NameImpl getMustunderstandAttributeName();
    protected abstract boolean getMustunderstandAttributeValue(String str);
    protected abstract String getMustunderstandLiteralValue(boolean mu);
    protected abstract NameImpl getRelayAttributeName();
    protected abstract boolean getRelayAttributeValue(String str);
    protected abstract String getRelayLiteralValue(boolean mu);
    protected abstract String getActorOrRole();


    public void setParentElement(SOAPElement element) throws SOAPException {
        if (!(element instanceof SOAPHeader)) {
            log.severe("SAAJ0130.impl.header.elem.parent.mustbe.header");
            throw new SOAPException("Parent of a SOAPHeaderElement has to be a SOAPHeader");
        }

        super.setParentElement(element);
    }

    public void setActor(String actorUri) {
        try {
            removeAttribute(getActorAttributeName());
            addAttribute((Name) getActorAttributeName(), actorUri);
        } catch (SOAPException ex) {
        }
    }

    //SOAP 1.2 supports Role
    public void setRole(String roleUri) throws SOAPException {
        // runtime exception thrown if called for SOAP 1.1
        removeAttribute(getRoleAttributeName());
        addAttribute((Name) getRoleAttributeName(), roleUri);
    }


    Name actorAttNameWithoutNS = NameImpl.createFromTagName("actor");

    public String getActor() {
        String actor = getAttributeValue(getActorAttributeName());
        return actor;
    }

    Name roleAttNameWithoutNS = NameImpl.createFromTagName("role");

    public String getRole() {
        // runtime exception thrown for 1.1
        String role = getAttributeValue(getRoleAttributeName());
        return role;
    }

    public void setMustUnderstand(boolean mustUnderstand) {
        try {
            removeAttribute(getMustunderstandAttributeName());
            addAttribute(
                (Name) getMustunderstandAttributeName(),
                getMustunderstandLiteralValue(mustUnderstand));
        } catch (SOAPException ex) {
        }
    }

    public boolean getMustUnderstand() {
        String mu = getAttributeValue(getMustunderstandAttributeName());

        if (mu != null)
            return getMustunderstandAttributeValue(mu);

        return false;
    }

    public void setRelay(boolean relay) throws SOAPException {
        // runtime exception thrown for 1.1
        removeAttribute(getRelayAttributeName());
        addAttribute(
            (Name) getRelayAttributeName(),
            getRelayLiteralValue(relay));
    }

    public boolean getRelay() {
        String mu = getAttributeValue(getRelayAttributeName());
        if (mu != null)
            return getRelayAttributeValue(mu);

        return false;
    }
}
