REM
REM Copyright 2004 Sun Microsystems, Inc. All rights reserved.
REM SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
REM

set SAAJ_LIB=..\..\lib
set JAXP_LIB=..\..\..\jaxp\lib\endorsed
set SHARED_LIB=..\..\..\jwsdp-shared\lib

set CLASSPATH=%SHARED_LIB%\activation.jar;%SAAJ_LIB%\saaj-api.jar;%SHARED_LIB%\mail.jar;%SAAJ_LIB%\saaj-impl.jar;%JAXP_LIB%\..\jaxp-api.jar;%JAXP_LIB%\sax.jar;%JAXP_LIB%\dom.jar;%JAXP_LIB%\xercesImpl.jar;%JAXP_LIB%\xalan.jar;

java -classpath %CLASSPATH% UddiPing %1 %2
