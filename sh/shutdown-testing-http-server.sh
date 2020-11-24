#!/bin/bash

echo "Shutting down json server..."

ps -ef | grep "server.js"

if test `ps -ef | grep "server.js" | wc -l` -ne 0; then
  echo "terminating server"
  ps -ef | grep "server.js" | awk '{print $2}' | xargs kill
fi
