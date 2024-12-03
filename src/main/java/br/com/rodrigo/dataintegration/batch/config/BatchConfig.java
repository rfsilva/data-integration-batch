package br.com.rodrigo.dataintegration.batch.config;

import br.com.rodrigo.dataintegration.batch.dto.*;
import br.com.rodrigo.dataintegration.batch.listener.*;
import br.com.rodrigo.dataintegration.batch.processor.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.*;
import org.springframework.batch.core.repository.*;
import org.springframework.batch.core.step.builder.*;
import org.springframework.batch.item.file.*;
import org.springframework.batch.item.file.builder.*;
import org.springframework.batch.item.file.transform.*;
import org.springframework.batch.item.json.*;
import org.springframework.batch.item.json.builder.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.core.io.*;
import org.springframework.transaction.*;

import java.time.*;
import java.time.format.*;

@Configuration
public class BatchConfig {

    @Value("${file.input-file}")
    private String inputFile;

    private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss");

    @Bean
    public JsonItemReader<OperationDTO> reader() {
        return new JsonItemReaderBuilder<OperationDTO>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(OperationDTO.class))
                .resource(new FileSystemResource(inputFile))
                .name("contextOperationItemReader")
                .build();
    }

    @Bean
    public FlatFileItemWriter<OperationDTO> writer() {
        return new FlatFileItemWriterBuilder<OperationDTO>()
                .resource(new FileSystemResource("C:/develop/data-integration/output/outputData-" + dtFormatter.format(LocalDateTime.now()) + ".csv"))
                .append(true)
                .name("contextOperationItemWriter")
                .lineAggregator(new DelimitedLineAggregator<>() {
                    {
                        setDelimiter(",");
                        setFieldExtractor(new BeanWrapperFieldExtractor<>() {
                            {
                                setNames(new String[] {"contextId", "productOperationId", "result.code", "result.correlationId"});
                            }
                        });
                    }
                })
                .build();
    }

    @Bean
    public Job dataIntegrationJob(JobRepository jobRepository, Step dataIntegrationStep, JobCompletionNotificationListener listener) {
        return new JobBuilder("dataIntegrationJob", jobRepository)
                .listener(listener)
                .start(dataIntegrationStep)
                .build();
    }

    @Bean
    public Step dataIntegrationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                    JsonItemReader<OperationDTO> reader, OperationItemProcessor processor, FlatFileItemWriter<OperationDTO> writer) {
        return new StepBuilder("dataIntegrationStep", jobRepository)
                .<OperationDTO, OperationDTO> chunk(3, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
