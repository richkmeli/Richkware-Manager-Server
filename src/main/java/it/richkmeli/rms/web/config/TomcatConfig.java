package it.richkmeli.rms.web.config;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.context.annotation.Configuration;

@Configuration
//@Profile("dev")
public class TomcatConfig extends TomcatServletWebServerFactory{

    @Override
    protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
        // uncomment to enable JNDI
        // tomcat.enableNaming();
        return super.getTomcatWebServer(tomcat);
    }

    @Override
    protected void postProcessContext(Context context) {
        super.postProcessContext(context);

        //ContextResource resource = new ContextResource();
        //resource.setName("jdbc/DOCKER");
        //resource.setType(DataSource.class.getName());
        //resource.setProperty("driverClassName", "oracle.jdbc.OracleDriver");
        //resource.setProperty("url", "jdbc:mysql://db:3306/richkware");
        //resource.setProperty("username", "richk");
        //resource.setProperty("password", "richk");

        //context.getNamingResources().addResource(resource);
    }

}
