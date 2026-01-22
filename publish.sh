#!/bin/bash
# SafeRoute SDK Publication Script

echo "Starting SafeRoute SDK Publication to Maven Central..."

# 1. Ensure we are in the right directory
cd "$(dirname "$0")"

# 2. Run the Maven deployment
# We skip tests and use the provided settings.xml
# GPG will prompt you for your passphrase during this process.
mvn clean deploy -s settings.xml -DskipTests -Dmaven.test.skip=true

if [ $? -eq 0 ]; then
    echo "------------------------------------------------"
    echo "SUCCESS: SafeRoute SDK published to OSSRH!"
    echo "Next steps: Log in to https://s01.oss.sonatype.org/"
    echo "and close/release the staging repository."
    echo "------------------------------------------------"
else
    echo "------------------------------------------------"
    echo "ERROR: Publication failed. Check the logs above."
    echo "------------------------------------------------"
fi
