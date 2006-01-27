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

oldCP=$CLASSPATH
 
SAAJ_LIB=../../lib

JAXP_HOME=../../lib

unset CLASSPATH
for i in $SAAJ_LIB/*.jar ; do
  if [ "$CLASSPATH" != "" ]; then
       CLASSPATH=${CLASSPATH}:$i
  else
    CLASSPATH=$i
  fi
done

CLASSPATH=$CLASSPATH:$SAAJ_LIB/saaj-impl.jar:.:$oldCP

$JAVACMD -classpath $CLASSPATH Client
CLASSPATH=${oldCP}
export CLASSPATH
