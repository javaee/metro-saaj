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

package com.sun.xml.messaging.saaj.packaging.mime.internet;

import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;
import com.sun.xml.messaging.saaj.soap.AttachmentPartImpl;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.activation.DataSource;
import org.jvnet.mimepull.MIMEConfig;
import org.jvnet.mimepull.MIMEMessage;
import org.jvnet.mimepull.MIMEPart;

/**
 *
 * @author Kumar
 */
public class MimePullMultipart  extends MimeMultipart {

    private InputStream in = null;
    private String boundary = null;
    private MIMEMessage mm = null;
    private DataSource dataSource = null;
    private ContentType contType = null;
    private String startParam = null;
    private MIMEPart soapPart = null;

    public MimePullMultipart(DataSource ds, ContentType ct)
        throws MessagingException {
        parsed = false;
        if (ct==null)
            contType = new ContentType(ds.getContentType());
        else
            contType = ct;

        dataSource = ds;
        boundary = contType.getParameter("boundary");
    }

    public  MIMEPart readAndReturnSOAPPart() throws  MessagingException {
         if (soapPart != null) {
            throw new MessagingException("Inputstream from datasource was already consumed");
         }
         readSOAPPart();
         return soapPart;
         
    }

    protected  void readSOAPPart() throws  MessagingException {
        try {
            if (soapPart != null) {
                return;
            }
            in = dataSource.getInputStream();
            MIMEConfig config = new MIMEConfig(); //use defaults
            mm = new MIMEMessage(in, boundary, config);
            String st = contType.getParameter("start");
            if(startParam == null) {
                soapPart = mm.getPart(0);
            } else {
                  // Strip <...> from root part's Content-I
 	        if (st != null && st.length() > 2 && st.charAt(0) == '<' && st.charAt(st.length()-1) == '>') {
 	            st = st.substring(1, st.length()-1);
 	        }
                startParam = st;
                soapPart = mm.getPart(startParam);

            }
        } catch (IOException ex) {
            throw new MessagingException("No inputstream from datasource", ex);
        }
    }

    public void parseAll() throws MessagingException {
        if (parsed) {
            return;
        }
        if (soapPart == null) {
            readSOAPPart();
        }

        List<MIMEPart> prts = mm.getAttachments();
        for(MIMEPart part : prts) {
            if (part != soapPart) {
                new AttachmentPartImpl(part);
                this.addBodyPart(new MimeBodyPart(part));
            }
       }
       parsed = true;
    }

    @Override
    protected  void parse() throws MessagingException {
        parseAll();
    }

}
