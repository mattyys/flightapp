package org.tokioschool.flightapp.batch.exporter;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.tokioschool.flightapp.config.AirportBatchConfigurationProperties;
import org.tokioschool.flightapp.domain.AirportRaw;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AirportRawExportBatchConfiguration {

    private final AirportBatchConfigurationProperties airportBatchConfigurationProperties;
    private final EntityManagerFactory entityManagerFactory;


    @Bean
    public JpaPagingItemReader<AirportRaw> airportRawItemReader(){

        JpaPagingItemReader<AirportRaw> itemReader = new JpaPagingItemReader<>();

        itemReader.setEntityManagerFactory(entityManagerFactory);
        itemReader.setPageSize(10);
    itemReader.setQueryString(
        """
                SELECT a FROM AirportRaw a \
                WHERE a.airportRawId.jobId = (SELECT MAX(b.airportRawId.jobId) FROM AirportRaw b) \
                ORDER BY a.airportRawId.acronym ASC
                """);

    return itemReader;
    }

    @Bean
    public JsonFileItemWriter<AirportRaw> airportRawItemWriter(){
        return new JsonFileItemWriterBuilder<AirportRaw>()
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>(new ObjectMapper()))
                .resource(new FileSystemResource(airportBatchConfigurationProperties.getExportPath()))
                .name("airportRawItemWriter")
                .build();
    }

}
