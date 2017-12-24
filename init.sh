#!/usr/bin/env bash

set -e          # exit if any command fails
set -u          # prevent using an undefined variable
set -o pipefail # force pipelines to fail on the first non-zero status

# In order for us to be able to run your solution in our environment, it would
# be preferrable if you use docker to launch your application

# docker build -t h24-test-task .
# docker run -d --rm -p 3000:80 h24-test-task <&0

# As our main language is golang you could also run your application with go,
# e.g.:

#  go run main.go 3000 <&0
