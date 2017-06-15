FROM docker.io/tomcat:latest
ADD out/artifacts/Richkware_Manager_Server/Richkware-Manager-Server.war /usr/local/tomcat/webapps/
