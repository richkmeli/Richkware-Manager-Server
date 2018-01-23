#!/usr/bin/env bash
scp out/artifacts/Richkware_Manager_Server/Richkware-Manager-Server.war pi@www2.richk.me:/var/lib/tomcat8/webapps/
#-r
#/run/user/1000/gvfs/sftp\:host\=www2.richk.me\,user\=pi/var/lib/tomcat8/webapps/