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

import com.sun.xml.soap.*;

import java.io.ByteArrayInputStream;

import javax.xml.soap.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.namespace.QName;

public class SOAPProcessorSample {

    public static void main(String[] args) throws Exception {

        // Create a sample SOAP msg
        String doc = 
            "<?xml version=\"1.0\" ?><env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/'><env:Header><abc:Extension1 xmlns:abc='http://example.org/2001/06/ext' env:actor='http://schemas.xmlsoap.org/soap/actor/next' env:mustUnderstand='1'/><def:Extension2 xmlns:def='http://example.com/stuff' env:mustUnderstand=\"1\"/></env:Header><env:Body/></env:Envelope>";
       
        byte[] testDocBytes = doc.getBytes("UTF-8");
        ByteArrayInputStream bais = new ByteArrayInputStream(testDocBytes);
        StreamSource strSource = new StreamSource(bais);
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage sm = mf.createMessage();
        SOAPPart sp = sm.getSOAPPart();
        sp.setContent(strSource);
        sm.saveChanges();
        
        // Create a SOAPProcessor
        SOAPProcessorFactory factory = new SOAPProcessorFactory(); 
        SOAPProcessor processor = factory.createSOAPProcessor();

        // Initialize recipient1
        SOAPRecipient recipient1 = new SampleRecipient();
        recipient1.addRole(
            "http://schemas.xmlsoap.org/soap/actor/next");
        recipient1.addRole(
            "http://schemas.xmlsoap.org/soap/actor/ultimateReceiver");
        QName name1 = new QName("http://example.org/2001/06/ext",
                                "Extension1",
                                "abc");
        QName name2 = new QName("http://example.com/stuff",
                                "Extension2",
                                "def");
        recipient1.addHeader(name1);
        recipient1.addHeader(name2);

        // Add recipient1 to SOAPProcessor
        processor.addRecipient(recipient1);

        // Initialize recipient2
        SOAPRecipient recipient2 = new SampleRecipient();
        recipient2.addRole(
            "http://schemas.xmlsoap.org/soap/actor/next");
        recipient2.addHeader(name1);
        recipient2.addHeader(name2);

        // Add recipient2 to SOAPProcessor
        processor.addRecipient(recipient2);

        // SOAPProcessor processing the msg
        processor.acceptMessage(sm);

        // Initialize annotator
        SOAPAnnotator annotator = new SampleAnnotator();

        // Annotate headers
        processor.addAnnotator(annotator);

        // Prepare the msg before it can be sent out
        processor.prepareMessage(sm);

        // Output the final msg
        System.out.println("final message : ");
        sm.writeTo(System.out);
        System.out.println();
        System.out.println("final message ends here.");

    }
}
