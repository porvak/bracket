#!/bin/bash

if [ -n "$WORKSPACE" ]; then
    coffee --lint  -o $WORKSPACE/src/main/webapp/resources/js/ -c $WORKSPACE/src/main/webapp/resources/coffee/
else
    coffee --lint  -o ../src/main/webapp/resources/js/ -c ../src/main/webapp/resources/coffee/
fi
