#!/bin/sh

# ----------------------------------------------------------------------------
# Maven Start Up Batch script for UN*X
# ----------------------------------------------------------------------------

# Required ENV vars
MAVEN_PROJECTBASEDIR="$(cd "$(dirname "$0")"; pwd)"

if [ -z "$MAVEN_PROJECTBASEDIR" ]; then
  MAVEN_PROJECTBASEDIR=$(dirname "$0")
fi

if [ -x "$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar" ]; then
  JAVA_EXE="java"
  exec "$JAVA_EXE" -jar "$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar" "$@"
else
  echo "Could not find Maven Wrapper jar. Please run 'mvn -N io.takari:maven:wrapper' to generate it."
  exit 1
fi
