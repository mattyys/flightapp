package org.tokioschool.flightapp.batch.importer;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.tokioschool.flightapp.config.AirportBatchConfigurationProperties;
import org.tokioschool.flightapp.csv.AirportCsv;
import org.tokioschool.flightapp.csv.AirportCsvMapper;
import org.tokioschool.flightapp.domain.AirportRaw;
import org.tokioschool.flightapp.domain.AirportRawId;

import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AirportCsvImporterBatchConfiguration {

  private final AirportBatchConfigurationProperties airportBatchConfigurationProperties;
  private final EntityManagerFactory entityManagerFactory;
  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;
  private final AirportCsvImporterBatchListener airportCsvImporterBatchListener;

  // reader que se encarga de leer el archivo csv
  @Bean
  public FlatFileItemReader<AirportCsv> airportCsvItemReader() {

    FlatFileItemReader<AirportCsv> flatFileItemReader = new FlatFileItemReader<>();

    flatFileItemReader.setLinesToSkip(1);
    flatFileItemReader.setEncoding(StandardCharsets.UTF_8.name());
    flatFileItemReader.setResource(
        new PathResource(airportBatchConfigurationProperties.getAirportsCsvPath()));

    DefaultLineMapper<AirportCsv> lineMapper = new DefaultLineMapper<>();
    DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

    lineTokenizer.setDelimiter(",");
    lineTokenizer.setNames(
        AirportCsvMapper
            .AIRPORT_CSV_FIELDS); // aca utilizamos un mapper el cual es una clase que se encarga de
    // mapear los campos del csv a un objeto java

    lineMapper.setFieldSetMapper(new AirportCsvMapper());
    lineMapper.setLineTokenizer(lineTokenizer);
    lineMapper.afterPropertiesSet();

    flatFileItemReader.setLineMapper(lineMapper);
    return flatFileItemReader;
  }

  @Bean
  @StepScope
  public ItemProcessor<AirportCsv, AirportRaw> airportCsvFilterProcessor(
      @Value("#{stepExecution.jobExecution.id}") Long jobId) {

    return new FunctionItemProcessor<>(
        airportCsv -> {
          if (airportCsv.getIataCode() == null) return null;

          if (airportCsv.getType() != AirportCsv.AirportType.LARGE_AIRPORT
              && airportCsv.getType() != AirportCsv.AirportType.MEDIUM_AIRPORT) return null;

          if (!airportCsv.getIsoCountry().equals("ES")) return null;

          return AirportRaw.builder()
              .airportRawId(
                  AirportRawId.builder().jobId(jobId).acronym(airportCsv.getIataCode()).build())
              .lat(airportCsv.getLatitudeDeg().setScale(8, RoundingMode.HALF_EVEN))
              .lon(airportCsv.getLongitudeDeg().setScale(8, RoundingMode.HALF_EVEN))
              .name(airportCsv.getName())
              .country(airportCsv.getIsoCountry())
              .build();
        });
  }

  @Bean
  public JpaItemWriter<AirportRaw> airportRawCsvItemWriter() {
    JpaItemWriter<AirportRaw> jpaItemWriter = new JpaItemWriter<>();
    jpaItemWriter.setEntityManagerFactory((entityManagerFactory));
    return jpaItemWriter;
  }

  @Bean
  public Step importAirportCsvStep(
          ItemReader<AirportCsv> airportCsvItemReader,
          ItemProcessor<AirportCsv, AirportRaw> airportCsvAirportRawItemProcessor){

    return new StepBuilder("import-airport-csv.step",jobRepository)
            .<AirportCsv,AirportRaw>chunk(10,transactionManager)
            .reader(airportCsvItemReader)
            .processor(airportCsvAirportRawItemProcessor)
            .writer(airportRawCsvItemWriter())
            .build();
  }

  @Bean
  public Job importAirportCsvJob(Step importAirportCsvStep){
    return new JobBuilder("import-airport-csv.job",jobRepository)
            .incrementer(new RunIdIncrementer())
            .listener(airportCsvImporterBatchListener)
            .start(importAirportCsvStep)
            .build();
  }
}
