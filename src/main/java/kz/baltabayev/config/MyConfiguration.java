package kz.baltabayev.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * This is a configuration class for the Spring MVC application.
 * It enables AspectJ auto proxying and component scanning in the "kz.baltabayev" package.
 * It also implements the WebApplicationInitializer interface to provide configuration for the application startup.
 */
@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan("kz.baltabayev")
public class MyConfiguration implements WebApplicationInitializer {

    /**
     * This method is called on application startup.
     * It sets up the application context, registers the configuration class, and adds a listener to the servlet context.
     * It also sets up the DispatcherServlet, sets it to load on startup, and maps it to the root URL ("/").
     *
     * @param servletContext the servlet context
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(MyConfiguration.class);

        servletContext.addListener(new ContextLoaderListener(context));

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
}