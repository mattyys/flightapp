package org.tokioschool.flightapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "org.tokioschool.flightapp.repository")
public class MongoDbConfiguration {

    @Bean
    public UUIDDocumentListener uuidDocumentListener() {
        return new UUIDDocumentListener();
    }
}
