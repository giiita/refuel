#!/bin/bash

if test `ps -ef | grep "json-server --watch" | wc -l` -ne 0; then
  ps -ef | grep "json-server --watch" | grep -v "grep" | awk '{print $2}' | xargs kill
fi