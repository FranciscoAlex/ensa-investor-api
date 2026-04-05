#!/bin/sh
# ============================================================
# Maven Wrapper — auto-downloads Maven 3.9.9 if not present
# ============================================================

MAVEN_VERSION=3.9.9
MAVEN_URL="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/${MAVEN_VERSION}/apache-maven-${MAVEN_VERSION}-bin.tar.gz"

SCRIPT_DIR="$( cd "$( dirname "$0" )" && pwd )"
WRAPPER_DIR="$SCRIPT_DIR/.mvn/wrapper"
MAVEN_HOME="$WRAPPER_DIR/apache-maven-${MAVEN_VERSION}"

# Download Maven if not already present
if [ ! -f "$MAVEN_HOME/bin/mvn" ]; then
    echo "[mvnw] Maven ${MAVEN_VERSION} not found. Downloading..."
    mkdir -p "$WRAPPER_DIR"
    ARCHIVE="$WRAPPER_DIR/apache-maven-${MAVEN_VERSION}-bin.tar.gz"
    if ! curl -sL "$MAVEN_URL" -o "$ARCHIVE"; then
        echo "[mvnw] ERROR: Failed to download Maven from $MAVEN_URL"
        exit 1
    fi
    tar -xzf "$ARCHIVE" -C "$WRAPPER_DIR"
    rm -f "$ARCHIVE"
    echo "[mvnw] Maven ${MAVEN_VERSION} installed at $MAVEN_HOME"
fi

# Set JAVA_HOME to OpenJDK if not already set
if [ -z "$JAVA_HOME" ]; then
    if [ -d "/opt/homebrew/opt/openjdk" ]; then
        export JAVA_HOME="/opt/homebrew/opt/openjdk"
    fi
fi

exec "$MAVEN_HOME/bin/mvn" "$@"
