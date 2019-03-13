#!/bin/bash
OldLine="$(grep "    server " etc/nginx/nginx.conf)"
NewLine="    server $1:6379;"
if [ "$NewLine" != "$OldLine" ]
then
	sed -i "s/^    server.*/$NewLine/" /etc/nginx/nginx.conf
	/usr/sbin/nginx -s reload
else
	echo "Warning: Server given is already in use."
fi

