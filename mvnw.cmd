@echo off

setlocal

set MAVEN_PROJECTBASEDIR=%~dp0

if exist "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" (
  set JAVA_EXE=java
  "%JAVA_EXE%" -jar "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" %*
) else (
  echo Could not find Maven Wrapper jar. Please run 'mvn -N io.takari:maven:wrapper' to generate it.
  exit /b 1
)
