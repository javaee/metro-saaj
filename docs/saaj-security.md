# SAAJ Security

This document explains how to set up authentication in the SAAJ
reference implementation, and how to set up HTTPS for secure message exchange.

## Authentication

For basic authentication, the SAAJ reference implementation uses the
`userInfo` part of the URL specification.

* Set up a user for the underlying container.
* Add a security constraint in `web.xml`. For example:
```
    <servlet>
        <servlet-name>saaj.authenticated</servlet-name>
        <jsp-file>/echo.jsp</jsp-file>
    </servlet>

    <servlet-mapping>
        <servlet-name>saaj.authenticated</servlet-name>
        <url-pattern>/authecho.jsp</url-pattern>
    </servlet-mapping>

    <security-constraint>
        <auth-constraint>
            <role-name>saaj</role-name>
        </auth-constraint>
    </security-constraint>

    <login-config>
        <auth-method>BASIC</auth-method>
    </login-config>
```
* The client should use the the following syntax for URLs:
```
    http://USER:PASSWORD@HOST:PORT/FILE
```

## Secure Transport

**Note:** Secure transport applies only to request/response messages
(those sent using the `SOAPConnection.call` method).

&nbsp;  

Setting up HTTPS is a bit more difficult. The critical part is setting
up the server certificates, required by Java Secure Socket Extension
(JSSE) in order to communicate with the server. You'll need to use the
following commands:
```
keytool -genkey -alias saaj-test -dname "cn=localhost" -keyalg RSA -storepass changeit
keytool -export -alias saaj-test -storepass changeit -file server.cer
keytool -import -v -trustcacerts -alias saaj-test -file server.cer \
        -keystore $JAVA_HOME/jre/lib/security/cacerts \
        -keypass changeit -storepass changeit
```
The first command will generate a server certificate in your `$HOME/.keystore`.
The `dname` should be localhost (if you use localhost in the URLs) or your hostname
(where you run the server).

&nbsp;  

The second command will export the certificate in a file, and the
third will import the certificate into the list of certificates the client
knows about.

An alternative is to use the `server.cer` and get it signed by one
of the certificate authorities; it will then work with any client,
without your having to import the certificate into each client VM.

&nbsp;  

The next step is getting the server of your choice to work with SSL.
If you are using a server that needs its SSL connector to be configured,
 consult the documentation for that server.

For example for Tomcat it is documented in the Tomcat documentation. You
may need to uncomment the "SSL Connector" portion from the `server.xml`
file. Please follow the Tomcat documentation.

&nbsp;  

Start the&nbsp; container and try a simple URL using HTTPS (like `https://host:8443/index.html`).
The browser should ask you to accept a certificate and then display the
page. If you got this to work, the server is running fine.

&nbsp;  

From the SAAJ side, all you need to do is use URLs with HTTPS as the protocol.
This will work *only* if the certificate was successfully imported into
`$JAVA_HOME/jre/lib/security/cacerts`; otherwise JSSE will not allow the connection.
