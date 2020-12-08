package it.richkmeli.rms.web.config;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
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

        // SSL - Redirect HTTP requests to HTTPS
//        SecurityConstraint securityConstraint = new SecurityConstraint();
//        securityConstraint.setUserConstraint("CONFIDENTIAL");
//        SecurityCollection collection = new SecurityCollection();
//        collection.addPattern("/*");
//        securityConstraint.addCollection(collection);
//        context.addConstraint(securityConstraint);

        // add JNDI
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
