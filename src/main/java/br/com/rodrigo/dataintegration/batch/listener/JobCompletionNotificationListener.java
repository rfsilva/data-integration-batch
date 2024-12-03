package br.com.rodrigo.dataintegration.batch.listener;

import lombok.extern.slf4j.*;
import org.springframework.batch.core.*;
import org.springframework.stereotype.*;

@Component
@Slf4j
public class JobCompletionNotificationListener implements JobExecutionListener {

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("Completou!");
		}
	}
}
