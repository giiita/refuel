#!/bin/bash

echo "Shutting down json server..."

ps -ef | grep "json-server"

if test `ps -ef | grep "json-server" | wc -l` -ne 0; then
  ps -ef | grep "json-server" | grep -v "grep" | awk '{print $2}' | xargs kill
fi