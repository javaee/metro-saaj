The SOAP with Attachments API for Java<sup><font size="-2">TM</font></sup>
(SAAJ) 1.4 provides the API for creating and sending SOAP messages by
means of the `javax.xml.soap` package. It is used for the SOAP
messaging that goes on behind the scenes in JAX-WS, JAX-RPC, and JAXR
implementations. SOAP Handlers in JAX-WS use SAAJ APIs to access the
SOAP Message. Developers can also use it to write SOAP messaging
applications directly instead of using JAX-WS/JAX-RPC.

&nbsp;  

The SAAJ API allows a client to send messages directly to the
ultimate recipient using a `SOAPConnection` object, which
provides a point-to-point connection to the intended recipient.
Response messages
are received *synchronously* using a *request-response*
model.
*SOAPConnection* (and its related classes) is a pure library
implementation that lets you send SOAP messages directly to a remote
party. A standalone client, that is, one that does not run in a
container such as a servlet, must include client-side libraries in its CLASSPATH.
This model is simple to get started but has limited possibilities for
reliability and message delivery guarantees. For instance, the
point-to-point message exchange model relies largely on the reliability
of the underlying transport for delivering a message.

## Licensing and Governance

SAAJ RI is licensed under a dual license - CDDL 1.1 and GPL 2.0 with Class-path Exception. That means you can choose which one of the two suits your needs better and use it under those terms.

We use [GlassFish Governance Policy](https://javaee.github.io/metro-saaj/CONTRIBUTING), which means we can only accept contributions under the 
terms of [OCA](http://oracle.com/technetwork/goto/oca).

## Note

The SAAJ specification is developed through the Java Community Process following
the process described at [jcp.org](http://www.jcp.org/en/procedures/overview).
This process involves an Expert Group with a lead that is responsible for
delivering the specification, a reference implementation (RI) and a test
compatibility kit (TCK).
The primary goal of an RI is to support the development of the specification
and to validate it. Specific RIs can have additional goals; the SAAJ RI is
a production-quality implementation that is used directly in a number of products
by Oracle and other vendors. To emphasize the quality of the implementation
we call it a <em>Standard Implementation</em>.

&nbsp;  

The SAAJ expert group has wide industry participation with Oracle as the EG lead.
The initial API was part of JAXM 1.0 in [JSR-67](http://www.jcp.org/en/jsr/detail?id=67)
and was released in December 2001; the specification was later separated from JAXM
in a maintenance release in June 2002.
