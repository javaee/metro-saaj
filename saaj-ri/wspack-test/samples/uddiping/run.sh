#! /bin/sh

#
# Copyright 2004 Sun Microsystems, Inc. All rights reserved.
# SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
#


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

#make sure there is atleast one argument
#usage: run.sh properties-file business-name

if [ $# -lt 2 ]; then
 echo Usage $0 properties-file business-name
 #exit stops the shell script
 exit
fi

oldCP=$CLASSPATH
 
SAAJ_LIB=../../lib
JAXP_LIB=../../../jaxp/lib/endorsed
SHARED_LIB=../../../jwsdp-shared/lib

unset CLASSPATH
for i in $SAAJ_LIB/*.jar ; do
  if [ "$CLASSPATH" != "" ]; then
       CLASSPATH=${CLASSPATH}:$i
  else
    CLASSPATH=$i
  fi
done

CLASSPATH=$CLASSPATH:.:$JAVA_HOME/lib/tools.jar:$SHARED_LIB/mail.jar:$SHARED_LIB/activation.jar:$SAAJ_LIB/saaj-api.jar:$SAAJ_LIB/saaj-impl.jar:$JAXP_LIB/../jaxp-api.jar:$JAXP_LIB/sax.jar:$JAXP_LIB/dom.jar:$JAXP_LIB/xercesImpl.jar:$JAXP_LIB/xalan.jar:$oldCP

$JAVACMD -classpath $CLASSPATH UddiPing "$1" "$2"
CLASSPATH=${oldCP}
export CLASSPATH
