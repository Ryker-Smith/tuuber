#!/bin/bash
watch -n 1 "uptime ; echo ERRORS; tail  -n 25  /var/log/httpd/error_log | grep tuuber | sort -r ; echo ACCESSES; tail -n 25  /var/log/httpd/access_log | grep tuuber | sort -r ; "
