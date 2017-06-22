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

/*
 * $Id: Client.java,v 1.5 2009-01-17 00:39:46 ramapulavarthi Exp $
 * $Revision: 1.5 $
 * $Date: 2009-01-17 00:39:46 $
 */


import java.io.IOException;
import java.net.InetAddress;

import javax.xml.soap.*;

public class Client implements SOAPCallback {

	private static final int PORT = 12322;
	private static final int NO_MSGS = 5;
	private SOAPListener httpServer;
	private int msgNo = 0;
	
	public Client() {
		httpServer = new SOAPListenerImpl();
		start();
	}
	
	public void start() {
		try {
			httpServer.initMsgLoop(PORT, this);
			httpServer.startMsgLoopInThread();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onMessage(SOAPMessage msg) {
		try {
			++msgNo;
			System.out.println("Received message ..."+msgNo);
			msg.writeTo(System.out);
			System.out.println();
			if (msgNo >= NO_MSGS) {
				httpServer.stopMsgLoopInThread();
			}
		} catch (SOAPException se) {
			se.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
	
	public void sendMessage() throws Exception {
		SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
		SOAPConnection con = scf.createConnection();
		MessageFactory mf = MessageFactory.newInstance();
		SOAPMessage msg = mf.createMessage();
		SOAPPart sp = msg.getSOAPPart();
		SOAPEnvelope envelope = sp.getEnvelope();
		SOAPBody body = envelope.getBody();
		SOAPBodyElement gltp = body.addBodyElement(envelope.createName(
			"GetTime", "time", "http://wombat.time.com"));
		String addr = "http://"+InetAddress.getLocalHost().getHostAddress()+":"
			+PORT+"/index.html";
		gltp.addChildElement(envelope.createName("addr", "time",
			"http://wombat.time.com")).addTextNode(addr);
		gltp.addChildElement(envelope.createName("no", "time",
			"http://wombat.time.com")).addTextNode(""+NO_MSGS);
/*		
		String xml = "<START><A>Hello World</A></START>";
		StringReader rdr = new StringReader(xml);
		StreamSource source = new StreamSource(rdr);  
		AttachmentPart ap = msg.createAttachmentPart(source, "text/xml");
		msg.addAttachmentPart(ap);
*/
		
		String url = "http://"+InetAddress.getLocalHost().getHostAddress()+":"
			+TimeServer.PORT+"/index.html";
		System.out.println("Sending SOAP message to ="+url);
		SOAPMessage reply = con.call(msg, url);		
		con.close();
	}
	
	public static void main(String[] args) {
		try {
			Client client = new Client();
			client.sendMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
