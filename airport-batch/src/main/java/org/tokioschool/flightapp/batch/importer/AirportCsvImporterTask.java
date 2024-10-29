package org.tokioschool.flightapp.batch.importer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AirportCsvImporterTask {

  private final JobLauncher jobLauncher;
  private final Job importAirportCsvJob;

  @Scheduled(cron = "30 * * ? * *")
  public void launchAirportCsvImporter() {

    JobParameters jobParameters =
        new JobParametersBuilder()
            .addString("jobExecutionId", UUID.randomUUID().toString())
            .toJobParameters();

    try {
      jobLauncher.run(importAirportCsvJob, jobParameters);
    } catch (JobExecutionAlreadyRunningException
        | JobRestartException
        | JobInstanceAlreadyCompleteException
        | JobParametersInvalidException e) {

      throw new RuntimeException(e);
    }
  }
}
