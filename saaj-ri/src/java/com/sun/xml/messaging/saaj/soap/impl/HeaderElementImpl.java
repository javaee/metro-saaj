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
import org.w3c.dom.Element;

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

    public HeaderElementImpl(SOAPDocumentImpl ownerDoc, Element domElement) {
        super(ownerDoc, domElement);
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


    @Override
    public void setParentElement(SOAPElement element) throws SOAPException {
        if (!(element instanceof SOAPHeader)) {
            log.severe("SAAJ0130.impl.header.elem.parent.mustbe.header");
            throw new SOAPException("Parent of a SOAPHeaderElement has to be a SOAPHeader");
        }

        super.setParentElement(element);
    }

    @Override
    public void setActor(String actorUri) {
        try {
            removeAttribute(getActorAttributeName());
            addAttribute((Name) getActorAttributeName(), actorUri);
        } catch (SOAPException ex) {
        }
    }

    //SOAP 1.2 supports Role
    @Override
    public void setRole(String roleUri) throws SOAPException {
        // runtime exception thrown if called for SOAP 1.1
        removeAttribute(getRoleAttributeName());
        addAttribute((Name) getRoleAttributeName(), roleUri);
    }


    Name actorAttNameWithoutNS = NameImpl.createFromTagName("actor");

    @Override
    public String getActor() {
        String actor = getAttributeValue(getActorAttributeName());
        return actor;
    }

    Name roleAttNameWithoutNS = NameImpl.createFromTagName("role");

    @Override
    public String getRole() {
        // runtime exception thrown for 1.1
        String role = getAttributeValue(getRoleAttributeName());
        return role;
    }

    @Override
    public void setMustUnderstand(boolean mustUnderstand) {
        try {
            removeAttribute(getMustunderstandAttributeName());
            addAttribute(
                (Name) getMustunderstandAttributeName(),
                getMustunderstandLiteralValue(mustUnderstand));
        } catch (SOAPException ex) {
        }
    }

    @Override
    public boolean getMustUnderstand() {
        String mu = getAttributeValue(getMustunderstandAttributeName());

        if (mu != null)
            return getMustunderstandAttributeValue(mu);

        return false;
    }

    @Override
    public void setRelay(boolean relay) throws SOAPException {
        // runtime exception thrown for 1.1
        removeAttribute(getRelayAttributeName());
        addAttribute(
            (Name) getRelayAttributeName(),
            getRelayLiteralValue(relay));
    }

    @Override
    public boolean getRelay() {
        String mu = getAttributeValue(getRelayAttributeName());
        if (mu != null)
            return getRelayAttributeValue(mu);

        return false;
    }
}
