#!/bin/bash
sed -i "s/^    server.*/    server $1:6379;/" /etc/nginx/nginx.conf
/usr/sbin/nginx -s reload
