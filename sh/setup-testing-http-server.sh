#!/bin/bash

script=$(cd $(dirname $0); pwd)

install_nvm() {
  if ! test -s $HOME/.nvm; then
    echo "NVM installing..."
    curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.33.11/install.sh | bash
  fi

  export NVM_DIR="$HOME/.nvm"
  [ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"
  [ -s "$NVM_DIR/bash_completion" ] && \. "$NVM_DIR/bash_completion"

  echo "NVM: `nvm --version`"
}

install_npm() {
  npm_exist=`npm --version; echo $?`
  if test $npm_exist -ne 0; then
    echo "NPM installing..."
    nvm install stable
  fi

  echo "NPM: `npm --version`"
}

install_json_server() {
  if test `npm ls -g --depth=0 | grep json-server | wc -l` -eq 0; then
    echo "JSON-SERVER installing"
    npm install -g json-server
  fi
}

run_jsonserver() {
  json-server --watch ${script}/mock-response.json &
  sleep 2
}

install_nvm
install_npm
install_json_server
run_jsonserver