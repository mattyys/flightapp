package org.tokioschool.flightapp.batch.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.tokioschool.flightapp.config.AirportBatchConfigurationProperties;
import org.tokioschool.flightapp.domain.AirportRaw;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AirportRawExportBatchConfiguration {

  private final AirportBatchConfigurationProperties airportBatchConfigurationProperties;
  private final EntityManagerFactory entityManagerFactory;
  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;

  @Bean
  public JpaPagingItemReader<AirportRaw> airportRawItemReader() {

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
  public JsonFileItemWriter<AirportRaw> airportRawItemWriter() {
    return new JsonFileItemWriterBuilder<AirportRaw>()
        .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>(new ObjectMapper()))
        .resource(new FileSystemResource(airportBatchConfigurationProperties.getExportPath()))
        .name("airportRawItemWriter")
        .build();
  }

  @Bean
  public Step exportAirportRawStep(ItemReader<AirportRaw> airportRawItemReader) {
    return new StepBuilder("export-airport-raw-step", jobRepository)
        .<AirportRaw, AirportRaw>chunk(10, transactionManager)
        .reader(airportRawItemReader)
        .writer(airportRawItemWriter())
        .build();
  }

  @Bean
  public Job exportAirportRawJob(Step exportAirportRawStep) {
    return new JobBuilder("export-airport-raw-job", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(exportAirportRawStep)
        .build();
  }
}
