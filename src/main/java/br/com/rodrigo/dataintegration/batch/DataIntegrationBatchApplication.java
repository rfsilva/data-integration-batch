package br.com.rodrigo.dataintegration.batch;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;

@SpringBootApplication
public class DataIntegrationBatchApplication {

	public static void main(String[] args) {
		System.exit(SpringApplication.exit(SpringApplication.run(DataIntegrationBatchApplication.class, args)));
	}
}
