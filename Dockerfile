FROM tomcat:9-jdk17

LABEL maintainer="Richk <richkmeli@gmail.com>"

ENV CATALINA_HOME=/usr/local/tomcat
WORKDIR /usr/local/tomcat

# Remove default webapps to keep image clean
RUN rm -rf webapps/*

# Copy pre-built WAR (built on host) as ROOT.war so app is served at '/'
COPY target/Richkware-Manager-Server.war webapps/ROOT.war

# Add curl for healthcheck
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*

EXPOSE 8080

ENV DB_HOST=db \
    DB_PORT=5432 \
    DEBUG_MODE=false \
    SPRING_PROFILES_ACTIVE=docker

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

CMD ["catalina.sh", "run"]
