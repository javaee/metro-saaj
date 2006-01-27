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
 * $Id: Client.java,v 1.1.1.1 2006-01-27 13:11:09 kumarjayanti Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2006-01-27 13:11:09 $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
