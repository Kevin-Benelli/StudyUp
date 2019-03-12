#!/usr/bin/env bash

PARAM=" $1"

IPaddress=$(grep -oP 'server\K.*?(?=:)' /etc/nginx/nginx.conf)

if [ $IPaddress == $PARAM ]; then
     echo $PARAM is currently being used.
else
     sed -i  "s/${IPaddress}":"/${PARAM}":"/" /etc/nginx/nginx.conf
     /usr/sbin/nginx -s reload
     echo Redirected to: $PARAM 
fi 
