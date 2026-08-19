#!/usr/bin/env sh

#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, embodiment or
# distribution of this software is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`/"$link"
    fi
done
SAVED="`pwd`"
CDPATH=
cd "`dirname \"$PRG\"`/" >/dev/null
APP_HOME="`pwd`"
cd "$SAVED" >/dev/null

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# Use the maximum available byte code version of the executing JVM.
# Check for JAVA_HOME first.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/libexec/java_home" ] ; then
        MY_JAVA_HOME=`"$JAVA_HOME/libexec/java_home"`
    else
        MY_JAVA_HOME="$JAVA_HOME"
    fi
fi

# Determine Java command
if [ -n "$MY_JAVA_HOME" ] ; then
    JAVACMD="$MY_JAVA_HOME/bin/java"
else
    JAVACMD="java"
fi

warn () {
    echo "$*"
}

die () {
    echo
    echo "$*"
    echo
    exit 1
}

# OS specific support. $var _must_ be set to true or false.
cygwin=false
msys=false
darwin=false
nonstop=false
case "`uname`" in
  CYGWIN* ) cygwin=true ;;
  Darwin* ) darwin=true ;;
  MINGW* ) msys=true ;;
  NONSTOP* ) nonstop=true ;;
esac

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar


# Local variables
SAVED_JAVA_HOME="$JAVA_HOME"

# Escape application args
exec "$JAVACMD" "-classpath" "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
