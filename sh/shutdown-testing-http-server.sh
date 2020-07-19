#!/bin/bash

echo "Shutting down json server..."

ps -ef | grep "server.js"

if test `ps -ef | grep "server.js" | wc -l` -ne 0; then
  ps -ef | grep "server.js" | grep -v "grep" | awk '{print $2}' | xargs kill
fi
