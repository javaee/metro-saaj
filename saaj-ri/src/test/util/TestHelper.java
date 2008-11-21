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
 * 
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

/**
 *
 * @author Edwin Goei
 */
package util;

import java.io.*;

import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;

public class TestHelper {
    private static final TestHelper INSTANCE = new TestHelper();

    /** Debug level.  A value of 0 means don't write any output. */
    private int debug;

    /**
     * OutputStream for debug output.  We need this b/c SAAJ writeTo method
     * takes OutputStream-s and not PrintWriter-s.
     */
    private OutputStream ostream = System.err;

    /** PrintWriter for debug output */
    private PrintWriter pw = new PrintWriter(ostream, true);

    public static TestHelper getInstance() {
        return INSTANCE;
    }

    private TestHelper() {
        try {
            debug = Integer.parseInt(System.getProperty("saaj.debug"));
        } catch (NumberFormatException x) {
            // assert(no such property defined or bad string)
            debug = 0;
        }
    }

    /**
     * @param dataId data resource identifier
     * @return an InputStream to the data
     * @throws Exception if resource is not found
     */
    public InputStream getInputStream(String dataId) throws Exception {
        InputStream is = getClass().getResourceAsStream("/resources/" + dataId);
        if (is == null) {
            throw new Exception("Resource not found: " + dataId);
        }
        return is;
    }

    /**
     * @return true if debug level is > 0
     */
    public boolean isDebug() {
        return debug > 0;
    }

    public void println(String s) {
        if (debug == 0) {
            return;
        }
        pw.println(s);
    }

    public void println() {
        if (debug == 0) {
            return;
        }
        pw.println();
    }

    public void print(String s) {
        if (debug == 0) {
            return;
        }
        pw.print(s);
    }

    /**
     * @param msg the SOAPMessage whose envelope to dump out
     * @throws Exception
     */
    public void dumpEnvelope(SOAPMessage msg) throws Exception {
        if (debug == 0) {
            return;
        }
        SOAPPart sp = msg.getSOAPPart();
        Source source = sp.getContent();
        TransformerFactory tf =
            new com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl();
        Transformer xform = tf.newTransformer();
        println("==== TestHelper.dumpEnvelope(...) Start ====");
        xform.transform(source, new StreamResult(pw));
        println();
        println("==== TestHelper.dumpEnvelope(...) End ====");
    }

    /**
     * Output similar to SOAPMessage.writeTo().
     *
     * @param msg the SOAPMessage to dump out
     * @throws Exception
     */
    public void writeTo(SOAPMessage msg) throws Exception {
        if (debug == 0) {
            return;
        }
        println("==== TestHelper.writeTo(...) Start ====");
        msg.writeTo(ostream);
        println();
        println("==== TestHelper.writeTo(...) End ====");
    }
}
