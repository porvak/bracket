#!/bin/bash

SCRIPT_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
source $SCRIPT_PATH/functions.sh

ANSIBLE_VERSION=1.5.3
installAnsible $ANSIBLE_VERSION

echo "Provision bracket application for development."
cd $SCRIPT_PATH
sudo ansible-playbook -i vagrant_local_dev developer.yml 
