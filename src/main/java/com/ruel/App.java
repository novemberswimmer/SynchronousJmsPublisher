package com.ruel;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
       ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-app-context.xml");

        System.out.println( "Hello World!" );

        context.registerShutdownHook();
        context.close();
    }
}
