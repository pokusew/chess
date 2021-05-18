#!/usr/bin/env bash

set -e

# script that produces an executable self-contained JAR for Java 11+ (tested with Java 15)

# ASCII color sequences
# credits: https://stackoverflow.com/questions/5947742/how-to-change-the-output-color-of-echo-in-linux
# see also: https://unix.stackexchange.com/questions/269077/tput-setaf-color-table-how-to-determine-color-codes
# to get all 256 colors:
#   for c in {0..255}; do tput setaf $c; tput setaf $c | cat -v; echo =$c; done
red=$(tput setaf 1)
green=$(tput setaf 2)
cyan=$(tput setaf 6)
gray=$(tput setaf 8)
bold=$(tput bold)
reset=$(tput sgr0)

# here you can rewrite JAVA_HOME and Maven will use it (in case you work with more java versions)
# export JAVA_HOME=/Library/Java/JavaVirtualMachines/adoptopenjdk-15.jdk/Contents/Home

echo "${gray}Running mvm clean ... ${reset}"

mvn clean --file build-pom.xml

echo "${gray}Building ... ${reset}"

mvn package --file build-pom.xml
