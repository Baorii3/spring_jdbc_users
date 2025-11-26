package com.ra2.users.spring_jdbc_users.logging;

import org.springframework.stereotype.Component;

@Component
public class customLogging {
    private final String logDirectory = "logs/";

    public customLogging() {
    }

    public void error() {
        // Escriu un error en el fitxer 
        
    }

    public void info() {
        // Escriu info en el fitxer 

    }
}
