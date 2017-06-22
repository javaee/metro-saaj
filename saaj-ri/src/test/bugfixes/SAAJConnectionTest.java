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

package bugfixes;

import java.net.URL;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPException;
import junit.framework.TestCase;


/*
 * CR :7013971
 */ 
public class SAAJConnectionTest extends TestCase {
    private static util.TestHelper th = util.TestHelper.getInstance();

    public SAAJConnectionTest(String name) {
        super(name);
    }

    public void testSAAJ65() throws Exception {
        
        for (int i = 0; i < 2; i++) {
            try {
                //Runtime.getRuntime().exec("ulimit -a");
                //TODO, need to add an assert.
                SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
                SOAPConnection con = scf.createConnection();
                SOAPMessage reply = MessageFactory.newInstance().createMessage();
                reply.writeTo(System.out);
                System.out.println("\n");
                reply = con.call(reply, new URL("http://www.oracle.com"));
            } catch (Exception ex) {
            }
        }
        
    }
    
    public void testBug7013971() throws Exception {
         try {
         SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
         SOAPConnection con = scf.createConnection();

         SOAPMessage reply = MessageFactory.newInstance().createMessage();
         reply.writeTo(System.out);
         System.out.println("\n");
         Thread.sleep(1000);
         reply = con.call(reply, new URL("http://www.oracle.com"));
         assertTrue(true);
        } catch (java.security.AccessControlException e) {
            assertTrue(false);
        }catch(SOAPException ex) {
            assertTrue(true);
        } 
    }

    public void testBug12308187() throws Exception {
         try {
         SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
         SOAPConnection con = scf.createConnection();
         SOAPMessage reply = con.get(new URL("http://www.oracle.com"));
         reply.writeTo(System.out);
         assertTrue(true);
        } catch (java.security.AccessControlException e) {
            assertTrue(false);
        }catch(SOAPException ex) {
            assertTrue(true);
        } 
    }
    
    public void testSAAJ56() throws Exception {
		try {
			SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
			SOAPConnection con = scf.createConnection();
			SOAPMessage msg = MessageFactory.newInstance().createMessage();
			con.call(msg, new URL("http://username:pass%25word@www.oracle.com"));
			// IPv6 - note this is the address from the bug, not www.oracle.com
			//con.call(msg, new URL("http://username:pass%25word@[fe80:0:0:0:2e0:81ff:fe33:1874]:9992"));
			assertTrue(true);
		} catch (java.security.AccessControlException e) {
			assertTrue(false);
		} catch (SOAPException ex) {
			assertTrue(true);
		}
   }


    public static void main(String argv[]) {
        junit.textui.TestRunner.run(SAAJConnectionTest.class);        
    }

}
