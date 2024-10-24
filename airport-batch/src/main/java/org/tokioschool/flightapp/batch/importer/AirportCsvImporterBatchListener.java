package org.tokioschool.flightapp.batch.importer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class AirportCsvImporterBatchListener implements JobExecutionListener {

  @Override
  public void afterJob(JobExecution jobExecution) {
    log.info("Starting job:{}", jobExecution.getJobInstance().getJobName());
  }

  @Override
  public void beforeJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.FAILED) {
      log.error(
          "Ending job:{} status:{} error:{}",
          jobExecution.getJobInstance().getJobName(),
          jobExecution.getStatus(),
          jobExecution.getExitStatus().getExitDescription());
    } else {
      LocalDateTime startTime =
          Optional.ofNullable(jobExecution.getStartTime()).orElse(LocalDateTime.now());
      LocalDateTime endTime =
          Optional.ofNullable(jobExecution.getEndTime()).orElse(LocalDateTime.now());
      log.info(
          "Ending job:{} status:{} seconds:{}",
          jobExecution.getJobInstance().getJobName(),
          jobExecution.getStatus(),
          ChronoUnit.SECONDS.between(startTime, endTime));
    }
  }
}
