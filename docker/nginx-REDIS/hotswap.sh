#!/usr/bin/env bash
  
PARAM=" $1"

cID=$(docker container ls -f "name=docker_database_1" -q)
IPaddress=$(docker exec $cID grep -oP 'server\K.*?(?=:)' /etc/nginx/nginx.conf)

if [ $IPaddress == $PARAM ]; then
     echo $PARAM is currently being used.
else
    docker exec $cID  sed -i  "s/${IPaddress}":"/${PARAM}":"/" /etc/nginx/nginx.conf
    docker exec $cID /usr/sbin/nginx -s reload
fi
