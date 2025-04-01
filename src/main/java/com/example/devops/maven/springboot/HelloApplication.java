package com.example.devops.maven.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.apache.catalina.connector.Connector;

@SpringBootApplication
public class HelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }

    // Configuration for multiple ports and security
    @Configuration
    class ServerConfig {

        @Bean
        public WebServerFactoryCustomizer<TomcatServletWebServerFactory> serverCustomizer() {
            return factory -> {
                factory.addAdditionalTomcatConnectors(httpConnector8080()); // For TLS on 8080
                // Removed 8000 connector; actuator will use management.server.port
            };
        }

        // Connector for 8080 (TLS route, internally HTTP since edge-terminated by OpenShift)
        @Bean
        public Connector httpConnector8080() {
            Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
            connector.setScheme("http");
            connector.setPort(8080);
            return connector;
        }

        // Security configuration
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/mtls/page", "/tls/page", "/actuator/**").permitAll()
                    .anyRequest().authenticated() // Require auth for other endpoints if needed
                )
                .csrf().disable(); // Disable CSRF for simplicity
            return http.build();
        }
    }
}