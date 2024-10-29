package org.tokioschool.flightapp.batch.exporter;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AirportRawExporterTask {

  private final JobLauncher jobLauncher;
  private final Job exportAirportRawJob;

  @Scheduled(cron = "0 * * ? * *")
  public void launchAirportRawExport() {
    JobParameters jobParameters =
        new JobParametersBuilder()
            .addString("jobExecutionId", UUID.randomUUID().toString())
            .toJobParameters();

    try {

      jobLauncher.run(exportAirportRawJob, jobParameters);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
