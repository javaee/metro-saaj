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
 * $Id: TimeServer.java,v 1.5 2009-01-17 00:39:46 ramapulavarthi Exp $
 * $Revision: 1.5 $
 * $Date: 2009-01-17 00:39:46 $
 */


 
import java.io.IOException;
import java.util.Date;

import javax.xml.soap.*;

public class TimeServer implements SOAPCallback {

	public static final int PORT = 12321;
	private SOAPListener httpServer;
	
	public TimeServer() {
		httpServer = new SOAPListenerImpl();
		start();
	}
	
	public void start() {
		try {
			httpServer.initMsgLoop(PORT, this);
			httpServer.startMsgLoop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onMessage(SOAPMessage msg) {
		MessageProcessor proc = new MessageProcessor(msg);
		// Start a new thread to process this client's request
		new Thread(proc, "Message Processor").start();
	}
	
	public static void main(String[] args) {
		TimeServer server = new TimeServer();
	}

	public class MessageProcessor implements Runnable {

		private SOAPMessage msg;

		public MessageProcessor(SOAPMessage msg) {
			this.msg = msg;
		}

		public void run() {
			try {
				System.out.println("Received Client Message...");
				msg.writeTo(System.out);
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope envelope = sp.getEnvelope();
				SOAPBody body = envelope.getBody();
				SOAPElement elem = (SOAPElement)body.getFirstChild().getFirstChild();
				
				String clientAddr = elem.getValue();
				String noOfTimes = ((SOAPElement)elem.getNextSibling()).getValue();
				int no = new Integer(noOfTimes).intValue();
				SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
				for(int i=0; i < no; i++) {
					SOAPConnection con = scf.createConnection();
					SOAPMessage msg = createMessage();
					System.out.println("Sending msg="+i+" to client at:"+clientAddr);
					SOAPMessage reply = con.call(msg, clientAddr);		
					con.close();
					try {
						Thread.sleep(500);
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (SOAPException se) {
				se.printStackTrace();
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}

		public SOAPMessage createMessage() throws SOAPException {
			MessageFactory mf = MessageFactory.newInstance();
			SOAPMessage msg = mf.createMessage();
			SOAPPart sp = msg.getSOAPPart();
			SOAPEnvelope envelope = sp.getEnvelope();
			SOAPBody body = envelope.getBody();
			SOAPBodyElement gltp = body.addBodyElement(
				envelope.createName("GetTime", "time",
					"http://wombat.time.com"));
			gltp.addTextNode(new Date().toString());
			return msg;
		}

	}

}
