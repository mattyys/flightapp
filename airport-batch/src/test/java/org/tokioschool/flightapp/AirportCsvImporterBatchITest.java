package org.tokioschool.flightapp;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.tokioschool.flightapp.batch.importer.AirportCsvImporterBatchConfiguration;
import org.tokioschool.flightapp.batch.importer.AirportCsvImporterBatchListener;
import org.tokioschool.flightapp.config.AirportBatchConfigurationProperties;
import org.tokioschool.flightapp.domain.AirportRaw;
import org.tokioschool.flightapp.domain.AirportRawId;
import org.tokioschool.flightapp.repository.AirportRawRepository;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBatchTest
@ContextConfiguration(
    classes = {
      AirportCsvImporterBatchConfiguration.class,
      AirportCsvImporterBatchListener.class,
      AirportCsvImporterBatchITest.Init.class
    })
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MYSQL;DATABASE_TO_LOWER=TRUE",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@EnableAutoConfiguration
@EntityScan(basePackages = {"org.tokioschool.flightapp.domain"})
@EnableJpaRepositories( basePackages= {"org.tokioschool.flightapp.repository"})
public class AirportCsvImporterBatchITest {

  @Autowired private Job importAirportCsvJob;
  @Autowired private JobLauncherTestUtils jobLauncherTestUtils;
  @Autowired private JobRepositoryTestUtils jobRepositoryTestUtils;
  @Autowired private AirportRawRepository airportRawRepository;


  @BeforeEach
  void beforeEach(){
    jobLauncherTestUtils.setJob(importAirportCsvJob);
    jobRepositoryTestUtils.removeJobExecutions();

  }

  @Test
  void givenNewJob_whenExecuted_thenOk() throws Exception{
    JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
    JobExecution jobExecution = jobLauncherTestUtils.launchJob();

    assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

    long counter = airportRawRepository.count();
    assertThat(counter).isEqualTo(49);

    List<AirportRaw> bcn = airportRawRepository.findAll(
            Example.of(
                    AirportRaw.builder()
                            .airportRawId(AirportRawId.builder().acronym("BCN").build()).build()
            ));

    Assertions.assertThat(bcn)
        .hasSize(1)
        .first()
        .returns(jobExecution.getJobId(), o -> o.getAirportRawId().getJobId())
        .returns("BCN", o -> o.getAirportRawId().getAcronym())
        .returns("Josep Tarradellas Barcelona-El Prat Airport", AirportRaw::getName)
        .returns("ES", AirportRaw::getCountry)
        .satisfies(
            o ->
                assertThat(o.getLat())
                    .usingComparator(BigDecimal::compareTo)
                    .isEqualTo(BigDecimal.valueOf(41.2971)))
        .satisfies(
            o ->
                assertThat(o.getLon())
                    .usingComparator(BigDecimal::compareTo)
                    .isEqualTo(BigDecimal.valueOf(2.07846)));
  }



  @TestConfiguration
  public static class Init {

    @Bean
    public AirportBatchConfigurationProperties airportBatchConfigurationProperties() {
      URL resource = this.getClass().getResource("/data");
      Path path = Paths.get(resource.getPath());
      return new AirportBatchConfigurationProperties(path);
    }
  }
}
