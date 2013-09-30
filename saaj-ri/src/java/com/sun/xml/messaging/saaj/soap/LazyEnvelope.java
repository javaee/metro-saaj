package com.sun.xml.messaging.saaj.soap;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public interface LazyEnvelope extends Envelope {
    public XMLStreamReader getPayloadReader() throws SOAPException;
    public boolean isLazy();
    public void writeTo(XMLStreamWriter writer) throws XMLStreamException, SOAPException;
    
    /**
     * Retrieve payload qname without materializing its contents
     * @return
     * @throws SOAPException
     */
    public QName getPayloadQName() throws SOAPException;
    
    /**
     * Retrieve payload attribute value without materializing its contents
     * @param localName
     * @return
     * @throws SOAPException
     */
    public String getPayloadAttributeValue(String localName) throws SOAPException;
    
    /**
     * Retrieve payload attribute value without materializing its contents
     * @param qName
     * @return
     * @throws SOAPException
     */
    public String getPayloadAttributeValue(QName qName) throws SOAPException;
}
