#!/bin/bash

# Startup script for Sports Tournament System

cd "$(dirname "$0")"

# Find the MySQL connector JAR
MYSQL_JAR=$(find lib -name "mysql-connector-j-*.jar" -print -quit)

if [ -z "$MYSQL_JAR" ]; then
    echo "Error: MySQL connector JAR not found in lib/"
    exit 1
fi

echo "Using MySQL JAR: $MYSQL_JAR"
echo "Starting Sports Management System..."
echo "=================================="

# Run the application
java -cp "$MYSQL_JAR:bin" ui.MainMenuUI

