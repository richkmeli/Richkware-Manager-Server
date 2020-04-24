package it.richkmeli.rms;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@ServletComponentScan("it.richkmeli.rms.web")
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        loadBeanFromConfigFile();
    }

    private static void loadBeanFromConfigFile() {
        // load bean from xml
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-bean.xml");
        BeanFactory factory = (BeanFactory) context;
        /*User user = (User) factory.getBean("user");
        System.out.println(user.getName());*/
    }


}
