package org.example.mssqlplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@SpringBootApplication
public class MssqlplayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MssqlplayApplication.class, args);
    }

    @Autowired
    ReportService reportService;
    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try ( BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("Report.json").getInputStream()))) {
            Reports reports = objectMapper.readValue(reader, Reports.class);
            System.out.println(reports.reports.size());
            reportService.updateData(reports).block();
//            System.out.println(count);
        }
    }
    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("schema.sql"));
        initializer.setDatabasePopulator(populator);
        return initializer;
    }
}
