REM
REM Copyright 2004 Sun Microsystems, Inc. All rights reserved.
REM SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
REM

set SAAJ_LIB=..\..\lib
set JAXP_HOME=..\..\lib
set CLASSPATH=%SAAJ_LIB%\activation.jar;%SAAJ_LIB%\saaj-api.jar;%SAAJ_LIB%\saaj-impl.jar;%SAAJ_LIB%\jax-qname.jar;%JAXP_HOME%\jaxp-api.jar;%JAXP_HOME%\sax.jar;%JAXP_HOME%\dom.jar;%JAXP_HOME%\xercesImpl.jar;%JAXP_HOME%\xalan.jar;

java -classpath %CLASSPATH% Client
