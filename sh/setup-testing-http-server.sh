#!/bin/bash

script=$(cd $(dirname $0); pwd)

install_nvm() {
  if ! test -e $HOME/.nvm; then
    mkdir -p $HOME/.nvm
  fi

  echo "NVM installing..."
  cd $HOME/.nvm
  curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.33.11/install.sh | bash

  export NVM_DIR="$HOME/.nvm"
  [ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"
  [ -s "$NVM_DIR/bash_completion" ] && \. "$NVM_DIR/bash_completion"

  echo "NVM: `nvm --version`"
}

install_npm() {
  echo "NPM INSTALLED STATE [ $npm_exist ] [ `which npm` ]"
  if test -z `which npm`; then
    echo "NPM installing..."
    nvm install stable
  fi

  echo "NPM: `npm --version`"
}

install_json_server() {
  if test `npm ls -g --depth=0 | grep json-server | wc -l` -eq 0; then
    cd $script
    echo "JSON-SERVER installing"
    npm install json-server
  fi
}

run_jsonserver() {
  echo "STARTING JSON SERVER"
  node ${script}/server.js > /dev/null &
  sleep 5
}

install_nvm
install_npm
install_json_server
run_jsonserver
