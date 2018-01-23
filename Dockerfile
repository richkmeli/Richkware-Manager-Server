#FROM mdelapenya/tomcat-mysql:7.0.77
#RUN rm -rf webapps/ROOT/*
#COPY out/artifacts/PTOexploded/* webapps/ROOT/

#FROM mysql:5.5
#FROM tomcat:7
MAINTAINER Richk <richkmeli@gmail.com>

CMD mysql -u root -p richk

CMD rm -rf /usr/local/tomcat/webapps/ROOT/*
COPY out/artifacts/Richkware-Manager-Server_E/* /usr/local/tomcat/webapps/ROOT/

#CMD service tomcat restart

EXPOSE 3306:3306
EXPOSE 8080:80
