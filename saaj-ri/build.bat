@echo off

REM
REM $Id: build.bat,v 1.1.1.1 2006-01-27 13:10:44 kumarjayanti Exp $
REM

REM
REM Copyright 2004 Sun Microsystems, Inc. All rights reserved.
REM SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
REM

echo SAAJ-RI Builder
echo -------------------

if "%JAVA_HOME%" == "" goto error

set LOCALCLASSPATH=%JAVA_HOME%\lib\tools.jar;.\lib\jaxp-api.jar;.\lib\ant.jar;.\lib\optional.jar;.\lib\junit.jar;%ADDITIONALCLASSPATH%
set OLD_ANT_HOME=%ANT_HOME%
set ANT_HOME=./lib

echo Building with classpath %LOCALCLASSPATH%

echo Starting Ant...

%JAVA_HOME%\bin\java.exe -Dant.home="%ANT_HOME%" -classpath "%LOCALCLASSPATH%" org.apache.tools.ant.Main %1 %2 %3 %4 %5

goto end

:error

echo ERROR: JAVA_HOME not found in your environment.
echo Please, set the JAVA_HOME variable in your environment to match the
echo location of the Java Virtual Machine you want to use.

:end

set LOCALCLASSPATH=
set ANT_HOME=%OLD_ANT_HOME%
