package ru.bookshop.api;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class SpringApplicationContextListener implements ServletContextListener {

    private AnnotationConfigApplicationContext applicationContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext("ru.bookshop.configuration");

        sce.getServletContext().setAttribute("applicationContext", applicationContext);
        this.applicationContext = applicationContext;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        applicationContext.close();
    }

}
