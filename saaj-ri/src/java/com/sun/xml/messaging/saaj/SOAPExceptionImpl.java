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
 * $Id: SOAPExceptionImpl.java,v 1.1.1.1 2006-01-27 13:10:55 kumarjayanti Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2006-01-27 13:10:55 $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sun.xml.messaging.saaj;

import java.io.PrintStream;
import java.io.PrintWriter;

import javax.xml.soap.SOAPException;

/**
 * An exception that signals that a SOAP exception has occurred. A
 * <code>SOAPExceptionImpl</code> object may contain a <code>String</code>
 * that gives the reason for the exception, an embedded
 * <code>Throwable</code> object, or both. This class provides methods
 * for retrieving reason messages and for retrieving the embedded
 * <code>Throwable</code> object.
 *
 * <P> Typical reasons for throwing a <code>SOAPExceptionImpl</code>
 * object are problems such as difficulty setting a header, not being
 * able to send a message, and not being able to get a connection with
 * the provider.  Reasons for embedding a <code>Throwable</code>
 * object include problems such as input/output errors or a parsing
 * problem, such as an error in parsing a header.
 */
public class SOAPExceptionImpl extends SOAPException {
    private Throwable cause;

    /**
     * Constructs a <code>SOAPExceptionImpl</code> object with no
     * reason or embedded <code>Throwable</code> object.
     */
    public SOAPExceptionImpl() {
        super();
	this.cause = null;
    }

    /**
     * Constructs a <code>SOAPExceptionImpl</code> object with the given
     * <code>String</code> as the reason for the exception being thrown.
     *
     * @param reason a description of what caused the exception
     */
    public SOAPExceptionImpl(String reason) {
        super(reason);
	this.cause = null;
    }

    /**
     * Constructs a <code>SOAPExceptionImpl</code> object with the given
     * <code>String</code> as the reason for the exception being thrown
     * and the given <code>Throwable</code> object as an embedded
     * exception.
     *
     * @param reason a description of what caused the exception
     * @param cause a <code>Throwable</code> object that is to
     *        be embedded in this <code>SOAPExceptionImpl</code> object
     */
    public SOAPExceptionImpl(String reason, Throwable cause) {
       super (reason);
       initCause(cause);
    }

    /**
     * Constructs a <code>SOAPExceptionImpl</code> object initialized
     * with the given <code>Throwable</code> object.
     */
    public SOAPExceptionImpl(Throwable cause) {
	super (cause.toString());
	initCause(cause);
    }

    /**
     * Returns the detail message for this <code>SOAPExceptionImpl</code>
     * object.
     * <P>
     * If there is an embedded <code>Throwable</code> object, and if the 
     * <code>SOAPExceptionImpl</code> object has no detail message of its 
     * own, this method will return the detail message from the embedded 
     * <code>Throwable</code> object.
     *
     * @return the error or warning message for this 
     *         <code>SOAPExceptionImpl</code> or, if it has none, the 
     *         message of the embedded <code>Throwable</code> object,
     *         if there is one
     */
    public String getMessage() {
   	String message = super.getMessage ();
	if (message == null && cause != null) {
	    return cause.getMessage();	
	} else {
	    return message;
	}
    }

    /**
     * Returns the <code>Throwable</code> object embedded in this 
     * <code>SOAPExceptionImpl</code> if there is one. Otherwise, this method 
     * returns <code>null</code>.
     *
     * @return the embedded <code>Throwable</code> object or <code>null</code> 
     *         if there is none
     */

    public Throwable getCause() {
	return cause;
    }

    /**
     * Initializes the <code>cause</code> field of this <code>SOAPExceptionImpl</code> 
     * object with the given <code>Throwable</code> object.
     * <P>
     * This method can be called at most once.  It is generally called from
     * within the constructor or immediately after the constructor has
     * returned a new <code>SOAPExceptionImpl</code> object.
     * If this <code>SOAPExceptionImpl</code> object was created with the
     * constructor {@link #SOAPExceptionImpl(Throwable)} or
     * {@link #SOAPExceptionImpl(String,Throwable)}, meaning that its
     * <code>cause</code> field already has a value, this method cannot be 
     * called even once.
     *
     * @param  cause the <code>Throwable</code> object that caused this
     *         <code>SOAPExceptionImpl</code> object to be thrown.  The value of this
     *         parameter is saved for later retrieval by the
     *         {@link #getCause()} method.  A <tt>null</tt> value is
     *         permitted and indicates that the cause is nonexistent or
     *         unknown.
     * @return  a reference to this <code>SOAPExceptionImpl</code> instance
     * @throws IllegalArgumentException if <code>cause</code> is this
     *         <code>Throwable</code> object.  (A <code>Throwable</code> object
     *         cannot be its own cause.)
     * @throws IllegalStateException if this <code>SOAPExceptionImpl</code> object
     *         was created with {@link #SOAPExceptionImpl(Throwable)} or
     *         {@link #SOAPExceptionImpl(String,Throwable)}, or this 
     *         method has already been called on this <code>SOAPExceptionImpl</code>
     *         object
     */
    public synchronized Throwable initCause(Throwable cause) 
    {
   	if(this.cause != null) {
	    throw new IllegalStateException("Can't override cause");
	}
	if(cause == this) {
	    throw new IllegalArgumentException("Self-causation not permitted");	
	}
	this.cause = cause;

	return this;
    }
    
    public void printStackTrace() {
        super.printStackTrace();
        if (cause != null) {
            System.err.println("\nCAUSE:\n");
            cause.printStackTrace();
        }
    }

    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        if (cause != null) {
            s.println("\nCAUSE:\n");
            cause.printStackTrace(s);
        }
    }

    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        if (cause != null) {
            s.println("\nCAUSE:\n");
            cause.printStackTrace(s);
        }
    }
}
