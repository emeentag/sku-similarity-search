#!/bin/bash

# Ser ENV vars.
export LOG_LEVEL="all" \
       SERVER_PORT=8080 \
       FILE_PATH="/Users/ssimsek/projects/recommendation_service/test-data.json" \
       WEIGHTS_MAP="a:10,b:9,c:8,d:7,e:6,f:5,g:4,h:3,i:2,j:1" \
       NUMBER_OF_RESULTS=10

PROD="--prod"
JAR_PATH=build/libs/recommendation_service-all-1.0.jar

if [ \( "$1" = "$PROD" \) -a \( "$#" -ne 0 \) ]
then
  if [ -f "$JAR_PATH" ]
  then
    echo "Application jar file is exist."
  else
    echo "Application jar file is not exist."
    echo "Building application jar file."

    gradle test
    gradle createJar
  fi

  echo "Running application Jar file."

  java -jar "$JAR_PATH"
else
  echo "Running application in dev mode."
  gradle run
fi