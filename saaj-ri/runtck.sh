#! /bin/sh

#
# $Id: runtck.sh,v 1.1.1.1 2006-01-27 13:10:44 kumarjayanti Exp $
#

#
# Copyright 2004 Sun Microsystems, Inc. All rights reserved.
# SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
#
if [ -z "$SAAJRI_HOME" ]
then
echo "Please set SAAJRI_HOME to the root directory of saaj-ri workspace."
exit 1
fi

if [ -z "$TS_HOME" ]
then
echo "Please set TS_HOME to the root directory of saajtck installation."
exit 1
fi

if [ -z "$JWSDP_HOME" ]
then
echo "Please set JWSDP_HOME to the root directory of jwsdp installation."
exit 1
fi

if [ -z "$JAVA_HOME" ]
then
echo "Please set JAVA_HOME to point to a JDK1.4.x installation."
exit 1
fi

if [ -z "$J2EE_HOME" ]
then
echo "Please set J2EE_HOME to point to a j2sdkee1.3 installation. This is needed to compile TCK tests."
exit 1
fi

if [ -z "$JAVA_HOME" ]
then
JAVACMD=`which java`
if [ -z "$JAVACMD" ]
then
echo "Cannot find JAVA. Please set your PATH."
exit 1
fi
JAVA_BINDIR=`dirname $JAVACMD`
JAVA_HOME=$JAVA_BINDIR/..
fi

JAVACMD=$JAVA_HOME/bin/java

cp=$JAVA_HOME/lib/tools.jar:$SAAJRI_HOME/lib/jaxp-api.jar:$SAAJRI_HOME/lib/ant.jar:$SAAJRI_HOME/lib/optional.jar:$SAAJRI_HOME/lib/junit.jar

cp $SAAJRI_HOME/tckprops/saajtck.properties $TS_HOME/bin/build.properties

$JAVACMD -classpath ${TS_HOME}/lib/ant_sun.jar:${TS_HOME}/lib/tsharness.jar:$cp:$CLASSPATH org.apache.tools.ant.Main -listener com.sun.ant.TSBuildListener -logger com.sun.ant.TSLogger "copytotck"

$JAVACMD -classpath ${TS_HOME}/lib/ant_sun.jar:${TS_HOME}/lib/tsharness.jar:$cp:$CLASSPATH org.apache.tools.ant.Main -listener com.sun.ant.TSBuildListener -logger com.sun.ant.TSLogger "runtck"
