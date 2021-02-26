package it.richkmeli.rms.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
//@EnableWebSecurity
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                //.addResourceHandler("/resources/**")
                // example http://localhost:8080/**
                .addResourceHandler("/**")
                // example: from webapp/resources/
                .addResourceLocations("/resources/", "/other-resources/")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());

        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("/webjars/");

    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //registry.addViewController("/home").setViewName("TEMPLATE tipo home.html");
        //registry.addViewController("/").setViewName("home");
        //registry.addViewController("/home").setViewName("home");
        //registry.addViewController("/dashboard").setViewName("dashboard");
        //   registry.addViewController("/login").setViewName("login");
        //registry.addViewController("/").setViewName("forward:/html/index.html");

    }

    // default servlet (mapped to "/") serves the resources
    /*@Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }*/


    /*@Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver resolver
                = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/");
        resolver.setSuffix(".jsp");
        return resolver;
    }*/

}


