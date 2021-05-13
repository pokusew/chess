#!/usr/bin/env bash

# note: must be run in the project root
#       see doc rule in the Makefile in the project root

set -e

mvn_javadoc_output_dir='target/site/apidocs'

echo "running mvn clean ..."
mvn clean >/dev/null 2>&1
# this fails but generates at least javadoc.sh and options
echo "running mvn javadoc:javadoc ..."
mvn javadoc:javadoc >/dev/null 2>&1 || true
if [[ ! -e "$mvn_javadoc_output_dir/options" || ! -x "$mvn_javadoc_output_dir/javadoc.sh" ]]; then
	echo "'$mvn_javadoc_output_dir/options' or '$mvn_javadoc_output_dir/javadoc.sh' not found"
	exit 1
fi
echo "patching javadoc options ..."
# patch options to make it work
#   1. remove --module-source-path option (2 lines)
#   3. remove -d option (2 lines)
sed -i '' -E \
	-e '/^--module-source-path$/{N;d;}' \
	-e '/^-d$/{N;d;}' \
	"$mvn_javadoc_output_dir/options"
# add custom output (-d option)
echo "-d '$PWD/docs/javadoc/dist'" >>"$mvn_javadoc_output_dir/options"

# show patched options
# cat target/site/apidocs/options

# run javadoc via the generated wrapper script
echo "running $mvn_javadoc_output_dir/javadoc.sh"
(cd "$mvn_javadoc_output_dir" && ./javadoc.sh)
